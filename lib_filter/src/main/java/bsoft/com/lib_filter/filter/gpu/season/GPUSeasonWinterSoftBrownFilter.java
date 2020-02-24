package bsoft.com.lib_filter.filter.gpu.season;


import bsoft.com.lib_filter.filter.gpu.normal.GPUImageToneCurveFilter;

public class GPUSeasonWinterSoftBrownFilter extends GPUImageToneCurveFilter {
    private static final String FRAGMENT_SHADER = " varying highp vec2 textureCoordinate;\n uniform sampler2D inputImageTexture;\n uniform sampler2D toneCurveTexture;\n\n const mediump vec3 luminanceWeighting = vec3(0.2125, 0.7154, 0.0721);\n uniform lowp float mixturePercent;\n void main()\n {\n    lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n    lowp float luminance = dot(textureColor.rgb, luminanceWeighting);\n    lowp vec3 greyScaleColor = vec3(luminance);\n    \n    lowp vec4 textureColor2  = vec4(mix(greyScaleColor, textureColor.rgb, 0.0), textureColor.w);\n     mediump vec4 layer1 =vec4(mix(textureColor.rgb, textureColor2.rgb, textureColor2.a * mixturePercent), textureColor.a);\n     \n     mediump vec4 whiteColor = vec4(1.0);\n     lowp vec4 textureColor3 = whiteColor - ((whiteColor - layer1) * (whiteColor - textureColor));\n     mediump vec4 layer2 = vec4(mix(textureColor.rgb, textureColor3.rgb, textureColor3.a*0.57), textureColor.a);\n     lowp float redCurveValue = texture2D(toneCurveTexture, vec2(layer2.r, 0.0)).r;\n     lowp float greenCurveValue = texture2D(toneCurveTexture, vec2(layer2.g, 0.0)).g;\n     lowp float blueCurveValue = texture2D(toneCurveTexture, vec2(layer2.b, 0.0)).b;\n\n     lowp vec4 textureColor4 = vec4(redCurveValue,greenCurveValue,blueCurveValue,layer2.a);\n     mediump vec4 layer3 = vec4(mix(layer2.rgb, textureColor4.rgb, textureColor4.a), layer2.a);\n     mediump vec4 overlay = vec4(210.0/255.0, 200.0/255.0, 231.0/255.0, 1.0);\n     \n     lowp vec4 textureColor5 = layer3 * (overlay.a * (layer3 / layer3.a) + (2.0 * overlay * (1.0 - (layer3 / layer3.a)))) + overlay * (1.0 - layer3.a) + layer3 * (1.0 - overlay.a);\n     mediump vec4 layer4 = vec4(mix(layer3.rgb, textureColor5.rgb, textureColor5.a), layer3.a);\n     overlay = vec4(161.0/255.0, 134.0/255.0, 107.0/255.0, 1.0);\n     highp float ra;\n     if (2.0 * overlay.r < overlay.a) {\n         ra = 2.0 * overlay.r * layer4.r + overlay.r * (1.0 - layer4.a) + layer4.r * (1.0 - overlay.a);\n     } else {\n         ra = overlay.a * layer4.a - 2.0 * (layer4.a - layer4.r) * (overlay.a - overlay.r) + overlay.r * (1.0 - layer4.a) + layer4.r * (1.0 - overlay.a);\n     }\n     \n     highp float ga;\n     if (2.0 * overlay.g < overlay.a) {\n         ga = 2.0 * overlay.g * layer4.g + overlay.g * (1.0 - layer4.a) + layer4.g * (1.0 - overlay.a);\n     } else {\n         ga = overlay.a * layer4.a - 2.0 * (layer4.a - layer4.g) * (overlay.a - overlay.g) + overlay.g * (1.0 - layer4.a) + layer4.g * (1.0 - overlay.a);\n     }\n     \n     highp float ba;\n     if (2.0 * overlay.b < overlay.a) {\n         ba = 2.0 * overlay.b * layer4.b + overlay.b * (1.0 - layer4.a) + layer4.b * (1.0 - overlay.a);\n     } else {\n         ba = overlay.a * layer4.a - 2.0 * (layer4.a - layer4.b) * (overlay.a - overlay.b) + overlay.b * (1.0 - layer4.a) + layer4.b * (1.0 - overlay.a);\n     }\n     \n     lowp vec4 textureColor6 = vec4(ra, ga, ba, 1.0);\n     gl_FragColor =vec4(mix(layer4.rgb, textureColor6.rgb, textureColor6.a*0.43), layer4.a);\n }";

    public GPUSeasonWinterSoftBrownFilter() {
        super();
    }
}
