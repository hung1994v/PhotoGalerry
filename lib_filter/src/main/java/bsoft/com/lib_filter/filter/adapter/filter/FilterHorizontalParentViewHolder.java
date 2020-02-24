package bsoft.com.lib_filter.filter.adapter.filter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import bsoft.com.lib_filter.R;
import bsoft.com.lib_filter.filter.recycler.ParentViewHolder;


public class FilterHorizontalParentViewHolder extends ParentViewHolder {
    private static final boolean HONEYCOMB_AND_ABOVE = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    public ImageView imgParent;

    public FilterHorizontalParentViewHolder(@NonNull View itemView) {
        super(itemView);
        imgParent = (ImageView) itemView.findViewById(R.id.filter_img_parent);
        imgParent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded()) {
                    collapseView();
                } else {
                    expandView();
                }
            }
        });

        final Context context = itemView.getContext();
        itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void bind(String path) {
        ImageLoader.getInstance().displayImage("assets://" + path, imgParent);
    }

    @SuppressLint("NewApi")
    @Override
    public void setExpanded(boolean expanded) {
        super.setExpanded(expanded);
        if (!HONEYCOMB_AND_ABOVE) {
            return;
        }
    }

    @Override
    public void onExpansionToggled(boolean expanded) {
        super.onExpansionToggled(expanded);
        if (!HONEYCOMB_AND_ABOVE) {
            return;
        }

    }

    @Override
    public boolean shouldItemViewClickToggleExpansion() {
        return false;
    }
}