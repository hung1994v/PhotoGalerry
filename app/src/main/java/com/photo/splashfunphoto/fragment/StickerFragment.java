package com.photo.splashfunphoto.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.gallery.R;
import com.photo.splashfunphoto.adapter.IconStickerAdapter;
import com.photo.splashfunphoto.adapter.StickerAdapter;
import com.photo.splashfunphoto.utils.ConstList;

import java.util.ArrayList;
import java.util.List;


public class StickerFragment extends BaseFragment implements StickerAdapter.OnStickerListener, View.OnClickListener {
    private RecyclerView mRSticker;
    private IconStickerAdapter.OnIconListener mIconListener;
    private RelativeLayout mContainerSticker;
    private ArrayList<String> mListIcon;
    private int mView;
    private Bundle bundle;
    ArrayList<String> listSticker = new ArrayList<>();
    private OnStickerListener mOnStickerListener;
    private RecyclerView mRvStickerChild;


    public static StickerFragment newInstance(Bundle bundle, ArrayList<String> list, IconStickerAdapter.OnIconListener listener, OnStickerListener onStickerListener, int id) {
        StickerFragment fragment = new StickerFragment();
        fragment.mView = id;
        fragment.mIconListener = listener;
        fragment.mListIcon = list;
        fragment.mOnStickerListener = onStickerListener;
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void backPressed() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sticker_crp, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initRecyclerView();
    }

