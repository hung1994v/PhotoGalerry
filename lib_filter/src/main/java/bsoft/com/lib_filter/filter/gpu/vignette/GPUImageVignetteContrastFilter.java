package bsoft.com.lib_filter.filter.gpu.vignette;

import android.graphics.PointF;
import android.opengl.GLES20;

import bsoft.com.lib_filter.filter.gpu.father.GPUImageFilter;


public class GPUImageVignetteContrastFilter extends GPUImageFilter {
    public static final String VIGNETTING_FRAGMENT_SHADER = " uniform sampler2D inputImageTexture;\n varying highp vec2 textureCoordinate;\n \n uniform lowp vec2 vignetteCenter;\n uniform highp float vignetteStart;\n uniform highp float vignetteEnd;\n uniform highp float contrastStart;\n uniform highp float contrastEnd;\n \n void main()\n {\n     lowp float d = distance(textureCoordinate, vec2(vignetteCenter.x, vignetteCenter.y));\n     lowp float percent = smoothstep(vignetteStart, vignetteEnd, d);\n     lowp float contrast = (contrastEnd - contrastStart) * percent + contrastStart;     mediump vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     gl_FragColor = vec4(((textureColor.rgb - vec3(0.5)) * contrast + vec3(0.5)), textureColor.w);\n }";
    private float mContrastEnd;
    private int mContrastEndLocation;
    private float mContrastStart;
    private int mContrastStartLocation;
    private PointF mVignetteCenter;
    private int mVignetteCenterLocation;
    private float mVignetteEnd;
    private int mVignetteEndLocation;
    private float mVignetteStart;
    private int mVignetteStartLocation;

    public GPUImageVignetteContrastFilter() {
        this(new PointF(), 0.3f, -0.3f, 0.3f, 0.75f);
    }

    public GPUImageVignetteContrastFilter(PointF vignetteCenter, float contrastStart, float contrastEnd, float vignetteStart, float vignetteEnd) {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, VIGNETTING_FRAGMENT_SHADER);
        this.mVignetteCenter = vignetteCenter;
        this.mVignetteStart = vignetteStart;
        this.mVignetteEnd = vignetteEnd;
        this.mContrastStart = contrastStart;
        this.mContrastEnd = contrastEnd;
    }

    public void onInit() {
        super.onInit();
        this.mVignetteCenterLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteCenter");
        this.mVignetteStartLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteStart");
        this.mVignetteEndLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteEnd");
        this.mContrastStartLocation = GLES20.glGetUniformLocation(getProgram(), "contrastStart");
        this.mContrastEndLocation = GLES20.glGetUniformLocation(getProgram(), "contrastEnd");
        setVignetteCenter(this.mVignetteCenter);
        setVignetteStart(this.mVignetteStart);
        setVignetteEnd(this.mVignetteEnd);
        setContrastStart(this.mContrastStart);
        setContrastEnd(this.mContrastEnd);
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

    public void setContrastStart(float contrastStart) {
        this.mContrastStart = contrastStart;
        setFloat(this.mContrastStartLocation, this.mContrastStart);
    }

    public void setContrastEnd(float contrastEnd) {
        this.mContrastEnd = contrastEnd;
        setFloat(this.mContrastEndLocation, this.mContrastEnd);
    }
}
