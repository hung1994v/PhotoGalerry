package com.photo.gallery.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.photo.gallery.R;
import com.photo.gallery.activities.EditPhotoActivity;
import com.photo.gallery.databinding.FragmentPhotoViewerBinding;
import com.photo.gallery.models.FileItem;
import com.photo.gallery.utils.ConstValue;
import com.photo.gallery.utils.CustomToast;
import com.photo.gallery.utils.DateUtils;
import com.photo.gallery.utils.DbUtils;
import com.photo.gallery.utils.Flog;
import com.photo.gallery.utils.ResizeImage;
import com.photo.gallery.utils.SharedPrefUtil;
import com.photo.gallery.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import bsoft.com.lib_filter.filter.gpu.util.BitmapUtil;

/**
 * Created by Tung on 3/23/2018.
 */

public class PhotoViewerFragment extends BaseFragment implements View.OnClickListener {

    public static final String PATH_TO_FAVOURITE_FOLDER = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator
            + "Favourites";
    private static final String TAG = PhotoViewerFragment.class.getSimpleName();
    private static final boolean FLAG_LOAD_GLIDE = false;
    private final boolean OLD_CODE = false;
    private View btnBack, btnRotate, btnDelete, btnShare, btnMore;
    private ImageView btnFavourite;
    private TextView tvNamePhoto, tvDatePhoto;
    private FileItem mFileItem = null;
    private OnPhotoViewerListener listener = null;
    private boolean mIsFavourited = false;
    private boolean isClick;
    private FragmentPhotoViewerBinding binding;
    private AlertDialog mDialogDelete;
    private AlertDialog mDialogConfirmSetWall;
    private AlertDialog mDialogSetHome;
    private AlertDialog mDialogSetLocscren;
    private AlertDialog.Builder builder;
    private ProgressDialog progressDialog;
    private BottomSheetDialog mBottomSheetDialog;

    public static PhotoViewerFragment newInstance(FileItem file) {

        PhotoViewerFragment fragment = new PhotoViewerFragment();
        fragment.mFileItem = file;
        return fragment;
    }

    public void showBottomDialog(){
        Flog.d(TAG, "actionOpenWith=" );
        mBottomSheetDialog = new BottomSheetDialog(mContext);
        mBottomSheetDialog.setContentView(getLayoutInflater().inflate(R.layout.bottom_set_wall, null));
        mBottomSheetDialog.show();
        mBottomSheetDialog.findViewById(R.id.home_screen).setOnClickListener(v -> {
            if(mFileItem.isImage){
                confirmSethome();
            }else {
                CustomToast.showContent(mContext,mContext.getString(R.string.no_file_found));
            }


        });
        mBottomSheetDialog.findViewById(R.id.lock_screen).setOnClickListener(v -> {
            if(mFileItem.isImage){
                confirmLockScreen();
            }else {
                CustomToast.showContent(mContext,mContext.getString(R.string.no_file_found));
            }

        });

        mBottomSheetDialog.findViewById(R.id.lock_home_screen_button).setOnClickListener(v -> {
            if(mFileItem.isImage){
                confirmSetWall();

            }else {
                CustomToast.showContent(mContext,mContext.getString(R.string.no_file_found));
            }

        });
    }


    private void confirmSethome() {
        mDialogSetHome = null;
        if(builder == null){
            builder = new AlertDialog.Builder(mContext);
            builder.setTitle(mContext.getResources().getString(R.string.home_screen))
                    .setMessage(mContext.getResources().getString(R.string.set_homeScreen_message))
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, (dialogInterface, i) ->
                    { mBottomSheetDialog.dismiss();
                        new SetWallHomeScreen().execute();
                        builder = null;
                    })
                    .setNegativeButton(android.R.string.no, (dialogInterface, i) -> builder = null);
            mDialogSetHome = builder.create();
            if (mDialogSetHome.getWindow() != null) {
                mDialogSetHome.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
            }
        }

