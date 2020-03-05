package com.photo.gallery.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.bsoft.core.PreloadNativeAdsList;
import com.photo.gallery.models.FileItem;
import com.photo.gallery.utils.sdcard.StorageBean;
import com.photo.gallery.utils.sdcard.StorageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Hoavt on 3/15/2018.
 */

public class GalleryUtil {

    private static final java.lang.String TAG = GalleryUtil.class.getSimpleName();

    public static void getAllMediaSdcard(Activity activity, ArrayList<FileItem> listOfAllImages) {

        ArrayList<StorageBean> listStorages = StorageUtils.getStorageData(activity);
        if (listStorages==null) {
            Flog.d(TAG,"listStorages");
            return;
        }

        for (int i = 0; i < listStorages.size(); i++) {
            StorageBean storageBean = listStorages.get(i);
            Flog.d(TAG, "bean "+i+": "+storageBean);
            if (storageBean.getRemovable()) {
                String path = storageBean.getPath();
                    getAllFilesOfDir(activity, new File(path), listOfAllImages);
            }
        }
    }

    private static void getAllFilesOfDir(Context  context, File directory, ArrayList<FileItem> data) {

        Flog.d(TAG, "Directory:111122 " + directory.getAbsolutePath() + "\n");
        final File[] files = directory.listFiles();
        if (files != null) {

            for (File file : files) {
                if (file != null) {
                    if (file.isDirectory()) {  // it is a folder...
                        getAllFilesOfDir(context, file, data);
                    } else {  // it is a file...

                        TYPE_FILE typeFile = isMediaFile(file.getAbsolutePath());
                        if (typeFile != TYPE_FILE.TYPE_NONE) {
                            FileItem item = new FileItem();
                            item._ID = "";
                            item.folder_ID = "";
                            item.folder = file.getParentFile().getName();
                            item.path = file.getAbsolutePath();
                            item.name = file.getName();
                            item.mSize = (file.length());
                            item.date_modified = String.valueOf(file.lastModified());
                            int[] dimens = FileUtil.getDimension(item.path);
                            item.width = String.valueOf(dimens[0]);
                            item.height = String.valueOf(dimens[1]);
                            item.mime_type = FileUtil.getMimeType(item.path);
                            item.isImage = (typeFile==TYPE_FILE.TYPE_IMAGE);

                            if (item.isImage) {
                                item.orientation = String.valueOf(FileUtil.getOrientation(item.path));
                            } else {
//                                item.duration = String.valueOf(FileUtil.getDurationVideo(context, item.path));
                            }

                            data.add(item);
                            Flog.d(TAG, "File: " + file.getAbsolutePath() + "\n");
                        }
                    }
                }
            }
        }
    }

    public enum TYPE_FILE {
        TYPE_NONE, TYPE_IMAGE, TYPE_VIDEO
    };

    private static TYPE_FILE isMediaFile(String path) {
        for (int i = 0; i < imageExtensions.length; i++) {
            if (path.toLowerCase().endsWith(imageExtensions[i])) {
                return TYPE_FILE.TYPE_IMAGE;
            }
        }

        for (int i = 0; i < videoExtensions.length; i++) {
            if (path.toLowerCase().endsWith(videoExtensions[i])) {
                return TYPE_FILE.TYPE_VIDEO;
            }
        }
        return TYPE_FILE.TYPE_NONE;
    }

    private static final String[] imageExtensions =  new String[] {"jpg", "png", "gif","jpeg"};
    private static final String[] videoExtensions =  new String[] {"3gp", "mp4", "m4a","mkv"};

