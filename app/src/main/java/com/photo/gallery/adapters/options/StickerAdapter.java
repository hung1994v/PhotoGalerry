package com.photo.gallery.adapters.options;

import android.content.Context;
import android.net.Uri;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.photo.gallery.R;

import java.util.ArrayList;



/**
 * Created by Computer on 3/9/2017.
 */
public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<String> mListSticker = new ArrayList<>();
    private OnStickerListener mStickerListener;
    private int prevPosition = 0;
    private int currentPosition = 0;

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
        View view = inflater.inflate(R.layout.item_sticker, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String path = "file:///android_asset/" + mListSticker.get(position);
        Glide.with(mContext)
                .asBitmap()
                .load(path)
                .into(holder.mImageSticker);

        holder.mBtnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevPosition = currentPosition;
                currentPosition = position;
                if (mStickerListener != null) {
                    mStickerListener.onItemStickerClickListener(position);
                }
                notifyItemChanged(prevPosition);
                notifyItemChanged(currentPosition);
            }
        });
        if(position == currentPosition){
            holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.color_white_alpha));
        }else {
            holder.itemView.setBackgroundColor(mContext.getResources().getColor(android.R.color.transparent));
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
