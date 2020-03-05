package com.photo.splashfunphoto.utils;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class ScreenInfoUtil {
    public static int dip2px(Context context, float dpValue) {
        Resources res = context.getResources();
        return (int) ((dpValue * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        return (int) ((pxValue / context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int screenWidthDp(Context context) {
        return px2dip(context, (float) context.getResources().getDisplayMetrics().widthPixels);
    }

    public static int screenHeightDp(Context context) {
        return px2dip(context, (float) context.getResources().getDisplayMetrics().heightPixels);
    }

    public static float screenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int screenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int screenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static void resizeView(View v, float rateW, float rateH) {
        LayoutParams layout = v.getLayoutParams();
        if (layout instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams f_layout = (FrameLayout.LayoutParams) layout;
            f_layout.setMargins((int) (((double) (((float) f_layout.leftMargin) * rateW)) + 0.5d), (int) (((double) (((float) f_layout.topMargin) * rateH)) + 0.5d), (int) (((double) (((float) f_layout.rightMargin) * rateW)) + 0.5d), (int) (((double) (((float) f_layout.bottomMargin) * rateH)) + 0.5d));
            if (f_layout.width >= 0) {
                f_layout.width = (int) (((double) (((float) f_layout.width) * rateW)) + 0.5d);
            }
            if (f_layout.height >= 0) {
                f_layout.height = (int) (((double) (((float) f_layout.height) * rateH)) + 0.5d);
            }
        }
        if (layout instanceof RelativeLayout.LayoutParams) {
            RelativeLayout.LayoutParams f_layout2 = (RelativeLayout.LayoutParams) layout;
            f_layout2.setMargins((int) (((double) (((float) f_layout2.leftMargin) * rateW)) + 0.5d), (int) (((double) (((float) f_layout2.topMargin) * rateH)) + 0.5d), (int) (((double) (((float) f_layout2.rightMargin) * rateW)) + 0.5d), (int) (((double) (((float) f_layout2.bottomMargin) * rateH)) + 0.5d));
            if (f_layout2.width >= 0) {
                f_layout2.width = (int) (((double) (((float) f_layout2.width) * rateW)) + 0.5d);
            }
            if (f_layout2.height >= 0) {
                f_layout2.height = (int) (((double) (((float) f_layout2.height) * rateH)) + 0.5d);
            }
        }
    }
}