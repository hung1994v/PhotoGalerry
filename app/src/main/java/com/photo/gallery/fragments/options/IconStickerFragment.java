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
import com.photo.gallery.adapters.options.IconStickerAdapter;
import com.photo.gallery.adapters.options.StickerAdapter;
import com.photo.gallery.utils.ConstValue;
import com.photo.gallery.utils.SharedPrefUtil;
import com.photo.gallery.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tung on 4/2/2018.
 */

public class IconStickerFragment extends BaseOptFragment implements StickerAdapter.OnStickerListener, IconStickerAdapter.OnIconListener {

    private static final String TAG = IconStickerFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerViewDetails;
    private ArrayList<String> mListStickerDetails = new ArrayList<>();
    private IconStickerAdapter iconAdapter = null;
    public static boolean flag_show_details = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sticker_icon, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        applyColor(view);
        initViews();
        setupRecyclerview();

        if (listener!=null) {
            listener.onStickerIconFragmentReady();
        }
    }

    private void applyColor(View view) {
        int defaultPrimary = ContextCompat.getColor(mContext, R.color.colorPrimary);
        int colorPrimary = SharedPrefUtil.getInstance().getInt(ConstValue.EXTRA_CURRENT_COLOR_PICKER, defaultPrimary);
        ViewGroup rv1 = (ViewGroup)view.findViewById(R.id.recycler_view_details);
        ViewGroup rv2 = (ViewGroup)view.findViewById(R.id.recycler_view);
        Utils.setColorViews(colorPrimary, rv1, rv2);
    }

    public static final int MAX_STICER = 12;
    public static final String KEY_STICKER = "sticker";

    private Map<String, List<String>> listMap = null;

    public Map<String, List<String>> getListMap() {
        return listMap;
    }

    public void initData() {
        
        listMap = new HashMap<>();
        for (int i = 1; i <= MAX_STICER; i++) {
            listMap.put(KEY_STICKER + (i), loadStickerFromAsset(i));
        }

        ArrayList<String> lisStickerIcon = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            List<String> mListIconSticker = listMap.get(KEY_STICKER + i);
            lisStickerIcon.add(mListIconSticker.get(0));
        }
        StickerAdapter stickerAdapter = new StickerAdapter(mContext, lisStickerIcon).setOnStickerListener(this);
        mRecyclerView.setAdapter(stickerAdapter);

        showDetails(false);
    }

    private ArrayList<String> loadStickerFromAsset(int numSticker) {
        ArrayList<String> list = new ArrayList<>();
        switch (numSticker) {
            case 1:
                list.clear();
                for (int i = 1; i <= 39; i++) {
                    list.add("sticker/1/" + i + ".png");
                }
                break;
            case 2:
                list.clear();
                for (int i = 1; i <= 40; i++) {
                    list.add("sticker/2/" + i + ".png");
                }
                break;
            case 3:
                list.clear();
                for (int i = 1; i <= 49; i++) {
                    list.add("sticker/3/" + i + ".png");
                }
                break;
            case 4:
                list.clear();
                for (int i = 1; i <= 35; i++) {
                    list.add("sticker/4/" + i + ".png");
                }
                break;
            case 5:
                list.clear();
                for (int i = 1; i <= 56; i++) {
                    list.add("sticker/5/" + i + ".png");
                }
                break;
            case 6:
                list.clear();
                for (int i = 1; i <= 36; i++) {
                    list.add("sticker/6/" + i + ".png");
                }
                break;
        }
        return list;

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
        mRecyclerViewDetails = viewParent.findViewById(R.id.recycler_view_details);
    }

    private void setupRecyclerview() {
        LinearLayoutManager lm = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(lm);

        LinearLayoutManager lmDetails = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewDetails.setLayoutManager(lmDetails);
    }

    private OnStickerIconListener listener = null;

    public IconStickerFragment setListener(OnStickerIconListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public void onItemStickerClickListener(int position) {
        int pos = position + 1;
        List<String> tmpListSticker = new ArrayList<>();
        ArrayList<String> listSticker = new ArrayList<>();
        switch (pos) {
            case 1:
                tmpListSticker.clear();
                listSticker.clear();
                tmpListSticker = listMap.get(KEY_STICKER + 1);
                for (int i = 0; i < tmpListSticker.size(); i++) {
                    listSticker.add(tmpListSticker.get(i));
                }
                break;
            case 2:
                tmpListSticker.clear();
                listSticker.clear();
                tmpListSticker = listMap.get(KEY_STICKER + 2);
                for (int i = 0; i < tmpListSticker.size(); i++) {
                    listSticker.add(tmpListSticker.get(i));
                }
                break;
            case 3:
                tmpListSticker.clear();
                listSticker.clear();
                tmpListSticker = listMap.get(KEY_STICKER + 3);
                for (int i = 0; i < tmpListSticker.size(); i++) {
                    listSticker.add(tmpListSticker.get(i));
                }
                break;
            case 4:
                tmpListSticker.clear();
                listSticker.clear();
                tmpListSticker = listMap.get(KEY_STICKER + 4);
                for (int i = 0; i < tmpListSticker.size(); i++) {
                    listSticker.add(tmpListSticker.get(i));
                }
                break;
            case 5:
                tmpListSticker.clear();
                listSticker.clear();
                tmpListSticker = listMap.get(KEY_STICKER + 5);
                for (int i = 0; i < tmpListSticker.size(); i++) {
                    listSticker.add(tmpListSticker.get(i));
                }
                break;
            case 6:
                tmpListSticker.clear();
                listSticker.clear();
                tmpListSticker = listMap.get(KEY_STICKER + 6);
                for (int i = 0; i < tmpListSticker.size(); i++) {
                    listSticker.add(tmpListSticker.get(i));
                }
                break;
        }

        if (listSticker.size()<=0) {
            return;
        }

        mListStickerDetails = listSticker;
        iconAdapter = new IconStickerAdapter(getActivity(), mListStickerDetails).setOnIconListener(this);
        mRecyclerViewDetails.setAdapter(iconAdapter);
        showDetails(true);
    }

    @Override
    public void onItemIconClickListener(String path) {
        if (listener!=null) {
            listener.onItemStickerIconClicked(path);
        }
    }

    public interface OnStickerIconListener {
        public void onStickerIconFragmentReady();
        public void onItemStickerIconClicked(String path);
    }

    public void showDetails(boolean shown) {
        flag_show_details = shown;
        mRecyclerViewDetails.setVisibility(shown?View.VISIBLE:View.GONE);
        mRecyclerView.setVisibility((!shown)?View.VISIBLE:View.GONE);
    }
}
