package bsoft.com.lib_filter.filter.gpu;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;

import bsoft.com.lib_filter.filter.gpu.util.BitmapUtil;
import bsoft.com.lib_filter.filter.listener.WBAsyncPostIconListener;


public abstract class WBRes {
    protected Boolean asyncIcon;
    protected Context context;
    private Bitmap iconBitmap;
    private String iconFileName;
    private int iconID;
    private LocationType iconType;
    private boolean isCircle;
    private boolean isNew;
    private boolean isSetTextBgColor;
    private boolean isShowText;
    private String managerName;
    private String name;
    private String showText;
    private int textBgColor;
    private int textColor;

    public enum LocationType {
        RES,
        ASSERT,
        FILTERED,
        ONLINE,
        CACHE
    }

    public WBRes() {
        this.asyncIcon = Boolean.valueOf(false);
        this.isNew = false;
        this.isShowText = false;
        this.textColor = 0;
        this.textBgColor = 0;
        this.isSetTextBgColor = false;
        this.isCircle = false;
    }

    public void setIsShowText(boolean flag) {
        this.isShowText = flag;
    }

    public Boolean getIsShowText() {
        return Boolean.valueOf(this.isShowText);
    }

    public void setShowText(String showText) {
        this.showText = showText;
    }

    public String getShowText() {
        return this.showText;
    }

    public int getTextColor() {
        return this.textColor;
    }

    public void setTextBgColor(int color) {
        setTextBgColor(color, true);
    }

    public void setTextBgColor(int color, boolean isSet) {
        this.textBgColor = color;
        this.isSetTextBgColor = isSet;
    }

    public boolean isSetTextBgColor() {
        return this.isSetTextBgColor;
    }

    public int getTextBgColor() {
        return this.textBgColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public boolean isCircle() {
        return this.isCircle;
    }

    public void setCircle(boolean isCircle) {
        this.isCircle = isCircle;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Resources getResources() {
        if (this.context != null) {
            return this.context.getResources();
        }
        return null;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconFileName() {
        return this.iconFileName;
    }

    public void setIconFileName(String icon) {
        this.iconFileName = icon;
    }

    public int getIconID() {
        return this.iconID;
    }

    public void setIconID(int id) {
        this.iconID = id;
    }

    public LocationType getIconType() {
        return this.iconType;
    }

    public void setIconType(LocationType iconType) {
        this.iconType = iconType;
    }

    public String getType() {
        return "TRes";
    }

    public Boolean getAsyncIcon() {
        return this.asyncIcon;
    }

    public void getAsyncIconBitmap(WBAsyncPostIconListener listener) {

    }

    public Bitmap getIconBitmap() {
        if (this.iconFileName == null) {
            return null;
        }
        if (this.iconType == LocationType.RES) {
            return BitmapUtil.getImageFromResourceFile(getResources(), this.iconID);
        }
        if (this.iconType == LocationType.ASSERT) {
            return BitmapUtil.getImageFromAssetsFile(getResources(), this.iconFileName);
        }
        return this.iconBitmap;
    }

    public boolean getIsNewValue() {
        return this.isNew;
    }

    public void setIsNewValue(boolean value) {
        this.isNew = value;
    }

    public String getManagerName() {
        return this.managerName;
    }

    public void setManagerName(String name) {
        this.managerName = name;
    }
}