package com.photo.gallery.exoplayer;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;

import androidx.core.view.GestureDetectorCompat;


public final class ExoPlayerView extends PlayerView implements PlayerControlView.VisibilityListener, GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private static final float DRAG_THRESHOLD = 10;
    private static final long LONG_PRESS_THRESHOLD_MS = 500;

    private boolean controllerVisible;
    private long tapStartTimeMs;
    private float tapPositionX;
    private float tapPositionY;

    private OnControllerVisibilityListener controllerVisibilityListener;
    private OnDoubleTapListener doubleTapListener;

    private GestureDetectorCompat mDetector;

    public ExoPlayerView(Context context) {
        this(context, null);
    }

    public ExoPlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExoPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setControllerVisibilityListener(this);

        mDetector = new GestureDetectorCompat(context, this);
        mDetector.setOnDoubleTapListener(this);
    }

    public void setOnControllerVisibilityListener(OnControllerVisibilityListener listener) {
        controllerVisibilityListener = listener;
    }

    public void setOnDoubleTapListener(OnDoubleTapListener listener) {
        doubleTapListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mDetector.onTouchEvent(ev)) return true;

//        switch (ev.getActionMasked()) {
//            case MotionEvent.ACTION_DOWN:
//                tapStartTimeMs = SystemClock.elapsedRealtime();
//                tapPositionX = ev.getX();
//                tapPositionY = ev.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (tapStartTimeMs != 0
//                        && (Math.abs(ev.getX() - tapPositionX) > DRAG_THRESHOLD
//                        || Math.abs(ev.getY() - tapPositionY) > DRAG_THRESHOLD)) {
//                    tapStartTimeMs = 0;
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                if (tapStartTimeMs != 0) {
//                    if (SystemClock.elapsedRealtime() - tapStartTimeMs < LONG_PRESS_THRESHOLD_MS) {
//                        if (!controllerVisible) {
//                            showController();
//                        } else if (getControllerHideOnTouch()) {
//                            hideController();
//                        }
//                    }
//                    tapStartTimeMs = 0;
//                }
//        }
        return true;
    }

    @Override
    public void onVisibilityChange(int visibility) {
        controllerVisible = visibility == View.VISIBLE;

        if (controllerVisibilityListener != null) {
            controllerVisibilityListener.onVisibility(controllerVisible);
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        tapStartTimeMs = SystemClock.elapsedRealtime();
        tapPositionX = e.getX();
        tapPositionY = e.getY();
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (tapStartTimeMs != 0
                && (Math.abs(e2.getX() - tapPositionX) > DRAG_THRESHOLD
                || Math.abs(e2.getY() - tapPositionY) > DRAG_THRESHOLD)) {
            tapStartTimeMs = 0;
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        if (tapStartTimeMs != 0) {
            if (SystemClock.elapsedRealtime() - tapStartTimeMs < LONG_PRESS_THRESHOLD_MS) {
                if (!controllerVisible) {
                    showController();
                } else if (getControllerHideOnTouch()) {
                    hideController();
                }
            }
            tapStartTimeMs = 0;
        }
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        if (doubleTapListener != null) {
            doubleTapListener.onDoubleTap(e);
        }
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    public interface OnControllerVisibilityListener {
        void onVisibility(boolean isVisible);
    }

    public interface OnDoubleTapListener {
        void onDoubleTap(MotionEvent e);
    }
}