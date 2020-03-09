package com.photo.gallery.adapters;

import android.content.ClipData;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bsoft.core.PreloadNativeAdsList;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.photo.gallery.R;
import com.photo.gallery.models.FileItem;
import com.photo.gallery.utils.ConstValue;
import com.photo.gallery.utils.Flog;
import com.photo.gallery.utils.SharedPrefUtil;
import com.photo.gallery.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class FileAlbumAdapter extends RecyclerView.Adapter<FileAlbumAdapter.MyViewHolder> {

    private static final java.lang.String TAG = FileAlbumAdapter.class.getSimpleName();
    public static boolean unAllSelected = false, allSelected = false;
    private Context mContext = null;
    private ArrayList<FileItem> mList = new ArrayList<>();
    private int mItemSize = 0;
    private OnAlbumsAdapterListener listener = null;

    public static final int ITEM_VIEW_TYPE = 0;
    public static final int NATIVE_AD_VIEW_TYPE = 1;
    private List<UnifiedNativeAd> unifiedNativeAds;
    private int positionAds;


    public FileAlbumAdapter(Context context, ArrayList<FileItem> list) {
        mContext = context;
        mList.addAll(list);
        intList(mList);
        unifiedNativeAds = PreloadNativeAdsList.getInstance().getAll();
        Flog.d("FileAlbumAdapter", "size" + unifiedNativeAds.size());
    }

    public void updateView(ArrayList<FileItem> fileItems){
        if(mList!=null){
            mList.clear();
            mList.addAll(fileItems);
            intList(mList);
            notifyDataSetChanged();
        }
    }

    private void intList(ArrayList<FileItem> list) {
        positionAds = 9;
        if (!PreloadNativeAdsList.getInstance().getAll().isEmpty()) {
            while (positionAds < list.size()) {
                list.add(positionAds, new FileItem( true));
                positionAds += 19;
            }
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType == NATIVE_AD_VIEW_TYPE) {
            View view = layoutInflater.inflate(R.layout.item_native_image_adapter, parent, false);
            return new NativeAdsViewHolder(view);
        } else {
            View view = layoutInflater.inflate(R.layout.section_item, parent, false);
            return new MyViewHolder(view);
        }
    }


    class NativeAdsViewHolder extends MyViewHolder {

        UnifiedNativeAdView adView;

        NativeAdsViewHolder(View itemView) {
            super(itemView);
            adView = itemView.findViewById(R.id.ad_view);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (mList.get(position).isAds) {
            return NATIVE_AD_VIEW_TYPE;
        }

        return ITEM_VIEW_TYPE;
    }

    private void populateNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        adView.setVisibility(View.VISIBLE);

        MediaView mediaView = (MediaView) adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);
        mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);


// Register the view used for each individual asset.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
// adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_icon));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        NativeAd.Image icon = nativeAd.getIcon();

        if (icon == null) {
            adView.getIconView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(icon.getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.GONE);

            if (nativeAd.getAdvertiser() == null) {
                adView.getAdvertiserView().setVisibility(View.INVISIBLE);
            } else {
                ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
                adView.getAdvertiserView().setVisibility(View.VISIBLE);
            }
        } else {
            ((RatingBar) adView.getStarRatingView()).setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        int viewType = getItemViewType(position);
        if (viewType == NATIVE_AD_VIEW_TYPE && !unifiedNativeAds.isEmpty()) {
            ((NativeAdsViewHolder) holder).adView.setVisibility(View.VISIBLE);
            populateNativeAdView(unifiedNativeAds.get(position % unifiedNativeAds.size()), ((NativeAdsViewHolder) holder).adView);
        } else if (viewType == NATIVE_AD_VIEW_TYPE) {
            ((NativeAdsViewHolder) holder).adView.setVisibility(View.GONE);
        } else {

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
                    .apply(new RequestOptions().placeholder(R.drawable.splash).centerCrop())
                    .thumbnail(Glide.with(mContext).load(filePath)
                            .apply(RequestOptions.overrideOf(20, 20)))
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
}