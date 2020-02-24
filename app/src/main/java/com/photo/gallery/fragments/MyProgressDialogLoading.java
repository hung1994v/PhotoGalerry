package com.photo.gallery.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.TextView;


import com.photo.gallery.R;

import bsoft.com.lib_filter.filter.indicators.AVLoadingIndicatorView;


public class MyProgressDialogLoading extends ProgressDialog {

    private String message;

    public MyProgressDialogLoading(Context context, String message) {
        super(context);
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.custom_progress_loading);
        TextView textView = (TextView) findViewById(R.id.text_message);
        textView.setText(message);
        AVLoadingIndicatorView progressBar = (AVLoadingIndicatorView) findViewById(R.id.avloading_indicators);

    }

    public MyProgressDialogLoading(Context context) {
        super(context);
    }

    public MyProgressDialogLoading(Context context, int theme) {
        super(context, theme);
    }

    public static ProgressDialog newInstance(Context context) {
        MyProgressDialogLoading dialog = new MyProgressDialogLoading(context);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        return dialog;
    }

    public static ProgressDialog newInstance(Context context, String message) {
        MyProgressDialogLoading dialog = new MyProgressDialogLoading(context, message);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        return dialog;
    }
}