    public static ArrayList<FileItem> getAllImages(Activity activity) {

        ArrayList<FileItem> listOfAllImages = new ArrayList<FileItem>();

        Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {
                MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_ID, MediaStore.Images.Media.DISPLAY_NAME
                , MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.MediaColumns.DATA, MediaStore.Images.Media.DATE_MODIFIED
                , MediaStore.Images.Media.HEIGHT, MediaStore.Images.Media.WIDTH, MediaStore.Images.Media.SIZE
                , MediaStore.Images.Media.MIME_TYPE
                , MediaStore.Images.Media.ORIENTATION
        };

        Cursor cursor = activity.getContentResolver().query(uri, projection, null,
                null, MediaStore.Images.Media.DATE_MODIFIED + " DESC");

        if (cursor == null)
            return listOfAllImages;

        boolean isImage = true;

        int column_index_id = cursor.getColumnIndex(MediaStore.Images.Media._ID);
        int column_index_bucket_id = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
        int column_index_name = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
        int column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        int column_index_date_modified = cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED);
        int column_index_height = cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT);
        int column_index_width = cursor.getColumnIndex(MediaStore.Images.Media.WIDTH);
        int column_index_size = cursor.getColumnIndex(MediaStore.Images.Media.SIZE);
        int column_index_mime_type = cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE);
        int column_index_orientation = cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION);

        int cnt = 0;
        while (cursor.moveToNext()) {
            FileItem model = new FileItem();

            model._ID = cursor.getString(column_index_id);
            model.folder_ID = cursor.getString(column_index_bucket_id);
            model.name = cursor.getString(column_index_name);
            try {
                model.name = model.name.replace(FileUtil.getExtension(model.name), "");
            }catch (Exception ex){
                ex.printStackTrace();
            }
            model.folder = cursor.getString(column_index_folder_name);
            model.path = cursor.getString(column_index_data);
            model.date_modified = cursor.getString(column_index_date_modified);

            model.date_modified = String.valueOf(Utils.parseLong(model.date_modified)*1000);

            if (model.date_modified.equals("-1000")) { // date_modified=null
                model.date_modified = listOfAllImages.get(listOfAllImages.size()-1).date_modified;
                Flog.d(TAG, "model.date_modified=="+model.date_modified);
            }

//            model.date_modified = String.valueOf(new File(model.path).lastModified());

            model.height = cursor.getString(column_index_height);
            model.width = cursor.getString(column_index_width);
            model.mSize = cursor.getLong(column_index_size);
            model.mime_type = cursor.getString(column_index_mime_type);
            model.orientation = cursor.getString(column_index_orientation);
            model.isImage = isImage;

            cnt++;
            Flog.d(TAG, "model at " + cnt + ": " + model);
            if(new File(model.path).exists() && model.mSize>0){
                listOfAllImages.add(model);
            }

        }

        cursor.close();


        // DEBUG:
