package bsoft.com.lib_filter.filter.gpu.vignette;

import android.graphics.PointF;
import android.opengl.GLES20;
import com.google.android.gms.cast.TextTrackStyle;

import bsoft.com.lib_filter.filter.gpu.father.GPUImageFilter;


public class GPUImageVignetteFilter extends GPUImageFilter {
    public static final String VIGNETTING_FRAGMENT_SHADER = " uniform sampler2D inputImageTexture;\n varying highp vec2 textureCoordinate;\n \n uniform lowp vec2 vignetteCenter;\n uniform lowp float vignetteInvert;\n uniform highp float vignetteStart;\n uniform highp float vignetteEnd;\n uniform lowp vec3 vignetteColor;\n uniform lowp float mixturePercent;\n \n void main()\n {\n     highp vec3 src = texture2D(inputImageTexture, textureCoordinate).rgb;\n     highp float d = distance(textureCoordinate, vec2(vignetteCenter.x, vignetteCenter.y));\n     highp float percent = smoothstep(vignetteStart, vignetteEnd, d);\n     lowp vec4 textureColor3 = vec4(mix(src.xyz, vignetteColor.xyz, percent), 1.0); \n     gl_FragColor =vec4(mix(src.xyz, textureColor3.xyz, textureColor3.w*mixturePercent), 1.0);\n }";
    private Boolean mInvert;
    private int mInvertLocation;
    private PointF mVignetteCenter;
    private int mVignetteCenterLocation;
    private float[] mVignetteColor;
    private int mVignetteColorLocation;
    private float mVignetteEnd;
    private int mVignetteEndLocation;
    private float mVignetteStart;
    private int mVignetteStartLocation;

    public GPUImageVignetteFilter() {
        this(VIGNETTING_FRAGMENT_SHADER, new PointF(0.5f, 0.5f), 0.3f, 0.75f);
    }

    public GPUImageVignetteFilter(String fragmentShader, PointF vignetteCenter, float vignetteStart, float vignetteEnd) {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, fragmentShader);
        this.mInvert = Boolean.valueOf(false);
        this.mVignetteColor = new float[]{0.0f, 0.0f, 0.0f};
        this.mVignetteCenter = vignetteCenter;
        this.mVignetteStart = vignetteStart;
        this.mVignetteEnd = vignetteEnd;
    }

    public GPUImageVignetteFilter(float vignetteEnd) {
        this(VIGNETTING_FRAGMENT_SHADER, new PointF(0.5f, 0.5f), 0.3f, vignetteEnd);
    }

    public GPUImageVignetteFilter(float vignetteEnd, float mixPercent) {
        this(VIGNETTING_FRAGMENT_SHADER, new PointF(0.5f, 0.5f), 0.3f, vignetteEnd);
        setMix(mixPercent);
    }

    public void onInit() {
        super.onInit();
        this.mVignetteCenterLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteCenter");
        this.mVignetteStartLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteStart");
        this.mVignetteEndLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteEnd");
        this.mInvertLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteInvert");
        this.mVignetteColorLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteColor");
        setVignetteCenter(this.mVignetteCenter);
        setVignetteStart(this.mVignetteStart);
        setVignetteEnd(this.mVignetteEnd);
        setInvert(this.mInvert);
        setVignetteColor(this.mVignetteColor);
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

    public void setInvert(Boolean invert) {
        this.mInvert = invert;
        if (this.mInvert.booleanValue()) {
            setFloat(this.mInvertLocation, TextTrackStyle.DEFAULT_FONT_SCALE);
        } else {
            setFloat(this.mInvertLocation, 0.0f);
        }
    }

    public void setVignetteColor(float[] vignetteColor) {
        this.mVignetteColor = vignetteColor;
        setFloatVec3(this.mVignetteColorLocation, vignetteColor);
    }
}
