package bsoft.healthy.tracker.menstrual.lib_sticker.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;
import androidx.core.view.MotionEventCompat;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import bsoft.healthy.tracker.menstrual.lib_sticker.R;
import bsoft.healthy.tracker.menstrual.lib_sticker.fragments.TextMoreAdjustFragment;

/**
 * Created by vutha on 3/29/2017.
 */

public class TextStickerView extends BaseStickerView {

    public static final int TEXT_ALIGN_LEFT = 0x11;
    public static final int TEXT_ALIGN_CENTER = 0x12;
    public static final int TEXT_ALIGN_RIGHT = 0x13;
    public static final float STEP_MOVE = 10f;
    public static final float STEP_ROTATE = 5f;
    public static final float STEP_SCALE_IN = 1.05f;
    public static final float STEP_SCALE_OUT = 0.95f;
    public static final int MOVE_UP = 0;
    public static final int MOVE_DOWN = 1;
    public static final int MOVE_LEFT = 2;
    public static final int MOVE_RIGHT = 3;
    public static final int MIN_OPACITY_VALUE = 20;
    public static final int MAX_OPACITY_VALUE = 255;
    private static final String TAG = TextStickerView.class.getSimpleName();
    public static int DEFAULT_PROGRESS_TEXT_SIZE;
    public final float MOVE_DISTANCE_MAX = 10f;
    private final float POINTER_DISTANCE_MAX = 20f;
    private final float POINTEER_ZOOM_COEFF = 0.09f;
    private final float BITMAP_SCALE = 0.5f;
    private int mHeighView = 0, mWidthView = 0;
    private boolean isMovable;
    private boolean isInBitmap;
    private float lastRotateDegree;
    private float lastLength;
    private float lastX, lastY;
    private float oldDis;
    private float scaleValue = 1.0f;
    private PointF mid = new PointF();
    private float minScale, maxScale;
    private Paint mPaint;
    private Paint mLinePaint;
    private Bitmap deleteBitmap;
    private Bitmap resizeBitmap;
    private Bitmap flipVBitmap;
    private Bitmap topBitmap;
    private Rect dst_delete;
    private Rect dst_resize;
    private Rect dst_flipV;
    private Rect dst_top;
    private int deleteBitmapWidth;
    private int deleteBitmapHeight;
    private int resizeBitmapWidth;
    private int resizeBitmapHeight;
    private int flipVBitmapWidth;
    private int flipVBitmapHeight;
    private int topBitmapWidth;
    private int topBitmapHeight;
    private Bitmap mBitmap;
    private boolean isInResize;
    private float[] arrayOfFloat = new float[9];
    private double halfDiagonalLength;
    private Matrix mMatrix;
    private int mIndex = -1;
    private Context mContext = null;
    private String mDefaultText;
    private String mText;
    private int mTextColor;
    private TextPaint mTextFont;
    private Canvas mTextCanvas;
    private boolean mIsInit;
    private int mScreenwidth, mScreenHeight;
    /**
     * Margin both: from left and from right.
     */
    private int mTextMargin;
    private int mTextMaxWidth;
    private String[] mTextLines;
    private ArrayList<String> mLines = new ArrayList<>();
    private Bitmap mPatternBmp;
    private DisplayMetrics mDisplayMetrics;
    /**
     * The progress value of seekbar for size and padding of the text sticker.
     */
    private int mProgressSize = 0;
    private int mTextPadding = 0;
    /**
     * The type of align of text.
     */
    private int mTextAlign = TEXT_ALIGN_LEFT;
    private StickerContainerView mParentView = null;
    private boolean mNotTranslate;
    private boolean mNotScale;
    private boolean isPointerDown;
    private boolean isInEdit;
    private TextStickerViewListener listener = null;

    public TextStickerView(StickerContainerView parentView) {

        mParentView = parentView;

        mContext = parentView.getContext();

        mMatrix = new Matrix();
        initPaints();
        initOptions();
        inits();
    }

    public TextStickerView(StickerContainerView parentView, int widthView, int heightView) {

        mParentView = parentView;
        mWidthView = widthView;
        mHeighView = heightView;

        mContext = parentView.getContext();

        mMatrix = new Matrix();
        initPaints();
        initOptions();
        inits();
    }

    public static int getDefaultTextSize(Context context) {
        return (int) context.getResources().getDimension(R.dimen.font_size_small);
    }

    public static Bitmap recycleBitmap(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }
        try {
            bitmap.recycle();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        bitmap = null;
        return null;
    }

    public void setIndex(int index) {
        mIndex = index;
    }

    private void initPaints() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

