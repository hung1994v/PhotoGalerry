package com.photo.splashfunphoto.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.photo.gallery.R;

import java.util.ArrayList;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<String> mListSticker = new ArrayList<>();
    private OnStickerListener mStickerListener;
    private int mCurrentPosition = 0;
    private int mCurrentPrev = 0;

    public StickerAdapter setOnStickerListener(OnStickerListener listener) {
        mStickerListener = listener;
        return this;
    }

    public StickerAdapter(Context context, ArrayList<String> list) {
        mContext = context;
        mListSticker = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.sticker_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String pathUri="file:///android_asset/" + mListSticker.get(position);
        Glide.with(mContext).asBitmap().load(pathUri).into(holder.mImageSticker);
        holder.mBtnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mStickerListener != null) {
                    mStickerListener.onItemStickerClickListener(position);
                    mCurrentPrev = mCurrentPosition;
                    mCurrentPosition = position;
                    notifyItemChanged(mCurrentPosition);
                    notifyItemChanged(mCurrentPrev);
                }
            }
        });

        if(position == mCurrentPosition){
            holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext,R.color.s_black));
        }else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext,R.color.s_black));
        }
    }

    @Override
    public int getItemCount() {
        return mListSticker.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageSticker;
        private LinearLayoutCompat mBtnIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageSticker = (ImageView) itemView.findViewById(R.id.img_sticker);
            mBtnIcon = (LinearLayoutCompat) itemView.findViewById(R.id.btn_icon_sticker);
        }
    }

    public interface OnStickerListener {
        void onItemStickerClickListener(int position);
    }
}
