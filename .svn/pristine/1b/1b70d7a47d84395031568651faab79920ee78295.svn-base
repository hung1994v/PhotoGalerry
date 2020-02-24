package bsoft.com.lib_filter.filter.gpu.blend;


import bsoft.com.lib_filter.filter.gpu.father.GPUImageTwoInputFilter;

public class GPUImageLinearBurnBlendFilter extends GPUImageTwoInputFilter {
    public static final String LINEAR_BURN_BLEND_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n varying highp vec2 textureCoordinate2;\n \n uniform sampler2D inputImageTexture;\n uniform sampler2D inputImageTexture2;\n \n uniform lowp float mixturePercent;\n void main()\n {\n     mediump vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     mediump vec4 textureColor2 = texture2D(inputImageTexture2, textureCoordinate2);\n     \n     lowp vec4 textureColor3 = vec4(clamp(textureColor.rgb + textureColor2.rgb - vec3(1.0), vec3(0.0), vec3(1.0)), textureColor.a);\n     gl_FragColor =vec4(mix(textureColor.rgb, textureColor3.rgb, textureColor3.a*mixturePercent), textureColor.a);\n }";

    public GPUImageLinearBurnBlendFilter() {
        super(LINEAR_BURN_BLEND_FRAGMENT_SHADER);
    }
}
