package bsoft.com.lib_filter.filter.border;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

public class IgnoreRecycleImageView extends ImageView {
    public Bitmap image;
    public IgnoreRecycleImageView(Context context) {
        super(context);
    }

    public IgnoreRecycleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IgnoreRecycleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public void setImageBitmap(Bitmap bm) {
        this.image = bm;
        super.setImageBitmap(bm);
    }
    protected void onDraw(Canvas canvas) {
        try {
            super.onDraw(canvas);
        } catch (Throwable th) {
        }
    }
}