package bsoft.healthy.tracker.menstrual.lib_sticker.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import bsoft.healthy.tracker.menstrual.lib_sticker.R;
import bsoft.healthy.tracker.menstrual.lib_sticker.utils.FontUtil;

/**
 * Created by nam on 4/5/2017.
 */

public class TextMoreFontFragment extends BaseStickerFragment {
    private static final String TAG = TextMoreFontFragment.class.getSimpleName();
    private static final String[] NAME_CUSTOM_FONTS = new String[]{
            "Coiny-regular",
            "Champagne & limousines",
            "Digs my hart"
    };
    private RecyclerView recyclerView = null;
    private TextFontAdapter adapter = null;
    private List<FontModel> fontList;
    private OnTextMoreFontListener listener = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycerview, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fontList = new ArrayList<>();
        setLocalFonts();
        setCustomFonts();
        initRecycleView();
        Log.d(TAG, "sizeall="+fontList.size());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // DO NOTHING
            }
        });
    }

    private void setCustomFonts() {
        for (int i = 1; i <= NAME_CUSTOM_FONTS.length; i++) {
            String fontPath = "font/f" + ((i < 10) ? "0" : "") + (i) + ".ttf";
            String fontPreview = NAME_CUSTOM_FONTS[i - 1];
            FontModel fontModel = new FontModel(fontPreview, fontPath);
            fontList.add(fontModel);
        }
    }

    private void setLocalFonts() {
        final List<FontUtil.SystemFont> fonts = FontUtil.safelyGetSystemFonts();

        String fontPath = "";
        String fontPreview = "Default";
        FontModel fontDefault = new FontModel(fontPreview, fontPath);

        fontList.add(fontDefault);

        for (int i = 0; i < fonts.size(); i++) {
            fontPath = fonts.get(i).path;
            fontPreview = fonts.get(i).name;

            FontModel fontModel = new FontModel(fontPreview, fontPath);

            if (new File(fontModel.getFontPath()).exists()) {
                fontList.add(fontModel);
            }
        }
    }

    private void initRecycleView() {

        adapter = new TextFontAdapter(mContext, fontList, NAME_CUSTOM_FONTS.length);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerview);
//        recyclerView.getLayoutParams().height = (int) getResources().getDimension(R.dimen.height_content_tablayout);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    public TextMoreFontFragment setListener(OnTextMoreFontListener listener) {
        this.listener = listener;
        return this;
    }

    public interface OnTextMoreFontListener {
        void onFontChanged(Typeface typeface);
    }

    public class TextFontAdapter extends RecyclerView.Adapter<TextFontAdapter.ViewHolder> {
        private int numOfCustomFonts = 0;
        private Context mContext = null;
        private List<FontModel> mList;
        private Typeface mTypeFace;

        public TextFontAdapter(Context context, List<FontModel> list, int numOfCustomFonts) {
            mContext = context;
            mList = list;
            this.numOfCustomFonts = numOfCustomFonts;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_font, null, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            if (position < 0 || position >= mList.size() || numOfCustomFonts > mList.size())
                return;
            final FontModel fontModel = mList.get(position);
            holder.fontPreview.setText(fontModel.getFontPreview());
            if (position == 0) {
                mTypeFace = Typeface.DEFAULT;
            } else if (position < (mList.size() - numOfCustomFonts)) {
                // system font
                Log.i("textfontadapter", "onBindViewHolder: " + fontModel.getFontPath());
                mTypeFace = Typeface.createFromFile(fontModel.getFontPath());
            } else {
                // asset fonts
                Log.i("TypeFace", "onBindViewHolder: " + " " + fontModel.getFontPath() + " ");

                mTypeFace = Typeface.createFromAsset(mContext.getAssets(), fontModel.getFontPath());
            }
            holder.fontPreview.setTypeface(mTypeFace);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Typeface tmpTypeFace;
                    if (position == 0)
                        tmpTypeFace = Typeface.DEFAULT;
                    else if (position < (mList.size() - numOfCustomFonts))
                        tmpTypeFace = Typeface.createFromFile(fontModel.getFontPath());
                    else
                        tmpTypeFace = Typeface.createFromAsset(mContext.getAssets(), fontModel.getFontPath());
                    if (listener != null)
                        listener.onFontChanged(tmpTypeFace);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView fontPreview;
            View viewParent;

            public ViewHolder(View itemView) {
                super(itemView);
                viewParent = itemView.findViewById(R.id.btn_font);
                fontPreview = (TextView) itemView.findViewById(R.id.preview_font);
            }
        }

    }

    private class FontModel {
        private String mFontPreview;
        private String mFontPath;

        public FontModel(String fontPreview, String fontPath) {
            this.mFontPreview = fontPreview;
            this.mFontPath = fontPath;
        }

        public String getFontPreview() {
            return mFontPreview;
        }

        public void setFontPreview(String mFontPreview) {
            this.mFontPreview = mFontPreview;
        }

        public String getFontPath() {
            return mFontPath;
        }

        public void setFontPath(String mFontPath) {
            this.mFontPath = mFontPath;
        }
    }
}

