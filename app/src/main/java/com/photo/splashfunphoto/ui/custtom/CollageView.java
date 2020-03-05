package com.photo.splashfunphoto.ui.custtom;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
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

import androidx.appcompat.widget.AppCompatEditText;

import com.photo.gallery.R;
import com.photo.splashfunphoto.ui.custtom.text.BaseItem;
import com.photo.splashfunphoto.ui.custtom.text.ItemBubbleTextView;
import com.photo.splashfunphoto.ui.custtom.text.ItemStickerView;
import com.photo.splashfunphoto.ui.custtom.text.ListAdaptiveItem;
import com.photo.splashfunphoto.utils.ScreenInfoUtil;

import java.util.ArrayList;
import java.util.List;

public class CollageView extends FrameLayout {
    public static final int MIN_PIXEL_WIDTH_SAVED = 960;
    public static final int DRAGGING_ALPHA_PAINT = 155;
    private static final String TAG = CollageView.class.getSimpleName();
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

    private ArrayList<ItemStickerView> lists = new ArrayList<>();

    public CollageView(Context context) {
        super(context);
        if (!isInEditMode()) {
            initViews(context);
        }
    }

    public CollageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            initViews(context);
        }
    }

    public CollageView(Context context, AttributeSet attrs, int defStyleAttr) {
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
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.save();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        if (mBitmap != null) canvas.drawBitmap(mBitmap, null, mDesRect, paint);
        mListItem.onDraw(canvas);
        canvas.restore();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        super.dispatchTouchEvent(event);
        if (event == null) return true;
        if(mListItem == null) return true;
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
        Log.d(TAG, "request Keyboard");
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
        virtualEditText.setInputType( InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD | InputType.TYPE_MASK_CLASS);
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
        Log.d(TAG, "dismiss keyboard");
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

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
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


    public void addItem(BaseItem item) {
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

}