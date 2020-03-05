package com.photo.gallery.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.photo.gallery.utils.DateUtils;
import com.photo.gallery.utils.Utils;

/**
 * Created by Hoavt on 3/15/2018.
 */

public class FileItem implements Parcelable {

    public static final Creator<FileItem> CREATOR = new Creator<FileItem>() {
        @Override
        public FileItem createFromParcel(Parcel in) {
            return new FileItem(in);
        }

        @Override
        public FileItem[] newArray(int size) {
            return new FileItem[size];
        }
    };

    public boolean isAds = false;
    public String _ID = null;
    public String folder_ID = null;
    public String date_modified = null;
    public String height = null;
    public String width = null;
    public String mime_type = null;
    public long mSize = 0;
    public String path = null;
    public String name = null;
    public String folder = null;
    public String orientation = null;
    public String duration = null;
    public boolean isImage = false;
    public boolean isSelected = false; // only used in adapter for click event.

    public FileItem() {
    }

    public FileItem(boolean isAds) {
        this.isAds = isAds;
    }

    public FileItem(Parcel in) {
        _ID = in.readString();
        folder_ID = in.readString();
        date_modified = in.readString();
        height = in.readString();
        width = in.readString();
        mime_type = in.readString();
        mSize = in.readLong();
        path = in.readString();
        name = in.readString();
        folder = in.readString();
        orientation = in.readString();
        duration = in.readString();
        isImage = in.readByte() != 0;
    }

    @Override
    public String toString() {
        return FileItem.class.getSimpleName() +
                "\nID=" + _ID + "\nFolderID=" + folder_ID + "\ndate_modified=" + date_modified +
                "\ndate_format=" + DateUtils.getDate(Utils.parseLong(date_modified), DateUtils.FORMAT_DATE_1) +
                "\nheight=" + height + "\nwidth=" + width +
                "\nmime_type=" + mime_type + "\nsize=" + mSize + "\npath=" + path + "\nname=" + name + "\nfolder=" + folder +
                "\norientation=" + orientation + "\nisImage=" + isImage + "\nduration=" + duration + "\n";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(_ID);
        parcel.writeString(folder_ID);
        parcel.writeString(date_modified);
        parcel.writeString(height);
        parcel.writeString(width);
        parcel.writeString(mime_type);
        parcel.writeLong(mSize);
        parcel.writeString(path);
        parcel.writeString(name);
        parcel.writeString(folder);
        parcel.writeString(orientation);
        parcel.writeString(duration);
        parcel.writeByte((byte) (isImage ? 1 : 0));
    }

    public FileItem copy() {
        FileItem newItem = new FileItem();
        newItem._ID = _ID;
        newItem.folder_ID = folder_ID;
        newItem.date_modified = date_modified;
        newItem.height = height;
        newItem.width = width;
        newItem.mime_type = mime_type;
        newItem.mSize = mSize;
        newItem.path = path;
        newItem.name = name;
        newItem.folder = folder;
        newItem.orientation = orientation;
        newItem.duration = duration;
        newItem.isImage = isImage;
        return newItem;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FileItem)) {
            return false;
        }
        FileItem other = (FileItem) obj;
        return other.path.equals(this.path);
    }
}
