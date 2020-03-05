package com.photo.splashfunphoto.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.photo.gallery.R;
import com.photo.splashfunphoto.adapter.EditerStickerAdapter;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;


public class EditerStickerFragment extends Fragment implements DiscreteSeekBar.OnProgressChangeListener, View.OnClickListener {
    private RecyclerView mREditer;

    private EditerStickerAdapter.OnItemEditerListener mEditerListener;
    private DiscreteSeekBar mStickerOpacitySeekbar;
    private int mOpacity = 0;
    private OnEditStickerListener mOnEditStickerListener;

    public static EditerStickerFragment newInstance(Bundle bundle, EditerStickerAdapter.OnItemEditerListener listener, OnEditStickerListener  onEditStickerListener, int opacity) {
        EditerStickerFragment fragment = new EditerStickerFragment();
        fragment.mEditerListener = listener;
        fragment.setArguments(bundle);
        fragment.mOpacity = opacity;
        fragment.mOnEditStickerListener = onEditStickerListener;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_editer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mREditer.setLayoutManager(layoutManager);
        EditerStickerAdapter editerAdapter = new EditerStickerAdapter(getActivity()).setOnItemEditerListener(mEditerListener);
        mREditer.setAdapter(editerAdapter);
    }

    private void initView() {
        mREditer = (RecyclerView) getView().findViewById(R.id.editer_rview);
        getView().findViewById(R.id.btn_done).setOnClickListener(this);
        mStickerOpacitySeekbar = (DiscreteSeekBar) getView().findViewById(R.id.sticker_opacity_seekbar);
        mStickerOpacitySeekbar.setMax(255 - 20);
        mStickerOpacitySeekbar.setProgress(mOpacity - 20);
        mStickerOpacitySeekbar.setOnProgressChangeListener(this);
    }

    @Override
    public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
        if (mEditerListener != null) {
            if(fromUser){
                mEditerListener.onItemOpacityClickListener(value);
            }
        }
    }

    @Override
    public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_done:
                if(mOnEditStickerListener != null){
                    mOnEditStickerListener.clickDoneEditSticker();
                }
                break;
        }
    }

    public interface OnEditStickerListener{
        void clickDoneEditSticker();
    }
}
