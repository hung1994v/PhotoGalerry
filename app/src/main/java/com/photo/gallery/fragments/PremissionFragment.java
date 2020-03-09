package com.photo.gallery.fragments;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.photo.gallery.R;
import com.photo.gallery.activities.MainActivity;
import com.photo.gallery.activities.SplashActivity;
import com.photo.gallery.databinding.FragmentPremissionLayoutBinding;
import com.photo.gallery.models.FileItem;
import com.photo.gallery.utils.ConstValue;
import com.photo.gallery.utils.Flog;
import com.photo.gallery.utils.GalleryUtil;
import com.photo.gallery.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static com.photo.gallery.utils.ConstValue.SPLASH_INTENT;

public class PremissionFragment extends BaseFragment implements EasyPermissions.PermissionCallbacks{
    private FragmentPremissionLayoutBinding binding;
    private String[] perm ={Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int GRANT_BTN = 0x124;
    private onPermissionListener listener;

    public static PremissionFragment newInstance(onPermissionListener listener) {
        PremissionFragment fragment = new PremissionFragment();
        fragment.listener = listener;
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_premission_layout,container,false);
       return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        intnit();
    }

    private void intnit() {
        binding.allowPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameTask();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        Flog.d("onPermissionsGranted","PermissionsResult");
    }



    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if(listener!=null){
            listener.onPermissionGranted();
        }

        Flog.d("onPermissionsGranted","granted");
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
//        new AppSettingsDialog.Builder(this).build().show();
    }

    @AfterPermissionGranted(GRANT_BTN)
    private void frameTask() {
        if (!EasyPermissions.hasPermissions(mContext, perm)) {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_read),
                    GRANT_BTN, perm);
        }else {
            if(listener!=null){
                listener.onPermissionGranted();
            }

        }
    }

    public interface onPermissionListener{
        void onPermissionGranted();
    }
}
