package bsoft.com.lib_filter.filter.gpu.newfilter;

import android.graphics.PointF;
import android.opengl.GLES20;
import com.google.android.gms.cast.TextTrackStyle;

import bsoft.com.lib_filter.filter.gpu.father.GPUImageTwoInputFilter;


public class GPUImageFisheyeFilter extends GPUImageTwoInputFilter {
    private static final String FOCUS_FRAGMENT_SHADER = "precision mediump float; varying highp vec2 textureCoordinate; varying highp vec2 textureCoordinate2; uniform sampler2D inputImageTexture; uniform sampler2D inputImageTexture2; uniform  float aspectRatio; uniform highp vec2 center; uniform  float radius; uniform highp float refractiveIndex; uniform int isMirror; uniform vec3 filterColor; uniform int isColor; const highp vec3 ambientLightPosition = vec3(0.0, 0.0, 1.0); void main() {    lowp vec4 blurredImageColor = texture2D(inputImageTexture2, textureCoordinate2);    if (isColor == 1){         blurredImageColor = vec4(mix(blurredImageColor.rgb,filterColor,0.35),1.0);}    highp vec2 textureCoordinateToUse = vec2(textureCoordinate.x, (textureCoordinate.y * aspectRatio + 0.5 - 0.5 * aspectRatio));    highp float distanceFromCenter = distance(center, textureCoordinateToUse);    lowp float checkForPresenceWithinSphere = step(distanceFromCenter, radius);    vec4 finalcolor;    if (distanceFromCenter < radius){            float normalizedDepth = sqrt(radius * radius - distanceFromCenter * distanceFromCenter);            highp vec3 sphereNormal = normalize(vec3(textureCoordinateToUse - center, normalizedDepth));            highp vec3 refractedVector = refract(vec3(0.0, 0.0, -1.0), sphereNormal, refractiveIndex);            if (isMirror == 0){refractedVector.xy = -refractedVector.xy;}            highp vec3 finalSphereColor = texture2D(inputImageTexture, (refractedVector.xy + 1.0) * 0.5).rgb;            float lightingIntensity = 0.45 * (1.0 - pow(clamp(dot(ambientLightPosition, sphereNormal), 0.0, 1.0), 0.25));            finalSphereColor += lightingIntensity;            finalcolor = vec4(finalSphereColor, 1.0) * checkForPresenceWithinSphere;            float d = radius- distanceFromCenter;            float t = smoothstep(0.0, 0.02, d);            finalcolor = mix(finalcolor, blurredImageColor,1.0- t);    }else{            finalcolor = blurredImageColor;    }    gl_FragColor = finalcolor;}";
    private float mAspectRatio;
    private int mAspectRatioLocation;
    private PointF mCenter;
    private int mCenterLocation;
    private float[] mColor;
    private int mFilterColorLocation;
    private boolean mIsMirror;
    private int mIsMirrorLocation;
    private boolean mIsSetColor;
    private int mIsSetColorLocation;
    private float mRadius;
    private int mRadiusLocation;
    private float mRefractiveIndex;
    private int mRefractiveIndexLocation;

    public GPUImageFisheyeFilter(String fragment_shader) {
        super(fragment_shader);
        this.mCenter = new PointF(0.5f, 0.5f);
        this.mRadius = 0.3f;
        this.mAspectRatio = TextTrackStyle.DEFAULT_FONT_SCALE;
        this.mRefractiveIndex = 0.5f;
        this.mColor = new float[]{0.97f, 0.77f, 0.8f, TextTrackStyle.DEFAULT_FONT_SCALE};
        this.mIsMirror = false;
        this.mIsSetColor = false;
    }

    public GPUImageFisheyeFilter(String vertex_shader, String fragment_shader) {
        super(vertex_shader, fragment_shader);
        this.mCenter = new PointF(0.5f, 0.5f);
        this.mRadius = 0.3f;
        this.mAspectRatio = TextTrackStyle.DEFAULT_FONT_SCALE;
        this.mRefractiveIndex = 0.5f;
        this.mColor = new float[]{0.97f, 0.77f, 0.8f, TextTrackStyle.DEFAULT_FONT_SCALE};
        this.mIsMirror = false;
        this.mIsSetColor = false;
    }

