package com.photo.gallery.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.bsoft.core.BUtils;
import com.photo.gallery.BuildConfig;
import com.photo.gallery.R;
import com.photo.gallery.databinding.FragmentSettingLayoutBinding;
import com.photo.gallery.utils.ConstValue;
import com.photo.gallery.utils.CustomToast;
import com.photo.gallery.utils.SharedPrefUtil;

import static com.photo.gallery.utils.ConstValue.DEFAULT_OPEN;

public class SettingFragment extends BaseFragment implements View.OnClickListener {

    private FragmentSettingLayoutBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_layout, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        intView();
    }

    private void intView() {
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });
        binding.versionNumber.setText(BuildConfig.VERSION_NAME);

        binding.securityBtn.setOnClickListener(this);
        binding.feedbackBtn.setOnClickListener(this);
        binding.btnShare.setOnClickListener(this);
            binding.setDefaultBtn.setVisibility(View.GONE);

//        binding.switchBtn.setChecked(SharedPrefUtil.getInstance().getBoolean(DEFAULT_OPEN,true));
//        SharedPrefUtil.getInstance().putBoolean(DEFAULT_OPEN, binding.switchBtn.isChecked());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.set_default_btn:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    startActivity(new Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS));
                    CustomToast.showContent(mContext,  mContext.getString(R.string.please_set) );
                }

                break;
            case R.id.feedback_btn:
                BUtils.sendFeedback(mContext,mContext.getApplicationInfo().packageName,"bsofttest2020@gmail.com");
                break;
            case R.id.security_btn:
                break;
            case R.id.btn_share:
                BUtils.shareApp(mContext, mContext.getApplicationInfo().packageName);
                break;
        }
    }
}
