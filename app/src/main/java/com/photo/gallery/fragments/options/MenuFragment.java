package com.photo.gallery.fragments.options;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.photo.gallery.R;
import com.photo.gallery.adapters.EditPhotoAdapter;
import com.photo.gallery.models.OptionItem;
import com.photo.gallery.utils.ConstValue;
import com.photo.gallery.utils.SharedPrefUtil;

import java.util.ArrayList;

/**
 * Created by Tung on 3/30/2018.
 */

public class MenuFragment extends BaseOptFragment implements EditPhotoAdapter.OnEditPhotoAdapterListener {

    private RecyclerView mRecyclerView;
    private ArrayList<OptionItem> mList = new ArrayList<>();
    private EditPhotoAdapter mAdapter = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        applyColor(view);
        initViews();
        setupRecyclerview();
    }

    public void applyColor(View view) {
        int defaultPrimary = ContextCompat.getColor(mContext, R.color.s_black);
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

    private void setupRecyclerview() {
//        LinearLayoutManager lm = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
//        Context context;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 5);
        mRecyclerView.setLayoutManager(gridLayoutManager);


        initList();
        mAdapter = new EditPhotoAdapter(mContext, mList).setListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initList() {
        if (mList==null) {
            mList = new ArrayList<>();
        }
        mList.clear();

        int[] resIDs = new int[]{
                R.drawable.ic_filter,
                R.drawable.ic_btn_splash,
                R.drawable.ic_editor,
                R.drawable.ic_txt_collage,
                R.drawable.ic_btn_sticker,
        };
        for (int resID : resIDs) {
            OptionItem item = new OptionItem();
            item.resID = resID;
            mList.add(item);
        }
    }

    @Override
    public void onItemOptionClicked(int position, boolean isCheck) {
        switch (position) {
            case EditPhotoAdapter.OPTION_SPLASH:
                for(OptionItem optionItem: mList){
                    optionItem.setCheck(false);
                }
                mList.get(position).setCheck(isCheck);
                if (listener!=null) {
                    listener.onOpenSplash();
                }
                mAdapter.notifyDataSetChanged();
                break;
            case EditPhotoAdapter.OPTION_FILTER:
                for(OptionItem optionItem: mList){
                    optionItem.setCheck(false);
                }
                mList.get(position).setCheck(isCheck);
                if (listener!=null) {
                    listener.onOpenFilter();
                }
                mAdapter.notifyDataSetChanged();
                break;
//            case EditPhotoAdapter.OPTION_PIP:
//                if (listener!=null) {
//                    listener.onOpenPip();
//                }
//                break;
            case EditPhotoAdapter.OPTION_STICKER:
                for(OptionItem optionItem: mList){
                    optionItem.setCheck(false);
                }
                mList.get(position).setCheck(isCheck);
                if (listener!=null) {
                    listener.onOpenSticker();
                }
                mAdapter.notifyDataSetChanged();
                break;
            case EditPhotoAdapter.OPTION_TEXT:
                for(OptionItem optionItem: mList){
                    optionItem.setCheck(false);
                }
                mList.get(position).setCheck(isCheck);
                if (listener!=null) {
                    listener.onOpenText();
                }
                mAdapter.notifyDataSetChanged();
                break;
            case EditPhotoAdapter.EDIT:
                for(OptionItem optionItem: mList){
                    optionItem.setCheck(false);
                }
                mList.get(position).setCheck(isCheck);
                if (listener!=null) {
                    listener.onEdit();
                }
                mAdapter.notifyDataSetChanged();
                break;
        }
    }

    private OnMenuListener listener = null;

    public MenuFragment setListener(OnMenuListener listener) {
        this.listener = listener;
        return this;
    }

    public interface OnMenuListener {
        void onOpenSplash();
        void onOpenFilter();
        void onOpenPip();
        void onOpenSticker();
        void onOpenText();
        void onEdit();
    }
}
