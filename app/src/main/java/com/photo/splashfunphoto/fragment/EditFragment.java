package com.photo.splashfunphoto.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;


import com.photo.gallery.R;
import com.photo.splashfunphoto.EditPhotoActivity;
import com.photo.splashfunphoto.utils.BitmapSwap;
import com.photo.splashfunphoto.utils.ResizeImage;

import java.io.ByteArrayOutputStream;

public class EditFragment extends BaseFragment implements View.OnClickListener, CropFragment.OnCropListener {

    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage";
    private Bitmap mBitmapEdit;
    private Matrix matrix = new Matrix();
    private DisplayMetrics mDisplayMetrics;
    private String mImagePath;
    private String mImageInputPath, mImageOutputPath;
    private Handler mHandler = new Handler();
    private int currentexit;
    private HandleBackEdit handleBackEdit;
    private ImageView mImEdit;

    public EditFragment() {
        // Required empty public constructor
    }

    public static EditFragment newInstance(Bundle bundle, HandleBackEdit backEdit) {
        EditFragment fragment = new EditFragment();
        fragment.handleBackEdit = backEdit;
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
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        setImageView();
    }

    private void initView() {
        mImEdit = (ImageView) getView().findViewById(R.id.img_edit);

        getView().findViewById(R.id.btn_crop).setOnClickListener(this);
        getView().findViewById(R.id.btn_rotate_left).setOnClickListener(this);
        getView().findViewById(R.id.btn_rotate_right).setOnClickListener(this);
        getView().findViewById(R.id.btn_flip_left).setOnClickListener(this);
        getView().findViewById(R.id.btn_flip_top).setOnClickListener(this);
        getView().findViewById(R.id.btn_edit_exit).setOnClickListener(this);
        getView().findViewById(R.id.btn_edit_save).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Bundle bundle = null;
        switch (view.getId()) {
            case R.id.btn_crop:
                Bitmap mBitmap = Bitmap.createBitmap(mBitmapEdit, 0, 0, mBitmapEdit.getWidth(), mBitmapEdit.getHeight(), matrix, true);
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.content_main, CropFragment.newInstance(mBitmap, this))
                        .addToBackStack(CropFragment.class.getSimpleName())
                        .commit();
                break;
            case R.id.btn_rotate_left:
                rotateLeft();
                break;
            case R.id.btn_rotate_right:
                rotateRigtht();
                break;
            case R.id.btn_flip_left:
                flip();
                break;
            case R.id.btn_flip_top:
                flip1();
                break;
            case R.id.btn_edit_exit:
                requireActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.btn_edit_save:
                mBitmapEdit = Bitmap.createBitmap(mBitmapEdit, 0, 0, mBitmapEdit.getWidth(), mBitmapEdit.getHeight(), matrix, true);
                BitmapSwap.mBitmapNew = mBitmapEdit;
                if (handleBackEdit != null) {
                    handleBackEdit.backPressEdit();
                    requireActivity().getSupportFragmentManager().popBackStack();
                }
                break;
        }
    }

    private void rotateRigtht() {
        matrix = new Matrix();
        matrix.postRotate(+90.0f, (float) (mBitmapEdit.getWidth() / 2), (float) (mBitmapEdit.getHeight() / 2));
        mBitmapEdit = Bitmap.createBitmap(mBitmapEdit, 0, 0, mBitmapEdit.getWidth(), mBitmapEdit.getHeight(), matrix, true);
        mImEdit.setImageBitmap(mBitmapEdit);
        matrix = mImEdit.getImageMatrix();
        matrix.set(matrix);
    }

    private void rotateLeft() {
        matrix = new Matrix();
        matrix.postRotate(-90.0f, (float) (mBitmapEdit.getWidth() / 2), (float) (mBitmapEdit.getHeight() / 2));
        mBitmapEdit = Bitmap.createBitmap(mBitmapEdit, 0, 0, mBitmapEdit.getWidth(), mBitmapEdit.getHeight(), matrix, true);
        mImEdit.setImageBitmap(mBitmapEdit);
        matrix = mImEdit.getImageMatrix();
        matrix.set(matrix);
    }

    /**
     * Called when the fragment is no longer in use.  This is called
     * after {@link #onStop()} and before {@link #onDetach()}.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onDestroyView() {
        //    BitmapUtil.free(mBitmapEdit);
        super.onDestroyView();
    }

    public void setImageView() {
        if (getArguments() != null) {
            mImagePath = getArguments().getString(EditPhotoActivity.PICK_IMAGE_BCROP, null);
            mDisplayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
            mBitmapEdit = new ResizeImage(getActivity()).getBitmap(mImagePath, mDisplayMetrics.widthPixels);
        } else {
            mBitmapEdit = BitmapSwap.mBitmapOld;
        }
        mImEdit.setImageBitmap(mBitmapEdit);
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

//    public Uri getImageUri(Context inContext, Bitmap inImage) {
//        Uri uri = null;
//        try {
//            File file = new File(getActivity().getFilesDir(), "crop_uri.jpeg");
//            FileOutputStream out = inContext.openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);
//            inImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
//            out.flush();
//            out.close();
//            String realPath = file.getAbsolutePath();
//            File f = new File(realPath);
//            uri = Uri.fromFile(f);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        Flog.d("getImageUri " + uri);
//        return uri;
//    }


    private void flip() {
        matrix = new Matrix();
        Bitmap bmp = mBitmapEdit;
        matrix.postScale(1, -1, (float) (bmp.getWidth() / 2), (float) (bmp.getHeight() / 2));
        matrix.postRotate(180, (float) (bmp.getWidth() / 2), (float) (bmp.getHeight() / 2));
        Bitmap bmptmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
        mBitmapEdit = bmptmp;
        mImEdit.setImageBitmap(mBitmapEdit);
        matrix = mImEdit.getImageMatrix();
        matrix.set(matrix);

    }

    private void flip1() {
        matrix = new Matrix();
        Bitmap bmp = mBitmapEdit;
        matrix.postScale(-1, 1, (float) (bmp.getWidth() / 2), (float) (bmp.getHeight() / 2));
        matrix.postRotate(180, (float) (bmp.getWidth() / 2), (float) (bmp.getHeight() / 2));
        Bitmap bmptmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
        mBitmapEdit = bmptmp;
        mImEdit.setImageBitmap(mBitmapEdit);
        matrix = mImEdit.getImageMatrix();
        matrix.set(matrix);

    }

    @Override
    public void onCropDone(Bitmap s) {
//        mDisplayMetrics = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
//        mBitmapEdit = new ResizeImage(getActivity()).getBitmap(s, mDisplayMetrics.widthPixels);
        mBitmapEdit = getResizedBitmap(s);
        mImEdit.setImageBitmap(mBitmapEdit);
    }

    public Bitmap getResizedBitmap(Bitmap bm) {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        float ratioW = bm.getWidth() / mDisplayMetrics.widthPixels;
        float ratioH = bm.getHeight() / mDisplayMetrics.heightPixels;
        if (ratioW > ratioH) {
            int width = mDisplayMetrics.widthPixels;
            int height = bm.getHeight() * mDisplayMetrics.widthPixels / bm.getWidth();
            return Bitmap.createScaledBitmap(bm, width, height, false);
        } else {
            int height = mDisplayMetrics.heightPixels;
            int width = bm.getWidth() * mDisplayMetrics.heightPixels / bm.getHeight();
            return Bitmap.createScaledBitmap(bm, width, height, false);
        }
    }

    public interface HandleBackEdit {
        void backPressEdit();
    }

}
