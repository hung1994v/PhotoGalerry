package com.photo.gallery.exoplayer;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bsoft.core.AdmobAdaptiveBanner;
import com.bsoft.core.AdmobFull;
import com.bumptech.glide.load.Key;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.photo.gallery.R;
import com.photo.gallery.utils.FileUtils;
import com.photo.gallery.utils.Flog;
import com.photo.gallery.utils.SharedPrefUtil;

import java.io.File;

import static com.photo.gallery.utils.ConstValue.DEFAULT_OPEN;
import static com.photo.gallery.utils.ConstValue.VIDEO_EDIT_URI_KEY;
import static com.photo.gallery.utils.ConstValue.VIDEO_NAME_KEY;
import static com.photo.gallery.utils.ConstValue.VIDEO_OPEN_WITH;

/**
 * Created by ad on 4/10/2019.
 */

public class ExoPlayerActivity extends AppCompatActivity implements ExoPlayerView.OnDoubleTapListener {

    private static final int PLAYER_KEY = 0x1001;

    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";
    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";

    private int mResumeWindow;
    private long mResumePosition;
    private boolean mExoPlayerFullscreen = false;
    private ExoPlayerView exoPlayerView = null;
    private SimpleExoPlayer exoPlayer;

    private View mForwardLayout, mBackwardLayout;

    private Toolbar mToolbar;
    private ImageView btnFullScreen;

    private String videoTitle;

