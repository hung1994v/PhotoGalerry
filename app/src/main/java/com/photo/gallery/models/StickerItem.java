package com.photo.gallery.models;

import java.util.List;

public class StickerItem {
    private String stickerName;
    private List<String> listStickerChild;

    public StickerItem(String stickerName, List<String> listStickerChild) {
        this.stickerName = stickerName;
        this.listStickerChild = listStickerChild;
    }

    public String getStickerName() {
        return stickerName;
    }

    public void setStickerName(String stickerName) {
        this.stickerName = stickerName;
    }

    public List<String> getListStickerChild() {
        return listStickerChild;
    }

    public void setListStickerChild(List<String> listStickerChild) {
        this.listStickerChild = listStickerChild;
    }
}