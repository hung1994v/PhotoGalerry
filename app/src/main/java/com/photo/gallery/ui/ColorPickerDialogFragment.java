package com.photo.gallery.ui;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.photo.gallery.R;
import com.photo.gallery.utils.ConstValue;
import com.photo.gallery.utils.SharedPrefUtil;

import java.util.ArrayList;

import bsoft.healthy.tracker.menstrual.lib_sticker.ui.CustomRoundImage;

public class ColorPickerDialogFragment extends DialogFragment {
    private RecyclerView mRecyclerView;
    private MoreBackgroundColorAdapter adapter;
    private ArrayList<Integer> listBgColor = new ArrayList<>();

    // this method create view for your Dialog
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate layout with recycler view
        View v = inflater.inflate(R.layout.fragment_diglog_color_picker, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));
        //setadapter
        int[] colorPick = getResources().getIntArray(R.array.color_app_picker);
        setBgColorList(colorPick);
        adapter = new MoreBackgroundColorAdapter(getContext(), listBgColor);
        mRecyclerView.setAdapter(adapter);
        //get your recycler view and populate it.
        return v;
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

    private class MoreBackgroundColorAdapter extends RecyclerView.Adapter<MoreBackgroundColorAdapter.ViewHolder> {
        private Context mContext = null;
        private ArrayList<Integer> mList;
        private int size;
        private int curPosition = -1;

        public MoreBackgroundColorAdapter(Context context, ArrayList<Integer> list) {
            mContext = context;
            mList = list;
            size = (int) mContext.getResources().getDimension(R.dimen.icon_size_xstand);
            curPosition = SharedPrefUtil.getInstance().getInt(ConstValue.EXTRA_CURRENT_POSITION_PICKER, 0);
        }

        @Override
        public MoreBackgroundColorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_color_picker, null, false);
            return new MoreBackgroundColorAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MoreBackgroundColorAdapter.ViewHolder holder, final int position) {
            holder.ivBg.setImageResource(R.drawable.ic_square_round);
            holder.ivBg.setColorFilter(mList.get(position), android.graphics.PorterDuff.Mode.SRC_IN);
            holder.ticked.setVisibility((position==curPosition)?View.VISIBLE:View.GONE);

            holder.ivBg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPrefUtil.getInstance().putInt(ConstValue.EXTRA_CURRENT_POSITION_PICKER, position);
                    SharedPrefUtil.getInstance().putInt(ConstValue.EXTRA_CURRENT_COLOR_PICKER, mList.get(position));

                    notifyItemChanged(curPosition);
                    curPosition = position;
                    notifyItemChanged(position);

                    if (listener!=null) {
                        listener.onColorPicked(mList.get(position));
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
            private View ticked;

            public ViewHolder(View itemView) {
                super(itemView);
                ivBg = (CustomRoundImage) itemView.findViewById(R.id.iv_background_editor);
                ticked = itemView.findViewById(R.id.iv_ticked);
            }
        }
    }

    private OnColorPickerDialogListener listener = null;

    public ColorPickerDialogFragment setListener(OnColorPickerDialogListener listener) {
        this.listener = listener;
        return this;
    }


    public interface OnColorPickerDialogListener {
        void onColorPicked(int color);
    }
}