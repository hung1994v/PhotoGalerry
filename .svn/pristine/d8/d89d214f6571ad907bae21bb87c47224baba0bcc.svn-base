package bsoft.com.lib_filter.filter.gpu.normal;

import android.opengl.GLES20;
import com.google.android.gms.cast.TextTrackStyle;

import bsoft.com.lib_filter.filter.gpu.father.GPUImageFilter;


public class GPUImagePixelationFilter extends GPUImageFilter {
    public static final String PIXELATION_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;uniform sampler2D inputImageTexture;uniform highp float fractionalWidthOfPixel;uniform highp float aspectRatio;void main(){highp vec2 sampleDivisor = vec2(fractionalWidthOfPixel, fractionalWidthOfPixel / aspectRatio);highp vec2 samplePos = textureCoordinate - mod(textureCoordinate, sampleDivisor) + 0.5 * sampleDivisor;gl_FragColor = texture2D(inputImageTexture, samplePos );}";
    private float aspectRatio;
    private int aspectRatioLocation;
    private float fractionalWidthOfPixel;
    private int fractionalWidthOfPixelLocation;
    private boolean isLandscape;
    private boolean isOutBitmap;
    private float showHeight;
    private float showWidth;

    public GPUImagePixelationFilter() {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, PIXELATION_FRAGMENT_SHADER);
        this.fractionalWidthOfPixel = 0.1f;
        this.showWidth = TextTrackStyle.DEFAULT_FONT_SCALE;
        this.showHeight = TextTrackStyle.DEFAULT_FONT_SCALE;
    }

    public GPUImagePixelationFilter(String fragmentShader) {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, fragmentShader);
        this.fractionalWidthOfPixel = 0.1f;
        this.showWidth = TextTrackStyle.DEFAULT_FONT_SCALE;
        this.showHeight = TextTrackStyle.DEFAULT_FONT_SCALE;
    }

    public void onInit() {
        super.onInit();
        this.fractionalWidthOfPixelLocation = GLES20.glGetUniformLocation(getProgram(), "fractionalWidthOfPixel");
        this.aspectRatioLocation = GLES20.glGetUniformLocation(getProgram(), "aspectRatio");
    }

    public void onInitialized() {
        super.onInitialized();
        setFractionalWidthOfPixel(this.fractionalWidthOfPixel);
        adjustAspectRatio();
    }

    public void onOutputSizeChanged(int width, int height) {
        super.onOutputSizeChanged(width, height);
        this.showWidth = (float) width;
        this.showHeight = (float) height;
        adjustAspectRatio();
    }

    public void setFractionalWidthOfPixel(float newValue) {
        this.fractionalWidthOfPixel = newValue;
        setFloat(this.fractionalWidthOfPixelLocation, newValue);
    }

    public void setImageSize(float width, float height) {
        if (width > height) {
            this.isLandscape = true;
        } else {
            this.isLandscape = false;
        }
    }

    private void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
        setFloat(this.aspectRatioLocation, aspectRatio);
    }

    private void adjustAspectRatio() {
        if (this.isOutBitmap) {
            setAspectRatio(this.showHeight / this.showWidth);
        } else if (this.isLandscape) {
            setAspectRatio(this.showWidth / this.showHeight);
        } else {
            setAspectRatio(this.showHeight / this.showWidth);
        }
    }

    public boolean isOutBitmap() {
        return this.isOutBitmap;
    }

    public void setOutBitmap(boolean isOutBitmap) {
        this.isOutBitmap = isOutBitmap;
    }

    @Deprecated
    public void setPixel(float pixel) {
    }
}