package com.photo.splashfunphoto.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.photo.gallery.R;


public class InputTextStickerFragment extends BaseFragment implements View.OnClickListener {

    private EditText txtInput;
    private String mTxtEdit;
    private boolean mIsCheckEditText;

    public static  InputTextStickerFragment newFragment(OnInputTextStickerListener onInputTextStickerListener, String txtEdit, boolean isCheckEditText){
        InputTextStickerFragment inputTextStickerFragment = new InputTextStickerFragment();
        inputTextStickerFragment.mOnInputTextStickerListener = onInputTextStickerListener;
        inputTextStickerFragment.mTxtEdit = txtEdit;
        inputTextStickerFragment.mIsCheckEditText = isCheckEditText;
        return inputTextStickerFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_input_text_sticker, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        addControl();
        addEvent();
        if(mIsCheckEditText){
            txtInput.setText(mTxtEdit);
        }
        txtInput.setSingleLine();
        txtInput.requestFocus();
        showKeyBoard();
    }

    @Override
    public void backPressed() {
    }

    private void addControl() {
        txtInput = getView().findViewById(R.id.txt_input);
    }

    private void addEvent() {
            getView().findViewById(R.id.btn_close_input).setOnClickListener(this);
            getView().findViewById(R.id.btn_save_input).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_close_input:
                getActivity().getSupportFragmentManager().popBackStack();
                break;

            case R.id.btn_save_input:
                if(mOnInputTextStickerListener != null){
                    requireActivity().getSupportFragmentManager().popBackStack();
                    mOnInputTextStickerListener.saveInputText(txtInput.getText().toString(),mIsCheckEditText);
                }
                break;
        }
        hideKeyBoard(txtInput);
    }

    private void showKeyBoard() {
//        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(txtInput, InputMethodManager.SHOW_IMPLICIT);
    }

    private void hideKeyBoard(EditText virtualEditText) {
        if(virtualEditText == null) return;
        if (virtualEditText.isFocused()) {
            virtualEditText.clearFocus();
        }
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(virtualEditText.getWindowToken(), 0);
    }

    private OnInputTextStickerListener mOnInputTextStickerListener;

    public interface OnInputTextStickerListener{
        void saveInputText(String txt, boolean isCheckEdit);
    }

    @Override
    public void onStop() {
        hideKeyBoard(txtInput);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        txtInput.clearFocus();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
