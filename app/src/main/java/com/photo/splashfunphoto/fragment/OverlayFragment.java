package com.photo.splashfunphoto.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.splashfunphoto.EditPhotoActivity;
import com.photo.gallery.R;
import com.photo.splashfunphoto.adapter.OverlayAdapter;
import com.photo.splashfunphoto.utils.Statics;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import bsoft.com.lib_filter.filter.GPUAdjustRange;
import bsoft.com.lib_filter.filter.gpu.GPUFilter;
import bsoft.com.lib_filter.filter.gpu.GPUFilterRes;
import bsoft.com.lib_filter.filter.gpu.GPUFilterType;
import bsoft.com.lib_filter.filter.gpu.GPUImageView;
import bsoft.com.lib_filter.filter.gpu.OverlayManager;
import bsoft.com.lib_filter.filter.gpu.WBManager;
import bsoft.com.lib_filter.filter.gpu.WBRes;
import bsoft.com.lib_filter.filter.gpu.adjust.GPUImageBrightnessFilter;
import bsoft.com.lib_filter.filter.gpu.adjust.GPUImageContrastFilter;
import bsoft.com.lib_filter.filter.gpu.adjust.GPUImageExposureFilter;
import bsoft.com.lib_filter.filter.gpu.adjust.GPUImageHighlightShadowFilter;
import bsoft.com.lib_filter.filter.gpu.adjust.GPUImageRGBFilter;
import bsoft.com.lib_filter.filter.gpu.adjust.GPUImageSaturationFilter;
import bsoft.com.lib_filter.filter.gpu.father.GPUImageFilter;
import bsoft.com.lib_filter.filter.gpu.father.GPUImageFilterGroup;
import bsoft.com.lib_filter.filter.gpu.vignette.GPUImageVignetteFilter;
import bsoft.com.lib_filter.filter.listener.OnPostFilteredListener;

public class OverlayFragment extends BaseFragment implements View.OnClickListener, DiscreteSeekBar.OnProgressChangeListener, OverlayAdapter.OnItemOverlayListener {
    private DiscreteSeekBar mSeekBarOverlay;
    private RecyclerView mROverlay;
    private String mStringOverlay;
    private Bitmap mBitmapOverlay;
    private Bitmap bitmap;
    private int mCurAlpha = 225;
    private GPUImageView mGPUOverlay;
    private Bitmap resulBitmap;
    private HandleBackOverlay handleBackOverlay;
    private GPUFilterRes curFilterRes;
    private OverlayManager manager;
    private ArrayList<WBRes> resArray;
    private float filterPercent = 0.4f;
    private TextView txtNoise;
    private TextView txtTexture;
    private TextView txtAdjust;
    private final int TAB_NOISE = 1;
    private final int TAB_ADJSUT = 2;
    private final int TAB_TEXTURE = 0;
    private int mPositionCurrentTab = 0;
    public static final int DEFAULT_ITEM_POSITION = 0;
    private Bitmap mBitmapDefault;
    private int mPositionItemOverlay = 0;
    private int mCurrentPositionTexture = 0;
    private int mCurrentPositionNoise = 0;
    private int mProgressText = 40;
    private int mProgressNoise = 40;
    private DiscreteSeekBar mSeekbarAdjust;
    private ImageView icBright,icContrast,icHue,icTone,icWarmth,icFade,icVignette;
    private TextView txtBright,txtContrast,txtHue,txtTone,txtWarmth,txtFade,txtVignette;
    private int mCurAdjust = 1;
    private int currentAdjust1 = 50;
    private int currentAdjust2 = 50;
    private int currentAdjust3 = 50;
    private int currentAdjust4 = 50;
    private int currentAdjust5 = 50;
    private int currentAdjust6 = 0;
    private int currentAdjust7 = 0;
    private GPUImageFilterGroup strengthfilter;
    private OverlayAdapter overlayOldAdapter;

