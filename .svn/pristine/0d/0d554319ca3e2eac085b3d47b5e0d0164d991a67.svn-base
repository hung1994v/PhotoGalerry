package bsoft.healthy.tracker.menstrual.lib_sticker.models;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;

import bsoft.healthy.tracker.menstrual.lib_sticker.main.BaseStickerView;
import bsoft.healthy.tracker.menstrual.lib_sticker.main.IconStickerView;
import bsoft.healthy.tracker.menstrual.lib_sticker.main.TextStickerView;


/**
 * Created by thuck on 9/19/2016.
 */
public class ListAdaptiveItem extends ArrayList<BaseStickerView> {

    private static final String TAG = ListAdaptiveItem.class.getSimpleName();
    private Context context;
    private int mCurrentItemIndex = -1;

    public void onDraw(Canvas canvas) {
        Log.d(TAG, "list current index=" + mCurrentItemIndex);

        for (BaseStickerView item : this) {
            if (item != null) {
                item.onDraw(canvas);
            }
        }
    }


    @Override
    public boolean remove(Object object) {
        return super.remove(object);
    }

    public BaseStickerView getCurrentItem() {
        int index = mCurrentItemIndex;
        if (index < 0 || index >= this.size()) {
//            Log.i("itemsticker current is null at getCurrentSticker");
            return null;
        }
        return this.get(index);
    }

    public void setCurrentItem(int index) {
        if (index < 0 || index >= this.size()) {
//            Log.i("itemsticker current is null");
            return;
        }
        mCurrentItemIndex = index;
        for (int i = 0; i < this.size(); i++) {
            BaseStickerView item = this.get(i);
            if (i != index && item != null) {
                item.setInEdit(false);
                return;
            }
            if (i == index && item != null) {
                item.setInEdit(true);
                return;
            }
        }
    }

    public boolean isInEdit() {
        for (BaseStickerView item : this) {
            if (item != null && item.isInEdit()) return true;
        }
        return false;
    }

    public boolean setCurrentItem(MotionEvent event) {
        boolean result = false;

        for (int i = this.size() - 1; i >= 0; i--) {
            final BaseStickerView item = this.get(i);

            Log.d("met", "item " + i + "_" + item);
            if (item != null && item.isInBitmap(event) && !result) {
                mCurrentItemIndex = i;
                item.setInEdit(true);
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
//                        if (item.getListener() != null) {
//                            item.getListener().onItemClicked(item, true);
//                        }
                    }
                });
                result = true;
            } else if (item != null) {
                item.setInEdit(false);
            }
        }
        if (!result) mCurrentItemIndex = -1;
        return result;
    }

    public boolean onTouchEvent(MotionEvent event) {
        BaseStickerView BaseStickerView = getCurrentItem();
        if (BaseStickerView != null) {
            if (BaseStickerView.onTouchEvent(event)) {
                return true;
            }
        } else {
//            Log.i("ItemSticker == null");
        }
        return false;
    }


    public boolean notTouchAll() {
        for (BaseStickerView item : this) {
            if (item != null && item.isInEdit())
                return false;
        }
        return true;
    }

    public boolean isDeleteAll() {
        for (BaseStickerView item : this) {
            if (item != null && !item.isItemDeleted())
                return false;
        }
        return true;
    }

    public void setNotTouchAll() {
        for (BaseStickerView item : this) {
            if (item != null) {
                item.setInEdit(false);
            }
        }
    }

    public void moveToTop() {

        if (true) {
            return;
        }
        int index = mCurrentItemIndex;
        BaseStickerView prevItem = this.get(index);
        this.remove(index);
        this.add(prevItem);
    }

    public void reset() {
        for (BaseStickerView item: this) {
            if (item instanceof IconStickerView) {
                ((IconStickerView)item).setIsDeleted(true);
            } else if (item instanceof TextStickerView){
                ((TextStickerView)item).disableDraw();
            }
        }

        this.clear();

        mCurrentItemIndex = -1;
    }
}
