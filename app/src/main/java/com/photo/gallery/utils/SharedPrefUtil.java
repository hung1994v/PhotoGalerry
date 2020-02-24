package com.photo.gallery.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by Hoavt on 5/4/2017.
 */
public class SharedPrefUtil {

    private static SharedPrefUtil instance = null;
    private SharedPreferences preferences = null;

    public SharedPrefUtil() {

    }

    public static SharedPrefUtil getInstance() {
        if (instance == null) {
            instance = new SharedPrefUtil();
        }
        return instance;
    }

    public static void init(Context ctx) {
        SharedPrefUtil instance = getInstance();

        SharedPreferences prefs = ctx.getSharedPreferences(ctx.getPackageName(), Context.MODE_PRIVATE);
        instance.setPreferences(prefs);

//        release();
    }

    public static void release() {
        if (instance == null) return;
    }

    private void setPreferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public void putString(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    public void putBoolean(String key, boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

    public void putInt(String key, int value) {
        preferences.edit().putInt(key, value).apply();
    }

    public void putLong(String key, long value) {
        preferences.edit().putLong(key, value).apply();
    }

    public void putFloat(String key, float value) {
        preferences.edit().putFloat(key, value).apply();
    }

    public String getString(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }

    public long getLong(String key, long defValue) {
        return preferences.getLong(key, defValue);
    }


    public float getFloat(String key, float defValue) {
        return preferences.getFloat(key, defValue);
    }


    public void remove(String key) {
        preferences.edit().remove(key).apply();
    }

}
