package com.photo.gallery.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * Created by Adm on 9/7/2016.
 */


public class ResizeImage {
    float orientation;
    private Context context;
    private int imageHeight;
    private int imageWidth;
    private Bitmap sampledSrcBitmap;

    public ResizeImage(Context applicationContext) {
        this.context = applicationContext;
    }

    public Bitmap getBitmap(String imagePath, int widthPixels) {
        this.orientation = getImageOrientation(imagePath);
        getAspectRatio(imagePath, widthPixels);

        return getResizedOriginalBitmap(imagePath, this.imageWidth, this.imageHeight);
    }

    public Bitmap getBitmap(String imagePath, int widthPixels,int height) {
        this.orientation = getImageOrientation(imagePath);
        getAspectRatio(imagePath, widthPixels,height);

        return getResizedOriginalBitmap(imagePath, this.imageWidth, this.imageHeight);
    }

    private void getAspectRatio(String selectedImagePath, int widthPixels, int height) {
        float scaleWidth;
        float scaleHeight;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        float scaleFactor = ((float) options.outWidth) / ((float) options.outHeight);
        if (scaleFactor < 1.0f) {
            scaleWidth = (float) widthPixels;
            scaleHeight = scaleWidth / scaleFactor;
        } else {
            scaleHeight = (float) height;
            scaleWidth = scaleHeight * scaleFactor;
        }
        this.imageWidth = (int) scaleWidth;
        this.imageHeight = (int) scaleHeight;
    }

    private void getAspectRatio(String selectedImagePath, int widthPixels) {
        float scaleWidth;
        float scaleHeight;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        float scaleFactor = ((float) options.outWidth) / ((float) options.outHeight);
        if (scaleFactor > 1.0f) {
            scaleWidth = (float) widthPixels;
            scaleHeight = scaleWidth / scaleFactor;
        } else {
            scaleHeight = (float) widthPixels;
            scaleWidth = scaleHeight * scaleFactor;
        }
        this.imageWidth = (int) scaleWidth;
        this.imageHeight = (int) scaleHeight;
    }

    private Bitmap getResizedOriginalBitmap(String imagePath, int imagwidth, int imageheight) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(imagePath), null, options);
            int srcWidth = options.outWidth;
            int srcHeight = options.outHeight;
            int desiredWidth = imagwidth;
            int desiredHeight = imageheight;
            int inSampleSize = 1;
            while (srcWidth / 2 > desiredWidth) {
                srcWidth /= 2;
                srcHeight /= 2;
                inSampleSize *= 2;
            }
            float desiredWidthScale = ((float) desiredWidth) / ((float) srcWidth);
            float desiredHeightScale = ((float) desiredHeight) / ((float) srcHeight);
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inSampleSize = inSampleSize;
            options.inScaled = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            sampledSrcBitmap = BitmapFactory.decodeStream(new FileInputStream(imagePath), null, options);
            if (sampledSrcBitmap == null) {
                return null;
            }
            Matrix matrix = new Matrix();
            matrix.postScale(desiredWidthScale, desiredHeightScale);
            matrix.postRotate(this.orientation);
            return Bitmap.createBitmap(sampledSrcBitmap, 0, 0, sampledSrcBitmap.getWidth(), sampledSrcBitmap.getHeight(), matrix, true);
        } catch (FileNotFoundException e) {
            Toast.makeText(context, "Image format not supported...please choose other image...", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private float getImageOrientation(String static_image) {
        try {
            int orientation = new ExifInterface(static_image).getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                return 90.0f;
            }
            if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                return 180.0f;
            }
            if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                return 270.0f;
            }
            return 0.0f;
        } catch (IOException e) {
            e.printStackTrace();
            return 0.0f;
        }
    }


}