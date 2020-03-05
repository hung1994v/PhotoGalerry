package com.photo.gallery.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bsoft.core.AdmobFull;
import com.bsoft.core.BRateApp;
import com.bsoft.core.PreloadNativeAdsList;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.photo.gallery.R;
import com.photo.gallery.databinding.ActivityContentBinding;
import com.photo.gallery.databinding.ActivitySplashBinding;
import com.photo.gallery.fragments.PremissionFragment;
import com.photo.gallery.models.FileItem;
import com.photo.gallery.utils.ConstValue;
import com.photo.gallery.utils.Flog;
import com.photo.gallery.utils.GalleryUtil;
import com.photo.gallery.utils.SharedPrefUtil;
import com.photo.gallery.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static com.photo.gallery.utils.ConstValue.SPLASH_INTENT;
import static com.photo.gallery.utils.ConstValue.VIDEO_EDIT_URI_KEY;

/**
 * Created by Hoavt on 3/15/2018.
 */

public class SplashActivity extends AppCompatActivity implements PremissionFragment.onPermissionListener {
    private ActivitySplashBinding binding;
    private static final int NUMBER_OF_ADS = 3;

    private int numberNativeAdsLoaded = 0;


    private InterstitialAd mInterstitialAd;


    private static final java.lang.String TAG = SplashActivity.class.getSimpleName();
    private Handler mWaitHandler = new Handler();
    private ProgressDialog progressDialog = null;
    private String[] perm = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private boolean isGettingData = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        if (EasyPermissions.hasPermissions(this, perm)) {
            Flog.d("AAAAAAAAAAAA", "111111111");
            loadNativeAds();
            loadFromGallery();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getSupportFragmentManager().beginTransaction().add(R.id.root_slpash, PremissionFragment.newInstance(SplashActivity.this)).addToBackStack(PremissionFragment.class.getSimpleName()).commit();
                    Flog.d("AAAAAAAAAAAA", "2222222");

                }
            }, 1000);

        }


    }

    private void loadNativeAds() {
        AdLoader.Builder builder = new AdLoader.Builder(this, getString(R.string.native_admob_id));
        AdLoader adLoader = builder.forUnifiedNativeAd(
                unifiedNativeAd -> {
                    PreloadNativeAdsList.getInstance().add(unifiedNativeAd);
                    numberNativeAdsLoaded++;
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        numberNativeAdsLoaded = NUMBER_OF_ADS;
                    }
                }).build();

        adLoader.loadAds(new AdRequest.Builder().build(), NUMBER_OF_ADS);
    }




    private void loadFromGallery() {
        Flog.d("AAAAAAAAAAAA", "444444");

        if (isGettingData) {
            return;
        }

        isGettingData = true;

//        showDialog();
//        mWaitHandler.removeCallbacksAndMessages(null);
        mWaitHandler.postDelayed(new Runnable() {
            @Override
            public void run() {



                //The following code will execute after the 2 seconds.

                try {
                    ArrayList<FileItem> listAllFiles = new ArrayList<>();
                    ArrayList<FileItem> listAllImgs = GalleryUtil.getAllImages(SplashActivity.this);
                    ArrayList<FileItem> listAllVideos = GalleryUtil.getAllVideos(SplashActivity.this);


                    ArrayList<FileItem> listAllMediaSdcard = new ArrayList<>();
                    GalleryUtil.getAllMediaSdcard(SplashActivity.this, listAllMediaSdcard);
                    Flog.d(TAG, "SIZE listAllMediaSdcard=" + listAllMediaSdcard.size());
                    ArrayList<FileItem> listAllImgsSdcard = new ArrayList<>();
                    ArrayList<FileItem> listAllVideosSdcard = new ArrayList<>();
                    for (int i = 0; i < listAllMediaSdcard.size(); i++) {
                        FileItem item = listAllMediaSdcard.get(i);
                        if (item.isImage) {
                            listAllImgsSdcard.add(item);
                        } else {
                            listAllVideosSdcard.add(item);
                        }
                    }
                    listAllImgs.addAll(listAllImgsSdcard);
                    listAllVideos.addAll(listAllVideosSdcard);
                    Flog.d(TAG, "SIZE SDCARD FILES=" + listAllImgsSdcard.size() + "_" + listAllVideosSdcard.size() + "_" + listAllMediaSdcard.size());


                    listAllFiles.addAll(listAllImgs);
                    listAllFiles.addAll(listAllVideos);
                    Flog.d(TAG, "SIZE ALL FILES=" + listAllImgs.size() + "_" + listAllVideos.size() + "_" + listAllFiles.size());

                    /**
                     * Sort all files descending based-on date-modified.
                     * */
                    Collections.sort(listAllFiles, new Comparator<FileItem>() {
                        @Override
                        public int compare(FileItem f1, FileItem f2) {

                            if (f1 == null || f2 == null || f1.date_modified == null || f2.date_modified == null) {
//                                Flog.d(TAG, "123compare = "+f1 + "_f2= "+f2);
                                return 0;
                            }

//                            Flog.d(TAG, "cmmp: f1="+f1.date_modified+"_f2="+f2.date_modified);
                            long v1 = Utils.parseLong(f1.date_modified);
                            long v2 = Utils.parseLong(f2.date_modified);
                            if (v1 == -1 || v2 == -1) {
                                return 0;
                            }

                            int cmp = 0;
                            if (v1 > v2) {
                                cmp = -1;
                            } else if (v1 < v2) {
                                cmp = 1;
                            }
                            return cmp;
                        }
                    });

//                    hideDialog();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putParcelableArrayListExtra(ConstValue.EXTRA_LIST_ALL_FILES, listAllFiles);
                    intent.putExtra(SPLASH_INTENT, true);
                    startActivity(intent);
                    Flog.d("AAAAAAAAAAAA", "5555555");
                    //Let's Finish Splash Activity since we don't want to open this when user press back button.
                    finish();
                } catch (Exception ignored) {
//                    hideDialog();
                    ignored.printStackTrace();
                }
            }
        }, 1500);  // Give a 2 seconds delay.
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //Remove all the callbacks otherwise navigation will execute even after activity is killed or closed.
        mWaitHandler.removeCallbacksAndMessages(null);
    }

    private void initDialog() {
        progressDialog = new ProgressDialog(this);
    }

    private void showDialog() {
        if (progressDialog != null) {
            progressDialog.show();
        }
    }

    private void hideDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onPermissionGranted() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.root_slpash);
        if (fragment instanceof PremissionFragment) {
            getSupportFragmentManager().popBackStack();
            Flog.d("AAAAAAAAAAAA", "3333333");
            loadFromGallery();
        }

    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentById(R.id.root_slpash) instanceof PremissionFragment) {
            finish();
        } else
            super.onBackPressed();
    }
}
