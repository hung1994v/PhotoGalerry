package com.photo.splashfunphoto.fragment;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.isseiaoki.simplecropview.CropImageView;
import com.photo.gallery.R;
import com.photo.splashfunphoto.adapter.RatioCropAdapter;
import com.photo.splashfunphoto.dialog.ProgressDialogFragment;
import com.photo.splashfunphoto.utils.Statics;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CropFragment extends BaseFragment implements View.OnClickListener, RatioCropAdapter.OnItemMirrorListener {
    private static final String KEY_FRAME_RECT = "FrameRect";
    private RectF mFrameRect = null;
    private CropImageView mCropView;
    private Uri mSourceUri = null;
    private Bitmap.CompressFormat mCompressFormat = Bitmap.CompressFormat.PNG;
    private OnCropListener mCropListener;
    private static final String PROGRESS_DIALOG = "ProgressDialog";
    private Bitmap mBitmapCrop;
    private RecyclerView mRecyclerRatio;
    private RatioCropAdapter mRatioAdapterCrop;
    public static int DEFAULT_RATIO_CROP = 0;
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

    @Override
    public void backPressed() {

    }

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
        if (savedInstanceState != null) {
            // restore data
            mFrameRect = savedInstanceState.getParcelable(KEY_FRAME_RECT);
        }
        mCropView.setImageBitmap(mBitmapCrop);
        initListRatio();
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


    private void initView() {
        mCropView = (CropImageView) getView().findViewById(R.id.cropImageView);
        mRecyclerRatio = (RecyclerView) getView().findViewById(R.id.recycler_ratio);
        getView().findViewById(R.id.btn_crop_exit).setOnClickListener(this);
        getView().findViewById(R.id.btn_crop_done).setOnClickListener(this);
    }

    private void initListRatio(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerRatio.setLayoutManager(layoutManager);
        mRatioAdapterCrop = new RatioCropAdapter(getActivity(), Statics.getListRatioCropType(),mRecyclerRatio,DEFAULT_RATIO_CROP).setOnItemMirrorListener(this);
        mRecyclerRatio.setAdapter(mRatioAdapterCrop);
    }

    public void showProgress() {
        ProgressDialogFragment f = ProgressDialogFragment.getInstance();
        getFragmentManager().beginTransaction().add(f, PROGRESS_DIALOG).commitAllowingStateLoss();
    }

    public void dismissProgress() {
        if (!isResumed()) return;
        FragmentManager manager = getFragmentManager();
        if (manager == null) return;
        ProgressDialogFragment f = (ProgressDialogFragment) manager.findFragmentByTag(PROGRESS_DIALOG);
        if (f != null) {
            getFragmentManager().beginTransaction().remove(f).commitAllowingStateLoss();
        }
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
                getActivity().getSupportFragmentManager().popBackStack();
                break;

            case R.id.btn_crop_exit:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }
    }

    @Override
    public void onItemMirrorRatioClickListener(int pos) {
        if(Statics.getListRatioCropType().get(pos).getRatioW() == Statics.FIT_IMAGE){
            mCropView.setCropMode(CropImageView.CropMode.FIT_IMAGE);
        }else if(Statics.getListRatioCropType().get(pos).getRatioW() == Statics.CIRCLE){
            mCropView.setCropMode(CropImageView.CropMode.CIRCLE);
        }else if(Statics.getListRatioCropType().get(pos).getRatioW() == Statics.FREE_IMAGE){
            mCropView.setCropMode(CropImageView.CropMode.FREE);
        }else {
            mCropView.setCustomRatio((int)Statics.getListRatioCropType().get(pos).getRatioW(),(int)Statics.getListRatioCropType().get(pos).getRatioH());
        }
    }

    public interface OnCropListener {
        void onCropDone(Bitmap s);
    }
}
