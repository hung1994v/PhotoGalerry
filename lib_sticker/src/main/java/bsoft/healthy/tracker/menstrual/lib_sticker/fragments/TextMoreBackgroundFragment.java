package bsoft.healthy.tracker.menstrual.lib_sticker.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import bsoft.healthy.tracker.menstrual.lib_sticker.R;
import bsoft.healthy.tracker.menstrual.lib_sticker.ui.CustomRoundImage;

/**
 * Created by nam on 4/5/2017.
 */

public class TextMoreBackgroundFragment extends BaseStickerFragment {
    private static final String TAG = TextMoreBackgroundFragment.class.getSimpleName();
    private RecyclerView rvBgPattern, rvBgColor;
    private OnTextMoreBackgroundListener listener;
    private ArrayList<String> listBgPattern = null;
    private ArrayList<Integer> listBgColor = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_two_recycerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setVal();
    }

    private void init(View view) {
        rvBgPattern = (RecyclerView) view.findViewById(R.id.recyclerview_top);
        rvBgColor = (RecyclerView) view.findViewById(R.id.recyclerview_bottom);
    }

    private void setVal() {

        LinearLayoutManager lm = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        rvBgPattern.setLayoutManager(lm);

        setBgPatternList(getListBackground());

        if (listBgPattern != null) {
            MoreBackgroundAdapter adapterBackground = new MoreBackgroundAdapter(mContext, listBgPattern);
            rvBgPattern.setAdapter(adapterBackground);
        }


        LinearLayoutManager lm1 = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        rvBgColor.setLayoutManager(lm1);

        int[] colorPick = mContext.getResources().getIntArray(R.array.color_design_picker);
        setBgColorList(colorPick);

        if (listBgColor != null) {
            MoreBackgroundColorAdapter adapterBackground = new MoreBackgroundColorAdapter(mContext, listBgColor);
            rvBgColor.setAdapter(adapterBackground);
        }
    }

    private void setBgColorList(int[] list) {
        if (listBgColor == null) {
            listBgColor = new ArrayList<>();
        }

        listBgColor.clear();
        for (int i = 0; i < list.length; i++) {
            listBgColor.add(list[i]);
        }
    }

    public TextMoreBackgroundFragment setListener(OnTextMoreBackgroundListener listener) {
        this.listener = listener;
        return this;
    }

    public void setBgPatternList(ArrayList<String> listBgPattern) {
        this.listBgPattern = listBgPattern;
    }

    public ArrayList<String> getListBackground() {

        final int sizeBg = 13;
        final String nameFormated = "ic_bag_%d.png";

        ArrayList<String> list = new ArrayList<>(sizeBg);

        for (int i = 0; i < sizeBg; i++) {
            list.add("ic_bag_" + i + ".png");   //String.format(nameFormated, i) depend on languages.
        }
        return list;
    }

    public interface OnTextMoreBackgroundListener {
        void onTextBackgroundSelected(String nameBackground);

        void onTextColorBackgroundSelected(int color);
    }

    private class MoreBackgroundAdapter extends RecyclerView.Adapter<MoreBackgroundAdapter.ViewHolder> {
        private Context mContext = null;
        private List<String> mList;
        private int size;

        public MoreBackgroundAdapter(Context context, ArrayList<String> list) {
            mContext = context;
            mList = list;
            size = (int) mContext.getResources().getDimension(R.dimen.icon_size_standard);
        }

        @Override
        public MoreBackgroundAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_more_background, null, false);
            return new MoreBackgroundAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MoreBackgroundAdapter.ViewHolder holder, final int position) {
            Glide.with(mContext).load(Uri.parse("file:///android_asset/bg/" + mList.get(position))).into(holder.ivBg);

            holder.ivBg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onTextBackgroundSelected(mList.get(position));
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private CustomRoundImage ivBg;

            public ViewHolder(View itemView) {
                super(itemView);
                ivBg = (CustomRoundImage) itemView.findViewById(R.id.iv_background_editor);
            }
        }
    }

    private class MoreBackgroundColorAdapter extends RecyclerView.Adapter<MoreBackgroundColorAdapter.ViewHolder> {
        private Context mContext = null;
        private ArrayList<Integer> mList;
        private int size;

        public MoreBackgroundColorAdapter(Context context, ArrayList<Integer> list) {
            mContext = context;
            mList = list;
            size = (int) mContext.getResources().getDimension(R.dimen.icon_size_standard);
        }

        @Override
        public MoreBackgroundColorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_more_background, null, false);
            return new MoreBackgroundColorAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MoreBackgroundColorAdapter.ViewHolder holder, final int position) {
            holder.ivBg.setImageResource(R.drawable.ic_square_round);
            holder.ivBg.setColorFilter(mList.get(position), android.graphics.PorterDuff.Mode.SRC_IN);

            holder.ivBg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onTextColorBackgroundSelected(mList.get(position));
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private CustomRoundImage ivBg;

            public ViewHolder(View itemView) {
                super(itemView);
                ivBg = (CustomRoundImage) itemView.findViewById(R.id.iv_background_editor);
            }
        }
    }
}
