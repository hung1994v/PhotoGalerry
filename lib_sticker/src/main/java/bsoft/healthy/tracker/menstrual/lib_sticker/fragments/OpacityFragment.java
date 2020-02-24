package bsoft.healthy.tracker.menstrual.lib_sticker.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import bsoft.healthy.tracker.menstrual.lib_sticker.R;
import bsoft.healthy.tracker.menstrual.lib_sticker.main.TextStickerView;

/**
 * Created by Hoavt on 12/12/2017.
 */

public class OpacityFragment extends BaseStickerFragment {


    private SeekBar seekbar;
    private int curID = -1;
    private OnOpacityListener listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_seekbar, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {
        TextView tvLabel = (TextView) view.findViewById(R.id.tv_label);
        tvLabel.setText(getString(R.string.opacity));

        seekbar = (SeekBar) view.findViewById(R.id.seekbar);
        seekbar.setMax(255 - TextStickerView.MIN_OPACITY_VALUE);
//        seekbar.setMin(20);
        seekbar.setProgress(seekbar.getMax());

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (listener != null) {
                    listener.onOpacityChanged(TextStickerView.MIN_OPACITY_VALUE + progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // DO NOTHING.
            }
        });
    }

    public OpacityFragment setListener(OnOpacityListener listener) {
        this.listener = listener;
        return this;
    }

    public void setCurrentValueOfItem(int value) {
        seekbar.setProgress(value);
    }

    public int getFragmentID() {
        return curID;
    }

    public void setFragmentID(int fragmentID) {
        this.curID = fragmentID;
    }

    public void resetSeekbar() {
        seekbar.setProgress(seekbar.getMax());
    }

    public interface OnOpacityListener {
        void onOpacityChanged(int progress);
    }
}