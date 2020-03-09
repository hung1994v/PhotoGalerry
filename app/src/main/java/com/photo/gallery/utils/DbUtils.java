package com.photo.gallery.utils;

import com.photo.gallery.models.FileItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Hoavt on 4/11/2018.
 */

public class DbUtils {

    private static final String EXTRA_FAVOURITES = "EXTRA_FAVOURITES";
    private static final String TAG = DbUtils.class.getSimpleName();

    public static void saveFavourites(ArrayList<FileItem> items) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (int i = 0; i < items.size(); i++) {
            FileItem item = items.get(i);
            sb.append("{");
            // content
            sb.append("\"_ID\":\"" + item._ID + "\"");
            sb.append(",");
            sb.append("\"folder_ID\":\"" + item.folder_ID + "\"");
            sb.append(",");
            sb.append("\"date_modified\":\"" + item.date_modified + "\"");
            sb.append(",");
            sb.append("\"height\":\"" + item.height + "\"");
            sb.append(",");
            sb.append("\"width\":\"" + item.width + "\"");
            sb.append(",");
            sb.append("\"mime_type\":\"" + item.mime_type + "\"");
            sb.append(",");
            sb.append("\"size\":\"" + item.mSize + "\"");
            sb.append(",");
            sb.append("\"path\":\"" + item.path + "\"");
            sb.append(",");
            sb.append("\"name\":\"" + item.name + "\"");
            sb.append(",");
            sb.append("\"folder\":\"" + item.folder + "\"");
            sb.append(",");
            sb.append("\"orientation\":\"" + item.orientation + "\"");
            sb.append(",");
            sb.append("\"duration\":\"" + item.duration + "\"");
            sb.append(",");
            sb.append("\"isImage\":\"" + (item.isImage ? 1 : 0) + "\"");

            sb.append("}");

            if (i < items.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");

        SharedPrefUtil.getInstance().putString(EXTRA_FAVOURITES, sb.toString());
    }

    public static ArrayList<FileItem> parseFavourites() {
        ArrayList<FileItem> list = new ArrayList<>();
        String json = SharedPrefUtil.getInstance().getString(EXTRA_FAVOURITES, null);
        if (json != null) {
            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    FileItem item = new FileItem();
                    item._ID = jsonObject.getString("_ID");
                    item.folder_ID = jsonObject.getString("folder_ID");
                    item.date_modified = jsonObject.getString("date_modified");
                    item.height = jsonObject.getString("height");
                    item.width = jsonObject.getString("width");
                    item.mime_type = jsonObject.getString("mime_type");
                    item.mSize = jsonObject.getLong("size");
                    item.path = jsonObject.getString("path");
                    item.name = jsonObject.getString("name");
                    item.folder = jsonObject.getString("folder");
                    item.orientation = jsonObject.getString("orientation");
                    item.duration = jsonObject.getString("duration");
                    item.isImage = jsonObject.getString("isImage").equals("1");

                    if (new File(item.path).exists()) {
                        list.add(item);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    public static void updateFavourite(FileItem lastItem) {
        ArrayList<FileItem> list = parseFavourites();
        for (int i = 0; i < list.size(); i++) {
            FileItem fileItem = list.get(i);
            if (fileItem.equals(lastItem)) {
                list.set(i, lastItem);
                break;
            }
        }
        saveFavourites(list);
    }


    public static boolean deleteFavourite(FileItem fileItem) {
        ArrayList<FileItem> oldList = DbUtils.parseFavourites();
        if(oldList.size()<1 && fileItem!=null){
            return false;
        }else {
            boolean ok = false ;
            for(FileItem fileItem1: oldList){
                if(fileItem1.equals(fileItem)){
                   ok =  oldList.remove(fileItem1);
                    Flog.d(TAG, "2size="+oldList.size());
                   break;
                }
            }
            DbUtils.saveFavourites(oldList);
            return ok;
        }

    }


    public static void addFavourite(FileItem fileItem) {
        ArrayList<FileItem> oldList = DbUtils.parseFavourites();
        oldList.add(fileItem);
        Collections.reverse(oldList);
        DbUtils.saveFavourites(oldList);
    }


}
