package bsoft.com.lib_filter.filter.gpu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import java.io.IOException;

import bsoft.com.lib_filter.filter.gpu.father.GPUImageFilter;
import bsoft.com.lib_filter.filter.gpu.father.GPUImageThreeInputFilter;
import bsoft.com.lib_filter.filter.gpu.father.GPUImageTwoInputFilter;
import bsoft.com.lib_filter.filter.gpu.normal.GPUImageToneCurveFilter;
import bsoft.com.lib_filter.filter.gpu.tonewithblend.GPUImageToneCurveWithNormalBlendOpacityFilter;
import bsoft.com.lib_filter.filter.gpu.vignette.GPUImageVignetteMapSelfBlendFilter;
import bsoft.com.lib_filter.filter.gpu.vignette.GPUImageVignetteToneCurveMapFilter;


public class GPUImageFilterCreator {
    public static GPUImageFilter createFilterForTwoInputFilter(Context context, String mapName, Class<? extends GPUImageTwoInputFilter> filterClass) {
        GPUImageFilter defaultFilter = new GPUImageFilter();
        try {
            GPUImageTwoInputFilter filter = (GPUImageTwoInputFilter) filterClass.newInstance();
            Bitmap map = BitmapFactory.decodeStream(context.getResources().getAssets().open(mapName));
            if (map == null) {
                return defaultFilter;
            }
            filter.setBitmap(map);
            return filter;
        } catch (Exception e) {
            e.printStackTrace();
            return defaultFilter;
        }
    }

    public static GPUImageFilter createFilterForThreeInputFilter(Context context, String layerName, String mapName, Class<? extends GPUImageThreeInputFilter> filterClass) {
        GPUImageFilter defaultFilter = new GPUImageFilter();
        try {
            GPUImageThreeInputFilter filter = (GPUImageThreeInputFilter) filterClass.newInstance();
            Bitmap layer = BitmapFactory.decodeStream(context.getResources().getAssets().open(layerName));
            Bitmap map = BitmapFactory.decodeStream(context.getResources().getAssets().open(mapName));
            filter.setBitmap2(layer);
            filter.setBitmap3(map);
            return filter;
        } catch (Exception e) {
            e.printStackTrace();
            return defaultFilter;
        }
    }

