package com.photo.gallery.taskes;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;
import android.util.Log;
import android.widget.Toast;

import com.photo.gallery.R;
import com.photo.gallery.utils.FileUtils;
import com.photo.gallery.utils.Flog;
import com.photo.gallery.utils.MimeTypes;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by hoa on 5/18/2018.
 */

public class FileHelper {

    public static final int NONE = 0x10;
    public static final int COPY = 0x11;
    public static final int MOVE = 0x12;
    public static final int DELETE = 0x13;
    public static final int NEW_FOLDER = 0x14;
    public static final int RESTORE = 0x15;
    public static final int RENAME = 0x16;
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    private static final int DEFAULT_BUFFER_SIZE_COPY = 1024 * 1024;
    private static final java.lang.String TAG = FileHelper.class.getSimpleName();
    private static final String ALBUM_ART_URI = "content://media/external/audio/albumart";
    private static final String[] ALBUM_PROJECTION = {
            BaseColumns._ID, MediaStore.Audio.AlbumColumns.ALBUM_ID, "media_type"
    };
    private static final boolean FLAG_OLD_COPY = false;
    private static long cntBytes = 0;

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

    /**
     * Starts copy of file
     * Supports : {@link File}, {@link DocumentFile}
     *
     * @param lowOnMemory defines whether system is running low on memory, in which case we'll switch to
     *                    using streams instead of channel which maps the who buffer in memory.
     *                    TODO: Use buffers even on low memory but don't map the whole file to memory but
     *                    parts of it, and transfer each part instead.
     * @throws IOException
     */
    public static boolean startCopy(Context context, boolean lowOnMemory, File srcFile, File dstFile) {

        boolean isOverwrite = (srcFile.equals(dstFile));
        if (isOverwrite) {
            return true;
        }

        boolean copied = false;

        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;

        InputStream myInputStream = null;
        OutputStream myOutStream = null;

        try {

            // source file is neither smb nor otg; getting a channel from direct file instead of stream
            File file = new File(srcFile.getPath());
            if (srcFile.canRead()) {

                if (FLAG_OLD_COPY) {
                    if (lowOnMemory) {
                        // our target is cloud, we need a stream not channel
                        bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                    } else {

                        inChannel = new RandomAccessFile(file, "r").getChannel();
                    }
                } else {
                    myInputStream = new FileInputStream(file.getAbsolutePath());
                }
            } else {

                ContentResolver contentResolver = context.getContentResolver();
                DocumentFile documentSourceFile = FileUtils.getDocumentFile(context, file, file.isDirectory());
                if (FLAG_OLD_COPY) {
                    if (documentSourceFile != null) {
                        bufferedInputStream = new BufferedInputStream(contentResolver
                                .openInputStream(documentSourceFile.getUri()), DEFAULT_BUFFER_SIZE);
                    }
                } else {
                    myInputStream = contentResolver.openInputStream(documentSourceFile.getUri());
                }
            }

            // initializing the output channels based on file types
            // copying normal dstF, target not in OTG
            File dstF = new File(dstFile.getPath());
            if (dstF.getParentFile().canWrite()) {

                if (lowOnMemory) {
                    bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(dstF));
                } else {
                    outChannel = new RandomAccessFile(dstF, "rw").getChannel();
                }

                myOutStream = new FileOutputStream(dstF.getAbsolutePath());
            } else {
                ContentResolver contentResolver = context.getContentResolver();
                DocumentFile documentTargetFile = FileUtils.getDocumentFile(context, dstF, dstF.isDirectory());

                Flog.d("TEST_FOLDER", "contentResolver=" + contentResolver + "_documentTargetFile=" + documentTargetFile);
                if (documentTargetFile != null) {
                    bufferedOutputStream = new BufferedOutputStream(contentResolver
                            .openOutputStream(documentTargetFile.getUri()), DEFAULT_BUFFER_SIZE);
                }

                myOutStream = contentResolver.openOutputStream(documentTargetFile.getUri());
            }

            if (FLAG_OLD_COPY) {
                if (bufferedInputStream != null) {
                    if (bufferedOutputStream != null)
                        copyFile(bufferedInputStream, bufferedOutputStream);
                    else if (outChannel != null) {
                        copyFile(bufferedInputStream, outChannel, srcFile);
                    }
                } else if (inChannel != null) {
                    if (bufferedOutputStream != null) {
                        copyFile(inChannel, bufferedOutputStream, srcFile);
                    } else if (outChannel != null) copyFile(inChannel, outChannel, srcFile);
                }
            } else {

                byte[] buffer = new byte[DEFAULT_BUFFER_SIZE_COPY];
                int read;
                while ((read = myInputStream.read(buffer)) != -1) {

                    cntBytes += read;
                    Flog.d(TAG, "copying : bytes=" + cntBytes + " / " + srcFile.length()
                            + "_percent=" + ((cntBytes * 100f) / srcFile.length()));
                    myOutStream.write(buffer, 0, read);
                }
                myInputStream.close();
                myInputStream = null;

                // write the output file (You have now copied the file)
                myOutStream.flush();
                myOutStream.close();
                myOutStream = null;
            }
            copied = true;
        } catch (IOException e) {
            e.printStackTrace();
            Flog.d(TAG, "I/O Error!");
            copied = false;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();

            // we ran out of memory to map the whole channel, let's switch to streams
            Toast.makeText(context, context.getResources().getString(R.string.copy_low_memory), Toast.LENGTH_LONG).show();

            startCopy(context, true, srcFile, dstFile);
        } finally {

            try {
                if (inChannel != null) inChannel.close();
                if (outChannel != null) outChannel.close();
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
                if (bufferedInputStream != null) bufferedInputStream.close();
                if (bufferedOutputStream != null) bufferedOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                // failure in closing stream
                copied = false;
            }
        }

