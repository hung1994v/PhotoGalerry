package bsoft.com.lib_filter.filter.gpu.adjust;

import android.opengl.GLES20;
import com.google.android.gms.cast.TextTrackStyle;

import bsoft.com.lib_filter.filter.gpu.father.GPUImageFilter;


public class GPUImageRB3DFilter extends GPUImageFilter {
    public static final String RB3D_FRAGMENT_SHADER = " varying highp vec2 textureCoordinate;\n uniform sampler2D inputImageTexture;\n uniform highp float xOffset;\n \n void main()\n {\n     highp vec2 textureCoordinateR = vec2(textureCoordinate.x + xOffset, textureCoordinate.y);\n     highp vec2 textureCoordinateB = vec2(textureCoordinate.x - xOffset, textureCoordinate.y);\n     highp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     highp float redValue = texture2D(inputImageTexture, textureCoordinateR).r;\n     highp float blueValue = texture2D(inputImageTexture, textureCoordinateB).b;\n     gl_FragColor = vec4(redValue,textureColor.g,blueValue,1.0);\n } ";
    private float mXOffset;
    private int mXOffsetLocation;

    public GPUImageRB3DFilter() {
        this(TextTrackStyle.DEFAULT_FONT_SCALE);
    }

    public GPUImageRB3DFilter(float offset) {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, RB3D_FRAGMENT_SHADER);
        this.mXOffset = offset;
    }

    public void onInit() {
        super.onInit();
        this.mXOffsetLocation = GLES20.glGetUniformLocation(getProgram(), "xOffset");
    }

    public void onInitialized() {
        super.onInitialized();
        setOffset(this.mXOffset);
    }

    public void setOffset(float offset) {
        this.mXOffset = offset;
        setFloat(this.mXOffsetLocation, this.mXOffset);
    }
}
