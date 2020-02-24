package com.photo.gallery.ui.options.splash;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import bsoft.com.lib_filter.filter.gpu.GPUFilter;
import bsoft.com.lib_filter.filter.gpu.core.GPUImage;
import bsoft.com.lib_filter.filter.gpu.father.GPUImageFilter;
import bsoft.com.lib_filter.filter.gpu.normal.GPUImagePixelationFilter;
import bsoft.com.lib_filter.filter.gpu.normal.GPUImagePolkaDotFilter;
import bsoft.com.lib_filter.filter.gpu.util.BitmapUtil;
import bsoft.com.lib_filter.filter.listener.OnPostFilteredListener;


/**
 * Created by Adm on 2/9/2017.
 */

public class SplashShapeView extends ReDrawView {
    public static final int DRAG = 1;
    public static final int JUMP = 2;
    public static final int NONE = 0;
    protected static final int ZOOM = 3;
    private static final float TOUCH_TOLERANCE = 4.0f;
    private static final String TAG = SplashShapeView.class.getSimpleName();
    protected PointF mCurPoint;
    protected PointF mMid;
    protected PointF mStart;
    protected int mode;
    protected float oldDegree;
    protected float oldDist;
    private DrawHandle bwDrawHandle;
    private int curShapeMode;
    private Bitmap filterBitmap;
    private DrawHandle filterDrawHandle;
    private ColorMatrixColorFilter grayColorFilter;
    private Bitmap mImageBitmap;
    private Matrix mImageMatrix;
    private float mImageScale;
    private Matrix mMaskMatrix;
    private Path mPath;
    private Bitmap mSplashFrame;
    private boolean mSplashInverse;
    private Bitmap mSplashMask;
    private SplashType mSplashType;
    private float mX;
    private float mY;
    private StyleMode myStyleMode;
    private GPUImageFilter mFilter;
    private Bitmap result;
    private GPUImage gpuImage;


    public SplashShapeView(Context context) {
        this(context, null);
    }

    public SplashShapeView(Context context, AttributeSet attrs) {
        this(context, attrs, NONE);
    }

