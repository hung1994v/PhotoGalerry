package bsoft.com.lib_filter.filter.gpu.normal;

import com.google.android.gms.cast.TextTrackStyle;

public class GPUImageEmbossFilter extends GPUImage3x3ConvolutionFilter {
    private float mIntensity;

    public GPUImageEmbossFilter() {
        this(TextTrackStyle.DEFAULT_FONT_SCALE);
    }

    public GPUImageEmbossFilter(float intensity) {
        this.mIntensity = intensity;
    }

    public void onInit() {
        super.onInit();
        setIntensity(this.mIntensity);
    }

    public void setIntensity(float intensity) {
        this.mIntensity = intensity;
        setConvolutionKernel(new float[]{-2.0f * intensity, -intensity, 0.0f, -intensity, TextTrackStyle.DEFAULT_FONT_SCALE, intensity, 0.0f, intensity, 2.0f * intensity});
    }

    public float getIntensity() {
        return this.mIntensity;
    }
}
