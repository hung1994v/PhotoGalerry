package com.photo.gallery.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.photo.gallery.activities.HomeFragment;
import com.photo.gallery.callback.OnDialogEventListener;
import com.photo.gallery.R;
import com.photo.gallery.adapters.MySection;
import com.photo.gallery.callback.OnFileDialogEventListener;
import com.photo.gallery.databinding.LayoutEditFragmentBinding;
import com.photo.gallery.databinding.LayoutGridviewPhotoBinding;
import com.photo.gallery.models.FileItem;
import com.photo.gallery.section_headergrid.SectionedRecyclerViewAdapter;
import com.photo.gallery.taskes.SelectAllTask;
import com.photo.gallery.utils.ConstValue;
import com.photo.gallery.utils.DbUtils;
import com.photo.gallery.utils.FileUtil;
import com.photo.gallery.utils.Flog;
import com.photo.gallery.utils.GalleryUtil;
import com.photo.gallery.utils.SharedPrefUtil;
import com.photo.gallery.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.photo.gallery.utils.ConstValue.EXTRA_LIST_ALL_FILES;
import static com.photo.gallery.utils.ConstValue.EXTRA_LIST_ALL_MAP;
import static com.photo.gallery.utils.ConstValue.NUM_OF_COLS_DAY_GRIDVIEW;


/**
 * Created by Hoavt on 3/15/2018.
 */

public class PhotoFragment extends BaseFragment implements MySection.OnMySetionListener, SelectAllTask.OnSelectAllTaskListener {


    private static final java.lang.String TAG = PhotoFragment.class.getSimpleName();
    private RecyclerView mRecyclerView = null;
    private FloatingActionButton mFloatingActionButton = null;
    private ArrayList<FileItem> mListAllImgs = new ArrayList<>();
    private Map<String, ArrayList<FileItem>> mListAllImgSections = new HashMap<>();
    private Map<String, ArrayList<FileItem>> mListAllsearch = new HashMap<>();
    private OnPhotoListener listener = null;
    private SectionedRecyclerViewAdapter sectionAdapter = null;
    private boolean isLongClickedEvent = false;
    private HashMap<Integer, FileItem> listPositionChanged = new HashMap<>();
    private CoordinatorLayout mCoordinatorLayout = null;
    private ProgressDialog progressDialog = null;
    private LayoutGridviewPhotoBinding binding;
    public static final int DAY_VIEW = 0;
    public static  final int MONTH_VIEW = 1;

    public static PhotoFragment newInstance(Bundle bundle) {
        PhotoFragment fragment = new PhotoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_gridview_photo, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getData();
        initDialog();
        initViews();
        setValues();
    }

    private void getData() {
        if(getArguments()!=null){
            this.mListAllImgs = getArguments().getParcelable(EXTRA_LIST_ALL_FILES);
            this.mListAllImgSections = (Map<String, ArrayList<FileItem>>) getArguments().getSerializable(EXTRA_LIST_ALL_MAP);
        }
    }


    private void setValues() {

        // Create an instance of SectionedRecyclerViewAdapter
        sectionAdapter = new SectionedRecyclerViewAdapter();

//        GalleryUtil.logListFolder(mListAllImgSections);

        int index = 0;
        // Add your Sections
        for (Map.Entry<String, ArrayList<FileItem>> entry : mListAllImgSections.entrySet()) {
            String key = entry.getKey();
            ArrayList<FileItem> items = mListAllImgSections.get(key);

            MySection section = new MySection(index, mContext, key, items, false).setListener(this);
            sectionAdapter.addSection(section);

            index++;
        }

        // Set up your RecyclerView with the SectionedRecyclerViewAdapter

        mRecyclerView.setLayoutManager(getLayoutManager());
        mRecyclerView.setAdapter(sectionAdapter);
    }

