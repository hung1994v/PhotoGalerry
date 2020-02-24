package com.photo.gallery.fragments.options;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.photo.gallery.R;
import com.photo.gallery.adapters.options.SplashAdapter;
import com.photo.gallery.ui.options.splash.SplashShapeView;
import com.photo.gallery.utils.ConstValue;
import com.photo.gallery.utils.SharedPrefUtil;
import com.photo.gallery.utils.options.Statics;

/**
 * Created by Tung on 3/30/2018.
 */

public class SplashFragment extends BaseOptFragment implements SplashAdapter.OnItemSplashListener {

    private RecyclerView mRecyclerView;
    private SplashAdapter mAdapter = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        applyColor(view);
        initViews();
        setupRecyclerview();

        if (listener!=null) {
            listener.onSplashFragmentReady();
        }
    }

    public void applyColor(View view) {
        int defaultPrimary = ContextCompat.getColor(mContext, R.color.colorPrimary);
        int colorPrimary = SharedPrefUtil.getInstance().getInt(ConstValue.EXTRA_CURRENT_COLOR_PICKER, defaultPrimary);

        ViewGroup viewGroup = view.findViewById(R.id.recycler_view);
        com.photo.gallery.utils.Utils.setColorViews(colorPrimary, viewGroup);
    }

    private void initViews() {

        View viewParent = getView();
        if (viewParent == null) {
            return;
        }
        viewParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // do nothing.
            }
        });

        mRecyclerView = viewParent.findViewById(R.id.recycler_view);
    }

    public void initData() {

        Statics.loadImageSplash();
        mAdapter = new SplashAdapter(mContext, Statics.mListSplash).setOnItemSplashListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setupRecyclerview() {
        LinearLayoutManager lm = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(lm);

//        initList();

    }

    private OnSplashListener listener = null;

    public SplashFragment setListener(OnSplashListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public void onItemSplashClickListener(int prevPos, int position) {
        if (listener!=null) {
            listener.onItemSplashClickListener(prevPos, position);
        }
        mAdapter.notifyItemChanged(position);
        mAdapter.notifyItemChanged(prevPos);
    }

    @Override
    public void onChangeClickListener(int position) {
        if (listener!=null) {
            listener.onChangeClickListener(position);
        }
    }

    @Override
    public void onStypeClickListener(SplashShapeView.StyleMode btnMode) {
        if (listener!=null) {
            listener.onStypeClickListener(btnMode);
        }
    }

    public interface OnSplashListener {
        public void onSplashFragmentReady();
        public void onItemSplashClickListener(int prevPos, int position);
        public void onChangeClickListener(int position);
        public void onStypeClickListener(SplashShapeView.StyleMode btnMode);
    }
}
