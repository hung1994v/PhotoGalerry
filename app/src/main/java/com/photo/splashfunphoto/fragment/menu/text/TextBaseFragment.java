package com.photo.splashfunphoto.fragment.menu.text;


import com.photo.splashfunphoto.fragment.BaseFragment;

public class TextBaseFragment extends BaseFragment {
    protected OnTextOptionCallback mOnKeyboardSoftListener = null;

    @Override
    public void backPressed() {

    }

    public interface OnTextOptionCallback {
        public void onKeyBoardSoftDisplayed();

        public void onFontDisplayed();

        public void onSettingDisplayed();

        public void onKeyBoardShown(int height);
    }
}