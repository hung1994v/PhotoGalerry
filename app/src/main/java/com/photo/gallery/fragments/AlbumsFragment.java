package com.photo.gallery.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.halilibo.bettervideoplayer.utility.Util;
import com.photo.gallery.BuildConfig;
import com.photo.gallery.R;
import com.photo.gallery.activities.HomeFragment;
import com.photo.gallery.adapters.AlbumsAdapter;
import com.photo.gallery.callback.OnFileDialogEventListener;
import com.photo.gallery.models.AlbumItem;
import com.photo.gallery.models.FileItem;
import com.photo.gallery.utils.ConstValue;
import com.photo.gallery.utils.DbUtils;
import com.photo.gallery.utils.FileUtil;
import com.photo.gallery.utils.Flog;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Hoavt on 3/15/2018.
 */

public class AlbumsFragment extends BaseFragment implements AlbumsAdapter.OnAlbumsAdapterListener {

    private static final java.lang.String TAG = AlbumsFragment.class.getSimpleName();
    private final boolean FLAG_ADD_LONG_CLICK_EVENT = true;
    private OnAlbumsListener listener = null;
    private RecyclerView mRecyclerView = null;
    private AlbumsAdapter mAdapter = null;
    private ArrayList<AlbumItem> mListAlbums = new ArrayList<>();
    private ArrayList<AlbumItem> listSearch = new ArrayList<>();
    private HashMap<String, ArrayList<FileItem>> mListFolders = new HashMap<>();
    private int mPosition = -1;
    private boolean isLongClickedEvent = false;
    private HashMap<Integer, AlbumItem> listPositionChanged = new HashMap<>();
    private ProgressDialog progressDialog = null;

