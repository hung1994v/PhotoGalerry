package com.photo.gallery.adapters;

import android.annotation.SuppressLint;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.photo.gallery.fragments.AlbumsFragment;
import com.photo.gallery.fragments.PhotoFragment;
import com.photo.gallery.models.AlbumItem;
import com.photo.gallery.models.FileItem;
import com.photo.gallery.utils.Flog;

public class MyViewPagerAdapter extends FragmentPagerAdapter implements PhotoFragment.OnPhotoListener, AlbumsFragment.OnAlbumsListener {
    static final int TOTAL_TAB = 2;
    public static final int TAB_PHOTO = 0;
    public static final int TAB_ALBUM = 1;
    private onPhotoListener listener;
    private ArrayList<FileItem> mListAllImgs = new ArrayList<>();
    private Map<String, ArrayList<FileItem>> mListAllImgSections = new HashMap<>() ;
    private HashMap<String, ArrayList<FileItem>> mListFolders;
    @SuppressLint("UseSparseArrays")
    private HashMap<Integer, Fragment> maps = new HashMap<>();

    public MyViewPagerAdapter(@NonNull FragmentManager fm, int behavior,ArrayList<FileItem> fileItems, Map<String, ArrayList<FileItem>> mListAllImgSections,HashMap<String, ArrayList<FileItem>> mListFolders) {
        super(fm, behavior);
        this.mListAllImgs = fileItems;
        this.mListAllImgSections = mListAllImgSections;
        this.mListFolders = mListFolders;
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case TAB_PHOTO:
                if (maps.get(position) == null) {
                    fragment = PhotoFragment.newInstance(this.mListAllImgs, this.mListAllImgSections).setListener(this);
                    maps.put(position, fragment);
                }
                break;
            case TAB_ALBUM:
                if (maps.get(position) == null) {
                    fragment = AlbumsFragment.newInstance(mListFolders).setListener(this);
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

   public interface onPhotoListener {
        void onPhotoFragmentReady();

        void onItemInPhotoLongClicked(FileItem fileItem);

        void openPhotoViewer(FileItem fileItem);

        void onItemInPhotoClicked(FileItem file, int numOfSelected);

        void onAlbumsFragmentReady();

        void onOpenAlbumViewer(String nameAlbum, ArrayList<FileItem> listFiles);

        void onItemAlbumLongClicked(AlbumItem album);

        void onItemAlbumClicked(AlbumItem album, int numOfSelected);

    }

    @Override
    public void onPhotoFragmentReady() {
        listener.onPhotoFragmentReady();
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
    public void onAlbumsFragmentReady() {
        listener.onAlbumsFragmentReady();
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
