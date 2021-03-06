package bsoft.com.lib_filter.filter.gpu;

import android.content.Context;
import android.graphics.PointF;
import androidx.annotation.FloatRange;
import android.util.Log;


import com.google.android.gms.cast.TextTrackStyle;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import bsoft.com.lib_filter.filter.GPUAdjustRange;
import bsoft.com.lib_filter.filter.gpu.adjust.GPUImageBrightnessFilter;
import bsoft.com.lib_filter.filter.gpu.adjust.GPUImageContrastFilter;
import bsoft.com.lib_filter.filter.gpu.adjust.GPUImageExposureFilter;
import bsoft.com.lib_filter.filter.gpu.adjust.GPUImageGammaFilter;
import bsoft.com.lib_filter.filter.gpu.adjust.GPUImageGaussianBlurSimpleFilter;
import bsoft.com.lib_filter.filter.gpu.adjust.GPUImageHighlightShadowFilter;
import bsoft.com.lib_filter.filter.gpu.adjust.GPUImageHueFilter;
import bsoft.com.lib_filter.filter.gpu.adjust.GPUImageRGBFilter;
import bsoft.com.lib_filter.filter.gpu.adjust.GPUImageSaturationFilter;
import bsoft.com.lib_filter.filter.gpu.adjust.GPUImageSharpenFilter;
import bsoft.com.lib_filter.filter.gpu.adjust.GPUImageSoftLightFilter;
import bsoft.com.lib_filter.filter.gpu.adjust.GPUImageWhiteBalanceFilter;
import bsoft.com.lib_filter.filter.gpu.blend.GPUImageAddBlendFilter;
import bsoft.com.lib_filter.filter.gpu.blend.GPUImageChromaKeyBlendFilter;
import bsoft.com.lib_filter.filter.gpu.blend.GPUImageColorBlendFilter;
import bsoft.com.lib_filter.filter.gpu.blend.GPUImageColorBurnBlendFilter;
import bsoft.com.lib_filter.filter.gpu.blend.GPUImageColorDodgeBlendFilter;
import bsoft.com.lib_filter.filter.gpu.blend.GPUImageDarkenBlendFilter;
import bsoft.com.lib_filter.filter.gpu.blend.GPUImageDifferenceBlendFilter;
import bsoft.com.lib_filter.filter.gpu.blend.GPUImageDissolveBlendFilter;
import bsoft.com.lib_filter.filter.gpu.blend.GPUImageDivideBlendFilter;
import bsoft.com.lib_filter.filter.gpu.blend.GPUImageExclusionBlendFilter;
import bsoft.com.lib_filter.filter.gpu.blend.GPUImageHardLightBlendFilter;
import bsoft.com.lib_filter.filter.gpu.blend.GPUImageHueBlendFilter;
import bsoft.com.lib_filter.filter.gpu.blend.GPUImageLightenBlendFilter;
import bsoft.com.lib_filter.filter.gpu.blend.GPUImageLinearBurnBlendFilter;
import bsoft.com.lib_filter.filter.gpu.blend.GPUImageLuminosityBlendFilter;
import bsoft.com.lib_filter.filter.gpu.blend.GPUImageMapBlendFilter;
import bsoft.com.lib_filter.filter.gpu.blend.GPUImageMapSelfBlendFilter;
import bsoft.com.lib_filter.filter.gpu.blend.GPUImageMultiplyBlendFilter;
import bsoft.com.lib_filter.filter.gpu.blend.GPUImageNormalBlendFilter;
import bsoft.com.lib_filter.filter.gpu.blend.GPUImageOverlayBlendFilter;
import bsoft.com.lib_filter.filter.gpu.blend.GPUImageSaturationBlendFilter;
import bsoft.com.lib_filter.filter.gpu.blend.GPUImageScreenBlendFilter;
import bsoft.com.lib_filter.filter.gpu.blend.GPUImageSoftLightBlendFilter;
import bsoft.com.lib_filter.filter.gpu.blend.GPUImageSourceOverBlendFilter;
import bsoft.com.lib_filter.filter.gpu.blend.GPUImageSubtractBlendFilter;
import bsoft.com.lib_filter.filter.gpu.fade.GPUFadeBeautifullyFilter;
import bsoft.com.lib_filter.filter.gpu.fade.GPUFadeCoolHazeFilter;
import bsoft.com.lib_filter.filter.gpu.father.GPUImageFilter;
import bsoft.com.lib_filter.filter.gpu.father.GPUImageFilterGroup;
import bsoft.com.lib_filter.filter.gpu.film.Film18Filter;
import bsoft.com.lib_filter.filter.gpu.film.FilmBVolTemplateFilter;
import bsoft.com.lib_filter.filter.gpu.film.FilmCP12Filter;
import bsoft.com.lib_filter.filter.gpu.film.FilmCarinaFilter;
import bsoft.com.lib_filter.filter.gpu.film.FilmClassicBlueFilter;
import bsoft.com.lib_filter.filter.gpu.film.FilmCoolBreezeFilter;
import bsoft.com.lib_filter.filter.gpu.film.FilmCoolerFilter;
import bsoft.com.lib_filter.filter.gpu.film.FilmFreeSpiritFilter;
import bsoft.com.lib_filter.filter.gpu.film.FilmNightFate3Filter;
import bsoft.com.lib_filter.filter.gpu.film.FilmPaprikaFilter;
import bsoft.com.lib_filter.filter.gpu.film.FilmPremium31Filter;
import bsoft.com.lib_filter.filter.gpu.film.FilmRendezvousFilter;
import bsoft.com.lib_filter.filter.gpu.food.FoodAdjustToneCoolShadowsFilter;
import bsoft.com.lib_filter.filter.gpu.food.FoodCaliFilter;
import bsoft.com.lib_filter.filter.gpu.food.FoodIceFilter;
import bsoft.com.lib_filter.filter.gpu.newfilter.GPUImageColorBalanceFilter;
import bsoft.com.lib_filter.filter.gpu.newfilter.GPUImageGaussianBlurFilter;
import bsoft.com.lib_filter.filter.gpu.normal.GPUImage3x3ConvolutionFilter;
import bsoft.com.lib_filter.filter.gpu.normal.GPUImageABaoFilter;
import bsoft.com.lib_filter.filter.gpu.normal.GPUImageColorInvertFilter;
import bsoft.com.lib_filter.filter.gpu.normal.GPUImageEmbossFilter;
import bsoft.com.lib_filter.filter.gpu.normal.GPUImageLookupFilter;
import bsoft.com.lib_filter.filter.gpu.normal.GPUImageMonochromeFilter;
import bsoft.com.lib_filter.filter.gpu.normal.GPUImageNoFilter;
import bsoft.com.lib_filter.filter.gpu.normal.GPUImageOpacityFilter;
import bsoft.com.lib_filter.filter.gpu.normal.GPUImagePixelationFilter;
import bsoft.com.lib_filter.filter.gpu.normal.GPUImagePosterizeFilter;
import bsoft.com.lib_filter.filter.gpu.normal.GPUImageSepiaFilter;
import bsoft.com.lib_filter.filter.gpu.normal.GPUImageSobelEdgeDetection;
import bsoft.com.lib_filter.filter.gpu.normal.GPUImageToneCurveFilter;
import bsoft.com.lib_filter.filter.gpu.normal.GPUImageToneCurveMapFilter;
import bsoft.com.lib_filter.filter.gpu.retro.Retro17Filter;
import bsoft.com.lib_filter.filter.gpu.retro.RetroCVol8Filter;
import bsoft.com.lib_filter.filter.gpu.retro.RetroChestnutBrownFilter;
import bsoft.com.lib_filter.filter.gpu.retro.RetroDelicateBrownFilter;
import bsoft.com.lib_filter.filter.gpu.retro.RetroPremiumFilter;
import bsoft.com.lib_filter.filter.gpu.retro.RetroVintageFilter;
import bsoft.com.lib_filter.filter.gpu.scene.GPUImageSceneLevelControlFilter;
import bsoft.com.lib_filter.filter.gpu.scene.GPUImageSceneLowSaturationFilter;
import bsoft.com.lib_filter.filter.gpu.season.GPUSeasonAutumnGentleFilter;
import bsoft.com.lib_filter.filter.gpu.season.GPUSeasonAutumnPremiumFilter;
import bsoft.com.lib_filter.filter.gpu.season.GPUSeasonGloriousSpringBabyFilter;
import bsoft.com.lib_filter.filter.gpu.season.GPUSeasonSpringBlossomFilter;
import bsoft.com.lib_filter.filter.gpu.season.GPUSeasonSpringLightFilter;
import bsoft.com.lib_filter.filter.gpu.season.GPUSeasonSummerDayFilter;
import bsoft.com.lib_filter.filter.gpu.season.GPUSeasonSummerIndianFilter;
import bsoft.com.lib_filter.filter.gpu.season.GPUSeasonWinterIcedFilter;
import bsoft.com.lib_filter.filter.gpu.season.GPUSeasonWinterSnappyBabyFilter;
import bsoft.com.lib_filter.filter.gpu.season.GPUSeasonWinterSoftBrownFilter;
import bsoft.com.lib_filter.filter.gpu.sweet.GPUSweetPremiumFilter;
import bsoft.com.lib_filter.filter.gpu.sweet.GPUSweetRomanceFilter;
import bsoft.com.lib_filter.filter.gpu.sweet.GPUSweetRustyTintFilter;
import bsoft.com.lib_filter.filter.gpu.sweet.GPUSweetSoCoolFilter;
import bsoft.com.lib_filter.filter.gpu.tonewithblend.GPUImageToneCurveLuminosityBlendFilter;
import bsoft.com.lib_filter.filter.gpu.vignette.GPUImageVignetteBrightnessFilter;
import bsoft.com.lib_filter.filter.gpu.vignette.GPUImageVignetteColorFilter;
import bsoft.com.lib_filter.filter.gpu.vignette.GPUImageVignetteColorInvertFilter;
import bsoft.com.lib_filter.filter.gpu.vignette.GPUImageVignetteContrastFilter;
import bsoft.com.lib_filter.filter.gpu.vignette.GPUImageVignetteExposureFilter;
import bsoft.com.lib_filter.filter.gpu.vignette.GPUImageVignetteFilter;
import bsoft.com.lib_filter.filter.gpu.vignette.GPUImageVignetteGammaFilter;
import bsoft.com.lib_filter.filter.gpu.vignette.GPUImageVignetteGaussianBlurFilter;
import bsoft.com.lib_filter.filter.gpu.vignette.GPUImageVignetteGrayscaleFilter;
import bsoft.com.lib_filter.filter.gpu.vignette.GPUImageVignetteHueFilter;
import bsoft.com.lib_filter.filter.gpu.vignette.GPUImageVignetteMapSelfBlendFilter;
import bsoft.com.lib_filter.filter.gpu.vignette.GPUImageVignetteSharpenFilter;
import bsoft.com.lib_filter.filter.gpu.vignette.GPUImageVignetteToneCurveFilter;
import bsoft.com.lib_filter.filter.gpu.vignette.GPUImageVignetteToneCurveMapFilter;


public class GPUFilterFactory {
    private static int[] $SWITCH_TABLE$com$baiwang$lib$filter$gpu$GPUFilterType;
    public static final int MM_CONTACTIMGFLAG_LOCAL_EXIST = 153;
    public static final int CUSTOM_PROPERTY_SIZE_LIMIT_BYTES = 124;
    public static final int SELECT_RECENTLY_FAILED = 103;
    public static final int PRIORITY_LOW_POWER = 104;
    public static final int PRIORITY_NO_POWER = 105;
    public static final int SELECT_COMPLETED_UNCLAIMED = 101;
    public static final float HUE_YELLOW = 60.0f;
    public static final float HUE_ORANGE = 30.0f;


