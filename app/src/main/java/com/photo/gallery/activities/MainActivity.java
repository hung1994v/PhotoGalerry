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
import com.bsoft.core.PreloadNativeAdsList;
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

public class MainActivity extends AppCompatActivity {

    private BRateApp bRateApp;
    boolean isGrandPermission;
    private ArrayList<FileItem> listAllFiles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setLightStatusBar();

        getSupportFragmentManager().beginTransaction().replace(R.id.root_view, new HomeFragment()).commit();
        bRateApp = new BRateApp.Builder(this, getString(R.string.native_admob_id), this::finish)
                .isPremium(false).displayDoNotShowAgain(false).build();
        AdmobFull.init(this, getString(R.string.full_admob_id));
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
        if (fragment instanceof PremissionFragment) {
            finish();
        } else if (fragment instanceof HomeFragment) {
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
        PreloadNativeAdsList.getInstance().destroy();
    }

}
