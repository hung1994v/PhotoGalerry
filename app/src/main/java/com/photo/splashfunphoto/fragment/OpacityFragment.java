package com.photo.splashfunphoto.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import com.photo.gallery.R;
import com.photo.splashfunphoto.utils.Statics;


public class OpacityFragment extends BaseFragment implements SeekBar.OnSeekBarChangeListener {
    private AppCompatSeekBar mSeekBar;
    private OnOpacityListener mOpacityListener;
    private LinearLayout mContainerOpacity;

    public OpacityFragment() {
    }

    public static OpacityFragment newInstance(Bundle bundle, OnOpacityListener listener) {
        OpacityFragment fragment = new OpacityFragment();
        fragment.mOpacityListener = listener;
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void backPressed() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_opacity, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        mSeekBar = (AppCompatSeekBar) getView().findViewById(R.id.seekbar_opacity);
        mContainerOpacity = (LinearLayout) getView().findViewById(R.id.bottombar_opacity_sticker);

        mSeekBar.setThumb(getResources().getDrawable(R.drawable.ic_oval));
        mSeekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);

        mSeekBar.setMax(225);
        mSeekBar.setProgress(Statics.OPACITY);
        mSeekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (mOpacityListener != null) {
            mOpacityListener.onSeekbarOpacityListener(i);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public interface OnOpacityListener {
        public void onSeekbarOpacityListener(int intensity);
    }


    public void handlOpacityFragment() {
        mContainerOpacity.setVisibility(View.GONE);
    }
}
