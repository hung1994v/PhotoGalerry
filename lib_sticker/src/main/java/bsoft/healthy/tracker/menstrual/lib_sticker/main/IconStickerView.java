package bsoft.healthy.tracker.menstrual.lib_sticker.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import androidx.core.view.MotionEventCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;

import bsoft.healthy.tracker.menstrual.lib_sticker.R;

/**
 * 表情贴纸
 */
public class IconStickerView extends BaseStickerView {
    public static final float INIT_SCALE = 1f;
    private static final float pointerLimitDis = 20f;
    private static final float pointerZoomCoeff = 0.09f;
    private static final float moveLimitDis = 10f;
    private static final float FIT_X_TRANSLATE = 100;
    private static final float FIT_Y_TRANSLATE = 95;
    //手指移动距离必须超过这个数值
    private static final String TAG = IconStickerView.class.getSimpleName();
    public final float MOVE_DISTANCE_MAX = 10f;
    private final long stickerId;
    private final float POINTER_DISTANCE_MAX = 20f;
    private final float POINTEER_ZOOM_COEFF = 0.09f;
    private final float BITMAP_SCALE = 0.5f;
    private Bitmap deleteBitmap;
    private Bitmap flipVBitmap;
    private Bitmap topBitmap;
    private Bitmap resizeBitmap;
    private Bitmap mBitmap;
    private Rect dst_delete;
    private Rect dst_resize;
    private Rect dst_flipV;
    private Rect dst_top;
    private int deleteBitmapWidth;
    private int deleteBitmapHeight;
    private int resizeBitmapWidth;
    private int resizeBitmapHeight;
    //水平镜像
    private int flipVBitmapWidth;
    private int flipVBitmapHeight;
    //置顶
    private int topBitmapWidth;
    //水平镜像
    private int topBitmapHeight;
    private int measuredWidth, measuredHeight;
    private Matrix matrix = new Matrix();
    //    private OnDeleteStickerListenner mDeleteStickerListener;
    private float MAX_SCALE = 1.5f;
    private Paint localPaint;
    private DisplayMetrics dm;
    private boolean isDeleted = false;
    private boolean isInEdit = true;
    private double halfDiagonalLength;
    private float MIN_SCALE = 0.5f;
    private PointF mid = new PointF();
    private float originalWidth = 0;
    private boolean isInResize = false;
    private float lastRotateDegree;
    private float lastLength;
    private boolean isHorizonMirror = false;
    private boolean isInSide;
    private float lastX, lastY;
    private float oldDis;
    private boolean isPointerDown = false;
    private boolean isMove = false;
    private boolean isUp = false;
    private float initScale = INIT_SCALE;
    private float mScreenLayoutWidth = 720.f;
    private Paint mStickerPaint;
    private StickerContainerView mParentView = null;
    private int mHeightView = 0, mWidthView = 0;
    private Context mContext = null;
    private boolean isInBitmap;
    private boolean isMovable;
    private float minScale, maxScale;
    private float scaleValue = 1.0f;

    public IconStickerView(StickerContainerView parentView, int widthView, int heightView) {

        mParentView = parentView;
        mWidthView = widthView;
        mHeightView = heightView;

        mContext = parentView.getContext();

        initScreen();

        stickerId = 0;
        init();
    }

    public static int[] getScreenSize(Context context) {
        int width = context.getResources().getSystem().getDisplayMetrics().widthPixels;
        int height = context.getResources().getSystem().getDisplayMetrics().heightPixels;
        return new int[]{width, height};
    }

//    public int getScreenWidth() {
//        return mScreenWidth;
////    }

    private void initScreen() {
        measuredWidth = getScreenSize(mContext)[0];
        measuredHeight = getScreenSize(mContext)[1];
    }

