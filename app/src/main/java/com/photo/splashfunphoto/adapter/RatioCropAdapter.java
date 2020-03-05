package com.photo.splashfunphoto.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.photo.gallery.R;
import com.photo.splashfunphoto.model.RatioCrop;
import com.photo.splashfunphoto.utils.Statics;

import java.util.List;

public class RatioCropAdapter extends RecyclerView.Adapter<RatioCropAdapter.ViewHolder> {
    private Context mContext;
    private List<RatioCrop> mLisRatioCrop ;
    private OnItemMirrorListener mMirrorListener;
    private int indexPos = 0;
//    private int heightDefaultItem = 0;
    private RecyclerView mRecycler ;
    private int prevPos = 0;

    public RatioCropAdapter setOnItemMirrorListener(OnItemMirrorListener listener){
        mMirrorListener = listener;
        return this;
    }

    public RatioCropAdapter(Context context, List<RatioCrop> list, RecyclerView recyclerView, int pos){
        mContext = context;
        mLisRatioCrop = list;
        mRecycler = recyclerView;
//        heightDefaultItem = (int) mContext.getResources().getDimension(R.dimen._40sdp);
        indexPos = pos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_ratio_crop_new,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.txtNameRatio.setText(mLisRatioCrop.get(position).getNameRatio());
        if(mLisRatioCrop.get(position).getType().contains("Ins")){
            holder.icAvatar.setVisibility(View.VISIBLE);
            holder.icAvatar.setImageResource(R.drawable.ic_instagram_logo);
        }else if(mLisRatioCrop.get(position).getType().contains("Fb")){
            holder.icAvatar.setVisibility(View.VISIBLE);
            holder.icAvatar.setImageResource(R.drawable.ic_facebook_logo);
        }else if(mLisRatioCrop.get(position).getType().contains("Youtube")){
            holder.icAvatar.setVisibility(View.VISIBLE);
            holder.icAvatar.setImageResource(R.drawable.ic_youtube_logo);
        }else if(mLisRatioCrop.get(position).getType().contains("Twitter")){
            holder.icAvatar.setVisibility(View.VISIBLE);
            holder.icAvatar.setImageResource(R.drawable.ic_twitter_logo);
        } else {
            holder.icAvatar.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ViewGroup.LayoutParams params = mRecycler.getLayoutParams();
//                params.height = (int) ((int) heightDefaultItem + mContext.getResources().getDimension(R.dimen._5sdp)*2);
//                mRecycler.setLayoutParams(params);
//                mRecycler.requestLayout();
                prevPos = indexPos;
                indexPos = position;
                if(mMirrorListener != null){
                    mMirrorListener.onItemMirrorRatioClickListener(indexPos);
                }
                notifyItemChanged(indexPos);
                notifyItemChanged(prevPos);
            }
        });

         if(indexPos == position){
             holder.viewItemCrop.setBackgroundResource(R.drawable.stroke_item_ratio_select);
         }else {
             holder.viewItemCrop.setBackgroundResource(R.drawable.stroke_item_ratio_default);
         }
         float height = 0;
         float with = 0;
         if(mLisRatioCrop.get(position).getRatioW() > mLisRatioCrop.get(position).getRatioH()){
             height =  mContext.getResources().getDimension(R.dimen._40sdp);
             with = (mLisRatioCrop.get(position).getRatioW() *height)/mLisRatioCrop.get(position).getRatioH() ;
         }else {
             with =  mContext.getResources().getDimension(R.dimen._40sdp);
             height = (mLisRatioCrop.get(position).getRatioH() *with)/mLisRatioCrop.get(position).getRatioW() ;
         }

        if(mLisRatioCrop.get(position).getRatioW()  == Statics.FIT_IMAGE){
            with =  mContext.getResources().getDimension(R.dimen._40sdp);
            height = (2*with)/1;
        }else if(mLisRatioCrop.get(position).getRatioW()  == Statics.CIRCLE){
            height =  mContext.getResources().getDimension(R.dimen._40sdp);
            with =  mContext.getResources().getDimension(R.dimen._40sdp);
        }else if(mLisRatioCrop.get(position).getRatioW()  == Statics.FREE_IMAGE){
            with =  mContext.getResources().getDimension(R.dimen._40sdp);
            height = (3*with)/2;
        }

        ViewGroup.LayoutParams params = holder.viewItemCrop.getLayoutParams();
        params.height = (int) height;
        params.width = (int) with;
        holder.viewItemCrop.setLayoutParams(params);
        holder.viewItemCrop.requestLayout();
//        if(height > heightDefaultItem){
//            heightDefaultItem = (int) height;
//        }
    }

    @Override
    public int getItemCount() {
        return mLisRatioCrop.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNameRatio;
        private ConstraintLayout viewItemCrop;
        private ImageView icAvatar;
        public ViewHolder(View itemView) {
            super(itemView);
            txtNameRatio = (TextView) itemView.findViewById(R.id.txt_name_ratio);
            viewItemCrop =  itemView.findViewById(R.id.view_item_crop);
            icAvatar =  itemView.findViewById(R.id.ic_avatar);
        }
    }

    public interface OnItemMirrorListener{
        void onItemMirrorRatioClickListener(int pos);
    }

}
