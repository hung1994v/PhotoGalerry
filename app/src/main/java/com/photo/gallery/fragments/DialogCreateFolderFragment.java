package com.photo.gallery.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.photo.gallery.R;
import com.photo.gallery.databinding.FragmentDialogDetailsBinding;
import com.photo.gallery.models.AlbumItem;
import com.photo.gallery.models.FileItem;
import com.photo.gallery.utils.CustomToast;
import com.photo.gallery.utils.FileUtil;

public class DialogCreateFolderFragment extends DialogFragment implements View.OnClickListener {
    private FragmentDialogDetailsBinding binding;
    private Context context;
    private HashMap<String, ArrayList<FileItem>> mapAllFolders;
    private ArrayList<AlbumItem> albumItems = new ArrayList<>();
    private onCreateNewFolder listener;

    public static DialogCreateFolderFragment newInstance(Context c, HashMap<String, ArrayList<FileItem>> mapAllFolders, onCreateNewFolder listener) {

        DialogCreateFolderFragment fragment = new DialogCreateFolderFragment();
        fragment.context = c;
        fragment.mapAllFolders = mapAllFolders;
        fragment.listener = listener;
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (null != getDialog() && null != getDialog().getWindow()) {
            getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        intData();

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dialog_details, container, false);
        return binding.getRoot();

    }

    private void intData() {
        for (Map.Entry<String, ArrayList<FileItem>> entry : mapAllFolders.entrySet()) {
            String key = entry.getKey();
            ArrayList<FileItem> items = mapAllFolders.get(key);

            FileItem firstItem = items.get(0);

            AlbumItem item = new AlbumItem();
            item.name = firstItem.folder;
            item.size = String.valueOf(items.size());
            item.pathFirstImg = firstItem.path;
            albumItems.add(item);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (null != getDialog() && null != getDialog().getWindow()) {

            WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            getDialog().getWindow().setAttributes(params);
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        (getDialog()).requestWindowFeature(Window.FEATURE_NO_TITLE);
        init();
    }

    private void init() {

        binding.btnCancel.setOnClickListener(this);
        binding.btnOk.setOnClickListener(this);
        binding.textInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.textInput.getText().length() >= 30)
                    CustomToast.showContent(context, getString(R.string.name_is_too_long));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private boolean checkNameFolderValid(String name) {
        for (AlbumItem albumItem : albumItems) {
            String hanleSpace = albumItem.name.trim();
            if (hanleSpace.equals(name))
                return false;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                getDialog().dismiss();
                break;
            case R.id.btn_ok:
                String name = binding.textInput.getText().toString().toLowerCase();
                String deleSpaceWhite = name.trim();
                if (deleSpaceWhite.equals("") || name.length() < 1)
                    CustomToast.showContent(context, getString(R.string.file_name_empty));
                else if (checkNameFolderValid(deleSpaceWhite)) {
                    if (!FileUtil.isStringHasCharacterSpecial(name)) {
                        if(listener!=null){
                            listener.onCreateNewFolderListener(name);
                        }
                    } else {
                        CustomToast.showContent(context, getString(R.string.file_name_error));
                    }

                } else
                    CustomToast.showContent(context, getString(R.string.file_name_exist));
                getDialog().dismiss();


                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(binding.textInput, InputMethodManager.SHOW_IMPLICIT);

                break;
        }

    }

    public interface onCreateNewFolder{
        void onCreateNewFolderListener(String name);
    }

}
