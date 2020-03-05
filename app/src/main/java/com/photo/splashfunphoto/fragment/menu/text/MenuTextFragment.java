package com.photo.splashfunphoto.fragment.menu.text;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.photo.splashfunphoto.EditPhotoActivity;
import com.photo.gallery.R;
import com.photo.splashfunphoto.adapter.text.TextAdapter;
import com.photo.splashfunphoto.adapter.text.TextFontAdapter;
import com.photo.splashfunphoto.fragment.BaseFragment;


public class MenuTextFragment extends BaseFragment implements ViewTreeObserver.OnGlobalLayoutListener, View.OnClickListener, ViewPager.OnPageChangeListener {
    public static final String IS_INITIAL_EXPAND_KEY = "EXPAND_KEY";
    public static final String TEXT_SIZE_KEY = "TEXT_SIZE";
    public static final String PADDING_TEXT_KEY = "PADDING_TEXT";
    private static final String TAG = MenuTextFragment.class.getSimpleName();
    View mRootView;
    private ViewPager viewPager = null;
    private Rect mRect = new Rect();
    private int keyboardHeight = -1;
    private LinearLayout mContainerText;
    private int mView;
    private TextFontAdapter.OnTextFontListener mOnTextFontListener;
    private TextSettingFragment.OnTextSettingListener mOnTextSettingListener;
    private TextEditFragment.OnTextEditListener mOnTextEditListener;
    private OnMenuTextListener mOnMenuTextListener;

    public static MenuTextFragment newFragment(int id, OnMenuTextListener onMenuTextListener, TextFontAdapter.OnTextFontListener listener, TextSettingFragment.OnTextSettingListener listener1, TextEditFragment.OnTextEditListener listener2) {
        MenuTextFragment fragment = new MenuTextFragment();
        Bundle bundle = new Bundle();
        fragment.mOnTextFontListener = listener;
        fragment.mOnTextSettingListener = listener1;
        fragment.mOnTextEditListener = listener2;
        fragment.mOnMenuTextListener = onMenuTextListener;
        fragment.mView = id;
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu_text, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addEvent();
        initTabLayout();
//        getHeightfromLibrary();

        ViewGroup.LayoutParams params = viewPager.getLayoutParams();
        params.height = 1;
        viewPager.setLayoutParams(params);
    }

    private void addEvent() {
        getView().findViewById(R.id.btn_font_text).setOnClickListener(this);
        getView().findViewById(R.id.btn_edit_text).setOnClickListener(this);
        getView().findViewById(R.id.btn_save_edit_text).setOnClickListener(this);
        getView().findViewById(R.id.btn_color_text).setOnClickListener(this);
    }

    private void getHeightfromLibrary() {
        String s = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(EditPhotoActivity.TEXT_HEIGHT, null);
        int a = Integer.parseInt(s);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, a);
//      mContainerText.getLayoutParams().height = a;
    }

    private void initTabLayout() {
        viewPager = (ViewPager) getView().findViewById(R.id.view_pager);
        mContainerText = (LinearLayout) getView().findViewById(R.id.menu_text);

        viewPager.setAdapter(new TextAdapter(getChildFragmentManager(), getArguments(),mOnTextFontListener,mOnTextSettingListener,mOnTextEditListener));

        if (getActivity() instanceof TextBaseFragment.OnTextOptionCallback &&
                getArguments() != null && getArguments().getBoolean(IS_INITIAL_EXPAND_KEY, true)) {
            ((TextBaseFragment.OnTextOptionCallback) getActivity()).onKeyBoardSoftDisplayed();
        }
        viewPager.addOnPageChangeListener(this);
    }


    public void setHeightPager(int height) {
        if (height <= -1) return;
        ViewGroup.LayoutParams params = viewPager.getLayoutParams();
        params.height = height;
        viewPager.setLayoutParams(params);
        viewPager.invalidate();
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (requireActivity().getCurrentFocus() != null) {
                    inputMethodManager.hideSoftInputFromWindow(requireActivity().getCurrentFocus().getWindowToken(), 0);
                }
            }
        },150);
    }

    @Override
    public void onPause() {
        super.onPause();
//        mRootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    public int getCurTab() {
        return viewPager.getCurrentItem();
    }

    @Override
    public void onGlobalLayout() {
    }

    @Override
    public void backPressed() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_color_text:
                viewPager.setCurrentItem(1);
                break;

            case R.id.btn_font_text:
                viewPager.setCurrentItem(0);
                DisplayMetrics displaymetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                int height = displaymetrics.heightPixels/4;
                setHeightPager(height);
                break;

            case R.id.btn_save_edit_text:
                if(mOnMenuTextListener != null){
                    mOnMenuTextListener.clickDoneMenuText();
                }
                break;

            case R.id.btn_edit_text:
                viewPager.setCurrentItem(2);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
            case 1:
            case 2:
                break;
        }
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels/4;
        setHeightPager(height);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public interface OnMenuTextListener{
        void clickDoneMenuText();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
