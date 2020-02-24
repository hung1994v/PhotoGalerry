package bsoft.com.lib_filter.filter.adapter.filter;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import bsoft.com.lib_filter.R;
import bsoft.com.lib_filter.filter.gpu.GPUFilterRes;
import bsoft.com.lib_filter.filter.recycler.ExpandableRecyclerAdapter;
import bsoft.com.lib_filter.filter.model.FilterHorizontalChild;
import bsoft.com.lib_filter.filter.model.FilterHorizontalParent;


/**
 * Created by Computer on 7/5/2017.
 */

public class HorizontalExpandableAdapter extends ExpandableRecyclerAdapter<FilterHorizontalParent, FilterHorizontalChild, FilterHorizontalParentViewHolder, FilterHorizontalChildViewHolder> {
    private LayoutInflater mInflater;
    private ArrayList<GPUFilterRes> mChildList = new ArrayList<>();
    private OnItemChildListener mOnItemChildListener;
    private int mSelectBorderColor = Color.rgb(0, 235, 232);
    private int current = -1;
    private Context mContext;

    public HorizontalExpandableAdapter setOnItemChildListener(OnItemChildListener listener) {
        mOnItemChildListener = listener;
        return this;
    }

    public HorizontalExpandableAdapter(Context context, @NonNull List<FilterHorizontalParent> parentList, ArrayList<GPUFilterRes> list) {
        super(parentList);
        mContext = context;
        mChildList = list;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public FilterHorizontalParentViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View view = mInflater.inflate(R.layout.filter_parent_horizontal, parentViewGroup, false);
        return new FilterHorizontalParentViewHolder(view);
    }

    @NonNull
    @Override
    public FilterHorizontalChildViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View view = mInflater.inflate(R.layout.filter_child_horizontal, childViewGroup, false);
        return new FilterHorizontalChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(@NonNull final FilterHorizontalParentViewHolder parentViewHolder, final int parentPosition, @NonNull FilterHorizontalParent parent) {
        parentViewHolder.bind(parent.getPath());
    }

    @Override
    public void onBindChildViewHolder(@NonNull final FilterHorizontalChildViewHolder childViewHolder, int parentPosition, final int childPosition, @NonNull FilterHorizontalChild child) {
        childViewHolder.bind(child.getGpuFilterRes());
        childViewHolder.imgFilterChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemChildListener != null) {
                    try {
                        mOnItemChildListener.onItemChildClick(childViewHolder.getParentAdapterPosition(), childViewHolder.getChildAdapterPosition());
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }

                }
            }
        });


    }

    public interface OnItemChildListener {
        void onItemChildClick(int parent, int child);
    }


}
