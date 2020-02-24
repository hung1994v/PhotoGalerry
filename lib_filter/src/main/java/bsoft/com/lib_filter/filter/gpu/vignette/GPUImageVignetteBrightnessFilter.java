package bsoft.com.lib_filter.filter.gpu.vignette;

import android.graphics.PointF;
import android.opengl.GLES20;

import bsoft.com.lib_filter.filter.gpu.father.GPUImageFilter;


public class GPUImageVignetteBrightnessFilter extends GPUImageFilter {
    public static final String VIGNETTING_FRAGMENT_SHADER = " uniform sampler2D inputImageTexture;\n varying highp vec2 textureCoordinate;\n \n uniform highp vec2 vignetteCenter;\n uniform highp float vignetteStart;\n uniform highp float vignetteEnd;\n uniform highp float brightnessStart;\n uniform highp float brightnessEnd;\n \n uniform lowp float mixturePercent;\n void main()\n {\n     highp float d = distance(textureCoordinate, vec2(vignetteCenter.x, vignetteCenter.y));\n     highp float percent = smoothstep(vignetteStart, vignetteEnd, d);\n     highp float brightness = (brightnessEnd - brightnessStart) * percent + brightnessStart;     highp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     highp vec4 textureColor3 = vec4((textureColor.rgb + vec3(brightness)), textureColor.w);\n     gl_FragColor =vec4(mix(textureColor.rgb, textureColor3.rgb, textureColor3.a*mixturePercent), textureColor.a);\n }";
    private float mBrightnessEnd;
    private int mBrightnessEndLocation;
    private float mBrightnessStart;
    private int mBrightnessStartLocation;
    private PointF mVignetteCenter;
    private int mVignetteCenterLocation;
    private float mVignetteEnd;
    private int mVignetteEndLocation;
    private float mVignetteStart;
    private int mVignetteStartLocation;

    public GPUImageVignetteBrightnessFilter() {
        this(new PointF(), 0.3f, -0.3f, 0.3f, 0.75f);
    }

    public GPUImageVignetteBrightnessFilter(PointF vignetteCenter, float brightnessStart, float brightnessEnd, float vignetteStart, float vignetteEnd) {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, VIGNETTING_FRAGMENT_SHADER);
        this.mVignetteCenter = vignetteCenter;
        this.mVignetteStart = vignetteStart;
        this.mVignetteEnd = vignetteEnd;
        this.mBrightnessStart = brightnessStart;
        this.mBrightnessEnd = brightnessEnd;
    }

    public void onInit() {
        super.onInit();
        this.mVignetteCenterLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteCenter");
        this.mVignetteStartLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteStart");
        this.mVignetteEndLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteEnd");
        this.mBrightnessStartLocation = GLES20.glGetUniformLocation(getProgram(), "brightnessStart");
        this.mBrightnessEndLocation = GLES20.glGetUniformLocation(getProgram(), "brightnessEnd");
        setVignetteCenter(this.mVignetteCenter);
        setVignetteStart(this.mVignetteStart);
        setVignetteEnd(this.mVignetteEnd);
        setBrightnessStart(this.mBrightnessStart);
        setBrightnessEnd(this.mBrightnessEnd);
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

    public void setBrightnessStart(float brightnessStart) {
        this.mBrightnessStart = brightnessStart;
        setFloat(this.mBrightnessStartLocation, this.mBrightnessStart);
    }

    public void setBrightnessEnd(float brightnessEnd) {
        this.mBrightnessEnd = brightnessEnd;
        setFloat(this.mBrightnessEndLocation, this.mBrightnessEnd);
    }
}
