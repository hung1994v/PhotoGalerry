package com.photo.gallery.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.bsoft.core.AdmobFull;
import com.bsoft.core.AdmobFullHelper;
import com.bsoft.core.BRateApp;
import com.photo.gallery.R;
import com.photo.gallery.fragments.FilesAlbumFragment;
import com.photo.gallery.fragments.PhotoViewerFragment;
import com.photo.gallery.fragments.PremissionFragment;
import com.photo.gallery.fragments.SettingFragment;
import com.photo.gallery.fragments.VideoViewerFragment;
import com.photo.gallery.fragments.options.SplashFragment;
import com.photo.gallery.models.FileItem;
import com.photo.gallery.utils.ConstValue;
import com.photo.gallery.utils.FileUtils;
import com.photo.gallery.utils.Flog;
import com.photo.gallery.utils.GalleryUtil;
import com.photo.gallery.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.photo.gallery.utils.ConstValue.SPLASH_INTENT;
import static com.photo.gallery.utils.ConstValue.VIDEO_EDIT_URI_KEY;

public class MainActivity extends AppCompatActivity implements PremissionFragment.onPermissionListener {

    private BRateApp bRateApp;
    boolean isGrandPermission;
    private ArrayList<FileItem> listAllFiles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getIntent()!=null) {
            isGrandPermission = getIntent().getBooleanExtra(SPLASH_INTENT,false);
        }else {
            isGrandPermission = false;
        }
        if(isGrandPermission){
            getSupportFragmentManager().beginTransaction().replace(R.id.root_view, new HomeFragment()).commit();
            bRateApp = new BRateApp.Builder(this, getString(R.string.native_admob_id), this::finish)
                    .isPremium(false).displayDoNotShowAgain(false).build();
            AdmobFull.init(this, getString(R.string.full_admob_id));
        }else {
                getSupportFragmentManager().beginTransaction().replace(R.id.root_view, PremissionFragment.newInstance(this)).commit();
        }
    }



    protected void setLightStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = getWindow().getDecorView().getSystemUiVisibility(); // get current flag
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;   // add LIGHT_STATUS_BAR to flag
            getWindow().getDecorView().setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE); // optional
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.root_view);
        if(fragment instanceof PremissionFragment){
            finish();
        }else if (fragment instanceof HomeFragment) {
            if (!((HomeFragment) fragment).isSearchViewClose()) {
                ((HomeFragment) fragment).SearchViewClose();
            } else if (((HomeFragment) fragment).getToolbarShow()) {
                ((HomeFragment) fragment).closeToolbar();
            } else {
                if (bRateApp.show()) {
                    AdmobFull.show();
                }
            }
        } else if (fragment instanceof PhotoViewerFragment) {
            getSupportFragmentManager().popBackStack();
            setLightStatusBar();
        } else if (fragment instanceof VideoViewerFragment) {
            getSupportFragmentManager().popBackStack();
            setLightStatusBar();
        } else if (fragment instanceof SettingFragment) {
            getSupportFragmentManager().popBackStack();
        } else if (fragment instanceof FilesAlbumFragment) {
            if (!((FilesAlbumFragment) fragment).isSearchViewClose()) {
                ((FilesAlbumFragment) fragment).SearchViewClose();
            } else if (((FilesAlbumFragment) fragment).getToolbarShow()) {
                ((FilesAlbumFragment) fragment).closeToolbar();
            } else
                getSupportFragmentManager().popBackStack();
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else if (bRateApp.show()) {
            AdmobFull.show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AdmobFull.destroy();
    }



    private void loadFromGallery() {

//        showDialog();
                try {
                    ArrayList<FileItem> listAllFiles = new ArrayList<>();
                    ArrayList<FileItem> listAllImgs = GalleryUtil.getAllImages(MainActivity.this);
                    ArrayList<FileItem> listAllVideos = GalleryUtil.getAllVideos(MainActivity.this);


                    ArrayList<FileItem> listAllMediaSdcard = new ArrayList<>();
                    GalleryUtil.getAllMediaSdcard(MainActivity.this, listAllMediaSdcard);
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


                    listAllFiles.addAll(listAllImgs);
                    listAllFiles.addAll(listAllVideos);

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

//                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                    intent.putParcelableArrayListExtra(ConstValue.EXTRA_LIST_ALL_FILES, listAllFiles);
//                    intent.putExtra(SPLASH_INTENT, true);
//
//                    startActivity(intent);
                    getSupportFragmentManager().beginTransaction().replace(R.id.root_view,HomeFragment.newInstance(listAllFiles,true)).commitAllowingStateLoss();


                    //Let's Finish Splash Activity since we don't want to open this when user press back button.
                } catch (Exception ignored) {
//                    hideDialog();
                    ignored.printStackTrace();
                }
    }
    public void onPermissionGranted() {
        bRateApp = new BRateApp.Builder(this, getString(R.string.native_admob_id), this::finish)
                .isPremium(false).displayDoNotShowAgain(false).build();
        AdmobFull.init(this, getString(R.string.full_admob_id));
        loadFromGallery();
    }
}
