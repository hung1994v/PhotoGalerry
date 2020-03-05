package com.photo.splashfunphoto.adapter.text;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.photo.gallery.R;
import com.photo.splashfunphoto.model.FontModel;

import java.util.List;

public class TextFontAdapter extends RecyclerView.Adapter<TextFontAdapter.ViewHolder> {

    private static final String TAG = TextFontAdapter.class.getSimpleName();
    private int numOfCustomFonts = 0;
    private Context context = null;
    private List<FontModel> mList;
    private Typeface mTypeFace;
    private OnTextFontListener listener = null;

    public TextFontAdapter setOnTextFontListener(OnTextFontListener listener) {
        this.listener = listener;
        return this;

    }

    public TextFontAdapter(Context context, List<FontModel> list, int numOfCustomFonts) {
        this.context = context;
        this.mList = list;
        this.numOfCustomFonts = numOfCustomFonts;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.text_font_item, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final FontModel fontModel = mList.get(position);
        holder.fontPreview.setText(fontModel.fontPreview);

        if (position == 0) {
            mTypeFace = Typeface.DEFAULT;
        } else if (position < (mList.size() - numOfCustomFonts)) {
            // system font
            mTypeFace = Typeface.createFromFile(fontModel.fontPath);
        } else {

            mTypeFace = Typeface.createFromAsset(context.getAssets(), fontModel.fontPath);
        }

        holder.fontPreview.setTypeface(mTypeFace);


        holder.btnFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    Typeface tmpTypeFace;
                    if (position == 0) {
                        tmpTypeFace = Typeface.DEFAULT;
                    } else if (position < (mList.size() - numOfCustomFonts)) {
                        // system font
                        tmpTypeFace = Typeface.createFromFile(fontModel.fontPath);
                    } else {
                        // asset fonts
                        tmpTypeFace = Typeface.createFromAsset(context.getAssets(), fontModel.fontPath);
                    }
                    listener.onFontChanged(tmpTypeFace);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public interface OnTextFontListener {
        public void onFontChanged(Typeface typeface);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView fontPreview;
        LinearLayoutCompat btnFont;

        public ViewHolder(View itemView) {
            super(itemView);
            btnFont = (LinearLayoutCompat) itemView.findViewById(R.id.btn_font);
            fontPreview = (TextView) itemView.findViewById(R.id.preview_font);
        }
    }

}