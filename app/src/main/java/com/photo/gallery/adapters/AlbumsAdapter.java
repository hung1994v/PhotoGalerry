package com.photo.gallery.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bsoft.core.PreloadNativeAdsList;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.photo.gallery.R;
import com.photo.gallery.models.AlbumItem;
import com.photo.gallery.utils.ConstValue;
import com.photo.gallery.utils.FileUtil;
import com.photo.gallery.utils.Flog;
import com.photo.gallery.utils.SharedPrefUtil;
import com.photo.gallery.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {

    private static final int ITEM_VIEW_TYPE = 0;
    private static final int NATIVE_AD_VIEW_TYPE = 1;
    private List<UnifiedNativeAd> unifiedNativeAds;
    private static final java.lang.String TAG = AlbumsAdapter.class.getSimpleName();
    public static boolean unAllSelected = false, allSelected = false;
    private Context mContext = null;
    private ArrayList<AlbumItem> mList = null;
    private int mItemSize = 0;
    private OnAlbumsAdapterListener listener = null;


    public AlbumsAdapter(Context context, ArrayList<AlbumItem> list) {
        mContext = context;
        mList = list;
        unifiedNativeAds = PreloadNativeAdsList.getInstance().getAll();
//        mItemSize = Utils.getScreenSize(context)[0] / ConstValue.NUM_OF_COLS_GRIDVIEW
//            - (int) context.getResources().getDimension(R.dimen.margin_xxsmall_size);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        if (viewType == NATIVE_AD_VIEW_TYPE) {
            View view = layoutInflater.inflate(R.layout.item_native_ads, parent, false);
            return new NativeAdsViewHolder(view);
        } else {
            View view = layoutInflater.inflate(R.layout.item_album, parent, false);
            return new MyViewHolder(view);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_VIEW_TYPE;
        } else {
            if (position % 5 == 0) {
                return NATIVE_AD_VIEW_TYPE;
            } else {
                return ITEM_VIEW_TYPE;
            }
        }

    }







    class NativeAdsViewHolder extends MyViewHolder {

        UnifiedNativeAdView adView;

        NativeAdsViewHolder(View itemView) {
            super(itemView);
            adView = itemView.findViewById(R.id.ad_view);
        }
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
        ((TextView) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
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
        }
        final AlbumItem item = mList.get(position);
        String string = item.numFile +" " + mContext.getResources().getString(R.string.item);
        holder.name.setText(item.name);
        holder.number_file.setText(string);
            if(item.pathFirstImg!=null){
                holder.size.setText(Formatter.formatFileSize(mContext, item.mSize));
            }else {
                holder.size.setText("0MB");
            }

        Glide.with(mContext)
                .load(item.pathFirstImg)
                .transform(new CenterCrop(),new RoundedCorners(25))
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
                    Flog.d("AAAAAAAAAA1", "album: "+ item.name + " size: "+ item.mSize + "position: "+ position + " sizeAlbum: "+ mList.size());
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

            name = (TextView) view.findViewById(R.id.tv_name_album);
            size = (TextView) view.findViewById(R.id.tv_size_album);
            ivFirst = (ImageView) view.findViewById(R.id.iv_first_of_album);
            viewTick = (ViewGroup) view.findViewById(R.id.view_tick);
            viewInfo = (ViewGroup) view.findViewById(R.id.view_info);
            number_file = view.findViewById(R.id.tv_sume_album);

            if (mItemSize <= 0) {
                return;
            }

        }
    }
}