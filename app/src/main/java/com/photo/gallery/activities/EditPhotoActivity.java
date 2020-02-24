package com.photo.gallery.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.photo.gallery.callback.OnDialogEventListener;
import com.photo.gallery.R;
import com.photo.gallery.fragments.options.BaseOptFragment;
import com.photo.gallery.fragments.options.CropFragment;
import com.photo.gallery.fragments.options.EditFragment;
import com.photo.gallery.fragments.options.IconStickerFragment;
import com.photo.gallery.fragments.options.MenuFragment;
import com.photo.gallery.fragments.options.SplashFragment;
import com.photo.gallery.models.FileItem;
import com.photo.gallery.models.options.PipEntity;
import com.photo.gallery.ui.options.splash.SplashShapeView;
import com.photo.gallery.utils.ConstValue;
import com.photo.gallery.utils.DateUtils;
import com.photo.gallery.utils.FileUtil;
import com.photo.gallery.utils.FileUtils;
import com.photo.gallery.utils.Flog;
import com.photo.gallery.utils.ResizeImage;
import com.photo.gallery.utils.SharedPrefUtil;
import com.photo.gallery.utils.Utils;
import com.photo.gallery.utils.options.FastBlurFilter;
import com.photo.gallery.utils.options.ScreenInfoUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import bsoft.com.lib_filter.filter.FilterFragment;
import bsoft.com.lib_filter.filter.gpu.util.BitmapUtil;
import bsoft.healthy.tracker.menstrual.lib_sticker.fragments.TextStickerFragment;
import bsoft.healthy.tracker.menstrual.lib_sticker.main.BaseStickerView;
import bsoft.healthy.tracker.menstrual.lib_sticker.main.IconStickerView;
import bsoft.healthy.tracker.menstrual.lib_sticker.main.StickerContainerView;
import bsoft.healthy.tracker.menstrual.lib_sticker.main.TextStickerView;

import static com.photo.gallery.fragments.options.BaseOptFragment.FRAGMENT_OPT_EDIT;
import static com.photo.gallery.utils.ConstValue.DEFAULT_OPEN;
import static com.photo.gallery.utils.ConstValue.VIDEO_OPEN_WITH;

/**
 * Created by Tung on 3/30/2018.
 */

