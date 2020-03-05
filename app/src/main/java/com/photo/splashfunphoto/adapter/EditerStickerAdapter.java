package com.photo.splashfunphoto.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.gallery.R;


public class EditerStickerAdapter extends RecyclerView.Adapter<EditerStickerAdapter.ViewHolder> {
    private Context mContext;
    private final int[] ICONS = new int[]{
//            R.drawable.ic_sticker_opacity,
            R.drawable.ic_sticker_r_right,
            R.drawable.ic_sticker_r_left,
            R.drawable.ic_sticker_up,
            R.drawable.ic_sticker_down,
            R.drawable.ic_sticker_left,
            R.drawable.ic_sticker_right,
            R.drawable.ic_sticker_zoom_in,
            R.drawable.ic_sticker_zoom_out};

    private OnItemEditerListener mEditerListener;

    public EditerStickerAdapter setOnItemEditerListener(OnItemEditerListener listener) {
        mEditerListener = listener;
        return this;
    }


    public EditerStickerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.editer_sticker_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mImageEidter.setImageResource(ICONS[position]);
        holder.mBtnEiterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (position) {
                    case 0:
                        if (mEditerListener != null) {
                            mEditerListener.onItemRotateClickListener();
                        }
                        break;
                    case 1:
                        if (mEditerListener != null) {
                            mEditerListener.onItemIRotateClickListener();
                        }
                        break;
                    case 2:
                        if (mEditerListener != null) {
                            mEditerListener.onItemUpClickListener();
                        }
                        break;
                    case 3:
                        if (mEditerListener != null) {
                            mEditerListener.onItemDownClickListener();
                        }
                        break;
                    case 4:
                        if (mEditerListener != null) {
                            mEditerListener.onItemLeftClickListener();
                        }
                        break;
                    case 5:
                        if (mEditerListener != null) {
                            mEditerListener.onItemRightClickListener();
                        }
                        break;
                    case 6:
                        if (mEditerListener != null) {
                            mEditerListener.onItemZoomInClickListener();
                        }
                        break;
                    case 7:
                        if (mEditerListener != null) {
                            mEditerListener.onItemZoomOutClickListener();
                        }
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return ICONS.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageEidter;
        private LinearLayoutCompat mBtnEiterIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageEidter = (ImageView) itemView.findViewById(R.id.editer_sticker);
            mBtnEiterIcon = (LinearLayoutCompat) itemView.findViewById(R.id.btn_editer_icon);
        }
    }

    public interface OnItemEditerListener {
        void onItemOpacityClickListener(int opacity);

        public void onItemRotateClickListener();

        public void onItemIRotateClickListener();

        public void onItemUpClickListener();

        public void onItemDownClickListener();

        public void onItemLeftClickListener();

        public void onItemRightClickListener();

        public void onItemZoomInClickListener();

        public void onItemZoomOutClickListener();
    }
}