        if(mDialogSetHome != null && !mDialogSetHome.isShowing()){
            mDialogSetHome.show();
        }

    }


    private void confirmLockScreen() {
        if(builder == null){
            builder = new AlertDialog.Builder(mContext);
            builder.setTitle(mContext.getResources().getString(R.string.lockscreen))
                    .setMessage(mContext.getResources().getString(R.string.set_lockscreen_message))
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, (dialogInterface, i) ->
                    { mBottomSheetDialog.dismiss();
                        new setWallLockScreen().execute();
                        builder = null;
                    })
                    .setNegativeButton(android.R.string.no, (dialogInterface, i) -> builder = null);
            mDialogSetLocscren = builder.create();
            if (mDialogSetLocscren.getWindow() != null) {
                mDialogSetLocscren.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
            }
        }

        if(mDialogSetLocscren != null && !mDialogSetLocscren.isShowing()){
            mDialogSetLocscren.show();
        }

    }


    private void confirmSetWall() {
        mDialogConfirmSetWall = null;
        if(builder == null){
            builder = new AlertDialog.Builder(mContext);
            builder.setTitle(mContext.getResources().getString(R.string.set_wall))
                    .setMessage(mContext.getResources().getString(R.string.set_image_message))
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, (dialogInterface, i) ->
                    {
                        mBottomSheetDialog.dismiss();
                        new setWallpaperHomeAndLock().execute();
                        builder = null;
                    })
                    .setNegativeButton(android.R.string.no, (dialogInterface, i) -> builder = null);
            mDialogConfirmSetWall = builder.create();
            if (mDialogConfirmSetWall.getWindow() != null) {
                mDialogConfirmSetWall.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
            }
        }

        if(mDialogConfirmSetWall != null && !mDialogConfirmSetWall.isShowing()){
            mDialogConfirmSetWall.show();
        }

    }

    @SuppressLint("StaticFieldLeak")
    private class setWallpaperHomeAndLock extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            if (progressDialog == null) {
                progressDialog = MyProgressDialogLoading.newInstance(getContext(), mContext.getResources().getString(R.string.please_wait));
                progressDialog.show();}
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            WallpaperManager myWallpaperManager
                    = WallpaperManager.getInstance(mContext);
            try {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if(mFileItem!=null){
                    myWallpaperManager.setBitmap(getBitmapFromPath(mFileItem.path));
                }else {
                    CustomToast.showContent(mContext,mContext.getString(R.string.no_file_found));
                }

//                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
            CustomToast.showContent(mContext,mContext.getString(R.string.set_wall_success));
            super.onPostExecute(aVoid);
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private Bitmap getBitmapFromPath(String path) {
        if(getActivity()!=null){
            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager window = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            window.getDefaultDisplay().getMetrics(metrics);
            Point size = new Point();
            window.getDefaultDisplay().getRealSize(size);
            int Width = size.x;
            int Height = size.y;
            return new ResizeImage(mContext).getBitmap(path, Width, Height);
        }else
            return null;
    }

    @SuppressLint("StaticFieldLeak")
    private class setWallLockScreen extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            if (progressDialog == null) {
                progressDialog = MyProgressDialogLoading.newInstance(getContext(), mContext.getResources().getString(R.string.please_wait));
                progressDialog.show();}
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            WallpaperManager myWallpaperManager
                    = WallpaperManager.getInstance(mContext);
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if(mFileItem.path!=null){
                        myWallpaperManager.setBitmap(getBitmapFromPath(mFileItem.path),null,true,WallpaperManager.FLAG_LOCK);
                    }else {
                        CustomToast.showContent(mContext,mContext.getString(R.string.no_file_found));
                    }

                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
            CustomToast.showContent(mContext,mContext.getString(R.string.set_wall_success));
            super.onPostExecute(aVoid);
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class SetWallHomeScreen extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            if (progressDialog == null) {
                progressDialog = MyProgressDialogLoading.newInstance(getContext(), mContext.getResources().getString(R.string.please_wait));
                progressDialog.show();}
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            WallpaperManager myWallpaperManager
                    = WallpaperManager.getInstance(mContext);
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if(mFileItem.path!=null){
                        myWallpaperManager.setBitmap(getBitmapFromPath(mFileItem.path),null,true,WallpaperManager.FLAG_SYSTEM);
                    }else {
                        CustomToast.showContent(mContext,mContext.getString(R.string.no_file_found));
                    }

                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
            CustomToast.showContent(mContext,mContext.getString(R.string.set_wall_success));
            super.onPostExecute(aVoid);
        }
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_photo_viewer,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initBannerAdmob();
        initViews();
        setValues();
//        if (listener != null) {
//            listener.onPhotoViewerFragmentReady();
//        }
    }


    private void initBannerAdmob() {
//        AdmobBannerHelper.init(mContext, (ViewGroup) getView().findViewById(R.id.admob_banner))
//                .setAdUnitId(getString(R.string.smart_banner_ad_id))
//                .setAdSize(AdSize.SMART_BANNER)
//                .loadAd();
    }

    public void clearLightStatusBar(Activity activity) {
        int defaultPrimary = ContextCompat.getColor(activity, R.color.s_black);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int flags = activity.getWindow().getDecorView().getSystemUiVisibility(); // get current flag
                flags = flags ^ View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; // use XOR here for remove LIGHT_STATUS_BAR from flags
                activity.getWindow().getDecorView().setSystemUiVisibility(flags);
            activity.getWindow().setStatusBarColor(defaultPrimary); // optional
            }
    }



    private void initViews() {

        View viewParent = getView();
        if (viewParent == null) {
            return;
        }
        viewParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // DO NOTHING.
            }
        });

        btnBack = viewParent.findViewById(R.id.btn_back);
        btnFavourite = viewParent.findViewById(R.id.btn_favourite);
