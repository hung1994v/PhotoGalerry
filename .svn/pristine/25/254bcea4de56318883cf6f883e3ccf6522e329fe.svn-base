package bsoft.com.lib_filter.filter.gpu.util;

import com.google.android.gms.cast.TextTrackStyle;

public class TextureRotationUtil {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$baiwang$lib$filter$gpu$util$Rotation;
    public static final float[] TEXTURE_NO_ROTATION = new float[]{0.0f, TextTrackStyle.DEFAULT_FONT_SCALE, TextTrackStyle.DEFAULT_FONT_SCALE, TextTrackStyle.DEFAULT_FONT_SCALE, 0.0f, 0.0f, TextTrackStyle.DEFAULT_FONT_SCALE, 0.0f};
    public static final float[] TEXTURE_ROTATED_180 = new float[]{TextTrackStyle.DEFAULT_FONT_SCALE, 0.0f, 0.0f, 0.0f, TextTrackStyle.DEFAULT_FONT_SCALE, TextTrackStyle.DEFAULT_FONT_SCALE, 0.0f, TextTrackStyle.DEFAULT_FONT_SCALE};
    public static final float[] TEXTURE_ROTATED_270 = new float[]{0.0f, 0.0f, 0.0f, TextTrackStyle.DEFAULT_FONT_SCALE, TextTrackStyle.DEFAULT_FONT_SCALE, 0.0f, TextTrackStyle.DEFAULT_FONT_SCALE, TextTrackStyle.DEFAULT_FONT_SCALE};
    public static final float[] TEXTURE_ROTATED_90 = new float[]{TextTrackStyle.DEFAULT_FONT_SCALE, TextTrackStyle.DEFAULT_FONT_SCALE, TextTrackStyle.DEFAULT_FONT_SCALE, 0.0f, 0.0f, TextTrackStyle.DEFAULT_FONT_SCALE, 0.0f, 0.0f};

    static /* synthetic */ int[] $SWITCH_TABLE$com$baiwang$lib$filter$gpu$util$Rotation() {
        int[] iArr = $SWITCH_TABLE$com$baiwang$lib$filter$gpu$util$Rotation;
        if (iArr == null) {
            iArr = new int[Rotation.values().length];
            try {
                iArr[Rotation.NORMAL.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[Rotation.ROTATION_180.ordinal()] = 3;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[Rotation.ROTATION_270.ordinal()] = 4;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[Rotation.ROTATION_90.ordinal()] = 2;
            } catch (NoSuchFieldError e4) {
            }
            $SWITCH_TABLE$com$baiwang$lib$filter$gpu$util$Rotation = iArr;
        }
        return iArr;
    }

    private TextureRotationUtil() {
    }

    public static float[] getRotation(Rotation rotation, boolean flipHorizontal, boolean flipVertical) {
        float[] rotatedTex;
        switch ($SWITCH_TABLE$com$baiwang$lib$filter$gpu$util$Rotation()[rotation.ordinal()]) {
            case 2:
                rotatedTex = TEXTURE_ROTATED_90;
                break;
            case 3:
                rotatedTex = TEXTURE_ROTATED_180;
                break;
            case 4:
                rotatedTex = TEXTURE_ROTATED_270;
                break;
            default:
                rotatedTex = TEXTURE_NO_ROTATION;
                break;
        }
        if (flipHorizontal) {
            rotatedTex = new float[]{flip(rotatedTex[0]), rotatedTex[1], flip(rotatedTex[2]), rotatedTex[3], flip(rotatedTex[4]), rotatedTex[5], flip(rotatedTex[6]), rotatedTex[7]};
        }
        if (!flipVertical) {
            return rotatedTex;
        }
        return new float[]{rotatedTex[0], flip(rotatedTex[1]), rotatedTex[2], flip(rotatedTex[3]), rotatedTex[4], flip(rotatedTex[5]), rotatedTex[6], flip(rotatedTex[7])};
    }

    private static float flip(float i) {
        if (i == 0.0f) {
            return TextTrackStyle.DEFAULT_FONT_SCALE;
        }
        return 0.0f;
    }
}
