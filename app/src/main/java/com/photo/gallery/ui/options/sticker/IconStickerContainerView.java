package com.photo.gallery.ui.options.sticker;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import androidx.appcompat.widget.AppCompatEditText;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.photo.gallery.utils.Flog;
import com.photo.gallery.utils.options.ScreenInfoUtil;

import java.util.List;

/**
 * Created by hoavt on 20/07/2016.
 */
public class IconStickerContainerView extends FrameLayout {
    public static final int MIN_PIXEL_WIDTH_SAVED = 960;
    public static final int DRAGGING_ALPHA_PAINT = 155;
    private static final String TAG = IconStickerContainerView.class.getSimpleName();
    boolean isInEdit = false;
    int[] oldSize = new int[2];
    private boolean mIsDragging = false;
    private int mSaveCanvasWidth;
    private int mSaveCanvasHeight;
    private float mRatioView = 1.0f;
    private boolean isKeyboardShown = false;
    private float lastTranslateY;
    private int keyboardHeight;
    private boolean isTranslated = false;
    private Context mContext;


    private ListAdaptiveItem mListItem = new ListAdaptiveItem();

    private GestureDetector mGestureDetector;
    private Paint mAlphaPaint;
    private TextWatcher curTextWatcher;
    private EditText virtualEditText;
    private Rect mDesRect = new Rect();
    private Bitmap mBitmap;

    private Matrix mImageMatrix = new Matrix();

    public IconStickerContainerView(Context context) {
        super(context);
        if (!isInEditMode()) {
            initViews(context);
        }
    }

    public IconStickerContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            initViews(context);
        }
    }

    public IconStickerContainerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            initViews(context);
        }
    }

    private void initViews(Context context) {

        mContext = context;
        initPaint();
        initGesture();

        virtualEditText = new AppCompatEditText(context);
        virtualEditText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(1, 1);
        virtualEditText.setLayoutParams(params);

        virtualEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        addView(virtualEditText);

        virtualEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    dismissKeyboard();
                }
                return false;
            }
        });

        setWillNotDraw(false);
    }

