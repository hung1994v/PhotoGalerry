package bsoft.com.lib_filter.filter.gpu.newfilter;

import android.opengl.GLES20;
import com.google.android.gms.cast.TextTrackStyle;

public class GPUImageFisheyeExFilter extends GPUImageFisheyeFilter {
    public static final String FOCUS_FRAGMENT_SHADER = "precision mediump float; varying highp vec2 textureCoordinate; varying highp vec2 textureCoordinate2; uniform sampler2D inputImageTexture; uniform sampler2D inputImageTexture2; uniform  float aspectRatio; uniform highp vec2 center; uniform  float radius; uniform highp float refractiveIndex; uniform int isMirror; uniform vec3 filterColor; uniform int isColor; uniform float ratio; uniform int isBlur; const highp vec3 ambientLightPosition = vec3(0.0, 0.0, 1.0); void main() {    lowp vec4 blurredImageColor;    if (isBlur == 0){    blurredImageColor = texture2D(inputImageTexture2, textureCoordinate2);    }    else{    blurredImageColor = texture2D(inputImageTexture, textureCoordinate);    }    if (isColor == 1){         blurredImageColor = vec4(mix(blurredImageColor.rgb,filterColor,0.35),1.0);}    if (textureCoordinate.x >= (0.5 - 0.5 / ratio) && textureCoordinate.x <= 1.0 - (0.5 - 0.5 / ratio))    {    highp vec2 textureCoordinateToUse = vec2(textureCoordinate.x, (textureCoordinate.y * aspectRatio + 0.5 - 0.5 * aspectRatio));    highp vec2 pos = vec2(textureCoordinateToUse.x * ratio - center.x * ratio + center.x, textureCoordinateToUse.y);    highp float distanceFromCenter = distance(center, pos);    lowp float checkForPresenceWithinSphere = step(distanceFromCenter, radius);    vec4 finalcolor;    if (distanceFromCenter < radius){            float normalizedDepth = sqrt(radius * radius - distanceFromCenter * distanceFromCenter);            highp vec3 sphereNormal = normalize(vec3(textureCoordinateToUse - center, normalizedDepth));            highp vec3 refractedVector = refract(vec3(0.0, 0.0, -1.0), sphereNormal, refractiveIndex);            if (isMirror == 0){refractedVector.xy = -refractedVector.xy;}            highp vec3 finalSphereColor = texture2D(inputImageTexture, (refractedVector.xy + 1.0) * 0.5).rgb;            float lightingIntensity = 0.45 * (1.0 - pow(clamp(dot(ambientLightPosition, sphereNormal), 0.0, 1.0), 0.25));            finalSphereColor += lightingIntensity;            finalcolor = vec4(finalSphereColor, 1.0) * 1.0;            float d = radius - distanceFromCenter;            float t = smoothstep(0.0, 0.02, d);            finalcolor = mix(finalcolor, blurredImageColor,1.0- t);    }else{            finalcolor = blurredImageColor;    }    gl_FragColor = finalcolor;    }}";
    public static final String GETOUTPUT_FRAGMENT_SHADER = "precision mediump float; varying highp vec2 textureCoordinate; varying highp vec2 textureCoordinate2; uniform sampler2D inputImageTexture; uniform sampler2D inputImageTexture2; uniform  float aspectRatio; uniform highp vec2 center; uniform  float radius; uniform highp float refractiveIndex; uniform int isMirror; uniform vec3 filterColor; uniform int isColor; uniform float ratio; uniform int isBlur; const highp vec3 ambientLightPosition = vec3(0.0, 0.0, 1.0); void main() {    lowp vec4 blurredImageColor;    if (isBlur == 0){    blurredImageColor = texture2D(inputImageTexture2, textureCoordinate2);    }    else{    blurredImageColor = texture2D(inputImageTexture, textureCoordinate);    }    if (isColor == 1){         blurredImageColor = vec4(mix(blurredImageColor.rgb,filterColor,0.35),1.0);}    if (textureCoordinate.y >= (0.5 - 0.5 / ratio) && textureCoordinate.y <= 1.0 - (0.5 - 0.5 / ratio))    {    highp vec2 textureCoordinateToUse = vec2(textureCoordinate.x, (textureCoordinate.y * aspectRatio + 0.5 - 0.5 * aspectRatio));    highp vec2 pos = vec2(textureCoordinateToUse.x, textureCoordinateToUse.y / ratio - center.y / ratio + center.y);    highp float distanceFromCenter = distance(center, pos);    lowp float checkForPresenceWithinSphere = step(distanceFromCenter, radius);    vec4 finalcolor;    if (distanceFromCenter < radius){            float normalizedDepth = sqrt(radius * radius - distanceFromCenter * distanceFromCenter);            highp vec3 sphereNormal = normalize(vec3(textureCoordinateToUse - center, normalizedDepth));            highp vec3 refractedVector = refract(vec3(0.0, 0.0, -1.0), sphereNormal, refractiveIndex);            if (isMirror == 0){refractedVector.xy = -refractedVector.xy;}            highp vec3 finalSphereColor = texture2D(inputImageTexture, (refractedVector.xy + 1.0) * 0.5).rgb;            float lightingIntensity = 0.45 * (1.0 - pow(clamp(dot(ambientLightPosition, sphereNormal), 0.0, 1.0), 0.25));            finalSphereColor += lightingIntensity;            finalcolor = vec4(finalSphereColor, 1.0) * 1.0;            float d = radius - distanceFromCenter;            float t = smoothstep(0.0, 0.02, d);            finalcolor = mix(finalcolor, blurredImageColor,1.0- t);    }else{            finalcolor = blurredImageColor;    }    gl_FragColor = finalcolor;    }}";
    private float mBlurSize;
    private boolean mIsBlur;
    private int mIsBlurLocation;
    private float mRatio;
    private int mRatioLocation;

