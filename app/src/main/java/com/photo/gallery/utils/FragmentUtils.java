package com.photo.gallery.utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.photo.gallery.R;

/**
 * Created by Hoavt on 11/29/2017.
 */

public class FragmentUtils {

    private static final String TAG = FragmentUtils.class.getSimpleName();

    public static void addFragment(FragmentManager fm, Fragment fragment) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.container_view, fragment);
        ft.commit();
    }

    public static void showFragment(FragmentManager fm, Fragment fragment) {
        fm.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .show(fragment)
                .commit();
    }

    public static void hideFragment(FragmentManager fm, Fragment fragment) {
        fm.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .hide(fragment)
                .commit();
    }



    public static int size(FragmentManager fm) {
        return fm.getBackStackEntryCount();
    }

    public static void clearBackstack(FragmentManager fm) {
        fm.popBackStack(R.id.container_view, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public Fragment getCurrentFragment(FragmentManager fm) {
        return fm.findFragmentById(R.id.container_view);
    }

    public static void listFragments(FragmentManager fm) {
        Flog.d(TAG, "list Fragments size="+fm.getBackStackEntryCount());
        for(int entry = 0; entry < fm.getBackStackEntryCount(); entry++){
            Flog.d(TAG, "Found fragment: " + fm.getBackStackEntryAt(entry).getId());
        }
    }
}
