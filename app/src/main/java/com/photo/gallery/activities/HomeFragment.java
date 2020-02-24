package com.photo.gallery.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bsoft.core.AdmobFullHelper;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;

import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.photo.gallery.R;
import com.photo.gallery.adapters.MyViewPagerAdapter;
import com.photo.gallery.callback.OnDialogEventListener;
import com.photo.gallery.callback.OnFileDialogEventListener;
import com.photo.gallery.databinding.ActivityContentBinding;
import com.photo.gallery.fragments.AlbumsFragment;
import com.photo.gallery.fragments.BaseFragment;
import com.photo.gallery.fragments.DialogCreateFolderFragment;
import com.photo.gallery.fragments.FavouriteFragment;
import com.photo.gallery.fragments.FilesAlbumFragment;
import com.photo.gallery.fragments.PhotoFragment;
import com.photo.gallery.fragments.PhotoViewerFragment;
import com.photo.gallery.fragments.SelectAlbumFragment;
import com.photo.gallery.fragments.SettingFragment;
import com.photo.gallery.fragments.VideoFragment;
import com.photo.gallery.fragments.VideoViewerFragment;
import com.photo.gallery.models.AlbumItem;
import com.photo.gallery.models.FileItem;
import com.photo.gallery.taskes.GroupFilesTask;
import com.photo.gallery.utils.ConstValue;
import com.photo.gallery.utils.CustomToast;
import com.photo.gallery.utils.DateUtils;
import com.photo.gallery.utils.DbUtils;
import com.photo.gallery.utils.FileUtil;
import com.photo.gallery.utils.Flog;
import com.photo.gallery.utils.GalleryUtil;
import com.photo.gallery.utils.KeyboardUtil;
import com.photo.gallery.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;
import static com.photo.gallery.adapters.MyViewPagerAdapter.TAB_ALBUM;
import static com.photo.gallery.adapters.MyViewPagerAdapter.TAB_PHOTO;


/**
 * Created by Hoavt on 3/15/2018.
 */

public class HomeFragment extends BaseFragment implements GroupFilesTask.OnGroupFilesListener, PhotoFragment.OnPhotoListener, VideoFragment.OnVideoListener, AlbumsFragment.OnAlbumsListener, View.OnClickListener, FilesAlbumFragment.OnFileAlbumsListener, PhotoViewerFragment.OnPhotoViewerListener, SelectAlbumFragment.OnSelectAlbumListener, VideoViewerFragment.OnVideoViewerListener, MyViewPagerAdapter.onPhotoListener, DialogCreateFolderFragment.onCreateNewFolder, FavouriteFragment.OnFavouriteListener {

    public static final int FLAG_MODE_NONE = 0;
    public static final int FLAG_MODE_ENABLE = 1;
    public static final int FLAG_MODE_DISABLE = 2;
    private static final java.lang.String TAG = HomeFragment.class.getSimpleName();
    private static final boolean FLAG_REFRESH_OPTIMIZE = true;
    public static boolean FLAG_RELOAD_GALLERY = false;
    public static int FLAG_OPEN_WITH_ACTION = FLAG_MODE_NONE;
    private final boolean FLAG_REFRESH_ENABLE = false;
    private final int TOTAL_THREADS = 1;
    private ArrayList<FileItem> listAllFiles = new ArrayList<>(), listAllImgs = new ArrayList<>(), listAllVideos = new ArrayList<>();
    private HashMap<String, ArrayList<FileItem>> mapAllFolders = new HashMap<>();
    private Map<String, ArrayList<FileItem>> mapAllImgSections = new HashMap<>(), mapAllImgMonthSections = new HashMap<>(), mapAllFileSetions = new HashMap<>();
    private ProgressDialog progressDialog = null;
    private PhotoFragment photoFragment = null;
    private BaseFragment videoFragment = null;
    private AlbumsFragment albumsFragment = null;
    private BaseFragment favouriteFragment = null;
    private FilesAlbumFragment filesAlbumFrag;
    private String mInputText = "";
    private BaseFragment searchFrag = null, photoViewerFrag = null,
            videoViewerFrag = null, selectAlbumFrag = null;
    private FragmentManager fm = null;
    private int heightLargeViewPager = 0, heightNormalViewPager = 0;
    private AlertDialog dialogPopupMulFile = null, dialogPopupFile = null;
    private long mLastClickTime = 0;
    private AdmobFullHelper admobFullHelper = null;
    private AlertDialog alertDialog = null;
    private Switch switchDefaultApp = null;
    private int mHeightAdmobBanner = 0;
    private View progressContainer = null;
    private ActivityContentBinding binding;
    private MyViewPagerAdapter adapter;
    private boolean isToolbarShow;
    private boolean isToolbarAlbumShow;
    private SearchView searchView;
    private boolean isDaySection = true;
    private boolean isFromMain;

    public static HomeFragment newInstance(ArrayList<FileItem> listAllFiles, boolean isFromMain) {
        HomeFragment fragment = new HomeFragment();
        fragment.listAllFiles = listAllFiles;
        fragment.isFromMain = isFromMain;
        return fragment;
    }


//    public static void buildAppRate(final Activity activity, final OnClickButtonListener listener) {
//        AppRate.with(activity)
//                .setInstallDays(0) // default 10, 0 means install day.
//                .setLaunchTimes(1) // default 10
//                .setRemindInterval(99) // default 1
//                .setDebug(true) // default false
//                .setOnClickButtonListener(listener)
//                .monitor();
//    }

    public static Map<Integer, FileItem> sort(HashMap<Integer, FileItem> input) {

        Map<Integer, FileItem> map = new TreeMap<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer idx1, Integer idx2) {
                int cmp = 0;
                if (idx1 < idx2) {
                    cmp = 1;
                } else if (idx1 > idx2) {
                    cmp = -1;
                }
                return cmp;
            }
        });
        map.putAll(input);

        return map;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_content, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setLightStatusBar();
        initViews(view);
        getData();
        loadAllFolders();
        intViewpage();