//        Flog.d(TAG, "gallery element 0: "+listOfAllImages.get(0).path);
//        Flog.d(TAG, "gallery element 1: "+listOfAllImages.get(1).path);
//        Flog.d(TAG, "gallery element last-1: "+listOfAllImages.get(listOfAllImages.size()-2).path);
//        Flog.d(TAG, "gallery element last: "+listOfAllImages.get(listOfAllImages.size()-1).path);

        return listOfAllImages;
    }

    public static ArrayList<FileItem> getAllVideos(Activity activity) {

        ArrayList<FileItem> listOfAllImages = new ArrayList<FileItem>();

        Uri uri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {
                MediaStore.Video.Media._ID, MediaStore.Video.Media.BUCKET_ID, MediaStore.Video.Media.DISPLAY_NAME
                , MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.MediaColumns.DATA, MediaStore.Video.Media.DATE_MODIFIED
                , MediaStore.Video.Media.HEIGHT, MediaStore.Video.Media.WIDTH, MediaStore.Video.Media.SIZE
                , MediaStore.Video.Media.MIME_TYPE
                , MediaStore.Video.Media.DURATION
        };

        Cursor cursor = activity.getContentResolver().query(uri, projection, null,
                null, MediaStore.Video.Media.DATE_MODIFIED + " DESC");

        if (cursor == null)
            return listOfAllImages;

        boolean isImage = false;

        int column_index_id = cursor.getColumnIndex(MediaStore.Video.Media._ID);
        int column_index_bucket_id = cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID);
        int column_index_name = cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME);
        int column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
        int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        int column_index_date_modified = cursor.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED);
        int column_index_height = cursor.getColumnIndex(MediaStore.Video.Media.HEIGHT);
        int column_index_width = cursor.getColumnIndex(MediaStore.Video.Media.WIDTH);
        int column_index_size = cursor.getColumnIndex(MediaStore.Video.Media.SIZE);
        int column_index_mime_type = cursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE);
        int column_index_duration = cursor.getColumnIndex(MediaStore.Video.Media.DURATION);

        int cnt = 0;
        while (cursor.moveToNext()) {
            FileItem model = new FileItem();

            model._ID = cursor.getString(column_index_id);
            model.folder_ID = cursor.getString(column_index_bucket_id);
            model.name = cursor.getString(column_index_name);
            try {
                model.name = model.name.replace(FileUtil.getExtension(model.name), "");
            }catch (Exception ex){
                ex.printStackTrace();
            }
            model.folder = cursor.getString(column_index_folder_name);
            model.path = cursor.getString(column_index_data);
            model.date_modified = cursor.getString(column_index_date_modified);

            model.date_modified = String.valueOf(Utils.parseLong(model.date_modified)*1000);

            if (model.date_modified.equals("-1000")) { // date_modified=null
                model.date_modified = listOfAllImages.get(listOfAllImages.size()-1).date_modified;
                Flog.d(TAG, "model.date_modified=="+model.date_modified);
            }

            model.height = cursor.getString(column_index_height);
            model.width = cursor.getString(column_index_width);
            model.mSize = cursor.getLong(column_index_size);
            model.mime_type = cursor.getString(column_index_mime_type);
            model.duration = cursor.getString(column_index_duration);
            model.isImage = isImage;

            long duration ;
            if(model.duration==null){
                duration = 0;
            }else {
                try {
                    duration = Long.parseLong(model.duration);
                }catch (NumberFormatException e){
                    duration =0;
                }

            }


            cnt++;
            Flog.d(TAG, "model at " + cnt + ": " + model);
            if(new File(model.path).exists() && duration>0){
                listOfAllImages.add(model);
            }
        }

        cursor.close();
        return listOfAllImages;
    }

    public static HashMap<String, ArrayList<FileItem>> groupListAllAlbums(ArrayList<FileItem> listAllFiles) {
//        int positionAds;
        if (listAllFiles == null) {
            return null;
        }
        HashMap<String, ArrayList<FileItem>> listAllFolders = new HashMap<>();
        for (int i = 0; i < listAllFiles.size(); i++) {
            FileItem file = listAllFiles.get(i);
            String folderID = file.folder_ID;
            if (!listAllFolders.containsKey(folderID)) {
                ArrayList<FileItem> listFiles = new ArrayList<>();
                listFiles.add(file);
                listAllFolders.put(folderID, listFiles);
            } else {
                ArrayList<FileItem> listExistsFiles = listAllFolders.get(folderID);
                listExistsFiles.add(file);
                listAllFolders.put(folderID, listExistsFiles);
            }
        }
        return listAllFolders;
    }

    public static void logListFolder(HashMap<String, ArrayList<FileItem>> listAllFolders) {
        Flog.d(TAG, "***** list all folders=" + listAllFolders.size() + "   ************");
        for (int i = 0; i < listAllFolders.keySet().size(); i++) {
            String key = (String) listAllFolders.keySet().toArray()[i];
            ArrayList<FileItem> listFiles = listAllFolders.get(key);
            key = DateUtils.getDate(Utils.parseLong(key), DateUtils.FORMAT_DATE_1);
            Flog.d(TAG, "key at " + key + "_size=" + listFiles.size());
        }
        Flog.d(TAG, "---------------------------------------------");
    }

    public static ArrayList<FileItem> groupListAllImages(ArrayList<FileItem> listAllFiles) {
        if (listAllFiles == null) {
            return null;
        }
        ArrayList<FileItem> listImages = new ArrayList<>();
        for (int i = 0; i < listAllFiles.size(); i++) {
            FileItem file = listAllFiles.get(i);
            if (file.isImage) {
                listImages.add(file);
            }
        }
        return listImages;
    }



    public static ArrayList<FileItem> groupListAllVideos(ArrayList<FileItem> listAllFiles) {
        if (listAllFiles == null) {
            return null;
        }
        ArrayList<FileItem> listVideos = new ArrayList<>();
        for (int i = 0; i < listAllFiles.size(); i++) {
            FileItem file = listAllFiles.get(i);
            if (!file.isImage) {
                listVideos.add(file);
            }
        }
        return listVideos;
    }

    public static Map<String, ArrayList<FileItem>> groupListSectionByDate(ArrayList<FileItem> listFiles) {
        if (listFiles==null) {
            return null;
        }
        HashMap<String, ArrayList<FileItem>> listSections = new HashMap<>();
        for (int i = 0; i < listFiles.size(); i++) {
            FileItem file = listFiles.get(i);
            String key = DateUtils.getDate(Utils.parseLong(file.date_modified), DateUtils.FORMAT_DATE_1);
            Flog.d(TAG, "gallery key="+key);
            if (!listSections.containsKey(key)) {
                ArrayList<FileItem> newList = new ArrayList<>();
                newList.add(file);
                listSections.put(key, newList);
            } else {
                ArrayList<FileItem> existsList = listSections.get(key);
                existsList.add(file);
                listSections.put(key, existsList);
            }
        }

        return sort(listSections);
    }

    public static Map<String, ArrayList<FileItem>> groupListSectionByMonth(ArrayList<FileItem> listFiles) {
        if (listFiles==null) {
            return null;
        }
        HashMap<String, ArrayList<FileItem>> listSections = new HashMap<>();
        for (int i = 0; i < listFiles.size(); i++) {
            FileItem file = listFiles.get(i);
            String key = DateUtils.getDate(Utils.parseLong(file.date_modified), DateUtils.FORMAT_MONTH_YEAR);
            Flog.d(TAG, "gallery key="+key);
            if (!listSections.containsKey(key)) {
                ArrayList<FileItem> newList = new ArrayList<>();
                newList.add(file);
                listSections.put(key, newList);
            } else {
                ArrayList<FileItem> existsList = listSections.get(key);
                existsList.add(file);
                listSections.put(key, existsList);
            }
        }

        return sortMonth(listSections);
    }

    public static Map<String, ArrayList<FileItem>> sortMonth(HashMap<String, ArrayList<FileItem>> input) {

        Map<String, ArrayList<FileItem>> map = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String date1, String date2) {
                int cmp = 0;

                if (date1.equals(date2)) {
                    cmp = 0;
                } else {
                    long dateInLong1 = DateUtils.convertDateToLong(date1, DateUtils.FORMAT_MONTH_YEAR);
                    long dateInLong2 = DateUtils.convertDateToLong(date2, DateUtils.FORMAT_MONTH_YEAR);
                    if (dateInLong1==0||dateInLong2==0) {
                        cmp = 0;
                    } else {
                        if (dateInLong1>dateInLong2) {
                            cmp = -1;
                        } else if (dateInLong1<dateInLong2) {
                            cmp = 1;
                        }
                    }
                }

                return cmp;
            }
        });
        map.putAll(input);

        return map;
    }

    public static Map<String, ArrayList<FileItem>> sort(HashMap<String, ArrayList<FileItem>> input) {

        Map<String, ArrayList<FileItem>> map = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String date1, String date2) {
                int cmp = 0;

                if (date1.equals(date2)) {
                    cmp = 0;
                } else {
                    long dateInLong1 = DateUtils.convertDateToLong(date1, DateUtils.FORMAT_DATE_1);
                    long dateInLong2 = DateUtils.convertDateToLong(date2, DateUtils.FORMAT_DATE_1);
                    if (dateInLong1==0||dateInLong2==0) {
                        cmp = 0;
                    } else {
                        if (dateInLong1>dateInLong2) {
                            cmp = -1;
                        } else if (dateInLong1<dateInLong2) {
                            cmp = 1;
                        }
                    }
                }

                return cmp;
            }
        });
        map.putAll(input);

        return map;
    }

    public static ArrayList<FileItem> filterByNameFile(ArrayList<FileItem> listAllFiles, String textInput) {
        ArrayList<FileItem> listFiltered = new ArrayList<>();
//        if (textInput==null||textInput.equals("")) {
//            return listFiltered;
//        }

        for (int i = 0; i < listAllFiles.size(); i++) {
            FileItem file = listAllFiles.get(i);
            Flog.d(TAG, "file.name at "+i+"="+file.name+"_textinput="+textInput);
            if (file.name !=null && file.name.toLowerCase().contains(textInput.toLowerCase().trim())) {
                listFiltered.add(file);
            }
        }

        return listFiltered;
    }
}
