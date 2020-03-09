package com.photo.gallery.utils;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.BaseColumns;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import androidx.documentfile.provider.DocumentFile;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.photo.gallery.R;
import com.photo.gallery.models.FileItem;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by hoa on 5/4/2018.
 */

public class FileUtils {

    public static final String EXTERNAL_VOLUME = "external";
    public static final String fileExtensionZip = "zip", fileExtensinJar = "jar", fileExtensionApk = "apk";
    public static final String fileExtensionTar = "tar";
    public static final String fileExtensionGzipTar = "tar.gz";
    public static final String fileExtensionRar = "rar";
    public static final int DOESNT_EXIST = 0;
    public static final int WRITABLE_OR_ON_SDCARD = 1;
    //For Android 5
    public static final int CAN_CREATE_FILES = 2;
    private static final java.lang.String TAG = FileUtils.class.getSimpleName();
    private static final String EMULATED_STORAGE_SOURCE = System.getenv("EMULATED_STORAGE_SOURCE");
    private static final String EMULATED_STORAGE_TARGET = System.getenv("EMULATED_STORAGE_TARGET");
    private static final String EXTERNAL_STORAGE = System.getenv("EXTERNAL_STORAGE");
    private static final String INTERNAL_VOLUME = "internal";


    public static File getFileDownloads() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    }

    // Utility methods for Android 5


    public static String parseFilePermission(File f) {
        String per = "";
        if (f.canRead()) {
            per = per + "r";
        }
        if (f.canWrite()) {
            per = per + "w";
        }
        if (f.canExecute()) {
            per = per + "x";
        }
        return per;
    }

    public static boolean canListFiles(File f) {
        return f.canRead() && f.isDirectory();
    }

    /**
     * Check if a file is readable.
     *
     * @param file The file
     * @return true if the file is reabable.
     */
    public static boolean isReadable(final File file) {
        if (file == null)
            return false;
        if (!file.exists()) return false;

        boolean result;
        try {
            result = file.canRead();
        } catch (SecurityException e) {
            return false;
        }

        return result;
    }

    /**
     * Check if a file is writable. Detects write issues on external SD card.
     *
     * @param file The file
     * @return true if the file is writable.
     */
    public static boolean isWritable(final File file) {
        if (file == null)
            return false;
        boolean isExisting = file.exists();

        try {
            FileOutputStream output = new FileOutputStream(file, true);
            try {
                output.close();
            } catch (IOException e) {
                // do nothing.
            }
        } catch (FileNotFoundException e) {
            return false;
        }
        boolean result = file.canWrite();

        // Ensure that file is not created during this process.
        if (!isExisting) {
            file.delete();
        }

        return result;
    }

    public static String[] getFolderNamesInPath(String path) {
        if (!path.endsWith("/")) path += "/";
        return ("root" + path).split("/");
    }

    public static String[] getPathsInPath(String path) {
        if (path.endsWith("/")) path = path.substring(0, path.length() - 1);

        ArrayList<String> paths = new ArrayList<>();

        while (path.length() > 0) {
            paths.add(path);
            path = path.substring(0, path.lastIndexOf("/"));
        }

        paths.add("/");
        Collections.reverse(paths);

        return paths.toArray(new String[paths.size()]);
    }

    /**
     * Determine if a file is on external sd card. (Kitkat or higher.)
     *
     * @param file The file.
     * @return true if on external sd card.
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean isOnExtSdCard(final File file, Context c) {
        return getExtSdCardFolder(file, c) != null;
    }

    /**
     * Determine the main folder of the external SD card containing the given file.
     *
     * @param file the file.
     * @return The main folder of the external SD card containing this file, if the file is on an SD card. Otherwise,
     * null is returned.
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getExtSdCardFolder(final File file, Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return null;
        }
        try {
            String[] extSdPaths = getExtSdCardPaths(context);
            for (int i = 0; i < extSdPaths.length; i++) {
                if (file.getCanonicalPath().startsWith(extSdPaths[i])) {
                    return extSdPaths[i];
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * Get a list of external SD card paths. (Kitkat or higher.)
     *
     * @return A list of external SD card paths.
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static String[] getExtSdCardPaths(Context context) {
        List<String> paths = new ArrayList<>();
        for (File file : context.getExternalFilesDirs("external")) {
            if (file != null && !file.equals(context.getExternalFilesDir("external"))) {
                int index = file.getAbsolutePath().lastIndexOf("/Android/data");
                if (index < 0) {
                    Flog.d(TAG, "Unexpected external file dir: " + file.getAbsolutePath());
                } else {
                    String path = file.getAbsolutePath().substring(0, index);
                    try {
                        path = new File(path).getCanonicalPath();
                    } catch (IOException e) {
                        // Keep non-canonical path.
                    }
                    paths.add(path);
                }
            }
        }
        if (paths.isEmpty()) paths.add("/storage/sdcard1");
        return paths.toArray(new String[0]);
    }

    private static void applyNewDocFlag(Intent i) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        } else {

            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME
                    | Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS);
        }
    }

    /**
     * Method supports showing a UI to ask user to open a file without any extension/mime
     */





    public static String normalizeMediaPath(String path) {
        // Retrieve all the paths and check that we have this environment vars
        if (TextUtils.isEmpty(EMULATED_STORAGE_SOURCE) ||
                TextUtils.isEmpty(EMULATED_STORAGE_TARGET) ||
                TextUtils.isEmpty(EXTERNAL_STORAGE)) {
            return path;
        }

        // We need to convert EMULATED_STORAGE_SOURCE -> EMULATED_STORAGE_TARGET
        if (path.startsWith(EMULATED_STORAGE_SOURCE)) {
            path = path.replace(EMULATED_STORAGE_SOURCE, EMULATED_STORAGE_TARGET);
        }
        return path;
    }

    private static String getExtension(String path) {
        return path.substring(path.indexOf('.') + 1, path.length()).toLowerCase();
    }

    public static boolean isFileExtractable(String path) {
        String type = getExtension(path);

        return isZip(type) || isTar(type) || isRar(type) || isGzippedTar(type);
    }

    private static boolean isZip(String type) {
        return type.endsWith(fileExtensionZip) || type.endsWith(fileExtensinJar)
                || type.endsWith(fileExtensionApk);
    }

    private static boolean isTar(String type) {
        return type.endsWith(fileExtensionTar);
    }

    private static boolean isGzippedTar(String type) {
        return type.endsWith(fileExtensionGzipTar);
    }

    private static boolean isRar(String type) {
        return type.endsWith(fileExtensionRar);
    }

    public static File[] listFiles(File parent) {
        if (parent != null && parent.exists() && canListFiles(parent)) {
            return parent.listFiles();
        }
        return new File[0];
    }


    /**
     * Helper method to calculate source files size
     */
    public static long getTotalBytes(ArrayList<FileItem> files) {
        long totalBytes = 0L;
        for (FileItem file : files) {
            totalBytes += getBaseFileSize(file);
        }
        return totalBytes;
    }

    public static int getTotalCounts(ArrayList<FileItem> files) {
        int totalCnt = 0;
        for (FileItem file : files) {
            totalCnt += getBaseFileCount(file);
        }
        return totalCnt;
    }

    public static boolean isStringHasCharacterSpecial(String text) {
        for (int i = 0; i < listSpecialCharacter.length; i++) {
            if (text.contains(listSpecialCharacter[i])) {
                return true;
            }
        }
        return false;
    }

    public static final String[] listSpecialCharacter = new String[]{"%", "/", "#", "^", ":", "?", ",","\\"};

    private static long getBaseFileSize(FileItem baseFile) {
        File file = new File(baseFile.path);
        if (file.isDirectory()) {
            return folderSize(file);
        } else {
            return file.length();
        }
    }

    private static int getBaseFileCount(FileItem baseFile) {
        File file = new File(baseFile.path);
        if (file.isDirectory()) {
            return folderCount(file);
        } else {
            return 1;
        }
    }

    public static long folderSize(File directory) {
        long length = 0;
        try {
            for (File file : directory.listFiles()) {
                if (file.isFile())
                    length += file.length();
                else
                    length += folderSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return length;
    }

    public static int folderCount(File directory) {
        int cnt = 0;
        try {
            for (File file : directory.listFiles()) {
                if (file.isFile())
                    cnt += 1;
                else
                    cnt += folderCount(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cnt;
    }

    /**
     * Check for a directory if it is possible to create files within this directory, either via normal writing or via
     * Storage Access Framework.
     *
     * @param folder The directory
     * @return true if it is possible to write in this directory.
     */
    public static boolean isWritableNormalOrSaf(final File folder, Context c) {
        // Verify that this is a directory.
        if (folder == null)
            return false;
        if (!folder.exists() || !folder.isDirectory()) {
            return false;
        }

        // First check regular writability
        if (folder.canWrite()) {
            return true;
        }

        // Next check SAF writability.
        DocumentFile document = getDocumentFile(c, folder, true);

        if (document == null) {
            return false;
        }

        return document.canWrite();
    }

    /**
     * Get a DocumentFile corresponding to the given file (for writing on ExtSdCard on Android 5). If the file is not
     * existing, it is created.
     *
     * @param file The file.
     * @return The DocumentFile
     */
    public static DocumentFile getDocumentFile(Context context, final File file, boolean isDirectory) {
        String baseFolder = getExtSdCardFolder(file, context);
        if (baseFolder == null) {
            return null;
        }

        String relativePath = null;
        try {
            String fullPath = file.getCanonicalPath();
            Flog.d("TEST_FOLDER", "fullPath=" + fullPath);
            if (!baseFolder.equals(fullPath))
                relativePath = fullPath.substring(baseFolder.length() + 1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        String as = SharedPrefUtil.getInstance().getString("PREF_URI", null);
        Flog.d("TEST_FOLDER", "as1=" + as);

        if (as == null) {
            return null;
        }

        Uri treeUri = Uri.parse(as);

        DocumentFile document = DocumentFile.fromTreeUri(context, treeUri);
        if (relativePath == null) {
            return document;
        }

        String[] parts = relativePath.split("\\/");

        Flog.d("TEST_FOLDER", "relativePath=" + relativePath + "_len=" + parts.length);


        for (int i = 0; i < parts.length; i++) {

            if (!document.isDirectory()) {
                break;
            }
            DocumentFile nextDocument = document.findFile(parts[i]);
            Flog.d("TEST_FOLDER", "part " + i + ": " + parts[i]);

            if (nextDocument == null) {
                if ((i < parts.length - 1) || isDirectory) {
                    nextDocument = document.createDirectory(parts[i]);
                } else {
                    nextDocument = document.createFile("image", parts[i]);
                }
            }
            document = nextDocument;
        }

//        Flog.d("TEST_FOLDER", "FIND_FILE: " + document.getName() + "_isDir=" + document.isDirectory());

        return document;
    }

    public static Uri getUriFromFile(final String path, Context context) {
        ContentResolver resolver = context.getContentResolver();

        Cursor filecursor = resolver.query(MediaStore.Files.getContentUri("external"),
                new String[]{BaseColumns._ID}, MediaStore.MediaColumns.DATA + " = ?",
                new String[]{path}, MediaStore.MediaColumns.DATE_ADDED + " desc");
        if (filecursor == null) {
            return null;
        }
        filecursor.moveToFirst();

        if (filecursor.isAfterLast()) {
            filecursor.close();
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DATA, path);
            return resolver.insert(MediaStore.Files.getContentUri("external"), values);
        } else {
            int imageId = filecursor.getInt(filecursor.getColumnIndex(BaseColumns._ID));
            Uri uri = MediaStore.Files.getContentUri("external").buildUpon().appendPath(
                    Integer.toString(imageId)).build();
            filecursor.close();
            return uri;
        }
    }

    public static int checkWriteFolder(AppCompatActivity context, final File folder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (isOnExtSdCard(folder, context)) {
                if (!folder.exists() || !folder.isDirectory()) {
                    return DOESNT_EXIST;
                }

                // On Android 5, trigger storage access framework.
                if (!isWritableNormalOrSaf(folder, context)) {
                    guideDialogForLEXA(context, folder.getPath());
                    return CAN_CREATE_FILES;
                }

                return WRITABLE_OR_ON_SDCARD;
            } else if (folder.canWrite()) {
                return WRITABLE_OR_ON_SDCARD;
            } else return DOESNT_EXIST;
        } else if (Build.VERSION.SDK_INT == 19) {
            if (isOnExtSdCard(folder, context)) {
                // Assume that Kitkat workaround works
                return WRITABLE_OR_ON_SDCARD;
            } else if (folder.canWrite()) {
                return WRITABLE_OR_ON_SDCARD;
            } else return DOESNT_EXIST;
        } else if (folder.canWrite()) {
            return WRITABLE_OR_ON_SDCARD;
        } else {
            return DOESNT_EXIST;
        }
    }

    public static int checkWriteFolderSimple(AppCompatActivity context, final File folder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (isOnExtSdCard(folder, context)) {
                if (!folder.exists() || !folder.isDirectory()) {
                    return DOESNT_EXIST;
                }

                // On Android 5, trigger storage access framework.
                if (!isWritableNormalOrSaf(folder, context)) {
//                    guideDialogForLEXA(context, folder.getPath());
                    return CAN_CREATE_FILES;
                }

                return WRITABLE_OR_ON_SDCARD;
            } else if (folder.canWrite()) {
                return WRITABLE_OR_ON_SDCARD;
            } else return DOESNT_EXIST;
        } else if (Build.VERSION.SDK_INT == 19) {
            if (isOnExtSdCard(folder, context)) {
                // Assume that Kitkat workaround works
                return WRITABLE_OR_ON_SDCARD;
            } else if (folder.canWrite()) {
                return WRITABLE_OR_ON_SDCARD;
            } else return DOESNT_EXIST;
        } else if (folder.canWrite()) {
            return WRITABLE_OR_ON_SDCARD;
        } else {
            return DOESNT_EXIST;
        }
    }

    public static void guideDialogForLEXA(final AppCompatActivity mainActivity, String path) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setTitle(R.string.needsaccess);
        LayoutInflater layoutInflater = (LayoutInflater) mainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (layoutInflater == null) {
            return;
        }
        View view = layoutInflater.inflate(R.layout.lexadrawer, null);
        builder.setView(view);
        // textView
        TextView textView = (TextView) view.findViewById(R.id.description);
        String message = mainActivity.getResources().getString(R.string.needsaccesssummary) + "\n" + path + "\n" + mainActivity.getResources().getString(R.string.needsaccesssummary1);
        textView.setText(message);
        ((ImageView) view.findViewById(R.id.icon)).setImageResource(R.drawable.sd_operate_step);
        builder.setPositiveButton(R.string.open, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FileUtils.triggerDocumentTree(mainActivity);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(mainActivity, R.string.skip, Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static boolean triggerDocumentTree(AppCompatActivity activity) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//            intent.setClassName("com.android.documentsui", "com.android.documentsui.DocumentsActivity");  // android 8 Oreo not working.
            try {
                activity.startActivityForResult(intent, ConstValue.REQUEST_CODE_OPEN_DOCUMENT_TREE);
                return true;
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static String getPath2(final Context context, final Uri uri) {
        String filePath = getPath(context, uri);
        if (filePath != null) {
            return filePath;
        } else {
            final ParcelFileDescriptor parcelFileDescriptor;
            final FileDescriptor fileDescriptor;
            FileInputStream fileInputStream = null;
            try {
                parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
                fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                fileInputStream = new FileInputStream(fileDescriptor);
                File outputFile = new File(context.getCacheDir(), "unknown.mp3");
                fileInputStreamToFile(fileInputStream, outputFile.getAbsolutePath());
                return outputFile.getAbsolutePath();
            } catch (Exception e) {
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                return null;
            }
        }
    }

    private static boolean fileInputStreamToFile(final FileInputStream fileInputStream, final String outputPath) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(outputPath);
        byte[] buffer = new byte[5 * 1024];
        int n = -1;

        while ((n = fileInputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, n);
        }

        fileOutputStream.flush();
        fileOutputStream.close();


        return true;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);

                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
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

    public static String getRealPathFromURIDocumentTree(AppCompatActivity context, Uri treeUri) {

        DocumentFile document = DocumentFile.fromTreeUri(context, treeUri);

//        for (int i = 0; i < parts.length; i++) {
//
//            if (!document.isDirectory()) {
//                break;
//            }
//            DocumentFile nextDocument = document.findFile(parts[i]);
//            Flog.d("TEST_FOLDER", "part " + i + ": " + parts[i]);
//
//            if (nextDocument == null) {
//                if ((i < parts.length - 1) || isDirectory) {
//                    nextDocument = document.createDirectory(parts[i]);
//                } else {
//                    nextDocument = document.createFile("image", parts[i]);
//                }
//            }
//            document = nextDocument;
//        }
        Flog.d(TAG, "getRealPathFromURIDocumentTree="+treeUri.getPath());
        Flog.d(TAG, "getRealPathFromURIDocumentTree="+document.getUri()+"_name="+document.getName());
        return null;
    }

    public interface OnFileFound {
        void onFileFound(FileItem file);
    }
}
