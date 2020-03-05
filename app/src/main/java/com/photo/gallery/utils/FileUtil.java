package com.photo.gallery.utils;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.documentfile.provider.DocumentFile;

import com.photo.gallery.BuildConfig;
import com.photo.gallery.R;
import com.photo.gallery.models.FileItem;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;

import bsoft.com.lib_filter.filter.gpu.util.BitmapUtil;

/**
 * Created by Hoavt on 3/15/2018.
 */

public class FileUtil {

    private static final java.lang.String TAG = FileUtil.class.getSimpleName();
    private static final boolean FLAG_COPY_OLD = false;

    public static String getExtension(String fileName) {
        String extension = null;

        int i = fileName.lastIndexOf('.');
        int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

        if (i > p) {
            extension = "." + fileName.substring(i + 1);
        }
        return extension;
    }

    public static String formatSize(long size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
                if (size >= 1024) {
                    suffix = "GB";
                    size /= 1024;
                }
            }
        } else {
            suffix = "B";
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }

    public static void openPhotoIntent(Context context, String path) {
        Uri uri;

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", new File(path));
        } else {
            uri = Uri.fromFile(new File(path));
        }
        intent.setDataAndType((uri), "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);

//        final File videoFile = new File(path);
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.fromFile(videoFile), "image/*");
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//DO NOT FORGET THIS EVER
//        context.startActivity(intent);
    }

    public static void openVideoIntent(Context context, String pathVideo) {
        Uri uri;

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", new File(pathVideo));
        } else {
            uri = Uri.fromFile(new File(pathVideo));
        }
        intent.setDataAndType((uri), "video/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
    }

    public static void setPictureAs(Activity activity, String path) {

        WallpaperManager myWallpaperManager
                = WallpaperManager.getInstance(activity);
        try {
            int[] screenSizes = Utils.getScreenSize(activity);
            int reqWidth = screenSizes[0];
            int reqHeight = screenSizes[1];
            if (reqWidth<=0) {
                reqWidth = 500;
            }
            if (reqHeight<=0) {
                reqHeight = 500;
            }
            Bitmap bmp = BitmapUtil.sampledBitmapFromFile(path, reqWidth, reqHeight);
            myWallpaperManager.setBitmap(bmp);
            Toast.makeText(activity, activity.getString(R.string.set_wallpaper)+ " "+activity.getString(R.string.successfully), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(activity, activity.getString(R.string.set_wallpaper)+ " "+activity.getString(R.string.unsuccessfully), Toast.LENGTH_SHORT).show();
        }


        if (true) {
            return;
        }
        try {
            InputStream input = new BufferedInputStream(new FileInputStream(path));
            Flog.d("INPUTSTREAM " + input);
            myWallpaperManager.setStream(input);
            Toast.makeText(activity, activity.getString(R.string.set_wallpaper)+ " "+activity.getString(R.string.successfully), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(activity, activity.getString(R.string.set_wallpaper)+ " "+activity.getString(R.string.unsuccessfully), Toast.LENGTH_SHORT).show();
        }
    }

    public static Uri insert(Context context, FileItem fileItem, Bitmap bitmap, String dstFile) throws IOException {

        File destFile = new File(dstFile);

        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        String name = destFile.getName();
        Flog.d(TAG, "dst naem file=" + name);

        Uri uri = null;
        try {
            FileOutputStream out = new FileOutputStream(dstFile);

            try {
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {

                    ContentValues conValues = new ContentValues();
                    //            conValues.put(MediaStore.Images.Media._ID, fileItem.);
                    //            conValues.put(MediaStore.Images.Media.BUCKET_ID, name);
                    conValues.put(MediaStore.Images.Media.DISPLAY_NAME, name);
                    conValues.put(MediaStore.Images.Media.BUCKET_DISPLAY_NAME, ConstValue.APP_FOLDER);
                    conValues.put(MediaStore.Images.Media.DATA, dstFile);
                    conValues.put(MediaStore.Images.Media.DATE_MODIFIED, String.valueOf(System.currentTimeMillis() / 1000));
                    conValues.put(MediaStore.Images.Media.HEIGHT, bitmap.getHeight());
                    conValues.put(MediaStore.Images.Media.WIDTH, bitmap.getWidth());
//                    conValues.put(MediaStore.Images.Media.SIZE, -1);
                    conValues.put(MediaStore.Images.Media.MIME_TYPE, fileItem.mime_type);
                    conValues.put(MediaStore.Images.Media.ORIENTATION, fileItem.orientation);

                    ContentResolver cr = context.getContentResolver();
                    uri = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, conValues);

                    out.flush();
                    out.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return uri;
    }

    private static boolean renameFile(File f, String name) {
        String newPath = f.getParent() + "/" + name;
        if (f.getParentFile().canWrite()) {
            return f.renameTo(new File(newPath));
        }
        return false;
    }

    public static boolean renameContentProvider(String newName, FileItem audioEntity, Context context, String dirSave) {
        boolean success = false;
        if(audioEntity.isImage){
            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media.DATA + " = ?", new String[]{audioEntity.path}, null);
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, newName);
            values.put(MediaStore.Images.Media.DATA, dirSave);

            File f = new File(audioEntity.path);

            try {
                contentResolver.update(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values, MediaStore.Images.Media.DATA + " = ?", new String[]{f.getAbsolutePath()});
            } catch (Exception e) {
                success = false;
            }

            if (cursor != null) {
                cursor.close();
            }

            File file = new File(audioEntity.path);
            if (file.exists()) {
                Log.e("xxx ", " frename " + file.renameTo(new File(dirSave)));
                success = true;
            }
        }else {
            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Video.Media.DATA + " = ?", new String[]{audioEntity.path}, null);
            ContentValues values = new ContentValues();
            values.put(MediaStore.Video.Media.TITLE, newName);
            values.put(MediaStore.Video.Media.DATA, dirSave);

            File f = new File(audioEntity.path);

            try {
                contentResolver.update(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values, MediaStore.Video.Media.DATA + " = ?", new String[]{f.getAbsolutePath()});
            } catch (Exception e) {
                success = false;
            }

            if (cursor != null) {
                cursor.close();
            }

            File file = new File(audioEntity.path);
            if (file.exists()) {
                Log.e("xxx ", " frename " + file.renameTo(new File(dirSave)));
                success = true;
            }
        }

        return success;
    }


    public static boolean rename(Context context, File f, String name) {
        String newPath = f.getParent() + "/" + name;
        Log.d("DCM",newPath);
        boolean success = false;
        if (f.getParentFile().canWrite()) {
            Flog.d(TAG, "oldPath=" + f.getPath());
            Flog.d(TAG, "newPath=" + newPath);
            File nf = new File(newPath);
            success = f.renameTo(nf);
            Flog.d(TAG, "exists=" + nf.exists());
            if (!success) {
                return success;
            }

            ContentValues conValues = new ContentValues();
            conValues.put("_display_name", name);
            conValues.put("_data", newPath);

            Uri externalUri = MediaStore.Files.getContentUri("external");
            boolean successMediaStore = context.getContentResolver().update(
                    externalUri, conValues,
                    MediaStore.MediaColumns.DATA + "='" + f.getAbsolutePath() + "'", null) == 1;
            Flog.d(TAG, "successMediaStore rename=" + successMediaStore);
            success &= successMediaStore;

        }
        return success;
    }


    /**
     * https://stackoverflow.com/questions/15487399/refresh-the-gallery-after-deleting-an-image-file
     */
    public static void scanMediaStore(Context context, String... paths) {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                        Uri.parse("file://" + Environment.getExternalStorageDirectory())));
            } else {

                MediaScannerConnection.scanFile(context, paths, null, new MediaScannerConnection.OnScanCompletedListener() {
                    /*
                     *   (non-Javadoc)
                     * @see android.media.MediaScannerConnection.OnScanCompletedListener#onScanCompleted(java.lang.String, android.net.Uri)
                     */
                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE));


//                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                File f = new File(paths[0]);
//                Uri contentUri = Uri.fromFile(f);
//                mediaScanIntent.setData(contentUri);
//                context.sendBroadcast(mediaScanIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Flog.d("scanMediaStore failed");
        }
    }

    public static void toastFailed(Context context, String action) {
        Toast.makeText(context, action + " " + context.getString(R.string.unsuccessfully), Toast.LENGTH_LONG).show();
    }

    public static void toastSuccess(Context context, String action) {
        Toast.makeText(context, action + " " + context.getString(R.string.successfully), Toast.LENGTH_SHORT).show();
    }


    public static Uri getUrifromFile(Context context, String pathVideo) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", new File(pathVideo));
        } else {
            uri = Uri.fromFile(new File(pathVideo));
        }
        return uri;
    }


    public static void share(Context context, Uri... arrUri) {
        if (arrUri == null) {
            return;
        }

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.");
        intent.setType("*/*");/* This example is sharing jpeg images. */
        ArrayList<Uri> files = new ArrayList<Uri>();
        Collections.addAll(files, arrUri);
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share)));
    }

    public static boolean delete(Context context, Uri... arrUri) {

        boolean success = true;
        for (int i = 0; i < arrUri.length; i++) {
            String path = arrUri[i].getPath();
            File file = new File(path);
            success = file.delete();
        }
        if (!success) {
            return success;
        }

        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = MediaStore.Files.getContentUri("external");
        int len = arrUri.length;
        String[] paths = new String[len];
        int result = 0;
        for (int i = 0; i < len; i++) {
            paths[i] = arrUri[i].getPath();
            result += contentResolver.delete(uri,
                    MediaStore.Files.FileColumns.DATA + "=?",
                    new String[]{paths[i]});
        }
        return (result == len);
    }

    public static void copyFileOrDirectory(String srcDir, String dstDir) {

        try {
            File src = new File(srcDir);
            File dst = new File(dstDir, src.getName());

            if (src.isDirectory()) {

                String files[] = src.list();
                int filesLength = files.length;
                for (int i = 0; i < filesLength; i++) {
                    String src1 = (new File(src, files[i]).getPath());
                    String dst1 = dst.getPath();
                    copyFileOrDirectory(src1, dst1);

                }
            } else {
                copyFile(src, dst);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    public static boolean copy(Context context, File sourceFile, File destFile, FileItem fileItem) throws IOException {
        boolean success = true;

        if (!destFile.getParentFile().exists())
            success &= destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            success &= destFile.createNewFile();
        }

        if (FLAG_COPY_OLD) {
            FileChannel source = null;
            FileChannel destination = null;

            try {
                source = new FileInputStream(sourceFile).getChannel();
                destination = new FileOutputStream(destFile).getChannel();

                destination.transferFrom(source, 0, source.size());
            } catch (Exception ex) {
                ex.printStackTrace();
                success = false;
                return success;
            } finally {
                if (source != null) {
                    source.close();
                }
                if (destination != null) {
                    destination.close();
                }
            }
        } else {

            InputStream in = null;
            OutputStream out = null;
            try {

                in = new FileInputStream(sourceFile);
                out = new FileOutputStream(destFile);

                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                in.close();
                in = null;

                // write the output file (You have now copied the file)
                out.flush();
                out.close();
                out = null;

                success &= true;
            }  catch (FileNotFoundException fnfe1) {
                success &= false;
            }
            catch (Exception e) {
                success &= false;
            }
        }

        if (!success) {
            return success;
        }

        ContentValues conValues = new ContentValues();
        if (fileItem.isImage) {
//            conValues.put(MediaStore.Images.Media._ID, fileItem.);
//            conValues.put(MediaStore.Images.Media.BUCKET_ID, name);
            conValues.put(MediaStore.Images.Media.DISPLAY_NAME, destFile.getName());
//            conValues.put(MediaStore.Images.Media.BUCKET_DISPLAY_NAME, fileItem.folder);
            conValues.put(MediaStore.Images.Media.DATA, destFile.getAbsolutePath());
            conValues.put(MediaStore.Images.Media.DATE_MODIFIED, String.valueOf(System.currentTimeMillis() / 1000));
            conValues.put(MediaStore.Images.Media.HEIGHT, fileItem.height);
            conValues.put(MediaStore.Images.Media.WIDTH, fileItem.width);
            conValues.put(MediaStore.Images.Media.SIZE, fileItem.mSize);
            conValues.put(MediaStore.Images.Media.MIME_TYPE, fileItem.mime_type);
            conValues.put(MediaStore.Images.Media.ORIENTATION, fileItem.orientation);
        } else {

//            conValues.put(MediaStore.Video.Media._ID, );
//            conValues.put(MediaStore.Video.Media.BUCKET_ID, );
            conValues.put(MediaStore.Video.Media.DISPLAY_NAME, destFile.getName());
//            conValues.put(MediaStore.Video.Media.BUCKET_DISPLAY_NAME, );
            conValues.put(MediaStore.Video.Media.DATA, destFile.getAbsolutePath());
            conValues.put(MediaStore.Video.Media.DATE_MODIFIED, String.valueOf(System.currentTimeMillis() / 1000));
            conValues.put(MediaStore.Video.Media.HEIGHT, fileItem.height);
            conValues.put(MediaStore.Video.Media.WIDTH, fileItem.width);
            conValues.put(MediaStore.Video.Media.SIZE, fileItem.mSize);
            conValues.put(MediaStore.Video.Media.MIME_TYPE, fileItem.mime_type);
            conValues.put(MediaStore.Video.Media.DURATION, fileItem.duration);
        }

        Uri externalUri = MediaStore.Files.getContentUri("external");
        boolean successMediaStore = context.getContentResolver().insert(
                externalUri, conValues) != null;
        success &= successMediaStore;
        Flog.d(TAG, "successMediaStore copy=" + successMediaStore);
        return success;
    }

    public static int getOrientation(String imagePath) {
        int rotate = 0;
        try {
            File imageFile = new File(imagePath);

            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static boolean isFileExistsInMediaStore(Context context, String path) {

        Uri externalUri = MediaStore.Files.getContentUri("external");
        String[] proj = {MediaStore.Files.FileColumns.DATA};
        Cursor cursor = context.getContentResolver().query(externalUri, proj,
                MediaStore.Files.FileColumns.DATA + "=?",
                new String[]{path}, null);

        if (cursor == null) {
            return false;
        }

        boolean exists = false;
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
            String pathAvail = cursor.getString(column_index);
            exists = new File(pathAvail).exists();
        }
        cursor.close();
        return exists;
    }

    public static String getRealPathFromURI(Context context, Uri contentURI) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public static int[] getDimension(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(path).getAbsolutePath(), options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        return new int[]{imageWidth, imageHeight};
    }

    public static long getDurationVideo(Context context, String path) {
        long timeInMillisec = 0;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        //use one of overloaded setDataSource() functions to set your data source
        try {
            retriever.setDataSource(context, Uri.fromFile(new File(path)));
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            timeInMillisec = Long.parseLong(time);
        } catch (Exception e) {
        }
        retriever.release();
        return timeInMillisec;
    }



    public static boolean isStringHasCharacterSpecial(String text) {
        for (int i = 0; i < listSpecialCharacter.length; i++) {
            if (text.contains(listSpecialCharacter[i])) {
                return true;
            }
        }
        return false;
    }
    public static final String[] listSpecialCharacter = new String[]{"%", "/", "#", "^", ":", "?", ","};
}