    private GridLayoutManager getLayoutManager() {
            GridLayoutManager glm = new GridLayoutManager(mContext, SharedPrefUtil.getInstance().getInt(ConstValue.COLUM_GIRD_NUMBER,NUM_OF_COLS_DAY_GRIDVIEW));
            glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    switch (sectionAdapter.getSectionItemViewType(position)) {
                        case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:
                            return SharedPrefUtil.getInstance().getInt(ConstValue.COLUM_GIRD_NUMBER,NUM_OF_COLS_DAY_GRIDVIEW);
                        default:
                            return 1;
                    }
                }
            });
            return glm;
    }

    private void initViews() {

        View viewParent = getView();
        if (viewParent == null) {
            return;
        }
        viewParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // DO NOTHING.
            }
        });
        mRecyclerView = (RecyclerView) viewParent.findViewById(R.id.recycler_view);
        mCoordinatorLayout = (CoordinatorLayout) viewParent.findViewById(R.id.coordinator);
    }

    public PhotoFragment setListener(OnPhotoListener listener) {
        this.listener = listener;
        return this;
    }

    private int getPositionInAdapter(int index, int position) {
        int positionInAdapter = 0;
        int cnt = 0;
        // Add your Sections
        for (Map.Entry<String, ArrayList<FileItem>> entry : mListAllImgSections.entrySet()) {
            if (index <= cnt) {
                break;
            }

            String key = entry.getKey();
            ArrayList<FileItem> items = mListAllImgSections.get(key);
            positionInAdapter += (1 + items.size());

            cnt++;
        }

        positionInAdapter += (1 + position);
        return positionInAdapter;
    }

    private void handleItemPhotoClicked(int index, int position, FileItem file) {
        int positionInAdapter = getPositionInAdapter(index, position);
        if (file.isSelected) {
            listPositionChanged.put(Integer.valueOf(positionInAdapter), file);
        } else {
            Integer obj = Integer.valueOf(positionInAdapter);
            if (listPositionChanged.containsKey(obj)) {
                listPositionChanged.remove(obj);
            }
        }
        sectionAdapter.notifyItemChanged(positionInAdapter);

        if (listPositionChanged.size() <= 0) {
            MySection.unAllSelected = true;
        }
        listener.onItemInPhotoClicked(file, listPositionChanged.size());
    }

    public void setLongClickedEvent(boolean longClickedEvent) {
        isLongClickedEvent = longClickedEvent;
    }

    public boolean getLongClickEvent(){
        return isLongClickedEvent;
    }


    @Override
    public void onItemInSetionLongClicked(int index, int position, FileItem file) {
        Flog.d(TAG, "onItemInSetionLONGClicked=" + file);

        if (listener != null) {
            sectionAdapter.notifyDataSetChanged();
            if (!isLongClickedEvent) {
                int positionInAdapter = getPositionInAdapter(index, position);
                listPositionChanged.put(Integer.valueOf(positionInAdapter), file);
                Flog.d(TAG, "isLongClickedEvent:p1=" + index + "_p2=" + position + "_clicked=" + positionInAdapter);
                sectionAdapter.notifyItemChanged(positionInAdapter);

                listener.onItemInPhotoLongClicked(file);
                setLongClickedEvent(true);
            } else {
                handleItemPhotoClicked(index, position, file);
            }
        }
    }

    public void printList(HashMap<Integer, FileItem> map) {
        for (Map.Entry<Integer, FileItem> entry : map.entrySet()) {
            int index = entry.getKey();
            Flog.d(TAG, "file map " + index + ": " + index + "_file: " + entry.getValue());
        }
    }

    @Override
    public void onItemInSetionClicked(int index, int position, FileItem file) {
        Flog.d(TAG, "onItemInSetionClicked=" + file.name);
        if (listener != null) {
            if (isLongClickedEvent) {
                handleItemPhotoClicked(index, position, file);
            } else {
                file.isSelected = false;
                openPhotoViewer(file);
            }
        }
    }

    private void openPhotoViewer(FileItem file) {
        listener.openPhotoViewer(file);
    }

    public void selectAll() {

        MySection.allSelected = true;
        MySection.unAllSelected = false;
//        sectionAdapter.notifyDataSetChanged();

        if (false) {
            Flog.d(TAG, "before load: " + System.currentTimeMillis());
            addAllPosChanged();
            Flog.d(TAG, "after load: " + System.currentTimeMillis());
        } else {
            showDialog();
            SelectAllTask task = new SelectAllTask(mContext, mListAllImgSections).setListener(this);
            task.execute();
        }
    }

    private void addAllPosChanged() {
        int index = 0;
        // Add your Sections
        for (Map.Entry<String, ArrayList<FileItem>> entry : mListAllImgSections.entrySet()) {
            String key = entry.getKey();
            ArrayList<FileItem> items = mListAllImgSections.get(key);

            for (int position = 0; position < items.size(); position++) {
                FileItem file = items.get(position);
                int positionInAdapter = getPositionInAdapter(index, position);
                listPositionChanged.put(positionInAdapter, file);
                sectionAdapter.notifyItemChanged(positionInAdapter);
            }

            index++;
        }
        Flog.d(TAG, "addAllPosChanged=" + listPositionChanged.size());
    }

    public void unselectAll() {

        MySection.unAllSelected = true;
        MySection.allSelected = false;

//        for (int i = 0; i < listPositionChanged.size(); i++) {
//            int changedIdx = listPositionChanged.get(i);
//            sectionAdapter.notifyItemChanged(changedIdx);
//        }

        for (Map.Entry<Integer, FileItem> entry : listPositionChanged.entrySet()) {
            int changedIdx = entry.getKey();
            Flog.d(TAG, "changedIdx is " + changedIdx);
            sectionAdapter.notifyItemChanged(changedIdx);
        }

        listPositionChanged.clear();
    }

    public void deleteFilesSelected(final OnDialogEventListener listener) {

        if (listPositionChanged.size() <= 0) {
            Utils.aleartRequestSelect(mCoordinatorLayout, mContext.getString(R.string.delete));
            return;
        }

//        printList(listPositionChanged);
        int len = listPositionChanged.size();
        FileItem[] items = new FileItem[len];
        int idx = 0;
        for (Map.Entry<Integer, FileItem> entry : listPositionChanged.entrySet()) {
            int key = entry.getKey();
            FileItem item = entry.getValue();
            items[idx] = item;
            idx++;
        }
        showDeleteDialog(mContext, listener, items);
    }

    public void deleteFilesSelected(final OnFileDialogEventListener listener) {

        if (listPositionChanged.size() <= 0) {
            Utils.aleartRequestSelect(mCoordinatorLayout, mContext.getString(R.string.delete));
            return;
        }

//        printList(listPositionChanged);
        int len = listPositionChanged.size();
        FileItem[] items = new FileItem[len];
        int idx = 0;
        for (Map.Entry<Integer, FileItem> entry : listPositionChanged.entrySet()) {
            int key = entry.getKey();
            FileItem item = entry.getValue();
            items[idx] = item;
            idx++;
        }
        showDeleteDialog(mContext, listener, items);
    }

    private void showDeleteDialog(final Context context,
                                  final OnFileDialogEventListener listener, final FileItem... items) {

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        String title = context.getString(R.string.delete);
        String message = context.getString(R.string.confirm_delete_dialog);
        alert.setCancelable(false)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int len = items.length;
                        Uri[] uries = new Uri[len];
                        String[] paths = new String[len];

                        ArrayList<FileItem> list = new ArrayList<>();
                        String mes = "";
                        for (int i = 0; i < len; i++) {
                            FileItem item = items[i];
                            paths[i] = item.path;
                            uries[i] = Uri.fromFile(new File(item.path));

                            boolean success = FileUtil.delete(context, uries[i]);
                            if (success) {
                                list.add(item);
                                DbUtils.deleteFavourite(item);
                            } else {
                                mes += paths[i] + "\n";
                            }
                        }

                        if (mes.equals("")) {
                            FileUtil.toastSuccess(context, context.getString(R.string.delete));
                            FileUtil.scanMediaStore(context, paths);
                        } else {
                            FileUtil.toastFailed(context, context.getString(R.string.delete) + ":\n" + mes);
                        }

                        FileItem[] arrItems = new FileItem[list.size()];
                        for (int i = 0; i < list.size(); i++) {
                            arrItems[i] = list.get(i);
                        }
                        if (listener != null) {
                            listener.onOk(arrItems);
                        }
                    }
                }).show();
    }

    private void showDeleteDialog(final Context context, final OnDialogEventListener listener,
                                  final FileItem... fileItem) {

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        String title = context.getString(R.string.delete);
        String message = context.getString(R.string.confirm_delete_dialog);
        alert.setCancelable(false)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (listener != null) {
                            listener.onCancel();
                        }
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int len = fileItem.length;
                        Uri[] uries = new Uri[len];
                        String[] paths = new String[len];

                        String mes = "";
                        for (int i = 0; i < len; i++) {
                            FileItem item = fileItem[i];
                            paths[i] = item.path;
                            uries[i] = Uri.fromFile(new File(item.path));

                            boolean success = FileUtil.delete(context, uries[i]);
                            if (success) {
                                DbUtils.deleteFavourite(item);
                            } else {
                                mes += paths[i] + "\n";
                            }
                        }

                        if (mes.equals("")) {
                            FileUtil.toastSuccess(context, context.getString(R.string.delete));
                            FileUtil.scanMediaStore(context, paths);
                        } else {
                            FileUtil.toastFailed(context, context.getString(R.string.delete) + ":\n" + mes);
                        }
                        if (listener != null) {
                            listener.onOk();
                        }
                    }
                }).show();
    }

    @Override
    public void onSelectAllLoading(int positionInAdapter) {
        int percent = positionInAdapter * 100 / (mListAllImgs.size() + mListAllImgSections.size());
        progressDialog.setMessage(mContext.getString(R.string.selecting) + " " + percent + "%");
        sectionAdapter.notifyItemChanged(positionInAdapter);
    }

    @Override
    public void onSelectAllFinished(HashMap<Integer, FileItem> listPositionChanged) {
        this.listPositionChanged = listPositionChanged;
        Flog.d(TAG, "12 addAllPosChanged=" + listPositionChanged.size());
        hideDialog();
    }

    public void updateUI
            (ArrayList<FileItem> listAllImgs, Map<String, ArrayList<FileItem>> listAllImgSections) {

        if (mRecyclerView == null || sectionAdapter == null) {
//            setValues();
            Flog.d("FFFFFFFFF", "1recycler not init");
            return;
        }


        mListAllImgs = listAllImgs;
        mListAllImgSections = listAllImgSections;
        if (mListAllImgs == null || mListAllImgSections == null) {
            return;
        }
        mRecyclerView.setLayoutManager(getLayoutManager());

        sectionAdapter.removeAllSections();
        Flog.d(TAG, "12list photo size=" + mListAllImgs.size() + "_" + mListAllImgSections.size());
        int index = 0;
        // Add your Sections
        for (Map.Entry<String, ArrayList<FileItem>> entry : mListAllImgSections.entrySet()) {
            String key = entry.getKey();
            ArrayList<FileItem> items = mListAllImgSections.get(key);

            MySection section = new MySection(index, mContext, key, items, false).setListener(this);
            sectionAdapter.addSection(section);

            index++;
        }
        sectionAdapter.notifyDataSetChanged();
    }

    public boolean shareFilesSelected() {
        if (listPositionChanged == null || listPositionChanged.size() <= 0) {
            return false;
        }
        shareFilesByPos(listPositionChanged);
        return true;
    }

    private void shareFilesByPos(HashMap<Integer, FileItem> listPosChanged) {
        Uri uries[] = new Uri[listPosChanged.size()];

        int idx = 0;
        for (Map.Entry<Integer, FileItem> entry : listPosChanged.entrySet()) {
            int changedIdx = entry.getKey();
            FileItem fileItem = entry.getValue();
            Flog.d(TAG, "share changedIdx is " + changedIdx + "_" + fileItem.path);
            uries[idx] = FileUtil.getUrifromFile(mContext, fileItem.path);
            idx++;
        }

        Flog.d(TAG, "share changedIdxes is " + uries.length);
        FileUtil.share(mContext, uries);
    }

    public HashMap<Integer, FileItem> getListPositionChanged() {
        return listPositionChanged;
    }


    public void onBackPressed() {
        unselectAll();
    }

    public void search(String newText, boolean isDaySection) {
        ArrayList<FileItem> listFileFiltered = GalleryUtil.filterByNameFile(mListAllImgs, newText);
        if (isDaySection) {
            updateMap(GalleryUtil.groupListSectionByDate(listFileFiltered));
        } else {
            updateMap(GalleryUtil.groupListSectionByMonth(listFileFiltered));
        }

        sectionAdapter.removeAllSections();
        Flog.d(TAG, "12list photo size=" + mListAllImgs.size() + "_" + mListAllImgSections.size());
        int index = 0;
        // Add your Sections
        for (Map.Entry<String, ArrayList<FileItem>> entry : mListAllImgSections.entrySet()) {
            String key = entry.getKey();
            ArrayList<FileItem> items = mListAllImgSections.get(key);

            MySection section = new MySection(index, mContext, key, items, false).setListener(this);
            sectionAdapter.addSection(section);

            index++;
        }
        sectionAdapter.notifyDataSetChanged();
    }

    public void updateMap(Map<String, ArrayList<FileItem>> listAllImgSections) {
        mListAllImgSections = listAllImgSections;
        if (mListAllImgSections == null) {
            return;
        }

        Flog.d(TAG, "update search photo size=" + mListAllImgSections.size());

        sectionAdapter.removeAllSections();

        int cnt = 0;
        for (Map.Entry<String, ArrayList<FileItem>> entry : mListAllImgSections.entrySet()) {
            String key = entry.getKey();
            ArrayList<FileItem> items = mListAllImgSections.get(key);

            MySection section = new MySection(cnt, mContext, key, items, false).setListener(this);
            sectionAdapter.addSection(section);
            cnt++;
        }
        Flog.d(TAG, "update total sections=" + sectionAdapter.getItemCount() + "_vs_" + cnt);

        sectionAdapter.notifyDataSetChanged();
    }

    public static ArrayList<FileItem> filterByNameFile
            (ArrayList<FileItem> listAllFiles, String textInput) {
        ArrayList<FileItem> listFiltered = new ArrayList<>();
        if (textInput == null || textInput.equals("")) {
            return listFiltered;
        }

        for (int i = 0; i < listAllFiles.size(); i++) {
            FileItem file = listAllFiles.get(i);
            Flog.d(TAG, "file.name at " + i + "=" + file.name + "_textinput=" + textInput);
            if (file.name != null && file.name.toLowerCase().contains(textInput.toLowerCase().trim())) {
                listFiltered.add(file);
            }
        }

        return listFiltered;
    }

    public interface OnPhotoListener {


        void onItemInPhotoLongClicked(FileItem file);

        void onItemInPhotoClicked(FileItem file, int numOfSelected);

        void openPhotoViewer(FileItem file);
    }

    private void initDialog() {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    return true; // Consumed
                }
                return false;
            }
        });
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void showDialog() {
        if (progressDialog != null) {
            progressDialog.show();
        }
    }

    private void hideDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
