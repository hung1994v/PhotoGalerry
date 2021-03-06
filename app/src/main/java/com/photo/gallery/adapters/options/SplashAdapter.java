package com.photo.gallery.adapters.options;

import android.content.Context;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.photo.gallery.R;
import com.photo.gallery.ui.options.splash.SplashShapeView;

import java.util.ArrayList;

/**
 * Created by Computer on 2/23/2017.
 */

public class SplashAdapter extends RecyclerView.Adapter<SplashAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<String> mListIconSplash = new ArrayList<>();
    private OnItemSplashListener mSplashListener = null;
    private int mSelectImgSplash;
    private SplashShapeView.StyleMode styleBtnMode;
    private int stypeId = 1;

    public SplashAdapter(Context context, ArrayList<String> list) {
        mContext = context;
        mListIconSplash = list;
    }

    public SplashAdapter setOnItemSplashListener(OnItemSplashListener listener) {
        mSplashListener = listener;
        return this;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.splash_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Glide.with(mContext)
                .load(Uri.parse("file:///android_asset/" + mListIconSplash.get(position)))
                .into(holder.mIgSplsh);
        holder.mIgSplsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSplashListener != null) {
                    int prevPos = mSelectImgSplash;
                    mSelectImgSplash = position;
                    mSplashListener.onItemSplashClickListener(prevPos, position);
                }
            }
        });
        holder.mIgStype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stypeId == 3) {
                    stypeId = 0;
                }
                stypeId++;
                setStypeMode(holder);
                if (mSplashListener != null) {
                    mSplashListener.onStypeClickListener(styleBtnMode);
                }
            }
        });
        holder.mIgChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSplashListener != null) {
                    mSplashListener.onChangeClickListener(position);
                }
            }
        });
        if (position == mSelectImgSplash) {
            setStypeMode(holder);
            holder.mContainerStype.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(R.drawable.splash_i2)
                    .into(holder.mIgSplashPress);
            holder.mIgSplsh.setVisibility(View.INVISIBLE);
            holder.mIgSplashPress.setVisibility(View.VISIBLE);
        } else {
            holder.mContainerStype.setVisibility(View.GONE);
            Glide.with(mContext)
                    .load(Uri.parse("file:///android_asset/" + mListIconSplash.get(position)))
                    .into(holder.mIgSplsh);
            holder.mIgSplsh.setVisibility(View.VISIBLE);
            holder.mIgSplashPress.setVisibility(View.INVISIBLE);
        }
    }

    private void setStypeMode(ViewHolder holder) {
        if (stypeId == 1) {
            styleBtnMode = SplashShapeView.StyleMode.B_W;
            holder.mIgStype.setImageResource(R.drawable.img_splash_style_1);
        } else if (stypeId == 2) {
            styleBtnMode = SplashShapeView.StyleMode.MOSAIC;
            holder.mIgStype.setImageResource(R.drawable.img_splash_style_2);
        } else if (stypeId == 3) {
            styleBtnMode = SplashShapeView.StyleMode.POLKA_DOT;
            holder.mIgStype.setImageResource(R.drawable.img_splash_style_3);
        }
    }

    @Override
    public int getItemCount() {
        return mListIconSplash.size();
    }

    public interface OnItemSplashListener {
        void onItemSplashClickListener(int prevPos, int position);

        void onChangeClickListener(int position);

        void onStypeClickListener(SplashShapeView.StyleMode btnMode);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mIgSplsh;
        private ImageView mIgSplashPress;
        private ImageView mIgStype;
        private ImageView mIgChange;
        private LinearLayout mContainerStype;

        public ViewHolder(View itemView) {
            super(itemView);
            mIgSplsh = (ImageView) itemView.findViewById(R.id.img_splash);
            mIgSplashPress = (ImageView) itemView.findViewById(R.id.img_splash_press);
            mIgStype = (ImageView) itemView.findViewById(R.id.img_stype);
            mIgChange = (ImageView) itemView.findViewById(R.id.img_change);
            mContainerStype = (LinearLayout) itemView.findViewById(R.id.container_stype);
        }
    }
}
