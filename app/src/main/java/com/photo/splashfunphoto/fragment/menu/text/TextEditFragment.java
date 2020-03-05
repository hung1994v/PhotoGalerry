package com.photo.splashfunphoto.fragment.menu.text;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;

import com.photo.gallery.R;
import com.photo.splashfunphoto.ui.custtom.text.ItemBubbleTextView;
import com.photo.splashfunphoto.utils.Statics;


public class TextEditFragment extends TextBaseFragment implements SeekBar.OnSeekBarChangeListener {
    private static final String TAG = TextEditFragment.class.getSimpleName();
    private AppCompatSeekBar sizeSeek;
    private AppCompatSeekBar paddingSeek;
    private OnTextEditListener mOnTextEditListener;

    public static TextEditFragment newInstance(OnTextEditListener listener) {
        TextEditFragment textEditFragment = new TextEditFragment();
        textEditFragment.mOnTextEditListener = listener;
        return textEditFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.text_edit_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        sizeSeek = (AppCompatSeekBar) getView().findViewById(R.id.sb_size_text);
        paddingSeek = (AppCompatSeekBar) getView().findViewById(R.id.sb_padding_text);

        sizeSeek.setThumb(getResources().getDrawable(R.drawable.ic_oval));
        sizeSeek.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);


        paddingSeek.setThumb(getResources().getDrawable(R.drawable.ic_oval));
        paddingSeek.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);

        sizeSeek.setMax(ItemBubbleTextView.MAX_FONT_SIZE - ItemBubbleTextView.MIN_FONT_SIZE);
        sizeSeek.setProgress((int) getArguments().getFloat(MenuTextFragment.TEXT_SIZE_KEY, ItemBubbleTextView.M_DEFULT_SIZE) - ItemBubbleTextView.MIN_FONT_SIZE);

        paddingSeek.setMax(Statics.MAX_PADDING_SIZE);
        paddingSeek.setProgress((int) getArguments().getFloat(MenuTextFragment.PADDING_TEXT_KEY, 0));

        sizeSeek.setOnSeekBarChangeListener(this);
        paddingSeek.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        int id = seekBar.getId();
        switch (id) {
            case R.id.sb_padding_text:
                if (mOnTextEditListener != null) {
                    mOnTextEditListener.onTextPaddingChangeListener(i);
                }
                break;
            case R.id.sb_size_text:
                if (mOnTextEditListener != null) {
                    mOnTextEditListener.onTextSizeChangeListener(i + ItemBubbleTextView.MIN_FONT_SIZE);
                }
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public interface OnTextEditListener{
        public void onTextSizeChangeListener(int unit);

        public void onTextPaddingChangeListener(int unit);
    }
}