    public SplashShapeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        myStyleMode = StyleMode.B_W;
        mSplashType = SplashType.shape;
        curShapeMode = NONE;
        mImageScale = 1.0f;
        mSplashInverse = false;
        bwDrawHandle = new DrawHandle() {
            public void drawView(Canvas canvas) {
                int sc = 0;
                if (!(mImageBitmap == null || mImageBitmap.isRecycled())) {
                    if (!mSplashInverse) {
                        mPaint.setColorFilter(grayColorFilter);
                    }
                    canvas.drawBitmap(mImageBitmap, mImageMatrix, mPaint);
                    mPaint.setColorFilter(null);
                }
                if (mSplashType == SplashType.shape) {
                    sc = canvas.saveLayer(0.0f, 0.0f, (float) canvasWidth, (float) canvasHeight, null, Canvas.ALL_SAVE_FLAG);
                    if (!(mSplashMask == null || mSplashMask.isRecycled())) {
                        canvas.drawBitmap(mSplashMask, mMaskMatrix, mPaint);
                    }
                    if (!(mImageBitmap == null || mImageBitmap.isRecycled())) {
                        mPaint.setXfermode(srcInXfermode);
                        if (mSplashInverse) {
                            mPaint.setColorFilter(grayColorFilter);
                        }
                        canvas.drawBitmap(mImageBitmap, mImageMatrix, mPaint);
                        mPaint.setColorFilter(null);
                        mPaint.setXfermode(null);
                    }
                    canvas.restoreToCount(sc);
                    if (!(mSplashFrame == null || mSplashFrame.isRecycled())) {
                        canvas.drawBitmap(mSplashFrame, mMaskMatrix, mPaint);
                    }
                }
                if (mSplashType == SplashType.touch) {

                    sc = canvas.saveLayer(0.0f, 0.0f, (float) canvas.getWidth(), (float) canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);
                    canvas.drawPath(mPath, mPaint);
                    mPaint.setXfermode(srcInXfermode);
                    if (mSplashInverse) {
                        mPaint.setColorFilter(grayColorFilter);
                    }
                    canvas.drawBitmap(mImageBitmap, mImageMatrix, mPaint);
                    mPaint.setColorFilter(null);
                    mPaint.setXfermode(null);
                    canvas.restoreToCount(sc);
                }
            }

            public void drawImage(Canvas canvas) {
                int sc = 0;
                int imageCanvasWidth = canvas.getWidth();
                int imageCanvasHeight = canvas.getHeight();
                float viewScale = ((float) imageCanvasWidth) / ((float) canvasWidth);
                Matrix srcImageMatrix = new Matrix();
                srcImageMatrix.set(mImageMatrix);
                Matrix maskImageMatrix = new Matrix();
                maskImageMatrix.set(mMaskMatrix);
                srcImageMatrix.postScale(viewScale, viewScale);
                maskImageMatrix.postScale(viewScale, viewScale);
                if (!(mImageBitmap == null || mImageBitmap.isRecycled())) {
                    if (!mSplashInverse) {
                        mPaint.setColorFilter(grayColorFilter);
                    }
                    canvas.drawBitmap(mImageBitmap, srcImageMatrix, mPaint);
                    mPaint.setColorFilter(null);
                }
                if (mSplashType == SplashType.shape) {
                    sc = canvas.saveLayer(0.0f, 0.0f, (float) imageCanvasWidth, (float) imageCanvasHeight, null, Canvas.ALL_SAVE_FLAG);
                    if (!(mSplashMask == null || mSplashMask.isRecycled())) {
                        canvas.drawBitmap(mSplashMask, maskImageMatrix, mPaint);
                    }
                    if (!(mImageBitmap == null || mImageBitmap.isRecycled())) {
                        mPaint.setXfermode(srcInXfermode);
                        if (mSplashInverse) {
                            mPaint.setColorFilter(grayColorFilter);
                        }
                        canvas.drawBitmap(mImageBitmap, srcImageMatrix, mPaint);
                        mPaint.setColorFilter(null);
                        mPaint.setXfermode(null);
                    }
                    canvas.restoreToCount(sc);
                    if (!(mSplashFrame == null || mSplashFrame.isRecycled())) {
                        canvas.drawBitmap(mSplashFrame, maskImageMatrix, mPaint);
                    }
                }
                if (mSplashType == SplashType.touch) {
                    sc = canvas.saveLayer(0.0f, 0.0f, (float) canvas.getWidth(), (float) canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);
                    canvas.drawPath(mPath, mPaint);
                    mPaint.setXfermode(srcInXfermode);
                    if (mSplashInverse) {
                        mPaint.setColorFilter(grayColorFilter);
                    }
                    canvas.drawBitmap(mImageBitmap, mImageMatrix, mPaint);
                    mPaint.setColorFilter(null);
                    mPaint.setXfermode(null);
                    canvas.restoreToCount(sc);
                }
            }
        };
        filterDrawHandle = new DrawHandle() {
            public void drawView(Canvas canvas) {
                int sc = 0;
                if (mSplashInverse) {
                    if (!(mImageBitmap == null || mImageBitmap.isRecycled())) {
                        canvas.drawBitmap(mImageBitmap, mImageMatrix, mPaint);
                        mPaint.setColorFilter(null);
                    }
                } else if (!(filterBitmap == null || filterBitmap.isRecycled())) {
                    canvas.drawBitmap(filterBitmap, mImageMatrix, mPaint);
                    mPaint.setColorFilter(null);
                }
                if (mSplashType == SplashType.shape) {
                    sc = canvas.saveLayer(0.0f, 0.0f, (float) canvasWidth, (float) canvasHeight, null, Canvas.ALL_SAVE_FLAG);
                    if (!(mSplashMask == null || mSplashMask.isRecycled())) {
                        canvas.drawBitmap(mSplashMask, mMaskMatrix, mPaint);
                    }
                    if (mSplashInverse) {
                        if (!(filterBitmap == null || filterBitmap.isRecycled())) {
                            mPaint.setXfermode(srcInXfermode);
                            canvas.drawBitmap(filterBitmap, mImageMatrix, mPaint);
                            mPaint.setColorFilter(null);
                            mPaint.setXfermode(null);
                        }
                    } else if (!(mImageBitmap == null || mImageBitmap.isRecycled())) {
                        mPaint.setXfermode(srcInXfermode);
                        canvas.drawBitmap(mImageBitmap, mImageMatrix, mPaint);
                        mPaint.setColorFilter(null);
                        mPaint.setXfermode(null);
                    }
                    canvas.restoreToCount(sc);
                    if (!(mSplashFrame == null || mSplashFrame.isRecycled())) {
                        canvas.drawBitmap(mSplashFrame, mMaskMatrix, mPaint);
                    }
                }
                if (mSplashType == SplashType.touch) {
                    sc = canvas.saveLayer(0.0f, 0.0f, (float) canvas.getWidth(), (float) canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);
                    canvas.drawPath(mPath, mPaint);
                    mPaint.setXfermode(srcInXfermode);
                    if (mSplashInverse) {
                        mPaint.setColorFilter(grayColorFilter);
                    }
                    canvas.drawBitmap(mImageBitmap, mImageMatrix, mPaint);
                    mPaint.setColorFilter(null);
                    mPaint.setXfermode(null);
                    canvas.restoreToCount(sc);
                }
            }

            public void drawImage(Canvas canvas) {
                int sc = 0;
                int imageCanvasWidth = canvas.getWidth();
                int imageCanvasHeight = canvas.getHeight();
                float viewScale = ((float) imageCanvasWidth) / ((float) canvasWidth);

                Matrix srcImageMatrix = new Matrix();
                srcImageMatrix.set(mImageMatrix);
                Matrix maskImageMatrix = new Matrix();
                maskImageMatrix.set(mMaskMatrix);
                srcImageMatrix.postScale(viewScale, viewScale);
                maskImageMatrix.postScale(viewScale, viewScale);

                if (mSplashInverse) {
                    if (!(mImageBitmap == null || mImageBitmap.isRecycled())) {
                        canvas.drawBitmap(mImageBitmap, srcImageMatrix, mPaint);
                        mPaint.setColorFilter(null);
                    }
                } else if (!(filterBitmap == null || filterBitmap.isRecycled())) {
                    canvas.drawBitmap(filterBitmap, srcImageMatrix, mPaint);
                    mPaint.setColorFilter(null);
                }
                if (mSplashType == SplashType.shape) {
                    sc = canvas.saveLayer(0.0f, 0.0f, (float) imageCanvasWidth, (float) imageCanvasHeight, null, Canvas.ALL_SAVE_FLAG);
                    if (!(mSplashMask == null || mSplashMask.isRecycled())) {
                        canvas.drawBitmap(mSplashMask, maskImageMatrix, mPaint);
                    }
                    if (mSplashInverse) {
                        if (!(filterBitmap == null || filterBitmap.isRecycled())) {
                            mPaint.setXfermode(srcInXfermode);
                            canvas.drawBitmap(filterBitmap, srcImageMatrix, mPaint);
                            mPaint.setColorFilter(null);
                            mPaint.setXfermode(null);
                        }
                    } else if (!(mImageBitmap == null || mImageBitmap.isRecycled())) {
                        mPaint.setXfermode(srcInXfermode);
                        canvas.drawBitmap(mImageBitmap, srcImageMatrix, mPaint);
                        mPaint.setColorFilter(null);
                        mPaint.setXfermode(null);
                    }
                    canvas.restoreToCount(sc);
                    if (!(mSplashFrame == null || mSplashFrame.isRecycled())) {
                        canvas.drawBitmap(mSplashFrame, maskImageMatrix, mPaint);
                    }
                }
                if (mSplashType == SplashType.touch) {
                    sc = canvas.saveLayer(0.0f, 0.0f, (float) canvas.getWidth(), (float) canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);
                    canvas.drawPath(mPath, mPaint);
                    mPaint.setXfermode(srcInXfermode);
                    if (mSplashInverse) {
                        mPaint.setColorFilter(grayColorFilter);
                    }
                    canvas.drawBitmap(mImageBitmap, mImageMatrix, mPaint);
                    mPaint.setColorFilter(null);
                    mPaint.setXfermode(null);
                    canvas.restoreToCount(sc);
                }
            }
        };
        mode = NONE;
        mStart = new PointF();
        mMid = new PointF();
        mCurPoint = new PointF();
        mPath = new Path();
        initColorMatrix();
    }

    private void initColorMatrix() {
        ColorMatrix grayColorMatrix = new ColorMatrix();
        grayColorMatrix.setSaturation(0.0f);
        grayColorFilter = new ColorMatrixColorFilter(grayColorMatrix);
    }

    public void setSplashType(SplashType type) {
        mSplashType = type;
        if (type == SplashType.touch) {
            mPaint.setStyle(Style.STROKE);
            mPaint.setStrokeJoin(Join.ROUND);
            mPaint.setStrokeCap(Cap.ROUND);
            mPaint.setStrokeWidth(12.0f);
        }
    }

    public void loadSplashShape(int mode) {
        if (mode == curShapeMode) {
            setSplashInverse();
            return;
        }
        if (!(mSplashMask == null || mSplashMask.isRecycled())) {
            mSplashMask.recycle();
        }
        mSplashMask = null;
        if (!(mSplashFrame == null || mSplashFrame.isRecycled())) {
            mSplashFrame.recycle();
        }
//        if (mode == DRAG) {
//            mSplashMask = Bitmap.createBitmap(68, 90, Config.ARGB_8888);
//            mSplashFrame = Bitmap.createBitmap(68, 90, Config.ARGB_8888);
//            Canvas canvasMask = new Canvas(mSplashMask);
//            Canvas canvasShape = new Canvas(mSplashFrame);
//            mPaint.setColor(-1);
//            canvasMask.drawRect(1.0f, 1.0f, 67.0f, 89.0f, mPaint);
//            canvasShape.drawColor(-1);
//            mPaint.setXfermode(clearXfermode);
//            canvasShape.drawRect(1.0f, 1.0f, 67.0f, 89.0f, mPaint);
//            mPaint.setXfermode(null);
//        } else {
//            mSplashFrame = BitmapUtil.getImageFromAssetsFile(getResources(), "splash/splash_frame/" + mode + "_frame.png");
//            mSplashMask = BitmapUtil.getImageFromAssetsFile(getResources(), "splash/splash_frame/" + mode + "_mask.png");
//        }

        mSplashFrame = BitmapUtil.getImageFromAssetsFile(getResources(), "splash/splash_frame/" + mode + "_frame.png");
        mSplashMask = BitmapUtil.getImageFromAssetsFile(getResources(), "splash/splash_frame/" + mode + "_mask.png");

        curShapeMode = mode;
        resetMaskMatrix();
        invalidate();
    }

    public void setStyleMode(StyleMode styleMode) {
        if (myStyleMode != styleMode) {
            if (styleMode == StyleMode.B_W) {
                myStyleMode = StyleMode.B_W;
                if (!(filterBitmap == null || filterBitmap.isRecycled())) {
                    filterBitmap.recycle();
                    filterBitmap = null;
                }
                invalidate();
            } else if (styleMode == StyleMode.MOSAIC) {
                if (mImageBitmap != null && !mImageBitmap.isRecycled()) {
                    if (!(filterBitmap == null || filterBitmap.isRecycled())) {
                        filterBitmap.recycle();
                        filterBitmap = null;
                    }
                    GPUImagePixelationFilter filter = new GPUImagePixelationFilter();
                    filter.setOutBitmap(true);
                    filter.setFractionalWidthOfPixel(0.04f);
                    GPUFilter.asyncFilterForFilter(mImageBitmap, filter, new OnPostFilteredListener() {
                        public void postFiltered(Bitmap result) {
                            filterBitmap = result;
                            myStyleMode = StyleMode.MOSAIC;
                            invalidate();
                        }
                    });
                }
            } else if (styleMode == StyleMode.POLKA_DOT && mImageBitmap != null && !mImageBitmap.isRecycled()) {
                if (!(filterBitmap == null || filterBitmap.isRecycled())) {
                    filterBitmap.recycle();
                    filterBitmap = null;
                }
                GPUImagePolkaDotFilter filter2 = new GPUImagePolkaDotFilter();
                filter2.setOutBitmap(true);
                filter2.setFractionalWidthOfPixel(0.04f);
                filter2.setDotScaling(0.8f);
                GPUFilter.asyncFilterForFilter(mImageBitmap, filter2, new OnPostFilteredListener() {
                    public void postFiltered(Bitmap result) {
                        filterBitmap = result;
                        myStyleMode = StyleMode.POLKA_DOT;
                        invalidate();
                    }
                });

            }
        }
    }

    private void resetMaskMatrix() {
        float[] whpoint = null;
        mMaskMatrix = new Matrix();
        if (!(mImageBitmap == null || mImageBitmap.isRecycled() || mSplashMask == null || mSplashMask.isRecycled())) {
            int width = mImageBitmap.getWidth();
            int height = mImageBitmap.getHeight();
            float shapeScale = ((float) width) / (((float) mSplashMask.getWidth()) * 2.0f);
            if (width > height) {
                shapeScale = ((float) height) / (((float) mSplashMask.getHeight()) * 2.0f);
            }
            if (curShapeMode == JUMP) {
                if (width < height) {
                    shapeScale = ((float) width) / ((float) mSplashMask.getWidth());
                    mMaskMatrix.postScale(shapeScale, shapeScale);
                    mMaskMatrix.postTranslate(0.0f, ((float) (height - width)) / 2.0f);
                } else {
                    shapeScale = ((float) height) / ((float) mSplashMask.getWidth());
                    mMaskMatrix.postScale(shapeScale, shapeScale);
                    mMaskMatrix.postTranslate(((float) (width - height)) / 2.0f, 0.0f);
                }
            } else if (curShapeMode != 4 && curShapeMode != 5 && curShapeMode != 6) {
                mMaskMatrix.postScale(shapeScale, shapeScale);
                mMaskMatrix.postTranslate(((float) width) / 5.0f, ((float) height) / TOUCH_TOLERANCE);
                mMaskMatrix.postRotate(-15.0f);
            } else if (width < height) {
                shapeScale = (((float) width) / 1.3f) / ((float) mSplashMask.getWidth());
                mMaskMatrix.postScale(shapeScale, shapeScale);
                whpoint = new float[JUMP];
                whpoint[NONE] = (float) mSplashMask.getWidth();
                whpoint[DRAG] = (float) mSplashMask.getHeight();
                mMaskMatrix.mapPoints(whpoint);
                mMaskMatrix.postTranslate((((float) width) - whpoint[NONE]) / 2.0f, (((float) height) - whpoint[DRAG]) / 2.0f);
            } else {
                shapeScale = (((float) height) / 1.3f) / ((float) mSplashMask.getHeight());
                mMaskMatrix.postScale(shapeScale, shapeScale);
                whpoint = new float[JUMP];
                whpoint[NONE] = (float) mSplashMask.getWidth();
                whpoint[DRAG] = (float) mSplashMask.getHeight();
                mMaskMatrix.mapPoints(whpoint);
                mMaskMatrix.postTranslate((((float) width) - whpoint[NONE]) / 2.0f, (((float) height) - whpoint[DRAG]) / 2.0f);
            }
        }
        mMaskMatrix.postScale(mImageScale, mImageScale);
    }

    public void setImageBitmap(Bitmap bitmap, float scale) {
        mImageBitmap = bitmap;
        mImageMatrix = new Matrix();


        mImageMatrix.postScale(scale, scale);
        mImageScale = scale;
        resetMaskMatrix();
    }

    public void destroy() {
        BitmapUtil.free(mSplashMask);
        BitmapUtil.free(mSplashFrame);
        BitmapUtil.free(filterBitmap);
        BitmapUtil.free(result);
        filterBitmap = null;
    }

    public void setSplashInverse() {
        mSplashInverse = !mSplashInverse;
        invalidate();
    }

    @SuppressLint({"DrawAllocation"})
    public void drawView(Canvas canvas) {
        if (myStyleMode == StyleMode.B_W) {
            bwDrawHandle.drawView(canvas);
        } else {
            filterDrawHandle.drawView(canvas);
        }
    }

    public void drawImage(Canvas canvas) {
        if (myStyleMode == StyleMode.B_W) {
            bwDrawHandle.drawImage(canvas);
        } else {
            filterDrawHandle.drawImage(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mCurPoint.set(event.getX(), event.getY());
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(mCurPoint);
                mode = 1;
                mStart.set(mCurPoint.x, mCurPoint.y);
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                mode = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(mCurPoint);
                float f = mCurPoint.x - mStart.x;
                float f1 = mCurPoint.y - mStart.y;
                if (mode == 1) {
                    postTranslate(f, f1);
                    mStart.set(mCurPoint.x, mCurPoint.y);
                }
                if (mode == 2) {
                    mode = 1;
                    mStart.set(mCurPoint.x, mCurPoint.y);
                }
                if (mode != 3) {
                    break;
                }
                float f2 = (float) spacing(event);
                midPoint(mMid, event);


                postScale(f2 / oldDist);
                oldDist = f2;
                float f3 = rotation(event);
                postRotation(f3 - oldDegree);
                oldDegree = f3;
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_OUTSIDE:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if (event.getPointerCount() == 2) {
                    if (event.getActionIndex() < 1) {
                        mStart.set(mCurPoint.x, mCurPoint.y);
                    }
                    oldDist = (float) spacing(event);
                    oldDegree = rotation(event);
                    mode = 3;
                    midPoint(mMid, event);
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mode = 2;
                break;
        }
        return true;
    }

    private double spacing(MotionEvent event) {
        float x = event.getX(NONE) - event.getX(DRAG);
        float y = event.getY(NONE) - event.getY(DRAG);
        return Math.sqrt((double) ((x * x) + (y * y)));
    }

    private void midPoint(PointF point, MotionEvent event) {
        point.set((event.getX(NONE) + event.getX(DRAG)) / 2.0f, (event.getY(NONE) + event.getY(DRAG)) / 2.0f);
    }

    private float rotation(MotionEvent event) {
        return (float) Math.toDegrees(Math.atan2((double) (event.getY(NONE) - event.getY(DRAG)), (double) (event.getX(NONE) - event.getX(DRAG))));
    }

    public void postTranslate(float dx, float dy) {
        mMaskMatrix.postTranslate(dx, dy);
        invalidate();
    }

    public void postScale(float scale) {
        if (scale < 0.92) return;
        mMaskMatrix.postScale(scale, scale, mMid.x, mMid.y);
        invalidate();
    }

    public void postRotation(float degrees) {
        mMaskMatrix.postRotate(degrees, mMid.x, mMid.y);
        invalidate();
    }

    private void touch_start(PointF pointF) {
        mPath.reset();
        mPath.moveTo(pointF.x, pointF.y);
        mX = pointF.x;
        mY = pointF.y;
    }

    private void touch_move(PointF pointF) {
        float dx = Math.abs(pointF.x - mX);
        float dy = Math.abs(pointF.y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (pointF.x + mX) / 2.0f, (pointF.y + mY) / 2.0f);
            mX = pointF.x;
            mY = pointF.y;
        }
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        mPath.reset();
    }

    public enum SplashType {
        shape,
        touch
    }

    public enum StyleMode {
        B_W,
        MOSAIC,
        POLKA_DOT
    }

    private interface DrawHandle {
        void drawImage(Canvas canvas);

        void drawView(Canvas canvas);
    }


}