public class EditPhotoActivity extends AppCompatActivity implements View.OnClickListener,
        MenuFragment.OnMenuListener, SplashFragment.OnSplashListener, FilterFragment.HandleBackFilter,
        IconStickerFragment.OnStickerIconListener, CropFragment.OnCropListener,
        TextStickerView.TextStickerViewListener, EditFragment.onEditListener {

    public static final int INDEX_PIP = 0;
    public static final int INDEX_PIP_FX = 1;
    public static final int TAB_SIZE = 2;
    public static final float BITMAP_SCALE = 0.4f;
    private static final java.lang.String TAG = EditPhotoActivity.class.getSimpleName();
    private static final int TOTAL_THREADS = 3;
    private FileItem mFileItem = null;
    private View mBtnBack, mBtnDone;
    private ImageView mImgPhoto = null;
    private ProgressBar progressDialog = null;
    private FragmentManager fm = null;
    private int cntThread = 0;
    private BaseOptFragment menuFrag, splashFrag, stickerFrag, editFrag, filterFrag;
    private TextStickerFragment textFrag;
    private int curFragID = -1;
    private TextView tittleView;
    private ViewGroup mViewSplashContainer;
    private StickerContainerView mViewIconStickerContainer;
    private StickerContainerView mViewTextStickerContainer;
    private Bitmap mBitmapSplash, mFinalBitmap;
    private int mCurSplashShape;
    private SplashShapeView mSplashShapeView;
    private long mLastClickTime = 0;
    private int measuredHeight;
    private int measuredWidth;
    private float topOffset;
    private float totalOffset;
    private int pipCurrentIndex;
    private ArrayList<PipEntity> myDataset;
    private Matrix matrixBitmap;
    private Bitmap bitmapShade;
    private Bitmap bitmapMask;
    private Bitmap bitmapSource;
    private Bitmap bitmapBlur;
    private Matrix matrixBlur;
    private Matrix matrixBubble;
    private Matrix matrixMask;
    private int filterMode = 0;
    private Bitmap filterSource, filterBlur;
    private int mImageViewWidth, mImageViewHeight;
    private View viewMain;
    private Uri imageUri;
    private String imagePath;
    private boolean isOpenWith;


    public static Bitmap createCroppedScaledBitmap(Bitmap src, int left, int top, int w, int h, boolean filter) {
        Matrix m = new Matrix();
        m.setScale(BITMAP_SCALE, BITMAP_SCALE);
        m.postTranslate(((float) (-left)) * BITMAP_SCALE, ((float) (-top)) * BITMAP_SCALE);
        int width = (int) (((float) w) * BITMAP_SCALE);
        int height = (int) (((float) h) * BITMAP_SCALE);
        if (width <= 0) {
            width = INDEX_PIP_FX;
        }
        if (height <= 0) {
            height = INDEX_PIP_FX;
        }
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setFilterBitmap(filter);
        canvas.drawBitmap(src, m, paint);
        return result;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        isOpenWith = getIntent().getBooleanExtra(VIDEO_OPEN_WITH, true);
        if (isOpenWith) {
            if (TextUtils.equals(getIntent().getAction(), Intent.ACTION_VIEW) && getIntent().getData() != null && getIntent().getType() != null && getIntent().getType().contains("image/")) {
                imageUri = getIntent().getData();
                imagePath = FileUtils.getPath2(this, imageUri);
            }
        } else {
            getData();
            if (mFileItem == null) {
                toastEditFailed();
                return;
            }
        }

        setContentView(R.layout.activity_edit_photo);

        initDialog();
        initViews();
        setValues();
        addFrags();
    }

//    public void applyColor() {
//        int defaultPrimary = ContextCompat.getColor(this, R.color.colorPrimary);
//        int colorPrimary = SharedPrefUtil.getInstance().getInt(ConstValue.EXTRA_CURRENT_COLOR_PICKER, defaultPrimary);
//        int colorPrimaryDark = Utils.getColorDarker(colorPrimary);
//        boolean isDarkTheme = Utils.isColorDark(colorPrimary);
//        Utils.setConstractStatusBar(this, isDarkTheme);
//        Utils.setColorStatusBar(this, colorPrimaryDark);
//    }

    private void initDialog() {
        progressDialog = findViewById(R.id.progress_circular);

    }

    private void showDialog() {
        if (progressDialog != null) {
            progressDialog.setVisibility(View.VISIBLE);
        }
    }

    private void hideDialog() {
        progressDialog.setVisibility(View.GONE);
    }

    private void setValues() {
        mBtnBack.setOnClickListener(this);
//        mBtnRotate.setOnClickListener(this);
        mBtnDone.setOnClickListener(this);

        showDialog();
        if (isOpenWith) {
            new DecodeImageAsync(this).execute(imagePath);
        } else {
            new DecodeImageAsync(this).execute(mFileItem.path);
        }


    }

    private void addViewSplash(Bitmap bitmap) {
        BitmapUtil.ourBitmapRecycle(mBitmapSplash, false);
        float imageScale;
        mBitmapSplash = bitmap;
        mSplashShapeView = new SplashShapeView(this);
        int srcWidth = mBitmapSplash.getWidth();
        int srcHeight = mBitmapSplash.getHeight();
        float srcRatio = ((float) srcWidth) / ((float) srcHeight);
        int screenWidthDp = ScreenInfoUtil.screenWidthDp(this);
        int screenHeightDp = ScreenInfoUtil.screenHeightDp(this);
        int imageContentWidth = ScreenInfoUtil.dip2px(this, (float) screenWidthDp);
        int imageContentHeight = ScreenInfoUtil.dip2px(this, (float) ((screenHeightDp) - 160));
        float contentRatio = ((float) imageContentWidth) / ((float) imageContentHeight);
        int viewWidth = imageContentWidth;
        int viewHeight = imageContentHeight;
        if (srcRatio <= contentRatio) {
            imageScale = ((float) imageContentHeight) / ((float) srcHeight);
            viewWidth = (int) (((float) srcWidth) * imageScale);
        } else {
            imageScale = ((float) imageContentWidth) / ((float) srcWidth);
            viewHeight = (int) (((float) srcHeight) * imageScale);
        }
        Flog.d("mCurSplashShape " + mCurSplashShape);
        mSplashShapeView.loadSplashShape(1);
        mSplashShapeView.setImageBitmap(mBitmapSplash, imageScale);
        FrameLayout.LayoutParams ly = new FrameLayout.LayoutParams(viewWidth, viewHeight);
        ly.gravity = Gravity.CENTER;
        if (mViewSplashContainer != null) {
            mViewSplashContainer.removeAllViews();
            mViewSplashContainer.addView(mSplashShapeView, ly);
        }

    }

    @Override
    public void onOpenSplash() {
        hideEditFragment();
        tittleView.setVisibility(View.VISIBLE);
        showOptFrag(BaseOptFragment.FRAGMENT_OPT_SPLASH);
    }

    @Override
    public void onOpenFilter() {
        hideEditFragment();
        showOptFrag(BaseOptFragment.FRAGMENT_OPT_FILTER);
        FilterFragment filterFragment = FilterFragment.newInstance(mFinalBitmap, this);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.recycler_view_container, filterFragment)
                .addToBackStack("FilterFragment")
                .commit();
    }

    @Override
    public void onOpenPip() {
        showOptFrag(BaseOptFragment.FRAGMENT_OPT_PIP);
    }

    @Override
    public void onOpenSticker() {
        tittleView.setVisibility(View.VISIBLE);
        hideEditFragment();
        showOptFrag(BaseOptFragment.FRAGMENT_OPT_ICON_STICKER);
    }

    @Override
    public void onOpenText() {
        hideEditFragment();
        Intent intent = new Intent(EditPhotoActivity.this, TextInputActivity.class);
        intent.putExtra(ConstValue.EXTRA_STATUS_TEXT_EDIT_INPUT, TextInputActivity.STATUS_NEW_TEXT_INPUT);
        startActivityForResult(intent, ConstValue.REQUEST_CODE_TEXT_INPUTED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Flog.d(TAG, "resultCode_3=" + resultCode);
        if (data == null)
            return;

        if (resultCode == RESULT_OK && requestCode == ConstValue.REQUEST_CODE_TEXT_INPUTED) {

            handleTextInputed(data);

        }
    }

    private void handleTextInputed(Intent data) {
        String inputed = data.getStringExtra(ConstValue.EXTRA_TEXT_INPUTED);
        int statusTextEditInput = data.getIntExtra(ConstValue.EXTRA_STATUS_TEXT_EDIT_INPUT, -1);
        Flog.d(TAG, "inputed_3=" + inputed);
        if (statusTextEditInput == TextInputActivity.STATUS_NEW_TEXT_INPUT) {
            if (inputed == null || inputed.isEmpty())
                return;

            Flog.d("addStickerTextFragment_3");

            textFrag.resetSeekbar();
            showOptFrag(BaseOptFragment.FRAGMENT_OPT_TEXT);

            addTextStickerView(inputed);

        } else if (statusTextEditInput == TextInputActivity.STATUS_EDIT_TEXT_INPUT) {

            BaseStickerView stickerView = mViewTextStickerContainer.getCurItem();
            if (!(stickerView instanceof TextStickerView)) {
                return;
            }

            ((TextStickerView) stickerView).setText(inputed);
        }
    }

    private void addTextStickerView(String text) {
        showDialog();

        Flog.d(TAG, "addTextStickerView: w=" + mImageViewWidth + "_h=" + mImageViewHeight);
        TextStickerView itemText = new TextStickerView(mViewTextStickerContainer, mImageViewWidth, mImageViewHeight)
                .setListener(this);
        itemText.setText(text);
        mViewTextStickerContainer.reset();
        mViewTextStickerContainer.addItem(itemText);
        mViewTextStickerContainer.invalidate();

        hideDialog();
    }

    private Fragment getCurrentFragment(int id) {
        return getSupportFragmentManager().findFragmentById(id);
    }

    @Override
    public void onEdit() {
//        Fragment fragment = new EditFragment().setListener(this);
        if (getCurrentFragment(R.id.bottom_menu) instanceof EditFragment) {
            return;
        }

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.menu_up, R.anim.menu_down, R.anim.menu_pop_enter, R.anim.menu_pop_exit)
                .add(R.id.bottom_menu, new EditFragment().setListener(this))
                .addToBackStack(EditFragment.class.getSimpleName())
                .commitAllowingStateLoss();


        curFragID = FRAGMENT_OPT_EDIT;
    }

    @Override
    public void onSplashFragmentReady() {

        if (cntThread == TOTAL_THREADS) {
            return;
        }


        ((SplashFragment) splashFrag).initData();

        cntThread++;
        if (cntThread == TOTAL_THREADS) {
            onAllThreadFinished();
        }
    }

    @Override
    public void onItemSplashClickListener(int prevPos, int position) {
        mCurSplashShape = position + 1;
        Flog.d("mCurSplashShape  ", "11111" + mCurSplashShape);
        mSplashShapeView.loadSplashShape(mCurSplashShape);
    }

    @Override
    public void onChangeClickListener(int position) {
        int currrentpos = position + 1;
        Flog.d("onChangeClickListener " + mCurSplashShape);
        mSplashShapeView.loadSplashShape(currrentpos);
    }

    @Override
    public void onStypeClickListener(SplashShapeView.StyleMode btnMode) {
        Flog.d("StyleBtnMode " + btnMode);
        mSplashShapeView.setStyleMode(btnMode);
    }

    private void onAllThreadFinished() {
        Flog.d(TAG, "onAllThreadFinished");
        hideDialog();
    }

    private void onPhotoLoaded(Bitmap bitmap) {

        if (bitmap == null) {
            toastEditFailed();
            return;
        }

        if (cntThread == TOTAL_THREADS) {
            return;
        }

        syncBitmapToAll(bitmap);

        cntThread++;
        if (cntThread == TOTAL_THREADS) {
            onAllThreadFinished();
        }
    }

    private void initViews() {
        tittleView = findViewById(R.id.tv_title);
        viewMain = findViewById(R.id.edit_main);
        mBtnBack = findViewById(R.id.btn_back);
//        mBtnRotate = findViewById(R.id.btn_rotate);
        mBtnDone = findViewById(R.id.btn_done);

        mImgPhoto = (ImageView) findViewById(R.id.iv_photo);

        mViewSplashContainer = (FrameLayout) findViewById(R.id.splash_container);
//        mViewPipContainer = (RelativeLayout) findViewById(R.id.pip_container);
        mViewIconStickerContainer = (StickerContainerView) findViewById(R.id.icon_sticker_container);
        mViewTextStickerContainer = (StickerContainerView) findViewById(R.id.text_sticker_container);
    }


    private void getData() {

        if (getIntent() == null) {
            toastEditFailed();
            return;
        }

        mFileItem = getIntent().getParcelableExtra(ConstValue.EXTRA_FILE_ITEM_TO_EDIT);
    }

    private void toastEditFailed() {
        Toast.makeText(this, getString(R.string.cannot_edit_photo), Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    protected void onDestroy() {

        Flog.d("onDestroyView editphoto");
        try {
            mSplashShapeView.destroy();
            BitmapUtil.free(mBitmapSplash);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_back:
                actionBack();
                break;
//            case R.id.btn_rotate:
//                actionRotate();
//                break;
            case R.id.btn_done:
                actionDone();
                break;
        }
    }

    private void actionDone() {
        hideEditFragment();

        // mis-clicking prevention, using threshold of 1000 ms
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        // do your magic here

        if (curFragID == BaseOptFragment.FRAGMENT_OPT_SPLASH) {
            // save splash.
            showDialog();
            saveEffectSplash();
            hideDialog();
            showOptFrag(BaseOptFragment.FRAGMENT_OPT_MENU);
        } else if (curFragID == BaseOptFragment.FRAGMENT_OPT_ICON_STICKER) {
            // save sticker icons.
            showDialog();
            saveStickerIcons();
            hideDialog();
            showOptFrag(BaseOptFragment.FRAGMENT_OPT_MENU);
        } else if (curFragID == BaseOptFragment.FRAGMENT_OPT_TEXT) {
            // save sticker texts.
            showDialog();
            saveStickerTexts();
            hideDialog();
            showOptFrag(BaseOptFragment.FRAGMENT_OPT_MENU);
        } else if (curFragID == FRAGMENT_OPT_EDIT) {
            // save crop.
            showDialog();
            hideDialog();
            showOptFrag(BaseOptFragment.FRAGMENT_OPT_MENU);
        } else {
            showSaveDialog(this, new OnDialogEventListener() {
                @Override
                public void onOk() {

//                    Uri uri = FileUtil.insert(EditPhotoActivity.this, mFileItem, mFinalBitmap);
//                    String savedPath = FileUtil.getRealPathFromURI(EditPhotoActivity.this, uri);
//                    FileUtil.scanMediaStore(EditPhotoActivity.this, mFileItem.path, savedPath);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MMdd_HHss");
                    String currentDateandTime = sdf.format(new Date());

                    String dstSavedDir = Environment.getExternalStorageDirectory().getAbsolutePath()
                            + File.separator + ConstValue.APP_FOLDER + File.separator;
                    String dstPath = dstSavedDir + mFileItem.name + "_"
                            + currentDateandTime
                            + FileUtil.getExtension(mFileItem.path);
                    Flog.d(TAG, "dstPath123=" + dstPath);
                    try {
                        FileUtil.insert(EditPhotoActivity.this, mFileItem, mFinalBitmap, dstPath);

                        FileUtil.scanMediaStore(EditPhotoActivity.this, dstPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    Intent intent = new Intent();
                    intent.putExtra(ConstValue.EXTRA_FILE_ITEM_EDIITED, "aaa");
                    setResult(RESULT_OK, intent);
                    finish();
                }

                @Override
                public void onCancel() {

                }
            });
        }
    }

    private void saveStickerTexts() {

        mViewTextStickerContainer.unSelectAllComponent();

        Bitmap bmp = Bitmap.createBitmap(mImageViewWidth, mImageViewHeight, Bitmap.Config.ARGB_8888);
        mViewTextStickerContainer.draw(new Canvas(bmp));

        syncBitmapToAll(bmp);

        mViewTextStickerContainer.reset();
    }


    private void saveStickerIcons() {

        mViewIconStickerContainer.unSelectAllComponent();

        Bitmap bmp = Bitmap.createBitmap(mImageViewWidth, mImageViewHeight, Bitmap.Config.ARGB_8888);
        mViewIconStickerContainer.draw(new Canvas(bmp));

        syncBitmapToAll(bmp);

        mViewIconStickerContainer.reset();
    }

    private void saveEffectSplash() {
        Bitmap bmp = Bitmap.createBitmap(mBitmapSplash.getWidth(), mBitmapSplash.getHeight(), Bitmap.Config.ARGB_8888);
        mSplashShapeView.drawImage(new Canvas(bmp));

        syncBitmapToAll(bmp);
    }

    private void syncSizeView(Bitmap srcBmp) {
        float imageScale;
        int srcWidth = srcBmp.getWidth();
        int srcHeight = srcBmp.getHeight();
        float srcRatio = ((float) srcWidth) / ((float) srcHeight);
        int screenWidthDp = ScreenInfoUtil.screenWidthDp(this);
        int screenHeightDp = ScreenInfoUtil.screenHeightDp(this);
        int imageContentWidth = ScreenInfoUtil.dip2px(this, (float) screenWidthDp);
        int imageContentHeight = ScreenInfoUtil.dip2px(this, (float) ((screenHeightDp) - 160));
        float contentRatio = ((float) imageContentWidth) / ((float) imageContentHeight);
        int viewWidth = imageContentWidth;
        int viewHeight = imageContentHeight;
        if (srcRatio <= contentRatio) {
            imageScale = ((float) imageContentHeight) / ((float) srcHeight);
            viewWidth = (int) (((float) srcWidth) * imageScale);
        } else {
            imageScale = ((float) imageContentWidth) / ((float) srcWidth);
            viewHeight = (int) (((float) srcHeight) * imageScale);
        }


        mImageViewWidth = viewWidth;
        mImageViewHeight = viewHeight;
        Flog.d(TAG, "_Imageview: w=" + mImageViewWidth + "_h=" + mImageViewHeight);
    }

    private void syncBitmapToAll(Bitmap bmp) {
        syncSizeView(bmp);
        bitmapSource = bmp;

        BitmapUtil.free(mFinalBitmap);
        mFinalBitmap = bmp;

        mImgPhoto.setImageBitmap(null);
        mImgPhoto.setImageBitmap(bmp);
        addViewIconSticker(bmp);
        addViewSplash(bmp);
        addViewTextSticker(bmp);
    }


    private void addViewIconSticker(Bitmap bitmap) {

        float imageScale;
        int srcWidth = bitmap.getWidth();
        int srcHeight = bitmap.getHeight();
        float srcRatio = ((float) srcWidth) / ((float) srcHeight);
        int screenWidthDp = ScreenInfoUtil.screenWidthDp(this);
        int screenHeightDp = ScreenInfoUtil.screenHeightDp(this);
        int imageContentWidth = ScreenInfoUtil.dip2px(this, (float) screenWidthDp);
        int imageContentHeight = ScreenInfoUtil.dip2px(this, (float) ((screenHeightDp) - 160));
        float contentRatio = ((float) imageContentWidth) / ((float) imageContentHeight);
        int viewWidth = imageContentWidth;
        int viewHeight = imageContentHeight;
        if (srcRatio <= contentRatio) {
            imageScale = ((float) imageContentHeight) / ((float) srcHeight);
            viewWidth = (int) (((float) srcWidth) * imageScale);
        } else {
            imageScale = ((float) imageContentWidth) / ((float) srcWidth);
            viewHeight = (int) (((float) srcHeight) * imageScale);
        }


        if (mViewIconStickerContainer != null) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mViewIconStickerContainer.getLayoutParams();
            params.width = viewWidth;
            params.height = viewHeight;
            mViewIconStickerContainer.requestLayout();

            mViewIconStickerContainer.setBitmap(bitmap, imageScale);
            mViewIconStickerContainer.invalidate();
        }

    }

    private void addViewTextSticker(Bitmap bitmap) {


        float imageScale;
        int srcWidth = bitmap.getWidth();
        int srcHeight = bitmap.getHeight();
        float srcRatio = ((float) srcWidth) / ((float) srcHeight);
        int screenWidthDp = ScreenInfoUtil.screenWidthDp(this);
        int screenHeightDp = ScreenInfoUtil.screenHeightDp(this);
        int imageContentWidth = ScreenInfoUtil.dip2px(this, (float) screenWidthDp);
        int imageContentHeight = ScreenInfoUtil.dip2px(this, (float) ((screenHeightDp) - 160));
        float contentRatio = ((float) imageContentWidth) / ((float) imageContentHeight);
        int viewWidth = imageContentWidth;
        int viewHeight = imageContentHeight;
        if (srcRatio <= contentRatio) {
            imageScale = ((float) imageContentHeight) / ((float) srcHeight);
            viewWidth = (int) (((float) srcWidth) * imageScale);
        } else {
            imageScale = ((float) imageContentWidth) / ((float) srcWidth);
            viewHeight = (int) (((float) srcHeight) * imageScale);
        }

        if (mViewTextStickerContainer != null) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mViewTextStickerContainer.getLayoutParams();
            params.width = viewWidth;
            params.height = viewHeight;
            mViewTextStickerContainer.requestLayout();

            mViewTextStickerContainer.setBitmap(bitmap, imageScale);
            mViewTextStickerContainer.invalidate();
        }

    }


    public void createBlurBitmap(Bitmap sourceBitmap) {
        Bitmap outputBitmap;
        int blurRadius = Math.round(9.0f * ((float) Math.sqrt((double) (((float) (sourceBitmap.getWidth() * sourceBitmap.getWidth())) / 1000000.0f))));
        if (blurRadius < TAB_SIZE) {
            blurRadius = TAB_SIZE;
        }
        if (sourceBitmap.getWidth() > sourceBitmap.getHeight()) {
            outputBitmap = createCroppedScaledBitmap(sourceBitmap, (sourceBitmap.getWidth() / TAB_SIZE) - (sourceBitmap.getHeight() / TAB_SIZE), INDEX_PIP, sourceBitmap.getHeight(), sourceBitmap.getHeight(), false);
        } else {
            outputBitmap = createCroppedScaledBitmap(sourceBitmap, INDEX_PIP, (sourceBitmap.getHeight() / TAB_SIZE) - (sourceBitmap.getWidth() / TAB_SIZE), sourceBitmap.getWidth(), sourceBitmap.getWidth(), false);
        }
//        EffectFragment.functionToBlur(outputBitmap, blurRadius);
//        bitmapBlur = createBlurBitmapNDK(outputBitmap, 0, 15);

        bitmapBlur = FastBlurFilter.blur(outputBitmap, 5, true);
        Flog.d("bitmapBlur ", " " + bitmapBlur);
    }


    Bitmap loadMaskBitmap2(int resId) {
        return convertToAlphaMask(BitmapFactory.decodeResource(getResources(), resId));
    }

    private Bitmap convertToAlphaMask(Bitmap b) {
        Bitmap a = Bitmap.createBitmap(b.getWidth(), b.getHeight(), Bitmap.Config.ALPHA_8);
        new Canvas(a).drawBitmap(b, 0.0f, 0.0f, null);
        b.recycle();
        return a;
    }

    private void showSaveDialog(final Context context, final OnDialogEventListener listener, final FileItem... fileItem) {

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        String title = context.getString(R.string.save_photo);
        String message = context.getString(R.string.confirm_save_to_storage_dialog);
        alert.setTitle(title)
                .setMessage(message)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (listener != null) {
                            listener.onCancel();
                        }
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        if (listener != null) {
                            listener.onOk();
                        }
                    }
                }).show();
    }

    private void actionRotate() {

        // mis-clicking prevention, using threshold of 1000 ms
        if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        // do your magic here

        showDialog();
        Bitmap bitmap = rotateBmp(mFinalBitmap, mImageViewWidth, mImageViewHeight, 90);
        syncBitmapToAll(bitmap);
        hideDialog();
    }

    private Bitmap rotateBmp(Bitmap bitmapOrg, int width, int height, int angle) {
        Matrix matrix = new Matrix();

        matrix.postRotate(angle);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmapOrg, width, height, true);

        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        return rotatedBitmap;
    }

    private void hideEditFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.bottom_menu);
        if (fragment instanceof EditFragment) {
            getSupportFragmentManager().popBackStack();
        }
    }

    private void actionBack() {
        tittleView.setVisibility(View.INVISIBLE);
        Flog.d(TAG, "actionBack=" + curFragID);
        switch (curFragID) {
            case BaseOptFragment.FRAGMENT_OPT_SPLASH:
                showOptFrag(BaseOptFragment.FRAGMENT_OPT_MENU);
                break;
            case BaseOptFragment.FRAGMENT_OPT_FILTER:
                backToHomeMenu();
                break;
            case BaseOptFragment.FRAGMENT_OPT_PIP:
                backToHomeMenu();
                break;
            case FRAGMENT_OPT_EDIT:
                backToHomeMenu();
                break;
            case BaseOptFragment.FRAGMENT_OPT_TEXT:
                if (TextStickerFragment.flag_back_opacity) {
                    ((TextStickerFragment) textFrag).showEventTextFragment();
                } else {
                    backToHomeMenu();
                }
                break;
            case BaseOptFragment.FRAGMENT_OPT_ICON_STICKER:
                tittleView.setVisibility(View.VISIBLE);
                if (IconStickerFragment.flag_show_details) {
                    ((IconStickerFragment) stickerFrag).showDetails(false);
                } else {
                    mViewIconStickerContainer.reset();
                    backToHomeMenu();
                }
                break;
            default:
                setResult(RESULT_CANCELED, null);
                finish();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        actionBack();
    }

    public void addFrags() {
        fm = getSupportFragmentManager();

        menuFrag = new MenuFragment().setListener(this);
        splashFrag = new SplashFragment().setListener(this);
//        filterFrag = new FilterFragment().setListener(this);
        stickerFrag = new IconStickerFragment().setListener(this);
        textFrag = new TextStickerFragment().setTextStickerContainerView(mViewTextStickerContainer);
        int defaultPrimary = ContextCompat.getColor(this, R.color.s_black);
        textFrag.setColorApp(SharedPrefUtil.getInstance().getInt(ConstValue.EXTRA_CURRENT_COLOR_PICKER, defaultPrimary));
//        editFrag = new EditFragment();

        if (size(fm) > 0) {
            clearBackstack(fm);
        }

        addFragment(fm, menuFrag);
        addFragment(fm, splashFrag);
//        addFragment(fm, pipFrag);
        addFragment(fm, stickerFrag);
        addFragment(fm, textFrag);
//        addFragment(fm, editFrag);


        showOptFrag(BaseOptFragment.FRAGMENT_OPT_MENU);
    }

    private void showOptFrag(int fragID) {

        if (curFragID == -1) {
            hideAllFrags();
        } else {
            showFragAt(curFragID, false);
        }

        showFragAt(fragID, true);
        curFragID = fragID;
    }

    private void showFragAt(int fragID, boolean visibility) {
        switch (fragID) {
            case BaseOptFragment.FRAGMENT_OPT_MENU:
                showFragment(menuFrag, visibility);
                mImgPhoto.setVisibility(visibility ? View.VISIBLE : View.GONE);
//                mBtnRotate.setVisibility(visibility ? View.VISIBLE : View.GONE);
                break;
            case BaseOptFragment.FRAGMENT_OPT_SPLASH:
                showFragment(splashFrag, visibility);
                mViewSplashContainer.setVisibility(visibility ? View.VISIBLE : View.GONE);
                break;
            case BaseOptFragment.FRAGMENT_OPT_ICON_STICKER:
                showFragment(stickerFrag, visibility);
                mViewIconStickerContainer.setVisibility(visibility ? View.VISIBLE : View.GONE);
                break;
            case BaseOptFragment.FRAGMENT_OPT_TEXT:
                showFragment(textFrag, visibility);
                mViewTextStickerContainer.setVisibility(visibility ? View.VISIBLE : View.GONE);
//                break;
//            case BaseOptFragment.FRAGMENT_OPT_EDIT:
//                showFragment(editFrag, visibility);
//                mViewCropContainer.setVisibility(visibility ? View.VISIBLE : View.GONE);
                break;
        }
    }

    private void showFragment(Fragment fragment, boolean visibility) {
        if (visibility) {
            showFragment(fm, fragment);
        } else {
            hideFragment(fm, fragment);
        }
    }

    private void hideAllFrags() {
        hideFragment(fm, menuFrag);
        hideFragment(fm, splashFrag);
        hideFragment(fm, stickerFrag);
        hideFragment(fm, textFrag);
//        hideFragment(fm, editFrag);

        mImgPhoto.setVisibility(View.GONE);
        mViewSplashContainer.setVisibility(View.GONE);
//        mViewPipContainer.setVisibility(View.GONE);
        mViewIconStickerContainer.setVisibility(View.GONE);
        mViewTextStickerContainer.setVisibility(View.GONE);
    }

    private void addFragment(FragmentManager fm, Fragment fragment) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.recycler_view_container, fragment);
        ft.commit();
    }

    private void showFragment(FragmentManager fm, Fragment fragment) {
        fm.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .show(fragment)
                .commit();
    }

    private void hideFragment(FragmentManager fm, Fragment fragment) {
        fm.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .hide(fragment)
                .commit();
    }

    private int size(FragmentManager fm) {
        return fm.getBackStackEntryCount();
    }

    private void clearBackstack(FragmentManager fm) {
        fm.popBackStack(R.id.recycler_view_container, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void backPressFilter(Bitmap bitmap) {
        syncBitmapToAll(bitmap);
    }

    @Override
    public void onFilterExit() {
        backToHomeMenu();
    }

    private void backToHomeMenu() {
        tittleView.setVisibility(View.INVISIBLE);
        getSupportFragmentManager().popBackStack();
        showOptFrag(BaseOptFragment.FRAGMENT_OPT_MENU);
    }

    @Override
    public void onStickerIconFragmentReady() {
        if (cntThread == TOTAL_THREADS) {
            return;
        }


        ((IconStickerFragment) stickerFrag).initData();

        cntThread++;
        if (cntThread == TOTAL_THREADS) {
            onAllThreadFinished();
        }
    }

    @Override
    public void onItemStickerIconClicked(String path) {
        addIconStickerView(path);
    }

    private void addIconStickerView(String path) {
        showDialog();

//        final ItemStickerView stickerView = new ItemStickerView(mViewIconStickerContainer);
//        stickerView.setOnItemInteractListener(this);
//        stickerView.setBitmap(BitmapUtil.getImageFromAssetsFile(getResources(), path));
//        mViewIconStickerContainer.addItem(stickerView, mViewIconStickerContainer.getHeight());
//        Statics.OPACITY = stickerView.getOpacity();
//        mViewIconStickerContainer.invalidate();

        final IconStickerView stickerView = new IconStickerView(mViewIconStickerContainer, mImageViewWidth, mImageViewHeight);
        stickerView.setBitmap(BitmapUtil.getImageFromAssetsFile(getResources(), path));
        mViewIconStickerContainer.addItem(stickerView);
        mViewIconStickerContainer.invalidate();

        hideDialog();
    }


    @Override
    public void onInputTextSticker(int textStickerIndex) {
        Flog.d(TAG, "onEditInputTextSticker idx=" + textStickerIndex);

        BaseStickerView stickerView = mViewTextStickerContainer.getCurItem();
        if (!(stickerView instanceof TextStickerView)) {
            return;
        }
        String oldText = ((TextStickerView) stickerView).getText();
        Flog.d(TAG, "oldText123=" + oldText);
        Intent intent = new Intent(EditPhotoActivity.this, TextInputActivity.class);
        intent.putExtra(ConstValue.EXTRA_CURRENT_TEXT_STICKER, oldText);
        intent.putExtra(ConstValue.EXTRA_STATUS_TEXT_EDIT_INPUT, TextInputActivity.STATUS_EDIT_TEXT_INPUT);
        startActivityForResult(intent, ConstValue.REQUEST_CODE_TEXT_INPUTED);
    }

    @Override
    public void onDeleteTextSticker(int textStickerIndex) {
        actionBack();
    }

    @Override
    public void onSelectCrop() {
//        mBitmapEdit = Bitmap.createBitmap(mBitmapEdit, 0, 0, mBitmapEdit.getWidth(), mBitmapEdit.getHeight(), matrix, true);
        getSupportFragmentManager().beginTransaction()
//                .setCustomAnimations(R.anim., R.anim.lidow_slide_left_exit, R.anim.lidow_slide_left_enter, R.anim.lidow_slide_left_exit)
                .add(R.id.recycler_view_container, CropFragment.newInstance(bitmapSource, this))
                .addToBackStack(CropFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public void onSelectFlip1() {
        float DEFAULT_FONT_SCALE = 1.0f;
        matrixBitmap = new Matrix();
        Bitmap bmp = bitmapSource;
        matrixBitmap.postScale(DEFAULT_FONT_SCALE, -1, (float) (bmp.getWidth() / 2), (float) (bmp.getHeight() / 2));
        matrixBitmap.postRotate(180, (float) (bmp.getWidth() / 2), (float) (bmp.getHeight() / 2));
        Bitmap bmptmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrixBitmap, true);
//        bitmapSource = bmptmp;
        mImgPhoto.setImageBitmap(bmptmp);
        matrixBitmap = mImgPhoto.getImageMatrix();
        matrixBitmap.set(matrixBitmap);
        syncBitmapToAll(bmptmp);
    }

    @Override
    public void onSelectFlip2() {
        matrixBitmap = new Matrix();
        Bitmap bmp = bitmapSource;
        matrixBitmap.postScale(-1, 1, (float) (bmp.getWidth() / 2), (float) (bmp.getHeight() / 2));
        matrixBitmap.postRotate(180, (float) (bmp.getWidth() / 2), (float) (bmp.getHeight() / 2));
        Bitmap bmptmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrixBitmap, true);
//        bitmapSource = bmptmp;
        mImgPhoto.setImageBitmap(bmptmp);
        matrixBitmap = mImgPhoto.getImageMatrix();
        matrixBitmap.set(matrixBitmap);
        syncBitmapToAll(bmptmp);
    }

    @Override
    public void onSelectRotateRight() {
        Bitmap mBitmapEdit = bitmapSource;
        matrixBitmap = new Matrix();
        matrixBitmap.postRotate(-90.0f, (float) (mBitmapEdit.getWidth() / 2), (float) (mBitmapEdit.getHeight() / 2));
        mBitmapEdit = Bitmap.createBitmap(mBitmapEdit, 0, 0, mBitmapEdit.getWidth(), mBitmapEdit.getHeight(), matrixBitmap, true);
        mImgPhoto.setImageBitmap(mBitmapEdit);
        matrixBitmap = mImgPhoto.getImageMatrix();
        matrixBitmap.set(matrixBitmap);
        syncBitmapToAll(mBitmapEdit);
    }

    @Override
    public void onSelectRotateLeft() {
        Bitmap mBitmapEdit = bitmapSource;
        matrixBitmap = new Matrix();
        matrixBitmap.postRotate(+90.0f, (float) (mBitmapEdit.getWidth() / 2), (float) (mBitmapEdit.getHeight() / 2));
        mBitmapEdit = Bitmap.createBitmap(mBitmapEdit, 0, 0, mBitmapEdit.getWidth(), mBitmapEdit.getHeight(), matrixBitmap, true);
        mImgPhoto.setImageBitmap(mBitmapEdit);
        matrixBitmap = mImgPhoto.getImageMatrix();
        matrixBitmap.set(matrixBitmap);
        syncBitmapToAll(mBitmapEdit);
    }

    public Bitmap getResizedBitmap(Bitmap bm) {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
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

    @Override
    public void onCropDone(Bitmap s) {
        getSupportFragmentManager().popBackStack();
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        mImgPhoto.setImageBitmap(getResizedBitmap(s));
        syncBitmapToAll(getResizedBitmap(s));
//        mBitmapEdit = s;
//        int desiredWidth = Utils.getScreenSize((ShowImageActivity) mContext)[0];
//        Bitmap bitmap = BitmapUtil.sampeZoomFromBitmap(mBitmapEdit, desiredWidth, desiredWidth);
//        mImEdit.setImageBitmap(bitmap);
    }


    private class DecodeImageAsync extends AsyncTask<String, Void, Bitmap> {

        private Context mContext = null;
        float height;

        public DecodeImageAsync(Context context) {
            mContext = context;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String path = strings[0];
            if (path == null || !new File(path).exists()) {
                return null;
            }
            int widthScreen = Utils.getScreenSize(mContext)[0];
            if (widthScreen <= 0) {
                return null;
            }

            DisplayMetrics mDisplayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
            return new ResizeImage(mContext).getBitmap(path, (mDisplayMetrics.widthPixels), mDisplayMetrics.heightPixels);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            onPhotoLoaded(bitmap);
        }
    }
}
