package com.photo.splashfunphoto.adapter.text;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.photo.gallery.R;
import com.photo.splashfunphoto.fragment.menu.text.TextBaseFragment;
import com.photo.splashfunphoto.fragment.menu.text.TextEditFragment;
import com.photo.splashfunphoto.fragment.menu.text.TextFontFragment;
import com.photo.splashfunphoto.fragment.menu.text.TextSettingFragment;

import io.karim.MaterialTabs;


public class TextAdapter extends FragmentPagerAdapter implements MaterialTabs.CustomTabProvider {

    private final int[] ICONS = new int[]{R.drawable.ic_keyboard_txt,
            R.drawable.ic_txt_collage,
            R.drawable.ic_text_settings};
    private TextBaseFragment tab = null;
    private TextBaseFragment.OnTextOptionCallback listener = null;
    private Bundle mData;
    private TextFontAdapter.OnTextFontListener mOnTextFontListener;
    private TextSettingFragment.OnTextSettingListener mOnTextSettingListener;
    private TextEditFragment.OnTextEditListener mOnTextEditListener;

    public TextAdapter(FragmentManager fm, Bundle data, TextFontAdapter.OnTextFontListener listener, TextSettingFragment.OnTextSettingListener listener1, TextEditFragment.OnTextEditListener listener2) {
        super(fm);
        this.mData = data;
        mOnTextFontListener = listener;
        mOnTextSettingListener = listener1;
        mOnTextEditListener = listener2;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                // fonts
                tab = TextFontFragment.newInstance(mOnTextFontListener);
                break;
            case 1:
                // color,SIZE,...
                tab = TextSettingFragment.newInstance(mOnTextSettingListener);
//                tab.setArguments(mData);
                break;
            case 2:
                tab = TextEditFragment.newInstance(mOnTextEditListener);
                tab.setArguments(mData);
                break;
        }
        return tab;
    }

    @Override
    public int getCount() {
        return ICONS.length;
    }

    @Override
    public View getCustomTabView(ViewGroup parent, int position) {
        View v = View.inflate(parent.getContext(), R.layout.image_tab, null);
        ImageView image = (ImageView) v.findViewById(R.id.image);
        image.setImageResource(ICONS[position]);
        return v;
    }

    @Override
    public void onCustomTabViewSelected(View view, int position, boolean alreadySelected) {

    }

    @Override
    public void onCustomTabViewUnselected(View view, int position, boolean alreadyUnselected) {

    }


}
