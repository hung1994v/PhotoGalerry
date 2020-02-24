package com.photo.gallery.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.photo.gallery.callback.OnDialogEventListener;
import com.photo.gallery.R;
import com.photo.gallery.adapters.MySection;
import com.photo.gallery.callback.OnFileDialogEventListener;
import com.photo.gallery.models.FileItem;
import com.photo.gallery.section_headergrid.SectionedRecyclerViewAdapter;
import com.photo.gallery.taskes.SelectAllTask;
import com.photo.gallery.utils.ConstValue;
import com.photo.gallery.utils.DbUtils;
import com.photo.gallery.utils.FileUtil;
import com.photo.gallery.utils.Flog;
import com.photo.gallery.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hoavt on 3/15/2018.
 */

public class VideoFragment extends BaseFragment implements MySection.OnMySetionListener, SelectAllTask.OnSelectAllTaskListener {

    private static final java.lang.String TAG = VideoFragment.class.getSimpleName();
    private ArrayList<FileItem> mListAllVideos = null;
    private OnVideoListener listener = null;
    private RecyclerView mRecyclerView = null;
    private Map<String, ArrayList<FileItem>> mListAllVideoSections = null;
    private SectionedRecyclerViewAdapter sectionAdapter = null;
    private HashMap<Integer, FileItem> listPositionChanged = new HashMap<>();
    private boolean isLongClickedEvent = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_gridview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initDialog();
        initViews();
        if (listener != null) {
            listener.onVideoFragmentReady();
        }
    }

    private void initViews() {

        View viewParent = getView();
        if (viewParent==null) {
            return;
        }
        viewParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // DO NOTHING.
            }
        });
        mCoordinatorLayout = (CoordinatorLayout) viewParent.findViewById(R.id.coordinator);
        mRecyclerView = (RecyclerView) viewParent.findViewById(R.id.recycler_view);
    }

    public void initialize(ArrayList<FileItem> listAllVideos, Map<String, ArrayList<FileItem>> listAllVideoSections) {
        mListAllVideos = listAllVideos;
        mListAllVideoSections = listAllVideoSections;
        if (mListAllVideos == null || mListAllVideoSections == null) {
            return;
        }

        Flog.d(TAG, "list video size=" + mListAllVideos.size());

        setValues();
    }

    public void setValues() {

        // Create an instance of SectionedRecyclerViewAdapter
        sectionAdapter = new SectionedRecyclerViewAdapter();

//        GalleryUtil.logListFolder(mListAllVideoSections);

        // Add your Sections
        int index = 0;
        for (Map.Entry<String, ArrayList<FileItem>> entry : mListAllVideoSections.entrySet()) {
            String key = entry.getKey();
            ArrayList<FileItem> items = mListAllVideoSections.get(key);

            MySection section = new MySection(index, mContext, key, items,false).setListener(this);
            sectionAdapter.addSection(section);

            index++;
        }

        // Set up your RecyclerView with the SectionedRecyclerViewAdapter
        GridLayoutManager glm = new GridLayoutManager(mContext, ConstValue.NUM_OF_COLS_GRIDVIEW);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (sectionAdapter.getSectionItemViewType(position)) {
                    case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:
                        return ConstValue.NUM_OF_COLS_GRIDVIEW;
                    default:
                        return 1;
                }
            }
        });
        mRecyclerView.setLayoutManager(glm);
        mRecyclerView.setAdapter(sectionAdapter);
    }

    public VideoFragment setListener(OnVideoListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public void onItemInSetionLongClicked(int index, int position, FileItem file) {
        Flog.d(TAG, "22 onItemInSetionLONGClicked=" + file);

        if (listener != null) {
            if (!isLongClickedEvent) {
                int positionInAdapter = getPositionInAdapter(index, position);
                listPositionChanged.put(Integer.valueOf(positionInAdapter), file);
                Flog.d(TAG, "22 isLongClickedEvent:p1=" + index + "_p2=" + position + "_clicked=" + positionInAdapter);
                sectionAdapter.notifyItemChanged(positionInAdapter);

                listener.onItemInVideoLongClicked(file);
                setLongClickedEvent(true);
            } else {
                handleItemVideoClicked(index, position, file);
            }
        }
    }

    private void handleItemVideoClicked(int index, int position, FileItem file) {
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
        listener.onItemInVideoClicked(file, listPositionChanged.size());
    }

    private int getPositionInAdapter(int index, int position) {
        int positionInAdapter = 0;
        int cnt = 0;
        // Add your Sections
        for (Map.Entry<String, ArrayList<FileItem>> entry : mListAllVideoSections.entrySet()) {
            if (index <= cnt) {
                break;
            }

            String key = entry.getKey();
            ArrayList<FileItem> items = mListAllVideoSections.get(key);
            positionInAdapter += (1 + items.size());

            cnt++;
        }

        positionInAdapter += (1 + position);
        return positionInAdapter;
    }

    @Override
    public void onItemInSetionClicked(int index, int position, FileItem file) {
        Flog.d(TAG, "22 onItemInSetionClicked=" + file.name);
        if (listener != null) {
            if (isLongClickedEvent) {
                handleItemVideoClicked(index, position, file);
            } else {
                file.isSelected = false;
                openVideoViewer(file);
            }
        }
    }

    private void openVideoViewer(FileItem file) {
        listener.openVideoViewer(file);
    }

    public void setLongClickedEvent(boolean longClickedEvent) {
        isLongClickedEvent = longClickedEvent;
    }

    @Override
    public void onSelectAllLoading(int positionInAdapter) {
        int percent = positionInAdapter*100/(mListAllVideos.size()+mListAllVideoSections.size());
        progressDialog.setMessage(mContext.getString(R.string.selecting) +" "+percent + "%");
        sectionAdapter.notifyItemChanged(positionInAdapter);
    }

    @Override
    public void onSelectAllFinished(HashMap<Integer, FileItem> listPositionChanged) {
        this.listPositionChanged = listPositionChanged;
        Flog.d(TAG, "22 addAllPosChanged="+listPositionChanged.size());
        hideDialog();
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
            Flog.d(TAG, "changedIdx is "+changedIdx);
            sectionAdapter.notifyItemChanged(changedIdx);
        }

        listPositionChanged.clear();
    }

    public void updateUI(ArrayList<FileItem> listAllVideos, Map<String, ArrayList<FileItem>> listAllVideoSections) {

        if (mRecyclerView==null || sectionAdapter == null) {
            Flog.d(TAG, "2recycler not init");
            return;
        }

        mListAllVideos = listAllVideos;
        mListAllVideoSections = listAllVideoSections;
        if (mListAllVideos == null || mListAllVideoSections == null) {
            return;
        }

        Flog.d(TAG, "32list video size=" + mListAllVideos.size());

        sectionAdapter.removeAllSections();
        // Add your Sections
        int index = 0;
        for (Map.Entry<String, ArrayList<FileItem>> entry : mListAllVideoSections.entrySet()) {
            String key = entry.getKey();
            ArrayList<FileItem> items = mListAllVideoSections.get(key);

            MySection section = new MySection(index, mContext, key, items,false).setListener(this);
            sectionAdapter.addSection(section);

            index++;
        }

        sectionAdapter.notifyDataSetChanged();
    }

    public HashMap<Integer, FileItem> getListPositionChanged() {
        return listPositionChanged;
    }

    public boolean shareFilesSelected() {
        if (listPositionChanged==null||listPositionChanged.size()<=0) {
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
            uries[idx] = FileUtil.getUrifromFile(mContext,fileItem.path);
            idx++;
        }

        Flog.d(TAG, "share changedIdxes is " + uries.length);
        FileUtil.share(mContext, uries);
    }

    public interface OnVideoListener {
        void onVideoFragmentReady();

        void onItemInVideoLongClicked(FileItem file);

        void onItemInVideoClicked(FileItem file, int numOfSelected);

        void openVideoViewer(FileItem file);
    }

    public void selectAll() {

        MySection.allSelected = true;
        MySection.unAllSelected = false;
//        sectionAdapter.notifyDataSetChanged();

        showDialog();
        SelectAllTask task = new SelectAllTask(mContext, mListAllVideoSections).setListener(this);
        task.execute();
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
        for (Map.Entry<Integer, FileItem> entry: listPositionChanged.entrySet()) {
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
        for (Map.Entry<Integer, FileItem> entry: listPositionChanged.entrySet()) {
            int key = entry.getKey();
            FileItem item = entry.getValue();
            items[idx] = item;
            idx++;
        }
        showDeleteDialog(mContext, listener, items);
    }

    private void showDeleteDialog(final Context context, final OnFileDialogEventListener listener, final FileItem... items) {

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
                            arrItems[i] =list.get(i);
                        }
                        if (listener != null) {
                            listener.onOk(arrItems);
                        }
                    }
                }).show();
    }

    private void showDeleteDialog(final Context context, final OnDialogEventListener listener, final FileItem... fileItem) {

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

    public void printList(HashMap<Integer, FileItem> map) {
        for (Map.Entry<Integer, FileItem> entry : map.entrySet()) {
            int index = entry.getKey();
            Flog.d(TAG, "abcfile map "+index+": "+index+"_file: "+entry.getValue());
        }
    }

    private CoordinatorLayout mCoordinatorLayout = null;

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

    private ProgressDialog progressDialog = null;
}
