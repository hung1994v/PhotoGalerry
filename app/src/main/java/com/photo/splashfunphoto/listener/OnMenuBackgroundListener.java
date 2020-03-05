package com.photo.splashfunphoto.listener;

import androidx.annotation.ColorInt;

/**
 * Created by Adm on 8/3/2016.
 */
public interface OnMenuBackgroundListener {
    public void onMenuPatternClickListener(String bgName);
    public void onMenuColorBackgroundClickListener(@ColorInt int color);
}
