package bsoft.healthy.tracker.menstrual.lib_sticker.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import bsoft.healthy.tracker.menstrual.lib_sticker.R;
import bsoft.healthy.tracker.menstrual.lib_sticker.interfaces.OnTextListener;
import bsoft.healthy.tracker.menstrual.lib_sticker.main.StickerContainerView;
import bsoft.healthy.tracker.menstrual.lib_sticker.main.TextStickerView;

/**
 * Created by Hoavt on 12/12/2017.
 */

public class TextStickerFragment extends BaseStickerFragment implements View.OnClickListener, TabLayout.OnTabSelectedListener, TextMoreFontFragment.OnTextMoreFontListener, TextMoreAdjustFragment.OnTabAlignTextListener, TextMoreEventFragment.OnTextEventListener, OpacityFragment.OnOpacityListener, TextMoreBackgroundFragment.OnTextMoreBackgroundListener {

    public static final int TAB_FONT_TEXT_EDITOR = 0;
    public static final int TAB_BACKGROUND_TEXT_EDITOR = 1;
    public static final int TAB_ADJUST_TEXT_EDITOR = 2;
    public static final int TAB_EVENT_TEXT_EDITOR = 3;
    public static final int TAB_OPACITY_TEXT_EDITOR = 4;
    private static final String TAG = TextStickerFragment.class.getSimpleName();
    public static boolean flag_back_opacity = false;
    private ImageView btnExpand;
    private TabLayout tabLayout;
    private int[] ICONS = new int[]{R.drawable.ic_font_text,
            R.drawable.ic_background,
            R.drawable.ic_adjust,
            R.drawable.ic_text_settings,
    };
    private int curFragID = -1;
    private boolean shown = true;
    private View fontContainer, bgContainer, adjustContainer, eventContainer, opacityContainer;
    private StickerContainerView mContainerView = null;
    private int colorApp = -1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_text_editor, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        applyColor(view);
        initViews(view);
        setValues();

