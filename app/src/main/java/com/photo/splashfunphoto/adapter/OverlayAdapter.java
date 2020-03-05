package com.photo.splashfunphoto.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.photo.gallery.R;
import com.photo.splashfunphoto.fragment.OverlayFragment;

import java.util.ArrayList;

import bsoft.com.lib_filter.filter.border.BorderImageView;
import bsoft.com.lib_filter.filter.gpu.WBRes;

public class OverlayAdapter extends RecyclerView.Adapter<OverlayAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<WBRes> mListOverlay = new ArrayList<>();
    private OnItemOverlayListener mOverlayListener = null;
    private int mSelectBorderColor = Color.rgb(0, 235, 232);
    private int current = 0;

    public OverlayAdapter setOnItemOverlayListener(OnItemOverlayListener listener) {
        mOverlayListener = listener;
        return this;
    }

    public OverlayAdapter(Context context, ArrayList<WBRes> list, int positionCurrent ) {
        mContext = context;
        mListOverlay = list;
        current = positionCurrent;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.overlay_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(position == OverlayFragment.DEFAULT_ITEM_POSITION){
            Glide.with(mContext)
                    .asBitmap().load(R.drawable.ic_none_overlay)
                    .transform(new CenterCrop(),new RoundedCorners(8))
                    .into(holder.mImOverlay);
        }else {
            WBRes posRes = mListOverlay.get(position);
            Bitmap bmp = posRes.getIconBitmap();
            Glide.with(mContext)
                    .asBitmap().load(bmp)
                    .transform(new CenterCrop(),new RoundedCorners(8))
                    .into(holder.mImOverlay);
        }

        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_border_layout);
        holder.mBtnOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int prevPos = current;
                current = position;
                if (mOverlayListener != null) {
                    mOverlayListener.onItemOverlayClickListenr(prevPos, position);
                    notifyItemChanged(prevPos);
                    notifyItemChanged(position);
                }
            }
        });

        if (position == current) {
            holder.mImOverlay.setBorderColor(mSelectBorderColor);
            holder.mImOverlay.setShowBorder(true);
            holder.mImOverlay.setBorderWidth(1.0f);
            holder.mImOverlay.setShowImageBorder(true, bitmap);
        } else {
            holder.mImOverlay.setShowBorder(false);
        }
    }

    @Override
    public int getItemCount() {
        return mListOverlay.size();
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private BorderImageView mImOverlay;
        private LinearLayout mBtnOverlay;

        public ViewHolder(View itemView) {
            super(itemView);

            mImOverlay = (BorderImageView) itemView.findViewById(R.id.img_overlay);
            mBtnOverlay = (LinearLayout) itemView.findViewById(R.id.btn_item_overlay);
        }
    }

    public interface OnItemOverlayListener {
        void onItemOverlayClickListenr(int prevPos, int position);
    }
}