        mLinePaint = new Paint();
        mLinePaint.setFilterBitmap(true);
        mLinePaint.setColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
        mLinePaint.setAntiAlias(true);
        mLinePaint.setDither(true);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(2.0f);
    }

    private void initOptions() {
        if (topBitmap == null)
            topBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_edit);
        if (deleteBitmap == null)
            deleteBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_delete);
        if (flipVBitmap == null)
            flipVBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_flip);
        if (resizeBitmap == null)
            resizeBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_resize);

        if (deleteBitmap != null) {
            deleteBitmapWidth = (int) (deleteBitmap.getWidth() * BITMAP_SCALE);
            deleteBitmapHeight = (int) (deleteBitmap.getHeight() * BITMAP_SCALE);
        }

        if (resizeBitmap != null) {
            resizeBitmapWidth = (int) (resizeBitmap.getWidth() * BITMAP_SCALE);
            resizeBitmapHeight = (int) (resizeBitmap.getHeight() * BITMAP_SCALE);
        }

        if (flipVBitmap != null) {
            flipVBitmapWidth = (int) (flipVBitmap.getWidth() * BITMAP_SCALE);
            flipVBitmapHeight = (int) (flipVBitmap.getHeight() * BITMAP_SCALE);
        }

        if (topBitmap != null) {
            topBitmapWidth = (int) (topBitmap.getWidth() * BITMAP_SCALE);
            topBitmapHeight = (int) (topBitmap.getHeight() * BITMAP_SCALE);
        }

        dst_delete = new Rect();
        dst_resize = new Rect();
        dst_flipV = new Rect();
        dst_top = new Rect();
    }

    private void inits() {

        DEFAULT_PROGRESS_TEXT_SIZE = (int) mContext.getResources().getDimension(R.dimen.font_size_small);
        mProgressSize = DEFAULT_PROGRESS_TEXT_SIZE;
        mDefaultText = mContext.getString(R.string.enter_text);
        mTextColor = Color.WHITE;
        mText = "";

        mDisplayMetrics = mContext.getResources().getDisplayMetrics();
        mScreenwidth = mDisplayMetrics.widthPixels;
        mScreenHeight = mDisplayMetrics.heightPixels;

        mTextFont = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        mTextFont.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, DEFAULT_PROGRESS_TEXT_SIZE, mDisplayMetrics));
        mTextFont.setColor(mTextColor);

        mTextMargin = 20;
        mTextMaxWidth = mScreenwidth * 8 / 9;
        mTextLines = new String[]{};

        mIsInit = true;

