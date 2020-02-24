package bsoft.com.lib_filter.filter.gpu.vignette;

import android.graphics.PointF;
import android.opengl.GLES20;
import com.google.android.gms.cast.TextTrackStyle;

import bsoft.com.lib_filter.filter.gpu.father.GPUImageFilter;


public class GPUImageVignetteSharpenFilter extends GPUImageFilter {
    public static final String SHARPEN_VERTEX_SHADER = "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n\nuniform float imageWidthFactor; \nuniform float imageHeightFactor; \n\nvarying vec2 textureCoordinate;\nvarying vec2 leftTextureCoordinate;\nvarying vec2 rightTextureCoordinate; \nvarying vec2 topTextureCoordinate;\nvarying vec2 bottomTextureCoordinate;\n\nvoid main()\n{\n    gl_Position = position;\n    \n    mediump vec2 widthStep = vec2(imageWidthFactor, 0.0);\n    mediump vec2 heightStep = vec2(0.0, imageHeightFactor);\n    \n    textureCoordinate = inputTextureCoordinate.xy;\n    leftTextureCoordinate = inputTextureCoordinate.xy - widthStep;\n    rightTextureCoordinate = inputTextureCoordinate.xy + widthStep;\n    topTextureCoordinate = inputTextureCoordinate.xy + heightStep;     \n    bottomTextureCoordinate = inputTextureCoordinate.xy - heightStep;\n    \n}";
    public static final String VIGNETTING_FRAGMENT_SHADER = " uniform sampler2D inputImageTexture;\n varying highp vec2 textureCoordinate;\nvarying highp vec2 leftTextureCoordinate;\nvarying highp vec2 rightTextureCoordinate; \nvarying highp vec2 topTextureCoordinate;\nvarying highp vec2 bottomTextureCoordinate;\nvarying highp float centerMultiplier;\nvarying highp float edgeMultiplier;\n \n uniform lowp vec2 vignetteCenter;\n uniform highp float vignetteStart;\n uniform highp float vignetteEnd;\n uniform highp float sharpnessStart;\n uniform highp float sharpnessEnd;\n \n uniform lowp float mixturePercent;\n void main()\n {\n     lowp float d = distance(textureCoordinate, vec2(vignetteCenter.x, vignetteCenter.y));\n     lowp float percent = smoothstep(vignetteStart, vignetteEnd, d);\n     lowp float sharpness = (sharpnessEnd - sharpnessStart) * percent + sharpnessStart;   lowp float centerMultiplier = 1.0 + 4.0 * (sharpness*mixturePercent);\n    lowp float edgeMultiplier = sharpness*mixturePercent;\n    mediump vec3 textureColor = texture2D(inputImageTexture, textureCoordinate).rgb;\n    mediump vec3 leftTextureColor = texture2D(inputImageTexture, leftTextureCoordinate).rgb;\n    mediump vec3 rightTextureColor = texture2D(inputImageTexture, rightTextureCoordinate).rgb;\n    mediump vec3 topTextureColor = texture2D(inputImageTexture, topTextureCoordinate).rgb;\n    mediump vec3 bottomTextureColor = texture2D(inputImageTexture, bottomTextureCoordinate).rgb;\n    lowp vec4 textureColor3  = vec4((textureColor * centerMultiplier - (leftTextureColor * edgeMultiplier + rightTextureColor * edgeMultiplier + topTextureColor * edgeMultiplier + bottomTextureColor * edgeMultiplier)), texture2D(inputImageTexture, bottomTextureCoordinate).w);\n    gl_FragColor = textureColor3; }";
    private int mImageHeightFactorLocation;
    private int mImageWidthFactorLocation;
    private float mSharpnessEnd;
    private int mSharpnessEndLocation;
    private float mSharpnessStart;
    private int mSharpnessStartLocation;
    private PointF mVignetteCenter;
    private int mVignetteCenterLocation;
    private float mVignetteEnd;
    private int mVignetteEndLocation;
    private float mVignetteStart;
    private int mVignetteStartLocation;

    public GPUImageVignetteSharpenFilter() {
        this(new PointF(), TextTrackStyle.DEFAULT_FONT_SCALE, -1.0f, 0.3f, 0.75f);
    }

    public GPUImageVignetteSharpenFilter(PointF vignetteCenter, float sharpnessStart, float sharpnessEnd, float vignetteStart, float vignetteEnd) {
        super(SHARPEN_VERTEX_SHADER, VIGNETTING_FRAGMENT_SHADER);
        this.mVignetteCenter = vignetteCenter;
        this.mVignetteStart = vignetteStart;
        this.mVignetteEnd = vignetteEnd;
        this.mSharpnessStart = sharpnessStart;
        this.mSharpnessEnd = sharpnessEnd;
    }

    public void onInit() {
        super.onInit();
        this.mVignetteCenterLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteCenter");
        this.mVignetteStartLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteStart");
        this.mVignetteEndLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteEnd");
        this.mSharpnessStartLocation = GLES20.glGetUniformLocation(getProgram(), "sharpnessStart");
        this.mSharpnessEndLocation = GLES20.glGetUniformLocation(getProgram(), "sharpnessEnd");
        this.mImageWidthFactorLocation = GLES20.glGetUniformLocation(getProgram(), "imageWidthFactor");
        this.mImageHeightFactorLocation = GLES20.glGetUniformLocation(getProgram(), "imageHeightFactor");
        setVignetteCenter(this.mVignetteCenter);
        setVignetteStart(this.mVignetteStart);
        setVignetteEnd(this.mVignetteEnd);
        setSharpnessStart(this.mSharpnessStart);
        setSharpnessEnd(this.mSharpnessEnd);
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

    public void setSharpnessStart(float sharpnessStart) {
        this.mSharpnessStart = sharpnessStart;
        setFloat(this.mSharpnessStartLocation, this.mSharpnessStart);
    }

    public void setSharpnessEnd(float sharpnessEnd) {
        this.mSharpnessEnd = sharpnessEnd;
        setFloat(this.mSharpnessEndLocation, this.mSharpnessEnd);
    }

    public void onOutputSizeChanged(int width, int height) {
        super.onOutputSizeChanged(width, height);
        setFloat(this.mImageWidthFactorLocation, TextTrackStyle.DEFAULT_FONT_SCALE / ((float) width));
        setFloat(this.mImageHeightFactorLocation, TextTrackStyle.DEFAULT_FONT_SCALE / ((float) height));
    }
}