//        btnRotate = viewParent.findViewById(R.id.btn_rotate);
        btnDelete = viewParent.findViewById(R.id.btn_delete);
        btnShare = viewParent.findViewById(R.id.btn_share);
        btnMore = viewParent.findViewById(R.id.btn_more);

        tvNamePhoto = (TextView) viewParent.findViewById(R.id.tv_name_photo);
        tvDatePhoto = (TextView) viewParent.findViewById(R.id.tv_date_photo);
        if (OLD_CODE) {
            File file = new File(mFileItem.path);
            Flog.d(TAG, "path parent 1=" + file.getParent());
            Flog.d(TAG, "path parent 2=" + PATH_TO_FAVOURITE_FOLDER);
            if (file.exists() && file.getParent().equals(PATH_TO_FAVOURITE_FOLDER)) {
                btnFavourite.setVisibility(View.GONE);
            } else {
                btnFavourite.setVisibility(View.VISIBLE);
                mIsFavourited = checkFileFavorited(mFileItem);
                toggleFavouriteIcon();
            }
        } else {
            mIsFavourited = checkFileFavorited1(mFileItem);
            toggleFavouriteIcon();
        }
        updateImage();

        String name = mFileItem.name;
        setNamePhoto(name);

        String dateFormated = DateUtils.getDate(Utils.parseLong(mFileItem.date_modified), DateUtils.FORMAT_DATE_2);
        setDatePhoto(dateFormated);
        if(!mFileItem.path.endsWith(".gif")){
            binding.btnEdit.setVisibility(View.VISIBLE);
        }else {
            binding.btnEdit.setVisibility(View.GONE);
        }
    }



    private boolean checkFileFavorited1(FileItem fileItem) {
        ArrayList<FileItem> list = DbUtils.parseFavourites();
        for (int i = 0; i < list.size(); i++) {
            FileItem item = list.get(i);
            if (item.equals(fileItem)) {
                return true;
            }
        }
        return false;
    }

    private void setDatePhoto(String dateFormated) {
        tvDatePhoto.setText(dateFormated);
    }

    private void setNamePhoto(String name) {
        tvNamePhoto.setText(name);
    }

    private void toastLoadGalleryFailed() {
        Toast.makeText(mContext, getString(R.string.load_gallery_failed), Toast.LENGTH_SHORT).show();
    }

    public void setValues() {
        binding.btnEdit.setOnClickListener(this);
        binding.ivPhoto.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnFavourite.setOnClickListener(this);
//        btnRotate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnMore.setOnClickListener(this);
    }

    public PhotoViewerFragment setListener(OnPhotoViewerListener listener) {
        this.listener = listener;
        return this;
    }

    public  void setLightStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = activity.getWindow().getDecorView().getSystemUiVisibility(); // get current flag
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;   // add LIGHT_STATUS_BAR to flag
            activity.getWindow().getDecorView().setSystemUiVisibility(flags);
        }
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_back:
                if (listener != null) {
                    listener.onBackPhotoViewerFragment();
                }
                break;
            case R.id.iv_photo:
                Animation slideUp = AnimationUtils.loadAnimation(mContext, R.anim.slide_up);
                Animation slideUp_up = AnimationUtils.loadAnimation(mContext, R.anim.slide_up_up);
                Animation slideDown = AnimationUtils.loadAnimation(mContext, R.anim.slide_down);
                Animation slideDown_down = AnimationUtils.loadAnimation(mContext, R.anim.slide_down_down);
                if(!isClick){
                    isClick = true;
//                    top.setAnimation(slideUp);
                    binding.myToolbar.setVisibility(View.GONE);
                    binding.myToolbar.setAnimation(slideUp_up);
                    binding.bottomContent.setVisibility(View.GONE);
                    binding.bottomContent.setAnimation(slideDown);
                }else {
                    isClick = false;
                    binding.myToolbar.setVisibility(View.VISIBLE);
                    binding.bottomContent.setVisibility(View.VISIBLE);
                    binding.myToolbar.setAnimation(slideDown_down);
                    binding.bottomContent.setAnimation(slideUp);
                }
                break;
            case R.id.btn_favourite:

                toggleFavouriteBtn();
                if (listener != null) {
                    listener.onUpdateToFavouriteAlbum();
                }
                break;
//            case R.id.btn_rotate:
                // do nothing
