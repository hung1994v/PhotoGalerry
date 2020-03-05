package bsoft.com.lib_filter.filter.adapter.filter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;
import java.util.List;

import bsoft.com.lib_filter.R;
import bsoft.com.lib_filter.filter.border.BorderImageView;
import bsoft.com.lib_filter.filter.gpu.AsyncSizeProcess;
import bsoft.com.lib_filter.filter.gpu.GPUFilterRes;
import bsoft.com.lib_filter.filter.listener.OnFilterFinishedListener;
import bsoft.com.lib_filter.filter.listener.OnPostFilteredListener;
import bsoft.com.lib_filter.filter.listener.WBAsyncPostIconListener;
import bsoft.com.lib_filter.filter.model.FilterChild;

public class FilterNewAdapter extends RecyclerView.Adapter<FilterNewAdapter.ViewHolder> {
    private Context mContext;
    private List<FilterChild> mListFilterChild ;
    private OnItemChildListener mOnItemChildListener;
    private int mCurrentPos = 0;
    private int prevPos = -1;
    private Bitmap mBitmapFilter;
    private ArrayList<String> mListTitleFilter = new ArrayList<>();
    private List<GPUFilterRes> mGpuFilterRes;

    public void setOnItemChildListener(OnItemChildListener listener) {
        mOnItemChildListener = listener;
    }

    public FilterNewAdapter(Context context, ArrayList<String> listTitleFilter,List<GPUFilterRes> gpuFilterRes, List<FilterChild> filterChildren, Bitmap bitmap) {
        mContext = context;
        mListFilterChild = filterChildren;
        mBitmapFilter = bitmap;
        mListTitleFilter = listTitleFilter;
        mGpuFilterRes = gpuFilterRes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.filter_new_horizontal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        SquareUiLidowFilterManager mFiterManager = new SquareUiLidowFilterManager(mContext, mListFilterChild.get(position).getCurrentParent(), "");
//        GPUFilterRes res = (GPUFilterRes) mFiterManager.getRes(position);
        AsyncSizeProcess.executeAsyncFilter(mContext, mBitmapFilter, mGpuFilterRes.get(position), null, null, new AnonymousClass1(new OnFilterFinishedListener() {
            @Override
            public void postFinished(Bitmap bitmap1) {
                Glide.with(mContext)
                        .asBitmap()
                        .load(bitmap1)
                        .transform(new CenterCrop(),new RoundedCorners(7))
                        .into(holder.filterImgChild);
            }
        }));
        String name = mListTitleFilter.get(mListFilterChild.get(position).getCurrentParent()).substring(0,3)+"-" + position;
        if(mListFilterChild.get(position).getCurrentParent() == 0){
            holder.txtFilterName.setText(mContext.getResources().getString(R.string.none));
        }else {
            holder.txtFilterName.setText(name);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevPos = mCurrentPos;
                mCurrentPos = position;
                if(mOnItemChildListener != null){
                    mOnItemChildListener.onItemChildClickListener(mListFilterChild.get(position).getCurrentParent(),position);
                }
                notifyItemChanged(mCurrentPos);
                notifyItemChanged(prevPos);
            }
        });
        if(position == mCurrentPos){
            holder.txtFilterName.setBackgroundColor(ContextCompat.getColor(mContext,android.R.color.transparent));
        }else {
            holder.txtFilterName.setBackgroundColor(ContextCompat.getColor(mContext,android.R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        return mListFilterChild.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        BorderImageView filterImgChild;
        TextView txtFilterName;
        public ViewHolder(View itemView) {
            super(itemView);
            filterImgChild = itemView.findViewById(R.id.filter_img_child);
            txtFilterName = itemView.findViewById(R.id.txt_filter_name);
        }
    }


    class AnonymousClass1 implements OnPostFilteredListener {
        private OnFilterFinishedListener val$listener;

        AnonymousClass1(OnFilterFinishedListener onFilterFinishedListener) {
            val$listener = onFilterFinishedListener;
        }

        public void postFiltered(Bitmap result) {
            val$listener.postFinished(result);
        }
    }

    public interface OnItemChildListener {
        void onItemChildClickListener(int parent, int child);
    }
}
