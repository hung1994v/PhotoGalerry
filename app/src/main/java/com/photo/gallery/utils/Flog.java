package com.photo.gallery.utils;

import android.util.Log;

import com.photo.gallery.BuildConfig;

/**
 * Created by vutha on 3/28/2017.
 */

public class Flog {

    private static final String TAG = "PrankScreen";
    private static final boolean IS_DEBUG = BuildConfig.DEBUG;

    public static void d(String tag, String msg) {

        if (IS_DEBUG)
            Log.d(tag, msg);
    }


    public static void d(String msg) {

        if (IS_DEBUG)
            Log.d(TAG, msg);
    }
}
