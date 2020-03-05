package com.photo.gallery.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;

import android.text.InputFilter;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;


import com.photo.gallery.R;

import java.io.File;

/**
 * Created by Hoavt on 3/15/2018.
 */

public class Utils {

    public static boolean isStringHasCharacterSpecial(String text) {
        for (int i = 0; i < listSpecialCharacter.length; i++) {
            if (text.contains(listSpecialCharacter[i])) {
                return true;
            }
        }
        return false;
    }
    public static final String[] listSpecialCharacter = new String[]{"%", "/", "#", "^", ":", "?", ","};

    public static int[] getScreenSize(Context context) {
        int width = context.getResources().getSystem().getDisplayMetrics().widthPixels;
        int height = context.getResources().getSystem().getDisplayMetrics().heightPixels;
        return new int[]{width, height};
    }

    public static int parseInt(String intInString) {
        try {
            return Integer.parseInt(intInString.trim());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public static InputFilter filter = (source, start, end, dest, dstart, dend) -> {

        for (int i = start;i < end;i++) {
            if (!Character.isLetterOrDigit(source.charAt(i)) &&
                    !Character.toString(source.charAt(i)).equals("_") &&
                    !Character.toString(source.charAt(i)).equals("-") &&
                    !Character.toString(source.charAt(i)).equals(" "))
            {
                return "";
            }
        }
        return null;
    };

    public static long parseLong(String longInString) {
        try {
            return Long.parseLong(longInString.trim());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return -1;
    }



    public static void rateApp(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }

    public static void shareApp(Context context) {
        String appName = context.getPackageName();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                appName +
                        ": https://play.google.com/store/apps/details?id=" + context.getPackageName());
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, "Share"));
    }

    public static void showFeedbackDialog(Context context, String appName, String dstEmail) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", dstEmail, null));
        intent.putExtra(Intent.EXTRA_SUBJECT, appName + ": Feedback");
        context.startActivity(Intent.createChooser(intent, "Feedback"));
    }

    public static void scaleView(View v, float startScale, float endScale) {
        Animation anim = new ScaleAnimation(
                1f, 1f, // Start and end values for the X axis scaling
                startScale, endScale, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(ConstValue.ANIM_DURATION);
        v.startAnimation(anim);
    }

    public static void translateVerticalView(View v, float startMoveY, float endMoveY) {
        Animation anim = new TranslateAnimation(0.0f, 0.0f, startMoveY, endMoveY);
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(ConstValue.ANIM_DURATION);
        v.startAnimation(anim);
    }

    public static float getActionBarHeight(Context context) {
        float actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
                    context.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    public static int convertDipToPixels(Context context, float dips) {
        return (int) (dips * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static int getStatusBarHeight(Activity activity) {
        Rect rectangle = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight = rectangle.top;
        if (statusBarHeight <= 0) {
            int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
            }
        }
        return statusBarHeight;
    }

    public static void aleartRequestSelect(CoordinatorLayout coordinatorLayout, String action) {
        Context context = coordinatorLayout.getContext();
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, context.getString(R.string.please_select_media_for)
                        + " " + action, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public static String getResolution(String filePath){
        File file = new File(filePath);
        return getResolution(file);
    }

    private static String getResolution(File file){
        try {
            String string ="";
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(file.getPath());
            int width = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
            int height = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            retriever.release();
            return string = string+width+" x "+ height;
        }catch (Exception e )
        {
            return ("Exception occurred");
        }
    }


    public static String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String hourString = "";
        String minuteString = "";
        String secondsString = "";

        //Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            if (hours < 10) {
                hourString = "0" + hours;
            } else {
                hourString = "" + hours;
            }
            finalTimerString = hourString + ":";
        }

        // Pre appending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        if (minutes < 10) {
            minuteString = "0" + minutes;
        } else {
            minuteString = "" + minutes;
        }

        finalTimerString = finalTimerString + minuteString + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    public static void resetPreferredLauncherAndOpenChooser(Context context, boolean isDefault) {

        PackageManager pm = context.getPackageManager();
        ComponentName compName =
                new ComponentName(context.getPackageName(), context.getPackageName() + ".activities.SplashActivity");
        pm.setComponentEnabledSetting(
                compName,
                isDefault?PackageManager.COMPONENT_ENABLED_STATE_ENABLED:PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    public static void setColorViews(int color, View... views) {
        for (View view : views) {
            if (view instanceof ImageView) {
                setTintColor((ImageView) view, color);
            } else if (view instanceof TextView) {
                ((TextView) view).setTextColor(color);
            } else if (view instanceof ViewGroup) {
                view.setBackgroundColor(color);
            }
        }
    }

    public static void setTintColor(ImageView imageView, int color) {
        imageView.setColorFilter(new
                PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
    }

    public static void setConstractStatusBar(Activity activity, boolean isDark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();

            if (!isDark) { // if childs is light.
                // then set dark theme.
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    View decor = window.getDecorView();
                    decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
            } else {
                // then set light theme.
                window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    View decor = window.getDecorView();
                    decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                }
            }
        }
    }

    public static boolean isColorDark(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        // It's a dark color
        return !(darkness < 0.5); // It's a light color
    }

    public static void setColorStatusBar(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    public static int getColorDarker(int colorPrimary) {
        return manipulateColor(colorPrimary, 0.8f);
    }

    public static int manipulateColor(int color, float factor) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
                Math.min(r, 255),
                Math.min(g, 255),
                Math.min(b, 255));
    }

    private void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }
}
