package bsoft.com.lib_filter.filter.gpu;

import com.google.android.gms.cast.TextTrackStyle;


public class PS2GpuImageValueConversion {
    public static final float HUE_MAGENTA = 300.0f;

    public static float getSaturaValue(float value) {
        if (value <= 0.0f) {
            return (value + 100.0f) / 100.0f;
        }
        return (value / 53.0f) + TextTrackStyle.DEFAULT_FONT_SCALE;
    }

    public static float getContrastValue(float value) {
        if (value <= 0.0f) {
            return (float) (((double) ((50.0f + value) / 1000.0f)) + 0.9d);
        }
        return (float) (((0.49d * ((double) value)) / 100.0d) + 1.0d);
    }

    public static float getHighlightsValue(float value) {
        return TextTrackStyle.DEFAULT_FONT_SCALE - (value / 7.0f);
    }

    public static float getBrightnessValue(float value) {
        return value / HUE_MAGENTA;
    }
}