    public OverlayFragment() {

    }

    // TODO: Rename and change types and number of parameters
    public static OverlayFragment newInstance(Bundle bundle,HandleBackOverlay backOverlay, Bitmap bitmap) {
        OverlayFragment fragment = new OverlayFragment();
        fragment.handleBackOverlay = backOverlay;
        fragment.mBitmapOverlay = bitmap;
        fragment.mBitmapDefault = bitmap;
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
        return inflater.inflate(R.layout.fragment_overlay_edit, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    public void init() {
        Statics.loadImageOverlayNoise();
        Statics.loadImageOverlayText();
        initView();
        setImageView();
        mStringOverlay = getArguments().getString(EditPhotoActivity.OVERLAY, "TEXTURE");
        mCurrentPositionTexture = 0;
        getKeyFromLibrary();
        selectTab();
    }

    private void getKeyFromLibrary() {
        if (mStringOverlay.equals("TEXTURE")) {
            manager = new OverlayManager(getActivity(), 5);
            initRecyclerView(manager,mCurrentPositionTexture);
            if(mCurrentPositionTexture > 0){
                this.curFilterRes = (GPUFilterRes) resArray.get(mCurrentPositionTexture);
                bitmap = this.curFilterRes.getLocalImageBitmap();
                mGPUOverlay.setFilter(GPUFilter.createFilterForBlendType(getActivity(), this.curFilterRes.getFilterType(), bitmap));
                if (mGPUOverlay.getFilter() != null) {
                    filterPercent = ((float) mProgressText) / 100.0f;
                    mGPUOverlay.getFilter().setMix(filterPercent);
                    mGPUOverlay.requestRender();
                }
            }else if(mCurrentPositionTexture == DEFAULT_ITEM_POSITION){
                GPUFilterRes res = new GPUFilterRes();
                res.setContext(requireContext());
                res.setFilterType(GPUFilterType.BLEND_SCREEN);
                mGPUOverlay.setFilter(GPUFilter.createFilterForBlendType(getActivity(), res.getFilterType(), mBitmapDefault));
                mGPUOverlay.getFilter().setMix(0f);
                mGPUOverlay.requestRender();
            }
        } else if (mStringOverlay.equals("NOISE")) {
            manager = new OverlayManager(getActivity(), 6);
            initRecyclerView(manager,mCurrentPositionNoise);
            if(mCurrentPositionNoise > 0){
                this.curFilterRes = (GPUFilterRes) resArray.get(mCurrentPositionNoise);
                bitmap = this.curFilterRes.getLocalImageBitmap();
                mGPUOverlay.setFilter(GPUFilter.createFilterForBlendType(getActivity(), this.curFilterRes.getFilterType(), bitmap));
                if (mGPUOverlay.getFilter() != null) {
                    filterPercent = ((float) mProgressNoise) / 100.0f;
                    mGPUOverlay.getFilter().setMix(filterPercent);
                    mGPUOverlay.requestRender();
                }
            }else if(mCurrentPositionNoise == DEFAULT_ITEM_POSITION){
                GPUFilterRes res = new GPUFilterRes();
                res.setContext(requireContext());
                res.setFilterType(GPUFilterType.BLEND_SCREEN);
                mGPUOverlay.setFilter(GPUFilter.createFilterForBlendType(getActivity(), res.getFilterType(), mBitmapDefault));
                mGPUOverlay.getFilter().setMix(0f);
                mGPUOverlay.requestRender();
            }
        }else {
            List<GPUImageFilter> filters = new LinkedList();
            filters.add(new GPUImageBrightnessFilter(0.0f));
            strengthfilter = new GPUImageFilterGroup(filters);
            mGPUOverlay.setFilter(this.strengthfilter);
            mGPUOverlay.requestRender();
        }
    }

    private void initRecyclerView(WBManager manage, int currentPosition) {
        int count = manage.getCount() + 1;
        resArray = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            if(i == DEFAULT_ITEM_POSITION){
                resArray.add(null);
            }else {
                resArray.add(manager.getRes(i - 1));
            }
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mROverlay.setLayoutManager(layoutManager);
        overlayOldAdapter = new OverlayAdapter(requireContext(), resArray,currentPosition).setOnItemOverlayListener(this);
        mROverlay.setAdapter(overlayOldAdapter);
    }

    private void initView() {
        mROverlay = (RecyclerView) getView().findViewById(R.id.overlay_recycler);
        mSeekBarOverlay = (DiscreteSeekBar) getView().findViewById(R.id.overlay_seekbar);
        mSeekbarAdjust = (DiscreteSeekBar) getView().findViewById(R.id.adjust_seekbar);
        mGPUOverlay = (GPUImageView) getView().findViewById(R.id.img_overlay_gpu);
        txtNoise = (TextView) getView().findViewById(R.id.txt_noise);
        txtTexture = (TextView) getView().findViewById(R.id.txt_texture);
        txtAdjust = (TextView) getView().findViewById(R.id.txt_adjust);

        getView().findViewById(R.id.img_adjust_1).setOnClickListener(this);
        getView().findViewById(R.id.img_adjust_2).setOnClickListener(this);
        getView().findViewById(R.id.img_adjust_3).setOnClickListener(this);
        getView().findViewById(R.id.img_adjust_4).setOnClickListener(this);
        getView().findViewById(R.id.img_adjust_5).setOnClickListener(this);
        getView().findViewById(R.id.img_adjust_6).setOnClickListener(this);
        getView().findViewById(R.id.img_adjust_7).setOnClickListener(this);

        icBright = (ImageView) getView().findViewById(R.id.ic_bright);
        icContrast= (ImageView) getView().findViewById(R.id.ic_contrast);
        icHue = (ImageView) getView().findViewById(R.id.ic_hue);
        icTone = (ImageView) getView().findViewById(R.id.ic_tone);
        icWarmth = (ImageView) getView().findViewById(R.id.ic_warmth);
        icFade = (ImageView) getView().findViewById(R.id.ic_fade);
        icVignette = (ImageView) getView().findViewById(R.id.ic_vignette);
        txtBright = (TextView) getView().findViewById(R.id.txt_bright);
        txtContrast = (TextView) getView().findViewById(R.id.txt_contrast);
        txtHue = (TextView) getView().findViewById(R.id.txt_hue);
        txtTone = (TextView) getView().findViewById(R.id.txt_tone);
        txtWarmth = (TextView) getView().findViewById(R.id.txt_warmth);
        txtFade = (TextView) getView().findViewById(R.id.txt_fade);
        txtVignette = (TextView) getView().findViewById(R.id.txt_vignette);

        getView().findViewById(R.id.btn_overlay_exit).setOnClickListener(this);
        getView().findViewById(R.id.btn_overlay_save).setOnClickListener(this);
        getView().findViewById(R.id.txt_texture).setOnClickListener(this);
        getView().findViewById(R.id.txt_noise).setOnClickListener(this);
        getView().findViewById(R.id.btn_adjust).setOnClickListener(this);

        List<GPUImageFilter> filters = new LinkedList();
        filters.add(new GPUImageBrightnessFilter(0.0f));
        strengthfilter = new GPUImageFilterGroup(filters);
        mGPUOverlay.setFilter(this.strengthfilter);

        mSeekBarOverlay.setMax(100);
        mSeekBarOverlay.setProgress(mProgressText);
        mSeekBarOverlay.setOnProgressChangeListener(this);

        mSeekbarAdjust.setMax(100);
        mSeekbarAdjust.setProgress(50);
        mSeekbarAdjust.setOnProgressChangeListener(this);
    }

    private void selectTab() {
        if (mPositionCurrentTab == TAB_TEXTURE) {
            txtTexture.setTextColor(getContext().getResources().getColor(android.R.color.white));
            txtNoise.setTextColor(getContext().getResources().getColor(R.color.white_50));
            txtAdjust.setTextColor(getContext().getResources().getColor(R.color.white_50));
            getView().findViewById(R.id.line_noise).setVisibility(View.INVISIBLE);
            getView().findViewById(R.id.line_adjust).setVisibility(View.INVISIBLE);
            getView().findViewById(R.id.line_texture).setVisibility(View.VISIBLE);
            if(mCurrentPositionTexture <= 0){
                mSeekBarOverlay.setVisibility(View.INVISIBLE);
            }else {
                mSeekBarOverlay.setVisibility(View.VISIBLE);
            }
            getView().findViewById(R.id.menu_overlay).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.menu_adjust).setVisibility(View.GONE);
        } else if(mPositionCurrentTab == TAB_NOISE){
            txtNoise.setTextColor(getContext().getResources().getColor(android.R.color.white));
            txtTexture.setTextColor(getContext().getResources().getColor(R.color.white_50));
            txtAdjust.setTextColor(getContext().getResources().getColor(R.color.white_50));
            getView().findViewById(R.id.line_noise).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.line_adjust).setVisibility(View.INVISIBLE);
            getView().findViewById(R.id.line_texture).setVisibility(View.INVISIBLE);
            if(mCurrentPositionNoise <= 0){
                mSeekBarOverlay.setVisibility(View.INVISIBLE);
            }else {
                mSeekBarOverlay.setVisibility(View.VISIBLE);
            }
            getView().findViewById(R.id.menu_overlay).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.menu_adjust).setVisibility(View.GONE);
        }else {
            txtAdjust.setTextColor(getContext().getResources().getColor(android.R.color.white));
            txtTexture.setTextColor(getContext().getResources().getColor(R.color.white_50));
            txtNoise.setTextColor(getContext().getResources().getColor(R.color.white_50));
            getView().findViewById(R.id.line_noise).setVisibility(View.INVISIBLE);
            getView().findViewById(R.id.line_adjust).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.line_texture).setVisibility(View.INVISIBLE);
            if(mCurrentPositionNoise <= 0){
                mSeekBarOverlay.setVisibility(View.INVISIBLE);
            }else {
                mSeekBarOverlay.setVisibility(View.VISIBLE);
            }
            getView().findViewById(R.id.menu_overlay).setVisibility(View.GONE);
            getView().findViewById(R.id.menu_adjust).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setImageView() {
        mGPUOverlay.setImage(mBitmapOverlay);
    }

