package bsoft.com.lib_filter.filter.adapter.filter;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import bsoft.com.lib_filter.R;
import bsoft.com.lib_filter.filter.border.BorderImageView;
import bsoft.com.lib_filter.filter.gpu.AsyncSizeProcess;
import bsoft.com.lib_filter.filter.gpu.GPUFilterRes;
import bsoft.com.lib_filter.filter.gpu.SquareUiLidowFilterManager;
import bsoft.com.lib_filter.filter.listener.OnFilterFinishedListener;
import bsoft.com.lib_filter.filter.listener.OnPostFilteredListener;
import bsoft.com.lib_filter.filter.listener.WBAsyncPostIconListener;
import bsoft.com.lib_filter.filter.recycler.ChildViewHolder;


public class FilterHorizontalChildViewHolder extends ChildViewHolder {

    private static final String TAG = "HorizontalChildVH";
    public BorderImageView imgFilterChild;

    public FilterHorizontalChildViewHolder(@NonNull View itemView) {
        super(itemView);
        imgFilterChild = (BorderImageView) itemView.findViewById(R.id.filter_img_child);


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void bind(GPUFilterRes res, final Context mContext,Bitmap mBitmapFilter) {
        GPUFilterRes posRes = res;
        AsyncSizeProcess.executeAsyncFilter(mContext, mBitmapFilter, res, null, null, new AnonymousClass1(new OnFilterFinishedListener() {
            @Override
            public void postFinished(Bitmap bitmap1) {
                Glide.with(mContext)
                        .asBitmap()
                        .load(bitmap1)
                        .transform(new CenterCrop(),new RoundedCorners(7))
                        .into(imgFilterChild);
            }
        }));
//        GPUFilterRes posRes = res;
//        posRes.getAsyncIconBitmap(new FilterImage());
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


    class FilterImage implements WBAsyncPostIconListener {
        public FilterImage() {

        }

        @Override
        public void postIcon(Bitmap bitmap) {
            Log.d("postIcon    ", " " + bitmap);
            if (bitmap != null) {
                imgFilterChild.setImageBitmap(bitmap);
            }

        }
    }
}
