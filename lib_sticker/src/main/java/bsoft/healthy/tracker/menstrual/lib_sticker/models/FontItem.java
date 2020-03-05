package bsoft.healthy.tracker.menstrual.lib_sticker.models;

public class FontItem {
    private String fontPath;
    private String fontName;

    public FontItem(String fontPath, String fontName) {
        this.fontPath = fontPath;
        this.fontName = fontName;
    }

    public String getFontPath() {
        return fontPath;
    }

    public void setFontPath(String fontPath) {
        this.fontPath = fontPath;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }
}
