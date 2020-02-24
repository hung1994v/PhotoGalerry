package bsoft.com.lib_filter.filter.adapter.filter;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.View;

import bsoft.com.lib_filter.R;
import bsoft.com.lib_filter.filter.border.BorderImageView;
import bsoft.com.lib_filter.filter.gpu.GPUFilterRes;
import bsoft.com.lib_filter.filter.listener.WBAsyncPostIconListener;
import bsoft.com.lib_filter.filter.recycler.ChildViewHolder;


public class FilterHorizontalChildViewHolder extends ChildViewHolder {

    private static final String TAG = "HorizontalChildVH";
    public BorderImageView imgChild;
    public BorderImageView imgFilterChild;

    public FilterHorizontalChildViewHolder(@NonNull View itemView) {
        super(itemView);
        imgChild = (BorderImageView) itemView.findViewById(R.id.img_child);
        imgFilterChild = (BorderImageView) itemView.findViewById(R.id.filter_img_child);


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void bind(GPUFilterRes res) {
        GPUFilterRes posRes = res;
        posRes.getAsyncIconBitmap(new FilterImage());
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