    @Override
    public void onItemOverlayClickListenr(int prevPos, int position) {
        if(position == DEFAULT_ITEM_POSITION){
            mSeekBarOverlay.setVisibility(View.INVISIBLE);
        }else {
            mSeekBarOverlay.setVisibility(View.VISIBLE);
        }

        mPositionItemOverlay = position;
        if(mPositionCurrentTab == TAB_NOISE){
            mCurrentPositionNoise = position;
        }else  if(mPositionCurrentTab == TAB_TEXTURE){
            mCurrentPositionTexture = position;
        }
        if(position != DEFAULT_ITEM_POSITION){
            this.curFilterRes = (GPUFilterRes) resArray.get(position);
            bitmap = this.curFilterRes.getLocalImageBitmap();
            mGPUOverlay.setFilter(GPUFilter.createFilterForBlendType(getActivity(), this.curFilterRes.getFilterType(), bitmap));
            mGPUOverlay.getFilter().setMix(filterPercent);
            mGPUOverlay.requestRender();
        }else {
            GPUFilterRes res = new GPUFilterRes();
            res.setContext(requireContext());
            res.setFilterType(GPUFilterType.BLEND_SCREEN);
            mGPUOverlay.setFilter(GPUFilter.createFilterForBlendType(getActivity(), res.getFilterType(), mBitmapDefault));
            mGPUOverlay.getFilter().setMix(0f);
            mGPUOverlay.requestRender();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_adjust_1:
                mPositionCurrentTab = TAB_ADJSUT;
                mCurAdjust = 1;
                setProgressBar(100, 0, currentAdjust1);
                withBrightnessProgressChange(currentAdjust1);
                break;

            case R.id.img_adjust_2:
                mPositionCurrentTab = TAB_ADJSUT;
                mCurAdjust = 2;
                setProgressBar(100, 0, currentAdjust2);
                withConstrastProgressChange(currentAdjust2);
                break;

            case R.id.img_adjust_3:
                mPositionCurrentTab = TAB_ADJSUT;
                mCurAdjust = 3;
                setProgressBar(100, 0, currentAdjust3);
                withSaturationProgressChange(currentAdjust3);
                break;

            case R.id.img_adjust_4:
                mPositionCurrentTab = TAB_ADJSUT;
                mCurAdjust = 4;
                setProgressBar(100, 0, currentAdjust4);
                withExposureProgressChange(currentAdjust4);
                break;

            case R.id.img_adjust_5:
                mPositionCurrentTab = TAB_ADJSUT;
                mCurAdjust = 5;
                setProgressBar(50, 25, currentAdjust5);
                withRProgressChange(currentAdjust5);
                break;

            case R.id.img_adjust_6:
                mPositionCurrentTab = TAB_ADJSUT;
                mCurAdjust = 6;
                setProgressBar(100, 0, currentAdjust6);
                withHighlightShadowProgressChange(currentAdjust6);
                break;

            case R.id.img_adjust_7:
                mPositionCurrentTab = TAB_ADJSUT;
                mCurAdjust = 7;
                setProgressBar(100, 0, currentAdjust7);
                withVigneeteProgressChange(currentAdjust7);
                break;

            case R.id.btn_adjust:
                mPositionCurrentTab = TAB_ADJSUT;
                selectTab();
                mStringOverlay = "ADJUST";
                getKeyFromLibrary();
                mCurAdjust = 1;
                setProgressBar(100, 0, currentAdjust1);
                withBrightnessProgressChange(currentAdjust1);
//                settingAdjust(mCurAdjust);
                break;

            case R.id.btn_overlay_exit:
                requireActivity().getSupportFragmentManager().popBackStack();
                break;

            case R.id.btn_overlay_save:
                if(mPositionCurrentTab == TAB_ADJSUT){
                    GPUFilter.asyncFilterForFilter(mBitmapOverlay, mGPUOverlay.getFilter(), new OnPostFilteredListener() {
                        @Override
                        public void postFiltered(Bitmap bitmap) {
                            filteredToApp(bitmap);
                        }
                    });
                    return;
                }
                if(curFilterRes == null){
                    requireActivity().getSupportFragmentManager().popBackStack();
                    return;
                }
                if(mPositionCurrentTab == TAB_TEXTURE){
                    if(mCurrentPositionTexture == 0 || mCurrentPositionTexture == -1){
                        requireActivity().getSupportFragmentManager().popBackStack();
                        return;
                    }
                }else if(mPositionCurrentTab == TAB_NOISE){
                    if(mCurrentPositionNoise == 0 || mCurrentPositionNoise == -1){
                        requireActivity().getSupportFragmentManager().popBackStack();
                        return;
                    }
                }
                GPUImageFilter filter = GPUFilter.createFilterForBlendType(getActivity(), curFilterRes.getFilterType(), bitmap);
                filter.setMix(filterPercent);
                GPUFilter.asyncFilterForFilter(mBitmapOverlay, filter, new OnPostFilteredListener() {
                    public void postFiltered(Bitmap bitmap) {
                        filteredToApp(bitmap);
                    }
                });

                break;

            case R.id.txt_noise:
                mPositionCurrentTab = TAB_NOISE;
                selectTab();
                mStringOverlay = "NOISE";
                mSeekBarOverlay.setProgress(mProgressNoise);
                getKeyFromLibrary();
                break;

            case R.id.txt_texture:
                mPositionCurrentTab = TAB_TEXTURE;
                selectTab();
                mStringOverlay = "TEXTURE";
                mSeekBarOverlay.setProgress(mProgressText);
                getKeyFromLibrary();
                break;
        }
        updateIconMenu();
    }