    private boolean isFullScreen;
    private boolean isFinished = false;
    private String videoPath ;
    private Uri videoUri;
    private boolean isOpenWith;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo_player);


        hideSystemUI();

        Intent intent = getIntent();
        isOpenWith = intent.getBooleanExtra(VIDEO_OPEN_WITH,true);
        if(SharedPrefUtil.getInstance().getBoolean(DEFAULT_OPEN,false) && isOpenWith){
            if(TextUtils.equals(getIntent().getAction(), Intent.ACTION_VIEW) && getIntent().getData() != null && getIntent().getType() != null && getIntent().getType().contains("video/")) {
                videoUri = getIntent().getData();
                videoPath = FileUtils.getPath2(this, videoUri);
                if (!TextUtils.isEmpty(videoPath) && new File(videoPath).exists()) {
                    videoTitle = new File(videoPath).getName();
                } else {
                    Cursor cursor = getContentResolver().query(videoUri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        videoTitle = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        cursor.close();
                    } else {
                        videoTitle = getString(R.string.app_name);
                    }
                }
            }
        }else {
            videoTitle = intent.getStringExtra(VIDEO_NAME_KEY);
            videoPath = intent.getStringExtra(VIDEO_EDIT_URI_KEY);
            videoUri = Uri.fromFile(new File(videoPath));
        }



        mToolbar = findViewById(R.id.toolbar);
        mForwardLayout = findViewById(R.id.layout_forward);
        mBackwardLayout = findViewById(R.id.layout_backward);

        btnFullScreen = findViewById(R.id.btn_full_screen);
        btnFullScreen.setOnClickListener(v ->{
            rotateVideo();
        } );

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (savedInstanceState != null) {
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
        } else {
            initMedia(videoUri);
            initToolbar();
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            btnFullScreen.setImageResource(R.drawable.ic_fullscreen_exit);
            isFullScreen = true;
        } else {
            btnFullScreen.setImageResource(R.drawable.ic_fullscreen);
            isFullScreen = false;
        }
    }

    private void initToolbar() {
        mToolbar.setVisibility(View.VISIBLE);
        mToolbar.setTitle(videoTitle);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    private void rotateVideo() {
        if (isFullScreen) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            btnFullScreen.setImageResource(R.drawable.ic_fullscreen);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            btnFullScreen.setImageResource(R.drawable.ic_fullscreen_exit);
        }
        isFullScreen = !isFullScreen;
    }

    private void hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

            final View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(flags);
            decorView.setOnSystemUiVisibilityChangeListener(visibility -> {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    decorView.setSystemUiVisibility(flags);
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
        outState.putLong(STATE_RESUME_POSITION, mResumePosition);
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, mExoPlayerFullscreen);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        shutdown();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
    }

    private void shutdown() {
        ExoPlayerManager.getInstance().stopPlayerFor(PLAYER_KEY);
        ExoPlayerManager.getInstance().releaseExoPlayer(PLAYER_KEY);
    }

    @Override
    protected void onDestroy() {
        AdmobFull.show();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (exoPlayerView != null && exoPlayerView.getPlayer() != null) {
            mResumeWindow = exoPlayerView.getPlayer().getCurrentWindowIndex();
            mResumePosition = Math.max(0, exoPlayerView.getPlayer().getContentPosition());
            exoPlayer.setPlayWhenReady(false);
        }
    }

    private void initMedia(Uri videoUri) {
        exoPlayerView = findViewById(R.id.exoplayer);

        exoPlayerView.setOnControllerVisibilityListener(isVisible -> {
            if (isVisible) {
                mToolbar.setVisibility(View.VISIBLE);
            } else {
                mToolbar.setVisibility(View.GONE);
            }
        });

        exoPlayerView.setOnDoubleTapListener(this);

        MediaSource videoSource = ExoPlayerManager.setUpMediaSource(this, videoUri);

        try {
            exoPlayer = ExoPlayerManager.getInstance().prepareExoPlayer(PLAYER_KEY, this, exoPlayerView, 0,
                    false, 0, videoSource);

            PlayerEventListener playerEventListener = new PlayerEventListener(exoPlayer) {
                @Override
                public void onPlayerError(ExoPlaybackException error) {
                    super.onPlayerError(error);
                    Toast.makeText(ExoPlayerActivity.this, "Cannot play this video 111", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    if (playbackState == Player.STATE_READY) {
                        isFinished = false;
                    }
                    if (!isFinished && playbackState == Player.STATE_ENDED && true) {
                        isFinished = true;
                        if (true || System.currentTimeMillis() % 2 == 0) {
                            AdmobFull.show();
                        }
                    }
                }
            };
            exoPlayer.addListener(playerEventListener);
            boolean haveResumePosition = mResumeWindow != C.INDEX_UNSET;

            if (haveResumePosition) {
                exoPlayer.seekTo(mResumeWindow, mResumePosition);
            }
            ExoPlayerManager.getInstance().setPlaybackParameters(PLAYER_KEY, 1.0f, 1.0f);
            exoPlayer.setPlayWhenReady(true);

        } catch (Exception e) {
            Toast.makeText(ExoPlayerActivity.this, "Cannot play this video 222", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            btnFullScreen.setImageResource(R.drawable.ic_fullscreen_exit);
            isFullScreen = true;
        } else {
            btnFullScreen.setImageResource(R.drawable.ic_fullscreen);
            isFullScreen = false;
        }
    }

    private Handler doubleTapHandler = new Handler();

    @Override
    public void onDoubleTap(MotionEvent e) {
        int mWidth = this.getResources().getDisplayMetrics().widthPixels;
        long currentDuration;
        if (e.getX() < mWidth / 2) {
            mBackwardLayout.setVisibility(View.VISIBLE);
            currentDuration = exoPlayer.getCurrentPosition() - 10 * 1000;
            if (currentDuration < 0) {
                currentDuration = 0;
            }
        } else {
            mForwardLayout.setVisibility(View.VISIBLE);
            currentDuration = exoPlayer.getCurrentPosition() + 10 * 1000;
            long totalDuration = exoPlayer.getDuration();
            if (currentDuration > totalDuration) {
                currentDuration = totalDuration;
            }
        }
        doubleTapHandler.postDelayed(() -> {
            mBackwardLayout.setVisibility(View.GONE);
            mForwardLayout.setVisibility(View.GONE);
        }, 500);

        exoPlayer.seekTo(currentDuration);
    }
}
