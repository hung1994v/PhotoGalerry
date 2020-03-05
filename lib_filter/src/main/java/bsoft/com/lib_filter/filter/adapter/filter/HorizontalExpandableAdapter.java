package bsoft.com.lib_filter.filter.adapter.filter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import androidx.annotation.NonNull;

import android.util.Log;
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
    private ArrayList<GPUFilterRes> mChildList ;
    private OnItemChildListener mOnItemChildListener;
    private int mSelectBorderColor = Color.rgb(0, 235, 232);
    private int current = -1;
    private Context mContext;
    private int currentParentPosition = -1;
    private int prvParentPosition = 0;
    private Bitmap mBitmapFilter;

    public HorizontalExpandableAdapter setOnItemChildListener(OnItemChildListener listener) {
        mOnItemChildListener = listener;
        return this;
    }

    public HorizontalExpandableAdapter(Context context, @NonNull List<FilterHorizontalParent> parentList, ArrayList<GPUFilterRes> list, Bitmap bitmap) {
        super(parentList);
        mContext = context;
        mChildList = list;
        mInflater = LayoutInflater.from(context);
        mBitmapFilter = bitmap;
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
    public void onBindChildViewHolder(@NonNull final FilterHorizontalChildViewHolder childViewHolder, final int parentPosition, final int childPosition, @NonNull FilterHorizontalChild child) {
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_border_layout);
        childViewHolder.bind(child.getGpuFilterRes(),mContext,mBitmapFilter);
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

                prvParentPosition = currentParentPosition;

                currentParentPosition = parentPosition;
                current = childPosition;
                notifyParentChanged(currentParentPosition);
                if(prvParentPosition < 0) return;
                notifyParentChanged(prvParentPosition);
            }
        });

        if (childPosition == current && currentParentPosition == parentPosition) {
            childViewHolder.imgFilterChild.setBorderColor(mSelectBorderColor);
            childViewHolder.imgFilterChild.setShowBorder(true);
            childViewHolder.imgFilterChild.setBorderWidth(1.0f);
            childViewHolder.imgFilterChild.setShowImageBorder(true, bitmap);
        } else {
            childViewHolder.imgFilterChild.setShowBorder(false);
        }
    }

    public interface OnItemChildListener {
        void onItemChildClick(int parent, int child);
    }
}
