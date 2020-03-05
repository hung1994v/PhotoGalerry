package com.photo.gallery.listener;


public class VideoFavoriteRemoveCallback {
    private static VideoFavoriteRemoveCallback mInstance;
    private OnVideoFavoriteListener mListener;

    private VideoFavoriteRemoveCallback() {}

    public static VideoFavoriteRemoveCallback getInstance() {
        if(mInstance == null) {
            mInstance = new VideoFavoriteRemoveCallback();
        }
        return mInstance;
    }

    public void setListener(OnVideoFavoriteListener listener) {
        mListener = listener;
    }

    public OnVideoFavoriteListener getListener() {
        return mListener;
    }
}
