package bsoft.com.lib_filter.filter.gpu.tonewithblend;

import android.opengl.GLES20;
import com.google.android.gms.cast.TextTrackStyle;

import bsoft.com.lib_filter.filter.gpu.normal.GPUImageToneCurveFilter;


public class GPUImageToneCurveWithNormalBlendOpacityFilter extends GPUImageToneCurveFilter {
    public static final String TONE_CURVE_FRAGMENT_SHADER = " varying highp vec2 textureCoordinate;\n uniform sampler2D inputImageTexture;\n uniform sampler2D toneCurveTexture;\n uniform lowp float opacity;\n uniform lowp float mixturePercent;\n\n void main()\n {\n     lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     lowp float redCurveValue = texture2D(toneCurveTexture, vec2(textureColor.r, 0.0)).r;\n     lowp float greenCurveValue = texture2D(toneCurveTexture, vec2(textureColor.g, 0.0)).g;\n     lowp float blueCurveValue = texture2D(toneCurveTexture, vec2(textureColor.b, 0.0)).b;\n\n     lowp vec4 textureColor2 = vec4(redCurveValue,greenCurveValue,blueCurveValue,textureColor.a);\n     lowp vec4 c1 = vec4(mix(textureColor.rgb, textureColor2.rgb, textureColor2.a*mixturePercent), textureColor.a);\n     lowp vec4 c2 = textureColor;\n     \n     lowp vec4 outputColor;\n     \n     outputColor.r = c1.r + c2.r * c2.a * (1.0 - c1.a);\n\n     outputColor.g = c1.g + c2.g * c2.a * (1.0 - c1.a);\n     \n     outputColor.b = c1.b + c2.b * c2.a * (1.0 - c1.a);\n     \n     outputColor.a = c1.a + c2.a * (1.0 - c1.a);\n     \n     lowp vec4 textureColor3 = outputColor;\n     gl_FragColor =vec4(mix(c2.rgb, textureColor3.rgb, textureColor3.a*opacity*mixturePercent), c2.a);\n }";
    private float mOpacity = TextTrackStyle.DEFAULT_FONT_SCALE;
    private int mOpacityLocation;

    public GPUImageToneCurveWithNormalBlendOpacityFilter() {
        super(TONE_CURVE_FRAGMENT_SHADER);
    }

    public GPUImageToneCurveWithNormalBlendOpacityFilter(String fragment_shader) {
        super(fragment_shader);
    }

    public void onInit() {
        super.onInit();
        this.mOpacityLocation = GLES20.glGetUniformLocation(getProgram(), "opacity");
    }

    public void setOpacity(float opacity) {
        this.mOpacity = opacity;
        setFloat(this.mOpacityLocation, opacity);
    }

    public void onInitialized() {
        super.onInitialized();
        setOpacity(this.mOpacity);
    }
}
