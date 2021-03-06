package com.photo.gallery.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by vutha on 3/31/2017.
 */

public class KeyboardUtil {

    /**
     * You can force Android to hide the virtual keyboard using the InputMethodManager,
     * calling hideSoftInputFromWindow, passing in the token of the window containing your focused view.
     *
     * @param context     the context of activity.
     * @param viewFocused the EditText view is being focused to open keyboard.
     */
    public static void hideKeyboard(Context context, View viewFocused) {
        if (viewFocused == null) return;
        if (viewFocused.isFocused()) {
//            viewFocused.setFocusableInTouchMode(false);
            viewFocused.clearFocus();
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        try {
            imm.hideSoftInputFromWindow(viewFocused.getWindowToken(), 0);
        }catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }

    public static void hideKeyboard(Activity activity){
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if(activity.getCurrentFocus() != null){
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void showKeyboard(Context context, View viewFocused) {
        if (viewFocused == null) return;
        if (!viewFocused.isFocused()) {
//            viewFocused.setFocusableInTouchMode(true);
            viewFocused.requestFocus();
        }
        Flog.d("StickerViewList showkeyboard = " + viewFocused.isFocused());
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        try {
            imm.showSoftInput(viewFocused, InputMethodManager.SHOW_IMPLICIT);
        }catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }

    /**
     * Remove on global layout listener.
     *
     * @param v        the root view, take it from "Activity.getWindow().getDecorView().findViewById(android.R.id.content)"
     * @param listener the OnGlobalLayoutListener of ViewTreeObserver
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void removeOnGlobalLayoutListener(View v, ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (Build.VERSION.SDK_INT < 16) {
            v.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        } else {
            v.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }
}
