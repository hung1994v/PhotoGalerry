package bsoft.com.lib_filter.filter.gpu.retro;


import bsoft.com.lib_filter.filter.gpu.normal.GPUImageToneCurveFilter;

public class RetroDelicateBrownFilter extends GPUImageToneCurveFilter {
    private static final String FRAGMENT_SHADER = " varying highp vec2 textureCoordinate;\n uniform sampler2D inputImageTexture;\n uniform sampler2D toneCurveTexture;\n uniform lowp float mixturePercent;\n highp float lum(lowp vec3 c) {\n     return dot(c, vec3(0.3, 0.59, 0.11));\n }\n \n lowp vec3 clipcolor(lowp vec3 c) {\n     highp float l = lum(c);\n     lowp float n = min(min(c.r, c.g), c.b);\n     lowp float x = max(max(c.r, c.g), c.b);\n     \n     if (n < 0.0) {\n         c.r = l + ((c.r - l) * l) / (l - n);\n         c.g = l + ((c.g - l) * l) / (l - n);\n         c.b = l + ((c.b - l) * l) / (l - n);\n     }\n     if (x > 1.0) {\n         c.r = l + ((c.r - l) * (1.0 - l)) / (x - l);\n         c.g = l + ((c.g - l) * (1.0 - l)) / (x - l);\n         c.b = l + ((c.b - l) * (1.0 - l)) / (x - l);\n     }\n     \n     return c;\n }\n\n lowp vec3 setlum(lowp vec3 c, highp float l) {\n     highp float d = l - lum(c);\n     c = c + vec3(d);\n     return clipcolor(c);\n }\n\n void main()\n {\n   highp vec4 baseColor = texture2D(inputImageTexture, textureCoordinate);\n   highp vec4 overlayColor = vec4(50.0/255.0, 45.0/255.0, 45.0/255.0, 1.0);\n\n     lowp vec4 textureColor3 = vec4(baseColor.rgb * (1.0 - overlayColor.a) + setlum(overlayColor.rgb, lum(baseColor.rgb)) * overlayColor.a, baseColor.a);\n     lowp vec4 textureColor =vec4(mix(baseColor.rgb, textureColor3.rgb, textureColor3.a*0.6*mixturePercent), baseColor.a);\n     lowp float redCurveValue = texture2D(toneCurveTexture, vec2(textureColor.r, 0.0)).r;\n     lowp float greenCurveValue = texture2D(toneCurveTexture, vec2(textureColor.g, 0.0)).g;\n     lowp float blueCurveValue = texture2D(toneCurveTexture, vec2(textureColor.b, 0.0)).b;\n\n     lowp vec4 textureColor2 = vec4(redCurveValue,greenCurveValue,blueCurveValue,textureColor.a);\n     mediump vec4 overlay = vec4(mix(textureColor.rgb, textureColor2.rgb, textureColor2.a*mixturePercent), textureColor.a);\n     mediump vec4 base = textureColor;\n     textureColor3 = vec4((overlay.rgb * base.a + base.rgb * overlay.a - 2.0 * overlay.rgb * base.rgb) + overlay.rgb * (1.0 - base.a) + base.rgb * (1.0 - overlay.a), base.a);\n     textureColor =vec4(mix(base.rgb, textureColor3.rgb, textureColor3.a*0.33*mixturePercent), base.a);\n     textureColor = vec4((textureColor.rgb + vec3(0.15)), textureColor.w);\n     gl_FragColor = vec4(((textureColor.rgb - vec3(0.5)) * 0.9 + vec3(0.5)), textureColor.w);\n }";

    public RetroDelicateBrownFilter() {
        super(FRAGMENT_SHADER);
    }
}
