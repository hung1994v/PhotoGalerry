package com.photo.gallery.activities;

import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.bsoft.core.AdmobFullHelper;
import com.halilibo.bettervideoplayer.BetterVideoCallback;
import com.halilibo.bettervideoplayer.BetterVideoPlayer;
import com.photo.gallery.R;
import com.photo.gallery.utils.FileUtil;


import java.io.File;

import static com.photo.gallery.utils.ConstValue.VIDEO_EDIT_URI_KEY;

public class PlayActivity extends AppCompatActivity  {
    private BetterVideoPlayer player;
    private Uri videoUri;
    String path = "";
    private AdmobFullHelper admobFullHelper = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_play);
        if(!getIntent().hasExtra(VIDEO_EDIT_URI_KEY)) {
            Toast.makeText(this, getResources().getString(R.string.video_not_found), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        this.videoUri = Uri.parse(getIntent().getStringExtra(VIDEO_EDIT_URI_KEY));
        this.path = videoUri.getPath();
        Log.d("videoUri111=",""+videoUri);

        if (!new File(videoUri.getPath()).exists()) {
            Toast.makeText(this, getResources().getString(R.string.video_not_found), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        init();
//        initad();
    }

     private void initad() {
        admobFullHelper = AdmobFullHelper.init(this)
                .setShowAfterLoaded(false)
                .setAdUnitId(this.getString(R.string.full_admob_id));
        admobFullHelper.load();
    }



    private void init() {
        player = findViewById(R.id.player);
        // Sets the callback to this Activity, since it inherits EasyVideoCallback
//        player.setCallback(this);
        player.setSource(FileUtil.getUrifromFile(this,path));
        player.showToolbar();

    }

    @Override
    protected void onStop() {
        player.pause();
        super.onStop();
    }


    private void showFullAd() {
        if (admobFullHelper != null)
            admobFullHelper.show();
    }



}
