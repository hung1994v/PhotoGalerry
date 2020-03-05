package com.photo.splashfunphoto.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Computer on 5/19/2017.
 */

public class ConstList {
    public static final String KEY_LAYOUT_PREFIX = "layout_";
    public static final String KEY_BG_PREFIX = "bg_";
    public static final String KEY_BORDER_PREFIX = "border_";
    public static final String KEY_STICKER = "sticker";
    public static final String KEY_PATTERN = "pattern";
    public static final String KEY_SQUARE_BORDER = "square_frame";

    public static Map<String, List<String>> listLayout = null;

    static {
        listLayout = new HashMap<>();

    }

    public static void init(Map<String, List<String>> listLayout) {
        ConstList.listLayout.putAll(listLayout);
    }

}


