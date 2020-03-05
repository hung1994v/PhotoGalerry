package com.photo.gallery.exoplayer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bsoft.core.AdmobFull;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.photo.gallery.R;
import com.photo.gallery.activities.SplashActivity;
import com.photo.gallery.models.FileItem;
import com.photo.gallery.utils.ConstValue;
import com.photo.gallery.utils.DateUtils;
import com.photo.gallery.utils.DbUtils;
import com.photo.gallery.utils.FileUtil;
import com.photo.gallery.utils.FileUtils;
import com.photo.gallery.utils.Flog;
import com.photo.gallery.utils.GalleryUtil;
import com.photo.gallery.utils.KeyboardUtil;
import com.photo.gallery.utils.Utils;

import org.jetbrains.annotations.Contract;

import java.io.File;
import java.util.ArrayList;

import static com.photo.gallery.utils.ConstValue.DELETE;
import static com.photo.gallery.utils.ConstValue.FAVORITE;
import static com.photo.gallery.utils.ConstValue.REFRESH;
import static com.photo.gallery.utils.ConstValue.PLAY_VIDEO;
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
    private boolean isRefresh;

    private View mForwardLayout, mBackwardLayout;

    private View mToolbar;
    private ImageView btnFullScreen,btnShare, btnFavorite, btnDelete;
    private View btnBack, btnDetails, btnRename;

    private String videoTitle;

    private boolean isFullScreen;
    private boolean isFinished = false;
    private String videoPath ;
    private Uri videoUri;
    private boolean isOpenWith;
    private ArrayList<FileItem> listAllFiles = new ArrayList<>();
    private AlertDialog alertDialog = null;
    private boolean isFavorite;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo_player);
        getData();

        hideSystemUI();

        Intent intent = getIntent();
        isOpenWith = intent.getBooleanExtra(VIDEO_OPEN_WITH,true);
        if(isOpenWith){
            if(TextUtils.equals(getIntent().getAction(), Intent.ACTION_VIEW) && getIntent().getData() != null && getIntent().getType() != null && getIntent().getType().contains("video/")) {
                listAllFiles = GalleryUtil.getAllVideos(ExoPlayerActivity.this);
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

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRefresh){
                    Intent intent = new Intent();
                    intent.putExtra(PLAY_VIDEO, REFRESH);
                    setResult(RESULT_OK,intent);
                }
                if(isFavorite){
                    Intent intent = new Intent();
                    intent.putExtra(PLAY_VIDEO, FAVORITE);
                    setResult(RESULT_OK,intent);
                }
                shutdown();
                finish();
            }
        });


        btnRename = findViewById(R.id.btn_rename);
        btnRename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getFileItem()!=null){
                    exoPlayer.setPlayWhenReady(false);
                    openDialogInputName(getFileItem(),ExoPlayerActivity.this);
                }

            }
        });

        btnDetails = findViewById(R.id.btn_details);
        btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exoPlayer.setPlayWhenReady(false);
                openDialogDetailsFile();
            }
        });


        btnFullScreen = findViewById(R.id.btn_full_screen);
        btnFullScreen.setOnClickListener(v ->{
            rotateVideo();
        } );
        btnShare = findViewById(R.id.btn_share);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFullScreen) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    btnFullScreen.setImageResource(R.drawable.ic_fullscreen);
                }
                isFullScreen = !isFullScreen;
            FileUtil.share(ExoPlayerActivity.this, FileUtil.getUrifromFile(ExoPlayerActivity.this,videoPath));

            }
        });
        btnDelete = findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exoPlayer.stop();
                showDeleteDialog();
            }
        });

        btnFavorite = findViewById(R.id.btn_favourite);
        mIsFavourited = checkFileFavorited1(getFileItem());
        toggleFavouriteIcon();

        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavouriteBtn();
            }
        });

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


    private FileItem getFileItem() {
        FileItem fileInstance = null;
        if(listAllFiles!=null){
            for(FileItem fileItem : listAllFiles){
                if(fileItem.path.equals(videoPath)){
                    fileInstance = fileItem;
                    break;
                }

            }
        }
        return fileInstance;
    }

    private void reanemFileItem(String oldPath, String newPath,String newName) {
        if(listAllFiles!=null){
            for(int i=0;i<listAllFiles.size();i++){
                Flog.d("ACACACAC", "name1: " + listAllFiles.get(i).path + "old_path: " + oldPath );
                if(oldPath.equals(listAllFiles.get(i).path)){
                    Flog.d("ACACACAC", "true " + newPath + " X " + newName);
                    listAllFiles.get(i).path = newPath;
                    listAllFiles.get(i).name = newName;
                    break;
                }

            }
        }
    }

    private boolean checkFileName(String newTitle, FileItem fileItem) {
        boolean check = false;
        if (listAllFiles.size() > 0) {
            for (int i = 0; i < listAllFiles.size(); i++) {
                if ((new File(listAllFiles.get(i).path).getParent().equals(new File(fileItem.path).getParent()))) {
                    Flog.d("CCCCCCC", "name1: " + listAllFiles.get(i).name + "___" + newTitle + "___Finame: " + fileItem.name);
                    if (newTitle.equals(listAllFiles.get(i).name)) {
                        Flog.d("CCCCCCC", "name2: " + listAllFiles.get(i).name + "___" + newTitle + "___Finame: " + fileItem.name);
                        check = true;
                        break;
                    }
                }
            }
        }
        return check;
    }


    private void openDialogInputName(final FileItem fileItem, Context mContext) {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.dialog_input_filename, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);
        userInput.setText(fileItem.name);
        userInput.setFilters(new InputFilter[]{Utils.filter});
        userInput.selectAll();

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text


                                try {

                                    String input = userInput.getText().toString().trim();

                                    if (input.length() <= 0) {
                                        return;
                                    }
                                    boolean success = false;
                                    if (!checkFileName(input, fileItem)) {
                                        success = FileUtil.rename(mContext, new File(fileItem.path), input + FileUtil.getExtension(fileItem.path));
                                    } else {
                                        FileUtil.toastFailed(mContext, getString(R.string.rename));
                                        dialog.dismiss();
                                    }
                                    String path = fileItem.path;
                                    String newPath = new File(path).getParent() + File.separator + input + FileUtil.getExtension(path);
//                                        success = FileUtil.rename(mContext, new File(path), input + FileUtil.getExtension(path));


                                    if (success) {

                                        if(mIsFavourited){
                                            boolean renamed = DbUtils.deleteFavourite(getFileItem());
                                            reanemFileItem(videoPath,newPath,input);
                                            videoPath = newPath;
                                                DbUtils.addFavourite(getFileItem());
                                                isFavorite = true;
                                            FileUtil.scanMediaStore(mContext, newPath);
                                            FileUtil.toastSuccess(mContext, getString(R.string.rename));
                                        }else {
                                            reanemFileItem(videoPath,newPath,input);
                                            isRefresh = true;
                                            videoPath = newPath;
                                            FileUtil.scanMediaStore(mContext, newPath);
                                            FileUtil.toastSuccess(mContext, getString(R.string.rename));
                                        }




                                    } else {
                                        isRefresh = true;
                                        FileUtil.toastFailed(mContext, getString(R.string.rename));
                                    }

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    FileUtil.toastFailed(mContext, getString(R.string.rename));
                                }


                                KeyboardUtil.hideKeyboard(mContext, userInput);
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                KeyboardUtil.hideKeyboard(mContext, userInput);
                                dialog.dismiss();
                            }
                        });

        // create alert dialog
        alertDialog = alertDialogBuilder.create();
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }

        // show it
        alertDialog.show();

        // Initially disable the button
        ((AlertDialog) alertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
                .setEnabled(false);

        // Now set the textchange listener for edittext
        userInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s) || checkFilename(s)) {
                    // Disable ok button
                    ((AlertDialog) alertDialog).getButton(
                            AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } else {
                    // Something into edit text. Enable the button.
                    ((AlertDialog) alertDialog).getButton(
                            AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }
        });
    }

    private boolean checkFilename(Editable s) {
        String videoTitle = new File(videoPath).getName();
        int lastIndex = videoTitle.lastIndexOf(".");
        if (lastIndex > 0) {
            videoTitle = videoTitle.substring(0, lastIndex);
        }
        return (s.toString().trim().equals(videoTitle));

    }

    private void getData() {
            listAllFiles.clear();
            listAllFiles = getIntent().getParcelableArrayListExtra(ConstValue.EXTRA_LIST_ALL_FILES);
    }

    private boolean mIsFavourited = false;

    private void toggleFavouriteBtn() {
        isFavorite = true;
        mIsFavourited = !mIsFavourited;
        toggleFavouriteIcon();

        if (mIsFavourited) {
            DbUtils.addFavourite(getFileItem());
        } else {
            DbUtils.deleteFavourite(getFileItem());
        }
    }


    private void addText(LinearLayout lnInfoContainer, String label, String text) {
        if (text == null || text.equals("null"))
            return;
        LinearLayout.LayoutParams parentParams = (LinearLayout.LayoutParams) lnInfoContainer.getLayoutParams();

        TextView textView = new TextView(this);
        textView.setLayoutParams(parentParams);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textView.getLayoutParams();
        params.setMargins(0, 0, 0, (int) getResources().getDimension(R.dimen.margin_small_size));
        String sourceString = "<b>" + label + ": " + "</b> " + text;
        textView.setText(Html.fromHtml(sourceString));

        lnInfoContainer.addView(textView);
    }

    private void openDialogDetailsFile() {
        File fileItem = new File(videoPath);

        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.dialog_details_file, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final LinearLayout lnInfoContainer = promptsView.findViewById(R.id.info_container);

        addText(lnInfoContainer, getString(R.string.filename), fileItem.getName());
        addText(lnInfoContainer, getString(R.string.last_modified),
                DateUtils.getDate(fileItem.lastModified(), DateUtils.FORMAT_DATE_2));

        addText(lnInfoContainer, getString(R.string.size),
                FileUtil.formatSize(fileItem.length()));
        addText(lnInfoContainer, getString(R.string.filetype), "video");
        addText(lnInfoContainer, getString(R.string.resolution), Utils.getResolution(videoPath));
        addText(lnInfoContainer, getString(R.string.duration), Utils.milliSecondsToTimer(FileUtil.getDurationVideo(this,videoPath)));
        addText(lnInfoContainer, getString(R.string.path), fileItem.getPath());

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    private void toggleFavouriteIcon() {
        btnFavorite.setImageResource(mIsFavourited?R.drawable.ic_favorite_check:R.drawable.ic_farvorite_none);
    }



    
    private void showDeleteDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        String title = this.getString(R.string.delete);
        String message = this.getString(R.string.confirm_delete_dialog);
        alert.setCancelable(false)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        shutdown();
                        Intent intent = new Intent();
                        intent.putExtra(PLAY_VIDEO, DELETE);
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                }).show();
    }

    private void initToolbar() {
        mToolbar.setVisibility(View.VISIBLE);
//        mToolbar.setTitle(videoTitle);
//        mToolbar.setNavigationIcon(R.drawable.ic_back_white);
//        mToolbar.setNavigationOnClickListener(view -> onBackPressed());
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
        if(isRefresh){
            Intent intent = new Intent();
            intent.putExtra(PLAY_VIDEO, REFRESH);
            setResult(RESULT_OK,intent);
        }
        if(isFavorite){
            Intent intent = new Intent();
            intent.putExtra(PLAY_VIDEO, FAVORITE);
            setResult(RESULT_OK,intent);
        }
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