//        resizeTextCanvas();
    }

    /**
     * When size of text is changed, resizing textCanvas for drawing text on it.
     */
    private void resizeTextCanvas() {

//        autoSplit(mText, mTextFont, mTextMaxWidth - mTextMargin);

        String textWithMaxLen = "";
        mTextLines = separateText(mText);
        mLines.clear();
        for (int i = 0; i < mTextLines.length; i++) {
            String[] pLines = autoSplit(mTextLines[i], mTextFont, mTextMaxWidth - mTextMargin);
            if (pLines.length <= 0) {
                continue;
            } else if (pLines.length == 1) {
                mLines.add(mTextLines[i]);
                if (textWithMaxLen.length() < mLines.get(mLines.size() - 1).length()) {
                    textWithMaxLen = mLines.get(mLines.size() - 1);
                }
            } else {
                for (int j = 0; j < pLines.length; j++) {
                    mLines.add(pLines[j]);
                    if (textWithMaxLen.length() < mLines.get(mLines.size() - 1).length()) {
                        textWithMaxLen = mLines.get(mLines.size() - 1);
                    }
                }
            }
        }

//        Log.d(TAG, "text size=" + mLines.size());
//        for (int i = 0; i < mLines.size(); i++) {
//            Log.d(TAG, "text at " + i + " : " + mLines.get(i));
//        }

        String curText = TextUtils.isEmpty(textWithMaxLen) ? mDefaultText : textWithMaxLen;

        /**
         * Get dimension(s) of text. There are two ways:
         * 1- Using Font.getTextBounds(str, 0, str.length(), rect) -> get width & height of text.
         * 2- Using Font.measureText(str) -> get width of text.
         * */
//        Rect boundRect = new Rect();
//        mTextFont.getTextBounds(curText, 0, curText.length(), boundRect);
//        Log.d(TAG, "mTextLines len=" + mTextLines.length);
//        Log.d(TAG, "height text = " + (mTextPadding * mTextLines.length));
        Paint.FontMetrics fontMetrics = mTextFont.getFontMetrics();
//        float additionalHeight = (mTextLines.length - 1) * (Math.abs(mTextFont.getFontMetrics().leading + mTextPadding));
        float additionalHeight = (mLines.size() - 1) * (Math.abs(mTextFont.getFontMetrics().leading + mTextPadding));
//        Log.d(TAG, "additionalHeight=" + additionalHeight);
//        float heightText = (fontMetrics.descent - fontMetrics.ascent) * mTextLines.length + additionalHeight;
        float heightText = (fontMetrics.descent - fontMetrics.ascent) * mLines.size() + additionalHeight;
//        Log.d(TAG, "width 1="+mTextFont.measureText(curText)+"_2="+boundRect.width());
        int widthText = (int) mTextFont.measureText(curText);
//        Log.d(TAG, "size text: w=" + widthText + "_h=" + heightText + "_mTextMaxWidth=" + mTextMaxWidth);
        if (widthText >= mTextMaxWidth)
            widthText = mTextMaxWidth + mTextMargin;
//        Log.d(TAG, "mHeighView=" + mHeighView);

        if (widthText <= 0 || heightText <= 0) return;
        try {
            Bitmap bmpText = Bitmap.createBitmap(widthText, Math.round(heightText), Bitmap.Config.ARGB_4444);
            Log.d(TAG, "bmp w=" + bmpText.getWidth() + "_h=" + bmpText.getHeight());
            setBitmap(bmpText);
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        }
    }

    private String[] separateText(String text) {
        return text.split("\n");
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        Log.d(TAG, "setText text=" + text + "\nlen=" + text.length());
        if (text == null || text.trim().equals("")) {
            deleteSelf();
            mParentView.invalidate();
            if (listener!=null) {
                listener.onDeleteTextSticker(mIndex);
            }
            return;
        }

        mText = text;
        resizeTextCanvas();
    }

    private String[] autoSplit(String content, Paint p, float width) {
        Log.d(TAG, "autoSplit: content=" + content);
        int length = content.length();
        float textWidth = p.measureText(content);
        Log.d(TAG, "autoSplit: textWidth=" + textWidth + "_width=" + width);
        if (textWidth <= width) {
            return new String[]{content};
        }

        int start = 0, end = 1, i = 0;
        int lines = (int) Math.ceil(textWidth / width); //计算行数
        String[] lineTexts = new String[lines];
        while (start < length) {
            if (i >= lines)
                break;
            if (p.measureText(content, start, end) > width) { //文本宽度超出控件宽度时
                lineTexts[i++] = (String) content.subSequence(start, end);
                start = end;
            }
            if (start < end && end == length) { //不足一行的文本
                lineTexts[i] = (String) content.subSequence(start, end);
                break;
            }
            end += 1;
        }
        return lineTexts;
    }

    @Override
    public void setType() {
        mType = BaseStickerView.TEXT_STICKER;
    }

    public void onDraw(Canvas canvas) {
        if (mBitmap == null || mBitmap.isRecycled() || mMatrix == null) return;

        if (mTextFont != null && mTextFont.getShader() != null) {
            mTextFont.getShader().setLocalMatrix(mMatrix);
        }

        Paint.FontMetrics fontMetrics = mTextFont.getFontMetrics();
        /* ref: http://wikiwiki.jp/android/?%A5%C6%A5%AD%A5%B9%A5%C8%A4%CE%C9%C1%B2%E8(FontMetrics) */
//        Log.d(TAG, "fontMetrics: ascent=" + fontMetrics.ascent + "_descent=" + fontMetrics.descent);
//        Log.d(TAG, "mTextLines=" + mTextLines.length + "_leading=" + mTextFont.getFontMetrics().leading);

        float distance = (fontMetrics.descent - fontMetrics.ascent) + mTextPadding;
//        Log.d(TAG, "distance="+distance+"_x="+mTextFont.measureText(mText)/2);
        float paddingTop = 0;
        for (int i = 0; i < mLines.size(); i++) {
            String text = mLines.get(i);
            if (text == null)
                text = "";
//            Log.d(TAG, "text=" + text);
            if (TextUtils.isEmpty(text)) {
//                continue;
            }
//            Log.d(TAG, "paddingTop=" + paddingTop);
//            float centerX = mTextFont.measureText(text) / 2;
            float centerY = (fontMetrics.descent + fontMetrics.ascent) / 2 + distance * (mLines.size() - 1) / 2;
//                mTextCanvas.drawText(text, (mBitmap.getWidth() >> 1) - centerX, (mBitmap.getHeight() >> 1) - centerY + paddingTop, mTextFont);
            float startX = getStartXAlign(text);
            Log.d(TAG, "startX=" + startX);
            if (startX < 0) {
                startX = 0;
            }
            mTextCanvas.drawText(text, startX, (mBitmap.getHeight() >> 1) - centerY + paddingTop, mTextFont);
            paddingTop += distance;
        }

        canvas.drawBitmap(mBitmap, mMatrix, mPaint);

        if (isInEdit) {

            mMatrix.getValues(arrayOfFloat);
            float f1 = arrayOfFloat[2];
            float f2 = arrayOfFloat[5];
            float f3 = arrayOfFloat[0] * mBitmap.getWidth() + arrayOfFloat[2];
            float f4 = arrayOfFloat[3] * mBitmap.getWidth() + arrayOfFloat[5];
            float f5 = arrayOfFloat[1] * mBitmap.getHeight() + arrayOfFloat[2];
            float f6 = arrayOfFloat[4] * mBitmap.getHeight() + arrayOfFloat[5];
            float f7 = arrayOfFloat[0] * mBitmap.getWidth() + arrayOfFloat[1] * mBitmap.getHeight() + arrayOfFloat[2];
            float f8 = arrayOfFloat[3] * mBitmap.getWidth() + arrayOfFloat[4] * mBitmap.getHeight() + arrayOfFloat[5];

            //删除在右上角
            dst_delete.left = (int) (f3 - (deleteBitmapWidth >> 1));
            dst_delete.right = (int) (f3 + (deleteBitmapWidth >> 1));
            dst_delete.top = (int) (f4 - (deleteBitmapHeight >> 1));
            dst_delete.bottom = (int) (f4 + (deleteBitmapHeight >> 1));
            //垂直镜像在左上角
            dst_top.left = (int) (f1 - (flipVBitmapWidth >> 1));
            dst_top.right = (int) (f1 + (flipVBitmapWidth >> 1));
            dst_top.top = (int) (f2 - (flipVBitmapHeight >> 1));
            dst_top.bottom = (int) (f2 + (flipVBitmapHeight >> 1));
            //拉伸等操作在右下角
            dst_resize.left = (int) (f7 - (resizeBitmapWidth >> 1));
            dst_resize.right = (int) (f7 + (resizeBitmapWidth >> 1));
            dst_resize.top = (int) (f8 - (resizeBitmapHeight >> 1));
            dst_resize.bottom = (int) (f8 + (resizeBitmapHeight >> 1));
            //水平镜像在左下角
            dst_flipV.left = (int) (f5 - (topBitmapWidth >> 1));
            dst_flipV.right = (int) (f5 + (topBitmapWidth >> 1));
            dst_flipV.top = (int) (f6 - (topBitmapHeight >> 1));
            dst_flipV.bottom = (int) (f6 + (topBitmapHeight >> 1));

            canvas.drawLine(f1, f2, f3, f4, mLinePaint);
            canvas.drawLine(f3, f4, f7, f8, mLinePaint);
            canvas.drawLine(f5, f6, f7, f8, mLinePaint);
            canvas.drawLine(f5, f6, f1, f2, mLinePaint);

            canvas.drawBitmap(topBitmap, null, dst_top, null);
            canvas.drawBitmap(deleteBitmap, null, dst_delete, null);
            canvas.drawBitmap(resizeBitmap, null, dst_resize, null);
            canvas.drawBitmap(flipVBitmap, null, dst_flipV, null);
        }
    }

    private float getStartXAlign(String text) {
        if (text == null || text.isEmpty())
            return 0;
        switch (mTextAlign) {
            case TEXT_ALIGN_CENTER:
                return (mBitmap.getWidth() >> 1) - mTextFont.measureText(text) / 2;
            case TEXT_ALIGN_RIGHT:
                return mBitmap.getWidth() - mTextFont.measureText(text);
            default:
                return 0;
        }
    }

    private void setDiagonalLength() {
        if (mBitmap == null) return;
        halfDiagonalLength = Math.hypot(mBitmap.getWidth(), mBitmap.getHeight()) / 2;
    }

    private void initScaleLimit() {
        if (mParentView == null || mBitmap == null) return;

//        float minWidth = mWidthView / 8;
        float minWidth = mWidthView / 8;

        Log.d(TAG, "mParentView w=" + mWidthView);

        if (mBitmap.getWidth() < minWidth) {
            minScale = 1.0f;
        } else {
            minScale = minWidth / mBitmap.getWidth();
        }

        if (mBitmap.getWidth() > mWidthView) {

            maxScale = 1.0f;
        } else {

            maxScale = mWidthView / mBitmap.getWidth();
        }

        Log.d(TAG, "initScaleLimit: min=" + minScale + "_max=" + maxScale);
    }

    public void setBitmap(Bitmap bitmap) {

        mBitmap = recycleBitmap(mBitmap);
        mBitmap = bitmap;
        Log.d(TAG, "bmp: w=" + mBitmap.getWidth() + "_h=" + mBitmap.getHeight());

        int screenWidth = mWidthView;
        int screenHeight = mHeighView;
        Log.d(TAG, "123bmp: w=" + mBitmap.getWidth() + "_h=" + mBitmap.getHeight());
        float dx = (screenWidth-mBitmap.getWidth())>>1;
        float dy = (screenHeight-mBitmap.getHeight())>>1;
        mMatrix.setTranslate(dx, dy);

        mTextCanvas = new Canvas(mBitmap);
        mTextCanvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        setDiagonalLength();
        initScaleLimit();

        mParentView.invalidate();
    }

    public void disableDraw() {
        mBitmap = null;
    }

    public void setTextAlign(int textAlign) {
        mTextAlign = textAlign;
        resizeTextCanvas();
    }

    /**
     * Translate text sticker at position above tablayout.
     *
     * @param heightTabLayout the height of tablayout that is below text sticker.
     */
    public void setTranslateInit(float heightTabLayout) {
//        if (mIsInit) {
//            float[] arrayOfFloat = new float[9];
//            mMatrix.getValues(arrayOfFloat);
//            float f4 = arrayOfFloat[3] * mBitmap.getWidth() + 0.0F * arrayOfFloat[4] + arrayOfFloat[5];
//            float f8 = arrayOfFloat[3] * mBitmap.getWidth() + arrayOfFloat[4] * mBitmap.getHeight() + arrayOfFloat[5];
//
//            float heightTextSticker = (f8 + resizeBitmapHeight / 2) - (f4 - deleteBitmapHeight / 2);
//            mMatrix.setTranslate((mScreenwidth >> 1) - mTextFont.measureText(mText) / 2, mScreenHeight - heightTabLayout - heightTextSticker - (mScreenHeight / 2 - mHeighView / 2));
//            Log.d(TAG, "heightTextSticker=" + heightTextSticker + "_heightTabLayout=" + heightTabLayout + "_mScreenHeight=" + mScreenHeight);
//            mParentView.invalidate();
//            mIsInit = false;
//        }
    }

    public TextPaint getTextFont() {
        return mTextFont;
    }

    public void setTextFont(Typeface fontType) {
        mTextFont.setTypeface(fontType);
        resizeTextCanvas();
    }

    public int getTextPadding() {
        return mTextPadding;
    }

    public void setTextPadding(int textPadding) {
        if (textPadding < TextMoreAdjustFragment.MIN_TEXT_PADDING || textPadding > TextMoreAdjustFragment.MAX_TEXT_PADDING) {
            return;
        }
        mTextPadding = textPadding;
        resizeTextCanvas();
    }

    public void setTextColor(@ColorInt int color) {
        if (mTextFont.getShader() != null)
            mTextFont.setShader(null);
        mTextFont.setColor(color);
        mParentView.invalidate();
    }

    public void setTextPattern(Context context, String bgName) {
        InputStream inputStream = null;
        try {
            inputStream = context.getResources().getAssets().open("bg/" + bgName);
            mPatternBmp = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Shader shader = new BitmapShader(mPatternBmp,
                Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        shader.setLocalMatrix(mMatrix);
        mTextFont.setShader(shader);
        mParentView.invalidate();
    }

    public void setTextPattern(Bitmap bmp) {
        Shader shader = new BitmapShader(bmp,
                Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        shader.setLocalMatrix(mMatrix);
        mTextFont.setShader(shader);
        mParentView.invalidate();
    }

    public float getTextSize() {
        return mTextFont.getTextSize();
    }

    public void setTextSize(int progressSize) {
        mProgressSize = progressSize;
        Log.d(TAG, "setTextSize size=" + progressSize);

//        int widthText = ((int) mTextFont.measureText(mText) + mTextMargin);
//        if ((overlaps(dst_resize, dst_delete)&&size<mTextFont.getTextSize()) || (widthText >= mTextMaxWidth)&&(size>mTextFont.getTextSize())) {
//            Log.d(TAG, "the text size exceeds limit.");
//            return;
//        }

        if (progressSize < TextMoreAdjustFragment.MIN_TEXT_SIZE || progressSize > TextMoreAdjustFragment.MAX_TEXT_SIZE) {
            return;
        }

        mTextFont.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, progressSize, mDisplayMetrics));
        resizeTextCanvas();
    }

    public void release() {

        if (mTextFont != null) {
            mTextFont.reset();
            mTextFont = null;
        }
        if (mTextCanvas != null) {
            mTextCanvas = null;
        }
        if (mPatternBmp != null) {
            mPatternBmp = recycleBitmap(mPatternBmp);
        }
        if (mDisplayMetrics != null) {
            mDisplayMetrics = null;
        }
        if (mLines != null) {
            mLines.clear();
            mLines = null;
        }
    }

    public boolean overlaps(Rect r1, Rect r2) {
        return r1.left < r2.left + r2.width() && r1.left + r1.width() > r2.left
                && r1.top < r2.top + r2.height() && r1.top + r1.height() > r2.top;
    }

    public int getProgressSize() {
        return mProgressSize;
    }

    private void postRotate(float angel) {
        if (mMatrix == null || mid == null || mParentView == null) return;
        initMidPoint();
        mMatrix.postRotate(angel * 1.0f, mid.x, mid.y);
        mParentView.invalidate();
    }

    private void initMidPoint() {
        if (mMatrix == null || mBitmap == null) return;
        float[] arrayOfFloat = new float[9];
        mMatrix.getValues(arrayOfFloat);
        float f7 = arrayOfFloat[0] * this.mBitmap.getWidth() + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2];
        float f8 = arrayOfFloat[3] * this.mBitmap.getWidth() + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5];
        midPointToStartPoint(f7, f8);
    }

    private void midPointToStartPoint(float f7, float f8) {

        float[] arrayOfFloat = new float[9];
        mMatrix.getValues(arrayOfFloat);
        float f1 = 0.0f * arrayOfFloat[0] + 0.0f * arrayOfFloat[1] + arrayOfFloat[2];
        float f2 = 0.0f * arrayOfFloat[3] + 0.0f * arrayOfFloat[4] + arrayOfFloat[5];
        float f3 = f1 + f7;
        float f4 = f2 + f8;
        mid.set(f3 / 2, f4 / 2);
    }

    public void moveUp() {
        postTranslate(MOVE_UP);
    }

    public void moveDown() {
        postTranslate(MOVE_DOWN);
    }

    public void moveLeft() {
        postTranslate(MOVE_LEFT);
    }

    public void moveRight() {
        postTranslate(MOVE_RIGHT);
    }

    public void zoomIn() {
        postScale(STEP_SCALE_IN);
    }

    public void zoomOut() {
        postScale(STEP_SCALE_OUT);
    }

    public void rotate90() {
        postRotate(90);
    }

    public void mirror90() {
        postRotate(-90);
    }

    public void rotate() {
        postRotate(STEP_ROTATE);
    }

    private boolean exceedsBound(int direction, float dx, float dy) {
        if (mParentView == null) return false;
        float rx = dx;
        float ry = dy;
//        int[] l = new int[2];
//        mParentView.getLocationOnScreen(l);
        int x = 0;
        int y = 0;
        int w = mWidthView;
        int h = mHeighView;

        switch (direction) {
            case MOVE_UP:
                if (ry < y)
                    return true;
                break;
            case MOVE_DOWN:
                if (ry > y + h)
                    return true;
                break;
            case MOVE_LEFT:
                if (rx < x)
                    return true;
                break;
            case MOVE_RIGHT:
                if (rx > x + w)
                    return true;
                break;
            default:
                break;
        }
        return false;
    }

    private void postTranslate(int direction) {
        initMidPoint();
        if (mid == null || mMatrix == null || mParentView == null) return;
        if (exceedsBound(direction, mid.x, mid.y)) {
            mNotTranslate = true;
            return;
        }
        mNotTranslate = false;

        switch (direction) {
            case MOVE_UP:
                mMatrix.postTranslate(0, -STEP_MOVE);
                break;
            case MOVE_DOWN:
                mMatrix.postTranslate(0, STEP_MOVE);
                break;
            case MOVE_LEFT:
                mMatrix.postTranslate(-STEP_MOVE, 0);
                break;
            case MOVE_RIGHT:
                mMatrix.postTranslate(STEP_MOVE, 0);
                break;
            default:
                break;
        }
        mParentView.invalidate();
    }

    private float diagonalLength(float f7, float f8) {
        float diagonalLength = (float) Math.hypot(f7 - mid.x, f8 - mid.y);
        return diagonalLength;
    }

    private void postScale(float scale) {
        initMidPoint();

        if (dst_resize == null || mMatrix == null || mParentView == null) return;
        float f7 = (dst_resize.right + dst_resize.left) / 2;
        float f8 = (dst_resize.top + dst_resize.bottom) / 2;
        if (((diagonalLength(f7, f8) /
                halfDiagonalLength <= minScale)) && scale < 1 ||
                (diagonalLength(f7, f8) / halfDiagonalLength >= maxScale) && scale > 1) {
            mNotScale = true;
            return;
        }
        mNotScale = false;
        mMatrix.postScale(scale, scale, mid.x, mid.y);
        mParentView.invalidate();
    }

    public void mirror() {
        postRotate(-STEP_ROTATE);
    }

    /**
     * Helper to setColor(), that only assigns the color's alpha value, leaving its r,g,b values unchanged.
     * Results are undefined if the alpha value is outside of the range [20..255]
     *
     * @param opacity int: set the alpha component [20..255] of the paint's color.
     */
    public void setOpacity(int opacity) {
        if (opacity < MIN_OPACITY_VALUE || opacity > MAX_OPACITY_VALUE)
            return;
        mPaint.setAlpha(opacity);
        mParentView.invalidate();
    }

    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent: BaseStickerModel");
        if (mParentView == null) return false;
        int action = MotionEventCompat.getActionMasked(event);
        boolean handled = true;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "ACTION_DOWN BaseStickerModel");
                if (isInButton(event, dst_top)) {

                    if (listener != null) {
                        listener.onInputTextSticker(mIndex);
                    }
                } else if (isInButton(event, dst_delete)) {

                    deleteSelf();
                    if (listener != null) {
                        listener.onDeleteTextSticker(mIndex);
                    }
                } else if (isInResize(event)) {
                    isInResize = true;
                    lastRotateDegree = rotationToStartPoint(event);
                    updateMiddlePoint(event);
                    lastLength = diagonalLength(event);
                    Log.d(TAG, "isInResize");
                } else if (isInButton(event, dst_flipV)) {

                    //水平镜像
                    PointF localPointF = new PointF();
                    midDiagonalPoint(localPointF);
                    mMatrix.postScale(-1.0F, 1.0F, localPointF.x, localPointF.y);
                    Log.d(TAG, "isInFlip");
                } else if (isInBitmap(event)) {

                    Log.d(TAG, "isInBitmap");
                    isInBitmap = true;
                    lastX = event.getX(0);
                    lastY = event.getY(0);
                    isMovable = false;
                    isPointerDown = false;
                    setInEdit(true);
                } else {

                    Log.d(TAG, "outside");
//                    handled = false;
//                    setInEdit(false);
                }
