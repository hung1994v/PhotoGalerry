package com.photo.gallery.fragments.options;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.photo.gallery.fragments.BaseFragment;

/**
 * Created by Tung on 3/30/2018.
 */

public class BaseOptFragment extends Fragment {

    private static final String TAG = BaseFragment.class.getSimpleName();
    public static final int FRAGMENT_OPT_MENU = 0;
    public static final int FRAGMENT_OPT_SPLASH = 1;
    public static final int FRAGMENT_OPT_PIP = 2;
    public static final int FRAGMENT_OPT_ICON_STICKER = 3;
    public static final int FRAGMENT_OPT_FILTER = 4;
    public static final int FRAGMENT_OPT_TEXT = 5;
    public static final int FRAGMENT_OPT_EDIT = 6;
    protected FragmentActivity mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (FragmentActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
