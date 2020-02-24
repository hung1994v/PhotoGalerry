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
public class IconStickerAdapter extends RecyclerView.Adapter<IconStickerAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<String> mListIconSticker = new ArrayList<>();
    private OnIconListener mIconListener;

    public IconStickerAdapter setOnIconListener(OnIconListener listener) {
        mIconListener = listener;
        return this;
    }

    public IconStickerAdapter(Context context, ArrayList<String> list) {
        mContext = context;
        mListIconSticker = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.icon_sticker_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final String stickerFileName = mListIconSticker.get(position);

        Glide.with(mContext)
                .load(Uri.parse("file:///android_asset/" + mListIconSticker.get(position)))
                .into(holder.mImageSticker);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIconListener != null) {
                    mIconListener.onItemIconClickListener(stickerFileName);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListIconSticker.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageSticker;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageSticker = (ImageView) itemView.findViewById(R.id.img_icon);
        }
    }

    public interface OnIconListener {
        void onItemIconClickListener(String s);
    }
}
