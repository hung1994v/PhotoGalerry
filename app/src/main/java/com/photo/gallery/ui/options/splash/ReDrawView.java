package com.photo.gallery.ui.options.splash;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Adm on 2/9/2017.
 */
public abstract class ReDrawView extends View {
    protected int canvasHeight;
    protected Rect canvasRect;
    protected int canvasWidth;
    protected PorterDuffXfermode clearXfermode;
    protected PorterDuffXfermode dstInXfermode;
    protected PorterDuffXfermode dstOutXfermode;
    protected Paint mPaint;
    protected PorterDuffXfermode srcInXfermode;
    protected PorterDuffXfermode srcOutXfermode;
    protected PorterDuffXfermode xorXfermode;

    public abstract void drawView(Canvas canvas);

    public ReDrawView(Context context) {
        super(context);
        this.mPaint = new Paint();
        this.clearXfermode = new PorterDuffXfermode(Mode.CLEAR);
        this.dstInXfermode = new PorterDuffXfermode(Mode.DST_IN);
        this.xorXfermode = new PorterDuffXfermode(Mode.XOR);
        this.srcInXfermode = new PorterDuffXfermode(Mode.SRC_IN);
        this.dstOutXfermode = new PorterDuffXfermode(Mode.DST_OUT);
        this.srcOutXfermode = new PorterDuffXfermode(Mode.SRC_OUT);
        this.canvasWidth = 0;
        this.canvasHeight = 0;
        this.canvasRect = new Rect();
        init();
    }

    public ReDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mPaint = new Paint();
        this.clearXfermode = new PorterDuffXfermode(Mode.CLEAR);
        this.dstInXfermode = new PorterDuffXfermode(Mode.DST_IN);
        this.xorXfermode = new PorterDuffXfermode(Mode.XOR);
        this.srcInXfermode = new PorterDuffXfermode(Mode.SRC_IN);
        this.dstOutXfermode = new PorterDuffXfermode(Mode.DST_OUT);
        this.srcOutXfermode = new PorterDuffXfermode(Mode.SRC_OUT);
        this.canvasWidth = 0;
        this.canvasHeight = 0;
        this.canvasRect = new Rect();
        init();
    }

    public ReDrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mPaint = new Paint();
        this.clearXfermode = new PorterDuffXfermode(Mode.CLEAR);
        this.dstInXfermode = new PorterDuffXfermode(Mode.DST_IN);
        this.xorXfermode = new PorterDuffXfermode(Mode.XOR);
        this.srcInXfermode = new PorterDuffXfermode(Mode.SRC_IN);
        this.dstOutXfermode = new PorterDuffXfermode(Mode.DST_OUT);
        this.srcOutXfermode = new PorterDuffXfermode(Mode.SRC_OUT);
        this.canvasWidth = 0;
        this.canvasHeight = 0;
        this.canvasRect = new Rect();
        init();
    }

    private void init() {
        this.mPaint.setDither(true);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setFilterBitmap(true);
        setWillNotDraw(false);
    }

    protected void onDraw(Canvas canvas) {
        this.canvasWidth = getWidth();
        this.canvasHeight = getHeight();
        this.canvasRect.set(0, 0, this.canvasWidth, this.canvasHeight);
        drawView(canvas);
    }
}