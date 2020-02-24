package bsoft.com.lib_filter.filter.gpu.mirror;


import bsoft.com.lib_filter.filter.gpu.father.GPUImageFilter;

public class GPUImageMirrorFilter extends GPUImageFilter {
    public static final String MIRROR_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\nuniform sampler2D inputImageTexture;\nuniform lowp float mirrorType;\nuniform lowp float mixturePercent;\nvoid main(){   highp vec2 textureCoordinateM = textureCoordinate;   if(textureCoordinate.x>0.5){textureCoordinateM.x = 1.0- textureCoordinate.x;}\thighp vec4 textureColor = texture2D(inputImageTexture, textureCoordinateM);   gl_FragColor = textureColor;}";

    public GPUImageMirrorFilter() {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, MIRROR_FRAGMENT_SHADER);
    }
}