    private void initRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 5);
        mRSticker.setLayoutManager(layoutManager);
        StickerAdapter stickerAdapter = new StickerAdapter(getActivity(), mListIcon).setOnStickerListener(this);
        mRSticker.setAdapter(stickerAdapter);

        getListStickerChild(1);
        IconStickerAdapter iconAdapter = new IconStickerAdapter(getActivity(), listSticker).setOnIconListener(mIconListener);
        mRvStickerChild.setAdapter(iconAdapter);
        GridLayoutManager layoutManager2 = new GridLayoutManager(getContext(),6, LinearLayoutManager.VERTICAL, false);
        mRvStickerChild.setLayoutManager(layoutManager2);
    }

    private void initView() {
        mRSticker = (RecyclerView) getView().findViewById(R.id.sticker_rview);
        mRvStickerChild = (RecyclerView) getView().findViewById(R.id.rv_sticker_child);
        getView().findViewById(R.id.btn_done).setOnClickListener(this);
        mContainerSticker = (RelativeLayout) getView().findViewById(R.id.sticker_bottombar);
    }

    private void getListStickerChild(int pos){
        List<String> tmpListSticker = new ArrayList<>();
        switch (pos) {
            case 1:
                tmpListSticker.clear();
                listSticker.clear();
                tmpListSticker = ConstList.listLayout.get(ConstList.KEY_STICKER + 1);
                for (int i = 0; i < tmpListSticker.size(); i++) {
                    listSticker.add(tmpListSticker.get(i));
                }
                break;
            case 2:
                tmpListSticker.clear();
                listSticker.clear();
                tmpListSticker = ConstList.listLayout.get(ConstList.KEY_STICKER + 2);
                for (int i = 0; i < tmpListSticker.size(); i++) {
                    listSticker.add(tmpListSticker.get(i));
                }
                break;
            case 3:
                tmpListSticker.clear();
                listSticker.clear();
                tmpListSticker = ConstList.listLayout.get(ConstList.KEY_STICKER + 3);
                for (int i = 0; i < tmpListSticker.size(); i++) {
                    listSticker.add(tmpListSticker.get(i));
                }
                break;
            case 4:
                tmpListSticker.clear();
                listSticker.clear();
                tmpListSticker = ConstList.listLayout.get(ConstList.KEY_STICKER + 4);
                for (int i = 0; i < tmpListSticker.size(); i++) {
                    listSticker.add(tmpListSticker.get(i));
                }
                break;
            case 5:
                tmpListSticker.clear();
                listSticker.clear();
                tmpListSticker = ConstList.listLayout.get(ConstList.KEY_STICKER + 5);
                for (int i = 0; i < tmpListSticker.size(); i++) {
                    listSticker.add(tmpListSticker.get(i));
                }
                break;
            case 6:
                tmpListSticker.clear();
                listSticker.clear();
                tmpListSticker = ConstList.listLayout.get(ConstList.KEY_STICKER + 6);
                for (int i = 0; i < tmpListSticker.size(); i++) {
                    listSticker.add(tmpListSticker.get(i));
                }
                break;

            case 7:
                tmpListSticker.clear();
                listSticker.clear();
                tmpListSticker = ConstList.listLayout.get(ConstList.KEY_STICKER + 7);
                for (int i = 0; i < tmpListSticker.size(); i++) {
                    listSticker.add(tmpListSticker.get(i));
                }
                break;

            case 8:
                tmpListSticker.clear();
                listSticker.clear();
                tmpListSticker = ConstList.listLayout.get(ConstList.KEY_STICKER + 8);
                for (int i = 0; i < tmpListSticker.size(); i++) {
                    listSticker.add(tmpListSticker.get(i));
                }
                break;

            case 9:
                tmpListSticker.clear();
                listSticker.clear();
                tmpListSticker = ConstList.listLayout.get(ConstList.KEY_STICKER + 9);
                for (int i = 0; i < tmpListSticker.size(); i++) {
                    listSticker.add(tmpListSticker.get(i));
                }
                break;

            case 10:
                tmpListSticker.clear();
                listSticker.clear();
                tmpListSticker = ConstList.listLayout.get(ConstList.KEY_STICKER + 10);
                for (int i = 0; i < tmpListSticker.size(); i++) {
                    listSticker.add(tmpListSticker.get(i));
                }
                break;

            case 11:
                tmpListSticker.clear();
                listSticker.clear();
                tmpListSticker = ConstList.listLayout.get(ConstList.KEY_STICKER + 11);
                for (int i = 0; i < tmpListSticker.size(); i++) {
                    listSticker.add(tmpListSticker.get(i));
                }
                break;

            case 12:
                tmpListSticker.clear();
                listSticker.clear();
                tmpListSticker = ConstList.listLayout.get(ConstList.KEY_STICKER + 12);
                for (int i = 0; i < tmpListSticker.size(); i++) {
                    listSticker.add(tmpListSticker.get(i));
                }
                break;

            case 13:
                tmpListSticker.clear();
                listSticker.clear();
                tmpListSticker = ConstList.listLayout.get(ConstList.KEY_STICKER + 13);
                for (int i = 0; i < tmpListSticker.size(); i++) {
                    listSticker.add(tmpListSticker.get(i));
                }
                break;

            case 14:
                tmpListSticker.clear();
                listSticker.clear();
                tmpListSticker = ConstList.listLayout.get(ConstList.KEY_STICKER + 14);
                for (int i = 0; i < tmpListSticker.size(); i++) {
                    listSticker.add(tmpListSticker.get(i));
                }
                break;

            case 15:
                tmpListSticker.clear();
                listSticker.clear();
                tmpListSticker = ConstList.listLayout.get(ConstList.KEY_STICKER + 15);
                for (int i = 0; i < tmpListSticker.size(); i++) {
                    listSticker.add(tmpListSticker.get(i));
                }
                break;
        }
    }

    @Override
    public void onItemStickerClickListener(int position) {
        int pos = position + 1;
        getListStickerChild(pos);
        IconStickerAdapter iconAdapter = new IconStickerAdapter(getActivity(), listSticker).setOnIconListener(mIconListener);
        mRvStickerChild.setAdapter(iconAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),6, LinearLayoutManager.VERTICAL, false);
        mRvStickerChild.setLayoutManager(layoutManager);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_done:
                if (mOnStickerListener != null){
                    mOnStickerListener.clickDoneSticker();
                }
                break;
        }
    }

    public interface OnStickerListener{
        void clickDoneSticker();
    }
}




