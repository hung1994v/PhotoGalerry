package bsoft.healthy.tracker.menstrual.lib_sticker.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import bsoft.healthy.tracker.menstrual.lib_sticker.R;

/**
 * Created by Hoavt on 11/29/2017.
 */

public class MyRecyclerviewAdapter extends RecyclerView.Adapter<MyRecyclerviewAdapter.MyViewHolder> {

    private static final String TAG = MyRecyclerviewAdapter.class.getSimpleName();
    private Context mContext;
    private List<Integer> mList = new ArrayList<>();
    private OnItemOptionClickListener listener = null;

    public MyRecyclerviewAdapter(Context context, List<Integer> list) {
        mContext = context;
        this.mList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_imageview, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Integer item = mList.get(position);
        holder.ivIcon.setImageResource(item.intValue());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemOptionClicked(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public MyRecyclerviewAdapter setListener(OnItemOptionClickListener listener) {
        this.listener = listener;
        return this;
    }

    public interface OnItemOptionClickListener {
        void onItemOptionClicked(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivIcon;

        public MyViewHolder(View view) {
            super(view);

            ivIcon = (ImageView) view.findViewById(R.id.imageview);

        }
    }
}
