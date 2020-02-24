package com.photo.gallery;

import android.app.Application;

import androidx.multidex.MultiDexApplication;

import com.photo.gallery.utils.SharedPrefUtil;

/**
 * Created by Tung on 4/9/2018.
 */

public class MyApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPrefUtil.init(getApplicationContext());
    }
}
