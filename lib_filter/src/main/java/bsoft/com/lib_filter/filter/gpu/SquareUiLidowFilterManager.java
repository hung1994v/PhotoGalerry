package bsoft.com.lib_filter.filter.gpu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import bsoft.com.lib_filter.R;
import bsoft.com.lib_filter.filter.gpu.util.BitmapUtil;

public class SquareUiLidowFilterManager implements WBManager {
    private static final int ALL = 0;
    public static final int BWFILTER = 7;
    public static final int CLASSICFILTER = 2;
    public static final int FADEFILTER = 6;
    public static final int FILMFILTER = 5;
    public static final int FOODFILTER = 17;
    public static final int HALOFILTER = 9;
    public static final int LIKE = 0;
    public static final int LOMOFILTER = 4;
    public static final int RETROFILTER = 10;
    public static final int SEASONFILTER = 1;
    public static final int SWEETFILTER = 3;
    public static final int VINTAGEFILTER = 8;
    private String isFilterLike;
    private Context mContext;
    private int mMode;
    private List<GPUFilterRes> resList;
    private String strValue;

    public SquareUiLidowFilterManager(Context context, int mode, String isFilterLike) {
        this.resList = new ArrayList();
        this.isFilterLike = isFilterLike;
        this.mContext = context;
        this.mMode = mode;
        if (mode == BWFILTER) {
            this.resList.clear();
            this.resList.add(initAssetItem("B1", "filter/image/mode7.png", GPUFilterType.GINGHAM));
            this.resList.add(initAssetItem("B2", "filter/image/mode7.png", GPUFilterType.CHARMES));
            this.resList.add(initAssetItem("B3", "filter/image/mode7.png", GPUFilterType.WILLOW));
            this.resList.add(initAssetItem("B4", "filter/image/mode7.png", GPUFilterType.ASHBY));
            this.resList.add(initAssetItem("B5", "filter/image/mode7.png", GPUFilterType.BWRetro));
            this.resList.add(initAssetItem("B6", "filter/image/mode7.png", GPUFilterType.CLARENDON));
            this.resList.add(initAssetItem("B7", "filter/image/mode7.png", GPUFilterType.INKWELL));
            this.resList.add(initAssetItem("B8", "filter/image/mode7.png", GPUFilterType.DOGPATCH));
        }
        if (mode == CLASSICFILTER) {
            this.resList.clear();
            this.resList.add(initAssetItem("C1", "filter/image/mode5.png", GPUFilterType.AMARO));
            this.resList.add(initAssetItem("C2", "filter/image/mode5.png", GPUFilterType.MAYFAIR));
            this.resList.add(initAssetItem("C3", "filter/image/mode5.png", GPUFilterType.RISE));
            this.resList.add(initAssetItem("C4", "filter/image/mode5.png", GPUFilterType.HUDSON));
            this.resList.add(initAssetItem("C5", "filter/image/mode5.png", GPUFilterType.VALENCIA));
            this.resList.add(initAssetItem("C6", "filter/image/mode5.png", GPUFilterType.SIERRA));
            this.resList.add(initAssetItem("C7", "filter/image/mode5.png", GPUFilterType.TOASTER));
            this.resList.add(initAssetItem("C8", "filter/image/mode5.png", GPUFilterType.BRANNAN));
            this.resList.add(initAssetItem("C9", "filter/image/mode5.png", GPUFilterType.WALDEN));
            this.resList.add(initAssetItem("C10", "filter/image/mode5.png", GPUFilterType.HEFE));
            this.resList.add(initAssetItem("C11", "filter/image/mode5.png", GPUFilterType.NASHVILLE));
            this.resList.add(initAssetItem("C12", "filter/image/mode5.png", GPUFilterType.KELVIN));
        }
        if (mode == FADEFILTER) {
            this.resList.clear();
            this.resList.add(initAssetItem("F1", "filter/image/mode6.png", GPUFilterType.FADE_DARK_DESATURATE));
            this.resList.add(initAssetItem("F2", "filter/image/mode6.png", GPUFilterType.FADE_DIFFUSED_MATTE));
            this.resList.add(initAssetItem("F3", "filter/image/mode6.png", GPUFilterType.FADE_EVERYDAY));
            this.resList.add(initAssetItem("F4", "filter/image/mode6.png", GPUFilterType.FADE_LIME));
            this.resList.add(initAssetItem("F5", "filter/image/mode6.png", GPUFilterType.FADE_LUCID));
            this.resList.add(initAssetItem("F6", "filter/image/mode6.png", GPUFilterType.FADE_RETRO));
            this.resList.add(initAssetItem("F7", "filter/image/mode6.png", GPUFilterType.FADE_WHITE_WASH));
            this.resList.add(initAssetItem("F8", "filter/image/mode6.png", GPUFilterType.FADE_BEAUTIFULLY));
            this.resList.add(initAssetItem("F9", "filter/image/mode6.png", GPUFilterType.FADE_COOL_HAZE));
        }
        if (mode == FILMFILTER) {
            this.resList.clear();
            this.resList.add(initAssetItem("M1", "filter/image/mode2.png", GPUFilterType.FILM_16));
            this.resList.add(initAssetItem("M2", "filter/image/mode2.png", GPUFilterType.FILM_3));
            this.resList.add(initAssetItem("M3", "filter/image/mode2.png", GPUFilterType.FILM_B_VOL));
            this.resList.add(initAssetItem("M4", "filter/image/mode2.png", GPUFilterType.FILM_CARINA));
            this.resList.add(initAssetItem("M5", "filter/image/mode2.png", GPUFilterType.FILM_CLASSIC_BLUE));
            this.resList.add(initAssetItem("M6", "filter/image/mode2.png", GPUFilterType.FILM_COOL_BREEZE));
            this.resList.add(initAssetItem("M7", "filter/image/mode2.png", GPUFilterType.FILM_COOLER));
            this.resList.add(initAssetItem("M8", "filter/image/mode2.png", GPUFilterType.FILM_CP_12));
            this.resList.add(initAssetItem("M9", "filter/image/mode2.png", GPUFilterType.FILM_18));
            this.resList.add(initAssetItem("M10", "filter/image/mode2.png", GPUFilterType.FILM_GREY_LIGHT));
            this.resList.add(initAssetItem("M11", "filter/image/mode2.png", GPUFilterType.FILM_LUST));
            this.resList.add(initAssetItem("M12", "filter/image/mode2.png", GPUFilterType.FILM_PAPRIKA));
        }
        if (mode == FOODFILTER) {
            this.resList.clear();
            this.resList.add(initAssetItem("F1", "filter/food_1.jpg", GPUFilterType.FOOD_ADJUST_TONE_COOL_SHADOWS));
            this.resList.add(initAssetItem("F2", "filter/food_2.jpg", GPUFilterType.FOOD_BRIGHTEN_MIDTONES));
            this.resList.add(initAssetItem("F3", "filter/food_3.jpg", GPUFilterType.FOOD_CALI));
            this.resList.add(initAssetItem("F4", "filter/food_4.jpg", GPUFilterType.FOOD_CONTRAST_HIGH_KEY));
            this.resList.add(initAssetItem("F5", "filter/food_5.jpg", GPUFilterType.FOOD_DETAILS_PAINT_IN_SATURATION));
            this.resList.add(initAssetItem("F6", "filter/food_6.jpg", GPUFilterType.FOOD_FIRST_CLASS));
            this.resList.add(initAssetItem("F7", "filter/food_7.jpg", GPUFilterType.FOOD_GEMMA));
            this.resList.add(initAssetItem("F8", "filter/food_8.jpg", GPUFilterType.FOOD_LUCIANA));
            this.resList.add(initAssetItem("F9", "filter/food_9.jpg", GPUFilterType.FOOD_ORTON));
            this.resList.add(initAssetItem("F10", "filter/food_10.jpg", GPUFilterType.FOOD_PRETTY_PEEPERS));
            this.resList.add(initAssetItem("F11", "filter/food_11.jpg", GPUFilterType.FOOD_RESTORE_COLOR));
        }
        if (mode == HALOFILTER) {
            this.resList.clear();
            this.resList.add(initAssetItem("H1", "filter/image/mode9.png", GPUFilterType.HALO4));
            this.resList.add(initAssetItem("H2", "filter/image/mode9.png", GPUFilterType.HALO3));
            this.resList.add(initAssetItem("H3", "filter/image/mode9.png", GPUFilterType.HALO2));
            this.resList.add(initAssetItem("H4", "filter/image/mode9.png", GPUFilterType.HALO7));
            this.resList.add(initAssetItem("H5", "filter/image/mode9.png", GPUFilterType.HALO6));
            this.resList.add(initAssetItem("H6", "filter/image/mode9.png", GPUFilterType.HALO5));
            this.resList.add(initAssetItem("H7", "filter/image/mode9.png", GPUFilterType.HALO1));
        }
        if (mode == LOMOFILTER) {
            this.resList.clear();
            this.resList.add(initAssetItem("L1", "filter/image/mode4.png", GPUFilterType.LOMO1, 40));
            this.resList.add(initAssetItem("L2", "filter/image/mode4.png", GPUFilterType.LOMO2, 40));
            this.resList.add(initAssetItem("L3", "filter/image/mode4.png", GPUFilterType.LOMO6, 80));
            this.resList.add(initAssetItem("L4", "filter/image/mode4.png", GPUFilterType.LOMO8, 45));
            this.resList.add(initAssetItem("L5", "filter/image/mode4.png", GPUFilterType.LOMO3, 60));
            this.resList.add(initAssetItem("L6", "filter/image/mode4.png", GPUFilterType.LOMO12, 50));
            this.resList.add(initAssetItem("L7", "filter/image/mode4.png", GPUFilterType.LOMO16, 15));
            this.resList.add(initAssetItem("L8", "filter/image/mode4.png", GPUFilterType.LOMO9, 30));
            this.resList.add(initAssetItem("L9", "filter/image/mode4.png", GPUFilterType.LOMO11, 80));
            this.resList.add(initAssetItem("L10", "filter/image/mode4.png", GPUFilterType.LOMO5, 80));
            this.resList.add(initAssetItem("L11", "filter/image/mode4.png", GPUFilterType.LOMO27, 90));
            this.resList.add(initAssetItem("L12", "filter/image/mode4.png", GPUFilterType.LOMO15, 30));
        }
        if (mode == RETROFILTER) {
            this.resList.clear();
            this.resList.add(initAssetItem("R1", "filter/retro/r1.jpg", GPUFilterType.RETRO_PS));
            this.resList.add(initAssetItem("R2", "filter/retro/r2.jpg", GPUFilterType.RETRO_A_VOL_1));
            this.resList.add(initAssetItem("R3", "filter/retro/r3.jpg", GPUFilterType.RETRO_A_VOL_2));
            this.resList.add(initAssetItem("R4", "filter/retro/r5.jpg", GPUFilterType.RETRO_A_VOL_4));
            this.resList.add(initAssetItem("R5", "filter/retro/r6.jpg", GPUFilterType.RETRO_A_VOL_12));
            this.resList.add(initAssetItem("R6", "filter/retro/r7.jpg", GPUFilterType.RETRO_A_VOL_20));
            this.resList.add(initAssetItem("R7", "filter/retro/r8.jpg", GPUFilterType.RETRO_A_VOL_22));
            this.resList.add(initAssetItem("R8", "filter/retro/r9.jpg", GPUFilterType.RETRO_AMBITIOUS));
            this.resList.add(initAssetItem("R9", "filter/retro/r10.jpg", GPUFilterType.RETRO_BRISK));
            this.resList.add(initAssetItem("R10", "filter/retro/r11.jpg", GPUFilterType.RETRO_C_VOL_2));
            this.resList.add(initAssetItem("R11", "filter/retro/r12.jpg", GPUFilterType.RETRO_C_VOL_8));
            this.resList.add(initAssetItem("R12", "filter/retro/r13.jpg", GPUFilterType.RETRO_C_VOL_13));
            this.resList.add(initAssetItem("R13", "filter/retro/r14.jpg", GPUFilterType.RETRO_CHESTNUT_BROWN));
            this.resList.add(initAssetItem("R14", "filter/retro/r15.jpg", GPUFilterType.RETRO_CP_24));
            this.resList.add(initAssetItem("R15", "filter/retro/r16.jpg", GPUFilterType.RETRO_DELICATE_BROWN));
            this.resList.add(initAssetItem("R16", "filter/retro/r18.jpg", GPUFilterType.RETRO_FLASH_BACK));
            this.resList.add(initAssetItem("R17", "filter/retro/r22.jpg", GPUFilterType.RETRO_PREMIUM));
            this.resList.add(initAssetItem("R18", "filter/retro/r23.jpg", GPUFilterType.RETRO_3));
            this.resList.add(initAssetItem("R19", "filter/retro/r24.jpg", GPUFilterType.RETRO_17));
            this.resList.add(initAssetItem("R20", "filter/retro/r26.jpg", GPUFilterType.RETRO_NIGHT_FATE));
            this.resList.add(initAssetItem("R21", "filter/retro/r27.jpg", GPUFilterType.RETRO_SPIRITED));
            this.resList.add(initAssetItem("R22", "filter/retro/r27.jpg", GPUFilterType.RETRO_VINTAGE));
        }
        if (mode == SEASONFILTER) {
            this.resList.clear();
            this.resList.add(initAssetItem("S1", "filter/image/mode3.png", GPUFilterType.SEASON_SPRING_BLOSSOM));
            this.resList.add(initAssetItem("S2", "filter/image/mode3.png", GPUFilterType.SEASON_AUTUMN_DAWOOD_HAMADA));
            this.resList.add(initAssetItem("S3", "filter/image/mode3.png", GPUFilterType.SEASON_WINTER_ICED));
            this.resList.add(initAssetItem("S4", "filter/image/mode3.png", GPUFilterType.SEASON_AUTUMN_GENTLE));
            this.resList.add(initAssetItem("S5", "filter/image/mode3.png", GPUFilterType.SEASON_SPRING_GLORIOUS_BABY));
            this.resList.add(initAssetItem("S6", "filter/image/mode3.png", GPUFilterType.SEASON_SUMMER_DAY));
            this.resList.add(initAssetItem("S7", "filter/image/mode3.png", GPUFilterType.SEASON_SUMMER_CLASSIC));
            this.resList.add(initAssetItem("S8", "filter/image/mode3.png", GPUFilterType.SEASON_SUMMER_INDIAN));
            this.resList.add(initAssetItem("S9", "filter/image/mode3.png", GPUFilterType.SEASON_AUTUMN_PREMIUM));
            this.resList.add(initAssetItem("S10", "filter/image/mode3.png", GPUFilterType.SEASON_SPRING_LIGHT));
            this.resList.add(initAssetItem("S11", "filter/image/mode3.png", GPUFilterType.SEASON_WINTER_SNAPPY_BABY));
            this.resList.add(initAssetItem("S12", "filter/image/mode3.png", GPUFilterType.SEASON_WINTER_SOFT_BROWN));
        }
        if (mode == SWEETFILTER) {
            this.resList.clear();
            this.resList.add(initAssetItem("S1", "filter/image/mode1.png", GPUFilterType.SWEET_PREMIUM));
            this.resList.add(initAssetItem("S2", "filter/image/mode1.png", GPUFilterType.SWEET_CERULEAN_BLUE));
            this.resList.add(initAssetItem("S3", "filter/image/mode1.png", GPUFilterType.SWEET_DEEP_PURPLE));
            this.resList.add(initAssetItem("S4", "filter/image/mode1.png", GPUFilterType.SWEET_HAZY_TEAL));
            this.resList.add(initAssetItem("S5", "filter/image/mode1.png", GPUFilterType.SWEET_LAVENDER_HAZE));
            this.resList.add(initAssetItem("S6", "filter/image/mode1.png", GPUFilterType.SWEET_MAGENTA));
            this.resList.add(initAssetItem("S7", "filter/image/mode1.png", GPUFilterType.SWEET_MORNING_GLOW));
            this.resList.add(initAssetItem("S8", "filter/image/mode1.png", GPUFilterType.SWEET_PRIMUEM));
            this.resList.add(initAssetItem("S9", "filter/image/mode1.png", GPUFilterType.SWEET_ROMANCE));
            this.resList.add(initAssetItem("S10", "filter/image/mode1.png", GPUFilterType.SWEET_RUSTY_TINT));
            this.resList.add(initAssetItem("S11", "filter/image/mode1.png", GPUFilterType.SWEET_SO_COOL));
            this.resList.add(initAssetItem("S12", "filter/image/mode1.png", GPUFilterType.SWEET_SWEET));
        }
        if (mode == VINTAGEFILTER) {
            this.resList.clear();
            this.resList.add(initAssetItem("V1", "filter/image/mode8.png", GPUFilterType.Y1970));
            this.resList.add(initAssetItem("V2", "filter/image/mode8.png", GPUFilterType.Y1975));
            this.resList.add(initAssetItem("V3", "filter/image/mode8.png", GPUFilterType.Y1977));
            this.resList.add(initAssetItem("V4", "filter/image/mode8.png", GPUFilterType.LOFI));
            this.resList.add(initAssetItem("V5", "filter/image/mode8.png", GPUFilterType.XPRO2));
            this.resList.add(initAssetItem("V6", "filter/image/mode8.png", GPUFilterType.EARLYBIRD));
            this.resList.add(initAssetItem("V7", "filter/image/mode8.png", GPUFilterType.SUTRO));
        }
        if (mode == 0) {
            this.resList.clear();
            this.resList.add(initAssetItem("ORI", "filter/image/mode0.png", GPUFilterType.NOFILTER));
            if (isFilterLike != null && !"".equals(isFilterLike)) {
                String[] arr = isFilterLike.split(",");
                for (int i = LIKE; i < arr.length / SWEETFILTER; i += SEASONFILTER) {
                    this.resList.add(initAssetItem(arr[i * SWEETFILTER], arr[(i * SWEETFILTER) + CLASSICFILTER], GPUFilterType.valueOf(arr[(i * SWEETFILTER) + SEASONFILTER])));
                }
            }
        }
    }

