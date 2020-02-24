package bsoft.com.lib_filter.filter.gpu;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import java.io.File;


import bsoft.com.lib_filter.filter.gpu.core.GPUImage;
import bsoft.com.lib_filter.filter.gpu.father.GPUImageFilter;
import bsoft.com.lib_filter.filter.gpu.father.GPUImageTwoInputFilter;

import static android.view.View.MeasureSpec.EXACTLY;

public class GPUImageView extends GLSurfaceView {
    private GPUImageFilter mFilter;
    public GPUImage mGPUImage;
    private float mRatio = 0.0f;

    public GPUImageView(Context context) {
        super(context);
        init();
    }

    public GPUImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        this.mGPUImage = new GPUImage(getContext());
        this.mGPUImage.setGLSurfaceView(this, Boolean.valueOf(false));
        this.mGPUImage.setScaleType(GPUImage.ScaleType.CENTER_INSIDE);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mRatio == 0.0f) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int newWidth;
        int newHeight;
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (((float) width) / this.mRatio < ((float) height)) {
            newWidth = width;
            newHeight = Math.round(((float) width) / this.mRatio);
        } else {
            newHeight = height;
            newWidth = Math.round(((float) height) * this.mRatio);
        }
        super.onMeasure(MeasureSpec.makeMeasureSpec(newWidth, EXACTLY), MeasureSpec.makeMeasureSpec(newHeight, EXACTLY));
    }

    public void setRatio(float ratio) {
        this.mRatio = ratio;
        requestLayout();
        this.mGPUImage.deleteImage();
    }

    public void setFilter(GPUImageFilter filter) {
        if (this.mFilter != null) {
            // recycleFilter();
        }
        this.mFilter = filter;
        this.mGPUImage.setFilter(filter);
        requestRender();
    }

//    private void recycleFilter() {
//        if (this.mFilter instanceof GPUImageFilterGroup) {
//            for (GPUImageFilter gpuImageFilter : this.mFilter.getFilters()) {
//                recycleTexture(gpuImageFilter);
//            }
//            return;
//        }
//        recycleTexture(this.mFilter);
//    }

    public void setFilterNotRecycle(GPUImageFilter filter) {
        this.mFilter = filter;
        this.mGPUImage.setFilter(filter);
        requestRender();
    }

    public void recycleTexture(GPUImageFilter filter) {
        if (filter instanceof GPUImageTwoInputFilter) {
            Bitmap texture2 = ((GPUImageTwoInputFilter) filter).getTextureBitmap();
            if (texture2 != null && !texture2.isRecycled()) {
                texture2.recycle();
            }
        }
    }

    public GPUImageFilter getFilter() {
        return this.mFilter;
    }

    public void setImage(Bitmap bitmap) {
        this.mGPUImage.setImage(bitmap);
    }

    public void setImage(Uri uri) {
        this.mGPUImage.setImage(uri);
    }

    public void setImage(File file) {
        this.mGPUImage.setImage(file);
    }

    public void saveToPictures(String folderName, String fileName, GPUImage.OnPictureSavedListener listener) {
        this.mGPUImage.saveToPictures(folderName, fileName, listener);
    }

    public Bitmap getBitmap() {
        return this.mGPUImage.getBitmapWithFilterApplied();
    }

    public void destroy() {
        // recycleFilter();
        this.mGPUImage.destroy();
    }
}
