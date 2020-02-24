package bsoft.com.lib_filter.filter.gpu;

import android.graphics.Color;
import android.opengl.GLES20;
import com.google.android.gms.cast.TextTrackStyle;

import bsoft.com.lib_filter.filter.gpu.father.GPUImageFilter;


public class GPUImageColorFilter extends GPUImageFilter {
    public static final String COLOR_FRAGMENT_SHADER = "  varying highp vec2 textureCoordinate;\n  uniform sampler2D inputImageTexture;\n  uniform lowp vec4 color;\n  \n  uniform lowp float mixturePercent;\n  \n  void main()\n  {\n      lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n      \n      gl_FragColor = vec4(mix(textureColor.rgb, color.rgb, textureColor.a*0.42), textureColor.a);  }\n";
    private int mColor;
    private int mColorLocation;
    private float[] mColorRGBA;

    public GPUImageColorFilter(int color) {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, COLOR_FRAGMENT_SHADER);
        this.mColor = color;
    }

    public GPUImageColorFilter(int color, float mix) {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, COLOR_FRAGMENT_SHADER);
        this.mColor = color;
        this.mMix = mix;
    }

    public void onInit() {
        super.onInit();
        this.mColorLocation = GLES20.glGetUniformLocation(getProgram(), "color");
    }

    public void onInitialized() {
        super.onInitialized();
        setColor(this.mColor);
    }

    public void setColor(int color) {
        this.mColor = color;
        this.mColorRGBA = new float[]{((float) Color.red(this.mColor)) / 255.0f, ((float) Color.green(this.mColor)) / 255.0f, ((float) Color.blue(this.mColor)) / 255.0f, TextTrackStyle.DEFAULT_FONT_SCALE};
        setFloatVec4(this.mColorLocation, this.mColorRGBA);
        setMix(this.mMix);
    }
}
