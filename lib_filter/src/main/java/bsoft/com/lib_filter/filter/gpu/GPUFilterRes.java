package bsoft.com.lib_filter.filter.gpu;

import android.graphics.Bitmap;
import android.util.Log;

import bsoft.com.lib_filter.filter.gpu.util.BitmapUtil;
import bsoft.com.lib_filter.filter.listener.OnPostFilteredListener;
import bsoft.com.lib_filter.filter.listener.WBAsyncPostIconListener;


public class GPUFilterRes extends WBImageRes {
    private GPUFilterType filterType;
    private Bitmap filtered;
    private Bitmap src;

    class PostFilter implements OnPostFilteredListener {
        private WBAsyncPostIconListener listener;

        PostFilter(WBAsyncPostIconListener wBAsyncPostIconListener) {
            listener = wBAsyncPostIconListener;
        }

        public void postFiltered(Bitmap result) {
            Log.d("postFiltered ", "  " + result);
            filtered = result;
            listener.postIcon(filtered);
        }
    }

    public GPUFilterRes() {
        filterType = GPUFilterType.NOFILTER;
        src = null;
        filtered = null;
    }

    public void setFilterType(GPUFilterType type) {
        filterType = type;
    }

    public GPUFilterType getFilterType() {
        return filterType;
    }

    public void setSRC(Bitmap bmp) {
        src = bmp;
    }

    public Bitmap getIconBitmap() {
        if (getIconType() == WBRes.LocationType.FILTERED) {
            asyncIcon = Boolean.valueOf(true);
            return src;
        } else if (getIconType() == WBRes.LocationType.RES) {
            return BitmapUtil.getImageFromResourceFile(getResources(), getIconID());
        } else {
            return BitmapUtil.getImageFromAssetsFile(getResources(), getIconFileName());
        }
    }

    public void getAsyncIconBitmap(WBAsyncPostIconListener listener) {
        Log.d("getAsyncIconBitmap ", " " + filtered + "___" + listener);
        if (filtered == null || filtered.isRecycled()) {
            try {
                Log.d("getAsyncIconBitmap ", "111111 " + filtered + "___" + listener + "__" + src);
                GPUFilter.asyncFilterForType(context, src, filterType, new PostFilter(listener));
            } catch (Exception e) {
                Log.d("getAsyncIconBitmap ", "333333 ");
                e.printStackTrace();
            }
        }
        Log.d("getAsyncIconBitmap ", "222222 " + filtered);
        listener.postIcon(filtered);
    }

    public void dispose() {
        if (!(filtered == null || filtered.isRecycled())) {
            filtered.recycle();
        }
        filtered = null;
    }
}