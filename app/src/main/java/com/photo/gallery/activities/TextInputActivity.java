package com.photo.gallery.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import android.text.InputFilter;
import android.view.View;
import android.view.ViewGroup;

import com.photo.gallery.R;
import com.photo.gallery.utils.ConstValue;
import com.photo.gallery.utils.Flog;
import com.photo.gallery.utils.KeyboardUtil;
import com.photo.gallery.utils.SharedPrefUtil;

/**
 * Created by Hoavt on 12/14/2017.
 */

public class TextInputActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = TextInputActivity.class.getSimpleName();
    public static final int STATUS_NEW_TEXT_INPUT = 0X12;
    public static final int STATUS_EDIT_TEXT_INPUT = 0X13;
    private final int LIMIT_TEXT_CHARACTERS = 150;
    private AppCompatEditText mEdInput;
    private int statusTextInput = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_input);
//        applyColor();
        initViews();
    }

    public void applyColor() {
        int defaultPrimary = ContextCompat.getColor(this, R.color.colorPrimary);
        int colorPrimary = SharedPrefUtil.getInstance().getInt(ConstValue.EXTRA_CURRENT_COLOR_PICKER, defaultPrimary);
        int colorPrimaryDark = com.photo.gallery.utils.Utils.getColorDarker(colorPrimary);

        ViewGroup viewGroup = findViewById(R.id.root_text_input_view);
        com.photo.gallery.utils.Utils.setColorViews(colorPrimary, viewGroup);

        boolean isDarkTheme = com.photo.gallery.utils.Utils.isColorDark(colorPrimary);
        com.photo.gallery.utils.Utils.setConstractStatusBar(this, isDarkTheme);
        com.photo.gallery.utils.Utils.setColorStatusBar(this, colorPrimaryDark);
    }

    private void initViews() {
        mEdInput = (AppCompatEditText) findViewById(R.id.ed_input);
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(LIMIT_TEXT_CHARACTERS);
        mEdInput.setFilters(filterArray);
        if (getIntent() != null) {
            statusTextInput = getIntent().getIntExtra(ConstValue.EXTRA_STATUS_TEXT_EDIT_INPUT, -1);
            String curText = getIntent().getStringExtra(ConstValue.EXTRA_CURRENT_TEXT_STICKER);
            if (curText != null && !curText.equals("")) {
                mEdInput.setText(curText);
                mEdInput.setSelection(curText.length());
            }
        }

        mEdInput.requestFocus();
        KeyboardUtil.showKeyboard(this, mEdInput);

        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.btn_ok).setOnClickListener(this);
        findViewById(R.id.btn_clear_text).setOnClickListener(this);
        findViewById(R.id.root_text_input_view).setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Flog.d(TAG, "requested=" + mEdInput.isFocused());
        if (mEdInput != null && !mEdInput.isFocused()) {
            Flog.d(TAG, "requested now");
            mEdInput.requestFocus();
            KeyboardUtil.showKeyboard(this, mEdInput);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_ok:
                KeyboardUtil.hideKeyboard(this, mEdInput);

                String input = mEdInput.getText().toString().trim();

//                Flog.d(TAG, "auto newlines = " + mEdInput.getLineCount());
//                Flog.d(TAG, "rslt=" + input);
                Intent resultIntent = new Intent();
                resultIntent.putExtra(ConstValue.EXTRA_TEXT_INPUTED, input);
                resultIntent.putExtra(ConstValue.EXTRA_STATUS_TEXT_EDIT_INPUT, statusTextInput);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();


                break;
            case R.id.btn_cancel:
                KeyboardUtil.hideKeyboard(this, mEdInput);

                setResult(Activity.RESULT_CANCELED);
                finish();
                break;
            case R.id.btn_clear_text:
                mEdInput.getText().clear();
                break;
            case R.id.root_text_input_view:
                // DO NOTHING
//                Flog.d(TAG, "keyboard shown="+ KeyboardUtil.isKeyboardShown);
                KeyboardUtil.showKeyboard(this, mEdInput);
                break;
        }
    }
}
