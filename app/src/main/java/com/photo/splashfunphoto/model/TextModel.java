package com.photo.splashfunphoto.model;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.text.TextPaint;

/**
 * Created by Computer on 6/7/2017.
 */

public class TextModel {
    private Bitmap mBitmap;
    private Shader mShader;
    private int mColor;
    private Matrix mMatrix;
    private TextPaint mTextPaint;

    public TextModel(Bitmap bitmap, Matrix matrix, TextPaint textPaint) {
        mBitmap = bitmap;
        mMatrix = matrix;
        mTextPaint = textPaint;
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public Shader getmShader() {
        return mShader;
    }

    public void setmShader(Shader mShader) {
        this.mShader = mShader;
    }

    public int getmColor() {
        return mColor;
    }

    public void setmColor(int mColor) {
        this.mColor = mColor;
    }

    public Matrix getmMatrix() {
        return mMatrix;
    }

    public void setmMatrix(Matrix mMatrix) {
        this.mMatrix = mMatrix;
    }

    public TextPaint getmTextPaint() {
        return mTextPaint;
    }

    public void setmTextPaint(TextPaint mTextPaint) {
        this.mTextPaint = mTextPaint;
    }
}
