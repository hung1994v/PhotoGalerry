package com.photo.splashfunphoto.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.TextView;

import com.photo.gallery.R;
import com.photo.splashfunphoto.ui.custtom.indicators.AVLoadingIndicatorView;


public class MyProgressDialog extends ProgressDialog {

    private String message;

    public MyProgressDialog(Context context, String message) {
        super(context);
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.custom_progress_dialog);
        TextView textView = (TextView) findViewById(R.id.text_message);
        textView.setText(message);
        AVLoadingIndicatorView progressBar = (AVLoadingIndicatorView) findViewById(R.id.avloading_indicators);
    }

    public MyProgressDialog(Context context) {
        super(context);
    }

    public MyProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    public static ProgressDialog newInstance(Context context) {
        MyProgressDialog dialog = new MyProgressDialog(context);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        return dialog;
    }

    public static ProgressDialog newInstance(Context context, String message) {
        MyProgressDialog dialog = new MyProgressDialog(context, message);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        return dialog;
    }
}