//    public void setListStickers(ListAdaptiveItem listStickers) {
//        mListStickers = listStickers;
//    }

    private Activity getActivity() {
        return (Activity) mContext;
    }

    private void initPaint() {
        mAlphaPaint = new Paint();
        mAlphaPaint.setAlpha(DRAGGING_ALPHA_PAINT);
    }

    private void initGesture() {
        GestureDetector.OnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent event) {
                super.onLongPress(event);

                if (mListItem.isDeleteAll() || mListItem.notTouchAll()) {  // apply only for dragging photo-item
                    // prepare dragging for photo item
                    mIsDragging = true;
                }
            }
        };

        mGestureDetector = new GestureDetector(mContext, gestureListener);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        canvas.save();
        Flog.d(TAG, "1111draw collage="+mBitmap);
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, mImageMatrix, null);
        }
        mListItem.onDraw(canvas);
        canvas.restore();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (event == null) return true;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            isInEdit = mListItem.isInEdit();
        }
        // onTouch of sticker is unique
        if (!mListItem.isDeleteAll() && mListItem.onTouchEvent(event)) {
//                 Flog.d("Duc", "move sticker");
            mIsDragging = false;
            invalidate();   // update onDraw Stickers
            return true;
        }

        if (mGestureDetector.onTouchEvent(event)) {
            invalidate();   // update onLongClick
            return true;
        }

        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!mListItem.isDeleteAll()) {
                    // update when touch to sticker
                    mListItem.setCurrentItem(event);
                }
                break;
        }
        postInvalidate();
        return true;
    }


    public void registerKeyboardEvent(TextWatcher textWatcher) {
        if (curTextWatcher != null) {
            virtualEditText.removeTextChangedListener(curTextWatcher);
        }
        curTextWatcher = textWatcher;
        virtualEditText.addTextChangedListener(textWatcher);
    }

    public void unregisterKeyboardEvent(TextWatcher textWatcher) {
        virtualEditText.removeTextChangedListener(textWatcher);
    }

    public void setStickerViewList(ListAdaptiveItem stickerViewList) {
        mListItem = stickerViewList;
    }

    public void requestKeyboard() {
        if (isKeyboardShown) return;
        if (!isCurrentBubbleText()) return;
        showRealEditText();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        isKeyboardShown = true;
        registerKeyboardEvent((TextWatcher) mListItem.getCurrentItem());
        invalidate();
        //  performAnimation();
    }

    private boolean isCurrentBubbleText() {
        return mListItem.getCurrentItem() instanceof ItemBubbleTextView;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void showRealEditText() {

        if (mListItem.getCurrentItem() == null) return;
        if (!isCurrentBubbleText()) return;
        ViewGroup.LayoutParams params = virtualEditText.getLayoutParams();
        params.width = 0;
        params.height = 0;

        virtualEditText.setLayoutParams(params);
//        virtualEditText.setBackground(getResources().getDrawable(R.drawable.rect));
        virtualEditText.setTextColor(Color.WHITE);
        virtualEditText.setIncludeFontPadding(true);
        virtualEditText.setPadding(ScreenInfoUtil.dip2px(getContext(), 5),
                0, ScreenInfoUtil.dip2px(getContext(), 15), virtualEditText.getPaddingBottom());
        virtualEditText.removeTextChangedListener(curTextWatcher);
        virtualEditText.getEditableText().clear();
        virtualEditText.append(((ItemBubbleTextView) mListItem.getCurrentItem()).getText());
        virtualEditText.addTextChangedListener(curTextWatcher);
        virtualEditText.invalidate();
        virtualEditText.requestFocus();
        virtualEditText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD | InputType.TYPE_MASK_CLASS);
        isKeyboardShown = true;
    }

    public void hideRealEditText() {
        ViewGroup.LayoutParams params = virtualEditText.getLayoutParams();
        params.width = 0;
        params.height = 0;
        virtualEditText.setLayoutParams(params);
        virtualEditText.invalidate();
        isKeyboardShown = false;
    }


    public boolean isKeyboardShown() {
        return isKeyboardShown;
    }

    public void unSelectAllComponent() {
        mListItem.setNotTouchAll();
        invalidate();
    }

    public void dismissKeyboard() {
        if (!isKeyboardShown) return;
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(virtualEditText.getWindowToken(), 0);
        hideRealEditText();
        if (curTextWatcher != null) {
            virtualEditText.removeTextChangedListener(curTextWatcher);
        }
        isKeyboardShown = false;
    }

    public void setSaveBitmapSize(int saveCanvasWidth, int saveCanvasHeight) {
        mSaveCanvasWidth = saveCanvasWidth;
        mSaveCanvasHeight = saveCanvasHeight;
    }

    public void setBitmap(Bitmap bitmap, int width, int height) {
        ourBitmapRecycle(mBitmap, false);
        mBitmap = bitmap;
        setImageMatrix(width, height);
    }

    public void setImageMatrix(int width, int height) {
        mImageMatrix = new Matrix();


        float left = (width - mBitmap.getWidth())/2f;
        float top = (height - mBitmap.getHeight())/2f;
        mImageMatrix.setTranslate(left, top);
        float scaleX = width / mBitmap.getWidth();
        float scaleY = height / mBitmap.getHeight();
        float scaleFitScreen = Math.min(scaleX, scaleY);
        mImageMatrix.postScale(scaleFitScreen, scaleFitScreen, width/2, height/2);
    }

    public static void ourBitmapRecycle(Bitmap bmp, boolean force) {
        if (!(bmp == null || bmp.isRecycled())) {
            bmp.recycle();
        }
    }

    public void setDesRect(Rect desRect) {
        mDesRect = desRect;
    }

    public int getSaveCanvasHeight() {
        return mSaveCanvasHeight;
    }

    public int getSaveCanvasWidth() {
        return mSaveCanvasWidth;
    }

    public float getRatioSavedView() {
        return mRatioView;
    }

    public void setRatioSavedView(float ratio) {
        mRatioView = ratio;
    }

    public int[] getOldSize() {
        return oldSize;
    }

    public void putOldSize() {
        oldSize[0] = getWidth();
        oldSize[1] = getHeight();
    }


    public void addItem(BaseItem item, int h) {
        if (item instanceof ItemStickerView) {
            ((ItemStickerView) item).setTranslateInit(h);
        } else if (item instanceof ItemBubbleTextView) {
            ((ItemBubbleTextView) item).setTranslateInit(h);
        }
        mListItem.setNotTouchAll();
        mListItem.add(item);
        mListItem.setCurrentItem(mListItem.size() - 1);
    }

    public void setNotTouchAll() {
        mListItem.notTouchAll();
        invalidate();
    }

    public int getItemSize() {
        return mListItem.size();
    }

    public BaseItem getCurrentItem() {
        return mListItem.getCurrentItem();
    }

    public ListAdaptiveItem getListItem() {
        return mListItem;
    }

    public void setListItem(ListAdaptiveItem listAdaptiveItem) {
        if (mListItem != null) releaseListItemView(mListItem);
        this.mListItem = listAdaptiveItem;
    }

    private void releaseListItemView(List<? extends BaseItem> list) {
        for (BaseItem item : list) {
            if (item != null) item.release();
        }
    }


    public void release() {
        releaseListItemView(mListItem);
        mGestureDetector = null;
        mAlphaPaint = null;
        curTextWatcher = null;
        virtualEditText = null;
        mDesRect = null;
    }

    public void reset() {
        releaseListItemView(mListItem);
        if (mListItem!=null) {
            mListItem.clear();
        }
    }
}