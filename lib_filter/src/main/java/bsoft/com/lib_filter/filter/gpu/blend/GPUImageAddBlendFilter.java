package bsoft.com.lib_filter.filter.gpu.blend;


import bsoft.com.lib_filter.filter.gpu.father.GPUImageTwoInputFilter;

public class GPUImageAddBlendFilter extends GPUImageTwoInputFilter {
    public static final String ADD_BLEND_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n varying highp vec2 textureCoordinate2;\n\n uniform sampler2D inputImageTexture;\n uniform sampler2D inputImageTexture2;\n \n uniform lowp float mixturePercent;\n void main()\n {\n   lowp vec4 base = texture2D(inputImageTexture, textureCoordinate);\n   lowp vec4 overlay = texture2D(inputImageTexture2, textureCoordinate2);\n\n   mediump float r;\n   if (overlay.r * base.a + base.r * overlay.a >= overlay.a * base.a) {\n     r = overlay.a * base.a + overlay.r * (1.0 - base.a) + base.r * (1.0 - overlay.a);\n   } else {\n     r = overlay.r + base.r;\n   }\n\n   mediump float g;\n   if (overlay.g * base.a + base.g * overlay.a >= overlay.a * base.a) {\n     g = overlay.a * base.a + overlay.g * (1.0 - base.a) + base.g * (1.0 - overlay.a);\n   } else {\n     g = overlay.g + base.g;\n   }\n\n   mediump float b;\n   if (overlay.b * base.a + base.b * overlay.a >= overlay.a * base.a) {\n     b = overlay.a * base.a + overlay.b * (1.0 - base.a) + base.b * (1.0 - overlay.a);\n   } else {\n     b = overlay.b + base.b;\n   }\n\n   mediump float a = overlay.a + base.a - overlay.a * base.a;\n   \n     lowp vec4 textureColor3 = vec4(r, g, b, a);\n     gl_FragColor =vec4(mix(base.rgb, textureColor3.rgb, textureColor3.a*mixturePercent), base.a);\n }";

    public GPUImageAddBlendFilter() {
        super(ADD_BLEND_FRAGMENT_SHADER);
    }
}
