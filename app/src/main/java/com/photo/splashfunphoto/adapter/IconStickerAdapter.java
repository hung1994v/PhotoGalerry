package com.photo.splashfunphoto.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.photo.gallery.R;

import java.util.ArrayList;


public class IconStickerAdapter extends RecyclerView.Adapter<IconStickerAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<String> mListIconSticker = new ArrayList<>();
    private OnIconListener mIconListener;


    public IconStickerAdapter(Context context, ArrayList<String> list) {
        mContext = context;
        mListIconSticker = list;
    }

    public IconStickerAdapter setOnIconListener(OnIconListener listener) {
        mIconListener = listener;
        return this;
    }

    @Override
    public IconStickerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.icon_sticker_item, parent, false);
        return new IconStickerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IconStickerAdapter.ViewHolder holder, final int position) {
        final String mStickerFileName = mListIconSticker.get(position);
        String path = "file:///android_asset/" + mListIconSticker.get(position);
        Glide.with(mContext)
                .asBitmap()
                .load(path)
                .into(holder.mImageIcon);

        holder.mImageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIconListener != null) {
                    mIconListener.onItemIconClickListener(mStickerFileName);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListIconSticker.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageIcon;
        public ViewHolder(View itemView) {
            super(itemView);
            mImageIcon = (ImageView) itemView.findViewById(R.id.img_icon);
        }
    }

    public interface OnIconListener {
        void onItemIconClickListener(String s);
    }
}
