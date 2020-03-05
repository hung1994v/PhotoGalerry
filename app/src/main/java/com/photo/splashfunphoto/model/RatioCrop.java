package com.photo.splashfunphoto.model;

public class RatioCrop {
    private String nameRatio;
    private float ratioW;
    private float ratioH;
    private String type;

    public RatioCrop() {
    }

    public RatioCrop(String type,String nameRatio, float ratioW, float ratioH) {
        this.nameRatio = nameRatio;
        this.ratioW = ratioW;
        this.ratioH = ratioH;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNameRatio() {
        return nameRatio;
    }

    public void setNameRatio(String nameRatio) {
        this.nameRatio = nameRatio;
    }

    public float getRatioW() {
        return ratioW;
    }

    public void setRatioW(float ratioW) {
        this.ratioW = ratioW;
    }

    public float getRatioH() {
        return ratioH;
    }

    public void setRatioH(float ratioH) {
        this.ratioH = ratioH;
    }
}