    public GPUImageFisheyeFilter() {
        super(FOCUS_FRAGMENT_SHADER);
        this.mCenter = new PointF(0.5f, 0.5f);
        this.mRadius = 0.3f;
        this.mAspectRatio = 0.75f;
        this.mRefractiveIndex = 0.5f;
        this.mColor = new float[]{0.97f, 0.77f, 0.8f, TextTrackStyle.DEFAULT_FONT_SCALE};
        this.mIsMirror = false;
        this.mIsSetColor = false;
    }

    public GPUImageFisheyeFilter(float[] colors, boolean isColor, boolean isMirror, float index, float radius) {
        this();
        this.mColor = colors;
        this.mIsSetColor = isColor;
        this.mIsMirror = isMirror;
        this.mRefractiveIndex = index;
        this.mRadius = radius;
    }

    public GPUImageFisheyeFilter(String fragment_shader, float[] colors, boolean isColor, boolean isMirror, float index, float radius) {
        this(fragment_shader);
        this.mColor = colors;
        this.mIsSetColor = isColor;
        this.mIsMirror = isMirror;
        this.mRefractiveIndex = index;
        this.mRadius = radius;
    }

    public GPUImageFisheyeFilter(String vertex_shader, String fragment_shader, float[] colors, boolean isColor, boolean isMirror, float index, float radius) {
        this(vertex_shader, fragment_shader);
        this.mColor = colors;
        this.mIsSetColor = isColor;
        this.mIsMirror = isMirror;
        this.mRefractiveIndex = index;
        this.mRadius = radius;
    }

    public void onInit() {
        super.onInit();
        this.mCenterLocation = GLES20.glGetUniformLocation(getProgram(), "center");
        this.mRadiusLocation = GLES20.glGetUniformLocation(getProgram(), "radius");
        this.mAspectRatioLocation = GLES20.glGetUniformLocation(getProgram(), "aspectRatio");
        this.mRefractiveIndexLocation = GLES20.glGetUniformLocation(getProgram(), "refractiveIndex");
        this.mIsMirrorLocation = GLES20.glGetUniformLocation(getProgram(), "isMirror");
        this.mFilterColorLocation = GLES20.glGetUniformLocation(getProgram(), "filterColor");
        this.mIsSetColorLocation = GLES20.glGetUniformLocation(getProgram(), "isColor");
    }

    public void onInitialized() {
        super.onInitialized();
        setRadius(this.mRadius);
        setCenter(this.mCenter);
        setRefractiveIndex(this.mRefractiveIndex);
        setAspectRatio(this.mAspectRatio);
        setIsMirror(this.mIsMirror);
        setmIsSetColor(this.mIsSetColor);
        setColor(this.mColor);
    }

    public void setAspectRatio(float aspectRatio) {
        this.mAspectRatio = aspectRatio;
        setFloat(this.mAspectRatioLocation, aspectRatio);
    }

    public void setRefractiveIndex(float refractiveIndex) {
        this.mRefractiveIndex = refractiveIndex;
        setFloat(this.mRefractiveIndexLocation, refractiveIndex);
    }

    public void setCenter(PointF center) {
        this.mCenter = center;
        setPoint(this.mCenterLocation, center);
    }

    public void setRadius(float radius) {
        this.mRadius = radius;
        setFloat(this.mRadiusLocation, radius);
    }

    public void setIsMirror(boolean isMirror) {
        this.mIsMirror = isMirror;
        if (isMirror) {
            setInteger(this.mIsMirrorLocation, 1);
        } else {
            setInteger(this.mIsMirrorLocation, 0);
        }
    }

    public void setmIsSetColor(boolean isColor) {
        this.mIsSetColor = isColor;
        if (isColor) {
            setInteger(this.mIsSetColorLocation, 1);
        } else {
            setInteger(this.mIsSetColorLocation, 0);
        }
    }

    public void setColor(float[] color) {
        this.mColor = color;
        setColorRed(this.mColor[0], this.mColor[1], this.mColor[2]);
    }

    public void setColorRed(float red, float green, float blue) {
        setFloatVec3(this.mFilterColorLocation, new float[]{red, green, blue});
    }
}
