package bsoft.healthy.tracker.menstrual.lib_sticker.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import bsoft.healthy.tracker.menstrual.lib_sticker.R;
import bsoft.healthy.tracker.menstrual.lib_sticker.adapters.MyRecyclerviewAdapter;

/**
 * Created by Hoavt on 12/12/2017.
 */

public class TextMoreEventFragment extends BaseStickerFragment implements MyRecyclerviewAdapter.OnItemOptionClickListener {

    public final int ROTATE = 1;
    public final int IROTATE = 2;
    public final int UP = 3;
    public final int DOWN = 4;
    public final int LEFT = 5;
    public final int RIGHT = 6;
    public final int ZOON_IN = 7;
    public final int ZOOM_OUT = 8;
    public final int OPACITY = 0;
    private List<Integer> listOpts = new ArrayList<>();
    private int[] MENU_ICON_RES = new int[]{R.drawable.ic_opacity,
            R.drawable.ic_rotate_5,
            R.drawable.ic_mirror_5,
            R.drawable.ic_up,
            R.drawable.ic_down,
            R.drawable.ic_left,
            R.drawable.ic_right,
            R.drawable.ic_zoom_in,
            R.drawable.ic_zoom_out};
    private OnTextEventListener listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {
        RecyclerView rvList = (RecyclerView) view.findViewById(R.id.recyclerview);
        LinearLayoutManager lm = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        rvList.setLayoutManager(lm);

        initList();
        MyRecyclerviewAdapter adapterList = new MyRecyclerviewAdapter(mContext, listOpts).setListener(this);
        rvList.setAdapter(adapterList);
    }

    private void initList() {
        listOpts.clear();

        for (int i = 0; i < MENU_ICON_RES.length; i++) {
            listOpts.add(MENU_ICON_RES[i]);
        }
    }

    @Override
    public void onItemOptionClicked(int position) {
        if (listener == null)
            return;

        switch (position) {
            case OPACITY:
                listener.onTextOpacityChanged(position);
                break;
            case ROTATE:
                listener.onTextRotateChanged(position);
                break;
            case IROTATE:
                listener.onTextMirrorChanged(position);
                break;
            case UP:
                listener.onTextMoveUpChanged(position);
                break;
            case DOWN:
                listener.onTextMoveDownChanged(position);
                break;
            case LEFT:
                listener.onTextMoveLeftChanged(position);
                break;
            case RIGHT:
                listener.onTextMoveRightChanged(position);
                break;
            case ZOON_IN:
                listener.onTextZoomInChanged(position);
                break;
            case ZOOM_OUT:
                listener.onTextZoomOutChanged(position);
                break;
        }
    }

    public TextMoreEventFragment setListener(OnTextEventListener listener) {
        this.listener = listener;
        return this;
    }

    public interface OnTextEventListener {

        void onTextOpacityChanged(int position);

        void onTextRotateChanged(int position);

        void onTextMirrorChanged(int position);

        void onTextMoveUpChanged(int position);

        void onTextMoveDownChanged(int position);

        void onTextMoveLeftChanged(int position);

        void onTextMoveRightChanged(int position);

        void onTextZoomInChanged(int position);

        void onTextZoomOutChanged(int position);
    }
}