//                break;
            case R.id.btn_delete:
                if (listener != null) {
                    listener.onDeletePhotoViewerFragment(mFileItem);
                }
                break;
            case R.id.btn_share:
                if (listener != null) {
                    listener.onSharePhotoViewerFragment(mFileItem);
                }
                break;
            case R.id.btn_more:
                if (listener != null) {
                    listener.onMorePhotoViewerFragment(mFileItem);
                }
                break;
            case R.id.btn_edit:
                Intent intent = new Intent(mContext, EditPhotoActivity.class);
                intent.putExtra(ConstValue.EXTRA_FILE_ITEM_TO_EDIT, mFileItem);
                intent.putExtra(ConstValue.VIDEO_OPEN_WITH,false);
                startActivityForResult(intent, ConstValue.REQUEST_CODE_EDIT_PHOTO_ACTIVITY);
                break;
        }
    }

    private void toggleFavouriteIcon() {
        btnFavourite.setImageResource(mIsFavourited ? R.drawable.ic_favorite_check : R.drawable.ic_farvorite_none);
    }

    private void toggleFavouriteBtn() {
        mIsFavourited = !mIsFavourited;
        toggleFavouriteIcon();

        if (OLD_CODE) {
            if (listener != null) {
                if (mIsFavourited) {

                    String oldPaths = SharedPrefUtil.getInstance().getString(ConstValue.EXTRA_FAVOURITED_FILES, "");
                    String newPaths = oldPaths + ConstValue.SEPARATE_SYMBOL + mFileItem.path;
                    SharedPrefUtil.getInstance().putString(ConstValue.EXTRA_FAVOURITED_FILES, newPaths);

                    listener.onCreateFileToFavouriteAlbum(mFileItem);
                } else {

                    String oldPaths = SharedPrefUtil.getInstance().getString(ConstValue.EXTRA_FAVOURITED_FILES, "");
                    String[] listPaths = oldPaths.split(ConstValue.SEPARATE_SYMBOL);
                    String newPaths = "";
                    for (int i = 0; i < listPaths.length; i++) {
                        String path = listPaths[i];
                        if (!new File(path).exists() || path.equals(mFileItem.path)) {
                            continue;
                        }
                        newPaths += ConstValue.SEPARATE_SYMBOL + path;
                    }
                    SharedPrefUtil.getInstance().putString(ConstValue.EXTRA_FAVOURITED_FILES, newPaths);

                    listener.onDeleteFileToFavouriteAlbum(mFileItem);
                }
            }
        } else {
            if (mIsFavourited) {
                DbUtils.addFavourite(mFileItem);
            } else {
                DbUtils.deleteFavourite(mFileItem);
            }
        }
    }

    private boolean checkFileFavorited(FileItem fileItem) {
        String oldPaths = SharedPrefUtil.getInstance().getString(ConstValue.EXTRA_FAVOURITED_FILES, "");
        String[] listPaths = oldPaths.split(ConstValue.SEPARATE_SYMBOL);
        for (String path : listPaths) {
            if (!new File(path).exists()) {
                continue;
            }
            Flog.d(TAG, "check item 1=" + path);
            Flog.d(TAG, "check item 2=" + fileItem.path);
            if (path.equals(fileItem.path)) {
                return true;
            }
        }
        return false;
    }

    public void updateUI(String nName, String nPath) {
        if(mIsFavourited){
            boolean renamed = DbUtils.deleteFavourite(mFileItem);
            mFileItem.name = nName;
            mFileItem.path = nPath;
            setNamePhoto(nName);
            DbUtils.addFavourite(mFileItem);
        }else {
            mFileItem.name = nName;
            mFileItem.path = nPath;
            setNamePhoto(nName);
        }


//        mFileItem.date_modified = String.valueOf(System.currentTimeMillis());
//        setDatePhoto(DateUtils.getDate(System.currentTimeMillis(), DateUtils.FORMAT_DATE_2));
//        if (renamed) {

//        }
    }

    public void updateImage() {
        String path = mFileItem.path;

        loadImage(mContext, binding.ivPhoto, path);
    }

    public void loadImage(Context context, final ImageView ivJpgPhoto, String photoPath) {
        Glide.with(context).load(photoPath)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(ivJpgPhoto);
    }

    public interface OnPhotoViewerListener {
        void onPhotoViewerFragmentReady();

        void onBackPhotoViewerFragment();

        void onMorePhotoViewerFragment(FileItem fileItem);

        void onSharePhotoViewerFragment(FileItem fileItem);

        void onDeletePhotoViewerFragment(FileItem fileItem);

        void onCreateFileToFavouriteAlbum(FileItem fileItem);

        void onDeleteFileToFavouriteAlbum(FileItem fileItem);

        void onUpdateToFavouriteAlbum();
    }



    private void onPhotoLoaded(Bitmap bitmap) {
        binding.ivPhoto.setImageBitmap(bitmap);
    }
}
