package bsoft.com.lib_filter.filter.gpu.adjust;

import android.opengl.GLES20;
import com.google.android.gms.cast.TextTrackStyle;

import bsoft.com.lib_filter.filter.gpu.father.GPUImageFilter;


public class GPUImageSoftLightFilter extends GPUImageFilter {
    public static final String SCREEN_BLEND_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n uniform sampler2D inputImageTexture;\n \n uniform lowp float blurSize;\n uniform lowp float contrast;\n uniform lowp float brightness;\n \n void main()\n {\n    mediump vec4 sum = vec4(0.0);    sum += texture2D(inputImageTexture, vec2(textureCoordinate.x, textureCoordinate.y)) * 41.0/273.0;    sum += texture2D(inputImageTexture, vec2(textureCoordinate.x + blurSize, textureCoordinate.y)) * 26.0/273.0;    sum += texture2D(inputImageTexture, vec2(textureCoordinate.x - blurSize, textureCoordinate.y)) * 26.0/273.0;    sum += texture2D(inputImageTexture, vec2(textureCoordinate.x, textureCoordinate.y + blurSize)) * 26.0/273.0;    sum += texture2D(inputImageTexture, vec2(textureCoordinate.x, textureCoordinate.y - blurSize)) * 26.0/273.0;    sum += texture2D(inputImageTexture, vec2(textureCoordinate.x + blurSize, textureCoordinate.y + blurSize)) * 16.0/273.0;    sum += texture2D(inputImageTexture, vec2(textureCoordinate.x + blurSize, textureCoordinate.y - blurSize)) * 16.0/273.0;    sum += texture2D(inputImageTexture, vec2(textureCoordinate.x - blurSize, textureCoordinate.y + blurSize)) * 16.0/273.0;    sum += texture2D(inputImageTexture, vec2(textureCoordinate.x - blurSize, textureCoordinate.y - blurSize)) * 16.0/273.0;    sum += texture2D(inputImageTexture, vec2(textureCoordinate.x + 2.0*blurSize, textureCoordinate.y)) * 7.0/273.0;    sum += texture2D(inputImageTexture, vec2(textureCoordinate.x - 2.0*blurSize, textureCoordinate.y)) * 7.0/273.0;    sum += texture2D(inputImageTexture, vec2(textureCoordinate.x, textureCoordinate.y + 2.0*blurSize)) * 7.0/273.0;    sum += texture2D(inputImageTexture, vec2(textureCoordinate.x, textureCoordinate.y - 2.0*blurSize)) * 7.0/273.0;    sum += texture2D(inputImageTexture, vec2(textureCoordinate.x + blurSize, textureCoordinate.y - 2.0*blurSize)) * 4.0/273.0;    sum += texture2D(inputImageTexture, vec2(textureCoordinate.x + blurSize, textureCoordinate.y + 2.0*blurSize)) * 4.0/273.0;    sum += texture2D(inputImageTexture, vec2(textureCoordinate.x + 2.0*blurSize, textureCoordinate.y - blurSize)) * 4.0/273.0;    sum += texture2D(inputImageTexture, vec2(textureCoordinate.x + 2.0*blurSize, textureCoordinate.y + blurSize)) * 4.0/273.0;    sum += texture2D(inputImageTexture, vec2(textureCoordinate.x - blurSize, textureCoordinate.y - 2.0*blurSize)) * 4.0/273.0;    sum += texture2D(inputImageTexture, vec2(textureCoordinate.x - blurSize, textureCoordinate.y + 2.0*blurSize)) * 4.0/273.0;    sum += texture2D(inputImageTexture, vec2(textureCoordinate.x - 2.0*blurSize, textureCoordinate.y - blurSize)) * 4.0/273.0;    sum += texture2D(inputImageTexture, vec2(textureCoordinate.x - 2.0*blurSize, textureCoordinate.y + blurSize)) * 4.0/273.0;    sum += texture2D(inputImageTexture, vec2(textureCoordinate.x + 2.0*blurSize, textureCoordinate.y - 2.0*blurSize)) * 1.0/273.0;    sum += texture2D(inputImageTexture, vec2(textureCoordinate.x + 2.0*blurSize, textureCoordinate.y + 2.0*blurSize)) * 1.0/273.0;    sum += texture2D(inputImageTexture, vec2(textureCoordinate.x - 2.0*blurSize, textureCoordinate.y - 2.0*blurSize)) * 1.0/273.0;    sum += texture2D(inputImageTexture, vec2(textureCoordinate.x - 2.0*blurSize, textureCoordinate.y + 2.0*blurSize)) * 1.0/273.0;     sum = vec4((sum.rgb + vec3(brightness)), sum.w);\n     sum = vec4(((sum.rgb - vec3(0.5)) * contrast + vec3(0.5)), sum.w);\n     mediump vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     mediump vec4 textureColor2 = sum;\n     mediump vec4 whiteColor = vec4(1.0);\n     gl_FragColor = whiteColor - ((whiteColor - textureColor2) * (whiteColor - textureColor));\n }";
    private float mBlurSize;
    private int mBlurSizeLocation;
    private float mBrightness;
    private int mBrightnessLocation;
    private float mContrast;
    private int mContrastLocation;

    public GPUImageSoftLightFilter() {
        this(0.0f, 0.0f, TextTrackStyle.DEFAULT_FONT_SCALE);
    }

    public GPUImageSoftLightFilter(float blurSize, float brightness, float contrast) {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, SCREEN_BLEND_FRAGMENT_SHADER);
        this.mBlurSize = blurSize;
        this.mBrightness = brightness;
        this.mContrast = contrast;
    }

    public void onInit() {
        super.onInit();
        this.mBlurSizeLocation = GLES20.glGetUniformLocation(getProgram(), "blurSize");
        this.mBrightnessLocation = GLES20.glGetUniformLocation(getProgram(), "brightness");
        this.mContrastLocation = GLES20.glGetUniformLocation(getProgram(), "contrast");
    }

    public void onInitialized() {
        super.onInitialized();
        this.mBlurSizeLocation = GLES20.glGetUniformLocation(getProgram(), "blurSize");
        this.mBrightnessLocation = GLES20.glGetUniformLocation(getProgram(), "brightness");
        this.mContrastLocation = GLES20.glGetUniformLocation(getProgram(), "contrast");
        setBlurSize(this.mBlurSize);
        setBrightness(this.mBrightness);
        setContrast(this.mContrast);
    }

    public void setBlurSize(float blurSize) {
        this.mBlurSize = blurSize;
        setFloat(this.mBlurSizeLocation, this.mBlurSize);
    }

    public void setBrightness(float brightness) {
        this.mBrightness = brightness;
        setFloat(this.mBrightnessLocation, this.mBrightness);
    }

    public void setContrast(float contrast) {
        this.mContrast = contrast;
        setFloat(this.mContrastLocation, this.mContrast);
    }
}
