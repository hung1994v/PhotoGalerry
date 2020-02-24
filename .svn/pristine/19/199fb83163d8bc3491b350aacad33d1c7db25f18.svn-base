package bsoft.com.lib_filter.filter.gpu.vignette;

import android.graphics.PointF;
import android.opengl.GLES20;

import bsoft.com.lib_filter.filter.gpu.father.GPUImageTwoInputFilter;


public class GPUImageVignetteBlendFilter extends GPUImageTwoInputFilter {
    public static final String MULTIPLY_BLEND_FRAGMENT_SHADER = " uniform sampler2D inputImageTexture;\n varying highp vec2 textureCoordinate;\n\n uniform sampler2D inputImageTexture2;\n varying highp vec2 textureCoordinate2;\n \n uniform lowp vec2 vignetteCenter;\n uniform highp float vignetteStart;\n uniform highp float vignetteEnd;\n \n void main()\n {\n     mediump vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     mediump vec4 textureColor2 = texture2D(inputImageTexture2, textureCoordinate2);\n     mediump vec4 textureColor3 = textureColor2 * textureColor + textureColor2 * (1.0 - textureColor.a) + textureColor * (1.0 - textureColor2.a);\n     lowp float d = distance(textureCoordinate, vec2(vignetteCenter.x, vignetteCenter.y));\n     lowp float percent = smoothstep(vignetteStart, vignetteEnd, d);\n     if(vignetteStart<=0.5) percent+=0.5-vignetteStart;     if(percent>1.0) percent=1.0;     if(percent<0.0) percent=0.0;     gl_FragColor = vec4(mix(textureColor.rgb, textureColor3.rgb, percent), 1.0);\n }";
    private PointF mVignetteCenter;
    private int mVignetteCenterLocation;
    private float mVignetteEnd;
    private int mVignetteEndLocation;
    private float mVignetteStart;
    private int mVignetteStartLocation;

    public GPUImageVignetteBlendFilter() {
        this(new PointF(), 0.3f, 0.75f);
    }

    public GPUImageVignetteBlendFilter(PointF vignetteCenter, float vignetteStart, float vignetteEnd) {
        super(MULTIPLY_BLEND_FRAGMENT_SHADER);
        this.mVignetteCenter = vignetteCenter;
        this.mVignetteStart = vignetteStart;
        this.mVignetteEnd = vignetteEnd;
    }

    public void onInit() {
        super.onInit();
        this.mVignetteCenterLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteCenter");
        this.mVignetteStartLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteStart");
        this.mVignetteEndLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteEnd");
        setVignetteCenter(this.mVignetteCenter);
        setVignetteStart(this.mVignetteStart);
        setVignetteEnd(this.mVignetteEnd);
    }

    public void setVignetteCenter(PointF vignetteCenter) {
        this.mVignetteCenter = vignetteCenter;
        setPoint(this.mVignetteCenterLocation, this.mVignetteCenter);
    }

    public void setVignetteStart(float vignetteStart) {
        this.mVignetteStart = vignetteStart;
        setFloat(this.mVignetteStartLocation, this.mVignetteStart);
    }

    public void setVignetteEnd(float vignetteEnd) {
        this.mVignetteEnd = vignetteEnd;
        setFloat(this.mVignetteEndLocation, this.mVignetteEnd);
    }
}
