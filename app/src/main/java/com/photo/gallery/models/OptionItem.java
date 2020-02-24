package com.photo.gallery.models;

/**
 * Created by Tung on 3/30/2018.
 */

public class OptionItem {

//    public String label = null;
    public int resID = -1;
    private boolean isCheck = false;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
