package bsoft.com.lib_filter.filter.gpu.father;

import android.opengl.GLES20;
import com.google.android.gms.cast.TextTrackStyle;

public class GPUImageTwoPassTextureSamplingFilter extends GPUImageTwoPassFilter {
    public GPUImageTwoPassTextureSamplingFilter(String firstVertexShader, String firstFragmentShader, String secondVertexShader, String secondFragmentShader) {
        super(firstVertexShader, firstFragmentShader, secondVertexShader, secondFragmentShader);
    }

    public void onInit() {
        super.onInit();
        initTexelOffsets();
    }

    protected void initTexelOffsets() {
        float ratio = getHorizontalTexelOffsetRatio();
        GPUImageFilter filter = mFilters.get(0);
        int texelWidthOffsetLocation = GLES20.glGetUniformLocation(filter.getProgram(), "texelWidthOffset");
        int texelHeightOffsetLocation = GLES20.glGetUniformLocation(filter.getProgram(), "texelHeightOffset");
        filter.setFloat(texelWidthOffsetLocation, ratio / ((float) this.mOutputWidth));
        filter.setFloat(texelHeightOffsetLocation, 0.0f);
        ratio = getVerticalTexelOffsetRatio();
        filter = (GPUImageFilter) this.mFilters.get(1);
        texelWidthOffsetLocation = GLES20.glGetUniformLocation(filter.getProgram(), "texelWidthOffset");
        texelHeightOffsetLocation = GLES20.glGetUniformLocation(filter.getProgram(), "texelHeightOffset");
        filter.setFloat(texelWidthOffsetLocation, 0.0f);
        filter.setFloat(texelHeightOffsetLocation, ratio / ((float) this.mOutputHeight));
    }

    public void onOutputSizeChanged(int width, int height) {
        super.onOutputSizeChanged(width, height);
        initTexelOffsets();
    }

    public float getVerticalTexelOffsetRatio() {
        return TextTrackStyle.DEFAULT_FONT_SCALE;
    }

    public float getHorizontalTexelOffsetRatio() {
        return TextTrackStyle.DEFAULT_FONT_SCALE;
    }
}
