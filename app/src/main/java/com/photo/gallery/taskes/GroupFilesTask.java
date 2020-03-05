package com.photo.gallery.taskes;

import android.content.Context;
import android.os.AsyncTask;

import com.photo.gallery.models.FileItem;
import com.photo.gallery.utils.Flog;
import com.photo.gallery.utils.GalleryUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GroupFilesTask extends AsyncTask<ArrayList<FileItem>, Void, Void> {

    private Context mContext = null;
    private OnGroupFilesListener listener = null;
    private HashMap<String, ArrayList<FileItem>> listFolders = null;
    private ArrayList<FileItem> listImgs = null, listVideos = null;
    private Map<String, ArrayList<FileItem>> listImgSections = null, listVideoSections = null,
            listVideoSectionsByMonth = null,
            listImgSectionsByMonth,
            listFileSections = null;

    public GroupFilesTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(ArrayList<FileItem>... listFiles) {
        ArrayList<FileItem> listAllFiles = listFiles[0];
        listFolders = GalleryUtil.groupListAllAlbums(listAllFiles);
        listImgs = GalleryUtil.groupListAllImages(listAllFiles);
        listVideos = GalleryUtil.groupListAllVideos(listAllFiles);
        listImgSectionsByMonth = GalleryUtil.groupListSectionByMonth(listImgs);
        listImgSections = GalleryUtil.groupListSectionByDate(listImgs);
        listVideoSections = GalleryUtil.groupListSectionByDate(listVideos);
        listVideoSectionsByMonth = GalleryUtil.groupListSectionByMonth(listVideos);
        listFileSections = GalleryUtil.groupListSectionByDate(listAllFiles);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (listener != null) {
            listener.onGroupFilesFinished(listImgs, listVideos, listFolders, listImgSections,
                    listVideoSections,listVideoSectionsByMonth,listImgSectionsByMonth, listFileSections);
        }


    }

    public GroupFilesTask setListener(OnGroupFilesListener groupFoldersListener) {
        this.listener = groupFoldersListener;
        return this;
    }

    public interface OnGroupFilesListener {
        void onGroupFilesFinished(ArrayList<FileItem> listImgs, ArrayList<FileItem> listVideos,
                                  HashMap<String, ArrayList<FileItem>> listFolders,
                                  Map<String, ArrayList<FileItem>> listImgSections,
                                  Map<String, ArrayList<FileItem>> listVideoSections,
                                  Map<String, ArrayList<FileItem>> listVideoSectionsByMonth,
                                  Map<String, ArrayList<FileItem>> listImgSectionsByMonth,
                                  Map<String, ArrayList<FileItem>> listFileSections
                                  );
    }
}