    private void filteredToApp(Bitmap bmp) {
        if (!(bmp == mBitmapOverlay || bmp == null || bmp.isRecycled())) {
            if (handleBackOverlay != null) {
                mBitmapOverlay = bmp;
                handleBackOverlay.backPressOverlay(mBitmapOverlay);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        }
    }

    private void setProgressBar(int max, int min, int progress) {
        mSeekbarAdjust.setMax(max);
        mSeekbarAdjust.setProgress(progress);
    }

    private void updateIconMenu(){
        txtBright.setTextColor(ContextCompat.getColor(requireContext(),R.color.white));
        txtContrast.setTextColor(ContextCompat.getColor(requireContext(),R.color.white));
        txtWarmth.setTextColor(ContextCompat.getColor(requireContext(),R.color.white));
        txtFade.setTextColor(ContextCompat.getColor(requireContext(),R.color.white));
        txtVignette.setTextColor(ContextCompat.getColor(requireContext(),R.color.white));
        txtHue.setTextColor(ContextCompat.getColor(requireContext(),R.color.white));
        txtTone.setTextColor(ContextCompat.getColor(requireContext(),R.color.white));
        icBright.setColorFilter(ContextCompat.getColor(requireContext(),R.color.white));
        icContrast.setColorFilter(ContextCompat.getColor(requireContext(),R.color.white));
        icWarmth.setColorFilter(ContextCompat.getColor(requireContext(),R.color.white));
        icFade.setColorFilter(ContextCompat.getColor(requireContext(),R.color.white));
        icVignette.setColorFilter(ContextCompat.getColor(requireContext(),R.color.white));
        icHue.setColorFilter(ContextCompat.getColor(requireContext(),R.color.white));
        icTone.setColorFilter(ContextCompat.getColor(requireContext(),R.color.white));
        switch (mCurAdjust){
            case 1:
                txtBright.setTextColor(ContextCompat.getColor(requireContext(),R.color.color_blue));
                icBright.setColorFilter(ContextCompat.getColor(requireContext(),R.color.color_blue));
                break;

            case 2:
                txtContrast.setTextColor(ContextCompat.getColor(requireContext(),R.color.color_blue));
                icContrast.setColorFilter(ContextCompat.getColor(requireContext(),R.color.color_blue));
                break;

            case 3:
                txtHue.setTextColor(ContextCompat.getColor(requireContext(),R.color.color_blue));
                icHue.setColorFilter(ContextCompat.getColor(requireContext(),R.color.color_blue));
                break;

            case 4:
                txtTone.setTextColor(ContextCompat.getColor(requireContext(),R.color.color_blue));
                icTone.setColorFilter(ContextCompat.getColor(requireContext(),R.color.color_blue));
                break;

            case 5:
                txtWarmth.setTextColor(ContextCompat.getColor(requireContext(),R.color.color_blue));
                icWarmth.setColorFilter(ContextCompat.getColor(requireContext(),R.color.color_blue));
                break;

            case 6:
                txtFade.setTextColor(ContextCompat.getColor(requireContext(),R.color.color_blue));
                icFade.setColorFilter(ContextCompat.getColor(requireContext(),R.color.color_blue));
                break;

            case 7:
                txtVignette.setTextColor(ContextCompat.getColor(requireContext(),R.color.color_blue));
                icVignette.setColorFilter(ContextCompat.getColor(requireContext(),R.color.color_blue));
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
        if(seekBar.getId() == R.id.overlay_seekbar){
//            if(mPositionItemOverlay == DEFAULT_ITEM_POSITION)return;

            if (mPositionCurrentTab == TAB_TEXTURE) {
                mProgressText = value;
            }else {
                mProgressNoise = value;
            }

            if (mGPUOverlay.getFilter() != null) {
                filterPercent = ((float) value) / 100.0f;
                mGPUOverlay.getFilter().setMix(filterPercent);
                mGPUOverlay.requestRender();
            }
        }else if(seekBar.getId() == R.id.adjust_seekbar){
            settingAdjust(mCurAdjust,value);
        }
    }

    @Override
    public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

    }

    private void settingAdjust(int position){
        switch (position) {
            case 1:
                setProgressBar(100, 0, currentAdjust1);
//                withBrightnessProgressChange(currentAdjust1);
                break;
            case 2:
                setProgressBar(100, 0, currentAdjust2);
//                withConstrastProgressChange(currentAdjust2);
                break;
            case 3:
                setProgressBar(100, 0, currentAdjust3);
//                withSaturationProgressChange(currentAdjust3);
                break;
            case 4:
                setProgressBar(100, 0, currentAdjust4);
//                withExposureProgressChange(currentAdjust4);
                break;
            case 5:
                setProgressBar(50, 25, currentAdjust5);
//                withRProgressChange(currentAdjust5);
                break;
            case 6:
                setProgressBar(100, 0, currentAdjust6);
//                withHighlightShadowProgressChange(currentAdjust6);
                break;
            case 7:
                setProgressBar(100, 0, currentAdjust7);
//                withVigneeteProgressChange(currentAdjust7);
                break;
        }
    }

    private void settingAdjust(int position, int value){
        switch (position) {
            case 1:
                withBrightnessProgressChange(value);
                currentAdjust1 = value;
                break;
            case 2:
                withConstrastProgressChange(value);
                currentAdjust2 = value;
                break;
            case 3:
                withSaturationProgressChange(value);
                currentAdjust3 = value;
                break;
            case 4:
                withExposureProgressChange(value);
                currentAdjust4 = value;
                break;
            case 5:
                withRProgressChange(value);
                currentAdjust5 = value;
                break;
            case 6:
                withHighlightShadowProgressChange(value);
                currentAdjust6 = value;
                break;
            case 7:
                withVigneeteProgressChange(value);
                currentAdjust7 = value;
                break;
        }
    }

    private void withVigneeteProgressChange(int progress) {
        float realValue = GPUAdjustRange.getVigneeteRange(progress);
        boolean hasFilter = false;
        for (GPUImageFilter gpuImageFilter : ((GPUImageFilterGroup) mGPUOverlay.getFilter()).getFilters()) {
            if (gpuImageFilter instanceof GPUImageVignetteFilter) {
                ((GPUImageVignetteFilter) gpuImageFilter).setVignetteStart(realValue);
                hasFilter = true;
            }
        }
        if (!hasFilter) {
            GPUImageVignetteFilter filter = new GPUImageVignetteFilter();
            filter.setVignetteStart(0.75f);
            this.strengthfilter.addFilter(filter);
            this.mGPUOverlay.setFilter(this.strengthfilter);
        }
        this.mGPUOverlay.requestRender();
    }

    private void withHighlightShadowProgressChange(int progress) {
        float realValue = GPUAdjustRange.getShadowRange(progress);
        boolean hasFilter = false;
        for (GPUImageFilter gpuImageFilter : ((GPUImageFilterGroup) mGPUOverlay.getFilter()).getFilters()) {
            if (gpuImageFilter instanceof GPUImageHighlightShadowFilter) {
                ((GPUImageHighlightShadowFilter) gpuImageFilter).setShadows(realValue);
                hasFilter = true;
            }
        }
        if (!hasFilter) {
            this.strengthfilter.addFilter(new GPUImageHighlightShadowFilter(realValue, 1.0f));
            mGPUOverlay.setFilter(this.strengthfilter);
        }
        mGPUOverlay.requestRender();

    }

    private void withExposureProgressChange(int progress) {
        float realValue = GPUAdjustRange.getExposureRange(progress);
        boolean hasFilter = false;
        for (GPUImageFilter gpuImageFilter : ((GPUImageFilterGroup) mGPUOverlay.getFilter()).getFilters()) {
            if (gpuImageFilter instanceof GPUImageExposureFilter) {
                ((GPUImageExposureFilter) gpuImageFilter).setExposure(realValue);
                hasFilter = true;
            }
        }
        if (!hasFilter) {
            strengthfilter.addFilter(new GPUImageExposureFilter(realValue));
            mGPUOverlay.setFilter(strengthfilter);
        }
        mGPUOverlay.requestRender();
    }

    private void withSaturationProgressChange(int progress) {
        float realValue = GPUAdjustRange.getSaturationRange(progress);
        boolean hasFilter = false;
        for (GPUImageFilter gpuImageFilter : ((GPUImageFilterGroup) mGPUOverlay.getFilter()).getFilters()) {
            if (gpuImageFilter instanceof GPUImageSaturationFilter) {
                ((GPUImageSaturationFilter) gpuImageFilter).setSaturation(realValue);
                hasFilter = true;
            }
        }
        if (!hasFilter) {
            strengthfilter.addFilter(new GPUImageSaturationFilter(realValue));
            mGPUOverlay.setFilter(strengthfilter);
        }
        mGPUOverlay.requestRender();
    }

    private void withRProgressChange(int progress) {
        float realValue = GPUAdjustRange.getRRange(progress);
        boolean hasFilter = false;
        for (GPUImageFilter gpuImageFilter : ((GPUImageFilterGroup) this.mGPUOverlay.getFilter()).getFilters()) {
            if (gpuImageFilter instanceof GPUImageRGBFilter) {
                ((GPUImageRGBFilter) gpuImageFilter).setRed(realValue);
                hasFilter = true;
            }
        }
        if (!hasFilter) {
            this.strengthfilter.addFilter(new GPUImageRGBFilter(realValue, 1.0f, 1.0f));
            this.mGPUOverlay.setFilter(this.strengthfilter);
        }
        this.mGPUOverlay.requestRender();
    }

    private void withConstrastProgressChange(int progress) {
        float realValue = GPUAdjustRange.getContrastRange(progress);
        boolean hasFilter = false;
        for (GPUImageFilter gpuImageFilter : ((GPUImageFilterGroup) mGPUOverlay.getFilter()).getFilters()) {
            if (gpuImageFilter instanceof GPUImageContrastFilter) {
                ((GPUImageContrastFilter) gpuImageFilter).setContrast(realValue);
                hasFilter = true;
            }
        }
        if (!hasFilter) {
            strengthfilter.addFilter(new GPUImageContrastFilter(realValue));
            mGPUOverlay.setFilter(strengthfilter);
        }
        mGPUOverlay.requestRender();

    }

    private void withBrightnessProgressChange(int progress) {
        float realValue = GPUAdjustRange.getBrightnessRange(progress);
        boolean hasFilter = false;
        for (GPUImageFilter gpuImageFilter : ((GPUImageFilterGroup) mGPUOverlay.getFilter()).getFilters()) {
            if (gpuImageFilter instanceof GPUImageBrightnessFilter) {
                ((GPUImageBrightnessFilter) gpuImageFilter).setBrightness(realValue);
                hasFilter = true;
            }
        }
        if (!hasFilter) {
            strengthfilter.addFilter(new GPUImageBrightnessFilter(realValue));
            mGPUOverlay.setFilter(strengthfilter);
        }
        mGPUOverlay.requestRender();
    }

    public interface HandleBackOverlay {
        void backPressOverlay(Bitmap bitmap);
    }
}

