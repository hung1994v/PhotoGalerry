package com.photo.gallery.fragments.options;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.util.Logger;
import com.photo.gallery.R;
import com.photo.gallery.fragments.BaseFragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class CropFragment extends BaseFragment implements View.OnClickListener {
    private static final String KEY_FRAME_RECT = "FrameRect";
    private RectF mFrameRect = null;
    private CropImageView mCropView;
    //    private CompositeDisposable mDisposable = new CompositeDisposable();
    private Uri mSourceUri = null;
    private AdView mAdView;
    private Bitmap.CompressFormat mCompressFormat = Bitmap.CompressFormat.PNG;
    private OnCropListener mCropListener;
    private static final String PROGRESS_DIALOG = "ProgressDialog";
    private Bitmap mBitmapCrop;

    public CropFragment() {

    }

    public static CropFragment newInstance(Bitmap bitmap, OnCropListener listener) {
        CropFragment fragment = new CropFragment();
        fragment.mCropListener = listener;
        fragment.mBitmapCrop = bitmap;
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_FRAME_RECT, mCropView.getActualCropRect());
//        outState.putParcelable(KEY_SOURCE_URI, mCropView.getSourceUri());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    @Override
//    public void backPressed() {
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crop, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
//        initAdmob();
        if (savedInstanceState != null) {
            // restore data
            mFrameRect = savedInstanceState.getParcelable(KEY_FRAME_RECT);
//            mSourceUri = savedInstanceState.getParcelable(KEY_SOURCE_URI);
        }
        mCropView.setImageBitmap(mBitmapCrop);
    }

    public static Uri getUriFromDrawableResId(Context context, int drawableResId) {
        StringBuilder builder = new StringBuilder().append(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .append("://")
                .append(context.getResources().getResourcePackageName(drawableResId))
                .append("/")
                .append(context.getResources().getResourceTypeName(drawableResId))
                .append("/")
                .append(context.getResources().getResourceEntryName(drawableResId));
        return Uri.parse(builder.toString());
    }


    private void initAdmob() {
//        mAdView = getView().findViewById(R.id.crop_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }



    private void initView() {
        mCropView = getView().findViewById(R.id.cropImageView);
        getView().findViewById(R.id.buttonFitImage).setOnClickListener(this);
        getView().findViewById(R.id.button1_1).setOnClickListener(this);
        getView().findViewById(R.id.button2_3).setOnClickListener(this);
        getView().findViewById(R.id.button3_2).setOnClickListener(this);
        getView().findViewById(R.id.button3_4).setOnClickListener(this);
        getView().findViewById(R.id.button4_3).setOnClickListener(this);
        getView().findViewById(R.id.button4_5).setOnClickListener(this);
        getView().findViewById(R.id.button5_4).setOnClickListener(this);
        getView().findViewById(R.id.button5_7).setOnClickListener(this);
        getView().findViewById(R.id.button7_5).setOnClickListener(this);
        getView().findViewById(R.id.button9_16).setOnClickListener(this);
        getView().findViewById(R.id.button16_9).setOnClickListener(this);
        getView().findViewById(R.id.buttonCircle).setOnClickListener(this);
        getView().findViewById(R.id.btn_back).setOnClickListener(this);
        getView().findViewById(R.id.btn_crop_done).setOnClickListener(this);
    }







    public Uri createSaveUri() {
        return createNewUri(getContext(), mCompressFormat);
    }

    public static Uri createNewUri(Context context, Bitmap.CompressFormat format) {
        long currentTimeMillis = System.currentTimeMillis();
        Date today = new Date(currentTimeMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String title = dateFormat.format(today);
        String dirPath = getDirPath();
        String fileName = "scv" + title + "." + getMimeType(format);
        String path = dirPath + "/" + fileName;
        File file = new File(path);
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/" + getMimeType(format));
        values.put(MediaStore.Images.Media.DATA, path);
        long time = currentTimeMillis / 1000;
        values.put(MediaStore.MediaColumns.DATE_ADDED, time);
        values.put(MediaStore.MediaColumns.DATE_MODIFIED, time);
        if (file.exists()) {
            values.put(MediaStore.Images.Media.SIZE, file.length());
        }

        ContentResolver resolver = context.getContentResolver();
        Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Logger.i("SaveUri = " + uri);
        return uri;
    }

    public static String getDirPath() {
        String dirPath = "";
        File imageDir = null;
        File extStorageDir = Environment.getExternalStorageDirectory();
        if (extStorageDir.canWrite()) {
            imageDir = new File(extStorageDir.getPath() + "/simplecropview");
        }
        if (imageDir != null) {
            if (!imageDir.exists()) {
                imageDir.mkdirs();
            }
            if (imageDir.canWrite()) {
                dirPath = imageDir.getPath();
            }
        }
        return dirPath;
    }

    public static String getMimeType(Bitmap.CompressFormat format) {
        switch (format) {
            case JPEG:
                return "jpeg";
            case PNG:
                return "png";
        }
        return "png";
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_crop_done:
                if (mCropListener != null) {
                    mCropListener.onCropDone(mCropView.getCroppedBitmap());
                }
//                mDisposable.add(cropImage());
                break;
            case R.id.buttonFitImage:
                mCropView.setCropMode(CropImageView.CropMode.FIT_IMAGE);
                break;
            case R.id.button1_1:
                mCropView.setCropMode(CropImageView.CropMode.SQUARE);
                break;
            case R.id.button2_3:
                mCropView.setCustomRatio(2, 3);
                break;
            case R.id.button3_2:
                mCropView.setCustomRatio(3, 2);
                break;
            case R.id.button3_4:
                mCropView.setCropMode(CropImageView.CropMode.RATIO_3_4);
                break;
            case R.id.button4_3:
                mCropView.setCropMode(CropImageView.CropMode.RATIO_4_3);
                break;
            case R.id.button4_5:
                mCropView.setCustomRatio(4, 5);
                break;
            case R.id.button5_4:
                mCropView.setCustomRatio(5, 4);
                break;
            case R.id.button5_7:
                mCropView.setCustomRatio(5, 7);
                break;
            case R.id.button7_5:
                mCropView.setCustomRatio(7, 5);
                break;
            case R.id.button9_16:
                mCropView.setCropMode(CropImageView.CropMode.RATIO_9_16);
                break;
            case R.id.button16_9:
                mCropView.setCropMode(CropImageView.CropMode.RATIO_16_9);
                break;
            case R.id.buttonCircle:
                mCropView.setCropMode(CropImageView.CropMode.CIRCLE);
                break;
            case R.id.btn_back:
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
                break;
        }
    }

    public interface OnCropListener {
        void onCropDone(Bitmap s);
    }
}
