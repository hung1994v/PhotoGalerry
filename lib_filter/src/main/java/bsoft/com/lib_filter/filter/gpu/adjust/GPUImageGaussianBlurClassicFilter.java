package bsoft.com.lib_filter.filter.gpu.adjust;

import android.opengl.GLES20;

import bsoft.com.lib_filter.filter.gpu.father.GPUImageFilter;


public class GPUImageGaussianBlurClassicFilter extends GPUImageFilter {
    public static final String BLUR_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n \n uniform sampler2D inputImageTexture;\n uniform lowp float blurSize;\n uniform lowp float mixturePercent;\n const highp vec4 kRGBToYPrime = vec4 (0.299, 0.587, 0.114, 0.0); void main() {     lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     mediump vec4 sum = vec4(0.0);      lowp float YPrime = dot(textureColor, kRGBToYPrime);  if(YPrime > 0.4){ sum = textureColor;}   gl_FragColor = vec4(mix(textureColor.rgb, sum.rgb, textureColor.a*mixturePercent), textureColor.a);\n }";
    private float mBlurSize;
    private int mBlurSizeLocation;

    public GPUImageGaussianBlurClassicFilter() {
        this(0.0017361111f);
    }

    public GPUImageGaussianBlurClassicFilter(float blurSize) {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, BLUR_FRAGMENT_SHADER);
        this.mBlurSize = blurSize;
    }

    public void onInit() {
        super.onInit();
        this.mBlurSizeLocation = GLES20.glGetUniformLocation(getProgram(), "blurSize");
    }

    public void onInitialized() {
        super.onInitialized();
        setBlurSize(this.mBlurSize);
    }

    public void setBlurSize(float blurSize) {
        this.mBlurSize = blurSize;
        setFloat(this.mBlurSizeLocation, this.mBlurSize);
    }
}
