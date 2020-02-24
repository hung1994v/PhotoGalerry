package bsoft.com.lib_filter.filter.gpu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.File;

import bsoft.com.lib_filter.filter.gpu.util.BitmapUtil;


public class WBImageRes extends WBRes {
    private FitType fitType;
    protected String imageFileName;
    private int imageID;
    protected LocationType imageType;
    private Boolean isShowLike;

    public enum FitType {
        TITLE,
        SCALE
    }

    public interface OnResImageDownLoadListener {
        void onImageDownLoadFaile();

        void onImageDownLoadFinish(String str);
    }

    public interface OnResImageLoadListener {
        void onImageLoadFaile();

        void onImageLoadFinish(Bitmap bitmap);
    }


    public WBImageRes() {
        this.isShowLike = Boolean.valueOf(false);
    }

    public void setIsShowLikeIcon(boolean flag) {
        this.isShowLike = Boolean.valueOf(flag);
    }

    public Boolean getIsShowLikeIcon() {
        return this.isShowLike;
    }

    public String getImageFileName() {
        return this.imageFileName;
    }

    public void setImageFileName(String image) {
        this.imageFileName = image;
    }

    public LocationType getImageType() {
        return this.imageType;
    }

    public void setImageType(LocationType imageType) {
        this.imageType = imageType;
    }



    public void getImageBitmap(Context context, OnResImageLoadListener listener) {
        if (this.imageType == null && listener != null) {
            listener.onImageLoadFaile();
        }
        if (this.imageType == LocationType.RES) {
            if (listener != null) {
                listener.onImageLoadFinish(BitmapUtil.getImageFromAssetsFile(getResources(), this.imageFileName));
            }
        } else if (this.imageType == LocationType.ASSERT) {
            if (listener != null) {
                listener.onImageLoadFinish(BitmapUtil.getImageFromAssetsFile(getResources(), this.imageFileName));
            }
        }
    }

    public Bitmap getLocalImageBitmap() {
        if (this.imageType == null) {
            return null;
        }
        if (this.imageType == LocationType.RES) {
            return BitmapUtil.getImageFromResourceFile(getResources(), this.imageID);
        }
        if (this.imageType == LocationType.ASSERT) {
            return BitmapUtil.getImageFromAssetsFile(getResources(), this.imageFileName);
        }
        return null;
    }

//    public void downloadImageOnlineRes(Context context, OnResImageDownLoadListener listener) {
//        if (context == null) {
//            listener.onImageDownLoadFaile();
//            return;
//        }
//        String path = context.getFilesDir().getAbsolutePath();
//        File dir = new File(new StringBuilder(String.valueOf(path)).append("/").append(WBMaterialFactory.MaterialRootDir).toString());
//        if (!dir.exists()) {
//            dir.mkdir();
//        }
//        File res_dir = new File(new StringBuilder(String.valueOf(path)).append("/").append(WBMaterialFactory.MaterialRootDir).append("/").append(getName()).toString());
//        if (!res_dir.exists()) {
//            res_dir.mkdir();
//        }
//        new AsyncImageLoader().loadImageToFile(context, getImageFileName(), new StringBuilder(String.valueOf(path)).append("/").append(WBMaterialFactory.MaterialRootDir).append("/").append(getName()).append("/").append(getName()).toString(), new AnonymousClass1(listener));
//    }

    public FitType getFitType() {
        return this.fitType;
    }

    public void setScaleType(FitType fitType) {
        this.fitType = fitType;
    }

    public FitType getScaleType() {
        return this.fitType;
    }
//
//    private String onlineImageResLocalFile(Context context) {
//        String path = context.getFilesDir().getAbsolutePath();
//        File dir = new File(new StringBuilder(String.valueOf(path)).append("/").append(WBMaterialFactory.MaterialRootDir).toString());
//        if (!dir.exists()) {
//            dir.mkdir();
//        }
//        if (new File(new StringBuilder(String.valueOf(path)).append("/").append(WBMaterialFactory.MaterialRootDir).append("/").append(getName()).toString()).exists()) {
//            String resImageFileName = new StringBuilder(String.valueOf(path)).append("/").append(WBMaterialFactory.MaterialRootDir).append("/").append(getName()).append("/").append(getName()).toString();
//            if (new File(resImageFileName).exists()) {
//                return resImageFileName;
//            }
//        }
//        return null;
//    }
}