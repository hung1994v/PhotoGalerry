package bsoft.com.lib_filter.filter.gpu;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import bsoft.com.lib_filter.filter.gpu.father.GPUImageFilter;
import bsoft.com.lib_filter.filter.listener.OnPostFilteredListener;


public class AsyncGPUFilter extends AsyncTask<String, Void, Bitmap> {
    private GPUImageFilter mFilter;
    private OnPostFilteredListener mListener;
    private Bitmap mSrc;

    public static void executeAsyncFilter(Bitmap src, GPUImageFilter filter, OnPostFilteredListener listener) {
        AsyncGPUFilter asyncTask = new AsyncGPUFilter();
        asyncTask.setData(src, filter, listener);
        asyncTask.execute(new String[0]);
    }

    public void setData(Bitmap src, GPUImageFilter filter, OnPostFilteredListener listener) {
        this.mSrc = src;
        this.mFilter = filter;
        this.mListener = listener;
    }

    protected Bitmap doInBackground(String... params) {
        return AsyncGpuFliterUtil.filter(this.mSrc, mFilter);
    }

    protected void onPostExecute(Bitmap result) {
        if (this.mListener != null) {
            this.mSrc = null;
            this.mListener.postFiltered(result);
        }
    }
}
