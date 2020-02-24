package bsoft.com.lib_filter.filter.gpu.newfilter;

import android.opengl.GLES20;
import com.google.android.gms.cast.TextTrackStyle;

import bsoft.com.lib_filter.filter.gpu.father.GPUImageFilter;


public class GPUImageMirrorSplashFilter extends GPUImageFilter {
    public static final String MONOCHROME_FRAGMENT_SHADER = " precision lowp float;\n  \n  varying highp vec2 textureCoordinate;\n  \n  uniform sampler2D inputImageTexture;\n  uniform float intensity;\n  uniform vec3 filterColor;\n  uniform int isMirror;  uniform float isShowOri;  \n  const mediump vec3 luminanceWeighting = vec3(0.212, 0.715, 0.072);\n  \n\thighp vec3 rgb2hsb(highp vec3 rgb) {\t\tlowp vec3 hsb ;    \tlowp vec3 rearranged = rgb;    \tlowp int maxIndex = 0,minIndex = 0;    \tlowp float tmp;    \tfor(int i=0;i<2;i++) {        for(int j=0;j<2-i;j++)            if(rearranged[j]>rearranged[j+1]) {                tmp=rearranged[j+1];                rearranged[j+1]=rearranged[j];                rearranged[j]=tmp;            }    \t} \tfor(int i=0;i<3;i++) { \t\tif(rearranged[0]==rgb[i]) minIndex=i; \t\tif(rearranged[2]==rgb[i]) maxIndex=i; \t} \thsb[2]=rearranged[2]/255.0; \thsb[1]=1.0-rearranged[0]/rearranged[2];\t\tlowp float tempFloat = 1.0;\t\tif (maxIndex != minIndex) tempFloat = -1.0;\t\thsb[0]=float(maxIndex)*120.0+60.0* (rearranged[1]/hsb[1]/rearranged[2]+(1.0-1.0/hsb[1])) *(mod(float(maxIndex-minIndex+3),3.0)==1.0?1.0:-1.0);\t\tif (hsb[0] <= 0.0){hsb[0] = hsb[0] + 360.0;} \treturn hsb;\t}highp vec3 hsb2rgb(highp vec3 hsb) {\tlowp vec3 rgb ;    for(int offset=240,i=0;i<3;i++,offset-=120) { \t\tlowp float tempFloat; \t\ttempFloat = mod(hsb[0] + float(offset),360.0) - 240.0;       \tlowp float x=abs(tempFloat);        \tif(x<=60.0) rgb[i]=255.0;        \telse if(60.0<x && x<120.0) rgb[i]=((1.0-(x-60.0)/60.0)*255.0);        \telse rgb[i]=0.0;   }    for(int i=0;i<3;i++)        rgb[i]+=(255.0-rgb[i])*(1.0-hsb[1]);    for(int i=0;i<3;i++)        rgb[i]*=hsb[2];    return rgb;}  void main()\n  {\n      highp vec2 texture = textureCoordinate;       highp float mIntensity = intensity;      if (isMirror == 1){texture.x = 1.0 - texture.x; mIntensity += 180.0;} \tlowp vec4 textureColor = texture2D(inputImageTexture, texture);\n \tfloat luminance = dot(textureColor.rgb, luminanceWeighting);\n \tlowp vec4 desat = vec4(vec3(luminance), 1.0);\n\t\tlowp vec3 hsbFilter = rgb2hsb(filterColor);\t\tlowp vec3 hsbTexture = rgb2hsb(textureColor.rgb);\t\tlowp float Hf = hsbFilter[0];\t\tlowp float Ht = hsbTexture[0];\t\tlowp float St = hsbTexture[1];\t\tlowp float t = hsbTexture[1];     if (filterColor.r != 255.0 || filterColor.g != 255.0 || filterColor.b != 255.0){\t\tlowp float threshold = 45.0;\t\tif (Hf > 0.0 && Hf < threshold) Hf = Hf + 360.0;\t\tif (Ht > 0.0 && Ht < threshold) Ht = Ht + 360.0;\t\tt = smoothstep(Hf - threshold,Hf + threshold,Ht) ;\t\tif (t >= 0.5) t = 1.0 - t; \tt = t * 2.0;}     lowp float Hout = mod((hsbTexture[0] + mIntensity),360.0);\t\tlowp vec3 hsbOut = vec3(Hout,hsbTexture[1] *1.5,hsbTexture[2]);\t\thighp vec4 rgbOut = vec4(hsb2rgb(hsbOut),1.0);\t\tgl_FragColor = mix(mix(desat,rgbOut,t),textureColor,isShowOri);\n}";
    private float[] mColor;
    private int mFilterColorLocation;
    private float mIntensity;
    private int mIntensityLocation;
    private boolean mIsMirror;
    private int mIsMirrorLocation;
    private boolean mIsShowOri;
    private int mIsShowOriLocation;

    public GPUImageMirrorSplashFilter(boolean isMirror) {
        this(0.0f, new float[]{0.6f, 0.45f, 0.3f, TextTrackStyle.DEFAULT_FONT_SCALE});
        this.mIsMirror = isMirror;
    }

    public GPUImageMirrorSplashFilter(float r, float g, float b, float intensity, boolean isMirror) {
        this(intensity, new float[]{r, g, b, TextTrackStyle.DEFAULT_FONT_SCALE});
        this.mIsMirror = isMirror;
    }

    public GPUImageMirrorSplashFilter(float intensity, float[] color) {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, MONOCHROME_FRAGMENT_SHADER);
        this.mIntensity = 0.0f;
        this.mIntensity = intensity;
        this.mColor = color;
    }

    public void onInit() {
        super.onInit();
        this.mIntensityLocation = GLES20.glGetUniformLocation(getProgram(), "intensity");
        this.mFilterColorLocation = GLES20.glGetUniformLocation(getProgram(), "filterColor");
        this.mIsMirrorLocation = GLES20.glGetUniformLocation(getProgram(), "isMirror");
        this.mIsShowOriLocation = GLES20.glGetUniformLocation(getProgram(), "isShowOri");
    }

    public void onInitialized() {
        super.onInitialized();
        setIntensity(this.mIntensity);
        setColor(this.mColor);
        setIsMirror(this.mIsMirror);
        setIsShowOri(this.mIsShowOri);
    }

    public void setIsMirror(boolean isMirror) {
        this.mIsMirror = isMirror;
        if (isMirror) {
            setInteger(this.mIsMirrorLocation, 1);
        } else {
            setInteger(this.mIsMirrorLocation, 0);
        }
    }

    public void setIsShowOri(boolean isOri) {
        this.mIsShowOri = isOri;
        if (isOri) {
            setFloat(this.mIsShowOriLocation, TextTrackStyle.DEFAULT_FONT_SCALE);
        } else {
            setFloat(this.mIsShowOriLocation, 0.0f);
        }
    }

    public void setIntensity(float intensity) {
        this.mIntensity = intensity;
        setFloat(this.mIntensityLocation, this.mIntensity);
    }

    public void setColor(float[] color) {
        this.mColor = color;
        setColorRed(this.mColor[0], this.mColor[1], this.mColor[2]);
    }

    public void setColorRed(float red, float green, float blue) {
        setFloatVec3(this.mFilterColorLocation, new float[]{red, green, blue});
    }
}
