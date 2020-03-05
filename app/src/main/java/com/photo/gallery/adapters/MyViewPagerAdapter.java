package com.photo.gallery.adapters;

import android.annotation.SuppressLint;
import android.os.Bundle;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.photo.gallery.fragments.AlbumsFragment;
import com.photo.gallery.fragments.PhotoFragment;
import com.photo.gallery.fragments.VideoFragment;
import com.photo.gallery.fragments.VideoViewerFragment;
import com.photo.gallery.models.AlbumItem;
import com.photo.gallery.models.FileItem;
import com.photo.gallery.utils.ConstValue;
import com.photo.gallery.utils.Flog;

public class MyViewPagerAdapter extends FragmentPagerAdapter implements PhotoFragment.OnPhotoListener, AlbumsFragment.OnAlbumsListener, VideoFragment.OnVideoListener {
    static final int TOTAL_TAB = 3;
    public static final int TAB_PHOTO = 0;
    public static final int TAB_VIDEO = 1;
    public static final int TAB_ALBUM = 2;
    private onPhotoListener listener;
    private ArrayList<FileItem> mListAllImgs = new ArrayList<>();
    private ArrayList<FileItem> mListAllVideos = new ArrayList<>();
    private Map<String, ArrayList<FileItem>> mListAllImgSections = new HashMap<>();
    private Map<String, ArrayList<FileItem>> mListAllVideosSections = new HashMap<>();
    private HashMap<String, ArrayList<FileItem>> mListFolders;
    @SuppressLint("UseSparseArrays")
    private HashMap<Integer, Fragment> maps = new HashMap<>();

    public MyViewPagerAdapter(@NonNull FragmentManager fm, int behavior, ArrayList<FileItem> fileItems,
                              Map<String, ArrayList<FileItem>> mListAllImgSections,
                              HashMap<String, ArrayList<FileItem>> mListFolders,
                              ArrayList<FileItem> fileVideo,
                              Map<String, ArrayList<FileItem>> mListAllVideosSections) {
        super(fm, behavior);
        this.mListAllImgs = fileItems;
        this.mListAllImgSections = mListAllImgSections;
        this.mListFolders = mListFolders;
        this.mListAllVideos = fileVideo;
        this.mListAllVideosSections = mListAllVideosSections;
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case TAB_PHOTO:
                if (maps.get(position) == null) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(ConstValue.EXTRA_LIST_ALL_FILES, this.mListAllImgs);
                    bundle.putSerializable(ConstValue.EXTRA_LIST_ALL_MAP, (Serializable) this.mListAllImgSections);
                    fragment = PhotoFragment.newInstance(bundle).setListener(this);
                    maps.put(position, fragment);
                }
                break;
            case TAB_VIDEO:
                if (maps.get(position) == null) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(ConstValue.EXTRA_LIST_ALL_FILES, this.mListAllVideos);
                    bundle.putSerializable(ConstValue.EXTRA_LIST_ALL_MAP, (Serializable) this.mListAllVideosSections);
                    fragment = VideoFragment.newInstance(bundle).setListener(this);
                    maps.put(position, fragment);
                }
                break;
            case TAB_ALBUM:
                if (maps.get(position) == null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ConstValue.EXTRA_LIST_ALL_MAP, this.mListFolders);
                    fragment = AlbumsFragment.newInstance(bundle).setListener(this);
                    maps.put(position, fragment);
                }
                break;

        }
        return maps.get(position);
    }

    public MyViewPagerAdapter setListener(onPhotoListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public int getCount() {
        return TOTAL_TAB;
    }


    @Override
    public void onItemInVideoLongClicked(FileItem file) {
        listener.onItemInVideoLongClicked(file);
    }

    @Override
    public void onItemInVideoClicked(FileItem file, int numOfSelected) {
        listener.onItemInVideoClicked(file,numOfSelected);
    }

    @Override
    public void openVideoViewer(FileItem file) {
        listener.openVideoViewer(file);
    }

    public interface onPhotoListener {

        void onItemInVideoLongClicked(FileItem file);

        void onItemInVideoClicked(FileItem file, int numOfSelected);

        void openVideoViewer(FileItem file);



        void onItemInPhotoLongClicked(FileItem fileItem);

        void openPhotoViewer(FileItem fileItem);

        void onItemInPhotoClicked(FileItem file, int numOfSelected);


        void onOpenAlbumViewer(String nameAlbum, ArrayList<FileItem> listFiles);

        void onItemAlbumLongClicked(AlbumItem album);

        void onItemAlbumClicked(AlbumItem album, int numOfSelected);

    }


    @Override
    public void onItemInPhotoLongClicked(FileItem file) {
        listener.onItemInPhotoLongClicked(file);
    }

    @Override
    public void onItemInPhotoClicked(FileItem file, int numOfSelected) {
        listener.onItemInPhotoClicked(file, numOfSelected);
    }

    @Override
    public void openPhotoViewer(FileItem file) {
        listener.openPhotoViewer(file);
    }


    @Override
    public void onOpenAlbumViewer(String nameAlbum, ArrayList<FileItem> listFiles) {
        listener.onOpenAlbumViewer(nameAlbum, listFiles);
    }

    @Override
    public void onItemAlbumLongClicked(AlbumItem album) {
        listener.onItemAlbumLongClicked(album);
    }

    @Override
    public void onItemAlbumClicked(AlbumItem album, int numOfSelected) {
        listener.onItemAlbumClicked(album, numOfSelected);
    }
}
