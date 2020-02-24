package bsoft.healthy.tracker.menstrual.lib_sticker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import bsoft.healthy.tracker.menstrual.lib_sticker.R;
import bsoft.healthy.tracker.menstrual.lib_sticker.main.TextStickerView;

/**
 * Created by nam on 4/5/2017.
 */

public class TextMoreAdjustFragment extends BaseStickerFragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    public static final int MAX_TEXT_SIZE = 50;
    public static final int MIN_TEXT_SIZE = 12;
    public static final int MAX_TEXT_PADDING = 100;
    public static final int MIN_TEXT_PADDING = 0;

    private static final String TAG = TextMoreAdjustFragment.class.getSimpleName();
    private SeekBar sizeSeek, paddingSeek;
    private OnTabAlignTextListener mOnTabAlignTextListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_text_more_adjust, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sizeSeek = (SeekBar) getView().findViewById(R.id.sb_size_text);
//        sizeSeek.setMin(MIN_TEXT_SIZE);
        sizeSeek.setMax(MAX_TEXT_SIZE-MIN_TEXT_SIZE);
        sizeSeek.setProgress(TextStickerView.getDefaultTextSize(mContext));
        sizeSeek.setOnSeekBarChangeListener(this);

        paddingSeek = (SeekBar) getView().findViewById(R.id.sb_padding_text);
//        paddingSeek.setMin(MIN_TEXT_PADDING);
        paddingSeek.setMax(MAX_TEXT_PADDING-MIN_TEXT_PADDING);
//        paddingSeek.setProgress(MIN_TEXT_PADDING);
        paddingSeek.setOnSeekBarChangeListener(this);

        view.findViewById(R.id.ic_left_alignment).setOnClickListener(this);
        view.findViewById(R.id.ic_center_alignment).setOnClickListener(this);
        view.findViewById(R.id.ic_right_alignment).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ic_left_alignment) {
            if (mOnTabAlignTextListener != null)
                mOnTabAlignTextListener.onAlignTextLeftChanged();

        } else if (id == R.id.ic_center_alignment) {
            if (mOnTabAlignTextListener != null)
                mOnTabAlignTextListener.onAlignTextCenterChanged();

        } else if (id == R.id.ic_right_alignment) {
            if (mOnTabAlignTextListener != null)
                mOnTabAlignTextListener.onAlignTextRightChanged();

        } else {
        }
    }

    public TextMoreAdjustFragment setListener(OnTabAlignTextListener onTabAlignTextListener) {
        mOnTabAlignTextListener = onTabAlignTextListener;
        return this;
    }

    public void setSizeTextProgress(int sizeText) {
        sizeSeek.setProgress(sizeText);
    }

    public void setPaddingTextProgress(int paddingText) {
        paddingSeek.setProgress(paddingText);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int i = seekBar.getId();
        if (i == R.id.sb_padding_text) {
            if (mOnTabAlignTextListener != null)
                mOnTabAlignTextListener.onPaddingTextChanged(progress+MIN_TEXT_PADDING);

        } else if (i == R.id.sb_size_text) {
            if (mOnTabAlignTextListener != null)
                mOnTabAlignTextListener.onSizeTextChanged(progress+MIN_TEXT_SIZE);

        } else {
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void resetSeekbar() {
        sizeSeek.setProgress(TextStickerView.getDefaultTextSize(mContext));
        paddingSeek.setProgress(0);
    }

    public interface OnTabAlignTextListener {
        void onAlignTextLeftChanged();

        void onAlignTextCenterChanged();

        void onAlignTextRightChanged();

        void onPaddingTextChanged(int value);

        void onSizeTextChanged(int value);
    }
}

