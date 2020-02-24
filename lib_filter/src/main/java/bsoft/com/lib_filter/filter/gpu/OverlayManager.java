package bsoft.com.lib_filter.filter.gpu;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class OverlayManager implements WBManager {
    private Context mContext;
    private int mMode;
    List<WBRes> resList;

    public OverlayManager(Context context, int mode) {
        this.resList = new ArrayList();
        this.mContext = context;
        this.mMode = mode;
        initResource();
    }

    public void initResource() {
        if (this.mMode == 1) {
            this.resList.add(initRes(this.mContext, "blur_13", "overlay/blur/icon/blur_013.jpg", "overlay/blur/blur_013.jpg", GPUFilterType.BLEND_SOFT_LIGHT));
            this.resList.add(initRes(this.mContext, "blur_14", "overlay/blur/icon/blur_014.jpg", "overlay/blur/blur_014.jpg", GPUFilterType.BLEND_SOFT_LIGHT));
            this.resList.add(initRes(this.mContext, "blur_15", "overlay/blur/icon/blur_015.jpg", "overlay/blur/blur_015.jpg", GPUFilterType.BLEND_HARD_LIGHT));
            this.resList.add(initRes(this.mContext, "blur_1", "overlay/blur/icon/blur_001.jpg", "overlay/blur/blur_001.jpg", GPUFilterType.BLEND_HARD_LIGHT));
            this.resList.add(initRes(this.mContext, "blur_2", "overlay/blur/icon/blur_002.jpg", "overlay/blur/blur_002.jpg", GPUFilterType.BLEND_SOFT_LIGHT));
            this.resList.add(initRes(this.mContext, "blur_3", "overlay/blur/icon/blur_003.jpg", "overlay/blur/blur_003.jpg", GPUFilterType.BLEND_HARD_LIGHT));
            this.resList.add(initRes(this.mContext, "blur_4", "overlay/blur/icon/blur_004.jpg", "overlay/blur/blur_004.jpg", GPUFilterType.BLEND_MULTIPLY));
            this.resList.add(initRes(this.mContext, "blur_5", "overlay/blur/icon/blur_005.jpg", "overlay/blur/blur_005.jpg", GPUFilterType.BLEND_MULTIPLY));
            this.resList.add(initRes(this.mContext, "blur_6", "overlay/blur/icon/blur_006.jpg", "overlay/blur/blur_006.jpg", GPUFilterType.BLEND_MULTIPLY));
            this.resList.add(initRes(this.mContext, "blur_7", "overlay/blur/icon/blur_007.jpg", "overlay/blur/blur_007.jpg", GPUFilterType.BLEND_HARD_LIGHT));
            this.resList.add(initRes(this.mContext, "blur_8", "overlay/blur/icon/blur_008.jpg", "overlay/blur/blur_008.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "blur_9", "overlay/blur/icon/blur_009.jpg", "overlay/blur/blur_009.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "blur_10", "overlay/blur/icon/blur_010.jpg", "overlay/blur/blur_010.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "blur_11", "overlay/blur/icon/blur_011.jpg", "overlay/blur/blur_011.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "blur_12", "overlay/blur/icon/blur_012.jpg", "overlay/blur/blur_012.jpg", GPUFilterType.BLEND_SCREEN));
        }
        if (this.mMode == 2) {
            this.resList.add(initRes(this.mContext, "flare_1", "overlay/flare/icon/flare_001.jpg", "overlay/flare/flare_001.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "flare_2", "overlay/flare/icon/flare_002.jpg", "overlay/flare/flare_002.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "flare_3", "overlay/flare/icon/flare_003.jpg", "overlay/flare/flare_003.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "flare_4", "overlay/flare/icon/flare_004.jpg", "overlay/flare/flare_004.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "flare_5", "overlay/flare/icon/flare_005.jpg", "overlay/flare/flare_005.jpg", GPUFilterType.BLEND_SCREEN));
        }
        if (this.mMode == 3) {
            this.resList.add(initRes(this.mContext, "leak_1", "overlay/leak/icon/leak_001.jpg", "overlay/leak/leak_001.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "leak_2", "overlay/leak/icon/leak_002.jpg", "overlay/leak/leak_002.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "leak_3", "overlay/leak/icon/leak_003.jpg", "overlay/leak/leak_003.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "leak_4", "overlay/leak/icon/leak_004.jpg", "overlay/leak/leak_004.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "leak_5", "overlay/leak/icon/leak_005.jpg", "overlay/leak/leak_005.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "leak_6", "overlay/leak/icon/leak_006.jpg", "overlay/leak/leak_006.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "leak_7", "overlay/leak/icon/leak_007.jpg", "overlay/leak/leak_007.jpg", GPUFilterType.BLEND_SCREEN));
        }
        if (this.mMode == 4) {
            this.resList.add(initRes(this.mContext, "sky_1", "overlay/sky/icon/sky_001.jpg", "overlay/sky/sky_001.jpg", GPUFilterType.BLEND_MULTIPLY));
            this.resList.add(initRes(this.mContext, "sky_2", "overlay/sky/icon/sky_002.jpg", "overlay/sky/sky_002.jpg", GPUFilterType.BLEND_MULTIPLY));
            this.resList.add(initRes(this.mContext, "sky_3", "overlay/sky/icon/sky_003.jpg", "overlay/sky/sky_003.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "sky_4", "overlay/sky/icon/sky_004.jpg", "overlay/sky/sky_004.jpg", GPUFilterType.BLEND_MULTIPLY));
            this.resList.add(initRes(this.mContext, "sky_5", "overlay/sky/icon/sky_005.jpg", "overlay/sky/sky_005.jpg", GPUFilterType.BLEND_MULTIPLY));
            this.resList.add(initRes(this.mContext, "sky_6", "overlay/sky/icon/sky_006.jpg", "overlay/sky/sky_006.jpg", GPUFilterType.BLEND_MULTIPLY));
        }
        if (this.mMode == 5) {
            this.resList.add(initRes(this.mContext, "texture_1", "overlay/texture/vintage_1.jpg", "overlay/texture/vintage_1.jpg", GPUFilterType.BLEND_MULTIPLY));
            this.resList.add(initRes(this.mContext, "texture_2", "overlay/texture/vintage_2.jpg", "overlay/texture/vintage_2.jpg", GPUFilterType.BLEND_MULTIPLY));
            this.resList.add(initRes(this.mContext, "texture_3", "overlay/texture/vintage_3.jpg", "overlay/texture/vintage_3.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "texture_4", "overlay/texture/vintage_4.jpg", "overlay/texture/vintage_4.jpg", GPUFilterType.BLEND_MULTIPLY));
            this.resList.add(initRes(this.mContext, "texture_5", "overlay/texture/vintage_5.jpg", "overlay/texture/vintage_5.jpg", GPUFilterType.BLEND_MULTIPLY));
            this.resList.add(initRes(this.mContext, "texture_6", "overlay/texture/vintage_6.jpg", "overlay/texture/vintage_6.jpg", GPUFilterType.BLEND_HARD_LIGHT));
            this.resList.add(initRes(this.mContext, "texture_7", "overlay/texture/vintage_7.jpg", "overlay/texture/vintage_7.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "texture_8", "overlay/texture/vintage_8.jpg", "overlay/texture/vintage_8.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "texture_9", "overlay/texture/vintage_9.jpg", "overlay/texture/vintage_9.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "texture_10", "overlay/texture/vintage_10.jpg", "overlay/texture/vintage_10.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "texture_11", "overlay/texture/vintage_11.jpg", "overlay/texture/vintage_11.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "texture_12", "overlay/texture/vintage_12.jpg", "overlay/texture/vintage_12.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "texture_13", "overlay/texture/vintage_13.jpg", "overlay/texture/vintage_13.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "texture_14", "overlay/texture/vintage_14.jpg", "overlay/texture/vintage_14.jpg", GPUFilterType.BLEND_SCREEN));
        }
        if (this.mMode == 6) {
            this.resList.add(initRes(this.mContext, "noise_1", "overlay/noise/overlay_1.jpg", "overlay/noise/overlay_1.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "noise_2", "overlay/noise/overlay_2.jpg", "overlay/noise/overlay_2.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "noise_3", "overlay/noise/overlay_3.jpg", "overlay/noise/overlay_3.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "noise_4", "overlay/noise/overlay_4.jpg", "overlay/noise/overlay_4.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "noise_5", "overlay/noise/overlay_5.jpg", "overlay/noise/overlay_5.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "noise_6", "overlay/noise/overlay_6.jpg", "overlay/noise/overlay_6.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "noise_7", "overlay/noise/overlay_7.jpg", "overlay/noise/overlay_7.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "noise_8", "overlay/noise/overlay_8.jpg", "overlay/noise/overlay_8.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "noise_9", "overlay/noise/overlay_9.jpg", "overlay/noise/overlay_9.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "noise_10", "overlay/noise/overlay_10.jpg", "overlay/noise/overlay_10.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "noise_11", "overlay/noise/overlay_11.jpg", "overlay/noise/overlay_11.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "noise_12", "overlay/noise/overlay_12.jpg", "overlay/noise/overlay_12.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "noise_13", "overlay/noise/overlay_13.jpg", "overlay/noise/overlay_13.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "noise_14", "overlay/noise/overlay_14.jpg", "overlay/noise/overlay_14.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "noise_15", "overlay/noise/overlay_15.jpg", "overlay/noise/overlay_15.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "noise_16", "overlay/noise/overlay_16.jpg", "overlay/noise/overlay_16.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "noise_17", "overlay/noise/overlay_17.jpg", "overlay/noise/overlay_17.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "noise_18", "overlay/noise/overlay_18.jpg", "overlay/noise/overlay_18.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "noise_19", "overlay/noise/overlay_19.jpg", "overlay/noise/overlay_19.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "noise_20", "overlay/noise/overlay_20.jpg", "overlay/noise/overlay_20.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "noise_21", "overlay/noise/overlay_21.jpg", "overlay/noise/overlay_21.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "noise_22", "overlay/noise/overlay_22.jpg", "overlay/noise/overlay_22.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "noise_23", "overlay/noise/overlay_23.jpg", "overlay/noise/overlay_23.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "noise_24", "overlay/noise/overlay_24.jpg", "overlay/noise/overlay_24.jpg", GPUFilterType.BLEND_SCREEN));
            this.resList.add(initRes(this.mContext, "noise_25", "overlay/noise/overlay_25.jpg", "overlay/noise/overlay_25.jpg", GPUFilterType.BLEND_SCREEN));
        }
    }

    public int getCount() {
        return this.resList.size();
    }

    public WBRes getRes(int pos) {
        return (WBRes) this.resList.get(pos);
    }

    public WBRes getRes(String name) {
        for (int i = 0; i < this.resList.size(); i++) {
            if (name.compareTo(((WBRes) this.resList.get(i)).getName()) == 0) {
                return (WBRes) this.resList.get(i);
            }
        }
        return null;
    }

    public boolean isRes(String name) {
        for (int i = 0; i < this.resList.size(); i++) {
            if (name.compareTo(((WBRes) this.resList.get(i)).getName()) == 0) {
                return true;
            }
        }
        return false;
    }

    protected GPUFilterRes initRes(Context context, String name, String icon, String image, GPUFilterType filterType) {
        GPUFilterRes res = new GPUFilterRes();
        res.setContext(context);
        res.setName(name);
        res.setIconFileName(icon);
        res.setIconType(WBRes.LocationType.ASSERT);
        res.setImageFileName(image);
        res.setImageType(WBRes.LocationType.ASSERT);
        res.setFilterType(filterType);
        return res;
    }
}