package bsoft.com.lib_filter.filter.gpu.vignette;

import android.graphics.PointF;
import android.opengl.GLES20;

public class GPUImageVignetteGaussianBlurFilter extends GPUImageVignetteFilter {
    public static final String VIGNETTING_FRAGMENT_SHADER = " uniform sampler2D inputImageTexture;\n varying highp vec2 textureCoordinate;\n \n uniform lowp vec2 vignetteCenter;\n uniform highp float vignetteStart;\n uniform highp float vignetteEnd;\n uniform highp float blurSizeStart;\n uniform highp float blurSizeEnd;\n \n void main()\n {\n     lowp float d = distance(textureCoordinate, vec2(vignetteCenter.x, vignetteCenter.y));\n     lowp float percent = smoothstep(vignetteStart, vignetteEnd, d);\n     lowp float blurSize = (blurSizeEnd - blurSizeStart) * percent + blurSizeStart;    mediump vec4 sum = vec4(0.0);\n   sum += texture2D(inputImageTexture, vec2(textureCoordinate.x, textureCoordinate.y)) * 0.147761;    sum += texture2D(inputImageTexture, vec2(textureCoordinate.x + blurSize, textureCoordinate.y)) * 0.118318;    sum += texture2D(inputImageTexture, vec2(textureCoordinate.x - blurSize, textureCoordinate.y)) * 0.118318;    sum += texture2D(inputImageTexture, vec2(textureCoordinate.x, textureCoordinate.y + blurSize)) * 0.118318;    sum += texture2D(inputImageTexture, vec2(textureCoordinate.x, textureCoordinate.y - blurSize)) * 0.118318;    sum += texture2D(inputImageTexture, vec2(textureCoordinate.x + blurSize, textureCoordinate.y + blurSize)) * 0.0947416;    sum += texture2D(inputImageTexture, vec2(textureCoordinate.x + blurSize, textureCoordinate.y - blurSize)) * 0.0947416;    sum += texture2D(inputImageTexture, vec2(textureCoordinate.x - blurSize, textureCoordinate.y + blurSize)) * 0.0947416;    sum += texture2D(inputImageTexture, vec2(textureCoordinate.x - blurSize, textureCoordinate.y - blurSize)) * 0.0947416;     gl_FragColor = sum;\n }";
    private float mBlurSizeEnd;
    private int mBlurSizeEndLocation;
    private float mBlurSizeStart;
    private int mBlurSizeStartLocation;

    public GPUImageVignetteGaussianBlurFilter() {
        this(new PointF(), 0.3f, -0.3f, 0.3f, 0.75f);
    }

    public GPUImageVignetteGaussianBlurFilter(PointF vignetteCenter, float blurSizeStart, float blurSizeEnd, float vignetteStart, float vignetteEnd) {
        super(VIGNETTING_FRAGMENT_SHADER, vignetteCenter, vignetteStart, vignetteEnd);
        this.mBlurSizeStart = blurSizeStart;
        this.mBlurSizeEnd = blurSizeEnd;
    }

    public void onInit() {
        super.onInit();
        this.mBlurSizeStartLocation = GLES20.glGetUniformLocation(getProgram(), "blurSizeStart");
        this.mBlurSizeEndLocation = GLES20.glGetUniformLocation(getProgram(), "blurSizeEnd");
        setBlurSizeStart(this.mBlurSizeStart);
        setBlurSizeEnd(this.mBlurSizeEnd);
    }

    public void setBlurSizeStart(float blurSizeStart) {
        this.mBlurSizeStart = blurSizeStart;
        setFloat(this.mBlurSizeStartLocation, this.mBlurSizeStart);
    }

    public void setBlurSizeEnd(float blurSizeEnd) {
        this.mBlurSizeEnd = blurSizeEnd;
        setFloat(this.mBlurSizeEndLocation, this.mBlurSizeEnd);
    }
}
