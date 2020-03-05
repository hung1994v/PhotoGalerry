package com.photo.splashfunphoto.adapter.text;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.photo.gallery.R;
import com.photo.splashfunphoto.listener.OnMenuBackgroundListener;


public class ColorBackgroundAdapter extends RecyclerView.Adapter<ColorBackgroundAdapter.ViewHolder> {

    private Context context = null;
    private int[] mList = null;

    private OnMenuBackgroundListener listener = null;

    public ColorBackgroundAdapter(Context context, int[] list) {
        this.context = context;
        this.mList = list;
    }

    public ColorBackgroundAdapter setOnMenuBackgroundListener(OnMenuBackgroundListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_item_color_bg, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.btnColor.setColorFilter(mList[position]);
        holder.btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onMenuColorBackgroundClickListener(mList[position]);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView btnColor;

        public ViewHolder(View itemView) {
            super(itemView);
            btnColor = (ImageView) itemView.findViewById(R.id.btn_color);
        }
    }
}