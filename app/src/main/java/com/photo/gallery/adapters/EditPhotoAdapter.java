package com.photo.gallery.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.photo.gallery.R;
import com.photo.gallery.models.OptionItem;

import java.util.ArrayList;

public class EditPhotoAdapter extends RecyclerView.Adapter<EditPhotoAdapter.MyViewHolder> {

    public static final int OPTION_SPLASH = 1;
    public static final int OPTION_FILTER = 0;
    public static final int OPTION_STICKER = 4;
    public static final int OPTION_TEXT = 3;
    public static final int EDIT = 2;


    private static final String TAG = EditPhotoAdapter.class.getSimpleName();
    private Context mContext = null;
    private ArrayList<OptionItem> mList = null;
    private OnEditPhotoAdapterListener listener = null;
    private int mLastPosition = -1;

    public EditPhotoAdapter(Context context, ArrayList<OptionItem> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate((R.layout.item_edit_photo), parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        MyViewHolder itemHolder = holder;

        // bind your view here
        final OptionItem item = mList.get(position);
        holder.ivIcon.setImageResource(item.resID);
        if(item.isCheck()){
            holder.tvLabel.setVisibility(View.VISIBLE);
        }else{
            holder.tvLabel.setVisibility(View.INVISIBLE);
        }

//        holder.tvLabel.setText(item.label);

        itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    if(mLastPosition!=position){
                        listener.onItemOptionClicked(position, !item.isCheck());
                        mLastPosition = position;
                    }else {
                        listener.onItemOptionClicked(position, item.isCheck());
                    }

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public EditPhotoAdapter setListener(OnEditPhotoAdapterListener listener) {
        this.listener = listener;
        return this;
    }

    public interface OnEditPhotoAdapterListener {

        void onItemOptionClicked(int position, boolean isCheck);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivIcon;
        private View tvLabel;

        public MyViewHolder(View itemView) {
            super(itemView);

            ivIcon = (ImageView) itemView.findViewById(R.id.iv_icon);
            tvLabel = itemView.findViewById(R.id.tv_label);
        }
    }
}