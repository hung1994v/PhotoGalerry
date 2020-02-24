package com.photo.gallery.adapters.options;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.photo.gallery.R;

import java.util.ArrayList;

import bsoft.com.lib_filter.filter.border.BorderImageView;


/**
 * Created by Computer on 9/7/2017.
 */

public class PIPAdapter extends RecyclerView.Adapter<PIPAdapter.ViewHolder> {
    private ArrayList<Integer> iconId = new ArrayList<>();
    private Context mContext;
    private int mSelectBorderColor = Color.rgb(0, 235, 232);
    private int current = 0;
    private OnItemPIPListener mOnItemPIPListener;

    public PIPAdapter(Context context, ArrayList<Integer> list) {
        mContext = context;
        iconId = list;
    }

    public PIPAdapter setOnItemPIPListener(OnItemPIPListener listener) {
        mOnItemPIPListener = listener;
        return this;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_pip, parent, false);
        return new PIPAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), iconId.get(position));
        Bitmap border = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_border_layout);
        holder.imgBorder.setImageBitmap(bitmap);

        holder.imgBorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int prevPos = current;
                current = position;
                if (mOnItemPIPListener != null) {
                    mOnItemPIPListener.onItemPIPClick(position);
                    notifyItemChanged(prevPos);
                    notifyItemChanged(current);
                }
            }
        });

        if (position == current) {
            holder.imgBorder.setBorderColor(mSelectBorderColor);
            holder.imgBorder.setShowBorder(true);
            holder.imgBorder.setBorderWidth(1.0f);
            holder.imgBorder.setShowImageBorder(true, border);
        } else {
            holder.imgBorder.setShowBorder(false);
        }

    }

    @Override
    public int getItemCount() {
        return iconId.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private BorderImageView imgBorder;

        public ViewHolder(View itemView) {
            super(itemView);
            imgBorder = (BorderImageView) itemView.findViewById(R.id.item_pip);
        }
    }

    public interface OnItemPIPListener {
        void onItemPIPClick(int pos);
    }
}