//                mParentView.invalidate();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if (spacing(event) > POINTER_DISTANCE_MAX) {
                    oldDis = spacing(event);
                    isPointerDown = true;
                    updateMiddlePoint(event);
                } else {
                    isPointerDown = false;
                }
                isInBitmap = false;
                isInResize = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (exceedsBound(event)) {
                    break;
                }

                Log.d(TAG, "ACTION_MOVE BaseStickerModel");
                if (isPointerDown) {
                    float scale;
                    float disNew = spacing(event);
                    if (disNew == 0 || disNew < POINTER_DISTANCE_MAX) {
                        scale = 1;
                    } else {
                        scale = disNew / oldDis;
                        //缩放缓慢
                        scale = (scale - 1) * POINTEER_ZOOM_COEFF + 1;
                    }
                    float scaleTemp = (scale * Math.abs(dst_flipV.left - dst_resize.left)) / mBitmap.getWidth();
                    if (((scaleTemp <= minScale)) && scale < 1 ||
                            (scaleTemp >= maxScale) && scale > 1) {
                        scale = 1;
                    } else {
                        lastLength = diagonalLength(event);
                    }
                    mMatrix.postScale(scale, scale, mid.x, mid.y);
                    mParentView.invalidate();
                } else if (isInResize) {
                    mMatrix.postRotate((rotationToStartPoint(event) - lastRotateDegree) * 2, mid.x, mid.y);
                    lastRotateDegree = rotationToStartPoint(event);

                    scaleValue = diagonalLength(event) / lastLength;

                    if (((diagonalLength(event) / halfDiagonalLength <= minScale)) && scaleValue < 1 ||
                            (diagonalLength(event) / halfDiagonalLength >= maxScale) && scaleValue > 1) {
                        scaleValue = 1;
                        if (!isInResize(event)) {
                            isInResize = false;
                        }
                    } else {
                        lastLength = diagonalLength(event);
                    }

                    mMatrix.postScale(scaleValue, scaleValue, mid.x, mid.y);
                    mParentView.invalidate();
                } else if (isInBitmap) {
                    float x = event.getX(0);
                    float y = event.getY(0);
                    //判断手指抖动距离 加上isMove判断 只要移动过 都是true
                    if (!isMovable && Math.abs(x - lastX) < MOVE_DISTANCE_MAX
                            && Math.abs(y - lastY) < MOVE_DISTANCE_MAX) {
                        isMovable = false;
                    } else {
                        if (!exceedsBound(event)) {
                            isMovable = true;
                            mMatrix.postTranslate(x - lastX, y - lastY);
                            lastX = x;
                            lastY = y;
                        } else {
                            isMovable = false;
                        }
                    }
                    mParentView.invalidate();
                }
