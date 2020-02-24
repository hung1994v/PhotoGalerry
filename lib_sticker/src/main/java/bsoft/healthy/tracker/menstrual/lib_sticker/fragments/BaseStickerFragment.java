package bsoft.healthy.tracker.menstrual.lib_sticker.fragments;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

/**
 * Created by Tung on 4/4/2018.
 */

public class BaseStickerFragment extends Fragment {

    private static final String TAG = BaseStickerFragment.class.getSimpleName();

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
