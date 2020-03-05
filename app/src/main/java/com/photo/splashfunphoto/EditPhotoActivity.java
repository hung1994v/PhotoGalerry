package com.photo.splashfunphoto;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import com.photo.gallery.R;
import com.photo.gallery.models.FileItem;
import com.photo.gallery.utils.ConstValue;
import com.photo.gallery.utils.FileUtils;
import com.photo.splashfunphoto.fragment.LibraryFragment;
import com.photo.splashfunphoto.utils.ConstList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.photo.gallery.utils.ConstValue.VIDEO_OPEN_WITH;

public class EditPhotoActivity extends AppCompatActivity {
    public static String PICK_IMAGE_PATH = "PICK_IMAGE_PATH";
    public static String FUNCITION_FRAGMENT = "FUNCITION_FRAGMENT";
    public static String FILE_PATH = "FILE_PATH";
    public static String OVERLAY = "OVERLAY";
    public static String PICK_IMAGE_BCROP = "PICK_IMAGE_BCROP";
    public static String TEXT_HEIGHT = "TEXT_HEIGHT";
    private boolean isOpenWith;
    private Uri imageUri;
    private String imagePath;
    private FileItem mFileItem = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isOpenWith = getIntent().getBooleanExtra(VIDEO_OPEN_WITH, true);
        if (isOpenWith) {
            if (TextUtils.equals(getIntent().getAction(), Intent.ACTION_VIEW) && getIntent().getData() != null && getIntent().getType() != null && getIntent().getType().contains("image/")) {
                imageUri = getIntent().getData();
                imagePath = FileUtils.getPath2(this, imageUri);
            }
        } else {
            getData();
            if (mFileItem == null) {
                toastEditFailed();
                return;
            }
        }

        setContentView(R.layout.activity_edit_fun_photo);
        setLightStatusBar();

        loadAllLayout();

        Bundle bundle = new Bundle();
        if(isOpenWith){
            getSupportFragmentManager().beginTransaction().add(R.id.content_main, LibraryFragment.newInstance(imagePath,mFileItem,false)).addToBackStack(LibraryFragment.class.getSimpleName()).commitAllowingStateLoss();
        }else {
            getSupportFragmentManager().beginTransaction().add(R.id.content_main, LibraryFragment.newInstance(imagePath,mFileItem,true)).addToBackStack(LibraryFragment.class.getSimpleName()).commitAllowingStateLoss();
        }


    }

    protected void setLightStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = getWindow().getDecorView().getSystemUiVisibility(); // get current flag
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;   // add LIGHT_STATUS_BAR to flag
            getWindow().getDecorView().setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE); // optional
        }
    }


    private void getData() {

        if (getIntent() == null) {
            toastEditFailed();
            return;
        }

        mFileItem = getIntent().getParcelableExtra(ConstValue.EXTRA_FILE_ITEM_TO_EDIT);
    }

    private void toastEditFailed() {
        Toast.makeText(this, getString(R.string.cannot_edit_photo), Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_main);
        if(fragment instanceof LibraryFragment){
            ((LibraryFragment) fragment).backPressed();
        }else {
            if(getSupportFragmentManager().getBackStackEntryCount()>0){
                getSupportFragmentManager().popBackStack();
            }else {
                finish();
            }
        }
    }

    public static final int MAX_STICER = 7;

    private void loadAllLayout() {
        Map<String, List<String>> listMap = new HashMap<>();

        for (int i = 1; i <= MAX_STICER; i++) {
            listMap.put(ConstList.KEY_STICKER + (i), loadStickerFromAsset(i));
        }
        ConstList.init(listMap);
    }

    private ArrayList<String> loadStickerFromAsset(int numSticker) {
        ArrayList<String> list = new ArrayList<>();
        switch (numSticker) {
            case 1:
                list.clear();
                for (int i = 1; i <= 14; i++) {
                        list.add("sticker/1/" + i + ".png");
                }
                break;

            case 2:
                list.clear();
                for (int i = 1; i <= 49; i++) {
                        list.add("sticker/2/" + i + ".png");
                }
                break;

            case 3:
                list.clear();
                for (int i = 1; i <= 20; i++) {
                    list.add("sticker/3/" + i + ".png");
                }

                break;
            case 4:
                list.clear();
                for (int i = 1; i <= 21; i++) {
                        list.add("sticker/4/" + i + ".png");
                }

                break;
            case 5:
                list.clear();
                for (int i = 1; i <= 15; i++) {
                        list.add("sticker/5/" + i + ".png");
                }

                break;
            case 6:
                list.clear();
                for (int i = 1; i <= 53; i++) {
                    if(i < 10){
                        list.add("sticker/6/0" + i + ".png");
                    }else{
                        list.add("sticker/6/" + i + ".png");
                    }
                }
                break;
            case 7:
                list.clear();
                for (int i = 1; i <= 33; i++) {
                    if(i < 10){
                        list.add("sticker/7/0" + i + ".png");
                    }else{
                        list.add("sticker/7/" + i + ".png");
                    }
                }

                break;


        }
        return list;
    }

}
