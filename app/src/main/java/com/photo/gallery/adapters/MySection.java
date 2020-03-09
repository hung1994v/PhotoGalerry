package com.photo.gallery.adapters;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.photo.gallery.R;
import com.photo.gallery.models.FileItem;
import com.photo.gallery.section_headergrid.SectionParameters;
import com.photo.gallery.section_headergrid.StatelessSection;
import com.photo.gallery.utils.ConstValue;
import com.photo.gallery.utils.Flog;
import com.photo.gallery.utils.SharedPrefUtil;
import com.photo.gallery.utils.Utils;

import java.util.ArrayList;

import static com.photo.gallery.utils.ConstValue.NUM_OF_COLS_DAY_GRIDVIEW;

public class MySection extends StatelessSection {
    private static final java.lang.String TAG = MySection.class.getSimpleName();
    public static boolean unAllSelected = false, allSelected = false;
    private Context mContext = null;
    private String mHeader = null;
    private ArrayList<FileItem> mItems = null;
    private int mItemSize = 0;
    private int mIndex = -1;
    private OnMySetionListener listener = null;
    private int selectedItemCount = 0;
    private boolean STATE_EDIT;



    public void setSelectedItemCount(int selectedItemCount) {
        this.selectedItemCount = selectedItemCount;
    }




    public MySection(int index, Context context, String header, ArrayList<FileItem> items, boolean state) {
        // call constructor with layout resources for this Section header and items
        super(SectionParameters.builder()
                .itemResourceId(R.layout.section_item)
                .headerResourceId(R.layout.section_header)
                .build());

        mIndex = index;

        mContext = context;
        mHeader = header;
        mItems = items;
        this.STATE_EDIT = state;
        int width = context.getResources().getDisplayMetrics().widthPixels;
        mItemSize = width / SharedPrefUtil.getInstance().getInt(ConstValue.COLUM_GIRD_NUMBER,NUM_OF_COLS_DAY_GRIDVIEW)
                - (int) context.getResources().getDimension(R.dimen.margin_xxsmall_size);
    }

    @Override
    public int getContentItemsTotal() {
        return mItems.size(); // number of items of this section
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        // return a custom instance of ViewHolder for the items of this section
        return new MyItemViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final MyItemViewHolder itemHolder = (MyItemViewHolder) holder;

        // bind your view here
        final FileItem item = mItems.get(position);
        String filePath = item.path;

        itemHolder.viewInfoVideo.setVisibility(item.isImage ? View.GONE : View.VISIBLE);
        if (!item.isImage) {
            itemHolder.textTimeVideo.setText(Utils.milliSecondsToTimer(Utils.parseLong(item.duration)));
        }

//        if (item.path.endsWith(".gif")) {
//            Glide.with(mContext)
//                    .load(filePath)
//                    .asGif()
//                    .error(R.drawable.ic_no_image)
//                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .override(mItemSize, mItemSize)
//                    .into(itemHolder.ivImage);
//        } else {

            Glide.with(mContext)
                    .load(filePath)
                    .error(R.drawable.ic_no_image)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(false)
//                .skipMemoryCache(false)  // old
                    .apply(new RequestOptions().placeholder(R.drawable.splash).override(mItemSize,mItemSize))
                    .centerCrop()
                    .into(itemHolder.ivImage);


//        }

        if (unAllSelected) {
            item.isSelected = false;
        } else if (allSelected) {
            item.isSelected = true;
        }

        final boolean isTicked = item.isSelected;
        Flog.d(TAG, "path=" + item.name + "_" + isTicked);
//        if(selectedItemCount>0){
//            itemHolder.iconTick.setVisibility(View.VISIBLE);
//        }else {
//            itemHolder.iconTick.setVisibility(View.INVISIBLE);
//        }
        showTicked(itemHolder.viewTick, isTicked);

        itemHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (listener != null) {
                    selectedItemCount =1;
                    unAllSelected = false;
                    allSelected = false;
                    item.isSelected = !item.isSelected;
                    listener.onItemInSetionLongClicked(mIndex, position, item);
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
                    listener.onItemInSetionClicked(mIndex, position, item);
                }
            }
        });
    }

    private void showTicked(View view, boolean isTicked) {
        view.setVisibility(isTicked ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

        // bind your view here
        headerHolder.tvTitle.setText(mHeader);
    }

    public MySection setListener(OnMySetionListener listener) {
        this.listener = listener;
        return this;
    }

    public interface OnMySetionListener {
        void onItemInSetionLongClicked(int index, int position, FileItem file);

        void onItemInSetionClicked(int index, int position, FileItem file);
    }

    class MyItemViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivImage;
        private ImageView iconTick;
        private ViewGroup image_container;
        private ViewGroup  viewTick,viewInfoVideo;
        private TextView textTimeVideo;

        public MyItemViewHolder(View itemView) {
            super(itemView);

            iconTick = (ImageView) itemView.findViewById(R.id.iv_ticked);

            ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
            viewTick = (ViewGroup) itemView.findViewById(R.id.view_tick);
            viewInfoVideo = (ViewGroup) itemView.findViewById(R.id.view_info_video);
            textTimeVideo = (TextView) itemView.findViewById(R.id.tv_time_video);

//            if (mItemSize <= 0) {
//                return;
//            }

//            image_container = (ViewGroup) itemView.findViewById(R.id.image_container);
//            ViewGroup.LayoutParams params = viewTick.getLayoutParams();
//            params.width = mItemSize;
//            params.height = mItemSize;
//            viewTick.requestLayout();
//
//            ViewGroup.LayoutParams params1 = ivImage.getLayoutParams();
//            params1.width = mItemSize;
//            params1.height = mItemSize;
//            ivImage.requestLayout();
//
//            ViewGroup.LayoutParams params2 = viewInfoVideo.getLayoutParams();
//            params2.width = mItemSize;
//            params2.height = mItemSize;
//            viewInfoVideo.requestLayout();
        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;

        HeaderViewHolder(View view) {
            super(view);

            tvTitle = (TextView) view.findViewById(R.id.tv_header);
        }
    }
}