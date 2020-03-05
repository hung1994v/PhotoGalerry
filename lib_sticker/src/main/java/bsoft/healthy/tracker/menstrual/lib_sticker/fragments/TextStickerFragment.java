package bsoft.healthy.tracker.menstrual.lib_sticker.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import bsoft.healthy.tracker.menstrual.lib_sticker.R;
import bsoft.healthy.tracker.menstrual.lib_sticker.adapters.CustomViewPager;
import bsoft.healthy.tracker.menstrual.lib_sticker.interfaces.OnTextListener;
import bsoft.healthy.tracker.menstrual.lib_sticker.main.StickerContainerView;
import bsoft.healthy.tracker.menstrual.lib_sticker.main.TextStickerView;
import io.karim.MaterialTabs;

/**
 * Created by Hoavt on 12/12/2017.
 */

public class TextStickerFragment extends BaseStickerFragment implements View.OnClickListener {

    public static final int TAB_FONT_TEXT_EDITOR = 0;
    public static final int TAB_BACKGROUND_TEXT_EDITOR = 1;
    public static final int TAB_ADJUST_TEXT_EDITOR = 2;
    public static final int TAB_EVENT_TEXT_EDITOR = 3;
    public static final int TAB_OPACITY_TEXT_EDITOR = 4;
    private static final String TAG = TextStickerFragment.class.getSimpleName();
    public static boolean flag_back_opacity = false;
    private ImageView btnExpand;
    private ImageView imgHide,imgkeyboard;
    private MaterialTabs tabLayout;
    private CustomViewPager viewPager;
    private MyPagerAdapter mAdapter;
    private int[] ICONS = new int[]{R.drawable.ic_font_text,
            R.drawable.background,
            R.drawable.ic_adjust,
    };
    private int curFragID = -1;
    private boolean shown = true;
//    private View fontContainer, bgContainer, adjustContainer, eventContainer, opacityContainer;
    private StickerContainerView mContainerView = null;
    private int colorApp = -1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_text_frame, container, false);

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        intView(view);

    }

    private void intView(View view) {
        imgkeyboard = view.findViewById(R.id.keyboard);
        imgHide        = view.findViewById(R.id.imgHideText);
        tabLayout      = view.findViewById(R.id.tabTextCollage);
        viewPager      = view.findViewById(R.id.viewPagerTextCollage);
        mAdapter       = new MyPagerAdapter(getChildFragmentManager());

        viewPager.setAdapter(mAdapter);
        tabLayout.setViewPager(viewPager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.disableScroll(true);
        imgkeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onKeyboardMenu();
            }
        });
        imgHide.setOnClickListener(view1 -> {
            listener.onHideMenu();
        });
    }

    private class MyPagerAdapter extends FragmentPagerAdapter implements MaterialTabs.CustomTabProvider, TextMoreFontFragment.OnTextMoreFontListener, TextMoreBackgroundFragment.OnTextMoreBackgroundListener, TextMoreAdjustFragment.OnTabAlignTextListener, TextMoreEventFragment.OnTextEventListener, OpacityFragment.OnOpacityListener, TabTextFontFragment.OnTextFontListener {

        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment ;
            switch (position) {
                case TAB_FONT_TEXT_EDITOR:
                    fragment = new TabTextFontFragment().setListener(this);
                    break;
                case TAB_BACKGROUND_TEXT_EDITOR:
                    fragment = new TextMoreBackgroundFragment().setListener(this);
                    break;
                case TAB_ADJUST_TEXT_EDITOR:
                    fragment = new TextMoreAdjustFragment().setListener(this);
                    break;
                case TAB_EVENT_TEXT_EDITOR:
                    fragment = new TextMoreEventFragment().setListener(this);

                default:
                    fragment = null;
                    break;
            }
            return fragment;

        }


        @Override
        public int getCount() {
            return ICONS.length;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }

        @Override
        public View getCustomTabView(ViewGroup parent, int position) {
            View v = View.inflate(parent.getContext(), R.layout.image_tab, null);
            ImageView image = v.findViewById(R.id.image);
            image.setImageResource(ICONS[position]);
            return v;
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
        public void onOpacityChanged(int progress) {
            Log.d(TAG, "progress33=" + progress);

            if (mContainerView!=null) {
                TextStickerView curItem = (TextStickerView) mContainerView.getCurItem();
                if (curItem!=null) {
                    curItem.setOpacity(progress);
                }
            }
        }

    }



    @Override
    public void onClick(View view) {
    }




    public TextStickerFragment setListener(onHideMenuListener listener) {
        this.listener = listener;
        return this;
    }

    private onHideMenuListener listener;

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

    public interface onHideMenuListener{
        void onHideMenu();
        void onKeyboardMenu();
    }





    public TextStickerFragment setTextStickerContainerView(StickerContainerView containerView) {
        mContainerView = containerView;
        return this;
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
//        showOptFrag(TAB_EVENT_TEXT_EDITOR);
    }
}