    static int[] $SWITCH_TABLE$com$baiwang$lib$filter$gpu$GPUFilterType() {
        int[] iArr = $SWITCH_TABLE$com$baiwang$lib$filter$gpu$GPUFilterType;
        if (iArr == null) {
            iArr = new int[GPUFilterType.values().length];
            try {
                iArr[GPUFilterType.ABAO.ordinal()] = 134;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[GPUFilterType.ABAO2.ordinal()] = 136;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[GPUFilterType.ALSA.ordinal()] = 328;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[GPUFilterType.AMARO.ordinal()] = 98;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[GPUFilterType.ASHBY.ordinal()] = 121;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[GPUFilterType.AUDREY.ordinal()] = 335;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[GPUFilterType.BETTY.ordinal()] = 332;
            } catch (NoSuchFieldError e7) {
            }
            try {
                iArr[GPUFilterType.BLEND_ADD.ordinal()] = 236;
            } catch (NoSuchFieldError e8) {
            }
            try {
                iArr[GPUFilterType.BLEND_CHROMA_KEY.ordinal()] = 248;
            } catch (NoSuchFieldError e9) {
            }
            try {
                iArr[GPUFilterType.BLEND_COLOR.ordinal()] = 241;
            } catch (NoSuchFieldError e10) {
            }
            try {
                iArr[GPUFilterType.BLEND_COLOR_BURN.ordinal()] = 227;
            } catch (NoSuchFieldError e11) {
            }
            try {
                iArr[GPUFilterType.BLEND_COLOR_DODGE.ordinal()] = 228;
            } catch (NoSuchFieldError e12) {
            }
            try {
                iArr[GPUFilterType.BLEND_DARKEN.ordinal()] = 229;
            } catch (NoSuchFieldError e13) {
            }
            try {
                iArr[GPUFilterType.BLEND_DIFFERENCE.ordinal()] = 230;
            } catch (NoSuchFieldError e14) {
            }
            try {
                iArr[GPUFilterType.BLEND_DISSOLVE.ordinal()] = 231;
            } catch (NoSuchFieldError e15) {
            }
            try {
                iArr[GPUFilterType.BLEND_DIVIDE.ordinal()] = 237;
            } catch (NoSuchFieldError e16) {
            }
            try {
                iArr[GPUFilterType.BLEND_EXCLUSION.ordinal()] = 232;
            } catch (NoSuchFieldError e17) {
            }
            try {
                iArr[GPUFilterType.BLEND_HARD_LIGHT.ordinal()] = 234;
            } catch (NoSuchFieldError e18) {
            }
            try {
                iArr[GPUFilterType.BLEND_HUE.ordinal()] = 242;
            } catch (NoSuchFieldError e19) {
            }
            try {
                iArr[GPUFilterType.BLEND_LIGHTEN.ordinal()] = 235;
            } catch (NoSuchFieldError e20) {
            }
            try {
                iArr[GPUFilterType.BLEND_LINEAR_BURN.ordinal()] = 245;
            } catch (NoSuchFieldError e21) {
            }
            try {
                iArr[GPUFilterType.BLEND_LUMINOSITY.ordinal()] = 244;
            } catch (NoSuchFieldError e22) {
            }
            try {
                iArr[GPUFilterType.BLEND_MULTIPLY.ordinal()] = 238;
            } catch (NoSuchFieldError e23) {
            }
            try {
                iArr[GPUFilterType.BLEND_NORMAL.ordinal()] = 249;
            } catch (NoSuchFieldError e24) {
            }
            try {
                iArr[GPUFilterType.BLEND_OVERLAY.ordinal()] = 239;
            } catch (NoSuchFieldError e25) {
            }
            try {
                iArr[GPUFilterType.BLEND_SATURATION.ordinal()] = 243;
            } catch (NoSuchFieldError e26) {
            }
            try {
                iArr[GPUFilterType.BLEND_SCREEN.ordinal()] = 240;
            } catch (NoSuchFieldError e27) {
            }
            try {
                iArr[GPUFilterType.BLEND_SOFT_LIGHT.ordinal()] = 246;
            } catch (NoSuchFieldError e28) {
            }
            try {
                iArr[GPUFilterType.BLEND_SOURCE_OVER.ordinal()] = 233;
            } catch (NoSuchFieldError e29) {
            }
            try {
                iArr[GPUFilterType.BLEND_SUBTRACT.ordinal()] = 247;
            } catch (NoSuchFieldError e30) {
            }
            try {
                iArr[GPUFilterType.BRANNAN.ordinal()] = 105;
            } catch (NoSuchFieldError e31) {
            }
            try {
                iArr[GPUFilterType.BRIGHTNESS.ordinal()] = 210;
            } catch (NoSuchFieldError e32) {
            }
            try {
                iArr[GPUFilterType.BROOKLYN.ordinal()] = 122;
            } catch (NoSuchFieldError e33) {
            }
            try {
                iArr[GPUFilterType.BWRetro.ordinal()] = 116;
            } catch (NoSuchFieldError e34) {
            }
            try {
                iArr[GPUFilterType.CHARMES.ordinal()] = 123;
            } catch (NoSuchFieldError e35) {
            }
            try {
                iArr[GPUFilterType.CLARENDON.ordinal()] = 124;
            } catch (NoSuchFieldError e36) {
            }
            try {
                iArr[GPUFilterType.COM_FILTER_GROUP.ordinal()] = 269;
            } catch (NoSuchFieldError e37) {
            }
            try {
                iArr[GPUFilterType.CONTRAST.ordinal()] = 207;
            } catch (NoSuchFieldError e38) {
            }
            try {
                iArr[GPUFilterType.DAT_BANBO.ordinal()] = 150;
            } catch (NoSuchFieldError e39) {
            }
            try {
                iArr[GPUFilterType.DAT_BETTERSKIN.ordinal()] = 151;
            } catch (NoSuchFieldError e40) {
            }
            try {
                iArr[GPUFilterType.DAT_DEEPWHITE.ordinal()] = 152;
            } catch (NoSuchFieldError e41) {
            }
            try {
                iArr[GPUFilterType.DAT_FENNEN.ordinal()] = 147;
            } catch (NoSuchFieldError e42) {
            }
            try {
                iArr[GPUFilterType.DAT_HDR.ordinal()] = 153;
            } catch (NoSuchFieldError e43) {
            }
            try {
                iArr[GPUFilterType.DAT_HEIBAI.ordinal()] = 143;
            } catch (NoSuchFieldError e44) {
            }
            try {
                iArr[GPUFilterType.DAT_HUIYI.ordinal()] = 149;
            } catch (NoSuchFieldError e45) {
            }
            try {
                iArr[GPUFilterType.DAT_JDHDR.ordinal()] = 154;
            } catch (NoSuchFieldError e46) {
            }
            try {
                iArr[GPUFilterType.DAT_LANDIAO.ordinal()] = 139;
            } catch (NoSuchFieldError e47) {
            }
            try {
                iArr[GPUFilterType.DAT_LANDIAOPATH.ordinal()] = 138;
            } catch (NoSuchFieldError e48) {
            }
            try {
                iArr[GPUFilterType.DAT_LOMO.ordinal()] = 142;
            } catch (NoSuchFieldError e49) {
            }
            try {
                iArr[GPUFilterType.DAT_LOMOPATH.ordinal()] = 141;
            } catch (NoSuchFieldError e50) {
            }
            try {
                iArr[GPUFilterType.DAT_NATURALWHITE.ordinal()] = 155;
            } catch (NoSuchFieldError e51) {
            }
            try {
                iArr[GPUFilterType.DAT_QINGXIN.ordinal()] = 146;
            } catch (NoSuchFieldError e52) {
            }
            try {
                iArr[GPUFilterType.DAT_QIUSE.ordinal()] = 148;
            } catch (NoSuchFieldError e53) {
            }
            try {
                iArr[GPUFilterType.DAT_SHENLAN.ordinal()] = 145;
            } catch (NoSuchFieldError e54) {
            }
            try {
                iArr[GPUFilterType.DAT_SKINSMOOTH.ordinal()] = 156;
            } catch (NoSuchFieldError e55) {
            }
            try {
                iArr[GPUFilterType.DAT_SUNNY.ordinal()] = 157;
            } catch (NoSuchFieldError e56) {
            }
            try {
                iArr[GPUFilterType.DAT_SWEETY.ordinal()] = 158;
            } catch (NoSuchFieldError e57) {
            }
            try {
                iArr[GPUFilterType.DAT_WEIMEI.ordinal()] = 144;
            } catch (NoSuchFieldError e58) {
            }
            try {
                iArr[GPUFilterType.DAT_XIAOZHEN.ordinal()] = 140;
            } catch (NoSuchFieldError e59) {
            }
            try {
                iArr[GPUFilterType.DAT_ZIRAN.ordinal()] = 137;
            } catch (NoSuchFieldError e60) {
            }
            try {
                iArr[GPUFilterType.DOGPATCH.ordinal()] = 125;
            } catch (NoSuchFieldError e61) {
            }
            try {
                iArr[GPUFilterType.EARLYBIRD.ordinal()] = 117;
            } catch (NoSuchFieldError e62) {
            }
            try {
                iArr[GPUFilterType.EMBOSS.ordinal()] = 219;
            } catch (NoSuchFieldError e63) {
            }
            try {
                iArr[GPUFilterType.EXPOSURE.ordinal()] = 212;
            } catch (NoSuchFieldError e64) {
            }
            try {
                iArr[GPUFilterType.F0.ordinal()] = 3;
            } catch (NoSuchFieldError e65) {
            }
            try {
                iArr[GPUFilterType.F1.ordinal()] = 4;
            } catch (NoSuchFieldError e66) {
            }
            try {
                iArr[GPUFilterType.F10.ordinal()] = 13;
            } catch (NoSuchFieldError e67) {
            }
            try {
                iArr[GPUFilterType.F11.ordinal()] = 14;
            } catch (NoSuchFieldError e68) {
            }
            try {
                iArr[GPUFilterType.F12.ordinal()] = 15;
            } catch (NoSuchFieldError e69) {
            }
            try {
                iArr[GPUFilterType.F13.ordinal()] = 16;
            } catch (NoSuchFieldError e70) {
            }
            try {
                iArr[GPUFilterType.F14.ordinal()] = 17;
            } catch (NoSuchFieldError e71) {
            }
            try {
                iArr[GPUFilterType.F15.ordinal()] = 18;
            } catch (NoSuchFieldError e72) {
            }
            try {
                iArr[GPUFilterType.F16.ordinal()] = 19;
            } catch (NoSuchFieldError e73) {
            }
            try {
                iArr[GPUFilterType.F17.ordinal()] = 20;
            } catch (NoSuchFieldError e74) {
            }
            try {
                iArr[GPUFilterType.F18.ordinal()] = 21;
            } catch (NoSuchFieldError e75) {
            }
            try {
                iArr[GPUFilterType.F19.ordinal()] = 22;
            } catch (NoSuchFieldError e76) {
            }
            try {
                iArr[GPUFilterType.F2.ordinal()] = 5;
            } catch (NoSuchFieldError e77) {
            }
            try {
                iArr[GPUFilterType.F20.ordinal()] = 23;
            } catch (NoSuchFieldError e78) {
            }
            try {
                iArr[GPUFilterType.F21.ordinal()] = 24;
            } catch (NoSuchFieldError e79) {
            }
            try {
                iArr[GPUFilterType.F22.ordinal()] = 25;
            } catch (NoSuchFieldError e80) {
            }
            try {
                iArr[GPUFilterType.F23.ordinal()] = 26;
            } catch (NoSuchFieldError e81) {
            }
            try {
                iArr[GPUFilterType.F24.ordinal()] = 27;
            } catch (NoSuchFieldError e82) {
            }
            try {
                iArr[GPUFilterType.F25.ordinal()] = 28;
            } catch (NoSuchFieldError e83) {
            }
            try {
                iArr[GPUFilterType.F26.ordinal()] = 29;
            } catch (NoSuchFieldError e84) {
            }
            try {
                iArr[GPUFilterType.F27.ordinal()] = 30;
            } catch (NoSuchFieldError e85) {
            }
            try {
                iArr[GPUFilterType.F28.ordinal()] = 31;
            } catch (NoSuchFieldError e86) {
            }
            try {
                iArr[GPUFilterType.F29.ordinal()] = 32;
            } catch (NoSuchFieldError e87) {
            }
            try {
                iArr[GPUFilterType.F3.ordinal()] = 6;
            } catch (NoSuchFieldError e88) {
            }
            try {
                iArr[GPUFilterType.F30.ordinal()] = 33;
            } catch (NoSuchFieldError e89) {
            }
            try {
                iArr[GPUFilterType.F31.ordinal()] = 34;
            } catch (NoSuchFieldError e90) {
            }
            try {
                iArr[GPUFilterType.F32.ordinal()] = 35;
            } catch (NoSuchFieldError e91) {
            }
            try {
                iArr[GPUFilterType.F33.ordinal()] = 36;
            } catch (NoSuchFieldError e92) {
            }
            try {
                iArr[GPUFilterType.F34.ordinal()] = 37;
            } catch (NoSuchFieldError e93) {
            }
            try {
                iArr[GPUFilterType.F35.ordinal()] = 38;
            } catch (NoSuchFieldError e94) {
            }
            try {
                iArr[GPUFilterType.F36.ordinal()] = 39;
            } catch (NoSuchFieldError e95) {
            }
            try {
                iArr[GPUFilterType.F37.ordinal()] = 40;
            } catch (NoSuchFieldError e96) {
            }
            try {
                iArr[GPUFilterType.F38.ordinal()] = 41;
            } catch (NoSuchFieldError e97) {
            }
            try {
                iArr[GPUFilterType.F39.ordinal()] = 42;
            } catch (NoSuchFieldError e98) {
            }
            try {
                iArr[GPUFilterType.F4.ordinal()] = 7;
            } catch (NoSuchFieldError e99) {
            }
            try {
                iArr[GPUFilterType.F40.ordinal()] = 43;
            } catch (NoSuchFieldError e100) {
            }
            try {
                iArr[GPUFilterType.F41.ordinal()] = 44;
            } catch (NoSuchFieldError e101) {
            }
            try {
                iArr[GPUFilterType.F42.ordinal()] = 45;
            } catch (NoSuchFieldError e102) {
            }
            try {
                iArr[GPUFilterType.F43.ordinal()] = 46;
            } catch (NoSuchFieldError e103) {
            }
            try {
                iArr[GPUFilterType.F44.ordinal()] = 47;
            } catch (NoSuchFieldError e104) {
            }
            try {
                iArr[GPUFilterType.F45.ordinal()] = 48;
            } catch (NoSuchFieldError e105) {
            }
            try {
                iArr[GPUFilterType.F46.ordinal()] = 49;
            } catch (NoSuchFieldError e106) {
            }
            try {
                iArr[GPUFilterType.F47.ordinal()] = 50;
            } catch (NoSuchFieldError e107) {
            }
            try {
                iArr[GPUFilterType.F48.ordinal()] = 51;
            } catch (NoSuchFieldError e108) {
            }
            try {
                iArr[GPUFilterType.F49.ordinal()] = 52;
            } catch (NoSuchFieldError e109) {
            }
            try {
                iArr[GPUFilterType.F5.ordinal()] = 8;
            } catch (NoSuchFieldError e110) {
            }
            try {
                iArr[GPUFilterType.F50.ordinal()] = 53;
            } catch (NoSuchFieldError e111) {
            }
            try {
                iArr[GPUFilterType.F51.ordinal()] = 54;
            } catch (NoSuchFieldError e112) {
            }
            try {
                iArr[GPUFilterType.F52.ordinal()] = 55;
            } catch (NoSuchFieldError e113) {
            }
            try {
                iArr[GPUFilterType.F53.ordinal()] = 56;
            } catch (NoSuchFieldError e114) {
            }
            try {
                iArr[GPUFilterType.F54.ordinal()] = 57;
            } catch (NoSuchFieldError e115) {
            }
            try {
                iArr[GPUFilterType.F55.ordinal()] = 58;
            } catch (NoSuchFieldError e116) {
            }
            try {
                iArr[GPUFilterType.F56.ordinal()] = 59;
            } catch (NoSuchFieldError e117) {
            }
            try {
                iArr[GPUFilterType.F57.ordinal()] = 60;
            } catch (NoSuchFieldError e118) {
            }
            try {
                iArr[GPUFilterType.F58.ordinal()] = 61;
            } catch (NoSuchFieldError e119) {
            }
            try {
                iArr[GPUFilterType.F59.ordinal()] = 62;
            } catch (NoSuchFieldError e120) {
            }
            try {
                iArr[GPUFilterType.F6.ordinal()] = 9;
            } catch (NoSuchFieldError e121) {
            }
            try {
                iArr[GPUFilterType.F60.ordinal()] = 63;
            } catch (NoSuchFieldError e122) {
            }
            try {
                iArr[GPUFilterType.F61.ordinal()] = 64;
            } catch (NoSuchFieldError e123) {
            }
            try {
                iArr[GPUFilterType.F62.ordinal()] = 65;
            } catch (NoSuchFieldError e124) {
            }
            try {
                iArr[GPUFilterType.F63.ordinal()] = 66;
            } catch (NoSuchFieldError e125) {
            }
            try {
                iArr[GPUFilterType.F64.ordinal()] = 67;
            } catch (NoSuchFieldError e126) {
            }
            try {
                iArr[GPUFilterType.F65.ordinal()] = 68;
            } catch (NoSuchFieldError e127) {
            }
            try {
                iArr[GPUFilterType.F66.ordinal()] = 69;
            } catch (NoSuchFieldError e128) {
            }
            try {
                iArr[GPUFilterType.F67.ordinal()] = 70;
            } catch (NoSuchFieldError e129) {
            }
            try {
                iArr[GPUFilterType.F68.ordinal()] = 71;
            } catch (NoSuchFieldError e130) {
            }
            try {
                iArr[GPUFilterType.F69.ordinal()] = 72;
            } catch (NoSuchFieldError e131) {
            }
            try {
                iArr[GPUFilterType.F7.ordinal()] = 10;
            } catch (NoSuchFieldError e132) {
            }
            try {
                iArr[GPUFilterType.F70.ordinal()] = 73;
            } catch (NoSuchFieldError e133) {
            }
            try {
                iArr[GPUFilterType.F71.ordinal()] = 74;
            } catch (NoSuchFieldError e134) {
            }
            try {
                iArr[GPUFilterType.F72.ordinal()] = 75;
            } catch (NoSuchFieldError e135) {
            }
            try {
                iArr[GPUFilterType.F73.ordinal()] = 76;
            } catch (NoSuchFieldError e136) {
            }
            try {
                iArr[GPUFilterType.F74.ordinal()] = 77;
            } catch (NoSuchFieldError e137) {
            }
            try {
                iArr[GPUFilterType.F75.ordinal()] = 78;
            } catch (NoSuchFieldError e138) {
            }
            try {
                iArr[GPUFilterType.F76.ordinal()] = 79;
            } catch (NoSuchFieldError e139) {
            }
            try {
                iArr[GPUFilterType.F77.ordinal()] = 80;
            } catch (NoSuchFieldError e140) {
            }
            try {
                iArr[GPUFilterType.F78.ordinal()] = 81;
            } catch (NoSuchFieldError e141) {
            }
            try {
                iArr[GPUFilterType.F79.ordinal()] = 82;
            } catch (NoSuchFieldError e142) {
            }
            try {
                iArr[GPUFilterType.F8.ordinal()] = 11;
            } catch (NoSuchFieldError e143) {
            }
            try {
                iArr[GPUFilterType.F80.ordinal()] = 83;
            } catch (NoSuchFieldError e144) {
            }
            try {
                iArr[GPUFilterType.F81.ordinal()] = 84;
            } catch (NoSuchFieldError e145) {
            }
            try {
                iArr[GPUFilterType.F82.ordinal()] = 85;
            } catch (NoSuchFieldError e146) {
            }
            try {
                iArr[GPUFilterType.F83.ordinal()] = 86;
            } catch (NoSuchFieldError e147) {
            }
            try {
                iArr[GPUFilterType.F84.ordinal()] = 87;
            } catch (NoSuchFieldError e148) {
            }
            try {
                iArr[GPUFilterType.F85.ordinal()] = 88;
            } catch (NoSuchFieldError e149) {
            }
            try {
                iArr[GPUFilterType.F86.ordinal()] = 89;
            } catch (NoSuchFieldError e150) {
            }
            try {
                iArr[GPUFilterType.F87.ordinal()] = 90;
            } catch (NoSuchFieldError e151) {
            }
            try {
                iArr[GPUFilterType.F88.ordinal()] = 91;
            } catch (NoSuchFieldError e152) {
            }
            try {
                iArr[GPUFilterType.F89.ordinal()] = 92;
            } catch (NoSuchFieldError e153) {
            }
            try {
                iArr[GPUFilterType.VSCO14.ordinal()] = 107;
            } catch (NoSuchFieldError e154) {
            }
            try {
                iArr[GPUFilterType.F9.ordinal()] = 12;
            } catch (NoSuchFieldError e155) {
            }
            try {
                iArr[GPUFilterType.F90.ordinal()] = 93;
            } catch (NoSuchFieldError e156) {
            }
            try {
                iArr[GPUFilterType.F91.ordinal()] = 94;
            } catch (NoSuchFieldError e157) {
            }
            try {
                iArr[GPUFilterType.F92.ordinal()] = 95;
            } catch (NoSuchFieldError e158) {
            }
            try {
                iArr[GPUFilterType.F93.ordinal()] = 96;
            } catch (NoSuchFieldError e159) {
            }
            try {
                iArr[GPUFilterType.F94.ordinal()] = 97;
            } catch (NoSuchFieldError e160) {
            }
            try {
                iArr[GPUFilterType.FADE_BEAUTIFULLY.ordinal()] = 467;
            } catch (NoSuchFieldError e161) {
            }
            try {
                iArr[GPUFilterType.FADE_COOL_HAZE.ordinal()] = 486;
            } catch (NoSuchFieldError e162) {
            }
            try {
                iArr[GPUFilterType.FADE_DARK_DESATURATE.ordinal()] = 469;
            } catch (NoSuchFieldError e163) {
            }
            try {
                iArr[GPUFilterType.FADE_DIFFUSED_MATTE.ordinal()] = 470;
            } catch (NoSuchFieldError e164) {
            }
            try {
                iArr[GPUFilterType.FADE_EVERYDAY.ordinal()] = 471;
            } catch (NoSuchFieldError e165) {
            }
            try {
                iArr[GPUFilterType.FADE_LIME.ordinal()] = 472;
            } catch (NoSuchFieldError e166) {
            }
            try {
                iArr[GPUFilterType.FADE_LUCID.ordinal()] = 473;
            } catch (NoSuchFieldError e167) {
            }
            try {
                iArr[GPUFilterType.FADE_MIST.ordinal()] = 474;
            } catch (NoSuchFieldError e168) {
            }
            try {
                iArr[GPUFilterType.FADE_RETRO.ordinal()] = 475;
            } catch (NoSuchFieldError e169) {
            }
            try {
                iArr[GPUFilterType.FADE_WHITE_WASH.ordinal()] = 476;
            } catch (NoSuchFieldError e170) {
            }
            try {
                iArr[GPUFilterType.FILM_16.ordinal()] = 501;
            } catch (NoSuchFieldError e171) {
            }
            try {
                iArr[GPUFilterType.FILM_18.ordinal()] = 520;
            } catch (NoSuchFieldError e172) {
            }
            try {
                iArr[GPUFilterType.FILM_3.ordinal()] = 502;
            } catch (NoSuchFieldError e173) {
            }
            try {
                iArr[GPUFilterType.FILM_A4.ordinal()] = 285;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.FILM_A5.ordinal()] = 286;
            } catch (NoSuchFieldError e174) {
            }

            try {
                iArr[GPUFilterType.FILM_A6.ordinal()] = 287;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.FILM_ADEN.ordinal()] = 288;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.FILM_B1.ordinal()] = 289;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.FILM_B5.ordinal()] = 290;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.FILM_B_VOL.ordinal()] = 503;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.FILM_CARINA.ordinal()] = 504;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.FILM_CLASSIC_BLUE.ordinal()] = 505;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.FILM_COOLER.ordinal()] = 507;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.FILM_COOL_BREEZE.ordinal()] = 506;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.FILM_CP_12.ordinal()] = 508;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.FILM_CREMA.ordinal()] = 291;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.FILM_FREE_SPIRIT.ordinal()] = 509;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.FILM_GREY_LIGHT.ordinal()] = 510;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.FILM_HB1.ordinal()] = 292;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.FILM_LUDWIG.ordinal()] = 293;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.FILM_LUST.ordinal()] = 511;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.FILM_M5.ordinal()] = 293;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.FILM_MARINE.ordinal()] = 512;
            } catch (NoSuchFieldError e174) {
            }


            try {
                iArr[GPUFilterType.FILM_NASHVILLE.ordinal()] = 513;
            } catch (NoSuchFieldError e174) {
            }

            try {
                iArr[GPUFilterType.FILM_NIGHT_FATE_2.ordinal()] = 521;
            } catch (NoSuchFieldError e174) {
            }

            try {
                iArr[GPUFilterType.FILM_NIGHT_FATE_3.ordinal()] = 523;
            } catch (NoSuchFieldError e174) {
            }

            try {
                iArr[GPUFilterType.FILM_NIGHT_FATE_6.ordinal()] = 522;
            } catch (NoSuchFieldError e174) {
            }

            try {
                iArr[GPUFilterType.FILM_PAPRIKA.ordinal()] = 514;
            } catch (NoSuchFieldError e174) {
            }

            try {
                iArr[GPUFilterType.FILM_PREMIUM_19.ordinal()] = 516;
            } catch (NoSuchFieldError e174) {
            }

            try {
                iArr[GPUFilterType.FILM_PREMIUM_31.ordinal()] = 517;
            } catch (NoSuchFieldError e174) {
            }

            try {
                iArr[GPUFilterType.FILM_PREMIUM_6.ordinal()] = 515;
            } catch (NoSuchFieldError e174) {
            }

            try {
                iArr[GPUFilterType.FILM_PRO_7.ordinal()] = 518;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.FILM_RENDEZVOUS.ordinal()] = 519;
            } catch (NoSuchFieldError e174) {
            }

            try {
                iArr[GPUFilterType.FILM_SLUMBER.ordinal()] = 295;
            } catch (NoSuchFieldError e174) {
            }

            try {
                iArr[GPUFilterType.FILM_T1.ordinal()] = 296;
            } catch (NoSuchFieldError e174) {
            }

            try {
                iArr[GPUFilterType.FILM_TONING_EVOLUTION.ordinal()] = 524;
            } catch (NoSuchFieldError e174) {
            }


            try {
                iArr[GPUFilterType.FILM_TONING_HAZARD.ordinal()] = 525;
            } catch (NoSuchFieldError e174) {
            }


            try {
                iArr[GPUFilterType.FILM_TONING_RUST.ordinal()] = 526;
            } catch (NoSuchFieldError e174) {
            }

            try {
                iArr[GPUFilterType.FILM_TONING_URBAN_CRIMINAL.ordinal()] = 527;
            } catch (NoSuchFieldError e174) {
            }

            try {
                iArr[GPUFilterType.FILM_VINTAGE_MADE_SIMPLE.ordinal()] = 528;
            } catch (NoSuchFieldError e174) {
            }

            try {
                iArr[GPUFilterType.FILM_WARM_TONES.ordinal()] = 529;
            } catch (NoSuchFieldError e174) {
            }

            try {
                iArr[GPUFilterType.FILM_X1.ordinal()] = 297;
            } catch (NoSuchFieldError e174) {
            }

            try {
                iArr[GPUFilterType.FOOD_ADJUST_TONE_COOL_SHADOWS.ordinal()] = 530;
            } catch (NoSuchFieldError e174) {
            }

            try {
                iArr[GPUFilterType.FOOD_BRIGHTEN_MIDTONES.ordinal()] = 531;
            } catch (NoSuchFieldError e174) {
            }


            try {
                iArr[GPUFilterType.FOOD_CALI.ordinal()] = 532;
            } catch (NoSuchFieldError e174) {
            }


            try {
                iArr[GPUFilterType.FOOD_CONTRAST_HIGH_KEY.ordinal()] = 533;
            } catch (NoSuchFieldError e174) {
            }

            try {
                iArr[GPUFilterType.FOOD_CUPCAKE.ordinal()] = 542;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.FOOD_CUSTARD.ordinal()] = 543;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.FOOD_DETAILS_PAINT_IN_SATURATION.ordinal()] = 534;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.FOOD_FIRST_CLASS.ordinal()] = 535;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.FOOD_GEMMA.ordinal()] = 536;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.FOOD_ICE.ordinal()] = 537;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.FOOD_ICE_COLD.ordinal()] = 544;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.FOOD_LUCIANA.ordinal()] = 538;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.FOOD_ORTON.ordinal()] = 539;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.FOOD_PASTA.ordinal()] = 545;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.FOOD_PRETTY_PEEPERS.ordinal()] = 540;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.FOOD_RESTORE_COLOR.ordinal()] = 541;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.FOOD_WARM_SHARP.ordinal()] = 546;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.GAMMA.ordinal()] = 209;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.GARBO.ordinal()] = 329;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.GINGHAM.ordinal()] = 126;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.GINZA.ordinal()] = 127;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.GRAYSCALE.ordinal()] = 215;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.GSBLUR.ordinal()] = 263;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.HALO1.ordinal()] = 432;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.HALO2.ordinal()] = 433;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.HALO3.ordinal()] = 434;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.HALO4.ordinal()] = 435;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.HALO5.ordinal()] = 436;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.HALO6.ordinal()] = 437;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.HALO7.ordinal()] = 438;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.HEFE.ordinal()] = 107;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.HELENA.ordinal()] = 128;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.HEPBURN.ordinal()] = 326;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.HIGHLIGHT_SHADOW.ordinal()] = 223;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.HUDSON.ordinal()] = 101;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.HUE.ordinal()] = 226;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.INGRID.ordinal()] = 330;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.INKWELL.ordinal()] = 115;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.INVERT.ordinal()] = 221;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.KELVIN.ordinal()] = 109;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LEMO_JD.ordinal()] = 276;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LEMO_JDB.ordinal()] = 283;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LEMO_JDR.ordinal()] = 281;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LEMO_JDY.ordinal()] = 282;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LEMO_PB.ordinal()] = 279;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LEMO_PB2.ordinal()] = 284;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LEMO_PO.ordinal()] = 280;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LEMO_PR.ordinal()] = 277;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LEMO_PY.ordinal()] = 278;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LILIANE.ordinal()] = 327;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LOFI.ordinal()] = 119;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LOMO1.ordinal()] = 336;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LOMO10.ordinal()] = 345;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LOMO11.ordinal()] = 346;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LOMO12.ordinal()] = 347;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LOMO13.ordinal()] = 348;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LOMO14.ordinal()] = 349;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LOMO15.ordinal()] = 350;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LOMO16.ordinal()] = 351;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LOMO17.ordinal()] = 352;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LOMO18.ordinal()] = 353;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LOMO19.ordinal()] = 254;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LOMO2.ordinal()] = 337;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LOMO20.ordinal()] = 355;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LOMO21.ordinal()] = 356;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LOMO22.ordinal()] = 357;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LOMO23.ordinal()] = 358;
            } catch (NoSuchFieldError e174) {
            } try {
                iArr[GPUFilterType.LOMO24.ordinal()] = 359;
            } catch (NoSuchFieldError e174) {
            } try {
                iArr[GPUFilterType.LOMO25.ordinal()] = 360;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LOMO26.ordinal()] = 361;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LOMO27.ordinal()] = 362;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LOMO28.ordinal()] = 363;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LOMO29.ordinal()] = 364;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LOMO3.ordinal()] = 338;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LOMO30.ordinal()] = 365;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LOMO4.ordinal()] = 339;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LOMO5.ordinal()] = 340;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LOMO6.ordinal()] = 341;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LOMO7.ordinal()] = 342;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LOMO8.ordinal()] = 343;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LOMO9.ordinal()] = 344;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.LOOKUP_AMATORKA.ordinal()] = 264;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.MAPBLEND.ordinal()] = 266;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.MAPSELFBLEND.ordinal()] = 265;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.MAVEN.ordinal()] = 129;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.MAYFAIR.ordinal()] = 99;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.MIHO.ordinal()] = 331;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.MONOCHROME.ordinal()] = 224;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.MONROE.ordinal()] = 324;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.MOON.ordinal()] = 130;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.NASHVILLE.ordinal()] = 108;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.NOFILTER.ordinal()] = 1;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.OLD_ABSINTH.ordinal()] = 161;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.OLD_ABSINTH_SHOT.ordinal()] = 182;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.OLD_BK1.ordinal()] = 167;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.OLD_BK2.ordinal()] = 168;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.OLD_BK3.ordinal()] = 169;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.OLD_BK4.ordinal()] = 170;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.OLD_BK5.ordinal()] = 171;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.OLD_BK6.ordinal()] = 172;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.OLD_BK7.ordinal()] = 173;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.OLD_BK8.ordinal()] = 174;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.OLD_BUENOS_AIRES.ordinal()] = 162;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.OLD_BUENOS_AIRES_SHOT.ordinal()] = 183;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.OLD_CLASSICSKETCH.ordinal()] = 176;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.OLD_COLORSKETCH.ordinal()] = 177;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.OLD_DENIM.ordinal()] = 163;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.OLD_DENIM2_SHOT.ordinal()] = 185;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.OLD_DENIM_SHOT.ordinal()] = 184;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.OLD_KRAFT.ordinal()] = 178;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.OLD_OLDMOVIE.ordinal()] = 180;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.OLD_OLDPHOTO.ordinal()] = 179;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.OLD_ROYAL_BLUE.ordinal()] = 164;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.OLD_ROYAL_BLUE_SHOT.ordinal()] = 186;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.OLD_SKETCH.ordinal()] = 175;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.OLD_SMOKY.ordinal()] = 165;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.OLD_SMOKY_SHOT.ordinal()] = 187;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.OLD_T7AM.ordinal()] = 159;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.OLD_TOASTER.ordinal()] = 166;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.OLD_TOASTER_SHOT.ordinal()] = 188;
            } catch (NoSuchFieldError e174) {
            }

            try {
                iArr[GPUFilterType.OLD_Y1974.ordinal()] = 160;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.OLD_Y1974_SHOT.ordinal()] = 181;
            } catch (NoSuchFieldError e174) {
            }

            try {
                iArr[GPUFilterType.OPACITY.ordinal()] = 214;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.PIXELATION.ordinal()] = 222;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.POSTERIZE.ordinal()] = 220;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.RETRO_17.ordinal()] = 496;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.RETRO_3.ordinal()] = 495;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.RETRO_AMBITIOUS.ordinal()] = 485;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.RETRO_A_VOL_1.ordinal()] = 478;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.RETRO_A_VOL_12.ordinal()] = 482;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.RETRO_A_VOL_2.ordinal()] = 479;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.RETRO_A_VOL_20.ordinal()] = 483;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.RETRO_A_VOL_22.ordinal()] = 484;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.RETRO_A_VOL_3.ordinal()] = 480;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.RETRO_A_VOL_4.ordinal()] = 481;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.RETRO_BRISK.ordinal()] = 486;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.RETRO_CHESTNUT_BROWN.ordinal()] = 490;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.RETRO_CP_24.ordinal()] = 491;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.RETRO_C_VOL_13.ordinal()] = 489;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.RETRO_C_VOL_2.ordinal()] = 487;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.RETRO_C_VOL_8.ordinal()] = 488;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.RETRO_DELICATE_BROWN.ordinal()] = 492;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.RETRO_FLASH_BACK.ordinal()] = 493;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.RETRO_NIGHT_FATE.ordinal()] = 498;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.RETRO_PREMIUM.ordinal()] = 494;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.RETRO_PS.ordinal()] = 477;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.RETRO_ROYAL.ordinal()] = 497;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.RETRO_SPIRITED.ordinal()] = 499;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.RETRO_VINTAGE.ordinal()] = 500;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.RGB.ordinal()] = 225;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.RISE.ordinal()] = 100;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.RIXI.ordinal()] = 135;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SATURATION.ordinal()] = 211;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SCENE_FJ.ordinal()] = 275;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SCENE_F_BACKLIT.ordinal()] = 306;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SCENE_F_CLOUDY.ordinal()] = 307;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SCENE_F_DARKEN.ordinal()] = 308;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SCENE_F_FLASH.ordinal()] = 309;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SCENE_F_FLUORESCENT.ordinal()] = 310;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SCENE_F_FOOD.ordinal()] = 311;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SCENE_F_LANDSCAPE.ordinal()] = 312;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SCENE_F_NIGHT.ordinal()] = 313;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SCENE_F_PORTRAIT.ordinal()] = 314;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SCENE_F_SANDSNOW.ordinal()] = 315;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SCENE_F_SHADE.ordinal()] = 316;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SCENE_F_SUNSET.ordinal()] = 317;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SCENE_F_THEATRE.ordinal()] = 318;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SCENE_MS.ordinal()] = 270;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SCENE_NG.ordinal()] = 272;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SCENE_RL.ordinal()] = 273;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SCENE_SGD.ordinal()] = 274;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SCENE_SN.ordinal()] = 271;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SCENE_WN_FENGJING.ordinal()] = 319;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SCENE_WN_SHIWU.ordinal()] = 320;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SCENE_WN_YANHUO.ordinal()] = 321;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SCENE_W_BACKLIT.ordinal()] = 301;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SCENE_W_BRIGHTEN.ordinal()] = 303;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SCENE_W_DARKEN.ordinal()] = 302;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SCENE_W_FIREWORK.ordinal()] = 298;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SCENE_W_HIGHSAT.ordinal()] = 304;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SCENE_W_LOWSAT.ordinal()] = 305;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SCENE_W_NIGHT.ordinal()] = 299;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SCENE_W_SUNSET.ordinal()] = 300;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SEASON_AUTUMN_DAWOOD_HAMADA.ordinal()] = 445;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SEASON_AUTUMN_GENTLE.ordinal()] = 446;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SEASON_AUTUMN_PREMIUM.ordinal()] = 447;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SEASON_SPRING_BLOSSOM.ordinal()] = 440;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SEASON_SPRING_GLORIOUS_BABY.ordinal()] = 439;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SEASON_SPRING_LIGHT.ordinal()] = 441;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SEASON_SUMMER_CLASSIC.ordinal()] = 442;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SEASON_SUMMER_DAY.ordinal()] = 444;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SEASON_SUMMER_INDIAN.ordinal()] = 443;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SEASON_WINTER_ICED.ordinal()] = 448;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SEASON_WINTER_SNAPPY_BABY.ordinal()] = 449;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SEASON_WINTER_SOFT_BROWN.ordinal()] = 450;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SEPIA.ordinal()] = 216;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHARPEN.ordinal()] = 208;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHIRLEY.ordinal()] = 325;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_FILM_1.ordinal()] = 398;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_FILM_10.ordinal()] = 407;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_FILM_2.ordinal()] = 399;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_FILM_3.ordinal()] = 400;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_FILM_4.ordinal()] = 401;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_FILM_5.ordinal()] = 402;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_FILM_6.ordinal()] = 403;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_FILM_7.ordinal()] = 404;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_FILM_8.ordinal()] = 405;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_FILM_9.ordinal()] = 406;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_FILM_S_1.ordinal()] = 408;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_FILM_S_2.ordinal()] = 409;
            } catch (NoSuchFieldError e174) {
            }

            try {
                iArr[GPUFilterType.SHX_FILM_S_3.ordinal()] = 410;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_FILM_S_4.ordinal()] = 411;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_FILM_S_5.ordinal()] = 412;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_FILM_S_6.ordinal()] = 413;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_LOMO_1.ordinal()] = 366;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_LOMO_10.ordinal()] = 375;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_LOMO_11.ordinal()] = 376;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_LOMO_12.ordinal()] = 377;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_LOMO_2.ordinal()] = 367;
            } catch (NoSuchFieldError e174) {
            }

            try {
                iArr[GPUFilterType.SHX_LOMO_3.ordinal()] = 368;
            } catch (NoSuchFieldError e174) {
            }

            try {
                iArr[GPUFilterType.SHX_LOMO_4.ordinal()] = 369;
            } catch (NoSuchFieldError e174) {
            }

            try {
                iArr[GPUFilterType.SHX_LOMO_5.ordinal()] = 370;
            } catch (NoSuchFieldError e174) {
            }


            try {
                iArr[GPUFilterType.SHX_LOMO_6.ordinal()] = 371;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_LOMO_7.ordinal()] = 372;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_LOMO_8.ordinal()] = 373;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_LOMO_9.ordinal()] = 374;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_PURE_1.ordinal()] = 378;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_PURE_2.ordinal()] = 379;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_PURE_3.ordinal()] = 380;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_PURE_4.ordinal()] = 381;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_PURE_5.ordinal()] = 382;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_PURE_6.ordinal()] = 383;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_PURE_7.ordinal()] = 384;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_PURE_S_1.ordinal()] = 386;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_PURE_S_10.ordinal()] = 395;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_PURE_S_11.ordinal()] = 396;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_PURE_S_12.ordinal()] = 397;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_PURE_S_2.ordinal()] = 387;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_PURE_S_3.ordinal()] = 388;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_PURE_S_4.ordinal()] = 389;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_PURE_S_5.ordinal()] = 390;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_PURE_S_6.ordinal()] = 391;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_PURE_S_7.ordinal()] = 392;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_PURE_S_8.ordinal()] = 393;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_PURE_S_9.ordinal()] = 394;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_RETRO_1.ordinal()] = 414;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_RETRO_10.ordinal()] = 423;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_RETRO_11.ordinal()] = 424;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_RETRO_12.ordinal()] = 425;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_RETRO_13.ordinal()] = 426;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_RETRO_2.ordinal()] = 415;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_RETRO_3.ordinal()] = 416;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_RETRO_4.ordinal()] = 417;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_RETRO_5.ordinal()] = 418;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_RETRO_6.ordinal()] = 419;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_RETRO_7.ordinal()] = 420;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_RETRO_8.ordinal()] = 421;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_RETRO_9.ordinal()] = 422;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_RETRO_S_1.ordinal()] = 427;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_RETRO_S_2.ordinal()] = 428;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_RETRO_S_3.ordinal()] = 429;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_RETRO_S_4.ordinal()] = 430;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SHX_RETRO_S_5.ordinal()] = 431;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SIERRA.ordinal()] = 103;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SKYLINE.ordinal()] = 131;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SOBEL_EDGE_DETECTION.ordinal()] = 217;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SOFTLIGHT.ordinal()] = 262;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SOPHIA.ordinal()] = 333;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.STINSON.ordinal()] = 132;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SURI.ordinal()] = 322;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SUTRO.ordinal()] = 118;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SWEET_ACTION.ordinal()] = 452;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SWEET_CERULEAN_BLUE.ordinal()] = 453;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SWEET_CUSTOM_CLEAN_LIGHT.ordinal()] = 466;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SWEET_DEEP_PURPLE.ordinal()] = 454;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SWEET_HAZY_TEAL.ordinal()] = 455;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SWEET_LAVENDER_HAZE.ordinal()] = 456;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SWEET_MAGENTA.ordinal()] = 457;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SWEET_MORNING_GLOW.ordinal()] = 458;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SWEET_PREMIUM.ordinal()] = 451;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SWEET_PRIMUEM.ordinal()] = 459;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SWEET_ROMANCE.ordinal()] = 460;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SWEET_RUSTY_TINT.ordinal()] = 461;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SWEET_SO_COOL.ordinal()] = 462;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SWEET_SWEET.ordinal()] = 463;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SWEET_SWEETFALLEMBRACE.ordinal()] = 464;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.SWEET_WAKE_UP.ordinal()] = 465;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.TAYLOR.ordinal()] = 323;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.TEST.ordinal()] = 2;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.THREE_X_THREE_CONVOLUTION.ordinal()] = 218;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.TOASTER.ordinal()] = 104;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.TONE_CURVE.ordinal()] = 267;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.TONE_CURVE_MAP.ordinal()] = 268;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.VALENCIA.ordinal()] = 102;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.VESPER.ordinal()] = 133;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.VIGNETTE.ordinal()] = 250;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.VIGNETTE_BRIGHTNESS.ordinal()] = 252;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.VIGNETTE_COLORINVERT.ordinal()] = 253;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.VIGNETTE_CONTRAST.ordinal()] = 251;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.VIGNETTE_EXPOSURE.ordinal()] = 254;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.VIGNETTE_GAMMA.ordinal()] = 255;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.VIGNETTE_GRAYSCALE.ordinal()] = 257;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.VIGNETTE_GSBLUR.ordinal()] = 256;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.VIGNETTE_HUE.ordinal()] = 258;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.VIGNETTE_MAPSELFBLEND.ordinal()] = 259;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.VIGNETTE_SHARPNESS.ordinal()] = 260;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.VIGNETTE_TONECURVEMAP.ordinal()] = 261;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.VIVIEN.ordinal()] = 334;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.VSCO1.ordinal()] = 189;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.VSCO10.ordinal()] = 198;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.VSCO11.ordinal()] = 199 ;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.VSCO12.ordinal()] = 200;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.VSCO13.ordinal()] = 201;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.VSCO14.ordinal()] = 202;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.VSCO15.ordinal()] = 203;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.VSCO16.ordinal()] = 204;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.VSCO17.ordinal()] = 205;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.VSCO18.ordinal()] = 206;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.VSCO2.ordinal()] = 190;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.VSCO3.ordinal()] = 191;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.VSCO4.ordinal()] = 192;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.VSCO5.ordinal()] = 193;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.VSCO6.ordinal()] = 194;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.VSCO7.ordinal()] = 195;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.VSCO8.ordinal()] = 196;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.VSCO9.ordinal()] = 197;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.WALDEN.ordinal()] = 106;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.WHITE_BALANCE.ordinal()] = 213;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.WILLOW.ordinal()] = 114;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.XPRO2.ordinal()] = 120;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.Y1970.ordinal()] = 110;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.Y1975.ordinal()] = 111;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.Y1977.ordinal()] = 112;
            } catch (NoSuchFieldError e174) {
            }
            try {
                iArr[GPUFilterType.Y1980.ordinal()] = 113;
            } catch (NoSuchFieldError e174) {
            }

            $SWITCH_TABLE$com$baiwang$lib$filter$gpu$GPUFilterType = iArr;
        }
        return iArr;
    }

    public static GPUImageFilter createFilterForType(Context context, GPUFilterType type) {
        PointF centerPoint = new PointF();
        centerPoint.x = 0.5f;
        centerPoint.y = 0.5f;
        float[] fArr = new float[3];
        fArr = new float[]{0.0f, 0.0f, 0.0f};
        float[] fArr2 = new float[3];
        fArr2 = new float[]{0.0f, 0.0f, 0.0f};
        float[] fArr3 = new float[3];
        fArr3 = new float[]{0.0f, 0.0f, 0.0f};
        List<GPUImageFilter> filters = new LinkedList();
        List<GPUImageFilter> list;
        float[] fArr4;
        float[] fArr5;
        Log.d("createFilterForType ", " " + $SWITCH_TABLE$com$baiwang$lib$filter$gpu$GPUFilterType()[type.ordinal()]);
        switch ($SWITCH_TABLE$com$baiwang$lib$filter$gpu$GPUFilterType()[type.ordinal()]) {
            case 1:
                return new GPUImageNoFilter();
            case 2:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/test/Fotor_yj.jpg", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 4:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a1.acv");
            case 5:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a2.acv");
            case 6:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a3.acv");
            case 7:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a4.acv");
            case 8:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a5.acv");
            case 9:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a6.acv");
            case 10:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a7.acv");
            case 11:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a8.acv");
            case 12:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a9.acv");
            case 13:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a10.acv");
            case 14:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a11.acv");
            case 15:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a12.acv");
            case 16:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a13.acv");
            case 17:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a14.acv");
            case 18:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a15.acv");
            case 19:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a16.acv");
            case 20:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a17.acv");
            case 21:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a18.acv");
            case 22 /*22*/:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a19.acv");
            case 23 /*23*/:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a20.acv");
            case 24 /*24*/:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a21.acv");
            case 25 /*25*/:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a22.acv");
            case 26:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a23.acv");
            case 27:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a24.acv");
            case 28:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a25.acv");
            case 29:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a26.acv");
            case 30:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a27.acv");
            case 31 /*31*/:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a28.acv");
            case 32:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a29.acv");
            case 33 /*33*/:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a30.acv");
            case 34 /*34*/:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a31.acv");
            case 35 /*35*/:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a32.acv");
            case 36 /*36*/:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a33.acv");
            case 37 /*37*/:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a34.acv");
            case 38 /*38*/:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a35.acv");
            case 39 /*39*/:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a36.acv");
            case 40/*40*/:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a37.acv");
            case 41 /*41*/:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a38.acv");
            case 42 /*42*/:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a39.acv");
            case 43 /*43*/:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a40.acv");
            case 44 /*44*/:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a41.acv");
            case 45 /*45*/:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a42.acv");
            case 46 /*46*/:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a43.acv");
            case 47/*47*/:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a44.acv");
            case 48 /*48*/:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a45.acv");
            case 49:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a46.acv");
            case 50:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a47.acv");
            case 51:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a48.acv");
            case 52:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/a49.acv");
            case 53:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/a50.acv"));
                break;
            case 54:
                break;
            case 55:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d2.dat");
            case 56:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d3.dat");
            case 57:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d4.dat");
            case 58:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d5.dat");
            case 59:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d6.dat");
            case 60:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d7.dat");
            case 61:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d8.dat");
            case 62:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d9.dat");
            case 63:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d10.dat");
            case 64:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d11.dat");
            case 65:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d12.dat");
            case 66:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d13.dat");
            case 67:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d14.dat");
            case 68:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d15.dat");
            case 69:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d16.dat");
            case 70:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d17.dat");
            case 71:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d18.dat");
            case 72:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d19.dat");
            case 73:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d20.dat");
            case 74:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d21.dat");
            case 75 /*75*/:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d22.dat");
            case 76:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d23.dat");
            case 77:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d24.dat");
            case 78:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d25.dat");
            case 79:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d26.dat");
            case 80 /*80*/:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d27.dat");
            case 81:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d28.dat");
            case 82:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d29.dat");
            case 83:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d30.dat");
            case 84:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d31.dat");
            case 85:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d32.dat");
            case 86:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d33.dat");
            case 87:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d34.dat");
            case 88:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d35.dat");
            case 89:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d36.dat");
            case 90 /*90*/:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d37.dat");
            case 91:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d38.dat");
            case 92:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d39.dat");
            case 93:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d40.dat");
            case 94:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d41.dat");
            case 95:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d42.dat");
            case 96:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d43.dat");
            case 97:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d44.dat");
            case 98:
                centerPoint.x = 0.55f;
                centerPoint.y = 0.45f;
                filters.add(new GPUImageContrastFilter(1.1f));
                list = filters;
                list.add(GPUImageFilterCreator.createFilterForVignetteTwoInputFilter(context, "filter/Classic/Gloss/map.png", centerPoint, 0.2f, 0.75f, GPUImageVignetteToneCurveMapFilter.class));
                filters.add(new GPUImageVignetteBrightnessFilter(centerPoint, 0.0f, -0.2f, 0.3f, 0.75f));
                list = filters;
                list.add(GPUImageFilterCreator.createFilterForVignetteTwoInputFilter(context, "filter/Classic/Gloss/overlay_map.png", centerPoint, 0.3f, 0.75f, GPUImageVignetteMapSelfBlendFilter.class));
                return new GPUImageFilterGroup(filters);
            case 99:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Classic/Versa/colorGradient.png", GPUImageToneCurveMapFilter.class));
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Classic/Versa/colorOverlay.png", GPUImageMapSelfBlendFilter.class));
                centerPoint.x = 0.3f;
                centerPoint.y = 0.48f;
                filters.add(new GPUImageVignetteBrightnessFilter(centerPoint, 0.08f, 0.0f, 0.1f, 0.25f));
                centerPoint = new PointF();
                centerPoint.x = 0.6f;
                centerPoint.y = 0.55f;
                filters.add(new GPUImageVignetteBrightnessFilter(centerPoint, 0.05f, 0.0f, 0.1f, 0.2f));
                centerPoint = new PointF();
                centerPoint.x = 0.53f;
                centerPoint.y = 0.99f;
                filters.add(new GPUImageVignetteBrightnessFilter(centerPoint, 0.1f, 0.0f, 0.1f, 0.25f));
                return new GPUImageFilterGroup(filters);
            case 100:
                centerPoint.x = 0.55f;
                centerPoint.y = 0.45f;
                filters.add(new GPUImageContrastFilter(1.1f));
                list = filters;
                list.add(GPUImageFilterCreator.createFilterForVignetteTwoInputFilter(context, "filter/Classic/Listless/map.png", centerPoint, 0.2f, 0.75f, GPUImageVignetteToneCurveMapFilter.class));
                filters.add(new GPUImageVignetteBrightnessFilter(centerPoint, 0.05f, -0.27f, 0.3f, 0.75f));
                return new GPUImageFilterGroup(filters);
            case 101 /*101*/:
                centerPoint.x = 0.6f;
                centerPoint.y = 0.65f;
                filters.add(new GPUImageContrastFilter(1.1f));
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Classic/Icy/map.png", GPUImageToneCurveMapFilter.class));
                filters.add(new GPUImageVignetteBrightnessFilter(centerPoint, 0.09f, -0.2f, 0.3f, 0.8f));
                return new GPUImageFilterGroup(filters);
            case 102:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Classic/Fade/map.png", GPUImageToneCurveMapFilter.class));
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Classic/Fade/gradient_map.png", GPUImageMapSelfBlendFilter.class));
                filters.add(new GPUImageBrightnessFilter(0.02f));
                return new GPUImageFilterGroup(filters);
            case 103 /*103*/:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Classic/Mild/soft_light.png", GPUImageMapSelfBlendFilter.class));
                filters.add(new GPUImageVignetteBrightnessFilter(centerPoint, 0.1f, -0.25f, 0.3f, 0.75f));
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Classic/Mild/map.png", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 104/*104*/:
                list = filters;
                list.add(GPUImageFilterCreator.createFilterForVignetteTwoInputFilter(context, "filter/Classic/Vigour/map.png", centerPoint, 0.3f, 0.75f, GPUImageVignetteToneCurveMapFilter.class));
                filters.add(new GPUImageVignetteBrightnessFilter(centerPoint, 0.16f, -0.2f, 0.2f, 0.75f));
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Classic/Vigour/color_shift_map.png", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 105 /*105*/:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Classic/Drama/luma_map.png", GPUImageToneCurveMapFilter.class));
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Classic/Drama/blowout_map.png", GPUImageToneCurveMapFilter.class));
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Classic/Drama/map.png", GPUImageToneCurveMapFilter.class));
                filters.add(new GPUImageSaturationFilter(0.6f));
                return new GPUImageFilterGroup(filters);
            case 106:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Classic/Pale/map.png", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 107:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Classic/Passion/map.png", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 108:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Classic/Pizazz/map.png", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 109:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Classic/Alone/map.png", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 110:
                filters.add(new GPUImageSaturationFilter(0.7f));
                filters.add(new GPUImageContrastFilter(1.1f));
                filters.add(new GPUImageVignetteBrightnessFilter(centerPoint, 0.18f, -0.2f, 0.2f, 0.75f));
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Era/1970/map.png", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 111:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/Era/1974/1974.acv"));
                return new GPUImageFilterGroup(filters);
            case 112:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Era/1977/map.png", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 113:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Era/1970/map.png", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 114:
                centerPoint.x = 0.55f;
                centerPoint.y = 0.45f;
                filters.add(new GPUImageContrastFilter(1.1f));
                filters.add(new GPUImageVignetteBrightnessFilter(centerPoint, 0.18f, -0.2f, 0.2f, 0.75f));
                filters.add(new GPUImageSaturationFilter(0.0f));
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/BW/Selium/willowMap.png", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 115:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/BW/Kopan/map.png", GPUImageToneCurveMapFilter.class));
                filters.add(new GPUImageSaturationFilter(0.0f));
                return new GPUImageFilterGroup(filters);
            case 116:
                filters.add(new GPUImageSaturationFilter(0.0f));
                filters.add(new GPUImageExposureFilter(-0.5f));
                centerPoint.x = 0.35f;
                centerPoint.y = 0.35f;
                filters.add(new GPUImageVignetteExposureFilter(centerPoint, 0.2f, -0.5f, 0.4f, 0.85f));
                filters.add(new GPUImageVignetteContrastFilter(centerPoint, 0.7f, 1.2f, 0.2f, 0.75f));
                filters.add(new GPUImageVignetteSharpenFilter(centerPoint, 0.18f, -0.45f, 0.2f, 0.75f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/BW/BWRetro/BWRetro.acv"));
                return new GPUImageFilterGroup(filters);
            case 117:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Vintage/Weson/curves_map.png", GPUImageToneCurveMapFilter.class));
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Vintage/Weson/overlay_map.png", GPUImageToneCurveMapFilter.class));
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Vintage/Weson/blowout_map.png", GPUImageToneCurveMapFilter.class));
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Vintage/Weson/map_2d.png", GPUImageMapSelfBlendFilter.class));
                filters.add(new GPUImageVignetteBrightnessFilter(centerPoint, 0.12f, -0.3f, 0.2f, 0.75f));
                return new GPUImageFilterGroup(filters);
            case 118:
                filters.add(new GPUImageVignetteBrightnessFilter(centerPoint, 0.12f, -0.2f, 0.2f, 0.75f));
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Vintage/Lethe/map.png", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 119:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Film/Agx100/map.png", GPUImageToneCurveMapFilter.class));
                filters.add(new GPUImageVignetteBrightnessFilter(centerPoint, 0.06f, -0.2f, 0.2f, 0.75f));
                filters.add(new GPUImageVignetteSharpenFilter(centerPoint, 0.3f, 0.0f, 0.3f, 0.75f));
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Film/Agx100/vignette_map.png", GPUImageMapSelfBlendFilter.class));
                return new GPUImageFilterGroup(filters);
            case 120:
                filters.add(new GPUImageVignetteBrightnessFilter(centerPoint, 0.1f, -0.25f, 0.3f, 0.75f));
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Film/Kuc100/map.png", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 121:
                centerPoint.x = 0.35f;
                centerPoint.y = 0.35f;
                filters.add(new GPUImageContrastFilter(1.1f));
                filters.add(new GPUImageVignetteBrightnessFilter(centerPoint, 0.18f, -0.2f, 0.2f, 0.75f));
                filters.add(new GPUImageSaturationFilter(0.0f));
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/BW/ashby/tonemap.png", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 122:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/BW/brooklyn/curves.png", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 123:
                filters.add(new GPUImageSaturationFilter(0.0f));
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/BW/charmes/map.png", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 124 /*124*/:
                centerPoint.x = 0.3f;
                centerPoint.y = 0.18f;
                filters.add(new GPUImageVignetteBrightnessFilter(centerPoint, 0.08f, 0.0f, 0.1f, 0.25f));
                centerPoint = new PointF();
                centerPoint.x = 0.45f;
                centerPoint.y = 0.55f;
                filters.add(new GPUImageVignetteBrightnessFilter(centerPoint, 0.05f, 0.0f, 0.1f, 0.2f));
                centerPoint = new PointF();
                centerPoint.x = 0.83f;
                centerPoint.y = 0.99f;
                filters.add(new GPUImageVignetteBrightnessFilter(centerPoint, 0.1f, 0.0f, 0.1f, 0.25f));
                filters.add(new GPUImageSaturationFilter(0.0f));
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/BW/clarendon/Glacial1.png", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 125:
                filters.add(new GPUImageSaturationFilter(0.0f));
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/BW/dogpatch/curves1.png", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 126/*126*/:
                filters.add(new GPUImageSaturationFilter(0.0f));
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/BW/gingham/curves1.png", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 127 /*127*/:
                filters.add(new GPUImageSaturationFilter(0.0f));
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/BW/ginza/curves1.png", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 128:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/BW/helena/epic_1.png", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 129:
                filters.add(new GPUImageSaturationFilter(0.0f));
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/BW/maven/Lansdowne1.png", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 130 /*130*/:
                filters.add(new GPUImageSaturationFilter(0.0f));
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/BW/moon/curves1.png", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 131:
                filters.add(new GPUImageSaturationFilter(0.0f));
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/BW/skyline/curves.png", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 132:
                filters.add(new GPUImageSaturationFilter(0.0f));
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/BW/stinson/curves.png", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 133:
                filters.add(new GPUImageSaturationFilter(0.0f));
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/BW/vesper/map.png", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 134:
                return GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Dat/abao.png", GPUImageLookupFilter.class);
            case 135:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Dat/rixi.png", GPUImageLookupFilter.class));
                filters.add(new GPUImageBrightnessFilter(-0.1f));
                return new GPUImageFilterGroup(filters);
            case 136:
                return new GPUImageABaoFilter();
            case 137:
                filters.add(GPUImageFilterCreator.createDATCurveFilter(context, "filter/Dat/zi_ran.dat"));
                filters.add(new GPUImageSoftLightFilter(0.002f, -0.2f, 0.8f));
                return new GPUImageFilterGroup(filters);
            case 138:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/Dat/lake_path.dat");
            case 139:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/Dat/lan_diao.dat");
            case 140:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/Dat/xiao_zhen.dat");
            case 141:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/Dat/lomo_path.dat");
            case 142:
                GPUImageVignetteToneCurveFilter gPUImageVignetteToneCurveFilter = new GPUImageVignetteToneCurveFilter(centerPoint, 0.2f, 0.75f);
                try {
                    gPUImageVignetteToneCurveFilter.setFromDatCurveFileInputStream(context.getAssets().open("filter/Dat/lomo_path.dat"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                gPUImageVignetteToneCurveFilter.setFileType("dat");
                gPUImageVignetteToneCurveFilter.setInvert(Boolean.valueOf(true));
                filters.add(gPUImageVignetteToneCurveFilter);
                filters.add(new GPUImageVignetteBrightnessFilter(centerPoint, 0.0f, -0.2f, 0.3f, 0.75f));
                gPUImageVignetteToneCurveFilter = new GPUImageVignetteToneCurveFilter(centerPoint, 0.3f, 0.75f);
                try {
                    gPUImageVignetteToneCurveFilter.setFromDatCurveFileInputStream(context.getAssets().open("filter/Dat/lomo.dat"));
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                gPUImageVignetteToneCurveFilter.setFileType("dat");
                filters.add(gPUImageVignetteToneCurveFilter);
                return new GPUImageFilterGroup(filters);
            case 143:
                filters.add(GPUImageFilterCreator.createDATCurveFilter(context, "filter/Dat/hei_bai.dat"));
                filters.add(new GPUImageSaturationFilter(0.0f));
                return new GPUImageFilterGroup(filters);
            case 144:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/Dat/wei_mei.dat");
            case 145:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/Dat/shen_lan.dat");
            case 146:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/Dat/qing_xin.dat");
            case 147:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/Dat/fen_nen.dat");
            case 148:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/Dat/qiu_se.dat");
            case 149:
                filters.add(new GPUImageContrastFilter(0.85f));
                filters.add(new GPUImageSaturationFilter(0.75f));
                filters.add(new GPUImageVignetteBrightnessFilter(centerPoint, -0.1f, -0.3f, 0.3f, 0.75f));
                filters.add(GPUImageFilterCreator.createDATCurveFilter(context, "filter/Dat/hui_yi.dat"));
                filters.add(GPUImageFilterCreator.createDATCurveFilter(context, "filter/Dat/hui_yi.dat"));
                return new GPUImageFilterGroup(filters);
            case 150 /*150*/:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/Dat/banbo.dat");
            case 151:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/Dat/better_skin.dat");
            case 152:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/Dat/deep_white.dat");
            case 153:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/Dat/HDR.dat");
            case 154:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/Dat/jingdianHDR.dat");
            case 155:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/Dat/natural_white.dat");
            case 156:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/Dat/skin_smooth.dat");
            case 157:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/Dat/sunny.dat");
            case 158:
                return GPUImageFilterCreator.createDATCurveFilter(context, "filter/Dat/sweety.dat");
            case 159:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/curves/acv/7AM.acv"));
                return new GPUImageFilterGroup(filters);
            case 160:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/curves/acv/1974.acv"));
                return new GPUImageFilterGroup(filters);
            case 161:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/curves/acv/Absinth.acv"));
                return new GPUImageFilterGroup(filters);
            case 162:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/curves/acv/Buenos Aires.acv"));
                return new GPUImageFilterGroup(filters);
            case 163:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/curves/acv/Denim.acv"));
                return new GPUImageFilterGroup(filters);
            case 164:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/curves/acv/Royal Blue.acv"));
                return new GPUImageFilterGroup(filters);
            case 165:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/curves/acv/Smoky.acv"));
                return new GPUImageFilterGroup(filters);
            case 166:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/curves/acv/Toaster.acv"));
                return new GPUImageFilterGroup(filters);
            case 167:
                filters.add(GPUImageFilterCreator.createBlendFilter(context, GPUImageScreenBlendFilter.class, "filter/bokeh/bokeh1.jpg"));
                return new GPUImageFilterGroup(filters);
            case 168:
                filters.add(GPUImageFilterCreator.createBlendFilter(context, GPUImageScreenBlendFilter.class, "filter/bokeh/bokeh2.jpg"));
                return new GPUImageFilterGroup(filters);
            case 169:
                filters.add(GPUImageFilterCreator.createBlendFilter(context, GPUImageScreenBlendFilter.class, "filter/bokeh/bokeh3.jpg"));
                return new GPUImageFilterGroup(filters);
            case 170:
                filters.add(GPUImageFilterCreator.createBlendFilter(context, GPUImageScreenBlendFilter.class, "filter/bokeh/bokeh4.jpg"));
                return new GPUImageFilterGroup(filters);
            case 171:
                filters.add(GPUImageFilterCreator.createBlendFilter(context, GPUImageScreenBlendFilter.class, "filter/bokeh/bokeh5.jpg"));
                return new GPUImageFilterGroup(filters);
            case 172:
                filters.add(GPUImageFilterCreator.createBlendFilter(context, GPUImageScreenBlendFilter.class, "filter/bokeh/bokeh6.jpg"));
                return new GPUImageFilterGroup(filters);
            case 173:
                filters.add(GPUImageFilterCreator.createBlendFilter(context, GPUImageScreenBlendFilter.class, "filter/bokeh/bokeh7.jpg"));
                return new GPUImageFilterGroup(filters);
            case 174:
                filters.add(GPUImageFilterCreator.createBlendFilter(context, GPUImageMultiplyBlendFilter.class, "filter/bokeh/bokeh8.jpg"));
                return new GPUImageFilterGroup(filters);
            case 175:
                filters.add(new GPUImageSaturationFilter(0.0f));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, GPUImageMultiplyBlendFilter.class, "filter/art/paper.jpg"));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, GPUImageScreenBlendFilter.class, "filter/art/pencil.jpg"));
                return new GPUImageFilterGroup(filters);
            case 176:
                filters.add(new GPUImageSaturationFilter(0.0f));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, GPUImageMultiplyBlendFilter.class, "filter/art/pencil.jpg"));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, GPUImageScreenBlendFilter.class, "filter/art/oldpaper.jpg"));
                return new GPUImageFilterGroup(filters);
            case 177:
                filters.add(new GPUImageSaturationFilter(0.0f));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, GPUImageScreenBlendFilter.class, "filter/art/pencil.jpg"));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, GPUImageScreenBlendFilter.class, "filter/art/colorpencil.jpg"));
                return new GPUImageFilterGroup(filters);
            case 178:
                filters.add(GPUImageFilterCreator.createBlendFilter(context, GPUImageScreenBlendFilter.class, "filter/art/blot.jpg"));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, GPUImageMultiplyBlendFilter.class, "filter/art/kraft.jpg"));
                return new GPUImageFilterGroup(filters);
            case 179:
                filters.add(GPUImageFilterCreator.createBlendFilter(context, GPUImageScreenBlendFilter.class, "filter/art/spot.jpg"));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, GPUImageMultiplyBlendFilter.class, "filter/art/oldpaper2.jpg"));
                return new GPUImageFilterGroup(filters);
            case 180:
                filters.add(new GPUImageSaturationFilter(0.0f));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, GPUImageScreenBlendFilter.class, "filter/art/moviespot.jpg"));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, GPUImageMultiplyBlendFilter.class, "filter/art/oldmovie.jpg"));
                return new GPUImageFilterGroup(filters);
            case 181:
                filters.add(GPUImageFilterCreator.createBlendFilter(context, GPUImageMultiplyBlendFilter.class, "filter/lens/1974.png"));
                return new GPUImageFilterGroup(filters);
            case 182:
                filters.add(GPUImageFilterCreator.createBlendFilter(context, GPUImageMultiplyBlendFilter.class, "filter/lens/absinth02.png"));
                return new GPUImageFilterGroup(filters);
            case 183:
                filters.add(GPUImageFilterCreator.createBlendFilter(context, GPUImageMultiplyBlendFilter.class, "filter/lens/buenos_aires.png"));
                return new GPUImageFilterGroup(filters);
            case 184:
                filters.add(GPUImageFilterCreator.createBlendFilter(context, GPUImageMultiplyBlendFilter.class, "filter/lens/denim.png"));
                return new GPUImageFilterGroup(filters);
            case 185:
                filters.add(GPUImageFilterCreator.createBlendFilter(context, GPUImageMultiplyBlendFilter.class, "filter/lens/denim02.png"));
                return new GPUImageFilterGroup(filters);
            case 186:
                filters.add(GPUImageFilterCreator.createBlendFilter(context, GPUImageMultiplyBlendFilter.class, "filter/lens/royalblue.png"));
                return new GPUImageFilterGroup(filters);
            case 187:
                filters.add(GPUImageFilterCreator.createBlendFilter(context, GPUImageMultiplyBlendFilter.class, "filter/lens/smoky.png"));
                return new GPUImageFilterGroup(filters);
            case 188:
                filters.add(GPUImageFilterCreator.createBlendFilter(context, GPUImageMultiplyBlendFilter.class, "filter/lens/toaster.png"));
                return new GPUImageFilterGroup(filters);
            case 189:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Vintage/AS/A4.jpg", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 190:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Vintage/AS/A5.jpg", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 191:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Vintage/AS/A6.jpg", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 192:
                filters.add(new GPUImageSaturationFilter(0.0f));
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Vintage/AS/B1.jpg", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 193:
                filters.add(new GPUImageSaturationFilter(0.0f));
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Vintage/AS/B5.jpg", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 194:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Vintage/AS/C1.jpg", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 195:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Vintage/AS/F2.jpg", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 196:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Vintage/AS/G3.jpg", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 197:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Vintage/AS/HB1.jpg", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 198:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Vintage/AS/HB2.jpg", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 199:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Vintage/AS/M3.jpg", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 200:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Vintage/AS/M5.jpg", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 201:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Vintage/AS/P5.jpg", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 202:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Vintage/AS/SE1.jpg", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 203:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Vintage/AS/SE2.jpg", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 204:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Vintage/AS/SE3.jpg", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 205:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Vintage/AS/T1.jpg", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 206:
                filters.add(new GPUImageSaturationFilter(0.0f));
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Vintage/AS/X1.jpg", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 207:
                return new GPUImageContrastFilter();
            case 208:
                return new GPUImageSharpenFilter();
            case 209:
                return new GPUImageGammaFilter();
            case 210:
                return new GPUImageBrightnessFilter();
            case 211:
                return new GPUImageSaturationFilter();
            case 212:
                return new GPUImageExposureFilter();
            case 213:
                return new GPUImageWhiteBalanceFilter();
            case 214:
                return new GPUImageOpacityFilter(TextTrackStyle.DEFAULT_FONT_SCALE);
            case 216:
                return new GPUImageSepiaFilter();
            case 217:
                return new GPUImageSobelEdgeDetection();
            case 218:
                GPUImage3x3ConvolutionFilter convolution = new GPUImage3x3ConvolutionFilter();
                convolution.setConvolutionKernel(new float[]{-1.0f, 0.0f, TextTrackStyle.DEFAULT_FONT_SCALE, -2.0f, 0.0f, 2.0f, -1.0f, 0.0f, TextTrackStyle.DEFAULT_FONT_SCALE});
                return convolution;
            case 219:
                return new GPUImageEmbossFilter();
            case 220:
                return new GPUImagePosterizeFilter();
            case 221:
                return new GPUImageColorInvertFilter();
            case 222:
                return new GPUImagePixelationFilter();
            case 223:
                return new GPUImageHighlightShadowFilter(0.0f, TextTrackStyle.DEFAULT_FONT_SCALE);
            case 224:
                fArr4 = new float[4];
                return new GPUImageMonochromeFilter(TextTrackStyle.DEFAULT_FONT_SCALE, new float[]{0.6f, 0.45f, 0.3f, TextTrackStyle.DEFAULT_FONT_SCALE});
            case 225:
                return new GPUImageRGBFilter(TextTrackStyle.DEFAULT_FONT_SCALE, TextTrackStyle.DEFAULT_FONT_SCALE, TextTrackStyle.DEFAULT_FONT_SCALE);
            case 226:
                return new GPUImageHueFilter();
            case 227:
                return GPUImageFilterCreator.createBlendFilter(context, GPUImageColorBurnBlendFilter.class);
            case 228:
                return GPUImageFilterCreator.createBlendFilter(context, GPUImageColorDodgeBlendFilter.class);
            case 229:
                return GPUImageFilterCreator.createBlendFilter(context, GPUImageDarkenBlendFilter.class);
            case 230:
                return GPUImageFilterCreator.createBlendFilter(context, GPUImageDifferenceBlendFilter.class);
            case 231:
                return GPUImageFilterCreator.createBlendFilter(context, GPUImageDissolveBlendFilter.class);
            case 232:
                return GPUImageFilterCreator.createBlendFilter(context, GPUImageExclusionBlendFilter.class);
            case 233:
                return GPUImageFilterCreator.createBlendFilter(context, GPUImageSourceOverBlendFilter.class);
            case 234:
                return GPUImageFilterCreator.createBlendFilter(context, GPUImageHardLightBlendFilter.class);
            case 235:
                return GPUImageFilterCreator.createBlendFilter(context, GPUImageLightenBlendFilter.class);
            case 236:
                return GPUImageFilterCreator.createBlendFilter(context, GPUImageAddBlendFilter.class);
            case 237:
                return GPUImageFilterCreator.createBlendFilter(context, GPUImageDivideBlendFilter.class);
            case 238:
                return GPUImageFilterCreator.createBlendFilter(context, GPUImageMultiplyBlendFilter.class);
            case 239:
                return GPUImageFilterCreator.createBlendFilter(context, GPUImageOverlayBlendFilter.class);
            case 240:
                return GPUImageFilterCreator.createBlendFilter(context, GPUImageScreenBlendFilter.class);
            case 241:
                return GPUImageFilterCreator.createBlendFilter(context, GPUImageColorBlendFilter.class);
            case 242:
                return GPUImageFilterCreator.createBlendFilter(context, GPUImageHueBlendFilter.class);
            case 243:
                return GPUImageFilterCreator.createBlendFilter(context, GPUImageSaturationBlendFilter.class);
            case 244:
                return GPUImageFilterCreator.createBlendFilter(context, GPUImageLuminosityBlendFilter.class);
            case 245:
                return GPUImageFilterCreator.createBlendFilter(context, GPUImageLinearBurnBlendFilter.class);
            case 246:
                return GPUImageFilterCreator.createBlendFilter(context, GPUImageSoftLightBlendFilter.class);
            case 247:
                return GPUImageFilterCreator.createBlendFilter(context, GPUImageSubtractBlendFilter.class);
            case 248:
                return GPUImageFilterCreator.createBlendFilter(context, GPUImageChromaKeyBlendFilter.class);
            case 249:
                return GPUImageFilterCreator.createBlendFilter(context, GPUImageNormalBlendFilter.class);
            case 250:
                fArr5 = new float[3];
                return new GPUImageVignetteColorFilter(new float[]{0.0f, 0.0f, 0.0f}, centerPoint, 0.3f, 0.75f);
            case 251:
                return new GPUImageVignetteContrastFilter(centerPoint, 0.9f, 1.5f, 0.2f, 0.75f);
            case 252:
                return new GPUImageVignetteBrightnessFilter(centerPoint, 0.12f, -0.3f, 0.2f, 0.75f);
            case 253:
                return new GPUImageVignetteColorInvertFilter(centerPoint, 0.0f, TextTrackStyle.DEFAULT_FONT_SCALE, 0.2f, 0.75f);
            case 254:
                return new GPUImageVignetteExposureFilter(centerPoint, 0.2f, -0.5f, 0.2f, 0.75f);
            case 255:
                return new GPUImageVignetteGammaFilter(centerPoint, 0.9f, 1.5f, 0.2f, 0.75f);
            case 256:
                return new GPUImageVignetteGaussianBlurFilter(centerPoint, 0.0f, 0.0026041667f, 0.2f, 0.75f);
            case 257:
                return new GPUImageVignetteGrayscaleFilter(centerPoint, 1.2f, 0.0f, 0.2f, 0.75f);
            case 258:
                return new GPUImageVignetteHueFilter(centerPoint, 0.0f, HUE_YELLOW, 0.3f, 0.75f);
            case 259:
                return GPUImageFilterCreator.createFilterForVignetteTwoInputFilter(context, "", centerPoint, 0.2f, 0.75f, GPUImageVignetteMapSelfBlendFilter.class);
            case 260:
                return new GPUImageVignetteSharpenFilter(centerPoint, 0.3f, -0.7f, 0.2f, 0.75f);
            case 261:
                return GPUImageFilterCreator.createFilterForVignetteTwoInputFilter(context, "", centerPoint, 0.2f, 0.75f, GPUImageVignetteToneCurveMapFilter.class);
            case 262:
                return new GPUImageSoftLightFilter();
            case 263:
                return new GPUImageGaussianBlurSimpleFilter();
            case 265:
                return new GPUImageMapSelfBlendFilter();
            case 266:
                return new GPUImageMapBlendFilter();
            case 267:
                return new GPUImageToneCurveFilter();
            case 268:
                return GPUImageFilterCreator.createBlendFilter(context, GPUImageToneCurveMapFilter.class);
            case 269:
                List<GPUImageFilter> filters2 = new LinkedList();
                filters2.add(new GPUImageGaussianBlurSimpleFilter(0.001953125f));
                filters2.add(new GPUImageGaussianBlurSimpleFilter(0.001953125f));
                filters2.add(new GPUImageGaussianBlurSimpleFilter(0.001953125f));
                filters2.add(new GPUImageGaussianBlurSimpleFilter(0.001953125f));
                filters2.add(new GPUImageGaussianBlurSimpleFilter(0.001953125f));
                return new GPUImageFilterGroup(filters2);
            case 270:
                filters.add(new GPUImageBrightnessFilter(0.05f));
                filters.add(new GPUImageSaturationFilter(1.1582757f));
                filters.add(new GPUImageContrastFilter(1.4586205f));
                filters.add(new GPUImageSharpenFilter(0.4229886f));
                filters.add(new GPUImageWhiteBalanceFilter(5270.1147f));
                filters.add(new GPUImageHighlightShadowFilter(0.37659633f, 0.6542146f));
                filters.add(new GPUImageVignetteFilter(1.0161124f));
                return new GPUImageFilterGroup(filters);
            case 271:
                filters.add(new GPUImageBrightnessFilter(0.059866f));
                filters.add(new GPUImageSaturationFilter(1.1310344f));
                filters.add(new GPUImageContrastFilter(1.1677182f));
                filters.add(new GPUImageSharpenFilter(0.5f));
                filters.add(new GPUImageWhiteBalanceFilter(6504.31f));
                filters.add(new GPUImageHighlightShadowFilter(0.2095685f, 0.8061302f));
                return new GPUImageFilterGroup(filters);
            case 272:
                filters.add(new GPUImageBrightnessFilter(0.1591954f));
                filters.add(new GPUImageSaturationFilter(1.379885f));
                filters.add(new GPUImageContrastFilter(1.265517f));
                filters.add(new GPUImageSharpenFilter(0.5287356f));
                filters.add(new GPUImageWhiteBalanceFilter(5133.6206f));
                filters.add(new GPUImageHighlightShadowFilter(0.26747108f, 0.643678f));
                return new GPUImageFilterGroup(filters);
            case 273:
                filters.add(new GPUImageBrightnessFilter(0.0655174f));
                filters.add(new GPUImageSaturationFilter(1.7224138f));
                filters.add(new GPUImageContrastFilter(1.2310346f));
                filters.add(new GPUImageSharpenFilter(0.2804598f));
                filters.add(new GPUImageWhiteBalanceFilter(6778.736f));
                return new GPUImageFilterGroup(filters);
            case 274:
                filters.add(new GPUImageBrightnessFilter(0.020115f));
                filters.add(new GPUImageSaturationFilter(1.2752872f));
                filters.add(new GPUImageContrastFilter(1.1448278f));
                filters.add(new GPUImageSharpenFilter(0.5632186f));
                filters.add(new GPUImageWhiteBalanceFilter(5321.839f));
                return new GPUImageFilterGroup(filters);
            case 275:
                filters.add(new GPUImageBrightnessFilter(0.066f));
                filters.add(new GPUImageSaturationFilter(1.411f));
                filters.add(new GPUImageContrastFilter(1.231f));
                filters.add(new GPUImageSharpenFilter(0.28f));
                filters.add(new GPUImageWhiteBalanceFilter(4500.0f));
                return new GPUImageFilterGroup(filters);
            case 276:
                filters.add(new GPUImageBrightnessFilter(0.06752875f));
                filters.add(new GPUImageSaturationFilter(1.547414f));
                filters.add(new GPUImageContrastFilter(1.2873555f));
                filters.add(new GPUImageSharpenFilter(0.48850575f));
                filters.add(new GPUImageWhiteBalanceFilter(5639.368f));
                filters.add(new GPUImageVignetteFilter(0.9181035f));
                filters.add(new GPUImageRGBFilter(1.0948275f, 1.1954023f, 0.98563224f));
                return new GPUImageFilterGroup(filters);
            case 277:
                filters.add(new GPUImageBrightnessFilter(0.10201175f));
                filters.add(new GPUImageSaturationFilter(1.2844827f));
                filters.add(new GPUImageContrastFilter(1.2356323f));
                filters.add(new GPUImageSharpenFilter(0.5114945f));
                filters.add(new GPUImageWhiteBalanceFilter(6734.9136f));
                filters.add(new GPUImageVignetteFilter(1.3265085f));
                filters.add(new GPUImageRGBFilter(1.2715517f, 1.0488505f, 1.0272988f));
                return new GPUImageFilterGroup(filters);
            case 278:
                filters.add(new GPUImageBrightnessFilter(0.0890805f));
                filters.add(new GPUImageSaturationFilter(1.1163793f));
                filters.add(new GPUImageContrastFilter(1.2270114f));
                filters.add(new GPUImageSharpenFilter(0.534483f));
                filters.add(new GPUImageWhiteBalanceFilter(7467.6724f));
                filters.add(new GPUImageVignetteFilter(1.3275863f));
                filters.add(new GPUImageRGBFilter(1.1666665f, 1.0790232f, 0.7557467f));
                return new GPUImageFilterGroup(filters);
            case 279:
                filters.add(new GPUImageBrightnessFilter(0.0416665f));
                filters.add(new GPUImageSaturationFilter(1.086207f));
                filters.add(new GPUImageContrastFilter(0.988506f));
                filters.add(new GPUImageSharpenFilter(0.37931025f));
                filters.add(new GPUImageWhiteBalanceFilter(4400.1436f));
                filters.add(new GPUImageVignetteFilter(1.3394397f));
                filters.add(new GPUImageRGBFilter(1.0114943f, 1.0100574f, 1.0847702f));
                return new GPUImageFilterGroup(filters);
            case 280:
                filters.add(new GPUImageBrightnessFilter(0.068966f));
                filters.add(new GPUImageSaturationFilter(0.810345f));
                filters.add(new GPUImageContrastFilter(1.16092f));
                filters.add(new GPUImageSharpenFilter(0.344828f));
                filters.add(new GPUImageWhiteBalanceFilter(6522.9883f));
                filters.add(new GPUImageVignetteFilter(1.112069f));
                filters.add(new GPUImageRGBFilter(1.316092f, 1.086207f, 0.867816f));
                return new GPUImageFilterGroup(filters);
            case 281:
                filters.add(new GPUImageBrightnessFilter(0.074713f));
                filters.add(new GPUImageSaturationFilter(0.856322f));
                filters.add(new GPUImageContrastFilter(1.218391f));
                filters.add(new GPUImageSharpenFilter(0.45977f));
                filters.add(new GPUImageWhiteBalanceFilter(5316.092f));
                filters.add(new GPUImageVignetteFilter(0.866379f));
                filters.add(new GPUImageRGBFilter(1.517241f, 0.971264f, 0.977012f));
                return new GPUImageFilterGroup(filters);
            case 282:
                filters.add(new GPUImageBrightnessFilter(0.074713f));
                filters.add(new GPUImageSaturationFilter(0.856322f));
                filters.add(new GPUImageContrastFilter(1.218391f));
                filters.add(new GPUImageSharpenFilter(0.45977f));
                filters.add(new GPUImageWhiteBalanceFilter(7025.8623f));
                filters.add(new GPUImageVignetteFilter(0.866379f));
                filters.add(new GPUImageRGBFilter(1.431034f, 1.143678f, 0.735632f));
                return new GPUImageFilterGroup(filters);
            case 283:
                filters.add(new GPUImageBrightnessFilter(0.074713f));
                filters.add(new GPUImageSaturationFilter(0.856322f));
                filters.add(new GPUImageContrastFilter(1.218391f));
                filters.add(new GPUImageSharpenFilter(0.45977f));
                filters.add(new GPUImageWhiteBalanceFilter(4511.494f));
                filters.add(new GPUImageVignetteFilter(0.866379f));
                filters.add(new GPUImageRGBFilter(1.183908f, 1.114943f, 1.649425f));
                return new GPUImageFilterGroup(filters);
            case 284:
                filters.add(new GPUImageBrightnessFilter(-0.034483f));
                filters.add(new GPUImageSaturationFilter(1.04023f));
                filters.add(new GPUImageContrastFilter(0.988506f));
                filters.add(new GPUImageSharpenFilter(0.275862f));
                filters.add(new GPUImageWhiteBalanceFilter(4267.241f));
                filters.add(new GPUImageVignetteFilter(1.12069f));
                filters.add(new GPUImageRGBFilter(1.149425f, 1.04023f, 0.718391f));
                return new GPUImageFilterGroup(filters);
            case 285:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/curves/acv/A4.acv"));
                filters.add(new GPUImageContrastFilter(PS2GpuImageValueConversion.getContrastValue(HUE_ORANGE)));
                filters.add(new GPUImageSaturationFilter(PS2GpuImageValueConversion.getSaturaValue(-23.0f)));
                filters.add(new GPUImageHueFilter(3.0f));
                return new GPUImageFilterGroup(filters);
            case 286:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/curves/acv/A5.acv"));
                filters.add(new GPUImageSaturationFilter(PS2GpuImageValueConversion.getSaturaValue(-30.0f)));
                filters.add(new GPUImageHueFilter(-7.0f));
                filters.add(new GPUImageContrastFilter(PS2GpuImageValueConversion.getContrastValue(100.0f)));
                return new GPUImageFilterGroup(filters);
            case 287:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/curves/acv/A6.acv"));
                filters.add(new GPUImageContrastFilter(PS2GpuImageValueConversion.getContrastValue(58.0f)));
                filters.add(new GPUImageSaturationFilter(PS2GpuImageValueConversion.getSaturaValue(-12.0f)));
                return new GPUImageFilterGroup(filters);
            case 288:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/curves/acv/ADEN.acv"));
                filters.add(new GPUImageSaturationFilter(PS2GpuImageValueConversion.getSaturaValue(-52.0f)));
                filters.add(new GPUImageContrastFilter(PS2GpuImageValueConversion.getContrastValue(-25.0f)));
                filters.add(new GPUImageHueFilter(-5.0f));
                return new GPUImageFilterGroup(filters);
            case 289:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/curves/acv/B1.acv"));
                filters.add(new GPUImageSaturationFilter(PS2GpuImageValueConversion.getSaturaValue(-100.0f)));
                return new GPUImageFilterGroup(filters);
            case 290:
                filters.add(new GPUImageSaturationFilter(PS2GpuImageValueConversion.getSaturaValue(-100.0f)));
                filters.add(new GPUImageContrastFilter(PS2GpuImageValueConversion.getContrastValue(100.0f)));
                return new GPUImageFilterGroup(filters);
            case 291:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/curves/acv/CREMA.acv"));
                filters.add(new GPUImageSaturationFilter(PS2GpuImageValueConversion.getSaturaValue(-33.0f)));
                filters.add(new GPUImageContrastFilter(PS2GpuImageValueConversion.getContrastValue(-50.0f)));
                filters.add(new GPUImageBrightnessFilter(PS2GpuImageValueConversion.getHighlightsValue(5.0f)));
                return new GPUImageFilterGroup(filters);
            case 292:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/curves/acv/HB1.acv"));
                filters.add(new GPUImageContrastFilter(PS2GpuImageValueConversion.getContrastValue(100.0f)));
                filters.add(new GPUImageSaturationFilter(PS2GpuImageValueConversion.getSaturaValue(-40.0f)));
                filters.add(new GPUImageHueFilter(-7.0f));
                return new GPUImageFilterGroup(filters);
            case 293:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/curves/acv/LUDWING.acv"));
                filters.add(new GPUImageContrastFilter(PS2GpuImageValueConversion.getContrastValue(64.0f)));
                filters.add(new GPUImageSaturationFilter(PS2GpuImageValueConversion.getSaturaValue(-20.0f)));
                filters.add(new GPUImageHueFilter(8.0f));
                return new GPUImageFilterGroup(filters);
            case 294:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/curves/acv/M5.acv"));
                filters.add(new GPUImageSaturationFilter(PS2GpuImageValueConversion.getSaturaValue(-20.0f)));
                filters.add(new GPUImageContrastFilter(PS2GpuImageValueConversion.getContrastValue(50.0f)));
                filters.add(new GPUImageHighlightShadowFilter(PS2GpuImageValueConversion.getHighlightsValue(5.0f)));
                return new GPUImageFilterGroup(filters);
            case 295:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/curves/acv/SLUMBER.acv"));
                filters.add(new GPUImageSaturationFilter(PS2GpuImageValueConversion.getSaturaValue(-40.0f)));
                filters.add(new GPUImageContrastFilter(PS2GpuImageValueConversion.getContrastValue(-7.0f)));
                filters.add(new GPUImageHueFilter(5.0f));
                filters.add(new GPUImageHighlightShadowFilter(PS2GpuImageValueConversion.getHighlightsValue(7.0f)));
                return new GPUImageFilterGroup(filters);
            case 296:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/curves/acv/T1.acv"));
                filters.add(new GPUImageSaturationFilter(PS2GpuImageValueConversion.getSaturaValue(-4.0f)));
                filters.add(new GPUImageContrastFilter(PS2GpuImageValueConversion.getContrastValue(-50.0f)));
                filters.add(new GPUImageHueFilter(-1.0f));
                GPUImageColorFilter gPUImageColorFilter = new GPUImageColorFilter(-7829368);
                gPUImageColorFilter.setMix(0.45f);
                filters.add(gPUImageColorFilter);
                return new GPUImageFilterGroup(filters);
            case 297:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/curves/acv/X1.acv"));
                filters.add(new GPUImageSaturationFilter(PS2GpuImageValueConversion.getSaturaValue(-100.0f)));
                filters.add(new GPUImageContrastFilter(PS2GpuImageValueConversion.getContrastValue(26.0f)));
                return new GPUImageFilterGroup(filters);
            case 298:
                GPUImageSceneLevelControlFilter sceneFireWorkLevelControlFilter = new GPUImageSceneLevelControlFilter();
                sceneFireWorkLevelControlFilter.setSceneParameter(0.0f, 0.81f, 0.7058824f, 0.0f, TextTrackStyle.DEFAULT_FONT_SCALE);
                filters.add(sceneFireWorkLevelControlFilter);
                return new GPUImageFilterGroup(filters);
            case 299:
                GPUImageSceneLevelControlFilter sceneNightLevelControlFilter = new GPUImageSceneLevelControlFilter();
                sceneNightLevelControlFilter.setSceneParameter(0.05882353f, 1.8f, 0.75686276f, 0.0f, TextTrackStyle.DEFAULT_FONT_SCALE);
                filters.add(sceneNightLevelControlFilter);
                return new GPUImageFilterGroup(filters);
            case 300:
                GPUImageSceneLevelControlFilter sceneSunsetLevelControlFilter = new GPUImageSceneLevelControlFilter();
                sceneSunsetLevelControlFilter.setSceneParameter(0.0f, 0.75f, TextTrackStyle.DEFAULT_FONT_SCALE, 0.0f, TextTrackStyle.DEFAULT_FONT_SCALE);
                filters.add(sceneSunsetLevelControlFilter);
                return new GPUImageFilterGroup(filters);
            case 301:
                GPUImageSceneLevelControlFilter sceneBacklitLevelControlFilter = new GPUImageSceneLevelControlFilter();
                sceneBacklitLevelControlFilter.setSceneParameter(0.11764706f, 1.2f, 0.9098039f, 0.0f, TextTrackStyle.DEFAULT_FONT_SCALE);
                filters.add(sceneBacklitLevelControlFilter);
                return new GPUImageFilterGroup(filters);
            case 302:
                GPUImageSceneLevelControlFilter sceneDarkenLevelControlFilter = new GPUImageSceneLevelControlFilter();
                sceneDarkenLevelControlFilter.setSceneParameter(0.05490196f, 0.64f, TextTrackStyle.DEFAULT_FONT_SCALE, 0.0f, TextTrackStyle.DEFAULT_FONT_SCALE);
                filters.add(sceneDarkenLevelControlFilter);
                return new GPUImageFilterGroup(filters);
            case 303:
                GPUImageSceneLevelControlFilter sceneBrightenLevelControlFilter = new GPUImageSceneLevelControlFilter();
                sceneBrightenLevelControlFilter.setSceneParameter(0.0f, 1.5f, 0.7921569f, 0.0f, TextTrackStyle.DEFAULT_FONT_SCALE);
                filters.add(sceneBrightenLevelControlFilter);
                return new GPUImageFilterGroup(filters);
            case 304:
                GPUImageSceneLevelControlFilter sceneHighsatLevelControlFilter = new GPUImageSceneLevelControlFilter();
                sceneHighsatLevelControlFilter.setSceneParameter(0.06666667f, TextTrackStyle.DEFAULT_FONT_SCALE, 0.7529412f, 0.0f, TextTrackStyle.DEFAULT_FONT_SCALE);
                filters.add(sceneHighsatLevelControlFilter);
                return new GPUImageFilterGroup(filters);
            case 305:
                filters.add(new GPUImageSceneLowSaturationFilter());
                return new GPUImageFilterGroup(filters);
            case 306:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Scene/scene_backlit.jpg", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 307:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Scene/scene_cloudy.jpg", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 308:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Scene/scene_darken.jpg", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 309:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Scene/scene_flash.jpg", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 310:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Scene/scene_fluorescent.jpg", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 311:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Scene/scene_food.jpg", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 312:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Scene/scene_landscape.jpg", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 313:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Scene/scene_night.jpg", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 314:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Scene/scene_portrait.jpg", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 315:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Scene/scene_sandsnow.jpg", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 316:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Scene/scene_shade.jpg", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 317:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Scene/scene_sunset.jpg", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 318:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/Scene/scene_theatre.jpg", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 319:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/SceneW/scene_w_fengjing.jpg", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 320:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/SceneW/scene_w_shiwu.jpg", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 321:
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/SceneW/scene_w_yanhuo.jpg", GPUImageToneCurveMapFilter.class));
                return new GPUImageFilterGroup(filters);
            case 322:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/name_acv/Suri.acv"));
                return new GPUImageFilterGroup(filters);
            case 323:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/name_acv/Taylor.acv"));
                return new GPUImageFilterGroup(filters);
            case 324:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/name_acv/Monroe.acv"));
                return new GPUImageFilterGroup(filters);
            case 325:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/name_acv/Shirley.acv"));
                return new GPUImageFilterGroup(filters);
            case 326:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/name_acv/Hepburn.acv"));
                return new GPUImageFilterGroup(filters);
            case 327:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/name_acv/Lily.acv"));
                return new GPUImageFilterGroup(filters);
            case 328:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/name_acv/Alsa.acv"));
                return new GPUImageFilterGroup(filters);
            case 329:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/name_acv/Garbo.acv"));
                return new GPUImageFilterGroup(filters);
            case 330:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/name_acv/Ingrid.acv"));
                return new GPUImageFilterGroup(filters);
            case 331:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/name_acv/Miho.acv"));
                return new GPUImageFilterGroup(filters);
            case 332:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/name_acv/Betty.acv"));
                return new GPUImageFilterGroup(filters);
            case 333:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/name_acv/Sophia.acv"));
                return new GPUImageFilterGroup(filters);
            case 334:
                filters.add(new GPUImageSaturationFilter(0.0f));
                filters.add(new GPUImageExposureFilter(-0.5f));
                centerPoint.x = 0.35f;
                centerPoint.y = 0.35f;
                filters.add(new GPUImageVignetteExposureFilter(centerPoint, 0.2f, -0.5f, 0.4f, 0.85f));
                filters.add(new GPUImageVignetteContrastFilter(centerPoint, 0.7f, 1.2f, 0.2f, 0.75f));
                filters.add(new GPUImageVignetteSharpenFilter(centerPoint, 0.18f, -0.45f, 0.2f, 0.75f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/name_acv/Vivien.acv"));
                return new GPUImageFilterGroup(filters);
            case 335:
                Log.d("xxxxxxxxx ", "");
                filters.add(GPUImageFilterCreator.createFilterForTwoInputFilter(context, "filter/BW/Kopan/map.png", GPUImageToneCurveMapFilter.class));
                filters.add(new GPUImageSaturationFilter(0.0f));
                return new GPUImageFilterGroup(filters);
            case 336:
                GPUImageFilter filter20 = GPUImageFilterCreator.createACVCurveFilter(context, "filter/lomo/l1.acv");
                filter20.setMix(0.6f);
                filters.add(filter20);
                filters.add(new GPUImageVignetteFilter());
                return new GPUImageFilterGroup(filters);
            case 337:
                filters.add(new GPUImageGrayscaleFilter());
                GPUImageFilter filter1 = GPUImageFilterCreator.createACVCurveFilter(context, "filter/lomo/l2.acv");
                filter1.setMix(0.6f);
                filters.add(filter1);
                filters.add(new GPUImageVignetteFilter());
                return new GPUImageFilterGroup(filters);
            case 338:
                GPUImageFilter filter21 = GPUImageFilterCreator.createACVCurveFilter(context, "filter/lomo/l3.acv");
                filter21.setMix(0.875f);
                filters.add(filter21);
                filters.add(new GPUImageVignetteFilter());
                return new GPUImageFilterGroup(filters);
            case 339:
                filters.add(new GPUImageSaturationFilter(0.63f));
                GPUImageLevelsFilter filter = new GPUImageLevelsFilter();
                filter.setMin(0.0f, 0.71f, 0.8901961f);
                filters.add(filter);
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/lomo/l4.acv"));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, GPUImageOverlayBlendFilter.class));
                filters.add(new GPUImageOpacityFilter(0.14f));
                filters.add(new GPUImageVignetteFilter());
                return new GPUImageFilterGroup(filters);
            case 340:
                GPUImageFilter filter22 = GPUImageFilterCreator.createACVCurveFilter(context, "filter/lomo/l5.acv");
                filter22.setMix(0.9f);
                filters.add(filter22);
                filters.add(new GPUImageVignetteFilter());
                return new GPUImageFilterGroup(filters);
            case 341:
                filters.add(new GPUImageSaturationFilter(0.75f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/lomo/l6.acv"));
                filters.add(new GPUImageVignetteFilter());
                return new GPUImageFilterGroup(filters);
            case 342:
                GPUImageLevelsFilter filter2 = new GPUImageLevelsFilter();
                filter2.setMin(0.0f, 0.42f, 0.92156863f);
                filter2.setRedMin(0.0f, 1.56f, 0.7019608f, 0.39215687f, 0.48235294f);
                filters.add(filter2);
                filters.add(new GPUImageVignetteFilter());
                return new GPUImageFilterGroup(filters);
            case 343:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/lomo/l8.acv"));
                GPUImageColorBalanceFilter filter3 = new GPUImageColorBalanceFilter();
                filter3.setShowdows(new float[]{-0.1f, -0.135f, 0.0f});
                filter3.setPreserveLuminosity(false);
                filters.add(filter3);
                filters.add(new GPUImageVignetteFilter());
                return new GPUImageFilterGroup(filters);
            case 344:
                GPUImageFilter filter23 = GPUImageFilterCreator.createACVCurveFilter(context, "filter/lomo/l9.acv");
                filter23.setMix(0.6f);
                filters.add(filter23);
                new GPUImageLevelsFilter().setMin(0.0f, 0.8f, TextTrackStyle.DEFAULT_FONT_SCALE, 0.13333334f, 0.8627451f);
                filters.add(new GPUImageVignetteFilter());
                return new GPUImageFilterGroup(filters);
            case 345:
                filters.add(new GPUImageSaturationFilter(0.85f));
                GPUImageFilter filter24 = GPUImageFilterCreator.createACVCurveFilter(context, "filter/lomo/l10.acv");
                filter24.setMix(0.55f);
                filters.add(filter24);
                filters.add(new GPUImageVignetteFilter());
                return new GPUImageFilterGroup(filters);
            case 346:
                fArr5 = new float[3];
                fArr4 = new float[3];
                filters.add(new GPUImageColorBalanceFilter(new float[]{0.0f, -0.05f, 0.0f}, new float[]{-0.12f, 0.09f, -0.03f}, new float[]{0.0f, -0.065f, 0.13f}));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, 863504640, GPUImageExclusionBlendFilter.class));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/lomo/l11.acv"));
                filters.add(new GPUImageVignetteFilter());
                return new GPUImageFilterGroup(filters);
            case 347:
                filters.add(new GPUImageOpacityFilter(0.6f));
                GPUImageColorBalanceFilter filter6 = new GPUImageColorBalanceFilter();
                filter6.setShowdows(new float[]{0.0f, 0.0f, 0.105f});
                filter6.setMidtones(new float[]{-0.23f, 0.01f, -0.16f});
                filter6.setPreserveLuminosity(false);
                filters.add(filter6);
                filters.add(GPUImageFilterCreator.createBlendFilter(context, -5456156, GPUImageSoftLightBlendFilter.class));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, -16767389, GPUImageLightenBlendFilter.class));
                filters.add(new GPUImageVignetteFilter());
                return new GPUImageFilterGroup(filters);
            case 348:
                filters.add(GPUImageFilterCreator.createBlendFilter(context, -9652759, GPUImageSoftLightBlendFilter.class));
                filters.add(new GPUImageVignetteFilter());
                return new GPUImageFilterGroup(filters);
            case 349:
                filters.add(new GPUImageSaturationFilter(1.21f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/lomo/l14.acv"));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, -16185051, GPUImageExclusionBlendFilter.class));
                filters.add(new GPUImageSharpenFilter());
                filters.add(new GPUImageVignetteFilter());
                return new GPUImageFilterGroup(filters);
            case 350:
                GPUImageLevelsFilter filter7 = new GPUImageLevelsFilter();
                filter7.setMin(0.0f, TextTrackStyle.DEFAULT_FONT_SCALE, TextTrackStyle.DEFAULT_FONT_SCALE, 0.11372549f, 0.8745098f);
                filters.add(filter7);
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/lomo/l15.acv"));
                filter7.setMin(0.1764706f, 1.16f, 0.8784314f, 0.08627451f, 0.85882354f);
                filters.add(filter7);
                filters.add(new GPUImageVignetteFilter());
                return new GPUImageFilterGroup(filters);
            case 351:
                GPUImageLevelsFilter filter8 = new GPUImageLevelsFilter();
                filter8.setMin(0.11764706f, 0.7f, TextTrackStyle.DEFAULT_FONT_SCALE, 0.19215687f, TextTrackStyle.DEFAULT_FONT_SCALE);
                filters.add(filter8);
                GPUImageFilter filter25 = GPUImageFilterCreator.createACVCurveFilter(context, "filter/lomo/l16.acv");
                filter25.setMix(0.6f);
                filters.add(filter25);
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/lomo/l16.acv"));
                filters.add(new GPUImageVignetteFilter());
                return new GPUImageFilterGroup(filters);
            case 352:
                fArr5 = new float[3];
                fArr4 = new float[3];
                filters.add(new GPUImageColorBalanceFilter(new float[]{-0.21f, -0.04f, 0.365f}, new float[]{-0.115f, 0.065f, 0.15f}, new float[]{0.21f, -0.01f, -0.335f}));
                GPUImageLevelsFilter filter10 = new GPUImageLevelsFilter();
                filter10.setMin(0.03529412f, 1.74f, 0.80784315f, 0.09803922f, 0.8980392f);
                filters.add(filter10);
                filters.add(new GPUImageOpacityFilter(0.6f));
                filters.add(new GPUImageVignetteFilter());
                return new GPUImageFilterGroup(filters);
            case 353:
                GPUImageLevelsFilter filter11 = new GPUImageLevelsFilter();
                filter11.setMin(0.0f, 0.6f, TextTrackStyle.DEFAULT_FONT_SCALE);
                filters.add(filter11);
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/lomo/l18.acv"));
                filters.add(new GPUImageHueFilter());
                filters.add(new GPUImageVignetteFilter());
                return new GPUImageFilterGroup(filters);
            case 354:
                filters.add(new GPUImageBrightnessFilter(-0.25f));
                filters.add(new GPUImageContrastFilter(2.0f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/lomo/l19.acv"));
                fArr5 = new float[3];
                fArr4 = new float[3];
                float[] fArr6 = new float[3];
                filters.add(new GPUImageColorBalanceFilter(new float[]{0.0f, -0.025f, 0.12f}, new float[]{0.0f, 0.0f, 0.31f}, new float[]{0.0f, -0.015f, -0.065f}, false));
                filters.add(new GPUImageBrightnessFilter(0.27f));
                filters.add(new GPUImageContrastFilter(0.76f));
                filters.add(new GPUImageBrightnessFilter(0.03f));
                filters.add(new GPUImageContrastFilter(1.11f));
                filters.add(new GPUImageVignetteFilter());
                return new GPUImageFilterGroup(filters);
            case 355:
                filters.add(new GPUImageContrastFilter(1.25f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/lomo/l20.acv"));
                filters.add(new GPUImageVignetteFilter());
                return new GPUImageFilterGroup(filters);
            case 356:
                GPUImageColorBalanceFilter filter12 = new GPUImageColorBalanceFilter();
                filter12.setMidtones(new float[]{-0.125f, 0.0f, 0.135f});
                filter12.setPreserveLuminosity(false);
                filters.add(filter12);
                filters.add(new GPUImageVignetteFilter());
                return new GPUImageFilterGroup(filters);
            case 357:
                GPUImageColorBalanceFilter filter14 = new GPUImageColorBalanceFilter();
                filter14.setMidtones(new float[]{0.195f, 0.0f, -0.37f});
                filters.add(filter14);
                filters.add(GPUImageFilterCreator.createBlendFilter(context, -2613, GPUImageMultiplyBlendFilter.class));
                GPUImageColorBalanceFilter filter15 = new GPUImageColorBalanceFilter();
                filter15.setMidtones(new float[]{0.315f, 0.0f, 0.0f});
                filters.add(filter15);
                filters.add(new GPUImageVignetteFilter());
                return new GPUImageFilterGroup(filters);
            case 358:
                GPUImageColorBalanceFilter filter18 = new GPUImageColorBalanceFilter();
                filter18.setMidtones(new float[]{0.415f, 0.0f, 0.0f});
                filters.add(filter18);
                filters.add(GPUImageFilterCreator.createBlendFilter(context, -2613, GPUImageMultiplyBlendFilter.class));
                GPUImageColorBalanceFilter filter16 = new GPUImageColorBalanceFilter();
                filter16.setMidtones(new float[]{-0.24f, 0.0f, 0.11f});
                filters.add(filter16);
                filters.add(new GPUImageVignetteFilter());
                return new GPUImageFilterGroup(filters);
            case 359:
                fArr2[0] = -0.315f;
                fArr2[1] = -0.085f;
                fArr2[2] = 0.23f;
                fArr3[2] = -0.5f;
                filters.add(new GPUImageColorBalanceFilter(fArr, fArr2, fArr3));
                GPUImageLevelsFilter filter17 = new GPUImageLevelsFilter();
                filter17.setMin(0.0f, 1.23f, 0.7647059f);
                filters.add(filter17);
                fArr2[0] = 0.125f;
                fArr2[1] = 0.0f;
                fArr2[2] = 0.185f;
                fArr3[2] = 0.0f;
                filters.add(new GPUImageColorBalanceFilter(fArr, fArr2, fArr3, false));
                fArr2[0] = -0.19f;
                fArr2[2] = 0.22f;
                filters.add(new GPUImageColorBalanceFilter(fArr, fArr2, fArr3, false));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, -2104, GPUImageMultiplyBlendFilter.class));
                fArr2[0] = -0.305f;
                fArr2[1] = -0.095f;
                fArr2[2] = -0.105f;
                filters.add(new GPUImageColorBalanceFilter(fArr, fArr2, fArr3, false));
                filters.add(new GPUImageVignetteFilter());
                return new GPUImageFilterGroup(filters);
            case 360:
                fArr2[2] = -0.195f;
                fArr3[1] = 0.145f;
                fArr[2] = 0.33f;
                filters.add(new GPUImageColorBalanceFilter(fArr, fArr2, fArr3, false));
                fArr2[0] = -0.45f;
                fArr2[1] = -0.32f;
                fArr2[2] = -0.45f;
                fArr3[0] = -0.085f;
                fArr3[1] = 0.0f;
                fArr[2] = 0.0f;
                filters.add(new GPUImageColorBalanceFilter(fArr, fArr2, fArr3, false));
                filters.add(new GPUImageVignetteFilter());
                return new GPUImageFilterGroup(filters);
            case 361:
                fArr2[1] = -0.29f;
                fArr2[2] = -0.145f;
                fArr3[2] = 0.175f;
                fArr[0] = -0.49f;
                filters.add(new GPUImageColorBalanceFilter(fArr, fArr2, fArr));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, -2063609232, GPUImageMultiplyBlendFilter.class));
                filters.add(new GPUImageVignetteFilter());
                return new GPUImageFilterGroup(filters);
            case 362:
                fArr2[2] = -0.125f;
                fArr3[2] = 0.13f;
                fArr[0] = -0.49f;
                filters.add(new GPUImageColorBalanceFilter(fArr, fArr2, fArr3));
                fArr2[2] = 0.0f;
                fArr3[2] = 0.0f;
                fArr[0] = 0.0f;
                fArr[2] = 0.34f;
                filters.add(new GPUImageColorBalanceFilter(fArr, fArr2, fArr3));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, -9559, GPUImageMultiplyBlendFilter.class));
                filters.add(new GPUImageOpacityFilter(0.55f));
                filters.add(new GPUImageVignetteFilter());
                return new GPUImageFilterGroup(filters);
            case 363:
                filters.add(new GPUImageSaturationFilter(0.0f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/lomo/l28.acv"));
                fArr2[0] = -0.055f;
                fArr2[2] = 0.08f;
                fArr[0] = 0.145f;
                fArr[2] = 0.085f;
                filters.add(new GPUImageColorBalanceFilter(fArr, fArr2, fArr3));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, -19456, GPUImageMultiplyBlendFilter.class));
                filters.add(new GPUImageOpacityFilter(0.09f));
                filters.add(new GPUImageVignetteFilter());
                return new GPUImageFilterGroup(filters);
            case 364:
                fArr2[0] = 0.315f;
                fArr2[1] = 0.3f;
                fArr2[2] = 0.345f;
                fArr[0] = 0.055f;
                fArr[1] = -0.245f;
                fArr[2] = -0.27f;
                fArr3[1] = 0.12f;
                fArr3[2] = 0.26f;
                filters.add(new GPUImageColorBalanceFilter(fArr, fArr2, fArr3));
                filters.add(new GPUImageColorFilter(-3417159));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, -3417159, GPUImageMultiplyBlendFilter.class));
                filters.add(new GPUImageVignetteFilter());
                return new GPUImageFilterGroup(filters);
            case 365:
                fArr2[0] = 0.23f;
                fArr2[1] = -0.145f;
                fArr2[2] = 0.03f;
                fArr3[0] = -0.25f;
                fArr3[1] = 0.05f;
                fArr3[2] = -0.15f;
                filters.add(new GPUImageColorBalanceFilter(fArr, fArr2, fArr3));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, -16763238, GPUImageExclusionBlendFilter.class));
                filters.add(new GPUImageOpacityFilter(0.51f));
                fArr2[0] = 0.0f;
                fArr2[1] = -0.17f;
                fArr2[2] = -0.01f;
                fArr3[0] = 0.0f;
                fArr3[1] = 0.0f;
                fArr3[2] = 0.0f;
                filters.add(new GPUImageColorBalanceFilter(fArr, fArr2, fArr3));
                filters.add(new GPUImageVignetteFilter());
                return new GPUImageFilterGroup(filters);
            case 366:
                filters.add(new GPUImageVignetteFilter(0.95f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_lomo/shx_lomo_1.acv"));
                return new GPUImageFilterGroup(filters);
            case 367:
                filters.add(new GPUImageVignetteFilter(0.95f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_lomo/shx_lomo_2.acv"));
                return new GPUImageFilterGroup(filters);
            case 368:
                filters.add(new GPUImageVignetteFilter(0.95f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_lomo/shx_lomo_3.acv"));
                return new GPUImageFilterGroup(filters);
            case 369:
                filters.add(new GPUImageVignetteFilter(0.95f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_lomo/shx_lomo_4.acv"));
                return new GPUImageFilterGroup(filters);
            case 370:
                filters.add(new GPUImageVignetteFilter(0.95f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_lomo/shx_lomo_5.acv"));
                return new GPUImageFilterGroup(filters);
            case 371:
                filters.add(new GPUImageVignetteFilter(0.95f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_lomo/shx_lomo_6.acv"));
                return new GPUImageFilterGroup(filters);
            case 372:
                filters.add(new GPUImageVignetteFilter(0.95f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_lomo/shx_lomo_7.acv"));
                return new GPUImageFilterGroup(filters);
            case 373:
                filters.add(new GPUImageVignetteFilter(0.95f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_lomo/shx_lomo_8.acv"));
                return new GPUImageFilterGroup(filters);
            case 374:
                filters.add(new GPUImageVignetteFilter(0.95f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_lomo/shx_lomo_9.acv"));
                return new GPUImageFilterGroup(filters);
            case 375:
                filters.add(new GPUImageVignetteFilter(0.95f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_lomo/shx_lomo_10.acv"));
                return new GPUImageFilterGroup(filters);
            case 376:
                filters.add(new GPUImageVignetteFilter(0.95f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_lomo/shx_lomo_11.acv"));
                return new GPUImageFilterGroup(filters);
            case 377:
                filters.add(new GPUImageVignetteFilter(0.95f, 0.5f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_lomo/shx_lomo_12.acv"));
                return new GPUImageFilterGroup(filters);
            case 378:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_pure/shx_pure_1.acv"));
                return new GPUImageFilterGroup(filters);
            case 379:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_pure/shx_pure_2.acv"));
                return new GPUImageFilterGroup(filters);
            case 380:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_pure/shx_pure_3.acv"));
                return new GPUImageFilterGroup(filters);
            case 381:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_pure/shx_pure_4.acv"));
                return new GPUImageFilterGroup(filters);
            case 382:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_pure/shx_pure_5.acv"));
                return new GPUImageFilterGroup(filters);
            case 383:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_pure/shx_pure_6.acv"));
                return new GPUImageFilterGroup(filters);
            case 384:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_pure/shx_pure_7.acv"));
                return new GPUImageFilterGroup(filters);
            case 385:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_pure/shx_pure_8.acv"));
                return new GPUImageFilterGroup(filters);
            case 386:
                filters.add(new GPUImageSaturationFilter(0.7f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_pure_s/shx_pure_s_1.acv"));
                return new GPUImageFilterGroup(filters);
            case 387:
                filters.add(new GPUImageSaturationFilter(0.7f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_pure_s/shx_pure_s_2.acv"));
                return new GPUImageFilterGroup(filters);
            case 388:
                filters.add(new GPUImageSaturationFilter(0.7f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_pure_s/shx_pure_s_3.acv"));
                return new GPUImageFilterGroup(filters);
            case 389:
                filters.add(new GPUImageSaturationFilter(0.7f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_pure_s/shx_pure_s_4.acv"));
                return new GPUImageFilterGroup(filters);
            case 390:
                filters.add(new GPUImageSaturationFilter(0.7f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_pure_s/shx_pure_s_5.acv"));
                return new GPUImageFilterGroup(filters);
            case 391:
                filters.add(new GPUImageSaturationFilter(0.7f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_pure_s/shx_pure_s_6.acv"));
                return new GPUImageFilterGroup(filters);
            case 392:
                filters.add(new GPUImageSaturationFilter(0.7f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_pure_s/shx_pure_s_7.acv"));
                return new GPUImageFilterGroup(filters);
            case 393:
                filters.add(new GPUImageSaturationFilter(0.7f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_pure_s/shx_pure_s_8.acv"));
                return new GPUImageFilterGroup(filters);
            case 394:
                filters.add(new GPUImageSaturationFilter(0.7f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_pure_s/shx_pure_s_9.acv"));
                return new GPUImageFilterGroup(filters);
            case 395:
                filters.add(new GPUImageSaturationFilter(0.7f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_pure_s/shx_pure_s_10.acv"));
                return new GPUImageFilterGroup(filters);
            case 396:
                filters.add(new GPUImageSaturationFilter(0.7f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_pure_s/shx_pure_s_11.acv"));
                return new GPUImageFilterGroup(filters);
            case 397:
                filters.add(new GPUImageSaturationFilter(0.7f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_pure_s/shx_pure_s_12.acv"));
                return new GPUImageFilterGroup(filters);
            case 398:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_film/shx_film_1.acv"));
                return new GPUImageFilterGroup(filters);
            case 399:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_film/shx_film_2.acv"));
                return new GPUImageFilterGroup(filters);
            case 400:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_film/shx_film_3.acv"));
                return new GPUImageFilterGroup(filters);
            case 401:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_film/shx_film_4.acv"));
                return new GPUImageFilterGroup(filters);
            case 402 /*402*/:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_film/shx_film_5.acv"));
                return new GPUImageFilterGroup(filters);
            case 403:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_film/shx_film_6.acv"));
                return new GPUImageFilterGroup(filters);
            case 404 /*404*/:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_film/shx_film_7.acv"));
                return new GPUImageFilterGroup(filters);
            case 405 /*405*/:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_film/shx_film_8.acv"));
                return new GPUImageFilterGroup(filters);
            case 406 /*406*/:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_film/shx_film_9.acv"));
                return new GPUImageFilterGroup(filters);
            case 407:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_film/shx_film_10.acv"));
                return new GPUImageFilterGroup(filters);
            case 408:
                filters.add(new GPUImageSaturationFilter(0.6f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_film_s/shx_film_s_1.acv"));
                return new GPUImageFilterGroup(filters);
            case 409 /*409*/:
                filters.add(new GPUImageSaturationFilter(0.6f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_film_s/shx_film_s_2.acv"));
                return new GPUImageFilterGroup(filters);
            case 410 /*410*/:
                filters.add(new GPUImageSaturationFilter(0.6f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_film_s/shx_film_s_3.acv"));
                return new GPUImageFilterGroup(filters);
            case 411 /*411*/:
                filters.add(new GPUImageSaturationFilter(0.6f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_film_s/shx_film_s_4.acv"));
                return new GPUImageFilterGroup(filters);
            case 412 /*412*/:
                filters.add(new GPUImageSaturationFilter(0.6f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_film_s/shx_film_s_5.acv"));
                return new GPUImageFilterGroup(filters);
            case 413 /*413*/:
                filters.add(new GPUImageSaturationFilter(0.6f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_film_s/shx_film_s_6.acv"));
                return new GPUImageFilterGroup(filters);
            case 414:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_retro/shx_retro_1.acv"));
                return new GPUImageFilterGroup(filters);
            case 415:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_retro/shx_retro_2.acv"));
                return new GPUImageFilterGroup(filters);
            case 416:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_retro/shx_retro_3.acv"));
                return new GPUImageFilterGroup(filters);
            case 417:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_retro/shx_retro_4.acv"));
                return new GPUImageFilterGroup(filters);
            case 418:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_retro/shx_retro_5.acv"));
                return new GPUImageFilterGroup(filters);
            case 419:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_retro/shx_retro_6.acv"));
                return new GPUImageFilterGroup(filters);
            case 420:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_retro/shx_retro_7.acv"));
                return new GPUImageFilterGroup(filters);
            case 421:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_retro/shx_retro_8.acv"));
                return new GPUImageFilterGroup(filters);
            case 422:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_retro/shx_retro_9.acv"));
                return new GPUImageFilterGroup(filters);
            case 423:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_retro/shx_retro_10.acv"));
                return new GPUImageFilterGroup(filters);
            case 424:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_retro/shx_retro_11.acv"));
                return new GPUImageFilterGroup(filters);
            case 425:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_retro/shx_retro_12.acv"));
                return new GPUImageFilterGroup(filters);
            case 426:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_retro/shx_retro_13.acv"));
                return new GPUImageFilterGroup(filters);
            case 427:
                filters.add(new GPUImageSaturationFilter(0.57f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_retro_s/shx_retro_s_1.acv"));
                return new GPUImageFilterGroup(filters);
            case 428:
                filters.add(new GPUImageSaturationFilter(0.57f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_retro_s/shx_retro_s_2.acv"));
                return new GPUImageFilterGroup(filters);
            case 429:
                filters.add(new GPUImageSaturationFilter(0.57f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_retro_s/shx_retro_s_3.acv"));
                return new GPUImageFilterGroup(filters);
            case 430:
                filters.add(new GPUImageSaturationFilter(0.57f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_retro_s/shx_retro_s_4.acv"));
                return new GPUImageFilterGroup(filters);
            case 431:
                filters.add(new GPUImageSaturationFilter(0.57f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/shx_retro_s/shx_retro_s_5.acv"));
                return new GPUImageFilterGroup(filters);
            case 432:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/softlight.acv"));
                filters.add(new GPUImageWhiteBalanceFilter(4850.0f, 40.0f));
                filters.add(new GPUImageGaussianBlurFilter(0.8f));
                return new GPUImageFilterGroup(filters);
            case 433:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/softlight.acv"));
                filters.add(new GPUImageWhiteBalanceFilter(4490.0f, 0.0f));
                filters.add(new GPUImageSaturationFilter(GPUAdjustRange.getSaturationRange(42)));
                filters.add(new GPUImageGaussianBlurFilter(0.4f));
                return new GPUImageFilterGroup(filters);
            case 434:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/softlight.acv"));
                filters.add(new GPUImageWhiteBalanceFilter(4850.0f, 40.0f));
                filters.add(new GPUImageGaussianBlurFilter(0.4f));
                return new GPUImageFilterGroup(filters);
            case 435:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/softlight.acv"));
                filters.add(new GPUImageWhiteBalanceFilter(7900.0f, 0.0f));
                filters.add(new GPUImageSaturationFilter(GPUAdjustRange.getSaturationRange(34)));
                filters.add(new GPUImageGaussianBlurFilter(0.4f));
                return new GPUImageFilterGroup(filters);
            case 436:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/softlight.acv"));
                filters.add(new GPUImageSaturationFilter(GPUAdjustRange.getSaturationRange(36)));
                filters.add(new GPUImageGaussianBlurFilter(0.2f));
                return new GPUImageFilterGroup(filters);
            case 437:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/softlight.acv"));
                filters.add(new GPUImageSaturationFilter(GPUAdjustRange.getSaturationRange(25)));
                filters.add(new GPUImageWhiteBalanceFilter(6300.0f, 0.0f));
                filters.add(new GPUImageGaussianBlurFilter(0.4f));
                return new GPUImageFilterGroup(filters);
            case 438:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/softlight.acv"));
                filters.add(new GPUImageSaturationFilter(GPUAdjustRange.getSaturationRange(40)));
                filters.add(new GPUImageWhiteBalanceFilter(4300.0f, 90.0f));
                filters.add(new GPUImageGaussianBlurFilter(0.64f));
                return new GPUImageFilterGroup(filters);
            case 439:
                return new GPUSeasonGloriousSpringBabyFilter();
            case 440:
                return new GPUSeasonSpringBlossomFilter();
            case 441:
                return new GPUSeasonSpringLightFilter();
            case 442:
                filters.add(new GPUImageBrightnessFilter(0.04f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/season/summer/classic_1.acv"));
                filters.add(new GPUImageBrightnessFilter(0.13f));
                GPUImageLevelsFilter filter13 = new GPUImageLevelsFilter();
                filter13.setRedMin(0.0f, 0.94f, TextTrackStyle.DEFAULT_FONT_SCALE);
                filter13.setGreenMin(0.0f, 0.96f, TextTrackStyle.DEFAULT_FONT_SCALE);
                filter13.setBlueMin(0.05490196f, 0.89f, 0.96862745f);
                filters.add(filter13);
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/season/summer/classic_2.acv"));
                return new GPUImageFilterGroup(filters);
            case 443:
                return new GPUSeasonSummerIndianFilter();
            case 444:
                return new GPUSeasonSummerDayFilter();
            case 445:
                GPUImageLevelsFilter filter19 = new GPUImageLevelsFilter();
                filter19.setMin(0.078431375f, TextTrackStyle.DEFAULT_FONT_SCALE, TextTrackStyle.DEFAULT_FONT_SCALE);
                filters.add(filter19);
                filters.add(new GPUImageExposureFilter(0.355f));
                filters.add(new GPUImageSaturationFilter(1.1f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/season/autumn/dawood_hamada.acv"));
                return new GPUImageFilterGroup(filters);
            case 446:
                return new GPUSeasonAutumnGentleFilter();
            case 447:
                return new GPUSeasonAutumnPremiumFilter();
            case 448:
                filters.add(new GPUImageSaturationFilter(1.3f));
                GPUImageLevelsFilter filter26 = new GPUImageLevelsFilter();
                filter26.setMin(0.09019608f, 1.16f, 0.88235295f, 0.07058824f, 0.9607843f);
                filters.add(filter26);
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/season/winter/iced.acv", GPUSeasonWinterIcedFilter.class));
                return new GPUImageFilterGroup(filters);
            case 449:
                return new GPUSeasonWinterSnappyBabyFilter();
            case 450:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/season/winter/soft_brown.acv", GPUSeasonWinterSoftBrownFilter.class);
            case 451:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/sweet/premium_1.acv"));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/sweet/premium_2.acv"));
                filters.add(new GPUSweetPremiumFilter());
                return new GPUImageFilterGroup(filters);
            case 452:
                return new GPUImageNewVibranceFilter(-1.0f);
            case 453:
                GPUImageLevelsFilter filter27 = new GPUImageLevelsFilter();
                filter27.setMin(0.0f, 1.23f, TextTrackStyle.DEFAULT_FONT_SCALE);
                filters.add(filter27);
                GPUImageLevelsFilter filter28 = new GPUImageLevelsFilter();
                filter28.setRedMin(0.0f, 1.1f, TextTrackStyle.DEFAULT_FONT_SCALE, 0.0f, 0.827451f);
                filter28.setGreenMin(0.0f, TextTrackStyle.DEFAULT_FONT_SCALE, 0.98039216f);
                filter28.setBlueMin(0.0f, TextTrackStyle.DEFAULT_FONT_SCALE, 0.85490197f);
                filters.add(filter28);
                return new GPUImageFilterGroup(filters);
            case 454:
                GPUImageLevelsFilter filter29 = new GPUImageLevelsFilter();
                filter29.setMin(0.0f, 0.81f, TextTrackStyle.DEFAULT_FONT_SCALE);
                filters.add(filter29);
                GPUImageLevelsFilter filter30 = new GPUImageLevelsFilter();
                filter30.setRedMin(0.0f, TextTrackStyle.DEFAULT_FONT_SCALE, TextTrackStyle.DEFAULT_FONT_SCALE, 0.11764706f, TextTrackStyle.DEFAULT_FONT_SCALE);
                filter30.setGreenMin(0.0f, TextTrackStyle.DEFAULT_FONT_SCALE, TextTrackStyle.DEFAULT_FONT_SCALE, 0.0f, 0.95686275f);
                filter30.setBlueMin(0.0f, 1.11f, TextTrackStyle.DEFAULT_FONT_SCALE, 0.078431375f, TextTrackStyle.DEFAULT_FONT_SCALE);
                filters.add(filter30);
                return new GPUImageFilterGroup(filters);
            case 455:
                GPUImageLevelsFilter filter31 = new GPUImageLevelsFilter();
                filter31.setMin(0.09803922f, 1.29f, TextTrackStyle.DEFAULT_FONT_SCALE, 0.25490198f, TextTrackStyle.DEFAULT_FONT_SCALE);
                filters.add(filter31);
                GPUImageLevelsFilter filter32 = new GPUImageLevelsFilter();
                filter32.setRedMin(0.17254902f, 1.1f, 0.95686275f);
                filter32.setGreenMin(0.0f, 1.14f, TextTrackStyle.DEFAULT_FONT_SCALE, 0.023529412f, TextTrackStyle.DEFAULT_FONT_SCALE);
                filter32.setBlueMin(0.0f, TextTrackStyle.DEFAULT_FONT_SCALE, TextTrackStyle.DEFAULT_FONT_SCALE, 0.06666667f, 0.9372549f);
                filters.add(filter32);
                filters.add(new GPUImageOpacityFilter(0.65f));
                return new GPUImageFilterGroup(filters);
            case 456:
                GPUImageLevelsFilter filter33 = new GPUImageLevelsFilter();
                filter33.setMin(0.07450981f, 1.24f, TextTrackStyle.DEFAULT_FONT_SCALE, 0.1882353f, TextTrackStyle.DEFAULT_FONT_SCALE);
                filters.add(filter33);
                GPUImageLevelsFilter filter34 = new GPUImageLevelsFilter();
                filter34.setRedMin(0.11372549f, 1.08f, 0.99215686f, 0.2901961f, 0.98039216f);
                filter34.setBlueMin(0.10980392f, 1.07f, TextTrackStyle.DEFAULT_FONT_SCALE, 0.2901961f, 0.93333334f);
                filters.add(filter34);
                return new GPUImageFilterGroup(filters);
            case 457:
                GPUImageLevelsFilter filter35 = new GPUImageLevelsFilter();
                filter35.setRedMin(0.0f, TextTrackStyle.DEFAULT_FONT_SCALE, TextTrackStyle.DEFAULT_FONT_SCALE, 0.10980392f, TextTrackStyle.DEFAULT_FONT_SCALE);
                filter35.setGreenMin(0.0f, TextTrackStyle.DEFAULT_FONT_SCALE, TextTrackStyle.DEFAULT_FONT_SCALE, 0.0f, 0.95686275f);
                filter35.setBlueMin(0.0f, TextTrackStyle.DEFAULT_FONT_SCALE, TextTrackStyle.DEFAULT_FONT_SCALE, 0.078431375f, TextTrackStyle.DEFAULT_FONT_SCALE);
                return filter35;
            case 458:
                GPUImageLevelsFilter filter36 = new GPUImageLevelsFilter();
                filter36.setBlueMin(0.0f, 1.3f, TextTrackStyle.DEFAULT_FONT_SCALE);
                return filter36;
            case 459:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/sweet/primuem_1.acv"));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, 142677526, GPUImageSoftLightBlendFilter.class));
                fArr2[0] = -0.06f;
                fArr2[1] = -0.03f;
                fArr2[2] = 0.115f;
                fArr[1] = -0.06f;
                fArr[2] = -0.06f;
                fArr3[0] = -0.06f;
                fArr3[2] = 0.025f;
                filters.add(new GPUImageColorBalanceFilter(fArr, fArr2, fArr3));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, 993300109, GPUImageScreenBlendFilter.class));
                new GPUImageLevelsFilter().setMin(0.08235294f, 0.77f, TextTrackStyle.DEFAULT_FONT_SCALE, 0.12941177f, TextTrackStyle.DEFAULT_FONT_SCALE);
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/sweet/primuem_2.acv"));
                filters.add(new GPUImageNewVibranceFilter(0.05f));
                return new GPUImageFilterGroup(filters);
            case 460:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/sweet/romance.acv", GPUSweetRomanceFilter.class);
            case 461:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/sweet/rusty_tint.acv", GPUSweetRustyTintFilter.class);
            case 462:
                return new GPUSweetSoCoolFilter();
            case 463:
                GPUImageLevelsFilter filter38 = new GPUImageLevelsFilter();
                filter38.setMin(0.0f, 1.5f, TextTrackStyle.DEFAULT_FONT_SCALE, 0.078431375f, TextTrackStyle.DEFAULT_FONT_SCALE);
                filters.add(filter38);
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/sweet/sweet.acv"));
                return new GPUImageFilterGroup(filters);
            case 464:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/sweet/sweet_fall_embrace.acv");
            case 465:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/sweet/wake_up_1.acv"));
                GPUImageLevelsFilter filter39 = new GPUImageLevelsFilter();
                filter39.setMin(0.050980393f, 1.13f, TextTrackStyle.DEFAULT_FONT_SCALE, 0.13725491f, 0.9529412f);
                filters.add(filter39);
                filter39.setMin(0.0f, TextTrackStyle.DEFAULT_FONT_SCALE, TextTrackStyle.DEFAULT_FONT_SCALE, 0.0f, TextTrackStyle.DEFAULT_FONT_SCALE);
                filter39.setBlueMin(0.0f, TextTrackStyle.DEFAULT_FONT_SCALE, TextTrackStyle.DEFAULT_FONT_SCALE, 0.20784314f, 0.89411765f);
                filters.add(filter39);
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/fresh/wake_up_2.acv"));
                filter39.setBlueMin(0.0f, TextTrackStyle.DEFAULT_FONT_SCALE, TextTrackStyle.DEFAULT_FONT_SCALE, 0.0f, TextTrackStyle.DEFAULT_FONT_SCALE);
                filter39.setGreenMin(0.0f, 1.11f, TextTrackStyle.DEFAULT_FONT_SCALE);
                filters.add(filter39);
                return new GPUImageFilterGroup(filters);
            case 466:
                return GPUImageFilterCreator.createBlendFilter(context, -65794, GPUImageSoftLightBlendFilter.class);
            case 467:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/fade/beautifully.acv", GPUFadeBeautifullyFilter.class);
            case 468:
                return new GPUFadeCoolHazeFilter();
            case 469:
                GPUImageLevelsFilter filter40 = new GPUImageLevelsFilter();
                filter40.setMin(0.039215688f, TextTrackStyle.DEFAULT_FONT_SCALE, TextTrackStyle.DEFAULT_FONT_SCALE);
                filters.add(filter40);
                filters.add(new GPUImageContrastFilter(0.8f));
                filters.add(new GPUImageNewVibranceFilter(0.25f));
                filters.add(new GPUImageSaturationFilter(0.45f));
                filters.add(new GPUImageExposureFilter(0.06f));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, 450746289, GPUImageSoftLightBlendFilter.class));
                return new GPUImageFilterGroup(filters);
            case 470:
                filters.add(GPUImageFilterCreator.createBlendFilter(context, -620159955, GPUImageExclusionBlendFilter.class));
                GPUImageLevelsFilter filter41 = new GPUImageLevelsFilter();
                filter41.setRedMin(0.0f, 1.03f, 0.99607843f);
                filter41.setBlueMin(0.0f, 0.97f, TextTrackStyle.DEFAULT_FONT_SCALE);
                filters.add(filter41);
                return new GPUImageFilterGroup(filters);
            case 471:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/fade/everyday.acv"));
                GPUImageLevelsFilter filter42 = new GPUImageLevelsFilter();
                filter42.setMin(0.039215688f, 1.1f, 0.9607843f);
                filters.add(filter42);
                filters.add(new GPUImageNewVibranceFilter(0.12f));
                filters.add(new GPUImageSaturationFilter(0.88f));
                filters.add(new GPUImageContrastFilter(0.96f));
                filters.add(new GPUImageExposureFilter(0.03f));
                return new GPUImageFilterGroup(filters);
            case 472:
                filters.add(new GPUImageBrightnessFilter(0.07f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/fade/lime.acv"));
                filters.add(new GPUImageBrightnessFilter(0.07f));
                return new GPUImageFilterGroup(filters);
            case 473:
                filters.add(new GPUImageContrastFilter(1.34f));
                filters.add(new GPUImageSaturationFilter(0.81f));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, 704643071, GPUImageNormalBlendFilter.class));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, 451103820, GPUImageScreenBlendFilter.class));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, 447183225, GPUImageLightenBlendFilter.class));
                return new GPUImageFilterGroup(filters);
            case 475:
                filters.add(new GPUImageSaturationFilter(0.55f));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, 862786269, GPUImageAddBlendFilter.class));
                filters.add(new GPUImageSaturationFilter(0.7f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/fade/retro.acv"));
                return new GPUImageFilterGroup(filters);
            case 476:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/fade/white_wash.acv"));
                filters.add(new GPUImageNewVibranceFilter(-0.3f));
                return new GPUImageFilterGroup(filters);
            case 477:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/retro/ps.acv"));
                filters.add(new GPUImageHueFilter(12.0f));
                filters.add(new GPUImageSaturationFilter(0.83f));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, 301989888, GPUImageNormalBlendFilter.class));
                GPUImageLevelsFilter filter43 = new GPUImageLevelsFilter();
                filter43.setMin(0.12941177f, TextTrackStyle.DEFAULT_FONT_SCALE, TextTrackStyle.DEFAULT_FONT_SCALE);
                filters.add(filter43);
                filters.add(new GPUImageContrastFilter(1.08f));
                return new GPUImageFilterGroup(filters);
            case 478:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/retro/a_vol_1_1.acv"));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/retro/a_vol_1_2.acv"));
                filters.add(new GPUImageHueFilter(TextTrackStyle.DEFAULT_FONT_SCALE));
                filters.add(new GPUImageSaturationFilter(0.52f));
                return new GPUImageFilterGroup(filters);
            case 479:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/retro/a_vol_2_1.acv"));
                return new GPUImageFilterGroup(filters);
            case 480:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/retro/a_vol_3_1.acv");
            case 481:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/retro/a_vol_4_1.acv");
            case 482:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/retro/a_vol_12_1.acv");
            case 483:
                filters.add(new GPUImageSaturationFilter(0.75f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/retro/a_vol_20_1.acv"));
                return new GPUImageFilterGroup(filters);
            case 484:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/retro/a_vol_22_1.acv");
            case 485:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/retro/ambitious_1.acv"));
                filters.add(new GPUImageNewVibranceFilter(0.39f));
                filters.add(new GPUImageSaturationFilter(0.9f));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, 452954624, GPUImageExclusionBlendFilter.class));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/retro/ambitious_2.acv"));
                return new GPUImageFilterGroup(filters);
            case 486:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/retro/brisk_1.acv", 0.5f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/retro/brisk_2.acv"));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/retro/brisk_3.acv", 0.5f));
                return new GPUImageFilterGroup(filters);
            case 487:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/retro/c_vol_2_1.acv");
            case 488:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/retro/c_vol_8_1.acv", RetroCVol8Filter.class);
            case 489:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/retro/c_vol_13_1.acv"));
                fArr2[0] = 0.1f;
                fArr2[1] = -0.215f;
                fArr2[2] = -0.26f;
                filters.add(new GPUImageColorBalanceFilter(fArr, fArr2, fArr3));
                filters.add(new GPUImageSaturationFilter(0.85f));
                return new GPUImageFilterGroup(filters);
            case 490:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/retro/chestnut_brown_1.acv", RetroChestnutBrownFilter.class);
            case 491:
                filters.add(GPUImageFilterCreator.createBlendFilter(context, 1506150620, GPUImageLuminosityBlendFilter.class));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/retro/cp_24.acv"));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, 452393048, GPUImageExclusionBlendFilter.class));
                return new GPUImageFilterGroup(filters);
            case 492:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/retro/delicate_brown.acv", RetroDelicateBrownFilter.class);
            case 493:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/retro/flash_back_1.acv", 0.5f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/retro/flash_back_2.acv"));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/retro/flash_back_3.acv", 0.5f, GPUImageToneCurveLuminosityBlendFilter.class));
                return new GPUImageFilterGroup(filters);
            case 494:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/retro/premium_1.acv", RetroPremiumFilter.class);
            case 495:
                filters.add(new GPUImageSaturationFilter(0.8f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/retro/3.acv"));
                GPUImageLevelsFilter filter44 = new GPUImageLevelsFilter();
                filter44.setMin(0.0f, TextTrackStyle.DEFAULT_FONT_SCALE, TextTrackStyle.DEFAULT_FONT_SCALE, 0.05882353f, 0.93333334f);
                filters.add(filter44);
                filters.add(GPUImageFilterCreator.createBlendFilter(context, 1303359540, GPUImageHardLightBlendFilter.class));
                return new GPUImageFilterGroup(filters);
            case 496:
                return new Retro17Filter();
            case 497:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/retro/royal_1.acv"));
                filters.add(new GPUImageNewVibranceFilter(0.3f));
                return new GPUImageFilterGroup(filters);
            case 498:
                filters.add(new GPUImageGrayscaleFilter());
                filters.add(GPUImageFilterCreator.createBlendFilter(context, -15526634, GPUImageExclusionBlendFilter.class));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, -1558043296, GPUImageExclusionBlendFilter.class));
                fArr[0] = 0.055f;
                fArr[2] = 0.055f;
                filters.add(new GPUImageColorBalanceFilter(fArr, fArr2, fArr3));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/retro/night_fate.acv"));
                return new GPUImageFilterGroup(filters);
            case 499:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/retro/spirited_1.acv", 0.5f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/retro/spirited_2.acv"));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/retro/spirited_3.acv", 0.5f, GPUImageToneCurveLuminosityBlendFilter.class));
                return new GPUImageFilterGroup(filters);
            case 500:
                return new RetroVintageFilter();
            case 501:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/Film/16.acv");
            case 502:
                GPUImageLevelsFilter filter45 = new GPUImageLevelsFilter();
                filter45.setRedMin(0.003921569f, TextTrackStyle.DEFAULT_FONT_SCALE, TextTrackStyle.DEFAULT_FONT_SCALE);
                filter45.setGreenMin(0.003921569f, TextTrackStyle.DEFAULT_FONT_SCALE, TextTrackStyle.DEFAULT_FONT_SCALE);
                filter45.setBlueMin(0.003921569f, TextTrackStyle.DEFAULT_FONT_SCALE, 0.99607843f);
                filters.add(filter45);
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/Film/3.acv"));
                return new GPUImageFilterGroup(filters);
            case 503:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/Film/b_vol_1.acv"));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, -8938919, GPUImageSoftLightBlendFilter.class));
                fArr2[0] = -0.105f;
                fArr2[1] = -0.145f;
                fArr2[2] = 0.205f;
                fArr3[2] = -0.065f;
                filters.add(new GPUImageColorBalanceFilter(fArr, fArr2, fArr3));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, -2020108894, GPUImageMultiplyBlendFilter.class));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/Film/b_vol_2.acv"));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, 1208420126, GPUImageExclusionBlendFilter.class));
                filters.add(new GPUImageNewVibranceFilter(0.08f));
                filters.add(new FilmBVolTemplateFilter());
                return new GPUImageFilterGroup(filters);
            case 504:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/Film/carina.acv", FilmCarinaFilter.class);
            case 505:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/Film/classic_blue.acv", FilmClassicBlueFilter.class);
            case 506:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/Film/cool_breeze.acv", FilmCoolBreezeFilter.class);
            case 507:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/Film/cooler.acv", FilmCoolerFilter.class);
            case 508:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/Film/cp_12.acv", FilmCP12Filter.class);
            case 509:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/Film/free_spirit_1.acv", FilmFreeSpiritFilter.class));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/Film/free_spirit_2.acv"));
                fArr2[0] = -0.035f;
                fArr2[2] = 0.02f;
                fArr3[0] = 0.015f;
                fArr3[2] = -0.085f;
                filters.add(new GPUImageColorBalanceFilter(fArr, fArr2, fArr3));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/Film/free_spirit_3.acv"));
                return new GPUImageFilterGroup(filters);
            case 510:
                filters.add(GPUImageFilterCreator.createBlendFilter(context, 436247807, GPUImageExclusionBlendFilter.class));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, 872403601, GPUImageDarkenBlendFilter.class));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/Film/grey_light.acv"));
                fArr2[0] = 0.035f;
                fArr2[1] = -0.05f;
                fArr2[2] = -0.08f;
                filters.add(new GPUImageColorBalanceFilter(fArr, fArr2, fArr3));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, -1027423550, GPUImageOverlayBlendFilter.class));
                return new GPUImageFilterGroup(filters);
            case 511:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/Film/lust_1.acv", 0.5f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/Film/lust_2.acv"));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/Film/lust_3.acv", 0.5f, GPUImageToneCurveLuminosityBlendFilter.class));
                return new GPUImageFilterGroup(filters);
            case 514:
                return new FilmPaprikaFilter();
            case 515:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/Film/premium_6.acv");
            case 516:
                GPUImageLevelsFilter filter46 = new GPUImageLevelsFilter();
                filter46.setRedMin(0.0f, TextTrackStyle.DEFAULT_FONT_SCALE, TextTrackStyle.DEFAULT_FONT_SCALE, 0.22745098f, 0.91764706f);
                filter46.setGreenMin(0.0f, TextTrackStyle.DEFAULT_FONT_SCALE, TextTrackStyle.DEFAULT_FONT_SCALE, 0.03137255f, 0.9019608f);
                filter46.setBlueMin(0.0f, TextTrackStyle.DEFAULT_FONT_SCALE, TextTrackStyle.DEFAULT_FONT_SCALE, 0.31764707f, 0.7490196f);
                filters.add(filter46);
                fArr2[1] = 0.025f;
                filters.add(new GPUImageColorBalanceFilter(fArr, fArr2, fArr3));
                return new GPUImageFilterGroup(filters);
            case 517:
                return new FilmPremium31Filter();
            case 519:
                filters.add(new FilmRendezvousFilter());
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/Film/rendezvous_1.acv"));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/Film/rendezvous_2.acv"));
                fArr2[0] = -0.045f;
                fArr2[1] = 0.005f;
                fArr3[0] = -0.06f;
                fArr3[1] = 0.035f;
                filters.add(new GPUImageColorBalanceFilter(fArr, fArr2, fArr3));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/Film/rendezvous_3.acv", 0.5f));
                return new GPUImageFilterGroup(filters);
            case 520:
                return GPUImageFilterCreator.createACVCurveFilter(context, "filter/Film/18.acv", Film18Filter.class);
            case 521:
                filters.add(new GPUImageSaturationFilter(0.45f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/Film/night_fate_2.acv"));
                fArr2[0] = 0.15f;
                fArr2[2] = -0.25f;
                filters.add(new GPUImageColorBalanceFilter(fArr, fArr2, fArr3));
                return new GPUImageFilterGroup(filters);
            case 522:
                fArr2[0] = -0.235f;
                filters.add(new GPUImageColorBalanceFilter(fArr, fArr2, fArr3, false));
                filters.add(new GPUImageSaturationFilter(0.55f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/Film/night_fate_6_1.acv"));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, -15855326, GPUImageDifferenceBlendFilter.class));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, -2059065023, GPUImageExclusionBlendFilter.class));
                fArr2[0] = 0.2f;
                fArr2[2] = 0.03f;
                filters.add(new GPUImageColorBalanceFilter(fArr, fArr2, fArr3));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/Film/night_fate_6_2.acv"));
                fArr2[0] = -0.105f;
                fArr2[2] = 0.11f;
                filters.add(new GPUImageColorBalanceFilter(fArr, fArr2, fArr3));
                return new GPUImageFilterGroup(filters);
            case 523:
                filters.add(new GPUImageSaturationFilter(1.3f));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, 1505957186, GPUImageLightenBlendFilter.class));
                fArr2[0] = -0.185f;
                fArr2[2] = 0.22f;
                filters.add(new GPUImageColorBalanceFilter(fArr, fArr2, fArr3));
                filters.add(new FilmNightFate3Filter());
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/Film/night_fate_3_1.acv"));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, 536857607, GPUImageNormalBlendFilter.class));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/Film/night_fate_3_2.acv"));
                return new GPUImageFilterGroup(filters);
            case 524:
                fArr2[0] = 0.045f;
                fArr2[1] = -0.085f;
                fArr2[2] = -0.115f;
                fArr[0] = -0.035f;
                fArr[1] = -0.03f;
                fArr[2] = -0.005f;
                fArr3[0] = -0.035f;
                fArr3[1] = -0.055f;
                fArr3[2] = -0.155f;
                filters.add(new GPUImageColorBalanceFilter(fArr, fArr2, fArr3));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/Film/toning_evolution.acv"));
                return new GPUImageFilterGroup(filters);
            case 525:
                fArr2[0] = -0.015f;
                fArr2[1] = 0.13f;
                fArr2[2] = -0.045f;
                fArr[0] = 0.02f;
                fArr[1] = -0.005f;
                fArr[2] = 0.05f;
                fArr3[0] = -0.115f;
                fArr3[1] = 0.07f;
                fArr3[2] = 0.045f;
                filters.add(new GPUImageColorBalanceFilter(fArr, fArr2, fArr3));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/Film/toning_hazard.acv"));
                return new GPUImageFilterGroup(filters);
            case 526:
                filters.add(GPUImageFilterCreator.createBlendFilter(context, -37888, GPUImageHueBlendFilter.class));
                GPUImageLevelsFilter filter47 = new GPUImageLevelsFilter();
                filter47.setMin(0.043137256f, TextTrackStyle.DEFAULT_FONT_SCALE, TextTrackStyle.DEFAULT_FONT_SCALE, 0.039215688f, TextTrackStyle.DEFAULT_FONT_SCALE);
                filters.add(filter47);
                filter47.setRedMin(0.039215688f, TextTrackStyle.DEFAULT_FONT_SCALE, TextTrackStyle.DEFAULT_FONT_SCALE, 0.08627451f, 0.9607843f);
                filter47.setGreenMin(0.0f, TextTrackStyle.DEFAULT_FONT_SCALE, TextTrackStyle.DEFAULT_FONT_SCALE, 0.0f, TextTrackStyle.DEFAULT_FONT_SCALE);
                filter47.setBlueMin(0.0f, TextTrackStyle.DEFAULT_FONT_SCALE, TextTrackStyle.DEFAULT_FONT_SCALE, 0.015686275f, 0.96862745f);
                filters.add(filter47);
                fArr2[0] = -0.07f;
                fArr2[1] = -0.015f;
                fArr[0] = -0.04f;
                fArr[1] = -0.015f;
                fArr3[0] = -0.04f;
                fArr3[1] = 0.005f;
                filters.add(new GPUImageColorBalanceFilter(fArr, fArr2, fArr3));
                return new GPUImageFilterGroup(filters);
            case 527:
                fArr2[0] = -0.185f;
                fArr2[1] = 0.04f;
                fArr2[2] = 0.015f;
                fArr[0] = -0.045f;
                fArr[1] = -0.07f;
                fArr[2] = 0.01f;
                fArr3[1] = 0.025f;
                fArr3[2] = -0.095f;
                filters.add(new GPUImageColorBalanceFilter(fArr, fArr2, fArr3));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/Film/toning_urban_criminal.acv"));
                return new GPUImageFilterGroup(filters);
            case 528:
                GPUImageLevelsFilter filter48 = new GPUImageLevelsFilter();
                filter48.setMin(0.05882353f, 1.27f, TextTrackStyle.DEFAULT_FONT_SCALE);
                filters.add(filter48);
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/Film/vintage_made_simple_1.acv"));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/Film/vintage_made_simple_2.acv"));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, 449676912, GPUImageNormalBlendFilter.class));
                return new GPUImageFilterGroup(filters);
            case 529:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/Film/warm_tones.acv"));
                GPUImageLevelsFilter filter49 = new GPUImageLevelsFilter();
                filter49.setRedMin(0.0f, 1.14f, TextTrackStyle.DEFAULT_FONT_SCALE);
                filter49.setGreenMin(0.0f, 1.1f, TextTrackStyle.DEFAULT_FONT_SCALE);
                filter49.setBlueMin(0.0f, 0.84f, TextTrackStyle.DEFAULT_FONT_SCALE);
                filters.add(filter49);
                filters.add(new GPUImageBrightnessFilter(0.04f));
                fArr2[0] = 0.05f;
                fArr2[1] = 0.01f;
                fArr2[2] = -0.01f;
                fArr[0] = -0.015f;
                fArr[1] = 0.01f;
                fArr[2] = -0.03f;
                fArr3[0] = 0.04f;
                fArr3[1] = -0.01f;
                fArr3[2] = -0.05f;
                filters.add(new GPUImageColorBalanceFilter(fArr, fArr2, fArr3));
                return new GPUImageFilterGroup(filters);
            case 530:
                return new FoodAdjustToneCoolShadowsFilter();
            case 531:
                GPUImageLevelsFilter filter50 = new GPUImageLevelsFilter();
                filter50.setMin(0.019607844f, 1.18f, 0.9882353f);
                return filter50;
            case 532:
                return new FoodCaliFilter();
            case 533:
                GPUImageLevelsFilter filter51 = new GPUImageLevelsFilter();
                filter51.setMin(0.11764706f, 1.28f, 0.9254902f);
                return filter51;
            case 534:
                return new GPUImageSaturationFilter(1.3f);
            case 535:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/food/first_class_1.acv"));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, 450154641, GPUImageScreenBlendFilter.class));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, 452971520, GPUImageMultiplyBlendFilter.class));
                fArr2[2] = 0.095f;
                filters.add(new GPUImageColorBalanceFilter(fArr, fArr2, fArr3));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/food/first_class_2.acv"));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, 1110847565, GPUImageExclusionBlendFilter.class));
                filters.add(new GPUImageBrightnessFilter(0.05f));
                filters.add(new GPUImageContrastFilter(1.08f));
                return new GPUImageFilterGroup(filters);
            case 536:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/food/gemma_1.acv"));
                filters.add(new GPUImageSaturationFilter(1.22f));
                fArr2[0] = -0.085f;
                fArr2[1] = -0.01f;
                fArr2[2] = 0.08f;
                fArr[0] = -0.12f;
                fArr[1] = 0.045f;
                fArr[2] = 0.105f;
                fArr3[2] = -0.06f;
                filters.add(new GPUImageColorBalanceFilter(fArr, fArr2, fArr3));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, -3170897, GPUImageSoftLightBlendFilter.class));
                filters.add(new GPUImageExposureFilter(-0.06f));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/food/gemma_2.acv"));
                return new GPUImageFilterGroup(filters);
            case 537:
                return new FoodIceFilter();
            case 538:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/food/luciana_1.acv"));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, 444612473, GPUImageDifferenceBlendFilter.class));
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/food/luciana_2.acv"));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, 729739007, GPUImageSoftLightBlendFilter.class));
                filters.add(GPUImageFilterCreator.createBlendFilter(context, 906761779, GPUImageScreenBlendFilter.class));
                return new GPUImageFilterGroup(filters);
            case 539:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/food/orton.acv"));
                filters.add(new GPUImageBrightnessFilter(0.12f));
                GPUImageLevelsFilter filter52 = new GPUImageLevelsFilter();
                filter52.setRedMin(0.078431375f, 0.91f, 0.94509804f);
                filter52.setBlueMin(0.0f, 0.94f, TextTrackStyle.DEFAULT_FONT_SCALE);
                filters.add(filter52);
                return new GPUImageFilterGroup(filters);
            case 540:
                filters.add(new GPUImageBrightnessFilter(0.04f));
                filters.add(new GPUImageContrastFilter(1.41f));
                filters.add(new GPUImageSaturationFilter(1.3f));
                return new GPUImageFilterGroup(filters);
            case 541:
                filters.add(GPUImageFilterCreator.createACVCurveFilter(context, "filter/food/restore_color.acv"));
                GPUImageLevelsFilter filter53 = new GPUImageLevelsFilter();
                filter53.setMin(0.0f, 1.1f, TextTrackStyle.DEFAULT_FONT_SCALE);
                filters.add(filter53);
                filters.add(new GPUImageSaturationFilter(1.3f));
                return new GPUImageFilterGroup(filters);
            default:
                throw new IllegalStateException("No filter of that type!");
        }
        return GPUImageFilterCreator.createDATCurveFilter(context, "filter/d1.dat");
    }
}
