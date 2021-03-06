package bsoft.com.lib_filter.filter.gpu.season;


import bsoft.com.lib_filter.filter.gpu.father.GPUImageFilter;

public class GPUSeasonAutumnDawoodHamadaFilter extends GPUImageFilter {
    private static final String FRAGMENT_SHADER = " varying highp vec2 textureCoordinate;\n\n uniform sampler2D inputImageTexture;\n \nvoid main()\n{\n     mediump vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     mediump vec4 layer1 = vec4( mix(vec3(15.0/255.0), vec3(1.0), pow(min(max(textureColor.rgb - vec3(31.0/255.0), vec3(0.0)) / (vec3(221.0/255.0) - vec3(31.0/255.0)  ), vec3(1.0)), 1.0 /vec3(1.08))) , textureColor.a);\n     gl_FragColor = vec4( mix(vec3(14.0/255.0), vec3(236.0/255.0), pow(min(max(layer1.rgb -vec3(22.0/255.0), vec3(0.0)) / (vec3(1.0) - vec3(22.0/255.0)  ), vec3(1.0)), 1.0 /vec3(1.7))) , layer1.a);\n     \n}";
}
