package com.photo.gallery.fragments.options;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.photo.gallery.R;
import com.photo.gallery.databinding.LayoutEditFragmentBinding;

public class EditFragment extends BaseOptFragment implements View.OnClickListener {

    private LayoutEditFragmentBinding binding;
    private onEditListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.layout_edit_fragment,container,false);
        return binding.getRoot();
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        intView();
    }

    public Fragment setListener(onEditListener listener) {
        this.listener = listener;
        return  this;
    }

    private void intView() {
        binding.cropBtn.setOnClickListener(this);
        binding.flip1Btn.setOnClickListener(this);
        binding.flip2Btn.setOnClickListener(this);
        binding.rotateLeft.setOnClickListener(this);
        binding.rotateRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.crop_btn:
                if(listener!=null){
                    listener.onSelectCrop();
                }
                break;
            case R.id.flip1_btn:
                if(listener!=null){
                    listener.onSelectFlip1();
                }
                break;
            case R.id.flip2_btn:
                if(listener!=null){
                    listener.onSelectFlip2();
                }
                break;
            case R.id.rotate_left:
                if(listener!=null){
                    listener.onSelectRotateLeft();
                }
                break;
            case R.id.rotate_right:
                if(listener!=null){
                    listener.onSelectRotateRight();
                }
                break;
        }
    }

    public interface onEditListener{
        void onSelectCrop();
        void onSelectFlip1();
        void onSelectFlip2();
        void onSelectRotateRight();
        void onSelectRotateLeft();
    }
}
