package bsoft.healthy.tracker.menstrual.lib_sticker.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import java.util.List;

import bsoft.healthy.tracker.menstrual.lib_sticker.models.ListAdaptiveItem;

/**
 * Created by hoavt on 20/07/2016.
 */
public class StickerContainerView extends FrameLayout {
    private static final String TAG = StickerContainerView.class.getSimpleName();
    boolean isInEdit = false;
    private Context mContext;
    private ListAdaptiveItem mListItem = new ListAdaptiveItem();
    private Paint mPaint;
    private Bitmap mBitmap;
    private Matrix mImageMatrix = new Matrix();
    private int curIdx = -1;

    public StickerContainerView(Context context) {
        super(context);
        if (!isInEditMode()) {
            initViews(context);
        }
    }

    public StickerContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            initViews(context);
        }
    }

    public StickerContainerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            initViews(context);
        }
    }

    public static Bitmap ourBitmapRecycle(Bitmap bmp) {
        if (!(bmp == null || bmp.isRecycled())) {
            bmp.recycle();
        }
        return null;
    }

    private void initViews(Context context) {

        mContext = context;
        initPaint();

        setWillNotDraw(false);
    }

    private void initPaint() {
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.save();
        Log.d(TAG, "draw collage=" + mBitmap);
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, mImageMatrix, null);
        }
        mListItem.onDraw(canvas);
        canvas.restore();
    }

    private void drawList(Canvas canvas) {
        for (int i = 0; i < mListItem.size(); i++) {
            BaseStickerView textItem = mListItem.get(i);
            textItem.onDraw(canvas);
        }
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
            invalidate();   // update onDraw Stickers
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
        invalidate();
        return true;
    }

//    public void setBitmap(Bitmap bitmap, int width, int height) {
//        mBitmap = ourBitmapRecycle(mBitmap);
//        mBitmap = bitmap;
//        setImageMatrix(width, height);
//    }

    public void setBitmap(Bitmap bitmap, float scale) {
        mBitmap = ourBitmapRecycle(mBitmap);
        mBitmap = bitmap;
        setImageMatrix(scale);
    }

    public void setImageMatrix(float scaleFitScreen) {
        mImageMatrix = new Matrix();

//
//        float left = (width - mBitmap.getWidth()) / 2f;
//        float top = (height - mBitmap.getHeight()) / 2f;
//        mImageMatrix.setTranslate(left, top);
//        float scaleX = width * 1f / mBitmap.getWidth();
//        float scaleY = height * 1f/ mBitmap.getHeight();
//        float scaleFitScreen = Math.min(scaleX, scaleY);
        mImageMatrix.postScale(scaleFitScreen,scaleFitScreen);
    }

    public int getItemSize() {
        return mListItem.size();
    }

    private void releaseListItemView(List<? extends BaseStickerView> list) {
        for (BaseStickerView item : list) {
            if (item != null) item.release();
        }
    }


    public void release() {
        releaseListItemView(mListItem);
        mPaint = null;
    }

    public void reset() {
        if (mListItem != null) {
            mListItem.clear();
        }
    }

    public void addItem(BaseStickerView itemSticker) {
        if (mListItem == null) {
            mListItem = new ListAdaptiveItem();
        }

        mListItem.setNotTouchAll();
        mListItem.add(itemSticker);
        mListItem.setCurrentItem(mListItem.size() - 1);
    }

    public BaseStickerView getCurItem() {
        try {

            return mListItem.getCurrentItem();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public int getCurIdx() {
        return curIdx;
    }

    public ListAdaptiveItem getListItem() {
        return mListItem;
    }

    public void unSelectAllComponent() {
        mListItem.setNotTouchAll();
        invalidate();
    }

    public void moveToTop() {
        mListItem.moveToTop();
    }
}