package bsoft.com.lib_filter.filter;

import com.google.android.gms.cast.TextTrackStyle;

public class GPUAdjustRange {
    public static float range(int percentage, float start, float end) {
        return (((end - start) * ((float) percentage)) / 100.0f) + start;
    }

    public static float getBrightnessRange(int progress) {
        return range(progress, -0.2f, 0.2f);
    }

    public static float getContrastRange(int progress) {
        if (progress < 50) {
            return range(progress, 0.8f, 1.2f);
        }
        return range(progress, 0.6f, 1.4f);
    }

    public static float getSaturationRange(int progress) {
        return range(progress, 0.0f, 2.0f);
    }



    public static float getExposureRange(int progress) {
        return range(progress, -0.2f, 0.2f);
    }

    public static float getTemperatureRange(int progress) {
        if (progress < 50) {
            return range(progress, 4000.0f, 6000.0f);
        }
        return range(progress, 2000.0f, 8000.0f);
    }

    public static float getTintRange(int progress) {
        return range(progress, -100.0f, 100.0f);
    }

    public static float getSharpenRange(int progress) {
        return range(progress, -0.5f, 0.5f);
    }

    public static float getGammaRange(int progress) {
        return range(progress, 1.2f, 0.8f);
    }

    public static float getHueRange(int progress) {
        return range(progress, 0.0f, 360.0f);
    }

    public static float getShadowRange(int progress) {
        return range(progress, 0.0f, TextTrackStyle.DEFAULT_FONT_SCALE);
    }

    public static float getHighlightRange(int progress) {
        return range(progress, TextTrackStyle.DEFAULT_FONT_SCALE, 0.0f);
    }

    public static float getRRange(int progress) {
        return range(progress, 0.0f, 2.0f);
    }

    public static float getGRange(int progress) {
        return range(progress, 0.0f, 2.0f);
    }

    public static float getBRange(int progress) {
        return range(progress, 0.0f, 2.0f);
    }

    public static float getVigneeteRange(int progress) {
        return range(progress, 0.75f, 0.3f);
    }


}