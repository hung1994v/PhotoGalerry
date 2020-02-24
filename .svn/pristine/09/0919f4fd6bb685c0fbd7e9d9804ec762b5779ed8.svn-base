package bsoft.com.lib_filter.filter.border;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

public class BorderImageView extends IgnoreRecycleImageView {
    int borderColor;
    float borderWidth;
    RectF boundRect;
    float circleBorderRadius;
    Bitmap imageBorderBitmap;
    int imageColor;
    boolean isCircle;
    boolean isCircleBorder;
    boolean isFillet;
    boolean isImageBorder;
    boolean isShowBorder;
    public Context mContext;
    Paint paint;
    int radius;
    Bitmap srcBitmap;

    public BorderImageView(Context context) {
        super(context);
        this.borderColor = 0;
        this.borderWidth = 5.0f;
        this.paint = new Paint();
        this.boundRect = new RectF();
        this.isShowBorder = false;
        this.isCircleBorder = false;
        this.circleBorderRadius = 50.0f;
        this.isImageBorder = false;
        this.imageBorderBitmap = null;
        this.srcBitmap = null;
        this.isCircle = false;
        this.radius = 0;
        this.isFillet = false;
        this.imageColor = 0;
        this.mContext = context;
    }

    public BorderImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.borderColor = 0;
        this.borderWidth = 5.0f;
        this.paint = new Paint();
        this.boundRect = new RectF();
        this.isShowBorder = false;
        this.isCircleBorder = false;
        this.circleBorderRadius = 50.0f;
        this.isImageBorder = false;
        this.imageBorderBitmap = null;
        this.srcBitmap = null;
        this.isCircle = false;
        this.radius = 0;
        this.isFillet = false;
        this.imageColor = 0;
        this.mContext = context;
    }

    public BorderImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.borderColor = 0;
        this.borderWidth = 5.0f;
        this.paint = new Paint();
        this.boundRect = new RectF();
        this.isShowBorder = false;
        this.isCircleBorder = false;
        this.circleBorderRadius = 50.0f;
        this.isImageBorder = false;
        this.imageBorderBitmap = null;
        this.srcBitmap = null;
        this.isCircle = false;
        this.radius = 0;
        this.isFillet = false;
        this.imageColor = 0;
        this.mContext = context;
    }

    public int getRadius() {
        return this.radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setImageColor(int color) {
        this.imageColor = color;
    }

    public int getImageColor() {
        return this.imageColor;
    }

    public boolean isCircleBorder() {
        return this.isCircleBorder;
    }

    public float getCircleBorderRadius() {
        return this.circleBorderRadius;
    }

    public void setCircleBorder(boolean showCircleBorder, float circleRadius) {
        this.isCircleBorder = showCircleBorder;
        this.circleBorderRadius = circleRadius;
    }

    public void setImageBitmap(Bitmap bmp) {
        if (bmp == null || bmp.isRecycled()) {
            super.setImageBitmap(null);
        } else {
            this.srcBitmap = bmp;
            super.setImageBitmap(bmp);
        }
        invalidate();
    }

    public void setShowBorder(boolean showBorder) {
        this.isShowBorder = showBorder;
    }

    public void setShowImageBorder(boolean imageBorder, Bitmap borderBitmap) {
        this.isImageBorder = imageBorder;
        this.imageBorderBitmap = borderBitmap;
    }

    public boolean isShowBorder() {
        return this.isShowBorder;
    }

    public void setBorderWidth(float width) {
        this.borderWidth = width;
    }

    public void setFilletState(boolean state) {
        this.isFillet = state;
    }

    public boolean getFilletState() {
        return this.isFillet;
    }

    public void setCircleState(boolean state) {
        this.isCircle = state;
    }

    public boolean getCircleState() {
        return this.isCircle;
    }

    public void setBorderColor(int color) {
        this.borderColor = color;
    }

    @SuppressLint({"DrawAllocation"})
    protected void onDraw(Canvas canvas) {
        Bitmap b;
        Rect rectSrc;
        Rect rectDest;
        if (!this.isCircle) {
            if (this.isFillet) {
                if (this.srcBitmap != null && !this.srcBitmap.isRecycled()) {
                    b = getFilletBitmap(this.srcBitmap);
                    rectSrc = new Rect(0, 0, b.getWidth(), b.getHeight());
                    rectDest = new Rect(0, 0, getWidth(), getWidth());
                    if (getWidth() > getHeight()) {
                        rectDest = new Rect((getWidth() - getHeight()) / 2, 0, getHeight() + ((getWidth() - getHeight()) / 2), getHeight());
                    } else {
                        rectDest = new Rect(0, (getHeight() - getWidth()) / 2, getWidth(), getWidth() + ((getHeight() - getWidth()) / 2));
                    }
                    this.paint.reset();
                    canvas.drawBitmap(b, rectSrc, rectDest, this.paint);
                    if (!(b == this.srcBitmap || b == null || b.isRecycled())) {
                        b.recycle();
                    }
                } else if (this.imageColor != 0) {
                    this.paint.setAntiAlias(true);
                    canvas.drawARGB(0, 0, 0, 0);
                    this.paint.setColor(this.imageColor);
                    canvas.drawRoundRect(new RectF(new Rect(0, 0, getWidth(), getHeight())), 10.0f, 10.0f, this.paint);
                }
            } else if (!(this.srcBitmap == null || this.srcBitmap.isRecycled())) {
                super.onDraw(canvas);
            }
            if (!this.isShowBorder) {
                return;
            }
            if (this.isImageBorder) {
                this.paint.reset();
                this.paint.setAntiAlias(true);
                this.boundRect.left = 0.0f;
                this.boundRect.top = 0.0f;
                this.boundRect.right = (float) getWidth();
                this.boundRect.bottom = (float) getHeight();
                if (this.imageBorderBitmap != null && !this.imageBorderBitmap.isRecycled()) {
                    canvas.drawBitmap(this.imageBorderBitmap, null, this.boundRect, this.paint);
                }
            } else if (this.isCircleBorder) {
                float radius = ((float) (getHeight() > getWidth() ? getWidth() : getHeight())) / 2.0f;
                if (radius > this.circleBorderRadius) {
                    radius = this.circleBorderRadius;
                }
                this.paint.reset();
                this.paint.setAntiAlias(true);
                this.paint.setColor(this.borderColor);
                this.paint.setStyle(Style.STROKE);
                this.paint.setStrokeWidth(this.borderWidth);
                canvas.drawCircle((float) (getWidth() / 2), (float) (getHeight() / 2), radius - 1.0f, this.paint);
            } else {
                this.boundRect.left = 0.0f;
                this.boundRect.top = 0.0f;
                this.boundRect.right = (float) getWidth();
                this.boundRect.bottom = (float) getHeight();
                this.paint.reset();
                this.paint.setAntiAlias(true);
                this.paint.setColor(this.borderColor);
                this.paint.setStyle(Style.STROKE);
                this.paint.setStrokeWidth(this.borderWidth);
                canvas.drawRect(this.boundRect, this.paint);
            }
        } else if (this.srcBitmap != null && !this.srcBitmap.isRecycled()) {
            if (this.radius == 0) {
                b = getCircleBitmap(this.srcBitmap);
            } else {
                b = getRadiusRectBitmap(this.srcBitmap);
            }
            rectSrc = new Rect(0, 0, b.getWidth(), b.getHeight());
            rectDest = new Rect(0, 0, getWidth(), getWidth());
            if (getWidth() > getHeight()) {
                rectDest = new Rect((getWidth() - getHeight()) / 2, 0, getHeight() + ((getWidth() - getHeight()) / 2), getHeight());
            } else {
                rectDest = new Rect(0, (getHeight() - getWidth()) / 2, getWidth(), getWidth() + ((getHeight() - getWidth()) / 2));
            }
            this.paint.reset();
            canvas.drawBitmap(b, rectSrc, rectDest, this.paint);
            if (b != this.srcBitmap && b != null && !b.isRecycled()) {
                b.recycle();
            }
        } else if (this.imageColor != 0) {
            this.paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            this.paint.setColor(this.imageColor);
            int x = getWidth();
            if (x > getHeight()) {
                x = getHeight();
            }
            canvas.drawCircle((float) (getWidth() / 2), (float) (getHeight() / 2), (float) (x / 2), this.paint);
        }
    }

    private Bitmap getFilletBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        this.paint.reset();
        this.paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        this.paint.setColor(Color.BLUE);
        canvas.drawRoundRect(new RectF(rect), 10.0f, 10.0f, this.paint);
        this.paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, this.paint);
        return output;
    }

    private Bitmap getCircleBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        this.paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        this.paint.setColor(Color.BLUE);
        int x = bitmap.getWidth();
        canvas.drawCircle((float) (x / 2), (float) (x / 2), (float) (x / 2), this.paint);
        this.paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, this.paint);
        return output;
    }

    private Bitmap getRadiusRectBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        this.paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        this.paint.setColor(Color.BLUE);
        int x = bitmap.getWidth();
        canvas.drawRoundRect(new RectF(0.0f, 0.0f, (float) bitmap.getWidth(), (float) bitmap.getHeight()), (float) this.radius, (float) this.radius, this.paint);
        this.paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, this.paint);
        return output;
    }
}