    class C15451 implements Runnable {
        C15451() {
        }

        public void run() {
            GPUImageFisheyeExFilter.this.initTexelOffsets();
        }
    }

    public GPUImageFisheyeExFilter(String fragment_shader) {
        super(fragment_shader);
        this.mBlurSize = TextTrackStyle.DEFAULT_FONT_SCALE;
        this.mRatio = TextTrackStyle.DEFAULT_FONT_SCALE;
        this.mIsBlur = false;
    }

    public GPUImageFisheyeExFilter(String fragment_shader, float ratio, boolean isBlur, float[] colors, boolean isColor, boolean isMirror, float index, float radius, float blurSize) {
        super(fragment_shader, colors, isColor, isMirror, index, radius);
        this.mBlurSize = TextTrackStyle.DEFAULT_FONT_SCALE;
        this.mRatio = ratio;
        this.mIsBlur = isBlur;
        this.mBlurSize = blurSize;
    }

    public void onInit() {
        super.onInit();
        this.mRatioLocation = GLES20.glGetUniformLocation(getProgram(), "ratio");
        this.mIsBlurLocation = GLES20.glGetUniformLocation(getProgram(), "isBlur");
    }

    public void onInitialized() {
        super.onInitialized();
        setRatio(this.mRatio);
        setIsBlur(this.mIsBlur);
        setBlurSize(this.mBlurSize);
    }

    public void setRatio(float ratio) {
        this.mRatio = ratio;
        setFloat(this.mRatioLocation, ratio);
    }

    public void setIsBlur(boolean isBlur) {
        this.mIsBlur = isBlur;
        if (isBlur) {
            setInteger(this.mIsBlurLocation, 1);
        } else {
            setInteger(this.mIsBlurLocation, 0);
        }
    }

    protected void initTexelOffsets() {
        int texelWidthOffsetLocation = GLES20.glGetUniformLocation(getProgram(), "texelWidthOffset");
        int texelHeightOffsetLocation = GLES20.glGetUniformLocation(getProgram(), "texelHeightOffset");
        setFloat(texelWidthOffsetLocation, TextTrackStyle.DEFAULT_FONT_SCALE / ((float) this.mOutputWidth));
        setFloat(texelHeightOffsetLocation, 0.0f);
    }

    public void setBlurSize(float blurSize) {
        this.mBlurSize = blurSize;
        runOnDraw(new C15451());
    }
}
