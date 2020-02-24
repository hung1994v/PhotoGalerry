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
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.photo.gallery.R;
import com.photo.gallery.models.AlbumItem;
import com.photo.gallery.utils.ConstValue;
import com.photo.gallery.utils.Flog;
import com.photo.gallery.utils.SharedPrefUtil;
import com.photo.gallery.utils.Utils;

import java.io.File;
import java.util.ArrayList;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {

    private static final java.lang.String TAG = AlbumsAdapter.class.getSimpleName();
    public static boolean unAllSelected = false, allSelected = false;
    private Context mContext = null;
    private ArrayList<AlbumItem> mList = null;
    private int mItemSize = 0;
    private OnAlbumsAdapterListener listener = null;


    public AlbumsAdapter(Context context, ArrayList<AlbumItem> list) {
        mContext = context;
        mList = list;

//        mItemSize = Utils.getScreenSize(context)[0] / ConstValue.NUM_OF_COLS_GRIDVIEW
//            - (int) context.getResources().getDimension(R.dimen.margin_xxsmall_size);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate((R.layout.item_album), parent, false);

        return new MyViewHolder(itemView);
    }

    private long getTotalSize(File directory){
        long length = 0;
        for (File file : directory.listFiles()) {
            if (file.isFile())
                length += file.length();
            else
                length += getTotalSize(file);
        }
        return length;
    }

    public static String humanReadableByteCountSI(long bytes) {
        String s = bytes < 0 ? "-" : "";
        long b = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        return b < 1000L ? bytes + " B"
                : b < 999_950L ? String.format("%s%.1f kB", s, b / 1e3)
                : (b /= 1000) < 999_950L ? String.format("%s%.1f MB", s, b / 1e3)
                : (b /= 1000) < 999_950L ? String.format("%s%.1f GB", s, b / 1e3)
                : (b /= 1000) < 999_950L ? String.format("%s%.1f TB", s, b / 1e3)
                : (b /= 1000) < 999_950L ? String.format("%s%.1f PB", s, b / 1e3)
                : String.format("%s%.1f EB", s, b / 1e6);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final AlbumItem item = mList.get(position);
        String string = item.size +" " + mContext.getResources().getString(R.string.item);
        holder.name.setText(item.name);
        holder.number_file.setText(string);
        if(item.pathFirstImg!=null){
            holder.size.setText(humanReadableByteCountSI(getTotalSize(new File(item.pathFirstImg).getParentFile())));
        }else {
            holder.size.setText("0MB");
        }

        Glide.with(mContext)
                .load(item.pathFirstImg)

                .error(R.drawable.ic_no_image)
//                .skipMemoryCache(false)
                .transform(new CenterCrop(), new RoundedCorners(25))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.ivFirst);

        if (unAllSelected) {
            item.isSelected = false;
        } else if (allSelected) {
            item.isSelected = true;
        }

        final boolean isTicked = item.isSelected;
        Flog.d(TAG, "12 path=" + item.name + "_" + isTicked);
        showTicked(holder.viewTick, isTicked);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Flog.d(TAG,"album onLongClick");

                if (listener != null) {
                    unAllSelected = false;
                    allSelected = false;
                    item.isSelected = !item.isSelected;
                    listener.onItemAlbumLongClicked(position, item);
                }
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (listener != null) {
                    unAllSelected = false;
                    allSelected = false;
                    item.isSelected = !item.isSelected;
                    listener.onItemAlbumClicked(position, item);
                    Flog.d("AAAAAAAAAA1", "album: "+ item.name + " size: "+ item.size + "position: "+ position + " sizeAlbum: "+ mList.size());
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

    public AlbumsAdapter setListener(OnAlbumsAdapterListener listener) {
        this.listener = listener;
        return this;
    }

    public interface OnAlbumsAdapterListener {
        void onItemAlbumClicked(int position, AlbumItem album);

        void onItemAlbumLongClicked(int position, AlbumItem album);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, size, number_file;
        private ImageView ivFirst;
        private ViewGroup viewTick, viewInfo;

        public MyViewHolder(View view) {
            super(view);

            ImageView ivTicked = (ImageView) view.findViewById(R.id.iv_ticked);

//            int defaultPrimary = ContextCompat.getColor(mContext, R.color.colorPrimary);
//            int colorPrimary = SharedPrefUtil.getInstance().getInt(ConstValue.EXTRA_CURRENT_COLOR_PICKER, defaultPrimary);
//            Utils.setColorViews(colorPrimary, ivTicked);

            name = (TextView) view.findViewById(R.id.tv_name_album);
            size = (TextView) view.findViewById(R.id.tv_size_album);
            ivFirst = (ImageView) view.findViewById(R.id.iv_first_of_album);
            viewTick = (ViewGroup) view.findViewById(R.id.view_tick);
            viewInfo = (ViewGroup) view.findViewById(R.id.view_info);
            number_file = view.findViewById(R.id.tv_sume_album);

            if (mItemSize <= 0) {
                return;
            }

//            ViewGroup.LayoutParams params = viewTick.getLayoutParams();
//            params.width = mItemSize;
//            params.height = mItemSize;
//            viewTick.requestLayout();

//            image_container = (ViewGroup) itemView.findViewById(R.id.image_container);
//            ViewGroup.LayoutParams params1 = ivFirst.getLayoutParams();
//            params1.width = mItemSize;
//            params1.height = mItemSize;
//            ivFirst.requestLayout();

//            ViewGroup.LayoutParams params2 = viewInfo.getLayoutParams();
//            params2.width = mItemSize;
//            params2.height = mItemSize;
//            viewInfo.requestLayout();
        }
    }
}