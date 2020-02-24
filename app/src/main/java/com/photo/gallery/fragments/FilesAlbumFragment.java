package com.photo.gallery.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.photo.gallery.R;
import com.photo.gallery.activities.HomeFragment;
import com.photo.gallery.adapters.FileAlbumAdapter;
import com.photo.gallery.callback.OnDialogEventListener;
import com.photo.gallery.callback.OnFileDialogEventListener;
import com.photo.gallery.databinding.FragmentFileAlbumsBinding;
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
 * Created by Hoavt on 3/19/2018.
 */

public class FilesAlbumFragment extends BaseFragment implements View.OnClickListener, FileAlbumAdapter.OnAlbumsAdapterListener {

    private static final String TAG = FilesAlbumFragment.class.getSimpleName();
    private RecyclerView mRecyclerview = null;
    private ImageView mBtnBack, mBtnSort, mBtnDelete;
    private OnFileAlbumsListener listener = null;
    private ArrayList<FileItem> mListFilesInAlbum = new ArrayList<>();
    private ArrayList<FileItem> listSearch = new ArrayList<>();
    private TextView mTvNameAlbum = null, tvNumOfMulti = null;
    private FileAlbumAdapter mAdapter = null;
    private String mNameAlbum = null;
    private ProgressDialog dialog = null;
    private ViewGroup multiSelectToolbar, myToolbar;
    private boolean isLongClickedEvent = false;
    private HashMap<Integer, FileItem> listPositionChanged = new HashMap<>();
    private View btnBackMulti, btnDelMulti, btnMoreMulti;
    private AlertDialog dialogPopupFile = null;
    public static int FLAG_SHARE_IN_FILEALBUM = HomeFragment.FLAG_MODE_NONE;
    private FragmentFileAlbumsBinding binding;
    private SearchView searchView;
    private boolean isToolbarShow;

    public static FilesAlbumFragment newInstance(ArrayList<FileItem> list, String nameAlbum) {

        FilesAlbumFragment fragment = new FilesAlbumFragment();
        fragment.mListFilesInAlbum = list;
        fragment.mNameAlbum = nameAlbum;
        fragment.listSearch.addAll(list);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_file_albums, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initBannerAdmob();
        initViews();
        setValues();
    }


    private void initBannerAdmob() {
//        AdmobBannerHelper.init(mContext, (ViewGroup) getView().findViewById(R.id.admob_banner))
//                .setAdUnitId(getString(R.string.smart_banner_ad_id))
//                .setAdSize(AdSize.SMART_BANNER)
//                .loadAd();
    }

    public boolean isSearchViewClose() {
        if (searchView != null) {
            return searchView.isIconified();
        } else
            return true;
    }

    public void SearchViewClose() {
        if (!searchView.isIconified()) {
            searchView.onActionViewCollapsed();
            binding.toolbar.collapseActionView();
//            KeyBoardHelper.hideKeyBoard(this);
        }
    }


    private void initViews() {
        initDialog();

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

        mRecyclerview = (RecyclerView) viewParent.findViewById(R.id.recycler_view);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });
        binding.toolbar.setTitle(mNameAlbum);
        binding.toolbar.inflateMenu(R.menu.menu_file_album);
        binding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.selectItem_btn:
                        isLongClickedEvent = true;
                        showMyToolbar();
                        setTextNumOfSelected(0);
                        break;
                }
                return false;
            }
        });

        intSearchView();
//        mBtnBack = (ImageView) viewParent.findViewById(R.id.btn_back);
//        mBtnSort = (ImageView) viewParent.findViewById(R.id.btn_sort);
//        mBtnDelete = (ImageView) viewParent.findViewById(R.id.btn_delete);
//        mTvNameAlbum = (TextView) viewParent.findViewById(R.id.tv_title_album);
//        mTvNameAlbum.setText(mNameAlbum);

//
//        myToolbar = (ViewGroup) viewParent.findViewById(R.id.my_toolbar);
//        multiSelectToolbar = (ViewGroup) viewParent.findViewById(R.id.multi_toolbar);

