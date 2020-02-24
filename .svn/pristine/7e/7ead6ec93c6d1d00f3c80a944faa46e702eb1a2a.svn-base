package bsoft.com.lib_filter.filter.gpu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Handler;

import bsoft.com.lib_filter.filter.listener.OnPostFilteredListener;


public class AsyncSizeProcess {
    Bitmap filtered = null;
    private final Handler handler = new Handler();
    private WBRes mBorderRes;
    private Context mContext;
    private WBRes mFilterRes;
    private OnPostFilteredListener mListener;
    private Paint mPaint;
    private Bitmap mSrc;
    private WBRes mVigRes;
    class SizeProcess implements Runnable {

        class HandleSize implements Runnable {
            HandleSize() {
            }

            public void run() {
                if (AsyncSizeProcess.this.mListener != null) {
                    AsyncSizeProcess.this.mListener.postFiltered(AsyncSizeProcess.this.filtered);
                }
            }
        }
        SizeProcess() {
        }

        public void run() {
            try {
                AsyncSizeProcess.this.filtered = AsyncSizeProcess.this.mSrc;
                if (AsyncSizeProcess.this.mSrc != null) {
                    if (AsyncSizeProcess.this.mFilterRes != null) {
                        GPUFilterRes filterRes = (GPUFilterRes) mFilterRes;
                        AsyncSizeProcess.this.filtered = GPUFilter.filterForType(AsyncSizeProcess.this.mContext, AsyncSizeProcess.this.mSrc, filterRes.getFilterType());
                    }
                }
                AsyncSizeProcess.this.handler.post(new HandleSize());
            } catch (Exception e) {
                if (AsyncSizeProcess.this.mListener != null) {
                    AsyncSizeProcess.this.mListener.postFiltered(AsyncSizeProcess.this.mSrc);
                }
            }
        }
    }

    public static void executeAsyncFilter(Context context, Bitmap src, WBRes filterRes, WBRes vigRes, WBRes borderRes, OnPostFilteredListener listener) {
        AsyncSizeProcess loader = new AsyncSizeProcess();
        loader.setData(context, src, filterRes, vigRes, borderRes, listener);
        loader.execute();
    }

    public void setData(Context context, Bitmap src, WBRes filterRes, WBRes vigRes, WBRes borderRes, OnPostFilteredListener listener) {
        this.mContext = context;
        this.mSrc = src;
        this.mFilterRes = filterRes;
        this.mVigRes = vigRes;
        this.mBorderRes = borderRes;
        this.mListener = listener;
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setXfermode(new PorterDuffXfermode(Mode.DST_ATOP));
    }

    public void execute() {
        new Thread(new SizeProcess()).start();
    }
}
