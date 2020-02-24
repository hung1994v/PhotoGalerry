package bsoft.healthy.tracker.menstrual.lib_sticker.interfaces;

import android.graphics.Typeface;

/**
 * Created by Hoavt on 12/12/2017.
 */

public interface OnTextListener {

    void onTextFontChanged(Typeface typeface);
    void onTextBgColorChanged(int color);
    void onTextBgPatternChanged(String name);
    void onTextSizeChanged(int size);
    void onTextPaddingChanged(int padding);
    void onTextAlignLeftChanged();
    void onTextAlignCenterChanged();
    void onTextAlignRightChanged();

    void onTextOpacitySeeked(int progress);

    void onTextRotateChanged(int position);

    void onTextMirrorChanged(int position);

    void onTextMoveUpChanged(int position);

    void onTextMoveDownChanged(int position);

    void onTextMoveLeftChanged(int position);

    void onTextMoveRightChanged(int position);

    void onTextZoomInChanged(int position);

    void onTextZoomOutChanged(int position);
}
