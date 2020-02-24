package com.photo.gallery.callback;

import com.photo.gallery.models.FileItem;

/**
 * Created by Tung on 3/28/2018.
 */

public interface OnFileDialogEventListener {

    void onOk(FileItem... items);
}
