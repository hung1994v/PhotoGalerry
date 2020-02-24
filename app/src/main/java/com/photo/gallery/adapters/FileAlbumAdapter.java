package com.photo.gallery.adapters;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.photo.gallery.R;
import com.photo.gallery.models.FileItem;
import com.photo.gallery.utils.ConstValue;
import com.photo.gallery.utils.Flog;
import com.photo.gallery.utils.SharedPrefUtil;
import com.photo.gallery.utils.Utils;

import java.util.ArrayList;

public class FileAlbumAdapter extends RecyclerView.Adapter<FileAlbumAdapter.MyViewHolder> {

    private static final java.lang.String TAG = FileAlbumAdapter.class.getSimpleName();
    public static boolean unAllSelected = false, allSelected = false;
    private Context mContext = null;
    private ArrayList<FileItem> mList = null;
    private int mItemSize = 0;
    private OnAlbumsAdapterListener listener = null;

    public FileAlbumAdapter(Context context, ArrayList<FileItem> list) {
        mContext = context;
        mList = list;

        mItemSize = Utils.getScreenSize(context)[0] / ConstValue.NUM_OF_COLS_GRIDVIEW
                - (int) context.getResources().getDimension(R.dimen.margin_xxsmall_size);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate((R.layout.section_item), parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final MyViewHolder itemHolder = holder;

        // bind your view here
        final FileItem item = mList.get(position);
        String filePath = item.path;

        itemHolder.viewInfoVideo.setVisibility(item.isImage ? View.GONE : View.VISIBLE);
        if (!item.isImage) {
            itemHolder.textTimeVideo.setText(Utils.milliSecondsToTimer(Utils.parseLong(item.duration)));
        }

        Glide.with(mContext)
                .load(filePath)
                .error(R.drawable.ic_no_image)
//                .skipMemoryCache(false)
                .override(mItemSize, mItemSize)
                .centerCrop()
                .into(itemHolder.ivImage);

        if (unAllSelected) {
            item.isSelected = false;
        } else if (allSelected) {
            item.isSelected = true;
        }

        final boolean isTicked = item.isSelected;
        Flog.d(TAG, "12 path=" + item.name + "_" + isTicked);
        showTicked(itemHolder.viewTick, isTicked);

        itemHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (listener != null) {
                    unAllSelected = false;
                    allSelected = false;
                    item.isSelected = !item.isSelected;
                    listener.onItemInAlbumLongClicked(position, item);
                }
                return true;
            }
        });

        itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    unAllSelected = false;
                    allSelected = false;
                    item.isSelected = !item.isSelected;
                    listener.onItemInAlbumClicked(position, item);
                }
            }
        });
    }

    private void showTicked(View view, boolean isTicked) {
        view.setVisibility(isTicked ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public FileAlbumAdapter setListener(OnAlbumsAdapterListener listener) {
        this.listener = listener;
        return this;
    }

    public interface OnAlbumsAdapterListener {
        public void onItemInAlbumLongClicked(int position, FileItem file);

        public void onItemInAlbumClicked(int position, FileItem file);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivImage;
        private ViewGroup viewTick, viewInfoVideo;
        private TextView textTimeVideo;

        public MyViewHolder(View itemView) {
            super(itemView);

            ImageView ivTicked = (ImageView) itemView.findViewById(R.id.iv_ticked);

//            int defaultPrimary = ContextCompat.getColor(mContext, R.color.colorPrimary);
//            int colorPrimary = SharedPrefUtil.getInstance().getInt(ConstValue.EXTRA_CURRENT_COLOR_PICKER, defaultPrimary);
//            Utils.setColorViews(colorPrimary, ivTicked);

            ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
            viewTick = (ViewGroup) itemView.findViewById(R.id.view_tick);

            viewInfoVideo = (ViewGroup) itemView.findViewById(R.id.view_info_video);
            textTimeVideo = (TextView) itemView.findViewById(R.id.tv_time_video);

            if (mItemSize <= 0) {
                return;
            }

            ViewGroup.LayoutParams params = viewTick.getLayoutParams();
            params.width = mItemSize;
            params.height = mItemSize;
            viewTick.requestLayout();

            ViewGroup.LayoutParams params1 = ivImage.getLayoutParams();
            params1.width = mItemSize;
            params1.height = mItemSize;
            ivImage.requestLayout();

            ViewGroup.LayoutParams params2 = viewInfoVideo.getLayoutParams();
            params2.width = mItemSize;
            params2.height = mItemSize;
            viewInfoVideo.requestLayout();
        }
    }
}