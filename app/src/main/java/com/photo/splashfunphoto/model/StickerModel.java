package com.photo.splashfunphoto.model;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by Computer on 6/7/2017.
 */
public class StickerModel {
    private Bitmap mBitmap;
    private Matrix mMatrix;
    private int mOpacity;

    public StickerModel(Bitmap bitmap, Matrix matrix, int i) {
        mBitmap = bitmap;
        mMatrix = matrix;
        mOpacity = i;
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public Matrix getmMatrix() {
        return mMatrix;
    }

    public void setmMatrix(Matrix mMatrix) {
        this.mMatrix = mMatrix;
    }

    public int getmOpacity() {
        return mOpacity;
    }

    public void setmOpacity(int mOpacity) {
        this.mOpacity = mOpacity;
    }
}
