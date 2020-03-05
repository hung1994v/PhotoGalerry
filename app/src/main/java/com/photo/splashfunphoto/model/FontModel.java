package com.photo.splashfunphoto.model;

/**
 * Created by nam on 4/5/2017.
 */

public class FontModel {
    public String fontPreview;
    public String fontPath;


    public FontModel(){

    }

    public FontModel(String mFontPreview, String mFontPath) {
        this.fontPreview = mFontPreview;
        this.fontPath = mFontPath;
    }

    public String getmFontPreview() {
        return fontPreview;
    }

    public void setmFontPreview(String mFontPreview) {
        this.fontPreview = mFontPreview;
    }

    public String getmFontPath() {
        return fontPath;
    }

    public void setmFontPath(String mFontPath) {
        this.fontPath = mFontPath;
    }
}
