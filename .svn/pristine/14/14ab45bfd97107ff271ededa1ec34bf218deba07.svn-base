package com.photo.gallery.taskes;

import android.content.Context;
import android.os.AsyncTask;

import com.photo.gallery.models.FileItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Hoavt on 3/22/2018.
 */

public class SelectAllTask extends AsyncTask<Map<String, ArrayList<FileItem>>, Integer, HashMap<Integer, FileItem>> {

    private Context mContext = null;
    private Map<String, ArrayList<FileItem>> mListAllImgSections = null;

    public SelectAllTask(Context context, Map<String, ArrayList<FileItem>> listAllImgSections) {
        mContext = context;
        mListAllImgSections = listAllImgSections;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int positionInAdapter = values[0];
        if (listener!=null) {
            listener.onSelectAllLoading(positionInAdapter);
        }
    }

    @Override
    protected HashMap<Integer, FileItem> doInBackground(Map<String, ArrayList<FileItem>>[] maps) {
        HashMap<Integer, FileItem> listPositionChanged = new HashMap<>();
        int index = 0;
        // Add your Sections
        for (Map.Entry<String, ArrayList<FileItem>> entry : mListAllImgSections.entrySet()) {
            String key = entry.getKey();
            ArrayList<FileItem> items = mListAllImgSections.get(key);

            for (int position = 0; position < items.size(); position++) {
                FileItem file = items.get(position);
                int positionInAdapter = getPositionInAdapter(index, position);
                listPositionChanged.put(positionInAdapter, file);
                publishProgress(positionInAdapter);
            }

            index++;
        }
        return listPositionChanged;
    }

    @Override
    protected void onPostExecute(HashMap<Integer, FileItem> listPositionChanged) {
        if (listener!=null) {
            listener.onSelectAllFinished(listPositionChanged);
        }
    }

    private int getPositionInAdapter(int index, int position) {
        int positionInAdapter = 0;
        int cnt = 0;
        // Add your Sections
        for (Map.Entry<String, ArrayList<FileItem>> entry : mListAllImgSections.entrySet()) {
            if (index <= cnt) {
                break;
            }

            String key = entry.getKey();
            ArrayList<FileItem> items = mListAllImgSections.get(key);
            positionInAdapter += (1 + items.size());

            cnt++;
        }

        positionInAdapter += (1 + position);
        return positionInAdapter;
    }

    private OnSelectAllTaskListener listener = null;

    public SelectAllTask setListener(OnSelectAllTaskListener listener) {
        this.listener = listener;
        return this;
    }

    public interface OnSelectAllTaskListener {
        void onSelectAllLoading(int positionInAdapter);
        void onSelectAllFinished(HashMap<Integer, FileItem> listPositionChanged);
    }
}