        try {

            addFrags();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void setColorApp(int color ) {
        colorApp = color;
    }

    public void applyColor(View view) {
        int defaultPrimary = ContextCompat.getColor(mContext, R.color.colorPrimary);
        int colorPrimary = colorApp==-1?defaultPrimary:colorApp;

        ViewGroup viewGroup = view.findViewById(R.id.layout_text_editor_parent);
        viewGroup.setBackgroundColor(colorPrimary);
    }

    private void setValues() {
        btnExpand.setOnClickListener(this);

        for (int i = 0; i < ICONS.length; i++) {
            View iconView = mContext.getLayoutInflater().inflate(R.layout.custom_tab_text, null).findViewById(R.id.icon_tab);
            iconView.setBackgroundResource(ICONS[i]);
            tabLayout.addTab(tabLayout.newTab().setCustomView(iconView));
        }
        tabLayout.addOnTabSelectedListener(this);
    }

    private void initViews(View view) {
        btnExpand = (ImageView) view.findViewById(R.id.btn_expand_text_editor);
        tabLayout = (TabLayout) view.findViewById(R.id.tablayout_text_editor);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_expand_text_editor) {
            shown = false;
            showFragAt(curFragID, false);

        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int index = tab.getPosition();
        boolean selected = false;
        switch (index) {
            case TAB_FONT_TEXT_EDITOR:
                selected = true;
                break;
            case TAB_BACKGROUND_TEXT_EDITOR:
                selected = true;
                break;
            case TAB_ADJUST_TEXT_EDITOR:
                selected = true;
                break;
            case TAB_EVENT_TEXT_EDITOR:
                selected = true;
                break;
            default:
                selected = false;
                break;
        }
        if (selected) {
            Log.d(TAG, "MORE index=" + index);
            shown = true;
            showOptFrag(index);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        if (shown) {
            return;
        }

        int index = tab.getPosition();
        boolean selected = false;
        switch (index) {
            case TAB_FONT_TEXT_EDITOR:
                selected = true;
                break;
            case TAB_BACKGROUND_TEXT_EDITOR:
                selected = true;
                break;
            case TAB_ADJUST_TEXT_EDITOR:
                selected = true;
                break;
            case TAB_EVENT_TEXT_EDITOR:
                selected = true;
                break;
            default:
                selected = false;
                break;
        }
        if (selected) {
            Log.d(TAG, "MORE index=" + index);
            showOptFrag(index);
        }
    }

    public TextStickerFragment setListener(OnTextListener listener) {
//        this.listener = listener;
        return this;
    }

    public void addFrags() {

        View view = getView();
        if (view == null) {
            return;
        }

        FragmentManager fm = getChildFragmentManager();

        fontContainer = view.findViewById(R.id.font_container);
        ((TextMoreFontFragment)fm.findFragmentById(R.id.font_container)).setListener(this);

        bgContainer = view.findViewById(R.id.bg_container);
        ((TextMoreBackgroundFragment)fm.findFragmentById(R.id.bg_container)).setListener(this);

        adjustContainer = view.findViewById(R.id.adjust_container);
        ((TextMoreAdjustFragment)fm.findFragmentById(R.id.adjust_container)).setListener(this);

        eventContainer = view.findViewById(R.id.event_container);
        ((TextMoreEventFragment)fm.findFragmentById(R.id.event_container)).setListener(this);

        opacityContainer = view.findViewById(R.id.opacity_container);
        ((OpacityFragment)fm.findFragmentById(R.id.opacity_container)).setListener(this);



        setDoNothingClicked(fontContainer, bgContainer, adjustContainer, eventContainer, opacityContainer);

        showOptFrag(TAB_FONT_TEXT_EDITOR);
    }

    private void setDoNothingClicked(View... views) {
        for (View view : views) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // do nothing
                }
            });
        }
    }

    private void showOptFrag(int fragID) {

        if (curFragID == -1) {
            hideAllFrags();
        } else {
            showFragAt(curFragID, false);
        }

        showFragAt(fragID, true);
        curFragID = fragID;
    }

    private void showFragAt(int fragID, boolean visibility) {
        switch (fragID) {
            case TAB_FONT_TEXT_EDITOR:
                fontContainer.setVisibility(visibility ? View.VISIBLE : View.GONE);
                break;
            case TAB_BACKGROUND_TEXT_EDITOR:
                bgContainer.setVisibility(visibility ? View.VISIBLE : View.GONE);
                break;
            case TAB_ADJUST_TEXT_EDITOR:
                adjustContainer.setVisibility(visibility ? View.VISIBLE : View.GONE);
                break;
            case TAB_EVENT_TEXT_EDITOR:
                eventContainer.setVisibility(visibility ? View.VISIBLE : View.GONE);
                break;
            case TAB_OPACITY_TEXT_EDITOR:
                opacityContainer.setVisibility(visibility ? View.VISIBLE : View.GONE);
                break;
        }
    }

    private void hideAllFrags() {
        fontContainer.setVisibility(View.GONE);
        bgContainer.setVisibility(View.GONE);
        adjustContainer.setVisibility(View.GONE);
        eventContainer.setVisibility(View.GONE);
        opacityContainer.setVisibility(View.GONE);
    }

    public TextStickerFragment setTextStickerContainerView(StickerContainerView containerView) {
        mContainerView = containerView;
        return this;
    }

    @Override
    public void onFontChanged(Typeface typeface) {
        Log.d(TAG, "onFontChanged="+typeface+"_mContainerView="+mContainerView);

        if (mContainerView!=null) {
            TextStickerView curItem = (TextStickerView) mContainerView.getCurItem();
            if (curItem!=null) {
                curItem.setTextFont(typeface);
                mContainerView.invalidate();
            }
        }
    }

    @Override
    public void onAlignTextLeftChanged() {

        if (mContainerView!=null) {
            TextStickerView curItem = (TextStickerView) mContainerView.getCurItem();
            if (curItem!=null) {
                curItem.setTextAlign(TextStickerView.TEXT_ALIGN_LEFT);
                mContainerView.invalidate();
            }
        }
    }

    @Override
    public void onAlignTextCenterChanged() {
        if (mContainerView!=null) {
            TextStickerView curItem = (TextStickerView) mContainerView.getCurItem();
            if (curItem!=null) {
                curItem.setTextAlign(TextStickerView.TEXT_ALIGN_CENTER);
                mContainerView.invalidate();
            }
        }
    }

    @Override
    public void onAlignTextRightChanged() {
        if (mContainerView!=null) {
            TextStickerView curItem = (TextStickerView) mContainerView.getCurItem();
            if (curItem!=null) {
                curItem.setTextAlign(TextStickerView.TEXT_ALIGN_RIGHT);
                mContainerView.invalidate();
            }
        }
    }

    @Override
    public void onPaddingTextChanged(int value) {
        if (mContainerView!=null) {
            TextStickerView curItem = (TextStickerView) mContainerView.getCurItem();
            if (curItem!=null) {
                curItem.setTextPadding(value);
                mContainerView.invalidate();
            }
        }
    }

    @Override
    public void onSizeTextChanged(int value) {
        if (mContainerView!=null) {
            TextStickerView curItem = (TextStickerView) mContainerView.getCurItem();
            if (curItem!=null) {
                curItem.setTextSize(value);
                mContainerView.invalidate();
            }
        }
    }

    @Override
    public void onTextOpacityChanged(int position) {
        flag_back_opacity = true;
        showOptFrag(TAB_OPACITY_TEXT_EDITOR);
    }

    @Override
    public void onTextRotateChanged(int position) {

        if (mContainerView!=null) {
            TextStickerView curItem = (TextStickerView) mContainerView.getCurItem();
            if (curItem!=null) {
                curItem.rotate();
            }
        }
    }

    @Override
    public void onTextMirrorChanged(int position) {

        if (mContainerView!=null) {
            TextStickerView curItem = (TextStickerView) mContainerView.getCurItem();
            if (curItem!=null) {
                curItem.mirror();
            }
        }
    }

    @Override
    public void onTextMoveUpChanged(int position) {

        if (mContainerView!=null) {
            TextStickerView curItem = (TextStickerView) mContainerView.getCurItem();
            if (curItem!=null) {
                curItem.moveUp();
            }
        }
    }

    @Override
    public void onTextMoveDownChanged(int position) {

        if (mContainerView!=null) {
            TextStickerView curItem = (TextStickerView) mContainerView.getCurItem();
            if (curItem!=null) {
                curItem.moveDown();
            }
        }
    }

    @Override
    public void onTextMoveLeftChanged(int position) {

        if (mContainerView!=null) {
            TextStickerView curItem = (TextStickerView) mContainerView.getCurItem();
            if (curItem!=null) {
                curItem.moveLeft();
            }
        }
    }

    @Override
    public void onTextMoveRightChanged(int position) {

        if (mContainerView!=null) {
            TextStickerView curItem = (TextStickerView) mContainerView.getCurItem();
            if (curItem!=null) {
                curItem.moveRight();
            }
        }
    }

    @Override
    public void onTextZoomInChanged(int position) {

        if (mContainerView!=null) {
            TextStickerView curItem = (TextStickerView) mContainerView.getCurItem();
            if (curItem!=null) {
                curItem.zoomIn();
            }
        }
    }

    @Override
    public void onTextZoomOutChanged(int position) {

        if (mContainerView!=null) {
            TextStickerView curItem = (TextStickerView) mContainerView.getCurItem();
            if (curItem!=null) {
                curItem.zoomOut();
            }
        }
    }

    @Override
    public void onTextBackgroundSelected(String nameBackground) {

        if (mContainerView!=null) {
            TextStickerView curItem = (TextStickerView) mContainerView.getCurItem();
            if (curItem!=null) {
                curItem.setTextPattern(mContext, nameBackground);
            }
        }
    }

    @Override
    public void onTextColorBackgroundSelected(int color) {

        if (mContainerView!=null) {
            TextStickerView curItem = (TextStickerView) mContainerView.getCurItem();
            if (curItem!=null) {
                curItem.setTextColor(color);
            }
        }
    }

    @Override
    public void onOpacityChanged(int progress) {
        Log.d(TAG, "progress33=" + progress);

        if (mContainerView!=null) {
            TextStickerView curItem = (TextStickerView) mContainerView.getCurItem();
            if (curItem!=null) {
                curItem.setOpacity(progress);
            }
        }
    }

    public void resetSeekbar() {
        try {
            FragmentManager fm = getChildFragmentManager();
            ((TextMoreAdjustFragment)fm.findFragmentById(R.id.adjust_container)).resetSeekbar();
            ((OpacityFragment)fm.findFragmentById(R.id.opacity_container)).resetSeekbar();
        }catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }

    public void showEventTextFragment() {
        flag_back_opacity = false;
        showOptFrag(TAB_EVENT_TEXT_EDITOR);
    }
}
