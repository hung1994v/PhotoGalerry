package com.photo.gallery.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.photo.gallery.R;

/**
 * Created by Adm on 3/30/2017.
 */
public abstract class BaseFragment extends Fragment {

    private static final String TAG = BaseFragment.class.getSimpleName();
    public static final int FRAGMENT_SEARCH = 0;
    public static final int FRAGMENT_FILES_ALBUM = 1;
    public static final int FRAGMENT_PHOTO_VIEWER = 2;
    public static final int FRAGMENT_VIDEO_VIEWER = 3;
    public static final int FRAGMENT_SELECT_ALBUM = 4;
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

    public void addMainFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = null;
        if (getFragmentManager() != null) {
            transaction = getFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter,R.anim.exit,R.anim.pop_enter, R.anim.pop_exit);
            transaction.add(R.id.root_view, fragment);
            transaction.addToBackStack(fragment.getClass().getSimpleName());
            transaction.commitAllowingStateLoss();
        }

    }
}