    public static GPUImageFilter createFilterForVignetteTwoInputFilter(Context context, String mapName, PointF centerPoint, float start, float end, Class<? extends GPUImageTwoInputFilter> filterClass) {
        Bitmap map = null;
        try {
            map = BitmapFactory.decodeStream(context.getResources().getAssets().open(mapName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (filterClass == GPUImageVignetteToneCurveMapFilter.class) {
                GPUImageVignetteToneCurveMapFilter v1 = new GPUImageVignetteToneCurveMapFilter(centerPoint, start, end);
                v1.setBitmap(map);
                return v1;
            } else if (filterClass != GPUImageVignetteMapSelfBlendFilter.class) {
                return new GPUImageFilter();
            } else {
                GPUImageTwoInputFilter v3 = new GPUImageVignetteMapSelfBlendFilter(centerPoint, start, end);
                v3.setBitmap(map);
                return v3;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static GPUImageFilter createBlendFilter(Context context, Class<? extends GPUImageTwoInputFilter> filterClass) {
        GPUImageFilter defaultFilter = new GPUImageFilter();
        try {
            return (GPUImageTwoInputFilter) filterClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return defaultFilter;
        }
    }

    public static GPUImageFilter createBlendFilter(Context context, Class<? extends GPUImageTwoInputFilter> filterClass, String blendName) {
        GPUImageFilter defaultFilter = new GPUImageFilter();
        try {
            GPUImageTwoInputFilter filter = (GPUImageTwoInputFilter) filterClass.newInstance();
            Bitmap map = BitmapFactory.decodeStream(context.getResources().getAssets().open(blendName));
            if (map == null) {
                return defaultFilter;
            }
            filter.setBitmap(map);
            return filter;
        } catch (Exception e) {
            e.printStackTrace();
            return defaultFilter;
        }
    }

    public static GPUImageFilter createACVCurveFilter(Context context, String acvName) {
        GPUImageFilter defaultFilter = new GPUImageFilter();
        try {
            GPUImageToneCurveFilter toneCurveFilter = new GPUImageToneCurveFilter();
            toneCurveFilter.setFromAcvCurveFileInputStream(context.getResources().getAssets().open(acvName));
            return toneCurveFilter;
        } catch (IOException e) {
            e.printStackTrace();
            return defaultFilter;
        }
    }

    public static GPUImageFilter createDATCurveFilter(Context context, String datName) {
        GPUImageFilter defaultFilter = new GPUImageFilter();
        try {
            GPUImageToneCurveFilter toneCurveFilter = new GPUImageToneCurveFilter();
            toneCurveFilter.setFromDatCurveFileInputStream(context.getResources().getAssets().open(datName));
            toneCurveFilter.setFileType("dat");
            return toneCurveFilter;
        } catch (IOException e) {
            e.printStackTrace();
            return defaultFilter;
        }
    }

    public static GPUImageFilter createBlendFilter(Context context, int color, Class<? extends GPUImageTwoInputFilter> filterClass) {
        GPUImageFilter defaultFilter = new GPUImageFilter();
        try {
            GPUImageTwoInputFilter filter = (GPUImageTwoInputFilter) filterClass.newInstance();
            filter.setBitmap(Bitmap.createBitmap(new int[]{color}, 1, 1, Config.ARGB_4444));
            return filter;
        } catch (Exception e) {
            e.printStackTrace();
            return defaultFilter;
        }
    }

    public static GPUImageFilter createBlendFilter(Context context, int color, float mix, Class<? extends GPUImageTwoInputFilter> filterClass) {
        GPUImageFilter defaultFilter = new GPUImageFilter();
        try {
            GPUImageTwoInputFilter filter = (GPUImageTwoInputFilter) filterClass.newInstance();
            filter.setBitmap(Bitmap.createBitmap(new int[]{color}, 1, 1, Config.ARGB_4444));
            filter.setMix(mix);
            return filter;
        } catch (Exception e) {
            e.printStackTrace();
            return defaultFilter;
        }
    }

    public static GPUImageFilter createBlendFilter(Context context, Bitmap bitmap, Class<? extends GPUImageTwoInputFilter> filterClass) {
        GPUImageFilter defaultFilter = new GPUImageFilter();
        try {
            GPUImageTwoInputFilter filter = (GPUImageTwoInputFilter) filterClass.newInstance();
            filter.setBitmap(bitmap);
            return filter;
        } catch (Exception e) {
            e.printStackTrace();
            return defaultFilter;
        }
    }

    public static GPUImageFilter createBlendFilter(Context context, int color1, int color2, Class<? extends GPUImageThreeInputFilter> filterClass) {
        GPUImageFilter defaultFilter = new GPUImageFilter();
        try {
            GPUImageThreeInputFilter filter = (GPUImageThreeInputFilter) filterClass.newInstance();
            filter.setBitmap2(Bitmap.createBitmap(new int[]{color1}, 1, 1, Config.ARGB_4444));
            filter.setBitmap3(Bitmap.createBitmap(new int[]{color2}, 1, 1, Config.ARGB_4444));
            return filter;
        } catch (Exception e) {
            e.printStackTrace();
            return defaultFilter;
        }
    }

    public static GPUImageFilter createACVCurveFilter(Context context, String acvName, Class<? extends GPUImageToneCurveFilter> filterClass) {
        GPUImageFilter defaultFilter = new GPUImageFilter();
        try {
            GPUImageToneCurveFilter toneCurveFilter = (GPUImageToneCurveFilter) filterClass.newInstance();
            toneCurveFilter.setFromAcvCurveFileInputStream(context.getResources().getAssets().open(acvName));
            return toneCurveFilter;
        } catch (Exception e) {
            e.printStackTrace();
            return defaultFilter;
        }
    }

    public static GPUImageFilter createACVCurveFilter(Context context, String acvName, float opacity) {
        GPUImageFilter defaultFilter = new GPUImageFilter();
        try {
            GPUImageToneCurveWithNormalBlendOpacityFilter toneCurveFilter = new GPUImageToneCurveWithNormalBlendOpacityFilter();
            toneCurveFilter.setFromAcvCurveFileInputStream(context.getResources().getAssets().open(acvName));
            toneCurveFilter.setOpacity(opacity);
            return toneCurveFilter;
        } catch (IOException e) {
            e.printStackTrace();
            return defaultFilter;
        }
    }

    public static GPUImageFilter createACVCurveFilter(Context context, String acvName, float opacity, Class<? extends GPUImageToneCurveWithNormalBlendOpacityFilter> filterClass) {
        GPUImageFilter defaultFilter = new GPUImageFilter();
        try {
            GPUImageToneCurveWithNormalBlendOpacityFilter toneCurveFilter = (GPUImageToneCurveWithNormalBlendOpacityFilter) filterClass.newInstance();
            toneCurveFilter.setFromAcvCurveFileInputStream(context.getResources().getAssets().open(acvName));
            toneCurveFilter.setOpacity(opacity);
            return toneCurveFilter;
        } catch (Exception e) {
            e.printStackTrace();
            return defaultFilter;
        }
    }
}
