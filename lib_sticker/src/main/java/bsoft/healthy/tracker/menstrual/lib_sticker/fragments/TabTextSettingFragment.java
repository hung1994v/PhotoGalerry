package bsoft.healthy.tracker.menstrual.lib_sticker.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import bsoft.healthy.tracker.menstrual.lib_sticker.R;
import bsoft.healthy.tracker.menstrual.lib_sticker.main.TextStickerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabTextSettingFragment extends BaseFragment {
    private RecyclerView rvColor, rvPatern;
    private int[] listColorPick;
    private ArrayList<String> mListPattern1  = new ArrayList<>();
    private ArrayList<String> mListPattern2  = new ArrayList<>();
    private ListColorAdapter adapterColor;
    private ListPaternAdapter adapterPatern;
    private int progressPadding;
    private SeekBar seekBarPadding;

    public static final int SIZE_PATTERN_TEXT = 28;


    public static TabTextSettingFragment newInstance(int progressPadding) {
        TabTextSettingFragment fragment = new TabTextSettingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.progressPadding = progressPadding;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_text_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        rvColor = view.findViewById(R.id.rvTextColor);
        rvPatern = view.findViewById(R.id.rvTextPattern);
        seekBarPadding = view.findViewById(R.id.seekbarTextPadding);


        adapterColor = new ListColorAdapter();
        rvColor.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvColor.setAdapter(adapterColor);

        adapterPatern = new ListPaternAdapter();
        rvPatern.setLayoutManager(new LinearLayoutManager(getActivity(),  LinearLayoutManager.HORIZONTAL, false));
        rvPatern.setAdapter(adapterPatern);

        seekBarPadding.setMax(TextStickerView.MAX_TEXT_SIZE);
        seekBarPadding.setProgress(progressPadding);
        seekBarPadding.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (listener != null) {
                    listener.onPaddingTextChanged(i);
                }
                progressPadding = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initData() {
        listColorPick = mContext.getResources().getIntArray(R.array.color_design_picker);
        for (int i = 1; i <= SIZE_PATTERN_TEXT ; i++) {
            if (i <= 14) {
                mListPattern1.add("bg/ic_bag_" + i + ".jpg");
            } else {
                mListPattern2.add("bg/ic_bag_" + i + ".jpg");
            }
        }
    }


    void updateProgress(int progressTextPadding) {
        this.progressPadding = progressTextPadding;
        if (seekBarPadding != null) {
            seekBarPadding.setProgress(progressTextPadding);
        }
    }

    public class ListPaternAdapter extends RecyclerView.Adapter<ListPaternAdapter.ViewHolder> {

        ListPaternAdapter() {}

        @Override
        public int getItemCount() {
            return mListPattern1.size();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            return new ViewHolder(inflater.inflate(R.layout.list_patern_text_adapter, parent, false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String path1 = "file:///android_asset/" + mListPattern1.get(position);
            Glide.with(mContext)
                    .asBitmap()
                    .load(path1)
                    .into(holder.imageView1);

            String path2 = "file:///android_asset/" + mListPattern2.get(position);
            Glide.with(mContext)
                    .asBitmap()
                    .load(path2)
                    .into(holder.imageView2);


            final String mPatternFileName1 = mListPattern1.get(position);
            final String mPatternFileName2 = mListPattern2.get(position);

            holder.imageView1.setOnClickListener(v -> {
                if(listener != null){
                    listener.onPatternBgTextChanged(mPatternFileName1);
                }
            });

            holder.imageView2.setOnClickListener(v -> {
                if(listener != null){
                    listener.onPatternBgTextChanged(mPatternFileName2);
                }
            });
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView1, imageView2;
            ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView1 = itemView.findViewById(R.id.imgPatternText1);
                imageView2 = itemView.findViewById(R.id.imgPatternText2);
            }
        }

    }

    public class ListColorAdapter extends RecyclerView.Adapter<ListColorAdapter.ViewHolder> {

        ListColorAdapter() {}

        @Override
        public int getItemCount() {
            return listColorPick.length;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            return new ViewHolder(inflater.inflate(R.layout.list_color_text_adapter, parent, false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.imageView.setColorFilter(listColorPick[position]);
        }


        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            ImageView imageView;
            ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imgTextColor);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if (listener != null){
                    listener.onColorBgTextChanged(listColorPick[position]);
                }
            }
        }

    }
    @Override
    public void backPressed() {

    }

    private OnTextSettingListener listener = null;
    public TabTextSettingFragment setListener(OnTextSettingListener listener) {
        this.listener = listener;
        return this;
    }

    public interface OnTextSettingListener{
        void onColorBgTextChanged(int colorCode);
        void onPatternBgTextChanged(String patternName);
        void onPaddingTextChanged(int value);
    }
}
