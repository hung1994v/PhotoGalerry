package bsoft.healthy.tracker.menstrual.lib_sticker.main;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Created by Tung on 4/5/2018.
 */

public abstract class BaseStickerView {

    public static final int ICON_STICKER = 0x12;
    public static final int TEXT_STICKER = 0x13;

    public int mType = -1;

    public abstract void setType();

    public abstract void onDraw(Canvas canvas);
    public abstract boolean onTouchEvent(MotionEvent event);
    public abstract void setInEdit(boolean isInEdit);
    public abstract boolean isInEdit();
    public abstract boolean isItemDeleted();
    public abstract boolean isInBitmap(MotionEvent event);
    public abstract void release();
}
