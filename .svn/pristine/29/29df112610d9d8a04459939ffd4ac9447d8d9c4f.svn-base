package bsoft.com.lib_filter.filter.gpu;

import android.graphics.Bitmap;

import java.util.List;

import bsoft.com.lib_filter.filter.gpu.core.GPUImageRenderer;
import bsoft.com.lib_filter.filter.gpu.father.GPUImageFilter;
import bsoft.com.lib_filter.filter.gpu.father.GPUImageFilterGroup;
import bsoft.com.lib_filter.filter.gpu.father.GPUImageTwoInputFilter;
import bsoft.com.lib_filter.filter.gpu.util.PixelBuffer;
import bsoft.com.lib_filter.filter.gpu.util.Rotation;


public class AsyncGpuFliterUtil {
    public static Bitmap filter(Bitmap src, GPUImageFilter filter, boolean destroyFilter) {
        PixelBuffer buffer;
        if (filter == null || src == null || src.isRecycled()) {
            return src;
        }
        GPUImageRenderer gPUImageRenderer = null;
        PixelBuffer buffer2 = null;
        Bitmap result = null;
        try {
            filterSetRotation(filter);
            if (filter instanceof GPUImageFilterGroup) {
                List<GPUImageFilter> filters = ((GPUImageFilterGroup) filter).getFilters();
                for (int i = 0; i < filters.size(); i++) {
                    filterSetRotation((GPUImageFilter) filters.get(i));
                }
            }
            GPUImageRenderer renderer = new GPUImageRenderer(filter);
            try {
                renderer.setImageBitmap(src, false);
                renderer.setRotation(Rotation.NORMAL, false, true);
                buffer = new PixelBuffer(src.getWidth(), src.getHeight());
            } catch (Exception e) {
                gPUImageRenderer = renderer;
                if (destroyFilter) {
                    try {
                        filter.destroy();
                    } catch (Throwable th) {
                        return src;
                    }
                }
                if (gPUImageRenderer != null) {
                    gPUImageRenderer.deleteImage();
                }
                if (buffer2 != null) {
                    buffer2.destroy();
                }
                if (result != null) {
                    return result;
                }
                return src;
            } catch (Throwable th2) {
                gPUImageRenderer = renderer;
                if (destroyFilter) {
                    try {
                        filter.destroy();
                    } catch (Throwable th3) {
                        if (result == null) {
                            return src;
                        }
                        return result;
                    }
                }
                if (gPUImageRenderer != null) {
                    gPUImageRenderer.deleteImage();
                }
                if (buffer2 != null) {
                    buffer2.destroy();
                }
                if (result == null) {
                    return result;
                }
                return src;
            }
            try {
                buffer.setRenderer(renderer);
                result = buffer.getBitmap();
                renderer.deleteImage();
                buffer.destroy();
                gPUImageRenderer = null;
                buffer2 = null;
                if (!destroyFilter) {
                    return result;
                }
                filter.destroy();
                return result;
            } catch (Exception e2) {
                buffer2 = buffer;
                gPUImageRenderer = renderer;
                if (destroyFilter) {
                    filter.destroy();
                }
                if (gPUImageRenderer != null) {
                    gPUImageRenderer.deleteImage();
                }
                if (buffer2 != null) {
                    buffer2.destroy();
                }
                if (result != null) {
                    return result;
                }
                return src;
            } catch (Throwable th4) {
                buffer2 = buffer;
                gPUImageRenderer = renderer;
                if (destroyFilter) {
                    filter.destroy();
                }
                if (gPUImageRenderer != null) {
                    gPUImageRenderer.deleteImage();
                }
                if (buffer2 != null) {
                    buffer2.destroy();
                }
                if (result == null) {
                    return src;
                }
                return result;
            }
        } catch (Exception e3) {
        } catch (Throwable th5) {
        }
        return null;

    }


    public static Bitmap filter(Bitmap src, GPUImageFilter filter) {
        return (filter == null || src == null || src.isRecycled()) ? src : filter(src, filter, true);
    }

    public static void recycleTexture(GPUImageFilter filter) {
        Bitmap texture2;
        if (filter instanceof GPUImageFilterGroup) {
            for (GPUImageFilter gpuImageFilter : ((GPUImageFilterGroup) filter).getFilters()) {
                if (gpuImageFilter instanceof GPUImageTwoInputFilter) {
                    texture2 = ((GPUImageTwoInputFilter) gpuImageFilter).getTextureBitmap();
                    if (texture2 != null && !texture2.isRecycled()) {
                        texture2.recycle();
                    } else {
                        return;
                    }
                }
            }
        } else if (filter instanceof GPUImageTwoInputFilter) {
            texture2 = ((GPUImageTwoInputFilter) filter).getTextureBitmap();
            if (texture2 != null && !texture2.isRecycled()) {
                texture2.recycle();
            }
        }
    }

    public static void filterSetRotation(GPUImageFilter filter) {
        if (filter instanceof GPUImageTwoInputFilter) {
            ((GPUImageTwoInputFilter) filter).setRotation(Rotation.NORMAL, false, true);
        }
    }
}
