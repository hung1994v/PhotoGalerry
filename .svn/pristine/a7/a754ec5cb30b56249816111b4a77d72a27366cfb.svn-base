package bsoft.com.lib_filter.filter.gpu;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

import bsoft.com.lib_filter.filter.gpu.father.GPUImageFilter;
import bsoft.com.lib_filter.filter.listener.OnPostFilteredListener;


public class AsyncGPUFilter23 {
    private final Handler handler = new Handler();
    private GPUImageFilter mFilter;
    private OnPostFilteredListener mListener;
    private Bitmap mSrc;

    class SaveFilterGPU implements Runnable {
        SaveFilterGPU() {

        }

        public void run() {
            final Bitmap bmp = AsyncGpuFliterUtil.filter(mSrc, mFilter);
            Log.d("asyncFilterForType ", "4444444  " + mListener);
            handler.post(new Runnable() {
                public void run() {
                    if (mListener != null) {
                        mSrc = null;
                        Log.d("bmppppp ", " " + bmp);
                        mListener.postFiltered(bmp);
                    }
                }
            });
        }
    }

    public static void executeAsyncFilter(Bitmap src, GPUImageFilter filter, OnPostFilteredListener listener) {
        AsyncGPUFilter23 asyncTask = new AsyncGPUFilter23();
        asyncTask.setData(src, filter, listener);
        asyncTask.execute();
    }

    public void setData(Bitmap src, GPUImageFilter filter, OnPostFilteredListener listener) {
        Log.d("asyncFilterForType ", "22222222  " + src);
        this.mSrc = src;
        this.mFilter = filter;
        this.mListener = listener;
    }

    public void execute() {
        Log.d("asyncFilterForType ", "3333333333  ");
        new Thread(new SaveFilterGPU()).start();
    }
}