//        btnBackMulti = viewParent.findViewById(R.id.btn_back_multiselected);
//        btnDelMulti = viewParent.findViewById(R.id.btn_delete_multiselected);
//        btnMoreMulti = viewParent.findViewById(R.id.btn_more_multiselected);
//        tvNumOfMulti = (TextView) viewParent.findViewById(R.id.tv_num_of_selected);
    }

    private void intSearchView() {
        searchView = (SearchView) binding.toolbar.getMenu().findItem(R.id.search_bar).getActionView();
        searchView.setQueryHint(getString(R.string.search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                queryText(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                queryText(newText);
                return false;
            }
        });
    }

    private void queryText(String newText) {
        if (mListFilesInAlbum != null) {
            mListFilesInAlbum.clear();
        }
        for (FileItem fileItem : listSearch) {
            if (fileItem.name.toLowerCase().contains(newText.toLowerCase())) {
                mListFilesInAlbum.add(fileItem);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    private ArrayList<FileItem> getListFromMap(HashMap<Integer, FileItem> mapPosChanged) {
        ArrayList<FileItem> list = new ArrayList<>();
        for (Map.Entry<Integer, FileItem> entry : mapPosChanged.entrySet()) {
            FileItem fileItem = entry.getValue();
            list.add(fileItem);
        }
        return list;
    }

    private void initPopupFileDialog() {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        // add a list
        String[] animals = {getString(R.string.share), getString(R.string.copy),
                getString(R.string.move), getString(R.string.select_all),
                getString(R.string.unselect_all)};
        builder.setItems(animals, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // share
                        actionShareFiles();
                        break;
                    case 1: // copy
                        if (listener != null) {
                            listener.onCopyFileAlbumFragment(getListFromMap(listPositionChanged));
                        }
                        break;
                    case 2: // move
                        if (listener != null) {
                            listener.onMoveFileAlbumFragment(getListFromMap(listPositionChanged));
                        }
                        break;
                    case 3: // select all
                        actionSelectAll();
                        break;
                    case 4: // unselect all
                        actionUnselectAll();
                        break;
                }
                dialog.dismiss();
            }
        });

        // create and open the alert dialogPopupFile
        dialogPopupFile = builder.create();

        Window window = dialogPopupFile.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.TOP | Gravity.RIGHT;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
    }

    private void actionUnselectAll() {
        unselectAll();
        setTextNumOfSelected(0);
    }

    private void actionSelectAll() {
        selectAll();
        setTextNumOfSelected(mListFilesInAlbum.size());
    }

    public void selectAll() {

        FileAlbumAdapter.allSelected = true;
        FileAlbumAdapter.unAllSelected = false;
//        sectionAdapter.notifyDataSetChanged();

        addAllPosChanged();
    }

    private void addAllPosChanged() {
        for (int position = 0; position < mListFilesInAlbum.size(); position++) {
            FileItem file = mListFilesInAlbum.get(position);
            int positionInAdapter = position;
            listPositionChanged.put(positionInAdapter, file);
            mAdapter.notifyItemChanged(positionInAdapter);
        }
        Flog.d(TAG, "addAllPosChanged=" + listPositionChanged.size());
    }

    private void actionShareFiles() {

        FLAG_SHARE_IN_FILEALBUM = HomeFragment.FLAG_MODE_ENABLE;
        HomeFragment.FLAG_OPEN_WITH_ACTION = HomeFragment.FLAG_MODE_DISABLE;
        boolean shown = showHintSelectImgsDialog(mContext.getString(R.string.share));
        if (shown) {
            return;
        }

        shareFilesByPos(listPositionChanged);
    }

    private void shareFilesByPos(HashMap<Integer, FileItem> listPosChanged) {
        Uri uries[] = new Uri[listPosChanged.size()];

        int idx = 0;
        for (Map.Entry<Integer, FileItem> entry : listPosChanged.entrySet()) {
            int changedIdx = entry.getKey();
            FileItem fileItem = entry.getValue();
            Flog.d(TAG, "2 share changedIdx is " + changedIdx + "_" + fileItem.path);
            uries[idx] = FileUtil.getUrifromFile(mContext, fileItem.path);
            idx++;
        }

        Flog.d(TAG, "share changedIdxes is " + uries.length);
        FileUtil.share(mContext, uries);
    }

    public void updateUI(ArrayList<FileItem> listFiles) {
        showDialog();
        FileAlbumAdapter.allSelected = false;
        FileAlbumAdapter.unAllSelected = true;

        mListFilesInAlbum = listFiles;

        if (mListFilesInAlbum == null) {
            return;
        }
        Flog.d(TAG, "1 list file albums photo size=" + mListFilesInAlbum.size());

        mAdapter = new FileAlbumAdapter(mContext, mListFilesInAlbum).setListener(this);
        mRecyclerview.setAdapter(mAdapter);

        hideDialog();
    }

    public void show(String nameAlbum, ArrayList<FileItem> listFiles) {
        showDialog();
        FileAlbumAdapter.allSelected = false;
        FileAlbumAdapter.unAllSelected = true;

        mNameAlbum = nameAlbum;
        mListFilesInAlbum = listFiles;

        if (mListFilesInAlbum == null) {
            return;
        }
        Flog.d(TAG, "2 list file albums photo size=" + mListFilesInAlbum.size());

        setTitle(mNameAlbum);
        resetData();
//        mAdapter.notifyDataSetChanged();
        mAdapter = new FileAlbumAdapter(mContext, mListFilesInAlbum).setListener(this);
        mRecyclerview.setAdapter(mAdapter);

        hideDialog();
    }

    private void resetData() {
//        showMyToolbar();
        isLongClickedEvent = false;
        if (listPositionChanged != null) {
            listPositionChanged.clear();
        } else {
            listPositionChanged = new HashMap<>();
        }
    }

    private void setValues() {


        mAdapter = new FileAlbumAdapter(mContext, mListFilesInAlbum).setListener(this);

        // Set up your RecyclerView with the SectionedRecyclerViewAdapter
        GridLayoutManager glm = new GridLayoutManager(mContext, ConstValue.NUM_OF_COLS_GRIDVIEW);
        mRecyclerview.setLayoutManager(glm);
        mRecyclerview.setAdapter(mAdapter);

//
//        mBtnBack.setOnClickListener(this);
//        mBtnSort.setOnClickListener(this);
//        mBtnDelete.setOnClickListener(this);

//        btnBackMulti.setOnClickListener(this);
//        btnDelMulti.setOnClickListener(this);
//        btnMoreMulti.setOnClickListener(this);
    }

    private void setTitle(String nameAlbum) {
        mTvNameAlbum.setText(nameAlbum);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
//            case R.id.btn_back:
//                if (listener != null) {
//                    listener.onBackFileAlbumsFragment();
//                }
//                break;
//            case R.id.btn_sort:
//                // TODO:
//                break;
//            case R.id.btn_delete:
//
//                if (listener != null) {
//                    listener.onDeleteAlbum();
//                }
//                break;
//            case R.id.btn_back_multiselected:
//
//                handleBackMultiSelected();
//                break;
//            case R.id.btn_delete_multiselected:
//
//                if (listener != null) {
//                    listener.onDeleteFileAlbumFragment();
//                }
//                break;
//            case R.id.btn_more_multiselected:
//
//                showPopupFileDialog();
//                break;
        }
    }

    public void deleteCurAlbum(OnDialogEventListener listener) {
        if (mListFilesInAlbum == null || mListFilesInAlbum.size() <= 0) {
            return;
        }

        int len = mListFilesInAlbum.size();
        FileItem[] items = new FileItem[len];
        for (int i = 0; i < len; i++) {
            items[i] = mListFilesInAlbum.get(i);
        }

        showDeleteAlbumDialog(mContext, listener, items);
    }

    public void deleteCurAlbum(OnFileDialogEventListener listener) {
        if (mListFilesInAlbum == null || mListFilesInAlbum.size() <= 0) {
            return;
        }

        int len = mListFilesInAlbum.size();
        FileItem[] items = new FileItem[len];
        for (int i = 0; i < len; i++) {
            items[i] = mListFilesInAlbum.get(i);
        }

        showDeleteAlbumDialog(mContext, listener, items);
    }

    private void showPopupFileDialog() {
        if (dialogPopupFile != null && !dialogPopupFile.isShowing()) {
            dialogPopupFile.show();
        }
    }

    public void handleBackMultiSelected() {

        setLongClickedEvent(false);
        unselectAll();
//        showMyToolbar();
    }

    private boolean showHintSelectImgsDialog(String action) {
        if (listPositionChanged == null || listPositionChanged.size() <= 0) {
            String message = mContext.getString(R.string.please_select_media_for)
                    + " " + action;
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public void deleteFilesSelected(OnDialogEventListener listener) {

        boolean shown = showHintSelectImgsDialog(mContext.getString(R.string.delete));
        if (shown) {
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

        boolean shown = showHintSelectImgsDialog(mContext.getString(R.string.delete));
        if (shown) {
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
                                list.add(item);
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

                        if (mListFilesInAlbum.size() == arrItems.length) {
                            if (FilesAlbumFragment.this.listener != null) {
                                FilesAlbumFragment.this.listener.onBackFileAlbumsFragment();
                            }
                        }
                        if (listener != null) {
                            listener.onOk(arrItems);
                            handleBackMultiSelected();
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
                            handleBackMultiSelected();
                        }
                    }
                }).show();
    }

    private void showDeleteAlbumDialog(final Context context, final OnFileDialogEventListener listener, final FileItem... items) {

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

                        ArrayList<FileItem> listFiles = new ArrayList<>();

                        int len = items.length;
                        Uri[] uries = new Uri[len];
                        String[] paths = new String[len];

                        String mes = "";
                        for (int i = 0; i < len; i++) {
                            FileItem item = items[i];
                            paths[i] = item.path;
                            uries[i] = Uri.fromFile(new File(item.path));

                            boolean success = FileUtil.delete(context, uries[i]);
                            if (success) {
                                listFiles.add(item);
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

                        FileItem[] files = new FileItem[listFiles.size()];
                        for (int i = 0; i < listFiles.size(); i++) {
                            FileItem file = listFiles.get(i);
                            files[i] = file;
                        }

                        if (listener != null) {
                            listener.onOk(files);
                        }

                        // delete directory
//                        if (len > 0 && mListFilesInAlbum.get(0) != null) {
//                            File dir = new File(mListFilesInAlbum.get(0).path).getParentFile();
//                            if (dir != null && dir.exists()) {
//                                boolean deleted = dir.delete();
//                                Flog.d(TAG, "folder " + dir.getAbsolutePath() + " deleted: " + deleted);
//
//                                if (dir.exists()) {
//                                    String deleteCmd = "rm -r " + dir.getAbsolutePath();
//                                    Runtime runtime = Runtime.getRuntime();
//                                    try {
//                                        runtime.exec(deleteCmd);
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }
//                        }
                    }
                }).show();
    }

    private void showDeleteAlbumDialog(final Context context, final OnDialogEventListener listener, final FileItem... fileItem) {

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
            Flog.d(TAG, "file map " + index + ": " + index + "_file: " + entry.getValue());
        }
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
            Flog.d(TAG, "223 changedIdx is " + changedIdx);
            mAdapter.notifyItemChanged(changedIdx);
        }

        listPositionChanged.clear();
    }

    private void initDialog() {
        dialog = new ProgressDialog(mContext);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    return true; // Consumed
                }
                return false;
            }
        });
        dialog.setCanceledOnTouchOutside(false);

        initPopupFileDialog();
    }

    private void showDialog() {
        if (dialog != null) {
            dialog.show();
        }
    }

    private void hideDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public FilesAlbumFragment setListener(OnFileAlbumsListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public void onItemInAlbumLongClicked(int position, FileItem file) {
        Flog.d(TAG, "onItemInAlbumLONGClicked=" + file);

        if (!isLongClickedEvent) {
            int positionInAdapter = position;
            listPositionChanged.put(Integer.valueOf(positionInAdapter), file);
            Flog.d(TAG, "12 isLongClickedEvent:p=" + position + "_clicked=" + positionInAdapter);
            mAdapter.notifyItemChanged(positionInAdapter);

            showMyToolbar();
            setTextNumOfSelected(1);

            setLongClickedEvent(true);
        } else {
            handleItemAlbumClicked(position, file);
        }
    }

    private void setTextNumOfSelected(int numOfSelected) {
        if (numOfSelected <= 0) {
            binding.tvTitleFileEdit.setText("0");
        } else {
            binding.tvTitleFileEdit.setText("" + numOfSelected);
        }
    }

    private void showMyToolbar() {
        isToolbarShow = true;
        binding.toolbar.setVisibility(View.INVISIBLE);
        binding.toolbarHideFilesEdit.setVisibility(View.VISIBLE);
        binding.toolbarHideFilesEdit.inflateMenu(R.menu.home_edit);
        binding.toolbarHideFilesEdit.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeToolbar();
            }
        });
        binding.toolbarHideFilesEdit.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.share_btn:
                        actionShareFiles();
