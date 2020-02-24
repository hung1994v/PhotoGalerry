package bsoft.com.lib_filter.filter.gpu.newfilter;

import android.annotation.TargetApi;
import android.opengl.GLES20;

import bsoft.com.lib_filter.filter.gpu.father.GPUImageFilter;


public class GPUImageNewFisheyeFilter extends GPUImageFilter {
    private static final String FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n\nuniform sampler2D inputImageTexture;\n\nuniform highp float aspectRatio;\nuniform highp float radius;\nuniform highp float scale;\n\nvoid main()\n {\nhighp vec2 center = vec2(0.5, 0.5);     highp vec2 textureCoordinateToUse = textureCoordinate;\n     highp float dist = distance(center, textureCoordinate);\n     textureCoordinateToUse -= center;\n     if (dist < radius)\n     {\n         highp float percent = (1.0 + dist * dist) * radius * 2.0 / 3.0;\n         textureCoordinateToUse = textureCoordinateToUse * percent;\n     }\n     textureCoordinateToUse += center;\n\n     gl_FragColor = texture2D(inputImageTexture, textureCoordinateToUse);\n\n }";
    private static final String VERTEX_SHADER = "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\nuniform mat4 transformMatrix;\nvarying vec2 textureCoordinate;\nvoid main()\n{\n  gl_Position = transformMatrix * vec4(position.xyz, 1.0);\n  textureCoordinate = inputTextureCoordinate.xy;\n}";
    private float mCurvature = 0.1f;
    private int mCurvatureLocation;
    private float mRadius = 0.3f;
    private int mRadiusLocation;

    public GPUImageNewFisheyeFilter() {
        super(VERTEX_SHADER, FRAGMENT_SHADER);
    }

    public void onInit() {
        super.onInit();
        this.mRadiusLocation = GLES20.glGetUniformLocation(getProgram(), "radius");
        this.mCurvatureLocation = GLES20.glGetUniformLocation(getProgram(), "signcurvature");
    }

    @TargetApi(14)
    public void onInitialized() {
        super.onInitialized();
        setmRadius(this.mRadius);
        setmCurvature(this.mCurvature);
    }

    public void setmRadius(float mRadius) {
        this.mRadius = mRadius;
        setFloat(this.mRadiusLocation, mRadius);
    }

    public void setmCurvature(float mCurvature) {
        this.mCurvature = mCurvature;
        setFloat(this.mCurvatureLocation, mCurvature);
    }

    @TargetApi(14)
    public void onOutputSizeChanged(int width, int height) {
        super.onOutputSizeChanged(width, height);
    }
}
