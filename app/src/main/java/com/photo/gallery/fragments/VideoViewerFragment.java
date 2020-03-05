package com.photo.gallery.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.photo.gallery.R;
import com.photo.gallery.databinding.FragmentVideoViewerBinding;
import com.photo.gallery.exoplayer.ExoPlayerActivity;
import com.photo.gallery.models.FileItem;
import com.photo.gallery.utils.DbUtils;

import java.util.ArrayList;

import static com.photo.gallery.utils.ConstValue.VIDEO_EDIT_URI_KEY;
import static com.photo.gallery.utils.ConstValue.VIDEO_NAME_KEY;
import static com.photo.gallery.utils.ConstValue.VIDEO_OPEN_WITH;

/**
 * Created by Tung on 3/23/2018.
 */

public class VideoViewerFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = VideoViewerFragment.class.getSimpleName();
    private View btnBack, btnDelete, btnShare, btnMore;
    private ImageView btnFavourite;
    private TextView tvNamePhoto, tvDatePhoto;
    private FileItem mFileItem = null;
    private ProgressDialog progressDialog = null;
    private OnVideoViewerListener listener = null;
    private boolean isHide;
    private FragmentVideoViewerBinding binding;

    public static VideoViewerFragment newInstance(FileItem fileItem) {

        VideoViewerFragment fragment = new VideoViewerFragment();
        fragment.mFileItem = fileItem;
        return fragment;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_video_viewer, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initBannerAdmob();
        open();
        init(view);
        if (listener != null) {
            listener.onVideoViewerFragmentReady();
        }
    }

    private void initBannerAdmob() {
//        AdmobBannerHelper.init(mContext, (ViewGroup) getView().findViewById(R.id.admob_banner))
//                .setAdUnitId(getString(R.string.smart_banner_ad_id))
//                .setAdSize(AdSize.SMART_BANNER)
//                .loadAd();
    }




    private void init(View view) {
        binding.playVideo.setOnClickListener(this);
        View clickView = view.findViewById(R.id.clickView);
        Animation slideUp = AnimationUtils.loadAnimation(mContext, R.anim.slide_up);
        Animation slideUp_up = AnimationUtils.loadAnimation(mContext, R.anim.slide_up_up);
        Animation slideDown = AnimationUtils.loadAnimation(mContext, R.anim.slide_down);
        Animation slideDown_down = AnimationUtils.loadAnimation(mContext, R.anim.slide_down_down);

        clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isHide){
                    isHide = true;
//                    top.setAnimation(slideUp);
                    binding.myToolbar.setVisibility(View.GONE);
                    binding.myToolbar.setAnimation(slideUp_up);
                    binding.bottomContent.setVisibility(View.GONE);
                    binding.bottomContent.setAnimation(slideDown);
                }else {
                    isHide = false;
                    binding.myToolbar.setVisibility(View.VISIBLE);
                    binding.bottomContent.setVisibility(View.VISIBLE);
                    binding.myToolbar.setAnimation(slideDown_down);
                    binding.bottomContent.setAnimation(slideUp);
                }
            }
        });


        binding.btnBack.setOnClickListener(this);
//        binding.btnMore.setOnClickListener(this);
        binding.btnShare.setOnClickListener(this);
        binding.btnDelete.setOnClickListener(this);
        binding.btnFavourite.setOnClickListener(this);
        binding.btnDelete.setOnClickListener(this);

    }





    private void open() {
        if (mFileItem == null) {
            toastLoadGalleryFailed();
            return;
        }

        mIsFavourited = checkFileFavorited1(mFileItem);
        toggleFavouriteIcon();

        String path = mFileItem.path;
        Glide.with(mContext).load(path).into(binding.previewVideo);
        String name = mFileItem.name;
        setNamePhoto(name);

//        String dateFormated = DateUtils.getDate(Utils.parseLong(mFileItem.date_modified), DateUtils.FORMAT_DATE_2);
//        setDatePhoto(dateFormated);
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

    private void toastLoadGalleryFailed() {
        Toast.makeText(mContext, getString(R.string.load_gallery_failed), Toast.LENGTH_SHORT).show();
    }

    private boolean mIsFavourited = false;
    private void toggleFavouriteIcon() {
        binding.btnFavourite.setImageResource(mIsFavourited?R.drawable.ic_favorite_check:R.drawable.ic_farvorite_none);
    }

    public VideoViewerFragment setListener(OnVideoViewerListener listener) {
        this.listener = listener;
        return this;
    }

    private long mLastClickTime = 0;

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.playVideo:

                break;
            case R.id.btn_back:
                if (listener != null) {
                    listener.onBackVideoViewerFragment();
                }
                break;
            case R.id.btn_favourite:

                toggleFavouriteBtn();
                break;
            case R.id.btn_delete:
                if (listener != null) {
                    listener.onDeleteVideoViewerFragment(mFileItem);
                }
                break;
            case R.id.btn_share:
                if (listener!=null) {
                    listener.onShareVideoViewerFragment(mFileItem);
                }
                break;
            case R.id.btn_details:

                if (listener!=null) {
                    listener.onMoreVideoViewerFragment(mFileItem);
                }
                break;
        }
    }

    private void toggleFavouriteBtn() {
        mIsFavourited = !mIsFavourited;
        toggleFavouriteIcon();

        if (mIsFavourited) {
            DbUtils.addFavourite(mFileItem);
        } else {
            DbUtils.deleteFavourite(mFileItem);
        }
    }

    public void updateUI(String nName, String nPath) {
        boolean renamed = DbUtils.deleteFavourite(mFileItem);
        mFileItem.name = nName;
        mFileItem.path = nPath;
        setNamePhoto(nName);

//        mFileItem.date_modified = String.valueOf(System.currentTimeMillis());
//        setDatePhoto(DateUtils.getDate(System.currentTimeMillis(), DateUtils.FORMAT_DATE_2));


        if (renamed) {
            DbUtils.addFavourite(mFileItem);
        }
    }

    private void setNamePhoto(String name) {
        binding.textName.setText(name);
    }

    public interface OnVideoViewerListener {
        void onVideoViewerFragmentReady();

        void onBackVideoViewerFragment();
        void onMoreVideoViewerFragment(FileItem fileItem);

        void onShareVideoViewerFragment(FileItem fileItem);

        void onDeleteVideoViewerFragment(FileItem fileItem);
    }
}