    @Override
    public void setType() {
        mType = ICON_STICKER;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (canvas == null || isDeleted) return;
        if (mBitmap != null) {
            float[] arrayOfFloat = new float[9];
            matrix.getValues(arrayOfFloat);
            float f1 = 0.0F * arrayOfFloat[0] + 0.0F * arrayOfFloat[1] + arrayOfFloat[2];
            float f2 = 0.0F * arrayOfFloat[3] + 0.0F * arrayOfFloat[4] + arrayOfFloat[5];
            float f3 = arrayOfFloat[0] * this.mBitmap.getWidth() + 0.0F * arrayOfFloat[1] + arrayOfFloat[2];
            float f4 = arrayOfFloat[3] * this.mBitmap.getWidth() + 0.0F * arrayOfFloat[4] + arrayOfFloat[5];
            float f5 = 0.0F * arrayOfFloat[0] + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2];
            float f6 = 0.0F * arrayOfFloat[3] + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5];
            float f7 = arrayOfFloat[0] * this.mBitmap.getWidth() + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2];
            float f8 = arrayOfFloat[3] * this.mBitmap.getWidth() + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5];

            canvas.save();
            canvas.drawBitmap(mBitmap, matrix, mStickerPaint);

            //删除在右上角
            dst_delete.left = (int) (f3 - deleteBitmapWidth / 2);
            dst_delete.right = (int) (f3 + deleteBitmapWidth / 2);
            dst_delete.top = (int) (f4 - deleteBitmapHeight / 2);
            dst_delete.bottom = (int) (f4 + deleteBitmapHeight / 2);
            //拉伸等操作在右下角
            dst_resize.left = (int) (f7 - resizeBitmapWidth / 2);
            dst_resize.right = (int) (f7 + resizeBitmapWidth / 2);
            dst_resize.top = (int) (f8 - resizeBitmapHeight / 2);
            dst_resize.bottom = (int) (f8 + resizeBitmapHeight / 2);
            //垂直镜像在左上角
            dst_top.left = (int) (f1 - flipVBitmapWidth / 2);
            dst_top.right = (int) (f1 + flipVBitmapWidth / 2);
            dst_top.top = (int) (f2 - flipVBitmapHeight / 2);
            dst_top.bottom = (int) (f2 + flipVBitmapHeight / 2);
            //水平镜像在左下角
            dst_flipV.left = (int) (f5 - topBitmapWidth / 2);
            dst_flipV.right = (int) (f5 + topBitmapWidth / 2);
            dst_flipV.top = (int) (f6 - topBitmapHeight / 2);
            dst_flipV.bottom = (int) (f6 + topBitmapHeight / 2);
            if (isInEdit) {

                canvas.drawLine(f1, f2, f3, f4, localPaint);
                canvas.drawLine(f3, f4, f7, f8, localPaint);
                canvas.drawLine(f5, f6, f7, f8, localPaint);
                canvas.drawLine(f5, f6, f1, f2, localPaint);

                canvas.drawBitmap(deleteBitmap, null, dst_delete, null);
                canvas.drawBitmap(resizeBitmap, null, dst_resize, null);
                canvas.drawBitmap(flipVBitmap, null, dst_flipV, null);
//                canvas.drawBitmap(topBitmap, null, dst_top, null);
            }

            canvas.restore();
        }
    }

    private void updateMiddlePoint(MotionEvent event) {
        if (matrix == null || mid == null) return;
        float[] arrayOfFloat = new float[9];
        matrix.getValues(arrayOfFloat);
        float f1 = 0.0f * arrayOfFloat[0] + 0.0f * arrayOfFloat[1] + arrayOfFloat[2];
        float f2 = 0.0f * arrayOfFloat[3] + 0.0f * arrayOfFloat[4] + arrayOfFloat[5];
        float f3 = f1 + event.getX(0);
        float f4 = f2 + event.getY(0);
        mid.set(f3 / 2, f4 / 2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isInEdit || isDeleted) {
            return false;
        }
        int action = MotionEventCompat.getActionMasked(event);
        boolean handled = true;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (isInButton(event, dst_delete)) {

                    deleteSelf();
                    //   return false;
                } else if (isInResize(event)) {

                    isInResize = true;
                    lastRotateDegree = rotationToStartPoint(event);
                    midPointToStartPoint(event);
                    lastLength = diagonalLength(event);
                } else if (isInButton(event, dst_flipV)) {
//                    Flog.i("dst_flipV");
                    //水平镜像
                    PointF localPointF = new PointF();
                    midDiagonalPoint(localPointF);
                    matrix.postScale(-1.0F, 1.0F, localPointF.x, localPointF.y);
                    isHorizonMirror = !isHorizonMirror;
                } else if (isInBitmap(event)) {
//                    Flog.i("InSide");

                    isInSide = true;
                    lastX = event.getX(0);
                    lastY = event.getY(0);
                } else {
                    handled = false;

                    mParentView.invalidate();
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:

                if (spacing(event) > pointerLimitDis) {
                    oldDis = spacing(event);
                    isPointerDown = true;
                    midPointToStartPoint(event);
                } else {
                    isPointerDown = false;
                }
                isInSide = false;
                isInResize = false;
                break;
            case MotionEvent.ACTION_MOVE:
//                Log.d(TAG, "ACTION_MOVE bubble text");

                if (isPointerDown) {
                    float scale;
                    float disNew = spacing(event);
                    if (disNew == 0 || disNew < pointerLimitDis) {
                        scale = 1;
                    } else {
                        scale = disNew / oldDis;
                        //缩放缓慢
                        scale = (scale - 1) * pointerZoomCoeff + 1;
                    }
                    float scaleTemp = (scale * Math.abs(dst_flipV.left - dst_resize.left)) / originalWidth;
                    if (((scaleTemp <= MIN_SCALE)) && scale < 1 ||
                            (scaleTemp >= MAX_SCALE) && scale > 1) {
                        scale = 1;
                    } else {
                        lastLength = diagonalLength(event);
                    }
                    matrix.postScale(scale, scale, mid.x, mid.y);
                    mParentView.invalidate();
                } else if (isInResize) {
                    matrix.postRotate((rotationToStartPoint(event) - lastRotateDegree) * 2, mid.x, mid.y);
                    lastRotateDegree = rotationToStartPoint(event);

                    float scale = diagonalLength(event) / lastLength;

                    if (((diagonalLength(event) / halfDiagonalLength <= MIN_SCALE)) && scale < 1 ||
                            (diagonalLength(event) / halfDiagonalLength >= MAX_SCALE) && scale > 1) {
                        scale = 1;
                        if (!isInResize(event)) {
                            isInResize = false;
                        }
                    } else {
                        lastLength = diagonalLength(event);
                    }
                    matrix.postScale(scale, scale, mid.x, mid.y);

                    mParentView.invalidate();
                } else if (isInSide) {
                    float x = event.getX(0);
                    float y = event.getY(0);
                    //判断手指抖动距离 加上isMove判断 只要移动过 都是true
                    Log.d(TAG, "disX = " + (x - lastX) + " disY = " + (y - lastY));
                    if (!isMove && Math.abs(x - lastX) < moveLimitDis
                            && Math.abs(y - lastY) < moveLimitDis) {
                        isMove = false;
                    } else {
                        if (!exceedsBound(event)) {
                            isMove = true;
                            matrix.postTranslate(x - lastX, y - lastY);
                            lastX = x;
                            lastY = y;
                        } else {
                            isMove = false;
                        }
                    }
                    mParentView.invalidate();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:

                isInResize = false;
                isInSide = false;
                isPointerDown = false;
                isUp = true;
                isMove = false;
                mParentView.invalidate();
                break;

        }
//        if (handled) {
//            setInEdit(true);
//        }

        return handled;
    }

    private boolean isInButton(MotionEvent event, Rect rect) {
        int left = rect.left;
        int right = rect.right;
        int top = rect.top;
        int bottom = rect.bottom;
        return event.getX(0) >= left && event.getX(0) <= right && event.getY(0) >= top && event.getY(0) <= bottom;
    }

    private void deleteSelf() {
        Log.d(TAG, "isInDelete");

        int index = mParentView.getListItem().indexOf(IconStickerView.this);
        mParentView.getListItem().remove(index);
    }

    @Override
    public boolean isInEdit() {
        return isInEdit;
    }

    @Override
    public void setInEdit(boolean isInEdit) {
        this.isInEdit = isInEdit;
    }

    @Override
    public boolean isItemDeleted() {
        return isDeleted;
    }

    public boolean isInBitmap(MotionEvent event) {
        float[] arrayOfFloat1 = new float[9];
        this.matrix.getValues(arrayOfFloat1);
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

    private boolean pointInRect(float[] xRange, float[] yRange, float x, float y) {
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

    @Override
    public void release() {
        Bitmap[] list = new Bitmap[]{deleteBitmap, flipVBitmap, topBitmap, resizeBitmap, mBitmap,};
        for (Bitmap item : list) {
            if (item != null) item.recycle();
        }
    }

    public void setTranslateInit(int h) {
        if (measuredHeight <= 0 || measuredWidth <= 0)
            return;
        float[] arrayOfFloat = new float[9];
        matrix.getValues(arrayOfFloat);
        float f4 = arrayOfFloat[3] * mBitmap.getWidth() + 0.0F * arrayOfFloat[4] + arrayOfFloat[5];
        float f8 = arrayOfFloat[3] * mBitmap.getWidth() + arrayOfFloat[4] * mBitmap.getHeight() + arrayOfFloat[5];

        float heightTextSticker = (f8 + resizeBitmapHeight / 2) - (f4 - deleteBitmapHeight / 2);
        matrix.setTranslate(measuredWidth / 2 - mBitmap.getWidth() / 2, h / 2 - mBitmap.getHeight() / 2);
        matrix.postScale(0.5f, 0.5f, measuredWidth / 2, h / 2);
        mParentView.invalidate();

    }

    private void initMiddlePoint() {
        float[] arrayOfFloat = new float[9];
        matrix.getValues(arrayOfFloat);
        float px = arrayOfFloat[0] * this.mBitmap.getWidth() + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2];
        float py = arrayOfFloat[3] * this.mBitmap.getWidth() + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5];
        midPointToStartPoint(px, py);
    }

    private float diagonalLength(float rx, float ry) {
        float diagonalLength = (float) Math.hypot(rx - mid.x, ry - mid.y);
        return diagonalLength;
    }

    private float diagonalLength(MotionEvent event) {
        float diagonalLength = (float) Math.hypot(event.getX(0) - mid.x, event.getY(0) - mid.y);
        return diagonalLength;
    }

    private void init() {
        MAX_SCALE = 1.2f;
        dst_delete = new Rect();
        dst_resize = new Rect();
        dst_flipV = new Rect();
        dst_top = new Rect();
        localPaint = new Paint();
        localPaint.setColor(Color.parseColor("#544EEE"));
        localPaint.setAntiAlias(true);
        localPaint.setDither(true);
        localPaint.setStyle(Paint.Style.STROKE);
        localPaint.setStrokeWidth(2.0f);
        mStickerPaint = new Paint();
        dm = mContext.getResources().getDisplayMetrics();
    }

    public void draw(Canvas canvas) {
        if (canvas == null || isDeleted) return;
        if (mBitmap != null) {
            float[] arrayOfFloat = new float[9];
            matrix.getValues(arrayOfFloat);
            float f1 = 0.0F * arrayOfFloat[0] + 0.0F * arrayOfFloat[1] + arrayOfFloat[2];
            float f2 = 0.0F * arrayOfFloat[3] + 0.0F * arrayOfFloat[4] + arrayOfFloat[5];
            float f3 = arrayOfFloat[0] * this.mBitmap.getWidth() + 0.0F * arrayOfFloat[1] + arrayOfFloat[2];
            float f4 = arrayOfFloat[3] * this.mBitmap.getWidth() + 0.0F * arrayOfFloat[4] + arrayOfFloat[5];
            float f5 = 0.0F * arrayOfFloat[0] + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2];
            float f6 = 0.0F * arrayOfFloat[3] + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5];
            float f7 = arrayOfFloat[0] * this.mBitmap.getWidth() + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2];
            float f8 = arrayOfFloat[3] * this.mBitmap.getWidth() + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5];

            canvas.save();
            canvas.drawBitmap(mBitmap, matrix, mStickerPaint);

            //删除在右上角
            dst_delete.left = (int) (f3 - deleteBitmapWidth / 2);
            dst_delete.right = (int) (f3 + deleteBitmapWidth / 2);
            dst_delete.top = (int) (f4 - deleteBitmapHeight / 2);
            dst_delete.bottom = (int) (f4 + deleteBitmapHeight / 2);
            //拉伸等操作在右下角
            dst_resize.left = (int) (f7 - resizeBitmapWidth / 2);
            dst_resize.right = (int) (f7 + resizeBitmapWidth / 2);
            dst_resize.top = (int) (f8 - resizeBitmapHeight / 2);
            dst_resize.bottom = (int) (f8 + resizeBitmapHeight / 2);
            //垂直镜像在左上角
            dst_top.left = (int) (f1 - flipVBitmapWidth / 2);
            dst_top.right = (int) (f1 + flipVBitmapWidth / 2);
            dst_top.top = (int) (f2 - flipVBitmapHeight / 2);
            dst_top.bottom = (int) (f2 + flipVBitmapHeight / 2);
            //水平镜像在左下角
            dst_flipV.left = (int) (f5 - topBitmapWidth / 2);
            dst_flipV.right = (int) (f5 + topBitmapWidth / 2);
            dst_flipV.top = (int) (f6 - topBitmapHeight / 2);
            dst_flipV.bottom = (int) (f6 + topBitmapHeight / 2);
            if (isInEdit) {

                canvas.drawLine(f1, f2, f3, f4, localPaint);
                canvas.drawLine(f3, f4, f7, f8, localPaint);
                canvas.drawLine(f5, f6, f7, f8, localPaint);
                canvas.drawLine(f5, f6, f1, f2, localPaint);

                canvas.drawBitmap(deleteBitmap, null, dst_delete, null);
                canvas.drawBitmap(resizeBitmap, null, dst_resize, null);
                canvas.drawBitmap(flipVBitmap, null, dst_flipV, null);
//                canvas.drawBitmap(topBitmap, null, dst_top, null);
            }

            canvas.restore();
        }
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    public void setImageResource(int resId) {
        setBitmap(BitmapFactory.decodeResource(mContext.getResources(), resId));
    }

    public void setScreenwidth(int screenwidth, float ratio) {
//        mScreenWidth = screenwidth;
//        mScreenHeight = (int) (screenwidth * ratio);
    }

    public void setSavedMatrix() {
        float raio = mParentView.getWidth() / mScreenLayoutWidth;
        int w = mBitmap.getWidth();
        int h = mBitmap.getHeight();
        matrix.postScale(initScale * raio, initScale * raio, w / 2f, h / 2f);
        //Y坐标为 （顶部操作栏+正方形图）/2
        Matrix concatMatrix = new Matrix();
        concatMatrix.setTranslate(FIT_X_TRANSLATE, FIT_Y_TRANSLATE);
        matrix.postConcat(concatMatrix);
    }

    public void postScale(float scale) {
        initMiddlePoint();
        float[] arrayOfFloat = new float[9];
        matrix.getValues(arrayOfFloat);
        float px = arrayOfFloat[0] * this.mBitmap.getWidth() + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2];
        float py = arrayOfFloat[3] * this.mBitmap.getWidth() + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5];


        if (((diagonalLength(px, py) / halfDiagonalLength <= MIN_SCALE)) && scale < 1 ||
                (diagonalLength(px, py) / halfDiagonalLength >= MAX_SCALE) && scale > 1) {
            return;
        }

        matrix.postScale(scale, scale, mid.x, mid.y);
    }

    public void setInitScale(float initScale) {
        this.initScale = initScale;
    }

    private void setDiagonalLength() {
        halfDiagonalLength = Math.hypot(mBitmap.getWidth(), mBitmap.getHeight()) / 2;
    }

    private void initBitmaps() {
        //当图片的宽比高大时 按照宽计算 缩放大小根据图片的大小而改变 最小为图片的1/8 最大为屏幕宽
        if (mBitmap.getWidth() >= mBitmap.getHeight()) {
            float minWidth = mParentView.getWidth() / 8;
            if (mBitmap.getWidth() < minWidth) {
                MIN_SCALE = 1f;
            } else {
                MIN_SCALE = 1.0f * minWidth / mBitmap.getWidth();
            }

            if (mBitmap.getWidth() > mParentView.getWidth()) {
                MAX_SCALE = 1;
            } else {
                MAX_SCALE = 1.0f * mParentView.getWidth() / mBitmap.getWidth();
            }
        } else {
            //当图片高比宽大时，按照图片的高计算
            float minHeight = mParentView.getWidth() / 8;
            if (mBitmap.getHeight() < minHeight) {
                MIN_SCALE = 1f;
            } else {
                MIN_SCALE = 1.0f * minHeight / mBitmap.getHeight();
            }

            if (mBitmap.getHeight() > mParentView.getWidth()) {
                MAX_SCALE = 1;
            } else {
                MAX_SCALE = 1.0f * mParentView.getWidth() / mBitmap.getHeight();
            }
        }

        if (topBitmap == null)
            topBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_edit);
        if (deleteBitmap == null)
            deleteBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_delete);
        if (flipVBitmap == null)
            flipVBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_flip);
        if (resizeBitmap == null)
            resizeBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_resize);


        deleteBitmapWidth = (int) (deleteBitmap.getWidth() * BITMAP_SCALE);
        deleteBitmapHeight = (int) (deleteBitmap.getHeight() * BITMAP_SCALE);

        resizeBitmapWidth = (int) (resizeBitmap.getWidth() * BITMAP_SCALE);
        resizeBitmapHeight = (int) (resizeBitmap.getHeight() * BITMAP_SCALE);

        flipVBitmapWidth = (int) (flipVBitmap.getWidth() * BITMAP_SCALE);
        flipVBitmapHeight = (int) (flipVBitmap.getHeight() * BITMAP_SCALE);

        topBitmapWidth = (int) (topBitmap.getWidth() * BITMAP_SCALE);
        topBitmapHeight = (int) (topBitmap.getHeight() * BITMAP_SCALE);
    }

    private void midPointToStartPoint(float px, float py) {
        float[] arrayOfFloat = new float[9];
        matrix.getValues(arrayOfFloat);
        float f1 = 0.0f * arrayOfFloat[0] + 0.0f * arrayOfFloat[1] + arrayOfFloat[2];
        float f2 = 0.0f * arrayOfFloat[3] + 0.0f * arrayOfFloat[4] + arrayOfFloat[5];
        float f3 = f1 + px;
        float f4 = f2 + py;
        mid.set(f3 / 2, f4 / 2);
    }

    public void setBitmap(Bitmap bitmap) {
        matrix.reset();

        mBitmap = bitmap;

        int screenWidth = mWidthView;
        int screenHeight = mHeightView;
        float dx = (screenWidth-mBitmap.getWidth())>>1;
        float dy = (screenHeight-mBitmap.getHeight())>>1;
        matrix.setTranslate(dx, dy);

        setDiagonalLength();
        initBitmaps();
        int w = mBitmap.getWidth();
        int h = mBitmap.getHeight();
        originalWidth = w;
    }

    public void printTest() {
        float[] arrayOfFloat1 = new float[9];
        this.matrix.getValues(arrayOfFloat1);
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
//        Flog.i("f1=" + f1 + "_f2=" + f2 + "_f3=" + f3 + "_f4=" + f4 + "_f5=" + f5 + "_f6=" + f6 + "_f7=" + f7 + "_f8=" + f8);
    }

    public void setScreenLayoutWidth(float layoutWidth) {
        mScreenLayoutWidth = layoutWidth;
    }

    public void setStickerPaint(int opacity) {
        mStickerPaint.setAlpha(opacity);
    }

    public int getOpacity() {
        return mStickerPaint.getAlpha();
    }

    private boolean isInResize(MotionEvent event) {
        int left = -20 + this.dst_resize.left;
        int top = -20 + this.dst_resize.top;
        int right = 20 + this.dst_resize.right;
        int bottom = 20 + this.dst_resize.bottom;
        return event.getX(0) >= left && event.getX(0) <= right && event.getY(0) >= top && event.getY(0) <= bottom;
    }

    private float rotationToStartPoint(MotionEvent event) {

        float[] arrayOfFloat = new float[9];
        matrix.getValues(arrayOfFloat);
        float x = 0.0f * arrayOfFloat[0] + 0.0f * arrayOfFloat[1] + arrayOfFloat[2];
        float y = 0.0f * arrayOfFloat[3] + 0.0f * arrayOfFloat[4] + arrayOfFloat[5];
        double arc = Math.atan2(event.getY(0) - y, event.getX(0) - x);

        return (float) Math.toDegrees(arc);
    }

    private void midPointToStartPoint(MotionEvent event) {
        float[] arrayOfFloat = new float[9];
        matrix.getValues(arrayOfFloat);
        float f1 = 0.0f * arrayOfFloat[0] + 0.0f * arrayOfFloat[1] + arrayOfFloat[2];
        float f2 = 0.0f * arrayOfFloat[3] + 0.0f * arrayOfFloat[4] + arrayOfFloat[5];
        float f3 = f1 + event.getX(0);
        float f4 = f2 + event.getY(0);
        mid.set(f3 / 2, f4 / 2);
    }

    private void midDiagonalPoint(PointF paramPointF) {
        float[] arrayOfFloat = new float[9];
        this.matrix.getValues(arrayOfFloat);
        float f1 = 0.0F * arrayOfFloat[0] + 0.0F * arrayOfFloat[1] + arrayOfFloat[2];
        float f2 = 0.0F * arrayOfFloat[3] + 0.0F * arrayOfFloat[4] + arrayOfFloat[5];
        float f3 = arrayOfFloat[0] * this.mBitmap.getWidth() + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2];
        float f4 = arrayOfFloat[3] * this.mBitmap.getWidth() + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5];
        float f5 = f1 + f3;
        float f6 = f2 + f4;
        paramPointF.set(f5 / 2.0F, f6 / 2.0F);
    }

    private float spacing(MotionEvent event) {
        if (event.getPointerCount() == 2) {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return (float) Math.sqrt(x * x + y * y);
        } else {
            return 0;
        }
    }

    private boolean checkOverBounds(MotionEvent event) {
        float rx = event.getRawX();
        float ry = event.getRawY();
        int x = 0;
        int y = 0;
        int w = mWidthView;
        int h = mHeightView;

        if (rx < x || rx > x + w || ry < y || ry > y + h) {
            return false;
        }
        return true;
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
        int h = mHeightView;

        if (rx < x || rx > x + w || ry < y || ry > y + h) {
            return true;
        }
        return false;
    }

    public void setIsDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
