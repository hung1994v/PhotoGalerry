package com.photo.splashfunphoto.fragment.menu.text;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.gallery.R;
import com.photo.splashfunphoto.adapter.text.ColorBackgroundAdapter;
import com.photo.splashfunphoto.adapter.text.PatternAdapter;
import com.photo.splashfunphoto.listener.OnMenuBackgroundListener;
import com.photo.splashfunphoto.utils.Statics;


public class TextSettingFragment extends TextBaseFragment implements OnMenuBackgroundListener {

    private RecyclerView recyclerView = null;
    private RecyclerView mRPattern;
    private ColorBackgroundAdapter colorAdapter = null;

    private OnTextSettingListener mOnTextSettingListener;

    public static TextSettingFragment newInstance(OnTextSettingListener listener) {
        TextSettingFragment settingFragment = new TextSettingFragment();
        settingFragment.mOnTextSettingListener = listener;
        return settingFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.text_setting_fragment, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Statics.loadPatterText();
        initSeekBar();
        initRecycleView();
        initRecylePattern();

    }

    private void initSeekBar() {
        mRPattern = (RecyclerView) getView().findViewById(R.id.recycle_view_pattenr);

    }

    private void initRecycleView() {
        int[] colorPick = getActivity().getResources().getIntArray(R.array.color_app_picker);
        colorAdapter = new ColorBackgroundAdapter(getActivity(), colorPick)
                .setOnMenuBackgroundListener(this);
        recyclerView = (RecyclerView) getView().findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(colorAdapter);
    }

    private void initRecylePattern() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRPattern.setLayoutManager(layoutManager);
        PatternAdapter patternAdapter = new PatternAdapter(getActivity(), Statics.mListPattern1, Statics.mListPattern2).setOnMenuBackgroundListener(this);
        mRPattern.setAdapter(patternAdapter);
    }

    @Override
    public void onMenuPatternClickListener(String bgName) {
        if (mOnTextSettingListener != null) {
            mOnTextSettingListener.onPatternClickListener(bgName);
        }
    }

    @Override
    public void onMenuColorBackgroundClickListener(@ColorInt int color) {
        if (mOnTextSettingListener != null) {
            mOnTextSettingListener.onColorClickListener(color);
        }
    }

    public interface OnTextSettingListener {
        void onPatternClickListener(String s);

        public void onColorClickListener(@ColorInt int color);
    }
}
