package bsoft.com.lib_filter.filter.gpu.adjust;

import android.opengl.GLES20;
import com.google.android.gms.cast.TextTrackStyle;

import bsoft.com.lib_filter.filter.gpu.father.GPUImageFilter;


public class GPUImageContrastFilter extends GPUImageFilter {
    public static final String CONTRAST_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n \n uniform sampler2D inputImageTexture;\n uniform lowp float contrast;\n \n void main()\n {\n     lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     \n     gl_FragColor = vec4(((textureColor.rgb - vec3(0.5)) * contrast + vec3(0.5)), textureColor.w);\n }";
    private float mContrast;
    private int mContrastLocation;

    public GPUImageContrastFilter() {
        this(TextTrackStyle.DEFAULT_FONT_SCALE);
    }

    public GPUImageContrastFilter(float contrast) {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, CONTRAST_FRAGMENT_SHADER);
        this.mContrast = contrast;
    }

    public void onInit() {
        super.onInit();
        this.mContrastLocation = GLES20.glGetUniformLocation(getProgram(), "contrast");
    }

    public void onInitialized() {
        super.onInitialized();
        setContrast(this.mContrast);
    }

    public void setContrast(float contrast) {
        this.mContrast = contrast;
        setFloat(this.mContrastLocation, this.mContrast);
    }
}
