package bsoft.healthy.tracker.menstrual.lib_sticker.fragments;


import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import bsoft.healthy.tracker.menstrual.lib_sticker.R;
import bsoft.healthy.tracker.menstrual.lib_sticker.adapters.CustomViewPager;
import bsoft.healthy.tracker.menstrual.lib_sticker.main.StickerContainerView;
import bsoft.healthy.tracker.menstrual.lib_sticker.main.TextStickerView;
import io.karim.MaterialTabs;

/**
 * A simple {@link Fragment} subclass.
 */
public class TextFrameFragment extends BaseFragment {
    private static final int TAB_FONT = 0;
    private static final int TAB_SETTING = 1;
    private int progressPadding;
    private ImageView imgHide;
    private MaterialTabs tabLayout;
    private CustomViewPager viewPager;
    private MyPagerAdapter mAdapter;
    private StickerContainerView mContainerView = null;
    private onBackTextFrameFragmentListener listener;

    public TextFrameFragment setListener(onBackTextFrameFragmentListener listener) {
        this.listener = listener;
        return this;
    }

    private final int[] ICONS = new int[]{
            R.drawable.btn_tab_font
            , R.drawable.btn_tab_setting};

    public static TextFrameFragment newInstance(int progressPadding) {
        TextFrameFragment fragment = new TextFrameFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        fragment.progressPadding = progressPadding;
        return fragment;
    }

    public TextFrameFragment setTextStickerContainerView(StickerContainerView containerView) {
        mContainerView = containerView;
        return this;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_text_frame, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imgHide        = view.findViewById(R.id.imgHideText);
        tabLayout      = view.findViewById(R.id.tabTextCollage);
        viewPager      = view.findViewById(R.id.viewPagerTextCollage);
        mAdapter       = new MyPagerAdapter(getChildFragmentManager());

        viewPager.setAdapter(mAdapter);
        tabLayout.setViewPager(viewPager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.disableScroll(true);

        imgHide.setOnClickListener(view1 -> {
           if(listener!=null){
               listener.onBackTextFrame();
           }
        });
    }
    public void updateProgress(int progressTextPadding) {
        this.progressPadding = progressTextPadding;

        Fragment fragmentSetting = getTab(TAB_SETTING);
        if (fragmentSetting != null && fragmentSetting.isAdded()) {
            TabTextSettingFragment fragment = (TabTextSettingFragment) fragmentSetting;
            fragment.updateProgress(progressTextPadding);
        }

    }

    public Fragment getTab(int position) {
        return getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewPagerTextCollage + ":" + position);
    }
    @Override
    public void backPressed() {

    }

    private class MyPagerAdapter extends FragmentPagerAdapter implements MaterialTabs.CustomTabProvider,
            TabTextFontFragment.OnTextFontListener, TabTextSettingFragment.OnTextSettingListener {

        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            Fragment fragment ;
            switch (position) {
                case TAB_FONT:
                    fragment = new TabTextFontFragment().setListener(this);
                    break;
                case TAB_SETTING:
                    fragment = TabTextSettingFragment.newInstance(progressPadding).setListener(this);
                    break;
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
        public void onFontChanged(Typeface tmpTypeFace) {

            if (mContainerView!=null) {
                TextStickerView curItem = (TextStickerView) mContainerView.getCurItem();
                if (curItem!=null) {
                    curItem.setTextFont(tmpTypeFace);
//                    mContainerView.invalidate();
                }
            }
        }

        @Override
        public void onColorBgTextChanged(int colorCode) {
            if (mContainerView!=null) {
                TextStickerView curItem = (TextStickerView) mContainerView.getCurItem();
                if (curItem!=null) {
                    curItem.setTextColor(colorCode);
                }
            }
        }

        @Override
        public void onPatternBgTextChanged(String patternName) {
            if (mContainerView!=null) {
                TextStickerView curItem = (TextStickerView) mContainerView.getCurItem();
                if (curItem!=null) {
                    curItem.setTextPattern(mContext, patternName);
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
        public View getCustomTabView(ViewGroup parent, int position) {
            View v = View.inflate(parent.getContext(), R.layout.image_tab, null);
            ImageView image = v.findViewById(R.id.image);
            image.setImageResource(ICONS[position]);
            return v;
        }
    }

    public interface onBackTextFrameFragmentListener{
        void onBackTextFrame();
    }




}
