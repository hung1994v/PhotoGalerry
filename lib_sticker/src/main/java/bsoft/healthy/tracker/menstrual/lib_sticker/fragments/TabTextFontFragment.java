package bsoft.healthy.tracker.menstrual.lib_sticker.fragments;


import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import bsoft.healthy.tracker.menstrual.lib_sticker.R;
import bsoft.healthy.tracker.menstrual.lib_sticker.models.FontItem;
import bsoft.healthy.tracker.menstrual.lib_sticker.utils.FontUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabTextFontFragment extends BaseFragment {

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
    private static final int NUMBER_CUSTOM_FONTS = NAME_FONTS.length;

    private RecyclerView recyclerView;
    private List<FontItem> listFont = new ArrayList<>();
    private ListFontAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_text_font, container, false);
    }

    @Override
    public void backPressed() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FontItem fontDefault = new FontItem("", "Default");
        listFont.add(fontDefault);
        setCustomFonts();
        setLocalFonts();

        recyclerView = view.findViewById(R.id.rvTextFont);
        adapter = new ListFontAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    private void setCustomFonts() {
        for (int i = 1; i <= NAME_FONTS.length; i++) {
            String fontPath = "font/f" + ((i < 10) ? "0" : "") + (i) + ".ttf";
            String fontPreview = NAME_FONTS[i-1];
            FontItem fontItem = new FontItem(fontPath, fontPreview);
            listFont.add(fontItem);

        }
    }

    private void setLocalFonts() {
        final List<FontUtil.SystemFont> fonts = FontUtil.safelyGetSystemFonts();
        for (int i = 0; i < fonts.size(); i++) {
            if (i == 5 || i == 6) {// font lỗi
                continue;
            }

            String fontPath = fonts.get(i).path;
            String fontPreview = fonts.get(i).name;

            FontItem fontItem = new FontItem(fontPath, fontPreview);
            if (new File(fontPath).exists()) {
                listFont.add(fontItem);
            }
        }
    }

    public class ListFontAdapter extends RecyclerView.Adapter<ListFontAdapter.ViewHolder> {

        ListFontAdapter() {}

        @Override
        public int getItemCount() {
            return listFont.size();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            return new ViewHolder(inflater.inflate(R.layout.list_font_adapter, parent, false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final FontItem item = listFont.get(position);
            holder.textView.setText(item.getFontName());
            Typeface mTypeFace;
            if (position == 0) {
                mTypeFace = Typeface.DEFAULT;
            } else if (position > NUMBER_CUSTOM_FONTS) {
                // system font
                mTypeFace = Typeface.createFromFile(item.getFontPath());
            } else {
                // asset fonts
                mTypeFace = Typeface.createFromAsset(mContext.getAssets(), item.getFontPath());
            }
            if (mTypeFace != null){
                holder.textView.setTypeface(mTypeFace);
            }

        }


        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView textView;
            ViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.tvListFont);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                FontItem FontItem = listFont.get(position);
                if (FontItem == null) return;
                Typeface tmpTypeFace;
                if (position == 0)
                    tmpTypeFace = Typeface.DEFAULT;
                else if (position > NUMBER_CUSTOM_FONTS)
                    tmpTypeFace = Typeface.createFromFile(FontItem.getFontPath());
                else{
                    tmpTypeFace = Typeface.createFromAsset(mContext.getAssets(), FontItem.getFontPath());
                }

                if (listener != null)
                    listener.onFontChanged(tmpTypeFace);
            }
        }

    }
    private OnTextFontListener listener = null;

    public TabTextFontFragment setListener(OnTextFontListener listener) {
        this.listener = listener;
        return this;
    }

    public interface OnTextFontListener{
        void onFontChanged(Typeface tmpTypeFace);
    }
}
