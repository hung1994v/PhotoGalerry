package com.photo.splashfunphoto.fragment.menu.text;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.photo.gallery.R;
import com.photo.splashfunphoto.adapter.text.TextFontAdapter;
import com.photo.splashfunphoto.model.FontModel;
import com.photo.splashfunphoto.utils.FontUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TextFontFragment extends TextBaseFragment {
    private static final int NUMBER_CUSTOM_FONTS = 13;
    private TextFontAdapter.OnTextFontListener mOnTextFontListener;

    private static final String[] NAME_FONTS = new String[]{
            "Bambi",
            "Cafe rojo",
            "Champagne & limousines",
            "Digs my hart",
            "Doris day",
            "Duality",
            "Fairfax station",
            "Honey script",
            "Romance fatal serif",
            "Lobster",
            "Missed your exit",
            "Lauren script",
            "Mom's typewriter"
    };
    private RecyclerView recyclerView = null;
    private TextFontAdapter adapter = null;
    private List<FontModel> fontList;

    public static TextFontFragment newInstance(TextFontAdapter.OnTextFontListener listener) {
        TextFontFragment fontFragment = new TextFontFragment();
        fontFragment.mOnTextFontListener = listener;
        return fontFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.text_font_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fontList = new ArrayList<>();
        setLocalFonts();
        setCustomFonts();
        initRecycleView();
    }


    private void setCustomFonts() {
        for (int i = 1; i <= NUMBER_CUSTOM_FONTS; i++) {
            FontModel fontModel = new FontModel();
            fontModel.fontPath = "text/font/f" + ((i < 10) ? "0" : "") + (i) + ".ttf";
            fontModel.fontPreview = NAME_FONTS[i-1];
            fontList.add(fontModel);
        }
    }

    private void setLocalFonts() {
        final List<FontUtils.SystemFont> fonts = FontUtils.safelyGetSystemFonts();

        FontModel fontDefault = new FontModel();
        fontDefault.fontPath = "";
        fontDefault.fontPreview = "Default";
        fontList.add(fontDefault);

        for (int i = 0; i < fonts.size(); i++) {
            FontModel fontModel = new FontModel();
            fontModel.fontPath = fonts.get(i).path;
            fontModel.fontPreview = fonts.get(i).name;
            if (new File(fontModel.fontPath).exists()) {
                fontList.add(fontModel);
            }
        }
    }


    private void initRecycleView() {

        adapter = new TextFontAdapter(getContext(), fontList, NUMBER_CUSTOM_FONTS).setOnTextFontListener(mOnTextFontListener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView = (RecyclerView) getView().findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }
}
