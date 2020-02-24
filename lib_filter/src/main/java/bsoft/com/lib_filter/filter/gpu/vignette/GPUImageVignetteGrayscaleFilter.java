package bsoft.com.lib_filter.filter.gpu.vignette;

import android.graphics.PointF;
import android.opengl.GLES20;

import bsoft.com.lib_filter.filter.gpu.father.GPUImageFilter;


public class GPUImageVignetteGrayscaleFilter extends GPUImageFilter {
    public static final String VIGNETTING_FRAGMENT_SHADER = " uniform sampler2D inputImageTexture;\n varying highp vec2 textureCoordinate;\n \n uniform lowp vec2 vignetteCenter;\n uniform highp float vignetteStart;\n uniform highp float vignetteEnd;\n uniform highp float saturationStart;\n uniform highp float saturationEnd;\n \n const mediump vec3 luminanceWeighting = vec3(0.2125, 0.7154, 0.0721);\n void main()\n {\n     lowp float d = distance(textureCoordinate, vec2(vignetteCenter.x, vignetteCenter.y));\n     lowp float percent = smoothstep(vignetteStart, vignetteEnd, d);\n     lowp float saturation = (saturationEnd - saturationStart) * percent + saturationStart;     mediump vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n    lowp float luminance = dot(textureColor.rgb, luminanceWeighting);\n    lowp vec3 grayScaleColor = vec3(luminance);\n    gl_FragColor = vec4(mix(grayScaleColor, textureColor.rgb, saturation), textureColor.w);\n }";
    private float mSaturationEnd;
    private int mSaturationEndLocation;
    private float mSaturationStart;
    private int mSaturationStartLocation;
    private PointF mVignetteCenter;
    private int mVignetteCenterLocation;
    private float mVignetteEnd;
    private int mVignetteEndLocation;
    private float mVignetteStart;
    private int mVignetteStartLocation;

    public GPUImageVignetteGrayscaleFilter() {
        this(new PointF(), 0.3f, -0.3f, 0.3f, 0.75f);
    }

    public GPUImageVignetteGrayscaleFilter(PointF vignetteCenter, float saturationStart, float saturationEnd, float vignetteStart, float vignetteEnd) {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, VIGNETTING_FRAGMENT_SHADER);
        this.mVignetteCenter = vignetteCenter;
        this.mVignetteStart = vignetteStart;
        this.mVignetteEnd = vignetteEnd;
        this.mSaturationStart = saturationStart;
        this.mSaturationEnd = saturationEnd;
    }

    public void onInit() {
        super.onInit();
        this.mVignetteCenterLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteCenter");
        this.mVignetteStartLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteStart");
        this.mVignetteEndLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteEnd");
        this.mSaturationStartLocation = GLES20.glGetUniformLocation(getProgram(), "saturationStart");
        this.mSaturationEndLocation = GLES20.glGetUniformLocation(getProgram(), "saturationEnd");
        setVignetteCenter(this.mVignetteCenter);
        setVignetteStart(this.mVignetteStart);
        setVignetteEnd(this.mVignetteEnd);
        setSaturationStart(this.mSaturationStart);
        setSaturationEnd(this.mSaturationEnd);
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

    public void setSaturationStart(float saturationStart) {
        this.mSaturationStart = saturationStart;
        setFloat(this.mSaturationStartLocation, this.mSaturationStart);
    }

    public void setSaturationEnd(float saturationEnd) {
        this.mSaturationEnd = saturationEnd;
        setFloat(this.mSaturationEndLocation, this.mSaturationEnd);
    }
}
