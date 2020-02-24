package bsoft.com.lib_filter.filter.gpu.normal;

import com.google.android.gms.cast.TextTrackStyle;

public class GPUImageSepiaFilter extends GPUImageColorMatrixFilter {
    public GPUImageSepiaFilter() {
        this(TextTrackStyle.DEFAULT_FONT_SCALE);
    }

    public GPUImageSepiaFilter(float intensity) {
        super(intensity, new float[]{0.3588f, 0.7044f, 0.1368f, 0.0f, 0.299f, 0.587f, 0.114f, 0.0f, 0.2392f, 0.4696f, 0.0912f, 0.0f, 0.0f, 0.0f, 0.0f, TextTrackStyle.DEFAULT_FONT_SCALE});
    }
}