//                            closeToolbar();
                        break;
                    case R.id.delete_btn:
                        listener.onDeleteFileAlbumFragment();

                        break;
                    case R.id.move_btn:
                        if (listener != null) {
                            listener.onMoveFileAlbumFragment(getListFromMap(listPositionChanged));
                        }
                        break;
                    case R.id.coppy_btn:
                        if (listener != null) {
                            listener.onCopyFileAlbumFragment(getListFromMap(listPositionChanged));
                        }
                        break;
                    case R.id.selectAll_btn:
                        selectAll();
                        setTextNumOfSelected(mListFilesInAlbum.size());
//                            closeToolbar();
                        break;
                    case R.id.unSelectAll_btn:
                        unselectAll();
                        setTextNumOfSelected(0);
//                            closeToolbar();
                        break;

                }
                return false;
            }
        });


    }

    public boolean getToolbarShow() {
        return isToolbarShow;
    }

    public void closeToolbar() {
        Flog.d("XXXXXX", "closeToolbar");
        isToolbarShow = false;
        binding.toolbarHideFilesEdit.getMenu().clear();
        binding.toolbarHideFilesEdit.setVisibility(View.INVISIBLE);
        binding.toolbar.setVisibility(View.VISIBLE);
        setLongClickedEvent(false);
        unselectAll();

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

        setTextNumOfSelected(listPositionChanged.size());
    }

    @Override
    public void onItemInAlbumClicked(int position, FileItem file) {
        Flog.d(TAG, "onItemInAlbumClicked=" + file.name);
        if (isLongClickedEvent) {
            handleItemAlbumClicked(position, file);
        } else {
            file.isSelected = false;
            openFileViewer(file);
        }
    }






    private void openFileViewer(FileItem file) {
        Flog.d(TAG, "openFileFavouriteViewer=" + file);
        listener.openFileViewer(file);
        if(!isSearchViewClose()){
            SearchViewClose();
        }
    }

    public boolean isLongClickedEvent() {
        return isLongClickedEvent;
    }

    public void setLongClickedEvent(boolean longClickedEvent) {
        isLongClickedEvent = longClickedEvent;
    }

    public interface OnFileAlbumsListener {
        void onBackFileAlbumsFragment();

        void openFileViewer(FileItem file);

        void onDeleteFileAlbumFragment();

        void onDeleteAlbum();

        void onCopyFileAlbumFragment(ArrayList<FileItem> listFromMap);

        void onMoveFileAlbumFragment(ArrayList<FileItem> listFromMap);
    }
}