    protected GPUFilterRes initAssetItem(String name, String iconName, GPUFilterType filterType) {
        Bitmap s1 = BitmapUtil.getImageFromAssetsFile(this.mContext.getResources(), iconName);
        GPUFilterRes item = new GPUFilterRes();
        item.setContext(this.mContext);
        item.setName(name);
        item.setFilterType(filterType);
        item.setIconFileName(iconName);
        item.setIconType(WBRes.LocationType.FILTERED);
        item.setImageType(WBRes.LocationType.ASSERT);
        item.setShowText(name);
        item.setIsShowText(true);
        item.setSRC(s1);
        if (!(this.isFilterLike == null || "".equals(this.isFilterLike))) {
            if (this.isFilterLike.contains(new StringBuilder(String.valueOf(name)).append(",").append(filterType).append(",").append(iconName).toString())) {
                item.setIsShowLikeIcon(true);
            } else {
                item.setIsShowLikeIcon(false);
            }
        }
        return item;
    }

    protected AdjustableFilterRes initAssetItem(String name, String iconName, GPUFilterType filterType, int mix) {
        Bitmap s1 = BitmapUtil.getImageFromAssetsFile(this.mContext.getResources(), iconName);
        AdjustableFilterRes item = new AdjustableFilterRes();
        item.setContext(this.mContext);
        item.setName(name);
        item.setFilterType(filterType);
        item.setIconFileName(iconName);
        item.setIconType(WBRes.LocationType.FILTERED);
        item.setImageType(WBRes.LocationType.ASSERT);
        item.setShowText(name);
        item.setIsShowText(true);
        item.setTextColor(Color.WHITE);
        item.setMix(mix);
        item.setSRC(s1);
        if (!(this.isFilterLike == null || "".equals(this.isFilterLike))) {
            if (this.isFilterLike.contains(new StringBuilder(String.valueOf(name)).append(",").append(filterType).append(",").append(iconName).toString())) {
                item.setIsShowLikeIcon(true);
            } else {
                item.setIsShowLikeIcon(false);
            }
        }
        return item;
    }

    public String getDesStringValue(String name) {
        this.strValue = name;
        return this.strValue;
    }

    public int getCount() {
        return this.resList.size();
    }

    public WBRes getRes(int pos) {
        Log.d("ttttttttt ", " " + resList.get(pos));
        return resList.get(pos);
    }

    public WBRes getRes(String name) {
        for (int i = LIKE; i < this.resList.size(); i += SEASONFILTER) {
            WBRes r = (WBRes) this.resList.get(i);
            if (r.getName().compareTo(name) == 0) {
                return r;
            }
        }
        return null;
    }

    public boolean isRes(String name) {
        return false;
    }
}