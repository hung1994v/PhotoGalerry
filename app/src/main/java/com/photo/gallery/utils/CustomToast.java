package com.photo.gallery.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.photo.gallery.R;

public class CustomToast extends Toast {
    public CustomToast(Context context, String content) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.custom_toast, null);
        TextView tv = view.findViewById(R.id.tvToast);
        tv.setText(content);
        setDuration(Toast.LENGTH_SHORT);
        setView(view);
    }

    public static void showContent(Context context, String content) {
        new CustomToast(context, content).show();
    }
}
