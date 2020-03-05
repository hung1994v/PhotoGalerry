package com.photo.splashfunphoto.utils;

import com.photo.splashfunphoto.model.RatioCrop;

import java.util.ArrayList;
import java.util.List;

public class Statics {

    public static final int RATIO_1_2 = 0;
    public static final int RATIO_2_1 = 10;
    public static final int RATIO_9_16 = 1;
    public static final int RATIO_2_3 = 2;
    public static final int RATIO_3_4 = 3;
    public static final int RATIO_4_5 = 4;
    public static final int RATIO_1_1 = 5;
    public static final int RATIO_5_4 = 6;
    public static final int RATIO_4_3 = 7;
    public static final int RATIO_3_2 = 8;
    public static final int RATIO_16_9 = 9;
    public static final int LIMIT_TEXT_CHARACTERS = 150;
    public static final int MOVE_UP = 1;
    public static final int MOVE_DOWN = 2;
    public static final int MOVE_LEFT = 3;
    public static final int MOVE_RIGHT = 4;

    public static final int COLLAGE_TEXT_TYPE = 0x13;
    public static final int COLLAGE_MAGAZINE_TYPE = 0x12;
    public static final int STATE_NEW_TEXT = 0x2;
    public static final int STATE_UPDATE_TEXT = 0x1;
    public static final int MAX_PADDING_SIZE = 50;

    public static final float ROTATE_STEP = 2;
    public static final int TRANSLATE_STEP = 5;
    public static final float STEP_SCALE_IN = 1.05f;
    public static final float STEP_SCALE_OUT = 0.95f;
    public static final String DES = "0xblender";
    public static final String SAVE_IMG_FILE_PATH = "save_img_file_path";
    public static int OPACITY = 225;

    public static final int OPACITY1 = 0;
    public static final int ROTATE = 1;
    public static final int IROTATE = 2;
    public static final int UP = 3;
    public static final int DOWN = 4;
    public static final int LEFT = 5;
    public static final int RIGHT = 6;
    public static final int ZOON_IN = 7;
    public static final int ZOOM_OUT = 8;

    public static ArrayList<String> mListOverlayText = new ArrayList<>();
    public static ArrayList<String> mListOverlayNoise = new ArrayList<>();


    public static ArrayList<String> mListPattern1 = new ArrayList<>();
    public static ArrayList<String> mListPattern2 = new ArrayList<>();
    public static ArrayList<String> mListIconPattern = new ArrayList<>();
    public static ArrayList<String> mListIconFrame = new ArrayList<>();


    public static final int SIZE_IMG_SPLASH = 15;
    public static final int SIZE_IMG_BLUR = 10;
    public static final int SIZE_IMG_MIRROR1 = 15;
    public static final int SIZE_IMG_MIRROR3D = 24;
    public static final int SIZE_IMG_MIRRORRDIO = 11;
    public static final int SIZE_IMG_FLARES = 6;
    public static final int SIZE_IMG_OVERLAY_TEXT = 14;
    public static final int SIZE_IMG_OVERLAY_NOSE = 25;


    public static final int SIZE_PATTERN_TEXT = 28;
    public static final int SIZE_FILTER = 37;
    public static ArrayList<String> mListFilter = new ArrayList<>();


    public static final int MAX_SIZE_SEEKBAR = 100;
    public static final String APP_QC = "http://bsoftjsc.com/bs/cross_apps.txt";
    public static final int POSDELAY = 15000;
    public static String APP_FOLDER = "PhotoBlender";
    public static final int COLLAGE_PHOTO_TYPE = 0x11;
    public static final String EXTRA_COLLAGE_TYPE = "EXTRA_COLLAGE_TYPE";
    public static final int SHADOW_SIZE = 22;
    public static final String VIEW_TYPE = "VIEW_TYPE";
    public static int countSquare = 3;

    public static void loadImageFilter() {
        mListFilter.clear();
        for (int i = 1; i <= SIZE_FILTER; i++) {
            mListFilter.add("filter/ic_filter_" + i + ".png");
        }
    }

    public static void loadImageOverlayText() {
        mListOverlayText.clear();
        for (int i = 1; i <= SIZE_IMG_OVERLAY_TEXT; i++) {
            mListOverlayText.add("overlay/texture/vintage_" + i + ".jpg");
        }
    }

    public static void loadImageOverlayNoise() {
        mListOverlayNoise.clear();
        for (int i = 1; i < SIZE_IMG_OVERLAY_NOSE; i++) {
            mListOverlayNoise.add("overlay/noise/overlay_" + i + ".jpg");
        }
    }

    public static void loadPatterText() {
        mListPattern1.clear();
        mListPattern2.clear();
        for (int i = 1; i <= SIZE_PATTERN_TEXT; i++) {
            if (i <= 14) {
                mListPattern1.add("text/pattern/ic_bag_" + i + ".jpg");
            } else {
                mListPattern2.add("text/pattern/ic_bag_" + i + ".jpg");
            }
        }
    }

    public static void loadIconPattern() {
        mListIconPattern.clear();
        for (int i = 0; i <= 8; i++) {
            mListIconPattern.add("square_bg/icon/pattern_icon_" + i + ".png");
        }
    }

    public static void loadIconFrame() {
        mListIconFrame.clear();
        for (int i = 0; i < 44; i++) {
            mListIconFrame.add("square_frame/icon/ic_border_" + i + ".png");
        }
    }

    public static final int FIT_IMAGE = 0;
    public static final int FREE_IMAGE = 1000;
    public static final int CIRCLE = 100;
    public static List<RatioCrop> getListRatioCropType(){
        List<RatioCrop> ratioCrops = new ArrayList<>();
        ratioCrops.add(new RatioCrop("Default","Default",FIT_IMAGE,FIT_IMAGE));
        ratioCrops.add(new RatioCrop("Free ratio","Free ratio",FREE_IMAGE,FREE_IMAGE));
        ratioCrops.add(new RatioCrop("Ins","1:1",1,1));
        ratioCrops.add(new RatioCrop("Ins","4:5",4,5));
        ratioCrops.add(new RatioCrop("Ins","Ins Horizontal",600,315));
        ratioCrops.add(new RatioCrop("Ins","9:16",9,16));
        ratioCrops.add(new RatioCrop("Fb","1:1",1,1));
        ratioCrops.add(new RatioCrop("Fb Cover","Fb Cover",820,461));
        ratioCrops.add(new RatioCrop("Fb","9:16",9,16));
        ratioCrops.add(new RatioCrop("Youtube Cover","Youtube Cover",2560,1440));
        ratioCrops.add(new RatioCrop("Twitter Cover","Twitter Cover",3,1));
        ratioCrops.add(new RatioCrop("Ratio","1:2",1,2));
        ratioCrops.add(new RatioCrop("Ratio","2:3",2,3));
        ratioCrops.add(new RatioCrop("Ratio","3:4",3,4));
        ratioCrops.add(new RatioCrop("Circle","Circle",CIRCLE,CIRCLE));
        ratioCrops.add(new RatioCrop("Ratio","5:4",5,4));
        ratioCrops.add(new RatioCrop("Ratio","4:3",4,3));
        ratioCrops.add(new RatioCrop("Ratio","3:2",3,2));
        ratioCrops.add(new RatioCrop("Ratio","16:9",16,9));
        ratioCrops.add(new RatioCrop("Ratio","2:1",2,1));

        return ratioCrops;
    }

    public static int[][] getListRatio(){
        return new int[][]{{1,2},{9,16},{2,3},{3,4},{4,5},{1,1},{5,4},{4,3},{3,2},{16,9},{2,1},};
    }

}
