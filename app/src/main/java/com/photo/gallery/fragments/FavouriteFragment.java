package com.photo.gallery.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.photo.gallery.callback.OnDialogEventListener;
import com.photo.gallery.R;
import com.photo.gallery.adapters.FileAlbumAdapter;
import com.photo.gallery.callback.OnFileDialogEventListener;
import com.photo.gallery.databinding.LayoutGridviewFavoriteBinding;
import com.photo.gallery.models.FileItem;
import com.photo.gallery.utils.ConstValue;
import com.photo.gallery.utils.DbUtils;
import com.photo.gallery.utils.FileUtil;
import com.photo.gallery.utils.Flog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Hoavt on 3/15/2018.
 */

public class FavouriteFragment extends BaseFragment implements FileAlbumAdapter.OnAlbumsAdapterListener {

    private static final String TAG = FavouriteFragment.class.getSimpleName();
    private RecyclerView mRecyclerView = null;
    private ArrayList<FileItem> mList = null;
    private OnFavouriteListener listener = null;
    private ProgressDialog progressDialog = null;
    private FileAlbumAdapter mAdapter = null;
    private View mNoFileFound = null;
    private boolean isLongClickedEvent = false;
    private HashMap<Integer, FileItem> listPositionChanged = new HashMap<>();
    private LayoutGridviewFavoriteBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_gridview_favorite,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDialog();
        initViews();
        setValues();

    }

    private void setValues() {
        initList();

        showNoFileFound(mList.size() <= 0);

        mAdapter = new FileAlbumAdapter(mContext, mList).setListener(this);

        // Set up your RecyclerView with the SectionedRecyclerViewAdapter
        GridLayoutManager glm = new GridLayoutManager(mContext, ConstValue.NUM_OF_COLS_GRIDVIEW);
        mRecyclerView.setLayoutManager(glm);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initList() {
        if (mList == null) {
            mList = new ArrayList<>();
        }
        mList.clear();
        mList = DbUtils.parseFavourites();
        Flog.d(TAG, "size of favourites1=" + mList.size());
    }

    public void refreshList() {
        showDialog();

        mList = DbUtils.parseFavourites();
        showNoFileFound(mList.size() <= 0);
        Flog.d(TAG, "size of favourites2=" + mList.size());
        mAdapter = new FileAlbumAdapter(mContext, mList).setListener(this);
        mRecyclerView.setAdapter(mAdapter);

        hideDialog();
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
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });


        mRecyclerView = (RecyclerView) viewParent.findViewById(R.id.recycler_view);
        mNoFileFound = viewParent.findViewById(R.id.tv_no_file_found);
        
    }

    private void showNoFileFound(boolean shown) {
        mNoFileFound.setVisibility(shown ? View.VISIBLE : View.GONE);
    }

    public FavouriteFragment setListener(OnFavouriteListener listener) {
        this.listener = listener;
        return this;
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

    @Override
    public void onItemInAlbumLongClicked(int position, FileItem file) {

//        Flog.d(TAG, "onItemInAlbumLONGClicked=" + file);
//
//        if (!isLongClickedEvent) {
//            int positionInAdapter = position;
//            listPositionChanged.put(Integer.valueOf(positionInAdapter), file);
//            Flog.d(TAG, "12 isLongClickedEvent:p=" + position + "_clicked=" + positionInAdapter);
//            mAdapter.notifyItemChanged(positionInAdapter);
//
//            listener.onItemInFavouriteLongClicked(file);
//            setLongClickedEvent(true);
//        } else {
//            handleItemAlbumClicked(position, file);
//        }
    }

    @Override
    public void onItemInAlbumClicked(int position, FileItem file) {
        Flog.d(TAG, "onItemInAlbumClicked=" + file.name);
        if (listener != null) {
            if (isLongClickedEvent) {
                handleItemAlbumClicked(position, file);
            } else {
                file.isSelected = false;
                openFileViewer(file);
            }
        }
    }

    private void openFileViewer(FileItem file) {
        Flog.d(TAG, "openFileFavouriteViewer=" + file);
        listener.openFileFavouriteViewer(file);
    }

    public void unselectAll() {
        FileAlbumAdapter.unAllSelected = true;
        FileAlbumAdapter.allSelected = false;

//        for (int i = 0; i < listPositionChanged.size(); i++) {
//            int changedIdx = listPositionChanged.get(i);
//            sectionAdapter.notifyItemChanged(changedIdx);
//        }

        for (Map.Entry<Integer, FileItem> entry : listPositionChanged.entrySet()) {
            int changedIdx = entry.getKey();
            Flog.d(TAG, "333 changedIdx is " + changedIdx);
            mAdapter.notifyItemChanged(changedIdx);
        }

        listPositionChanged.clear();
    }

    public void deleteFilesSelected(OnDialogEventListener listener) {
        if (listPositionChanged.size() <= 0) {
            String message = mContext.getString(R.string.please_select_media_for)
                    + " " + mContext.getString(R.string.delete);
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
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

    public void deleteFilesSelected(OnFileDialogEventListener listener) {
        if (listPositionChanged.size() <= 0) {
            String message = mContext.getString(R.string.please_select_media_for)
                    + " " + mContext.getString(R.string.delete);
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
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
            uries[idx] = FileUtil.getUrifromFile(mContext, fileItem.path);
            idx++;
        }

        Flog.d(TAG, "share changedIdxes is " + uries.length);
        FileUtil.share(mContext, uries);
    }

    public HashMap<Integer, FileItem> getListPositionChanged() {
        return listPositionChanged;
    }

    public void selectAll() {

        FileAlbumAdapter.allSelected = true;
        FileAlbumAdapter.unAllSelected = false;
//        sectionAdapter.notifyDataSetChanged();

        addAllPosChanged();
    }

    private void addAllPosChanged() {
        for (int position = 0; position < mList.size(); position++) {
            FileItem file = mList.get(position);
            int positionInAdapter = position;
            listPositionChanged.put(positionInAdapter, file);
            mAdapter.notifyItemChanged(positionInAdapter);
        }
        Flog.d(TAG, "addAllPosChanged=" + listPositionChanged.size());
    }

    public void setLongClickedEvent(boolean longClickedEvent) {
        isLongClickedEvent = longClickedEvent;
    }

    private void handleItemAlbumClicked(int position, FileItem file) {
        int positionInAdapter = position;
        if (file.isSelected) {
            listPositionChanged.put(Integer.valueOf(positionInAdapter), file);
        } else {
            Integer obj = Integer.valueOf(positionInAdapter);
            if (listPositionChanged.containsKey(obj)) {
                listPositionChanged.remove(obj);
            }
        }
        mAdapter.notifyItemChanged(positionInAdapter);

        if (listPositionChanged.size() <= 0) {
            FileAlbumAdapter.unAllSelected = true;
        }

        listener.onItemInFavouriteClicked(file, listPositionChanged.size());
    }

    public interface OnFavouriteListener {
        void onItemInFavouriteLongClicked(FileItem fileItem);

        void onItemInFavouriteClicked(FileItem fileItem, int size);

        void openFileFavouriteViewer(FileItem file);
    }
}
