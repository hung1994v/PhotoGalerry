package bsoft.com.lib_filter.filter.gpu.vignette;

import android.graphics.PointF;
import android.opengl.GLES20;
import com.google.android.gms.cast.TextTrackStyle;

import bsoft.com.lib_filter.filter.gpu.father.GPUImageTwoInputFilter;


public class GPUImageVignetteTwoInputFilter extends GPUImageTwoInputFilter {
    public static final String VIGNETTING_FRAGMENT_SHADER = " uniform sampler2D inputImageTexture;\n varying highp vec2 textureCoordinate;\n \n uniform lowp vec2 vignetteCenter;\n uniform highp float vignetteStart;\n uniform highp float vignetteEnd;\n uniform lowp float vignetteInvert;\n \n void main()\n {\n     /*\n     lowp vec3 rgb = texture2D(inputImageTexture, textureCoordinate).rgb;\n     lowp float d = distance(textureCoordinate, vec2(0.5,0.5));\n     rgb *= (1.0 - smoothstep(vignetteStart, vignetteEnd, d));\n     gl_FragColor = vec4(vec3(rgb),1.0);\n      */\n     \n     lowp vec3 rgb = texture2D(inputImageTexture, textureCoordinate).rgb;\n     lowp float d = distance(textureCoordinate, vec2(vignetteCenter.x, vignetteCenter.y));\n     lowp float percent = smoothstep(vignetteStart, vignetteEnd, d);\n     gl_FragColor = vec4(mix(rgb.x, 0.0, percent), mix(rgb.y, 0.0, percent), mix(rgb.z, 0.0, percent), 1.0);\n }";
    private Boolean mInvert;
    private int mInvertLocation;
    private PointF mVignetteCenter;
    private int mVignetteCenterLocation;
    private float mVignetteEnd;
    private int mVignetteEndLocation;
    private float mVignetteStart;
    private int mVignetteStartLocation;

    public GPUImageVignetteTwoInputFilter() {
        this(VIGNETTING_FRAGMENT_SHADER, new PointF(), 0.3f, 0.75f);
    }

    public GPUImageVignetteTwoInputFilter(String fragmentShader, PointF vignetteCenter, float vignetteStart, float vignetteEnd) {
        super(fragmentShader);
        this.mInvert = Boolean.valueOf(false);
        this.mVignetteCenter = vignetteCenter;
        this.mVignetteStart = vignetteStart;
        this.mVignetteEnd = vignetteEnd;
    }

    public void onInit() {
        super.onInit();
        this.mVignetteCenterLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteCenter");
        this.mVignetteStartLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteStart");
        this.mVignetteEndLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteEnd");
        this.mInvertLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteInvert");
        setVignetteCenter(this.mVignetteCenter);
        setVignetteStart(this.mVignetteStart);
        setVignetteEnd(this.mVignetteEnd);
        setInvert(Boolean.valueOf(false));
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
}