//                if (mStickerViewListener != null)
//                    mStickerViewListener.onStickerMoving(mIndex);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "ACTION_UP");
                isInResize = false;
                isInBitmap = false;
                isPointerDown = false;
                isMovable = false;
//                if (mStickerViewListener != null)
//                    mStickerViewListener.onStickerStoped(mIndex);
                mParentView.invalidate();
                break;
        }
        setInEdit(handled);
        return handled;
    }

    private void deleteSelf() {
        Log.d(TAG, "isInDelete");
        Log.d(TAG, "idx=" + mIndex + ": this=" + this);

        int index = mParentView.getListItem().indexOf(TextStickerView.this);
        mParentView.getListItem().remove(index);
    }

    private boolean isInButton(MotionEvent event, Rect rect) {
        if (event == null || rect == null) return false;
        int left = rect.left;
        int right = rect.right;
        int top = rect.top;
        int bottom = rect.bottom;
        return event.getX(0) >= left && event.getX(0) <= right && event.getY(0) >= top && event.getY(0) <= bottom;
    }

    private boolean isInResize(MotionEvent event) {
        if (event == null || this.dst_resize == null) return false;
        int left = -20 + this.dst_resize.left;
        int top = -20 + this.dst_resize.top;
        int right = 20 + this.dst_resize.right;
        int bottom = 20 + this.dst_resize.bottom;
        return event.getX(0) >= left && event.getX(0) <= right && event.getY(0) >= top && event.getY(0) <= bottom;
    }

    private float rotationToStartPoint(MotionEvent event) {

        if (event == null || mMatrix == null) return 0f;
        float[] arrayOfFloat = new float[9];
        mMatrix.getValues(arrayOfFloat);
        float x = 0.0f * arrayOfFloat[0] + 0.0f * arrayOfFloat[1] + arrayOfFloat[2];
        float y = 0.0f * arrayOfFloat[3] + 0.0f * arrayOfFloat[4] + arrayOfFloat[5];
        double arc = Math.atan2(event.getY(0) - y, event.getX(0) - x);

        return (float) Math.toDegrees(arc);
    }

    private void updateMiddlePoint(MotionEvent event) {
        if (mMatrix == null || mid == null) return;
        float[] arrayOfFloat = new float[9];
        mMatrix.getValues(arrayOfFloat);
        float f1 = 0.0f * arrayOfFloat[0] + 0.0f * arrayOfFloat[1] + arrayOfFloat[2];
        float f2 = 0.0f * arrayOfFloat[3] + 0.0f * arrayOfFloat[4] + arrayOfFloat[5];
        float f3 = f1 + event.getX(0);
        float f4 = f2 + event.getY(0);
        mid.set(f3 / 2, f4 / 2);
    }

    private float diagonalLength(MotionEvent event) {
        float diagonalLength = (float) Math.hypot(event.getX(0) - mid.x, event.getY(0) - mid.y);
        return diagonalLength;
    }

    private void midDiagonalPoint(PointF pointF) {
        if (pointF == null || mMatrix == null || this.mBitmap == null) return;
        float[] arrayOfFloat = new float[9];
        mMatrix.getValues(arrayOfFloat);
        float f1 = 0.0F * arrayOfFloat[0] + 0.0F * arrayOfFloat[1] + arrayOfFloat[2];
        float f2 = 0.0F * arrayOfFloat[3] + 0.0F * arrayOfFloat[4] + arrayOfFloat[5];
        float f3 = arrayOfFloat[0] * this.mBitmap.getWidth() + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2];
        float f4 = arrayOfFloat[3] * this.mBitmap.getWidth() + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5];
        float f5 = f1 + f3;
        float f6 = f2 + f4;
        pointF.set(f5 / 2.0F, f6 / 2.0F);
    }

    public boolean isInBitmap(MotionEvent event) {
        if (mMatrix == null || mBitmap == null) return false;
        float[] arrayOfFloat1 = new float[9];
        mMatrix.getValues(arrayOfFloat1);
        //左上角
        float f1 = 0.0F * arrayOfFloat1[0] + 0.0F * arrayOfFloat1[1] + arrayOfFloat1[2];
        float f2 = 0.0F * arrayOfFloat1[3] + 0.0F * arrayOfFloat1[4] + arrayOfFloat1[5];
        //右上角
        float f3 = arrayOfFloat1[0] * this.mBitmap.getWidth() + 0.0F * arrayOfFloat1[1] + arrayOfFloat1[2];
        float f4 = arrayOfFloat1[3] * this.mBitmap.getWidth() + 0.0F * arrayOfFloat1[4] + arrayOfFloat1[5];
        //左下角
        float f5 = 0.0F * arrayOfFloat1[0] + arrayOfFloat1[1] * this.mBitmap.getHeight() + arrayOfFloat1[2];
        float f6 = 0.0F * arrayOfFloat1[3] + arrayOfFloat1[4] * this.mBitmap.getHeight() + arrayOfFloat1[5];
        //右下角
        float f7 = arrayOfFloat1[0] * this.mBitmap.getWidth() + arrayOfFloat1[1] * this.mBitmap.getHeight() + arrayOfFloat1[2];
        float f8 = arrayOfFloat1[3] * this.mBitmap.getWidth() + arrayOfFloat1[4] * this.mBitmap.getHeight() + arrayOfFloat1[5];

        float[] arrayOfFloat2 = new float[4];
        float[] arrayOfFloat3 = new float[4];
        //确定X方向的范围
        arrayOfFloat2[0] = f1;//左上的左
        arrayOfFloat2[1] = f3;//右上的右
        arrayOfFloat2[2] = f7;//右下的右
        arrayOfFloat2[3] = f5;//左下的左
        //确定Y方向的范围
        arrayOfFloat3[0] = f2;//左上的上
        arrayOfFloat3[1] = f4;//右上的上
        arrayOfFloat3[2] = f8;
        arrayOfFloat3[3] = f6;
        return pointInRect(arrayOfFloat2, arrayOfFloat3, event.getX(0), event.getY(0));
    }

    @Override
    public boolean isInEdit() {
        return isInEdit;
    }

    public void setInEdit(boolean isInEdit) {
        this.isInEdit = isInEdit;
    }

    @Override
    public boolean isItemDeleted() {
        return false;
    }

    private boolean pointInRect(float[] xRange, float[] yRange, float x, float y) {
        if (xRange.length < 4 || yRange.length < 4) return false;
        //四条边的长度
        double a1 = Math.hypot(xRange[0] - xRange[1], yRange[0] - yRange[1]);
        double a2 = Math.hypot(xRange[1] - xRange[2], yRange[1] - yRange[2]);
        double a3 = Math.hypot(xRange[3] - xRange[2], yRange[3] - yRange[2]);
        double a4 = Math.hypot(xRange[0] - xRange[3], yRange[0] - yRange[3]);
        //待检测点到四个点的距离
        double b1 = Math.hypot(x - xRange[0], y - yRange[0]);
        double b2 = Math.hypot(x - xRange[1], y - yRange[1]);
        double b3 = Math.hypot(x - xRange[2], y - yRange[2]);
        double b4 = Math.hypot(x - xRange[3], y - yRange[3]);

        double u1 = (a1 + b1 + b2) / 2;
        double u2 = (a2 + b2 + b3) / 2;
        double u3 = (a3 + b3 + b4) / 2;
        double u4 = (a4 + b4 + b1) / 2;

        //矩形的面积
        double s = a1 * a2;
        double ss = Math.sqrt(u1 * (u1 - a1) * (u1 - b1) * (u1 - b2))
                + Math.sqrt(u2 * (u2 - a2) * (u2 - b2) * (u2 - b3))
                + Math.sqrt(u3 * (u3 - a3) * (u3 - b3) * (u3 - b4))
                + Math.sqrt(u4 * (u4 - a4) * (u4 - b4) * (u4 - b1));
        return Math.abs(s - ss) < 0.5;
    }

    private float spacing(MotionEvent event) {
        if (event == null) return 0;
        if (event.getPointerCount() == 2) {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return (float) Math.sqrt(x * x + y * y);
        } else {
            return 0;
        }
    }

    private boolean exceedsBound(MotionEvent event) {
        if (event == null || mParentView == null) return false;
        float rx = event.getRawX();
        float ry = event.getRawY();
        int[] l = new int[2];
        mParentView.getLocationOnScreen(l);
        int x = l[0];
        int y = l[1];
        int w = mWidthView;
        int h = mHeighView;

        if (rx < x || rx > x + w || ry < y || ry > y + h) {
            return true;
        }
        return false;
    }

    public TextStickerView setListener(TextStickerViewListener listener) {
        this.listener = listener;
        return this;
    }

    public interface TextStickerViewListener {

        void onInputTextSticker(int textStickerIndex);
        void onDeleteTextSticker(int textStickerIndex);
    }
}