        if (copied) {
            ContentValues conValues = new ContentValues();
            conValues.put("_display_name", dstFile.getName());
            conValues.put("_data", dstFile.getAbsolutePath());

            Uri externalUri = MediaStore.Files.getContentUri("external");
            boolean successMediaStore = context.getContentResolver().insert(
                    externalUri, conValues) != null;
            Flog.d(TAG, "successMediaStore copy=" + successMediaStore + "_dst=" + dstFile.getAbsolutePath());
            scanMediaStore(context, srcFile.getAbsolutePath(), dstFile.getAbsolutePath());
        }
        return copied;
    }

    private static void copyFile(BufferedInputStream bufferedInputStream, FileChannel outChannel, File srcFile)
            throws IOException {

        MappedByteBuffer byteBuffer = outChannel.map(FileChannel.MapMode.READ_WRITE, 0,
                srcFile.length());
        int count = 0;
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        while (count != -1) {

            count = bufferedInputStream.read(buffer);
            if (count != -1) {

                byteBuffer.put(buffer, 0, count);
            }
        }
    }

    private static void copyFile(FileChannel inChannel, FileChannel outChannel, File srcFile) throws IOException {

        //MappedByteBuffer inByteBuffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
        //MappedByteBuffer outByteBuffer = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());

        ReadableByteChannel inByteChannel = new CustomReadableByteChannel(inChannel);
        outChannel.transferFrom(inByteChannel, 0, srcFile.length());
    }

    private static void copyFile(BufferedInputStream bufferedInputStream, BufferedOutputStream bufferedOutputStream)
            throws IOException {
        int count = 0;
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];

        while (count != -1) {

            count = bufferedInputStream.read(buffer);
            if (count != -1) {

                bufferedOutputStream.write(buffer, 0, count);
            }
        }
        bufferedOutputStream.flush();
    }

    private static void copyFile(FileChannel inChannel, BufferedOutputStream bufferedOutputStream, File srcFile)
            throws IOException {
        MappedByteBuffer inBuffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, srcFile.length());

        int count = -1;
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        while (inBuffer.hasRemaining() && count != 0) {

            int tempPosition = inBuffer.position();

            try {

                // try normal way of getting bytes
                ByteBuffer tempByteBuffer = inBuffer.get(buffer);
                count = tempByteBuffer.position() - tempPosition;
            } catch (BufferUnderflowException exception) {
                exception.printStackTrace();

                // not enough bytes left in the channel to read, iterate over each byte and store
                // in the buffer

                // reset the counter bytes
                count = 0;
                for (int i = 0; i < buffer.length && inBuffer.hasRemaining(); i++) {
                    buffer[i] = inBuffer.get();
                    count++;
                }
            }

            if (count != -1) {

                bufferedOutputStream.write(buffer, 0, count);
            }

        }
        bufferedOutputStream.flush();
    }

    public static boolean move(File srcFile, File dstFile) {
        return srcFile.renameTo(dstFile);
    }

    /**
     * Delete a folder.
     *
     * @param file The folder name.
     * @return true if successful.
     */
    private static boolean rmdir(@NonNull final File file, Context context) {
        if (!file.exists()) return true;

        File[] files = file.listFiles();
        if (files != null && files.length > 0) {
            for (File child : files) {
                rmdir(child, context);
            }
        }

        // Try the normal way
        if (file.delete()) {
            return true;
        }

        // Try with Storage Access Framework.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            DocumentFile document = FileUtils.getDocumentFile(context, file, true);
            if (document != null && document.delete()) {
                return true;
            }
        }

        // Try the Kitkat workaround.
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            ContentResolver resolver = context.getContentResolver();
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
            resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            // Delete the created entry, such that content provider will delete the file.
            resolver.delete(MediaStore.Files.getContentUri("external"), MediaStore.MediaColumns.DATA + "=?",
                    new String[]{file.getAbsolutePath()});
        }

        return !file.exists();
    }

    /**
     * Delete a file. May be even on external SD card.
     *
     * @param file the file to be deleted.
     * @return True if successfully deleted.
     */
    public static boolean deleteFile(Context context, @NonNull final File file) {
        boolean deleted = false;
        // First try the normal deletion.
        boolean fileDelete = rmdir(file, context);
        if (file.delete() || fileDelete) {
            deleted = true;
        } else {
            // Try with Storage Access Framework.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && FileUtils.isOnExtSdCard(file, context)) {

                DocumentFile document = FileUtils.getDocumentFile(context, file, false);
                deleted = (document != null && document.delete());
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {  // Try the Kitkat workaround.

                ContentResolver resolver = context.getContentResolver();
                try {
                    Uri uri = FileUtils.getUriFromFile(file.getAbsolutePath(), context);
                    if (uri == null) {
                        deleted = false;
                    } else {

                        resolver.delete(uri, null, null);
                        deleted = !file.exists();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Flog.d(TAG, "Error when deleting file " + file.getAbsolutePath());
                    deleted = false;
                }
            } else {
                deleted = !file.exists();
            }
        }

        if (deleted) {
            ContentResolver contentResolver = context.getContentResolver();
            Uri uri = MediaStore.Files.getContentUri("external");
            boolean successMediaStore = contentResolver.delete(uri,
                    MediaStore.Files.FileColumns.DATA + "=?",
                    new String[]{file.getAbsolutePath()}) == 1;
            Flog.d(TAG, "successMediaStore delete= 2_" + successMediaStore);
            scanMediaStore(context, file.getAbsolutePath());
        }
        return deleted;
    }


    public static String getMessageListFile(ArrayList<File> list) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            File file = list.get(i);
            Flog.d(TAG, "failed " + i + "_" + file.getPath());

            String id = String.valueOf(i + 1);
            builder.append(id);
            builder.append(". ");
            builder.append(file.getName());
            builder.append("\n");
        }
        return builder.toString();
    }


    /**
     * Deletes the file. Returns true if the file has been successfully deleted or otherwise does
     * not exist. This operation is not recursive.
     */
    private static boolean delete(final Context context, final File file) {
        final String where = MediaStore.MediaColumns.DATA + "=?";
        final String[] selectionArgs = new String[]{
                file.getAbsolutePath()
        };
        final ContentResolver contentResolver = context.getContentResolver();
        final Uri filesUri = MediaStore.Files.getContentUri("external");
        // Delete the entry from the media database. This will actually delete media files.
        contentResolver.delete(filesUri, where, selectionArgs);
        // If the file is not a media file, create a new entry.
        if (file.exists()) {
            final ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            // Delete the created entry, such that content provider will delete the file.
            contentResolver.delete(filesUri, where, selectionArgs);
        }
        return !file.exists();
    }

    private static File getExternalFilesDir(final Context context) {
        return context.getExternalFilesDir(null);
    }

    private static File installTemporaryTrack(final Context context) throws IOException {
        final File externalFilesDir = getExternalFilesDir(context);
        if (externalFilesDir == null) {
            return null;
        }
        final File temporaryTrack = new File(externalFilesDir, "temptrack.mp3");
        if (!temporaryTrack.exists()) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = context.getResources().openRawResource(R.raw.temptrack);
                out = new FileOutputStream(temporaryTrack);
                final byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            } finally {
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
            }
        }
        return temporaryTrack;
    }

    /**
     * Returns an OutputStream to write to the file. The file will be truncated immediately.
     */
    private static int getTemporaryAlbumId(final Context context) {
        final File temporaryTrack;
        try {
            temporaryTrack = installTemporaryTrack(context);
        } catch (final IOException ex) {
            Log.w("MediaFile", "Error installing tempory track.", ex);
            return 0;
        }
        final Uri filesUri = MediaStore.Files.getContentUri("external");
        final String[] selectionArgs = {
                temporaryTrack.getAbsolutePath()
        };
        final ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(filesUri, ALBUM_PROJECTION,
                MediaStore.MediaColumns.DATA + "=?", selectionArgs, null);
        if (cursor == null || !cursor.moveToFirst()) {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
            final ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DATA, temporaryTrack.getAbsolutePath());
            values.put(MediaStore.MediaColumns.TITLE, "{MediaWrite Workaround}");
            values.put(MediaStore.MediaColumns.SIZE, temporaryTrack.length());
            values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mpeg");
            values.put(MediaStore.Audio.AudioColumns.IS_MUSIC, true);
            contentResolver.insert(filesUri, values);
        }
        cursor = contentResolver.query(filesUri, ALBUM_PROJECTION, MediaStore.MediaColumns.DATA
                + "=?", selectionArgs, null);
        if (cursor == null) {
            return 0;
        }
        if (!cursor.moveToFirst()) {
            cursor.close();
            return 0;
        }
        final int id = cursor.getInt(0);
        final int albumId = cursor.getInt(1);
        final int mediaType = cursor.getInt(2);
        cursor.close();
        final ContentValues values = new ContentValues();
        boolean updateRequired = false;
        if (albumId == 0) {
            values.put(MediaStore.Audio.AlbumColumns.ALBUM_ID, 13371337);
            updateRequired = true;
        }
        if (mediaType != 2) {
            values.put("media_type", 2);
            updateRequired = true;
        }
        if (updateRequired) {
            contentResolver.update(filesUri, values, BaseColumns._ID + "=" + id, null);
        }
        cursor = contentResolver.query(filesUri, ALBUM_PROJECTION, MediaStore.MediaColumns.DATA
                + "=?", selectionArgs, null);
        if (cursor == null) {
            return 0;
        }
        try {
            if (!cursor.moveToFirst()) {
                return 0;
            }
            return cursor.getInt(1);
        } finally {
            cursor.close();
        }
    }


    private static boolean mkdir(final Context context, final File file) throws IOException {
        if (file.exists()) {
            return file.isDirectory();
        }
        final File tmpFile = new File(file, ".MediaWriteTemp");
        final int albumId = getTemporaryAlbumId(context);
        if (albumId == 0) {
            throw new IOException("Failed to create temporary album id.");
        }
        final Uri albumUri = Uri.parse(String.format(Locale.US, ALBUM_ART_URI + "/%d", albumId));
        final ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, tmpFile.getAbsolutePath());
        final ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver.update(albumUri, values, null, null) == 0) {
            values.put(MediaStore.Audio.AlbumColumns.ALBUM_ID, albumId);
            contentResolver.insert(Uri.parse(ALBUM_ART_URI), values);
        }
        try {
            final ParcelFileDescriptor fd = contentResolver.openFileDescriptor(albumUri, "r");
            if (fd == null) {
                return false;
            }
            fd.close();
        } finally {
            delete(context, tmpFile);
        }
        return file.exists();
    }

    /**
     * Create a folder. The folder may even be on external SD card for Kitkat.
     *
     * @param file The folder to be created.
     * @return True if creation was successful.
     */
    public static boolean mkdirs(Context context, final File file) {
        if (file == null)
            return false;
        if (file.exists()) {
            // nothing to create.
            return file.isDirectory();
        }

        // Try the normal way
        if (file.mkdirs()) {
            return true;
        }

        // Try with Storage Access Framework.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && FileUtils.isOnExtSdCard(file, context)) {
            DocumentFile document = FileUtils.getDocumentFile(context, file, true);
            if (document == null) {
                return false;
            }
            // getDocumentFile implicitly creates the directory.
            return document.exists();
        }

        // Try the Kitkat workaround.
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            try {
                return mkdir(context, file);
            } catch (IOException e) {
                return false;
            }
        }

        return false;
    }

    public static boolean mkfiles(Context context, final File file) throws IOException {
        if (file == null)
            return false;
        if (file.exists()) {
            // nothing to create.
            return !file.isDirectory();
        }

        // Try the normal way
        try {
            if (file.createNewFile()) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Try with Storage Access Framework.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && FileUtils.isOnExtSdCard(file, context)) {
            DocumentFile document = FileUtils.getDocumentFile(context, file.getParentFile(), true);
            Flog.d(TAG, "document=" + document + "_path=" + file.getAbsolutePath());
            if (document == null) {
                return false;
            }
            // getDocumentFile implicitly creates the directory.
            try {
                return document.createFile(MimeTypes.getMimeType(file.getPath(), file.isDirectory()), file.getName()) != null;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            try {
                return mkfile(context, file);
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    private static boolean mkfile(final Context context, final File file) {
        final OutputStream outputStream = getOutputStream(context, file.getPath());
        if (outputStream == null) {
            return false;
        }
        try {
            outputStream.close();
            return true;
        } catch (final IOException e) {
        }
        return false;
    }

    public static OutputStream getOutputStream(Context context, String str) {
        OutputStream outputStream = null;
        Uri fileUri = getUriFromFile(str, context);
        if (fileUri != null) {
            try {
                outputStream = context.getContentResolver().openOutputStream(fileUri);
            } catch (Throwable th) {
            }
        }
        return outputStream;
    }

    public static Uri getUriFromFile(final String path, Context context) {
        ContentResolver resolver = context.getContentResolver();

        Cursor filecursor = resolver.query(MediaStore.Files.getContentUri("external"),
                new String[]{BaseColumns._ID}, MediaStore.MediaColumns.DATA + " = ?",
                new String[]{path}, MediaStore.MediaColumns.DATE_ADDED + " desc");
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

    private static boolean rename(File f, String name) {
        String newPath = f.getParent() + "/" + name;
        if (f.getParentFile().canWrite()) {
            return f.renameTo(new File(newPath));
        }
        return false;
    }

    /**
     * Rename a folder. In case of extSdCard in Kitkat, the old folder stays in place, but files are moved.
     *
     * @param source The source folder.
     * @param target The target folder.
     * @return true if the renaming was successful.
     */
    public static boolean renameFolder(Context context, @NonNull final File source, @NonNull final File target) {

        boolean renamed = false;
        // First try the normal rename.
        if (rename(source, target.getName())) {

            renamed = true;
        } else if (!target.exists()) {

            // Try the Storage Access Framework if it is just a rename within the same parent folder.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                    && source.getParent().equals(target.getParent()) && FileUtils.isOnExtSdCard(source, context)) {
                DocumentFile document = FileUtils.getDocumentFile(context, source, true);
                if (document != null && document.renameTo(target.getName())) {
                    renamed = true;
                }
            }
        }

        if (renamed) {
            ContentValues conValues = new ContentValues();
            conValues.put("_display_name", target.getName());
            conValues.put("_data", target.getAbsolutePath());

            Uri externalUri = MediaStore.Files.getContentUri("external");
            boolean successMediaStore = context.getContentResolver().update(
                    externalUri, conValues,
                    MediaStore.MediaColumns.DATA + "='" + source.getAbsolutePath() + "'", null) == 1;
            Flog.d(TAG, "successMediaStore rename= 2_" + successMediaStore);

            scanMediaStore(context, source.getAbsolutePath(), target.getAbsolutePath());
        }

        return renamed;
    }



    /**
     * https://stackoverflow.com/questions/15487399/refresh-the-gallery-after-deleting-an-image-file
     * (non-Javadoc)
     *
     * @see android.media.MediaScannerConnection.OnScanCompletedListener#onScanCompleted(java.lang.String, android.net.Uri)
     */
    public static void scanMediaStore(Context context, final String... paths) {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {

                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                        Uri.parse("file://" + Environment.getExternalStorageDirectory())));
            } else {

                MediaScannerConnection.scanFile(context, paths, null, new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Flog.d("ExternalStorage12", "Scanned " + path + ":");
                        Flog.d("ExternalStorage12", "-> uri=" + uri);
                    }
                });
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Flog.d("scanMediaStore failed");
        }
    }

    /**
     * Inner class responsible for getting a {@link ReadableByteChannel} from the input channel
     * and to watch over the read progress
     */
    private static class CustomReadableByteChannel implements ReadableByteChannel {

        ReadableByteChannel byteChannel;

        CustomReadableByteChannel(ReadableByteChannel byteChannel) {
            this.byteChannel = byteChannel;
        }

        @Override
        public int read(ByteBuffer dst) throws IOException {
            int bytes;
            if (((bytes = byteChannel.read(dst)) > 0)) {

                return bytes;

            }
            return 0;
        }

        @Override
        public boolean isOpen() {
            return byteChannel.isOpen();
        }

        @Override
        public void close() throws IOException {

            byteChannel.close();
        }
    }
}
