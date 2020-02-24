package bsoft.com.lib_filter.filter.gpu.vignette;

import android.graphics.PointF;
import android.opengl.GLES20;

import bsoft.com.lib_filter.filter.gpu.father.GPUImageFilter;


public class GPUImageVignetteRGBFilter extends GPUImageFilter {
    public static final String VIGNETTING_FRAGMENT_SHADER = " uniform sampler2D inputImageTexture;\n varying highp vec2 textureCoordinate;\n \n uniform lowp vec2 vignetteCenter;\n uniform highp float vignetteStart;\n uniform highp float vignetteEnd;\n uniform highp float gammaStart;\n uniform highp float gammaEnd;\n \n void main()\n {\n     lowp float d = distance(textureCoordinate, vec2(vignetteCenter.x, vignetteCenter.y));\n     lowp float percent = smoothstep(vignetteStart, vignetteEnd, d);\n     lowp float gamma = (gammaEnd - gammaStart) * percent + gammaStart;     mediump vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     gl_FragColor = vec4(pow(textureColor.rgb, vec3(gamma)), textureColor.w);\n }";
    private float mGammaEnd;
    private int mGammaEndLocation;
    private float mGammaStart;
    private int mGammaStartLocation;
    private PointF mVignetteCenter;
    private int mVignetteCenterLocation;
    private float mVignetteEnd;
    private int mVignetteEndLocation;
    private float mVignetteStart;
    private int mVignetteStartLocation;

    public GPUImageVignetteRGBFilter() {
        this(new PointF(), 0.3f, -0.3f, 0.3f, 0.75f);
    }

    public GPUImageVignetteRGBFilter(PointF vignetteCenter, float gammaStart, float gammaEnd, float vignetteStart, float vignetteEnd) {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, " uniform sampler2D inputImageTexture;\n varying highp vec2 textureCoordinate;\n \n uniform lowp vec2 vignetteCenter;\n uniform highp float vignetteStart;\n uniform highp float vignetteEnd;\n uniform highp float gammaStart;\n uniform highp float gammaEnd;\n \n void main()\n {\n     lowp float d = distance(textureCoordinate, vec2(vignetteCenter.x, vignetteCenter.y));\n     lowp float percent = smoothstep(vignetteStart, vignetteEnd, d);\n     lowp float gamma = (gammaEnd - gammaStart) * percent + gammaStart;     mediump vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     gl_FragColor = vec4(pow(textureColor.rgb, vec3(gamma)), textureColor.w);\n }");
        this.mVignetteCenter = vignetteCenter;
        this.mVignetteStart = vignetteStart;
        this.mVignetteEnd = vignetteEnd;
        this.mGammaStart = gammaStart;
        this.mGammaEnd = gammaEnd;
    }

    public void onInit() {
        super.onInit();
        this.mVignetteCenterLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteCenter");
        this.mVignetteStartLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteStart");
        this.mVignetteEndLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteEnd");
        this.mGammaStartLocation = GLES20.glGetUniformLocation(getProgram(), "gammaStart");
        this.mGammaEndLocation = GLES20.glGetUniformLocation(getProgram(), "gammaEnd");
        setVignetteCenter(this.mVignetteCenter);
        setVignetteStart(this.mVignetteStart);
        setVignetteEnd(this.mVignetteEnd);
        setGammaStart(this.mGammaStart);
        setGammaEnd(this.mGammaEnd);
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

    public void setGammaStart(float gammaStart) {
        this.mGammaStart = gammaStart;
        setFloat(this.mGammaStartLocation, this.mGammaStart);
    }

    public void setGammaEnd(float gammaEnd) {
        this.mGammaEnd = gammaEnd;
        setFloat(this.mGammaEndLocation, this.mGammaEnd);
    }
}
