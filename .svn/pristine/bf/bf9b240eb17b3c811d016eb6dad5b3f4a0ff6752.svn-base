package bsoft.com.lib_filter.filter.gpu;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import bsoft.com.lib_filter.filter.GPUImageNativeLibrary;
import bsoft.com.lib_filter.filter.gpu.father.GPUImageFilter;
import bsoft.com.lib_filter.filter.gpu.father.GPUImageTwoInputFilter;
import bsoft.com.lib_filter.filter.listener.OnPostFilteredListener;


public class GPUFilter {
    static class PostFiltes implements OnPostFilteredListener {
        private GPUImageFilter filter;
        private OnPostFilteredListener postListener;

        PostFiltes(GPUImageFilter gPUImageFilter, OnPostFilteredListener onPostFilteredListener) {
            this.filter = gPUImageFilter;
            this.postListener = onPostFilteredListener;
        }

        public void postFiltered(Bitmap result) {
            GPUFilter.recycleTexture(this.filter);
            this.postListener.postFiltered(result);
        }
    }

    class PostFilter implements OnPostFilteredListener {
        private OnPostFilteredListener postListener;

        PostFilter(OnPostFilteredListener onPostFilteredListener) {
            postListener = onPostFilteredListener;
        }

        public void postFiltered(Bitmap result) {
            postListener.postFiltered(result);
        }
    }

    public static Bitmap filterForType(Context context, Bitmap src, GPUFilterType filterType) {
//        boolean isInit = GPUImageNativeLibrary.initGpuNativeLibrary(context);
        GPUImageFilter filter = createFilterForType(context, filterType);
        Bitmap dst = filterForFilter(src, filter);
        recycleTexture(filter);
        return dst;
    }

    public static Bitmap filterForType(Context context, Bitmap src, GPUFilterType filterType, float mix) {
  //      boolean isInit = GPUImageNativeLibrary.initGpuNativeLibrary(context);
        GPUImageFilter filter = createFilterForType(context, filterType);
        filter.setMix(mix);
        Bitmap dst = filterForFilter(src, filter);
        recycleTexture(filter);
        return dst;
    }

    public static void asyncFilterForType(Context context, Bitmap src, GPUFilterType filterType, OnPostFilteredListener postListener) {
        //  boolean isInit = GPUImageNativeLibrary.initGpuNativeLibrary(context);
        asyncFilterForFilter(src, createFilterForType(context, filterType), postListener);
    }

    public static Bitmap filterForFilter(Bitmap src, GPUImageFilter filter) {
        Bitmap dst = AsyncGpuFliterUtil.filter(src, filter);
        recycleTexture(filter);
        return dst;
    }

    public static void asyncFilterForFilter(Bitmap src, GPUImageFilter filter, OnPostFilteredListener postListener) {
        Log.d("asyncFilterForType ", "111111 " + src);
        AsyncGPUFilter23.executeAsyncFilter(src, filter, new PostFiltes(filter, postListener));
    }

    public void asyncFilterForFilterNotRecycle(Bitmap src, GPUImageFilter filter, OnPostFilteredListener postListener) {
        AsyncGPUFilter23.executeAsyncFilter(src, filter, new PostFilter(postListener));
    }

    public static GPUImageFilter createFilterForType(Context context, GPUFilterType type) {
//        boolean isInit = GPUImageNativeLibrary.initGpuNativeLibrary(context);
        Log.d("createFilterForType ", " ");
        return GPUFilterFactory.createFilterForType(context, type);
    }

    public static GPUImageFilter createFilterForBlendType(Context context, GPUFilterType type, Bitmap layer) {
        //    boolean isInit = GPUImageNativeLibrary.initGpuNativeLibrary(context);
        GPUImageTwoInputFilter filter = (GPUImageTwoInputFilter) GPUFilterFactory.createFilterForType(context, type);
        if (filter == null || layer == null || layer.isRecycled()) {
            return new GPUImageFilter();
        }
        filter.setBitmap(layer);
        return filter;
    }

    public static void recycleTexture(GPUImageFilter filter) {
        AsyncGpuFliterUtil.recycleTexture(filter);
    }
}