//        intViewpage();
    }

    private String m_Text = "";


    private void actionCreatFolder(HashMap<String, ArrayList<FileItem>> mapAllFolders) {
        FragmentManager fm = getFragmentManager();
        DialogCreateFolderFragment dialogCreateFolderFragment = DialogCreateFolderFragment.newInstance(mContext, mapAllFolders, this);
        dialogCreateFolderFragment.setCancelable(false);
        if (fm != null) {
            dialogCreateFolderFragment.show(fm, dialogCreateFolderFragment.getClass().getSimpleName());
        }
    }


    protected void setLightStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = getActivity().getWindow().getDecorView().getSystemUiVisibility(); // get current flag
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;   // add LIGHT_STATUS_BAR to flag
            getActivity().getWindow().getDecorView().setSystemUiVisibility(flags);
            getActivity().getWindow().setStatusBarColor(Color.WHITE); // optional
        }
    }


    private void loadAllFolders() {
        if (listAllFiles == null) {
            toastLoadGalleryFailed();
            return;
        }

        showDialog();
        GroupFilesTask groupFoldersTask = new GroupFilesTask(mContext).setListener(this);
        groupFoldersTask.execute(listAllFiles);
    }

    private void toastLoadGalleryFailed() {
        Toast.makeText(mContext, getString(R.string.load_gallery_failed), Toast.LENGTH_SHORT).show();
    }

    private void initViews(View view) {
        initAdmob();
        initDialog(view);

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


    protected Fragment getMainFragment() {
        return getFragmentManager().findFragmentById(R.id.container_view);
    }


    @Override
    public void onVideoViewerFragmentReady() {

    }

    @Override
    public void onBackVideoViewerFragment() {
        getFragmentManager().popBackStack();
        setLightStatusBar();
    }


    @Override
    public void onMoreVideoViewerFragment(FileItem fileItem) {
        showPopupFileDialog(BaseFragment.FRAGMENT_VIDEO_VIEWER, fileItem);
    }

    @Override
    public void onShareVideoViewerFragment(FileItem fileItem) {
        FileUtil.share(mContext, FileUtil.getUrifromFile(mContext, fileItem.path));
    }

    @Override
    public void onDeleteVideoViewerFragment(FileItem fileItem) {
        showDeleteDialog(mContext,
                new OnFileDialogEventListener() {
                    @Override
                    public void onOk(FileItem... items) {
                        onBackVideoViewerFragment();
                        if (!FLAG_REFRESH_ENABLE) {
                            if (!FLAG_REFRESH_OPTIMIZE) {
                                refreshGallery();
                            } else {
                                refreshFileGalleryDeleted(items);
                            }
                        }
                    }
                },
                fileItem);
    }

    private void exitApp() {
        try {
            showAppRate();
        } catch (Exception ex) {
            ex.printStackTrace();
//            finishAffinity();
        }
    }

    private void showAppRate() {
    }


    private void initDialog(View view) {

        progressContainer = view.findViewById(R.id.progress_circular);
        progressContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // DO NOTHING.
            }
        });

        initPopupMulFileDialog();

        if (true) {
            return;
        }

        progressDialog = new ProgressDialog(mContext);

    }

    private void showDialog() {

        if (progressContainer != null) {
            progressContainer.setVisibility(View.VISIBLE);
        }
        if (true) {
            return;
        }
        if (progressDialog != null) {
            progressDialog.show();
        }
    }

    private void hideDialog() {

        if (progressContainer != null) {
            progressContainer.setVisibility(View.GONE);
        }
        if (true) {
            return;
        }
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void getData() {
        if (!isFromMain) {
            listAllFiles.clear();
            listAllFiles = getActivity().getIntent().getParcelableArrayListExtra(ConstValue.EXTRA_LIST_ALL_FILES);
        }
        Flog.d(TAG, "listAllFiles size=" + (listAllFiles == null ? -1 : listAllFiles.size()));
    }

    public void refreshFileGalleryDeleted(FileItem... items) {
        closeToolbar();
        Flog.d(TAG, "refreshFileGalleryDeleted=" + items);
        if (items == null || items.length <= 0) {
            return;
        }
        showDialog();

        HashMap<Integer, FileItem> listItemOrderAll = new HashMap<>();
        HashMap<Integer, FileItem> listItemOrderImg = new HashMap<>();
        HashMap<Integer, FileItem> listItemOrderVideo = new HashMap<>();
        for (int i = 0; i < items.length; i++) {
            FileItem item = items[i];
            Flog.d(TAG, "refresh item " + i + ": " + item.path);

            int posInList = -1;
            if (item.isImage && this.listAllImgs.contains(item)) {
                posInList = this.listAllImgs.indexOf(item);
                listItemOrderImg.put(posInList, item);
            } else if (!item.isImage && this.listAllVideos.contains(item)) {
                posInList = this.listAllVideos.indexOf(item);
                listItemOrderVideo.put(posInList, item);
            }

            if (this.listAllFiles.contains(item)) {
                posInList = this.listAllFiles.indexOf(item);
                listItemOrderAll.put(posInList, item);
            }
            Flog.d(TAG, "list posInList " + posInList + ": " + item.path);
        }

        Map<Integer, FileItem> listSortedAll = null;
        if (listItemOrderAll.size() > 0) {
            listSortedAll = sort(listItemOrderAll);
        }
        Map<Integer, FileItem> listSortedImg = null;
        if (listItemOrderImg.size() > 0) {
            listSortedImg = sort(listItemOrderImg);
        }
        Map<Integer, FileItem> listSortedVideo = null;
        if (listItemOrderVideo.size() > 0) {
            listSortedVideo = sort(listItemOrderVideo);
        }

        if (listSortedAll != null) {
            ArrayList<FileItem> arrSortedAll = new ArrayList<>();
            for (Map.Entry item : listSortedAll.entrySet()) {
                int idx = (int) item.getKey();
                FileItem obj = (FileItem) item.getValue();
                Flog.d(TAG, "list posInList23 " + idx + ": " + obj.path);
                arrSortedAll.add(obj);
            }

            for (int i = arrSortedAll.size() - 1; i >= 0; i--) {
                FileItem item = arrSortedAll.get(i);
//            Flog.d(TAG, "delete at "+listSorted1.get(i)+": "+item.path);
                this.listAllFiles.remove(item);
            }

            this.mapAllFileSetions = GalleryUtil.groupListSectionByDate(listAllFiles);

            mapAllFolders = GalleryUtil.groupListAllAlbums(listAllFiles);

            ((AlbumsFragment) albumsFragment).updateUI(mapAllFolders);
//            ((SelectAlbumFragment) selectAlbumFrag).updateUI(mapAllFolders);


            List<String> indexes = new ArrayList<String>(mapAllFolders.keySet()); // <== Set to List
            int position = ((AlbumsFragment) albumsFragment).getPosition();
            try {
                String key = indexes.get(position);
                ArrayList<FileItem> listFilesInCurDir = mapAllFolders.get(key);
                ((FilesAlbumFragment) filesAlbumFrag).updateUI(listFilesInCurDir);
            } catch (Exception npe) {
                npe.printStackTrace();
            }
        }

        if (listSortedImg != null) {
            ArrayList<FileItem> arrSortedImg = new ArrayList<>();
            for (Map.Entry item : listSortedImg.entrySet()) {
                int idx = (int) item.getKey();
                FileItem obj = (FileItem) item.getValue();
//                Flog.d(TAG, "list posInList23 " + idx + ": " + obj.path);
                arrSortedImg.add(obj);
            }

            for (int i = arrSortedImg.size() - 1; i >= 0; i--) {
                FileItem item = arrSortedImg.get(i);
//            Flog.d(TAG, "delete at "+listSorted1.get(i)+": "+item.path);
                this.listAllImgs.remove(item);
            }

            this.mapAllImgSections = GalleryUtil.groupListSectionByDate(this.listAllImgs);
            this.mapAllImgMonthSections = GalleryUtil.groupListSectionByMonth(this.listAllImgs);
            if (isDaySection) {
                ((PhotoFragment) photoFragment).updateUI(listAllImgs, mapAllImgSections);
            } else {
                ((PhotoFragment) photoFragment).updateUI(listAllImgs, mapAllImgMonthSections);
            }

        }

        if (listSortedVideo != null) {
            ArrayList<FileItem> arrSortedVideo = new ArrayList<>();
            for (Map.Entry item : listSortedVideo.entrySet()) {
                int idx = (int) item.getKey();
                FileItem obj = (FileItem) item.getValue();
//                Flog.d(TAG, "list posInList23 " + idx + ": " + obj.path);
                arrSortedVideo.add(obj);
            }

            for (int i = arrSortedVideo.size() - 1; i >= 0; i--) {
                FileItem item = arrSortedVideo.get(i);
//            Flog.d(TAG, "delete at "+listSorted1.get(i)+": "+item.path);
                this.listAllVideos.remove(item);
            }

            this.mapAllImgSections = GalleryUtil.groupListSectionByDate(this.listAllImgs);
            this.mapAllImgMonthSections = GalleryUtil.groupListSectionByMonth(this.listAllImgs);
//            ((VideoFragment) videoFragment).updateUI(listAllVideos, mapAllImgMonthSections);
        }

        Flog.d(TAG, "size after deleted: " + this.listAllImgs.size());
        if (favouriteFragment != null) {
            ((FavouriteFragment) favouriteFragment).refreshList();
        }


        hideDialog();
    }

    public void refreshFileGalleryCopied(FileItem... items) {
        Flog.d(TAG, "refreshFileGalleryCopied=" + items);
        if (items == null || items.length <= 0) {
            return;
        }
        showDialog();

        ArrayList<FileItem> arrAll = new ArrayList<>();
        ArrayList<FileItem> arrImg = new ArrayList<>();
        ArrayList<FileItem> arrVideo = new ArrayList<>();
        for (int i = 0; i < items.length; i++) {
            FileItem item = items[i];
            if (item.isImage) {
                arrImg.add(item);
            } else {
                arrVideo.add(item);
            }

            arrAll.add(item);
        }

        if (arrAll.size() > 0) {

            for (int i = 0; i < arrAll.size(); i++) {
                FileItem item = arrAll.get(i);
                //            Flog.d(TAG, "delete at "+listSorted1.get(i)+": "+item.path);
                this.listAllFiles.add(item);
            }

            this.mapAllFileSetions = GalleryUtil.groupListSectionByDate(listAllFiles);

            mapAllFolders = GalleryUtil.groupListAllAlbums(listAllFiles);

            ((AlbumsFragment) albumsFragment).updateUI(mapAllFolders);
            ((SelectAlbumFragment) selectAlbumFrag).updateUI(mapAllFolders);


            List<String> indexes = new ArrayList<String>(mapAllFolders.keySet()); // <== Set to List
            int position = ((AlbumsFragment) albumsFragment).getPosition();
            try {
                String key = indexes.get(position);
                ArrayList<FileItem> listFilesInCurDir = mapAllFolders.get(key);
                ((FilesAlbumFragment) filesAlbumFrag).updateUI(listFilesInCurDir);
            } catch (Exception npe) {
                npe.printStackTrace();
            }
        }

        if (arrImg.size() > 0) {

            Flog.d(TAG, "arrImg before=" + listAllImgs.size());
            for (int i = 0; i < arrImg.size(); i++) {
                FileItem item = arrImg.get(i);
                Flog.d(TAG, "copy at " + ": " + item.path);
                this.listAllImgs.add(item);
            }
            Flog.d(TAG, "arrImg after=" + listAllImgs.size());

            this.mapAllImgSections = GalleryUtil.groupListSectionByDate(this.listAllImgs);
            this.mapAllImgMonthSections = GalleryUtil.groupListSectionByMonth(this.listAllImgs);
            if (isDaySection) {
                ((PhotoFragment) photoFragment).updateUI(listAllImgs, mapAllImgSections);
            } else {
                ((PhotoFragment) photoFragment).updateUI(listAllImgs, mapAllImgMonthSections);
            }

        }

        if (arrVideo.size() > 0) {

            for (int i = 0; i < arrVideo.size(); i++) {
                FileItem item = arrVideo.get(i);
                //            Flog.d(TAG, "delete at "+listSorted1.get(i)+": "+item.path);
                this.listAllVideos.add(item);
            }
            this.mapAllImgSections = GalleryUtil.groupListSectionByDate(this.listAllImgs);
            this.mapAllImgMonthSections = GalleryUtil.groupListSectionByMonth(this.listAllVideos);
            ((VideoFragment) videoFragment).updateUI(listAllVideos, mapAllImgMonthSections);
        }

        Flog.d(TAG, "size after deleted: " + this.listAllImgs.size());

        ((FavouriteFragment) favouriteFragment).refreshList();

        hideDialog();
    }

    public void refreshGallery() {
        closeToolbar();
        Flog.d(TAG, "jump to refreshGallery");
        try {

            ArrayList<FileItem> listAllFiles = new ArrayList<>();
            ArrayList<FileItem> listAllImgs = GalleryUtil.getAllImages(getActivity());
            ArrayList<FileItem> listAllVideos = GalleryUtil.getAllVideos(getActivity());


            ArrayList<FileItem> listAllMediaSdcard = new ArrayList<>();
            GalleryUtil.getAllMediaSdcard(getActivity(), listAllMediaSdcard);
            Flog.d(TAG, "re SIZE listAllMediaSdcard=" + listAllMediaSdcard.size());
            ArrayList<FileItem> listAllImgsSdcard = new ArrayList<>();
            ArrayList<FileItem> listAllVideosSdcard = new ArrayList<>();
            for (int i = 0; i < listAllMediaSdcard.size(); i++) {
                FileItem item = listAllMediaSdcard.get(i);
                if (item.isImage) {
                    listAllImgsSdcard.add(item);
                } else {
                    listAllVideosSdcard.add(item);
                }
            }
            listAllImgs.addAll(listAllImgsSdcard);
            listAllVideos.addAll(listAllVideosSdcard);
            Flog.d(TAG, "re SIZE SDCARD FILES=" + listAllImgsSdcard.size() + "_" + listAllVideosSdcard.size() + "_" + listAllMediaSdcard.size());


            listAllFiles.addAll(listAllImgs);
            listAllFiles.addAll(listAllVideos);
            Flog.d(TAG, "re SIZE ALL FILES=" + listAllImgs.size() + "_" + listAllVideos.size() + "_" + listAllFiles.size());

            /**
             * Sort all files descending based-on date-modified.
             * */
            Collections.sort(listAllFiles, new Comparator<FileItem>() {
                @Override
                public int compare(FileItem f1, FileItem f2) {

                    if (f1 == null || f2 == null || f1.date_modified == null || f2.date_modified == null) {
//                                Flog.d(TAG, "123compare = "+f1 + "_f2= "+f2);
                        return 0;
                    }

//                            Flog.d(TAG, "cmmp: f1="+f1.date_modified+"_f2="+f2.date_modified);
                    long v1 = Utils.parseLong(f1.date_modified);
                    long v2 = Utils.parseLong(f2.date_modified);
                    if (v1 == -1 || v2 == -1) {
                        return 0;
                    }

                    int cmp = 0;
                    if (v1 > v2) {
                        cmp = -1;
                    } else if (v1 < v2) {
                        cmp = 1;
                    }
                    return cmp;
                }
            });


            this.listAllFiles.clear();
            this.listAllFiles.addAll(listAllFiles);

            loadAllFolders();
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    @Override // Load all folders is finished.
    public void onGroupFilesFinished(ArrayList<FileItem> listImgs,
                                     ArrayList<FileItem> listVideos,
                                     HashMap<String, ArrayList<FileItem>> listFolders,
                                     Map<String, ArrayList<FileItem>> listImgSections,
                                     Map<String, ArrayList<FileItem>> listVideoSections,
                                     Map<String, ArrayList<FileItem>> listFileSections) {

        if (listFolders == null || listVideos == null || listImgs == null) {
            toastLoadGalleryFailed();
            return;
        }
        listAllImgs = listImgs;
        listAllVideos = listVideos;
        mapAllFolders = listFolders;
        mapAllImgSections = listImgSections;
        mapAllImgMonthSections = listVideoSections;
        mapAllFileSetions = listFileSections;
        Flog.d(TAG, "list final: imgs=" + listAllImgs.size() + "_videos=" + listAllVideos.size() + "_folders=" + mapAllFolders.size()
                + "_imgsecs=" + mapAllImgSections.size() + "_videosecs=" + mapAllImgMonthSections.size() + "_filesecs=" + mapAllFileSetions.size());

        GalleryUtil.logListFolder(mapAllFolders);
        updateViewPage();

    }

    private void updateViewPage() {
        photoFragment = (PhotoFragment) getTab(TAB_PHOTO);
        albumsFragment = (AlbumsFragment) getTab(TAB_ALBUM);
        if (photoFragment != null) {
            if (isDaySection) {
                photoFragment.updateUI(listAllImgs, mapAllImgSections);
            } else {
                photoFragment.updateUI(listAllImgs, mapAllImgMonthSections);
            }

        }
        if (albumsFragment != null) {
            albumsFragment.updateUI(mapAllFolders);
        }
        hideDialog();
    }


    private void intViewpage() {

//        hideDialog();
        binding.navigation.selectTabAtPosition(0);
        adapter = new MyViewPagerAdapter(getChildFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, listAllImgs, mapAllImgSections, mapAllFolders).setListener(this);
        binding.viewpager.setAdapter(adapter);
        binding.viewpager.setOffscreenPageLimit(2);
        binding.viewpager.disableScroll(true);
        binding.navigation.setOnTabSelectListener(tabId -> {
            switch (tabId) {
                case R.id.photo:
                    if (isToolbarAlbumShow) {
                        closeToolbarAlbum();
                    }
                    binding.viewpager.setCurrentItem(TAB_PHOTO);
                    Fragment fragment = getTab(TAB_PHOTO);
                    binding.toolbar.getMenu().clear();
                    binding.toolbar.inflateMenu(R.menu.home_photo);
                    binding.toolbar.setOnMenuItemClickListener(item -> {
                        switch (item.getItemId()) {
                            case R.id.search_photo_btn:
                                searchView = (SearchView) binding.toolbar.getMenu().findItem(R.id.search_photo_btn).getActionView();
                                searchView.setQueryHint(getString(R.string.search));
                                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                    @Override
                                    public boolean onQueryTextSubmit(String query) {
//                queryText(query);
                                        return false;
                                    }

                                    @Override
                                    public boolean onQueryTextChange(String newText) {
                                        queryText(newText, TAB_PHOTO);
                                        return false;
                                    }
                                });
                                break;
                            case R.id.favorite_photo_btn:
                                if(!isSearchViewClose()){
                                    SearchViewClose();
                                }
                                favouriteFragment = new FavouriteFragment().setListener(this);
                                addFragment(favouriteFragment, true);

                                break;
                            case R.id.setting_btn:
                                KeyboardUtil.hideKeyboard(getActivity());
                                addMainFragment(new SettingFragment());
                                break;
                            case R.id.selectItem_btn:
                                KeyboardUtil.hideKeyboard(getActivity());
                                intToolbarPhoto();
                                break;
                            case R.id.monthView_btn:
                                KeyboardUtil.hideKeyboard(getActivity());
                                isDaySection = false;
                                if (fragment instanceof PhotoFragment) {
                                    ((PhotoFragment) fragment).updateUI(listAllImgs, mapAllImgMonthSections);
                                }
                                break;
                            case R.id.dayView_btn:
                                KeyboardUtil.hideKeyboard(getActivity());
                                isDaySection = true;
                                ((PhotoFragment) fragment).updateUI(listAllImgs, mapAllImgSections);
                                break;
                        }
                        return false;
                    });

                    break;
                case R.id.folder:
                    binding.viewpager.setCurrentItem(TAB_ALBUM);
                    if (isToolbarShow) {
                        closeToolbarPhoto();
                    }
                    binding.toolbar.getMenu().clear();
                    binding.toolbar.inflateMenu(R.menu.home_album);
                    binding.toolbar.setOnMenuItemClickListener(item -> {
                        switch (item.getItemId()) {
                            case R.id.favorite_album_btn:
                                if(!isSearchViewClose()){
                                    SearchViewClose();
                                }
                                favouriteFragment = new FavouriteFragment().setListener(this);
                                addFragment(favouriteFragment, true);
                                break;
                            case R.id.search_btn:
                                searchView = (SearchView) binding.toolbar.getMenu().findItem(R.id.search_btn).getActionView();
                                searchView.setQueryHint(getString(R.string.search));
                                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                    @Override
                                    public boolean onQueryTextSubmit(String query) {
//                queryText(query);
                                        return false;
                                    }

                                    @Override
                                    public boolean onQueryTextChange(String newText) {
                                        queryText(newText, TAB_ALBUM);
                                        return false;
                                    }
                                });
                                break;
                            case R.id.selectAlbum_btn:
                                KeyboardUtil.hideKeyboard(getActivity());
                                addMainFragment(new SettingFragment());
                                break;
                            case R.id.selectItem_folder_btn:
                                KeyboardUtil.hideKeyboard(getActivity());
                                intToolbarAlbum();

                                break;
                        }
                        return false;
                    });
                    break;

            }

        });
    }

    private void intToolbarAlbum() {
        isToolbarAlbumShow = true;
        Fragment fragment = getTab(TAB_ALBUM);
        if (fragment instanceof AlbumsFragment) {
            setTextNumOfSelected(0);
            ((AlbumsFragment) fragment).setLongClickedEvent(true);
            ((AlbumsFragment) fragment).setLongClickedEvent(true);
            binding.toolbarHideFilesEdit.setVisibility(View.VISIBLE);
            binding.toolbarHideFilesEdit.inflateMenu(R.menu.home_edit_album);
            binding.toolbarHideFilesEdit.setNavigationOnClickListener(v -> {
                closeToolbarAlbum();
            });
            binding.toolbarHideFilesEdit.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.share_btn:
                            boolean shared = ((AlbumsFragment) fragment).shareAlbumsSelected();
                            if (!shared) {
                                showHintSelectImgsDialog(getString(R.string.share));
                            }
                            break;
                        case R.id.delete_btn:
                            ((AlbumsFragment) fragment).deleteAlbumsSelected(new OnFileDialogEventListener() {
                                @Override
                                public void onOk(FileItem... items) {
                                    handleBackMultiSelected();
                                    refreshFileGalleryDeleted(items);
                                }
                            });
                            break;
                        case R.id.selectAll_btn:
                            ((AlbumsFragment) fragment).selectAll();
                            setTextNumOfSelected(mapAllFolders.size());
                            break;
                        case R.id.unSelectAll_btn:
                            ((AlbumsFragment) fragment).unselectAll();
                            setTextNumOfSelected(0);
                            break;
                    }
                    return false;
                }
            });
        }

    }

    public boolean isSearchViewClose() {
        if (searchView != null) {
            return searchView.isIconified();
        } else
            return true;

    }

    public void SearchViewClose() {
        if (searchView == null)
            return;
        if (!searchView.isIconified()) {
            searchView.onActionViewCollapsed();
            binding.toolbar.collapseActionView();
//            KeyBoardHelper.hideKeyBoard(this);
        }
//        if(searchView!=null && searchView.isIconified()){
//                searchView.onActionViewCollapsed();
//        }
    }

    private void queryText(String newText, int tabPhoto) {
        if (tabPhoto == TAB_ALBUM) {
            albumsFragment.search(newText);
        } else {
            photoFragment.search(newText, isDaySection);
        }

    }

    private void openDialogInputNameAlbum() {
        LayoutInflater li = LayoutInflater.from(mContext);
        View promptsView = li.inflate(R.layout.dialog_input_filename, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        TextView titleInput = (TextView) promptsView.findViewById(R.id.textView1);
        titleInput.setText(getString(R.string.enter_album_name));

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);


        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                mInputText = userInput.getText().toString();

                                if (mInputText.length() <= 0) {
                                    Toast.makeText(mContext, getString(R.string.please_enter_name_of_album),
                                            Toast.LENGTH_SHORT).show();
                                    return;
                                }
//                                onItemAlbumClicked(-1, null);
                                KeyboardUtil.hideKeyboard(mContext, userInput);
                                dialog.cancel();
                            }
                        })
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                KeyboardUtil.hideKeyboard(mContext, userInput);
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        alertDialog = alertDialogBuilder.create();
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }

        // show it
        alertDialog.show();

        // Initially disable the button
        ((AlertDialog) alertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
                .setEnabled(false);

        // Now set the textchange listener for edittext
        userInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    // Disable ok button
                    ((AlertDialog) alertDialog).getButton(
                            AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } else {
                    // Something into edit text. Enable the button.
                    ((AlertDialog) alertDialog).getButton(
                            AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }
        });
        refreshGallery();
    }


    @Override
    public void onPhotoFragmentReady() {
//        ((PhotoFragment) photoFragment).initialize(listAllImgs, mapAllImgSections);
//        ((PhotoFragment) photoFragment).updateUI(listAllImgs, mapAllImgSections);
    }


    @Override
    public void onItemInPhotoLongClicked(FileItem file) {
        handleItemLongClicked(file);
    }


    private Fragment getTab(int position) {
        return adapter.getItem(position);
    }


    @Override
    public void onItemInPhotoClicked(FileItem file, int numOfSelected) {
        handleItemClicked(file, numOfSelected);
    }

    private void handleItemClicked(FileItem file, int numOfSelected) {
        if (file == null) {
            return;
        }
        setTextNumOfSelected(numOfSelected);

    }

    Handler handler = new Handler();

    @Override
    public void openPhotoViewer(FileItem file) {
        Flog.d(TAG, "openPhotoViewer=" + file);
        if (!isSearchViewClose()) {
            SearchViewClose();
        }

        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                photoViewerFrag = PhotoViewerFragment.newInstance(file).setListener(HomeFragment.this);
                addFragment(photoViewerFrag, true);
                PhotoViewerFragment photoViewerFragment = (PhotoViewerFragment) photoViewerFrag;
                photoViewerFragment.clearLightStatusBar(getActivity());
            }
        }, 200);


    }


    private void handleItemLongClicked(AlbumItem album) {
        if (album == null) {
            return;
        }

        intToolbarAlbum();

        setTextNumOfSelected(1);
//        Utils.scaleView(tabLayout, 1, 0);

        setHeightViewPager(heightLargeViewPager);
//        viewPager.animate().translationY(-heightTabLayout).setDuration(ConstValue.ANIM_DURATION);

        Flog.d(TAG, "onGlobalLayout height123=" + mHeightAdmobBanner);
    }

    public boolean getToolbarShow() {
        return isToolbarShow || isToolbarAlbumShow;
    }

    public void closeToolbar() {
        if (isToolbarAlbumShow) {
            closeToolbarAlbum();
        }
        if (isToolbarShow) {
            closeToolbarPhoto();
        }
    }

    public void closeToolbarPhoto() {
        isToolbarShow = false;
        Fragment fragment = getTab(TAB_PHOTO);
        if (fragment instanceof PhotoFragment) {
            binding.toolbarHideFilesEdit.getMenu().clear();
            binding.toolbarHideFilesEdit.setVisibility(View.GONE);
            ((PhotoFragment) fragment).setLongClickedEvent(false);
            ((PhotoFragment) fragment).unselectAll();
        }
    }

    public void closeToolbarAlbum() {
        isToolbarAlbumShow = false;
        Fragment fragment = getTab(TAB_ALBUM);
        if (fragment instanceof AlbumsFragment) {
            binding.toolbarHideFilesEdit.getMenu().clear();
            binding.toolbarHideFilesEdit.setVisibility(View.GONE);
            ((AlbumsFragment) fragment).setLongClickedEvent(false);
            ((AlbumsFragment) fragment).unselectAll();
        }
    }

    private void intToolbarPhoto() {
        isToolbarShow = true;
        Fragment fragment = getTab(TAB_PHOTO);
        if (fragment instanceof PhotoFragment) {
            setTextNumOfSelected(0);
            ((PhotoFragment) fragment).setLongClickedEvent(true);
            ((PhotoFragment) fragment).setLongClickedEvent(true);
            binding.toolbarHideFilesEdit.setVisibility(View.VISIBLE);
            binding.toolbarHideFilesEdit.inflateMenu(R.menu.home_edit);
            binding.toolbarHideFilesEdit.setNavigationOnClickListener(v -> {
                closeToolbarPhoto();
            });
            binding.toolbarHideFilesEdit.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.share_btn:
                            boolean shared = ((PhotoFragment) fragment).shareFilesSelected();
                            if (!shared) {
                                showHintSelectImgsDialog(getString(R.string.share));
                            }
//                            closeToolbarPhoto();
                            break;
                        case R.id.delete_btn:
                            ((PhotoFragment) photoFragment).deleteFilesSelected(new OnDialogEventListener() {
                                @Override
                                public void onOk() {
                                    refreshGallery();
                                    closeToolbarPhoto();
                                }

                                @Override
                                public void onCancel() {

                                }
                            });
                            break;
                        case R.id.move_btn:
                            if (!isSearchViewClose()) {
                                KeyboardUtil.hideKeyboard(mContext, searchView);
                            }
                            HashMap<Integer, FileItem> mapPosChanged1 = ((PhotoFragment) photoFragment).getListPositionChanged();
                            ArrayList<FileItem> listPosChanged1 = getListFromMap(mapPosChanged1);
                            if (listPosChanged1.size() <= 0) {
                                showHintSelectImgsDialog(getString(R.string.move));
                            } else {
                                openSelectAlbum(ConstValue.ACTION_MOVE, listPosChanged1);
                            }
                            break;
                        case R.id.coppy_btn:
                            if (!isSearchViewClose()) {
                                KeyboardUtil.hideKeyboard(mContext, searchView);
                            }
                            HashMap<Integer, FileItem> mapPosChanged = ((PhotoFragment) fragment).getListPositionChanged();
                            ArrayList<FileItem> listPosChanged = getListFromMap(mapPosChanged);
                            if (listPosChanged.size() <= 0) {
                                showHintSelectImgsDialog(getString(R.string.copy));
                            } else {
                                openSelectAlbum(ConstValue.ACTION_COPY, listPosChanged);
                            }
                            break;
                        case R.id.selectAll_btn:
                            ((PhotoFragment) fragment).selectAll();
                            setTextNumOfSelected(listAllImgs.size());
                            break;
                        case R.id.unSelectAll_btn:
                            ((PhotoFragment) fragment).unselectAll();
                            setTextNumOfSelected(0);
                            break;
                    }
                    return false;
                }
            });
        }


    }

    private void handleItemLongClicked(FileItem file) {
        if (file == null) {
            return;
        }
        intToolbarPhoto();
        setTextNumOfSelected(1);
//        Utils.scaleView(tabLayout, 1, 0);

        setHeightViewPager(heightLargeViewPager);
//        viewPager.animate().translationY(-heightTabLayout).setDuration(ConstValue.ANIM_DURATION);

        Flog.d(TAG, "onGlobalLayout height123=" + mHeightAdmobBanner);
    }

    private void setTextNumOfSelected(int numOfSelected) {
        if (numOfSelected <= 0) {
            binding.tvTitleFileEdit.setText("0");
        } else {
            binding.tvTitleFileEdit.setText("" + numOfSelected);
        }
    }


    @Override
    public void onAlbumsFragmentReady() {
//        ((AlbumsFragment) albumsFragment).initialize(mapAllFolders);
//        ((AlbumsFragment) albumsFragment).updateUI(mapAllFolders);
    }

    @Override
    public void onOpenAlbumViewer(String nameAlbum, ArrayList<FileItem> listFiles) {
        if (!isSearchViewClose()) {
            SearchViewClose();
        }
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                filesAlbumFrag = FilesAlbumFragment.newInstance(listFiles, nameAlbum).setListener(HomeFragment.this);
                addFragment(filesAlbumFrag, true);
            }
        }, 200);

    }

    @Override
    public void onItemAlbumLongClicked(AlbumItem album) {
        handleItemLongClicked(album);
    }

    @Override
    public void onItemAlbumClicked(AlbumItem album, int numOfSelected) {
        setTextNumOfSelected(numOfSelected);
    }

    private void showAdDialogApps() {
    }

    private void showPopupMulAlbumDialog() {

        // mis-clicking prevention, using threshold of 1000 ms
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        // do your magic here

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        // add a list
        String[] animals = {getString(R.string.share), getString(R.string.select_all),
                getString(R.string.unselect_all)};
        builder.setItems(animals, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // share
                        actionShareFiles();
                        break;
                    case 1: // select all
                        actionSelectAll();
                        break;
                    case 2: // unselect all
                        actionUnselectAll();
                        break;
                }
                dialog.dismiss();
            }
        });

        // create and open the alert dialogPopup
        AlertDialog dialogPopup = builder.create();

        Window window = dialogPopup.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.TOP | Gravity.RIGHT;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        dialogPopup.show();
    }


    private void handleDeleteMultiSelected() {
//        if (tabLayout.getSelectedTabPosition() == 0) { // Tab Photos
//
//            if (!FLAG_REFRESH_OPTIMIZE) {
//                ((PhotoFragment) photoFragment).deleteFilesSelected(new OnDialogEventListener() {
//                    @Override
//                    public void onOk() {
//                        handleBackMultiSelected();
//                        refreshGallery();
//                    }
//
//                    @Override
//                    public void onCancel() {
//
//                    }
//                });
//            } else {
//                ((PhotoFragment) photoFragment).deleteFilesSelected(new OnFileDialogEventListener() {
//                    @Override
//                    public void onOk(FileItem... items) {
//                        handleBackMultiSelected();
//                        refreshFileGalleryDeleted(items);
//                    }
//                });
//            }
//        } else if (tabLayout.getSelectedTabPosition() == 1) { // Tab Videos
//
//
//        } else if (tabLayout.getSelectedTabPosition() == 2) { // Tab Albums
//
//            ((AlbumsFragment) albumsFragment).deleteAlbumsSelected(new OnFileDialogEventListener() {
//                @Override
//                public void onOk(FileItem... items) {
//                    handleBackMultiSelected();
//                    refreshFileGalleryDeleted(items);
//                }
//            });
//        } else if (tabLayout.getSelectedTabPosition() == 3) { // Tab Favourite
//
//            if (!FLAG_REFRESH_OPTIMIZE) {
//                ((FavouriteFragment) favouriteFragment).deleteFilesSelected(new OnDialogEventListener() {
//                    @Override
//                    public void onOk() {
//                        handleBackMultiSelected();
//                        refreshGallery();
//                    }
//
//                    @Override
//                    public void onCancel() {
//
//                    }
//                });
//            } else {
//                ((FavouriteFragment) favouriteFragment).deleteFilesSelected(new OnFileDialogEventListener() {
//                    @Override
//                    public void onOk(FileItem... items) {
//                        handleBackMultiSelected();
//                        refreshFileGalleryDeleted(items);
//                    }
//                });
//            }
//        }
    }

    private void showPopupMulFileDialog() {
        if (dialogPopupMulFile != null && !dialogPopupMulFile.isShowing()) {
            dialogPopupMulFile.show();
        }
    }

    private void showPopupFileDialog(int type, final FileItem fileItem) {

        // mis-clicking prevention, using threshold of 1000 ms
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        // do your magic here

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        // add a list
        String[] items = null;

        if (type == BaseFragment.FRAGMENT_PHOTO_VIEWER) {
            items = new String[]{getString(R.string.details),
                    getString(R.string.rename), getString(R.string.open_with),
                    getString(R.string.set_picture_as)};
        } else if (type == BaseFragment.FRAGMENT_VIDEO_VIEWER) {
            items = new String[]{getString(R.string.details),
                    getString(R.string.rename), getString(R.string.open_with)};
        }

        if (fileItem.path.endsWith(".gif")) {
            items = new String[]{getString(R.string.details),
                    getString(R.string.rename), getString(R.string.open_with)};
        }

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // details
                        actionDetails(fileItem);
                        break;
                    case 1: // rename
                        actionRename(fileItem);
                        break;
                    case 2: // open with
                        actionOpenWith(fileItem);
                        break;
                    case 3: // set picture as
                        actionSetPictureAs(fileItem);
                        break;
                }
                dialog.dismiss();
            }
        });

        // create and open the alert dialogPopupMulFile
        dialogPopupFile = builder.create();

        Window window = dialogPopupFile.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.TOP | Gravity.RIGHT;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        if (dialogPopupFile != null && !dialogPopupFile.isShowing()) {
            dialogPopupFile.show();
        }
    }

    private void actionEditPhoto(FileItem fileItem) {

        Intent intent = new Intent(mContext, EditPhotoActivity.class);
        intent.putExtra(ConstValue.EXTRA_FILE_ITEM_TO_EDIT, fileItem);
        startActivityForResult(intent, ConstValue.REQUEST_CODE_EDIT_PHOTO_ACTIVITY);
    }


    private void actionRename(FileItem fileItem) {
        openDialogInputName(fileItem);
    }

    private boolean checkFileName(String newTitle, FileItem fileItem) {
        boolean check = false;
        if (listAllFiles.size() > 0) {
            for (int i = 0; i < listAllFiles.size(); i++) {
                if ((new File(listAllFiles.get(i).path).getParent().equals(new File(fileItem.path).getParent()))) {
                    Flog.d("CCCCCCC", "name1: " + listAllFiles.get(i).name + "___" + newTitle + "___Finame: "+fileItem.name);
                    if (newTitle.equals(listAllFiles.get(i).name)) {
                        Flog.d("CCCCCCC", "name2: " + listAllFiles.get(i).name + "___" + newTitle + "___Finame: "+fileItem.name);
                        check = true;
                        break;
                    }
                }
            }
        }
        return check;
    }

    private void openDialogInputName(final FileItem fileItem) {

        LayoutInflater li = LayoutInflater.from(mContext);
        View promptsView = li.inflate(R.layout.dialog_input_filename, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);
        userInput.setText(fileItem.name);
        userInput.selectAll();

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text


                                try {

                                    String input = userInput.getText().toString().trim();

                                    if (input.length() <= 0) {
                                        return;
                                    }
                                    boolean success = false;
                                    if (!checkFileName(input, fileItem)) {
                                        success = FileUtil.rename(mContext, new File(fileItem.path), input + FileUtil.getExtension(fileItem.path));
                                    }else {

                                    }
                                    String path = fileItem.path;
                                    String newPath = new File(path).getParent() + File.separator + input + FileUtil.getExtension(path);
//                                        success = FileUtil.rename(mContext, new File(path), input + FileUtil.getExtension(path));


                                    if (success) {
                                        if (fileItem.isImage) {
                                            ((PhotoViewerFragment) photoViewerFrag).updateUI(input, newPath);
                                        } else {
                                            ((VideoViewerFragment) videoViewerFrag).updateUI(input, newPath);
                                        }
                                        FileUtil.scanMediaStore(mContext, newPath);
                                        if (!FLAG_REFRESH_ENABLE) {
                                            refreshGallery();
                                        }
                                        FileUtil.toastSuccess(mContext, getString(R.string.rename));
                                    } else {
                                        FileUtil.toastFailed(mContext, getString(R.string.rename));
                                    }

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    FileUtil.toastFailed(mContext, getString(R.string.rename));
                                }


                                KeyboardUtil.hideKeyboard(mContext, userInput);
                            }
                        })
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                KeyboardUtil.hideKeyboard(mContext, userInput);
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        alertDialog = alertDialogBuilder.create();
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }

        // show it
        alertDialog.show();

        // Initially disable the button
        ((AlertDialog) alertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
                .setEnabled(false);

        // Now set the textchange listener for edittext
        userInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    // Disable ok button
                    ((AlertDialog) alertDialog).getButton(
                            AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } else {
                    // Something into edit text. Enable the button.
                    ((AlertDialog) alertDialog).getButton(
                            AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }
        });
    }

    private void actionSetPictureAs(FileItem fileItem) {
        Flog.d(TAG, "actionOpenWith=");
        if (photoViewerFrag != null) {
            ((PhotoViewerFragment) photoViewerFrag).showBottomDialog();
        }
//        FileUtil.setPictureAs(mContext, fileItem.path);
    }

    private void actionOpenWith(FileItem fileItem) {

        FLAG_OPEN_WITH_ACTION = FLAG_MODE_ENABLE;
        FilesAlbumFragment.FLAG_SHARE_IN_FILEALBUM = FLAG_MODE_DISABLE;
        Flog.d(TAG, "actionOpenWith=" + FLAG_RELOAD_GALLERY);
        if (fileItem.isImage) {
            FileUtil.openPhotoIntent(mContext, fileItem.path);
        } else {
            FileUtil.openVideoIntent(mContext, fileItem.path);
        }
    }

    private void actionDetails(FileItem fileItem) {
        openDialogDetailsFile(fileItem);
    }

    private void openDialogDetailsFile(FileItem fileItem) {

        LayoutInflater li = LayoutInflater.from(mContext);
        View promptsView = li.inflate(R.layout.dialog_details_file, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final LinearLayout lnInfoContainer = promptsView.findViewById(R.id.info_container);

        addText(lnInfoContainer, getString(R.string.filename), fileItem.name);
        addText(lnInfoContainer, getString(R.string.last_modified),
                DateUtils.getDate(Utils.parseLong(fileItem.date_modified), DateUtils.FORMAT_DATE_2));

        Flog.d(TAG, "size of file=" + fileItem.size);
        addText(lnInfoContainer, getString(R.string.size),
                FileUtil.formatSize(Utils.parseLong(fileItem.size)));
        addText(lnInfoContainer, getString(R.string.filetype), fileItem.mime_type);
        addText(lnInfoContainer, getString(R.string.width), fileItem.width);
        addText(lnInfoContainer, getString(R.string.height), fileItem.height);
        if (!fileItem.isImage) {
            addText(lnInfoContainer, getString(R.string.duration),
                    (fileItem.duration == null ? null : Utils.milliSecondsToTimer(Utils.parseLong(fileItem.duration))));
        } else {
            addText(lnInfoContainer, getString(R.string.orientation),
                    fileItem.orientation == null ? null : (fileItem.orientation + "°"));
        }
        addText(lnInfoContainer, getString(R.string.path), fileItem.path);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    private void addText(LinearLayout lnInfoContainer, String label, String text) {
        if (text == null || text.equals("null"))
            return;
        LinearLayout.LayoutParams parentParams = (LinearLayout.LayoutParams) lnInfoContainer.getLayoutParams();

        TextView textView = new TextView(mContext);
        textView.setLayoutParams(parentParams);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textView.getLayoutParams();
        params.setMargins(0, 0, 0, (int) getResources().getDimension(R.dimen.margin_small_size));
        String sourceString = "<b>" + label + ": " + "</b> " + text;
        textView.setText(Html.fromHtml(sourceString));

        lnInfoContainer.addView(textView);
    }

    private void initPopupMulFileDialog() {
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
                        actionCopyFiles();
                        break;
                    case 2: // move
                        actionMoveFiles();
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

        // create and open the alert dialogPopupMulFile
        dialogPopupMulFile = builder.create();

        Window window = dialogPopupMulFile.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.TOP | Gravity.RIGHT;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
    }

    private void actionMoveFiles() {
//        if (tabLayout.getSelectedTabPosition() == 0) { // Tab Photos
//
//            HashMap<Integer, FileItem> mapPosChanged = ((PhotoFragment) photoFragment).getListPositionChanged();
//            ArrayList<FileItem> listPosChanged = getListFromMap(mapPosChanged);
//            if (listPosChanged.size() <= 0) {
//                showHintSelectImgsDialog(getString(R.string.move));
//            } else {
//                openSelectAlbum(ConstValue.ACTION_MOVE, listPosChanged);
//            }
//        } else if (tabLayout.getSelectedTabPosition() == 1) { // Tab Videos
//
//            HashMap<Integer, FileItem> mapPosChanged = ((VideoFragment) videoFragment).getListPositionChanged();
//            ArrayList<FileItem> listPosChanged = getListFromMap(mapPosChanged);
//            if (listPosChanged.size() <= 0) {
//                showHintSelectImgsDialog(getString(R.string.move));
//            } else {
//                openSelectAlbum(ConstValue.ACTION_MOVE, listPosChanged);
//            }
//        } else if (tabLayout.getSelectedTabPosition() == 2) { // Tab Albums
//
//        } else if (tabLayout.getSelectedTabPosition() == 3) { // Tab Favourite
//            HashMap<Integer, FileItem> mapPosChanged = ((FavouriteFragment) favouriteFragment).getListPositionChanged();
//            ArrayList<FileItem> listPosChanged = getListFromMap(mapPosChanged);
//            if (listPosChanged.size() <= 0) {
//                showHintSelectImgsDialog(getString(R.string.move));
//            } else {
//                openSelectAlbum(ConstValue.ACTION_MOVE, listPosChanged);
//            }
//        }
    }

    private void actionCopyFiles() {
//        if (tabLayout.getSelectedTabPosition() == 0) { // Tab Photos
//
//            HashMap<Integer, FileItem> mapPosChanged = ((PhotoFragment) photoFragment).getListPositionChanged();
//            ArrayList<FileItem> listPosChanged = getListFromMap(mapPosChanged);
//            if (listPosChanged.size() <= 0) {
//                showHintSelectImgsDialog(getString(R.string.copy));
//            } else {
//                openSelectAlbum(ConstValue.ACTION_COPY, listPosChanged);
//            }
//        } else if (tabLayout.getSelectedTabPosition() == 1) { // Tab Videos
//
//            HashMap<Integer, FileItem> mapPosChanged = ((VideoFragment) videoFragment).getListPositionChanged();
//            ArrayList<FileItem> listPosChanged = getListFromMap(mapPosChanged);
//            if (listPosChanged.size() <= 0) {
//                showHintSelectImgsDialog(getString(R.string.copy));
//            } else {
//                openSelectAlbum(ConstValue.ACTION_COPY, listPosChanged);
//            }
//        } else if (tabLayout.getSelectedTabPosition() == 2) { // Tab Albums
//
//        } else if (tabLayout.getSelectedTabPosition() == 3) { // Tab Favourite
//
//            HashMap<Integer, FileItem> mapPosChanged = ((FavouriteFragment) favouriteFragment).getListPositionChanged();
//            ArrayList<FileItem> listPosChanged = getListFromMap(mapPosChanged);
//            if (listPosChanged.size() <= 0) {
//                showHintSelectImgsDialog(getString(R.string.copy));
//            } else {
//                openSelectAlbum(ConstValue.ACTION_COPY, listPosChanged);
//            }
//        }
    }

    private void showHintSelectImgsDialog(String action) {
        Toast.makeText(mContext, getString(R.string.please_select_media_for)
                + " " + action, Toast.LENGTH_LONG).show();
    }

    private void openSelectAlbum(int typeAction, ArrayList<FileItem> listPosChanged) {
        selectAlbumFrag = SelectAlbumFragment.newInstance(mapAllFolders, listPosChanged, typeAction).setListener(this);
        addFragment(selectAlbumFrag, false);
    }

    private ArrayList<FileItem> getListFromMap(HashMap<Integer, FileItem> mapPosChanged) {
        ArrayList<FileItem> list = new ArrayList<>();
        for (Map.Entry<Integer, FileItem> entry : mapPosChanged.entrySet()) {
            FileItem fileItem = entry.getValue();
            list.add(fileItem);
        }
        return list;
    }

    private void actionUnselectAll() {
//
//        if (tabLayout.getSelectedTabPosition() == 0) { // Tab Photos
//
//            ((PhotoFragment) photoFragment).unselectAll();
//        } else if (tabLayout.getSelectedTabPosition() == 1) { // Tab Videos
//
//            ((VideoFragment) videoFragment).unselectAll();
//        } else if (tabLayout.getSelectedTabPosition() == 2) { // Tab Albums
//
//            ((AlbumsFragment) albumsFragment).unselectAll();
//        } else if (tabLayout.getSelectedTabPosition() == 3) { // Tab Favourite
//
//            ((FavouriteFragment) favouriteFragment).unselectAll();
//        }
//
//        setTextNumOfSelected(0);
    }

    private void actionSelectAll() {

//        if (tabLayout.getSelectedTabPosition() == 0) { // Tab Photos
//
//            ((PhotoFragment) photoFragment).selectAll();
//            setTextNumOfSelected(listAllImgs.size());
//        } else if (tabLayout.getSelectedTabPosition() == 1) { // Tab Videos
//
//            ((VideoFragment) videoFragment).selectAll();
//            setTextNumOfSelected(listAllVideos.size());
//        } else if (tabLayout.getSelectedTabPosition() == 2) { // Tab Albums
//
//            ((AlbumsFragment) albumsFragment).selectAll();
//            setTextNumOfSelected(mapAllFolders.size());
//        } else if (tabLayout.getSelectedTabPosition() == 3) { // Tab Favourite
//
//            ((FavouriteFragment) favouriteFragment).selectAll();
//            setTextNumOfSelected(listAllVideos.size());
//        }
    }

    private void actionShareFiles() {
//
//        if (tabLayout.getSelectedTabPosition() == 0) { // Tab Photos
//
//            boolean shared = ((PhotoFragment) photoFragment).shareFilesSelected();
//            if (!shared) {
//                showHintSelectImgsDialog(getString(R.string.share));
//            }
//        } else if (tabLayout.getSelectedTabPosition() == 1) { // Tab Videos
//
//            boolean shared = ((VideoFragment) videoFragment).shareFilesSelected();
//            if (!shared) {
//                showHintSelectImgsDialog(getString(R.string.share));
//            }
//        } else if (tabLayout.getSelectedTabPosition() == 2) { // Tab Albums
//
//            boolean shared = ((AlbumsFragment) albumsFragment).shareAlbumsSelected();
//            if (!shared) {
//                showHintSelectImgsDialog(getString(R.string.share));
//            }
//        } else if (tabLayout.getSelectedTabPosition() == 3) { // Tab Favourite
//
//            boolean shared = ((FavouriteFragment) favouriteFragment).shareFilesSelected();
//            if (!shared) {
//                showHintSelectImgsDialog(getString(R.string.share));
//            }
//        }
    }

    private void handleBackMultiSelected() {
//
//        if (tabLayout.getSelectedTabPosition() == 0) { // Tab Photos
//
//            ((PhotoFragment) photoFragment).setLongClickedEvent(false);
//            ((PhotoFragment) photoFragment).unselectAll();
//        } else if (tabLayout.getSelectedTabPosition() == 1) { // Tab Videos
//
//            ((VideoFragment) videoFragment).setLongClickedEvent(false);
//            ((VideoFragment) videoFragment).unselectAll();
//        } else if (tabLayout.getSelectedTabPosition() == 2) { // Tab Albums
//
//            ((AlbumsFragment) albumsFragment).setLongClickedEvent(false);
//            ((AlbumsFragment) albumsFragment).unselectAll();
//        } else if (tabLayout.getSelectedTabPosition() == 3) { // Tab Favourite
//            ((FavouriteFragment) favouriteFragment).setLongClickedEvent(false);
//            ((FavouriteFragment) favouriteFragment).unselectAll();
//        }
//
//        showMyToolbar(true);
//        Utils.scaleView(tabLayout, 0, 1);
//
//        setHeightViewPager(heightNormalViewPager);
//        viewPager.animate().translationY(0).setDuration(ConstValue.ANIM_DURATION);
    }

    private void setHeightViewPager(int heightViewPager) {
//        ViewGroup.LayoutParams params = viewPager.getLayoutParams();
//        params.height = heightViewPager;
//        viewPager.requestLayout();
    }


//    @Override
//    protected void onResume() {
//        if (FLAG_RELOAD_GALLERY) {
//            Flog.d(TAG, "refresh lazy!!!");
//            refreshGallery();
//        }
//        FLAG_RELOAD_GALLERY = false;
//        super.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        Flog.d(TAG, "onPause=" + FLAG_RELOAD_GALLERY);
//        setFlags();
//
//        super.onPause();
//    }


    private void initAdmob() {
    }


    private void showVersionDialog(Activity context, String version) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.version))
                .setMessage(version)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.dismiss();
                    }
                });
