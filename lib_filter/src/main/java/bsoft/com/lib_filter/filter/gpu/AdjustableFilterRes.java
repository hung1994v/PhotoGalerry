package bsoft.com.lib_filter.filter.gpu;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import bsoft.com.lib_filter.filter.gpu.father.GPUImageFilter;
import bsoft.com.lib_filter.filter.listener.OnPostFilteredListener;
import bsoft.com.lib_filter.filter.listener.WBAsyncPostIconListener;


public class AdjustableFilterRes extends GPUFilterRes {
    private GPUFilterType filterType;
    private Bitmap filtered;
    private int mix;
    private Bitmap src;

    class AnonymousClass1 implements OnPostFilteredListener {
        private WBAsyncPostIconListener listener;

        AnonymousClass1(WBAsyncPostIconListener wBAsyncPostIconListener) {
            listener = wBAsyncPostIconListener;
        }

        public void postFiltered(Bitmap result) {
            filtered = result;
            listener.postIcon(filtered);
        }
    }

    public AdjustableFilterRes() {
        mix = 100;
        filterType = GPUFilterType.NOFILTER;
        src = null;
        filtered = null;
    }

    public void setFilterType(GPUFilterType type) {
        super.setFilterType(type);
        filterType = type;
    }

    public GPUFilterType getFilterType() {
        super.getFilterType();
        return filterType;
    }

    public void setSRC(Bitmap bmp) {
        super.setSRC(bmp);
        src = bmp;
    }

    public void setMix(int mix) {
        this.mix = mix;
    }

    public int getMix() {
        return mix;
    }

    public void getAsyncIconBitmap(WBAsyncPostIconListener listener) {
        if (filtered == null || filtered.isRecycled()) {
            try {
                synchronized (src) {
                    asyncFilterForType(context, src, filterType, mix, new AnonymousClass1(listener));
                }
                //     return;
            } catch (Exception e) {
                //      return;
            } catch (Throwable th) {
                //       return;
            }
        }

        Log.d("asyncFilterForType ", " " + src + "__" + filterType + "___" + mix);
        listener.postIcon(filtered);
    }

    public void dispose() {
        super.dispose();
        if (!(filtered == null || filtered.isRecycled())) {
            filtered.recycle();
        }
        filtered = null;
    }

    public static void asyncFilterForType(Context context, Bitmap src, GPUFilterType filterType, int mix, OnPostFilteredListener postListener) {
        Log.d("asyncFilterForType ", "4444444  " + src + "__" +GPUFilter.createFilterForType(context, filterType));
        GPUImageFilter filter = GPUFilter.createFilterForType(context, filterType);
        filter.setMix(((float) mix) / 100.0f);
        GPUFilter.asyncFilterForFilter(src, filter, postListener);
    }
}