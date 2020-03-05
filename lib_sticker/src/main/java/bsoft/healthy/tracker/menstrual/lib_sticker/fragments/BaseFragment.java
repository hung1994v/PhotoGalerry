package bsoft.healthy.tracker.menstrual.lib_sticker.fragments;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import bsoft.healthy.tracker.menstrual.lib_sticker.R;

public abstract class BaseFragment extends Fragment {
    protected FragmentActivity mContext = null;

    @Override
    public Context getContext() {
        if (mContext != null) return mContext;
        return super.getContext();
    }

    public abstract void backPressed();

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

