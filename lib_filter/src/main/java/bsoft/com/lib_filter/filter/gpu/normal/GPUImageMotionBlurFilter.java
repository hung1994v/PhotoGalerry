package bsoft.com.lib_filter.filter.gpu.normal;

import android.opengl.GLES20;

import bsoft.com.lib_filter.filter.gpu.father.GPUImageFilter;


public class GPUImageMotionBlurFilter extends GPUImageFilter {
    public static final String BLUR_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n \n uniform sampler2D inputImageTexture;\n uniform lowp float bluramount;\n \n void main()\n {\n     lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     \n     mediump vec2 tv = (vec2(0.8, 0.8)-vec2(0.5, 0.5))*2.0;     mediump float alphamul = clamp( 1.0-sqrt( tv.x*tv.x + tv.y*tv.y ), 0.0, 1.0);     gl_FragColor = vec4( texture2D( inputImageTexture, textureCoordinate, 0.3).rgb, 0.7*textureColor.a ); }";
    private float mBlurAmount;
    private int mBlurAmountLocation;

    public GPUImageMotionBlurFilter() {
        this(0.0f);
    }

    public GPUImageMotionBlurFilter(float bluramount) {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, BLUR_FRAGMENT_SHADER);
        this.mBlurAmount = bluramount;
    }

    public void onInit() {
        super.onInit();
        this.mBlurAmountLocation = GLES20.glGetUniformLocation(getProgram(), "bluramount");
    }

    public void onInitialized() {
        super.onInitialized();
        setBlurAmount(this.mBlurAmount);
    }

    public void setBlurAmount(float blurAmount) {
        this.mBlurAmount = blurAmount;
        setFloat(this.mBlurAmountLocation, this.mBlurAmount);
    }
}
