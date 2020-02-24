package bsoft.com.lib_filter.filter.gpu.newfilter;

import android.graphics.PointF;
import android.opengl.GLES20;
import com.google.android.gms.cast.TextTrackStyle;

import bsoft.com.lib_filter.filter.gpu.father.GPUImageFilter;


public class GPUImageBulgeDistortionFilter extends GPUImageFilter {
    public static final String BULGE_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n\nuniform sampler2D inputImageTexture;\n\nuniform highp float aspectRatio;\nuniform highp vec2 center;\nuniform highp float radius;\nuniform highp float scale;\n\nvoid main()\n {\n     highp vec2 textureCoordinateToUse = textureCoordinate;\n     highp float dist = distance(center, textureCoordinate);\n     textureCoordinateToUse -= center;\n     if (dist < radius)\n     {\n         highp float percent = (1.0 + dist * dist * scale) * radius * 2.0 / 3.0;\n         textureCoordinateToUse = textureCoordinateToUse * percent;\n     }\n     textureCoordinateToUse += center;\n\n     gl_FragColor = texture2D(inputImageTexture, textureCoordinateToUse);\n\n }";
    private float mAspectRatio;
    private int mAspectRatioLocation;
    private PointF mCenter;
    private int mCenterLocation;
    private float mRadius;
    private int mRadiusLocation;
    private float mScale;
    private int mScaleLocation;

    public GPUImageBulgeDistortionFilter() {
        this(TextTrackStyle.DEFAULT_FONT_SCALE, 1.5f, new PointF(0.5f, 0.5f));
    }

    public GPUImageBulgeDistortionFilter(float radius, float scale, PointF center) {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, BULGE_FRAGMENT_SHADER);
        this.mRadius = radius;
        this.mScale = scale;
        this.mCenter = center;
    }

    public GPUImageBulgeDistortionFilter(String fragment_shader) {
        this(fragment_shader, 0.25f, 0.5f, new PointF(0.5f, 0.5f));
    }

    public GPUImageBulgeDistortionFilter(String fragment_shader, float radius, float scale, PointF center) {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, fragment_shader);
        this.mRadius = radius;
        this.mScale = scale;
        this.mCenter = center;
    }

    public void onInit() {
        super.onInit();
        this.mScaleLocation = GLES20.glGetUniformLocation(getProgram(), "scale");
        this.mRadiusLocation = GLES20.glGetUniformLocation(getProgram(), "radius");
        this.mCenterLocation = GLES20.glGetUniformLocation(getProgram(), "center");
        this.mAspectRatioLocation = GLES20.glGetUniformLocation(getProgram(), "aspectRatio");
    }

    public void onInitialized() {
        super.onInitialized();
        setRadius(this.mRadius);
        setScale(this.mScale);
        setCenter(this.mCenter);
    }

    public void onOutputSizeChanged(int width, int height) {
        this.mAspectRatio = ((float) height) / ((float) width);
        setAspectRatio(this.mAspectRatio);
        super.onOutputSizeChanged(width, height);
    }

    private void setAspectRatio(float aspectRatio) {
        this.mAspectRatio = aspectRatio;
        setFloat(this.mAspectRatioLocation, aspectRatio);
    }

    public void setRadius(float radius) {
        this.mRadius = radius;
        setFloat(this.mRadiusLocation, radius);
    }

    public void setScale(float scale) {
        this.mScale = scale;
        setFloat(this.mScaleLocation, scale);
    }

    public void setCenter(PointF center) {
        this.mCenter = center;
        setPoint(this.mCenterLocation, center);
    }
}
