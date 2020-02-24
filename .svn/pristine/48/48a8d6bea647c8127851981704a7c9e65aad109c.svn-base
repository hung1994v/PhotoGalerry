package bsoft.healthy.tracker.menstrual.lib_sticker.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by nam on 4/11/2017.
 */

public class CustomRoundImage extends ImageView {

    private float radius = 15.0f;
    private Path path;
    private RectF rect = new RectF();
    private Paint paint;

    public CustomRoundImage(Context context) {
        super(context);
        init();
    }

    public CustomRoundImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomRoundImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        path = new Path();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);

    }
    @Override
    protected void onDraw(Canvas canvas) {

        rect.set(0, 0, this.getWidth(), this.getHeight());
        path.addRoundRect(rect, radius, radius, Path.Direction.CW);
//        Flog.d("onDraw "+path);
        canvas.clipPath(path);
        super.onDraw(canvas);

    }
}