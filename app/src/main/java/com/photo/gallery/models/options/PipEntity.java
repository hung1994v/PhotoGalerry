package com.photo.gallery.models.options;

import android.graphics.Bitmap;
import android.util.Log;

public class PipEntity {
    private static final String TAG;
    public float dx;
    public float dy;
    public Bitmap iconBitmap;
    public int iconId;
    public String iconUrl;
    public String id;
    public boolean isFromSdCard;
    public int maskId;
    public String maskUrl;
    public int shadeId;
    public String shadeUrl;

    static {
        TAG = PipEntity.class.getSimpleName();
    }

    public PipEntity(float dx, float dy, int maskId, int shadeId, int iconId) {
        this.isFromSdCard = false;
        this.dx = dx;
        this.dy = dy;
        this.maskId = maskId;
        this.shadeId = shadeId;
        this.iconId = iconId;
    }


//    public static PipEntity loadFromJson(String path, boolean decodeBitmap) {
//        String jsonString = BuildConfig.FLAVOR;
//        try {
//            DataInputStream in = new DataInputStream(new FileInputStream(path));
//            BufferedReader br = new BufferedReader(new InputStreamReader(in));
//            while (true) {
//                String strLine = br.readLine();
//                if (strLine == null) {
//                    break;
//                }
//                jsonString = jsonString + strLine;
//            }
//            in.close();
//            PipEntity resultPipEntity = (PipEntity) new Gson().fromJson(jsonString, PipEntity.class);
//            if (resultPipEntity == null) {
//                return resultPipEntity;
//            }
//            String folder = new File(path).getParent() + "/";
//            Log.e(TAG, folder + resultPipEntity.iconUrl);
//            if (decodeBitmap) {
//                resultPipEntity.iconBitmap = BitmapFactory.decodeFile(folder + resultPipEntity.iconUrl);
//            }
//            resultPipEntity.isFromSdCard = true;
//            return resultPipEntity;
//        } catch (Exception e) {
//            System.err.println("Error: " + e.getMessage());
//            return null;
//        }
//    }

//    public String getFullShadePath(Context context) {
//        return PipOnlineLib.getDirectoryName(context, this.id) + "/" + this.shadeUrl;
//    }
//
//    public String getFullMaskPath(Context context) {
//        return PipOnlineLib.getDirectoryName(context, this.id) + "/" + this.maskUrl;
//    }
}