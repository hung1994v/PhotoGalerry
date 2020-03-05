package com.photo.splashfunphoto.adapter.text;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.photo.gallery.R;
import com.photo.splashfunphoto.listener.OnMenuBackgroundListener;

import java.util.ArrayList;

public class PatternAdapter extends RecyclerView.Adapter<PatternAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<String> mListPattern1  = new ArrayList<>();
    private ArrayList<String> mListPattern2  = new ArrayList<>();
    private OnMenuBackgroundListener mMenuListener = null;


    public PatternAdapter(Context context ,ArrayList<String> list1,ArrayList<String> list2){
        mContext = context;
        mListPattern1 = list1;
        mListPattern2 = list2;
    }

    public PatternAdapter setOnMenuBackgroundListener(OnMenuBackgroundListener listener){
        mMenuListener = listener ;
        return this;
    }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.pattern_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String path1 = "file:///android_asset/" + mListPattern1.get(position);
        Glide.with(mContext)
                .asBitmap()
                .load(path1)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.mImageViewPattern1);

        String path2 = "file:///android_asset/" + mListPattern2.get(position);

        Glide.with(mContext)
                .asBitmap()
                .load(path2)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.mImageViewPattern2);

        final String mPatternFileName1 = mListPattern1.get(position);
        final String mPatternFileName2 = mListPattern2.get(position);
        holder.mImageViewPattern1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMenuListener != null){
                    mMenuListener.onMenuPatternClickListener(mPatternFileName1);
                }
            }
        });

        holder.mImageViewPattern2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMenuListener != null){
                    mMenuListener.onMenuPatternClickListener(mPatternFileName2);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListPattern1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageViewPattern1,mImageViewPattern2;
        public ViewHolder(View itemView) {
            super(itemView);
            mImageViewPattern1 = (ImageView) itemView.findViewById(R.id.img_pattern_1);
            mImageViewPattern2 = (ImageView) itemView.findViewById(R.id.img_pattern_2);
        }
    }
}