//                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                    public void onClick(final DialogInterface dialogPopupMulFile, @SuppressWarnings("unused") final int id) {
//                        dialogPopupMulFile.dismiss();
//                    }
//                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onBackFileAlbumsFragment() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void openFileViewer(FileItem file) {

        if (!isSearchViewClose()) {
            SearchViewClose();
        }

        if (file.isImage) {
            openPhotoViewer(file);
        } else {
            openVideoViewer(file);
        }

    }

    @Override
    public void onDeleteFileAlbumFragment() {
        if (!FLAG_REFRESH_OPTIMIZE) {
            ((FilesAlbumFragment) filesAlbumFrag).deleteFilesSelected(new OnDialogEventListener() {
                @Override
                public void onOk() {
                    refreshGallery();
                    if (filesAlbumFrag != null) {
                        filesAlbumFrag.closeToolbar();
                    }
                }

                @Override
                public void onCancel() {

                }
            });
        } else {
            ((FilesAlbumFragment) filesAlbumFrag).deleteFilesSelected(new OnFileDialogEventListener() {
                @Override
                public void onOk(FileItem... items) {
                    refreshFileGalleryDeleted(items);
                    if (filesAlbumFrag != null) {
                        filesAlbumFrag.closeToolbar();
                    }
                }
            });
        }
    }

    @Override
    public void onDeleteAlbum() {
        if (!FLAG_REFRESH_OPTIMIZE) {
            ((FilesAlbumFragment) filesAlbumFrag).deleteCurAlbum(new OnDialogEventListener() {
                @Override
                public void onOk() {
                    onBackFileAlbumsFragment();
                    refreshGallery();
                }

                @Override
                public void onCancel() {

                }
            });
        } else {
            ((FilesAlbumFragment) filesAlbumFrag).deleteCurAlbum(new OnFileDialogEventListener() {
                @Override
                public void onOk(FileItem... items) {
                    Flog.d(TAG, "action back fulfilled");
                    onBackFileAlbumsFragment();
                    refreshFileGalleryDeleted(items);
                }
            });
        }
    }

    @Override
    public void onCopyFileAlbumFragment(ArrayList<FileItem> listFromMap) {

        if (listFromMap.size() <= 0) {
            showHintSelectImgsDialog(getString(R.string.copy));
        } else {
            openSelectAlbum(ConstValue.ACTION_COPY, listFromMap);
            if (filesAlbumFrag != null) {
                filesAlbumFrag.closeToolbar();
            }
        }
    }

    @Override
    public void onMoveFileAlbumFragment(ArrayList<FileItem> listFromMap) {

        if (listFromMap.size() <= 0) {
            showHintSelectImgsDialog(getString(R.string.move));
        } else {
            openSelectAlbum(ConstValue.ACTION_MOVE, listFromMap);
            if (filesAlbumFrag != null) {
                filesAlbumFrag.closeToolbar();
            }
        }
    }

    @Override
    public void onPhotoViewerFragmentReady() {
        PhotoViewerFragment photoViewerFragment = (PhotoViewerFragment) this.photoViewerFrag;
        photoViewerFragment.setValues();
    }

    @Override
    public void onBackPhotoViewerFragment() {
        getFragmentManager().popBackStack();
        setLightStatusBar();
//        if (FLAG_REFRESH_ENABLE) {
//            refreshGallery();
//        }
//        showOptFrag(parentCurFragID);
    }

    @Override
    public void onMorePhotoViewerFragment(FileItem fileItem) {
        showPopupFileDialog(BaseFragment.FRAGMENT_PHOTO_VIEWER, fileItem);
    }

    @Override
    public void onSharePhotoViewerFragment(FileItem fileItem) {
        FileUtil.share(mContext, FileUtil.getUrifromFile(mContext, fileItem.path));
    }

    @Override
    public void onDeletePhotoViewerFragment(final FileItem fileItem) {
        showDeleteDialog(mContext,
                new OnFileDialogEventListener() {
                    @Override
                    public void onOk(FileItem... items) {
                        onBackPhotoViewerFragment();
                        if (!FLAG_REFRESH_ENABLE) {
                            Flog.d(TAG, "onDeletePhotoViewerFragment");
                            if (!FLAG_REFRESH_OPTIMIZE) {
                                refreshGallery();
                            } else {
                                refreshFileGalleryDeleted(items);
                            }
                        }
                    }
                },
                fileItem);
    }

    @Override
    public void onCreateFileToFavouriteAlbum(FileItem fileItem) {


        String srcPath = fileItem.path;

        String nameNewFile = fileItem.name + FileUtil.getExtension(fileItem.path);
        String dstPath = PhotoViewerFragment.PATH_TO_FAVOURITE_FOLDER + File.separator + nameNewFile;
        Flog.d(TAG, "favourite " + ": src=" + srcPath + "\n\tdst=" + dstPath);

        boolean success = true;
        boolean exists = FileUtil.isFileExistsInMediaStore(mContext, dstPath);
        Flog.d(TAG, "eexists=" + exists);
        if (exists) {
            FileUtil.delete(mContext, Uri.fromFile(new File(dstPath)));
        }

        try {
            success &= FileUtil.copy(mContext, new File(srcPath), new File(dstPath), fileItem);
        } catch (IOException e) {
            e.printStackTrace();
            success &= false;
        }
        Flog.d(TAG, "favourited " + success);
    }

    @Override
    public void onDeleteFileToFavouriteAlbum(FileItem fileItem) {

        String nameNewFile = fileItem.name + FileUtil.getExtension(fileItem.path);
        String dstPath = PhotoViewerFragment.PATH_TO_FAVOURITE_FOLDER + File.separator + nameNewFile;
        boolean success = FileUtil.delete(mContext, Uri.fromFile(new File(dstPath)));
        Flog.d(TAG, "favourited removed " + success);
    }

    @Override
    public void onUpdateToFavouriteAlbum() {
        if (favouriteFragment != null) {
            ((FavouriteFragment) favouriteFragment).refreshList();
        }

    }

    private void showDeleteDialog(final Context context,
                                  final OnFileDialogEventListener listener, final FileItem... fileItem) {

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

                        int len = fileItem.length;
                        Uri[] uries = new Uri[len];
                        String[] paths = new String[len];

                        ArrayList<FileItem> list = new ArrayList<>();
                        String mes = "";
                        for (int i = 0; i < len; i++) {
                            FileItem item = fileItem[i];
                            uries[i] = Uri.fromFile(new File(item.path));
                            paths[i] = item.path;

                            boolean success = FileUtil.delete(context, uries[i]);
                            Flog.d(TAG, "success ATAA=" + success);
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

                        FileItem[] items = new FileItem[list.size()];
                        for (int i = 0; i < list.size(); i++) {
                            items[i] = list.get(i);
                        }
                        if (listener != null) {
                            listener.onOk(items);
                        }
                    }
                }).show();
    }


    @Override
    public void onSelectAlbumFragmentReady() {
        closeToolbar();
//        ((SelectAlbumFragment) selectAlbumFrag).initialize(mapAllFolders);
    }

    @Override
    public void onBackSelectAlbumFragment() {
//        handleBackMultiSelected();
//        refreshGallery();
        if (getFragmentManager() != null) {
            getFragmentManager().popBackStack();
        }
        closeToolbarPhoto();
    }

    @Override
    public void onUpdateMainUI(int typeAction, FileItem[] items) {
        if (false) {
            if (typeAction == ConstValue.ACTION_COPY) {
                refreshFileGalleryCopied(items);
            } else if (typeAction == ConstValue.ACTION_MOVE) {
                refreshGallery();
            }
        } else {
            refreshGallery();
        }
    }

    @Override
    public void onVideoFragmentReady() {

    }

    @Override
    public void onItemInVideoLongClicked(FileItem file) {

    }

    @Override
    public void onItemInVideoClicked(FileItem file, int numOfSelected) {

    }

    @Override
    public void openVideoViewer(FileItem file) {
        if (!isSearchViewClose()) {
            SearchViewClose();
        }

        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Flog.d(TAG, "openPhotoViewer=" + file);
                videoViewerFrag = VideoViewerFragment.newInstance(file).setListener(HomeFragment.this);
                addFragment(videoViewerFrag, true);
                VideoViewerFragment videoViewerFragment = (VideoViewerFragment) videoViewerFrag;
                videoViewerFragment.clearLightStatusBar(getActivity());
            }
        }, 200);

    }

    @Override
    public void onCreateNewFolderListener(String name) {
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + name);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            CustomToast.showContent(mContext, getResources().getString(R.string.add_folder_success));
        } else {
            CustomToast.showContent(mContext, getString(R.string.file_name_exist));
        }
        onUpDateAlbum();
    }

    private void onUpDateAlbum() {

    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemInFavouriteLongClicked(FileItem fileItem) {

    }

    @Override
    public void onItemInFavouriteClicked(FileItem fileItem, int size) {

    }

    @Override
    public void openFileFavouriteViewer(FileItem file) {
        if (file.isImage) {
            openPhotoViewer(file);
        } else {
            openVideoViewer(file);
        }
    }
}
