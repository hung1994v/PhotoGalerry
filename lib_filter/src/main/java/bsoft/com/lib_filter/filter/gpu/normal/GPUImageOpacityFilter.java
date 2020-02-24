package bsoft.com.lib_filter.filter.gpu.normal;

import android.opengl.GLES20;
import com.google.android.gms.cast.TextTrackStyle;

import bsoft.com.lib_filter.filter.gpu.father.GPUImageFilter;


public class GPUImageOpacityFilter extends GPUImageFilter {
    public static final String OPACITY_FRAGMENT_SHADER = "  varying highp vec2 textureCoordinate;\n  \n  uniform sampler2D inputImageTexture;\n  uniform lowp float opacity;\n  \n  void main()\n  {\n      lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n      \n      gl_FragColor = vec4(textureColor.rgb, textureColor.a * opacity);\n  }\n";
    private float mOpacity;
    private int mOpacityLocation;

    public GPUImageOpacityFilter() {
        this(TextTrackStyle.DEFAULT_FONT_SCALE);
    }

    public GPUImageOpacityFilter(float opacity) {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, OPACITY_FRAGMENT_SHADER);
        this.mOpacity = opacity;
    }

    public void onInit() {
        super.onInit();
        this.mOpacityLocation = GLES20.glGetUniformLocation(getProgram(), "opacity");
    }

    public void onInitialized() {
        super.onInitialized();
        setOpacity(this.mOpacity);
    }

    public void setOpacity(float opacity) {
        this.mOpacity = opacity;
        setFloat(this.mOpacityLocation, this.mOpacity);
    }
}