    public static AlbumsFragment newInstance(HashMap<String, ArrayList<FileItem>> mListFolders) {
        AlbumsFragment fragment = new AlbumsFragment();
        fragment.mListFolders = mListFolders;
        return fragment;
    }

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
            listener.onAlbumsFragmentReady();
        }
        initialize(mListFolders);
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
    }

    private void intSearchView() {
    }

    public void initialize(HashMap<String, ArrayList<FileItem>> listFolders) {
        mListFolders = listFolders;

//        if (mListFolders == null) {
//            return;
//        }

        Flog.d(TAG, "list folder size=" + mListFolders.size());

        setValues();
    }

    private void setValues() {

        initList();
        mAdapter = new AlbumsAdapter(mContext, mListAlbums).setListener(this);

        GridLayoutManager glm = new GridLayoutManager(mContext, ConstValue.NUM_OF_COLS_GRIDVIEW);
        mRecyclerView.setLayoutManager(glm);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initList() {

        if (mListAlbums == null) {
            mListAlbums = new ArrayList<>();
        }
        mListAlbums.clear();
        listSearch.clear();
        for (Map.Entry<String, ArrayList<FileItem>> entry : mListFolders.entrySet()) {
            String key = entry.getKey();
            ArrayList<FileItem> items = mListFolders.get(key);

            FileItem firstItem = items.get(0);

            AlbumItem item = new AlbumItem();
            item.name = firstItem.folder;
            item.size = String.valueOf(items.size());
            item.pathFirstImg = firstItem.path;
            item.alBumId = firstItem.folder_ID;
            mListAlbums.add(item);
            listSearch.add(item);
        }
        Flog.d(TAG, "size albums=" + mListAlbums.size());
    }

    public AlbumsFragment setListener(OnAlbumsListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public void onItemAlbumClicked(int position, AlbumItem album) {
        Flog.d("AAAAAAAAAA2", "album: "+ album.name + " size: "+ album.size + "position: "+ position + " sizeAlbum: "+ mListAlbums.size());
        if (FLAG_ADD_LONG_CLICK_EVENT) {
            if (listener != null) {
                if (isLongClickedEvent) {
                    handleItemAlbumClicked(position, album);
                } else {
                    album.isSelected = false;
                    openAlbumViewer(position,album);
                }
            }
        } else {
            if (listener != null) {
                mPosition = position;
                List<String> indexes = new ArrayList<String>(mListFolders.keySet()); // <== Set to List
                String key = indexes.get(position);
                ArrayList<FileItem> items = mListFolders.get(key);

                String nameAlbum = mListAlbums.get(position).name;
                listener.onOpenAlbumViewer(nameAlbum, items);
            }
        }
    }

    private void openAlbumViewer(int position, AlbumItem album) {
        mPosition = position;
//        List<String> indexes = new ArrayList<String>(mListFolders.keySet()); // <== Set to List
//        String key = indexes.get(position);
        ArrayList<FileItem> items = mListFolders.get(album.alBumId);

        String nameAlbum = mListAlbums.get(position).name;
        listener.onOpenAlbumViewer(nameAlbum, items);

    }
    private void addFragment(Fragment fragment, boolean isAnim) {
        // load fragment
//        binding.navigation.setVisibility(View.GONE);
        if (getFragmentManager() != null) {

            if (isAnim) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                transaction.add(R.id.root_view, fragment);
                transaction.addToBackStack(fragment.getClass().getSimpleName());
                transaction.commitAllowingStateLoss();
            } else {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.root_view, fragment);
                transaction.addToBackStack(fragment.getClass().getSimpleName());
                transaction.commitAllowingStateLoss();
            }
        }


    }

    @Override
    public void onItemAlbumLongClicked(int position, AlbumItem album) {
        Flog.d(TAG, "onItemAlbumLongClicked album at " + position + "_1");
        if (listener != null) {
            if (!isLongClickedEvent) {
                int positionInAdapter = position;
                listPositionChanged.put(Integer.valueOf(positionInAdapter), album);
                Flog.d(TAG, "33 isLongClickedEvent:p1=" + position + "_clicked=" + positionInAdapter);
                mAdapter.notifyItemChanged(positionInAdapter);

                listener.onItemAlbumLongClicked(album);
                setLongClickedEvent(true);
            } else {
                handleItemAlbumClicked(position, album);
            }
        }
    }

    private void handleItemAlbumClicked(int position, AlbumItem album) {
        int positionInAdapter = position;
        if (album.isSelected) {
            listPositionChanged.put(Integer.valueOf(positionInAdapter), album);
        } else {
            Integer obj = Integer.valueOf(positionInAdapter);
            if (listPositionChanged.containsKey(obj)) {
                listPositionChanged.remove(obj);
            }
        }
        mAdapter.notifyItemChanged(positionInAdapter);

        if (listPositionChanged.size() <= 0) {
            AlbumsAdapter.unAllSelected = true;
        }

        listener.onItemAlbumClicked(album, listPositionChanged.size());
    }

    public void setLongClickedEvent(boolean longClickedEvent) {
        isLongClickedEvent = longClickedEvent;
    }

    public void selectAll() {

        AlbumsAdapter.allSelected = true;
        AlbumsAdapter.unAllSelected = false;
//        sectionAdapter.notifyDataSetChanged();

        addAllPosChanged();
    }

    private void addAllPosChanged() {
        for (int position = 0; position < mListAlbums.size(); position++) {
            AlbumItem file = mListAlbums.get(position);
            int positionInAdapter = position;
            listPositionChanged.put(positionInAdapter, file);
            mAdapter.notifyItemChanged(positionInAdapter);
        }
        Flog.d(TAG, "addAllPosChanged=" + listPositionChanged.size());
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

    public void unselectAll() {

        AlbumsAdapter.unAllSelected = true;
        AlbumsAdapter.allSelected = false;

//        for (int i = 0; i < listPositionChanged.size(); i++) {
//            int changedIdx = listPositionChanged.get(i);
//            sectionAdapter.notifyItemChanged(changedIdx);
//        }

        for (Map.Entry<Integer, AlbumItem> entry : listPositionChanged.entrySet()) {
            int changedIdx = entry.getKey();
            Flog.d(TAG, "changedIdx is " + changedIdx);
            mAdapter.notifyItemChanged(changedIdx);
        }

        listPositionChanged.clear();
    }

    public int getPosition() {
        return mPosition;
    }

    public void updateUI(HashMap<String, ArrayList<FileItem>> listFolders) {
        if (mRecyclerView == null || mAdapter == null) {
            Flog.d(TAG, "3recycler not init");
            return;
        }

        mListFolders = listFolders;

        if (mListFolders == null) {
            return;
        }

        Flog.d(TAG, "3list folder size=" + mListFolders.size());
        initList();
        mAdapter.notifyDataSetChanged();
    }

    public boolean shareAlbumsSelected() {
        if (listPositionChanged==null||listPositionChanged.size()<=0) {
            return false;
        }
        shareFilesByPos(listPositionChanged);
        return true;
    }

    private void shareFilesByPos(HashMap<Integer, AlbumItem> listPosChanged) {
        ArrayList<Uri> listUris = new ArrayList<>();

        List<String> indexes = new ArrayList<String>(mListFolders.keySet()); // <== Set to List

        for (Map.Entry<Integer, AlbumItem> entry : listPosChanged.entrySet()) {
            int changedIdx = entry.getKey();
//            AlbumItem albumItem = entry.getValue();
//            Flog.d(TAG, "2 share changedIdx is "+changedIdx+"_"+albumItem.pathFirstImg);

            String key = indexes.get(changedIdx);
            ArrayList<FileItem> items = mListFolders.get(key);
            for (FileItem file : items) {
                listUris.add(FileUtil.getUrifromFile(mContext, file.path));
            }

        }
        Flog.d(TAG, "share all in ablums changedIdxes is " + listUris.size());

        Uri uries[] = new Uri[listUris.size()];
        for (int i = 0; i < listUris.size(); i++) {
            uries[i] = listUris.get(i);
        }

        FileUtil.share(mContext, uries);
    }

    public void deleteAlbumsSelected(OnFileDialogEventListener listener) {
        if (listPositionChanged.size() <= 0) {
            String message = mContext.getString(R.string.please_select_media_for)
                    + " " + mContext.getString(R.string.delete);
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
            return;
        }

//        printList(listPositionChanged);
        int len = listPositionChanged.size();
        AlbumItem[] items = new AlbumItem[len];
        int idx = 0;
        for (Map.Entry<Integer, AlbumItem> entry : listPositionChanged.entrySet()) {
            int key = entry.getKey();
            AlbumItem item = entry.getValue();
            items[idx] = item;
            idx++;
        }
        showDeleteDialog(mContext, listener, items);
    }

    private void showDeleteDialog(final Context context, final OnFileDialogEventListener listener, final AlbumItem... albums) {

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        String title = context.getString(R.string.delete);
        String message = context.getString(R.string.confirm_delete_album_dialog);
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

                        ArrayList<String> listPaths = new ArrayList<>();
                        ArrayList<FileItem> listFiles = new ArrayList<>();
                        String mes = "";

                        List<String> indexes = new ArrayList<String>(mListFolders.keySet()); // <== Set to List

                        for (Map.Entry<Integer, AlbumItem> entry : listPositionChanged.entrySet()) {
                            int changedIdx = entry.getKey();

                            String key = indexes.get(changedIdx);
                            ArrayList<FileItem> items = mListFolders.get(key);
                            for (FileItem file : items) {
                                listPaths.add(file.path);
                                Uri uri = Uri.fromFile(new File(file.path));

                                boolean success = FileUtil.delete(context, uri);
                                if (success) {
                                    listFiles.add(file);
                                    DbUtils.deleteFavourite(file);
                                } else {
                                    mes += file.path + "\n";
                                }
                            }

                        }
                        Flog.d(TAG, "delete all in ablums changedIdxes 12is " + listPaths.size());

                        String[] paths = new String[listPaths.size()];
                        for (int i = 0; i < listPaths.size(); i++) {
                            paths[i] = listPaths.get(i);
                        }

                        if (mes.equals("")) {
                            FileUtil.toastSuccess(context, context.getString(R.string.delete));
                            FileUtil.scanMediaStore(context, paths);
                        } else {
                            FileUtil.toastFailed(context, context.getString(R.string.delete) + ":\n" + mes);
                        }

                        FileItem[] files = new FileItem[listFiles.size()];
                        for (int i = 0; i < listFiles.size(); i++) {
                            files[i] = listFiles.get(i);
                        }

//                        for (int i = 0; i < albums.length; i++) {
//                            AlbumItem album = albums[i];
//                            File dir = new File(album.pathFirstImg).getParentFile();
//                            if (dir!=null&&dir.exists()) {
//                                boolean deleted = dir.delete();
//                                Flog.d(TAG, "folder at "+i+" deleted: "+deleted);
//                            }
//                        }

                        if (listener != null) {
                            listener.onOk(files);
                        }
                    }
                }).show();
    }

    public void onBackPressed() {

    }

    public void search(String newText) {
        if(mListAlbums!=null){
            mListAlbums.clear();
        }
        for (AlbumItem fileItem: listSearch ){
                if(fileItem.name.toLowerCase().contains(newText.toLowerCase())){
                    mListAlbums.add(fileItem);
                }
        }
        Flog.d("AAAAAAAAAA3","size album: " + mListAlbums.size());
        mAdapter.notifyDataSetChanged();
    }

    public interface OnAlbumsListener {
        void onAlbumsFragmentReady();

        void onOpenAlbumViewer(String nameAlbum, ArrayList<FileItem> listFiles);

        void onItemAlbumLongClicked(AlbumItem album);

        void onItemAlbumClicked(AlbumItem album, int numOfSelected);
    }
}
