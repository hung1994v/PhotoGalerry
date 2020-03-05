package com.photo.splashfunphoto.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bsoft.core.AdmobFull;

import com.photo.gallery.R;
import com.photo.gallery.callback.OnDialogEventListener;
import com.photo.gallery.models.FileItem;
import com.photo.gallery.utils.ConstValue;
import com.photo.gallery.utils.CustomToast;
import com.photo.gallery.utils.FileUtil;
import com.photo.gallery.utils.Flog;
import com.photo.splashfunphoto.EditPhotoActivity;
import com.photo.splashfunphoto.adapter.EditerStickerAdapter;
import com.photo.splashfunphoto.adapter.IconStickerAdapter;
import com.photo.splashfunphoto.adapter.text.TextFontAdapter;
import com.photo.splashfunphoto.dialog.MyProgressDialog;
import com.photo.splashfunphoto.fragment.menu.text.MenuTextFragment;
import com.photo.splashfunphoto.fragment.menu.text.TextEditFragment;
import com.photo.splashfunphoto.fragment.menu.text.TextSettingFragment;
import com.photo.splashfunphoto.model.StickerModel;
import com.photo.splashfunphoto.model.TextModel;
import com.photo.splashfunphoto.ui.custtom.CollageView;
import com.photo.splashfunphoto.ui.custtom.text.BaseItem;
import com.photo.splashfunphoto.ui.custtom.text.ItemBubbleTextView;
import com.photo.splashfunphoto.ui.custtom.text.ItemStickerView;
import com.photo.splashfunphoto.ui.custtom.text.ListAdaptiveItem;
import com.photo.splashfunphoto.utils.BitmapSwap;
import com.photo.splashfunphoto.utils.ConstList;
import com.photo.splashfunphoto.utils.ResizeImage;
import com.photo.splashfunphoto.utils.Statics;
import com.photo.splashfunphoto.adapter.EditerStickerAdapter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bsoft.com.lib_filter.filter.FilterFragment;
import bsoft.com.lib_filter.filter.gpu.util.BitmapUtil;

import static android.app.Activity.RESULT_OK;


public class LibraryFragment extends BaseFragment implements View.OnClickListener, IconStickerAdapter.OnIconListener,
        BaseItem.OnItemInteractListener, EditerStickerAdapter.OnItemEditerListener,
        OpacityFragment.OnOpacityListener,
        TextSettingFragment.OnTextSettingListener,
        TextFontAdapter.OnTextFontListener,
        FilterFragment.HandleBackFilter,
        EditFragment.HandleBackEdit, StickerFragment.OnStickerListener, OverlayFragment.HandleBackOverlay, TextEditFragment.OnTextEditListener, InputTextStickerFragment.OnInputTextStickerListener, EditerStickerFragment.OnEditStickerListener, MenuTextFragment.OnMenuTextListener {
    private ProgressDialog progressDialog;
    private ImageView mIPhoto;
    private String mImagePathOpenWith;
    private String mImagePath;
    private DisplayMetrics mDisplayMetrics;
    private Bitmap mBitmapPhoto;
    private CollageView mCollageView;
    private FrameLayout mContainerPhoto;
    private String st;
    private String stringFilePath;
    private LinearLayout mFrameOverlay;
    private RelativeLayout mTopbar;
    private int heightBottom = 0;
    public boolean menuCheck;
    private RelativeLayout mContainerLib;
    private LinearLayoutCompat mLayoutSquare;
    private List<String> mListIconSticker = new ArrayList<>();
    private ArrayList<StickerModel> stickerModels = new ArrayList<>();
    private ArrayList<TextModel> textModels = new ArrayList<>();
    private ArrayList<Bitmap> stickerBitmap = new ArrayList<>();
    private ArrayList<ItemBubbleTextView> textBubble = new ArrayList<>();
    private ItemStickerView savedStickerView;
    private TextView mTitleLib;
    private boolean isOpenWidth;
    private FileItem fileItem;


    public static LibraryFragment newInstance(String mImagePath, FileItem fileItem, boolean isOpenWidth) {
        LibraryFragment fragment = new LibraryFragment();
        fragment.isOpenWidth = isOpenWidth;
        fragment.fileItem = fileItem;
        fragment.mImagePathOpenWith = mImagePath;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void backPressed() {
        Fragment fragment = null;
        if (getFragmentManager() != null) {
            fragment = getFragmentManager().findFragmentById(R.id.container_menu_bottom);
            if(fragment instanceof StickerFragment){
                getFragmentManager().popBackStack();
            }else if(fragment instanceof MenuTextFragment){
                getFragmentManager().popBackStack();
            }else {
                requireActivity().finish();
            }
        }

    }

    public void clearLightStatusBar(Activity activity) {
        int defaultPrimary = ContextCompat.getColor(activity, R.color.s_black);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = activity.getWindow().getDecorView().getSystemUiVisibility(); // get current flag
            flags = flags ^ View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; // use XOR here for remove LIGHT_STATUS_BAR from flags
            activity.getWindow().getDecorView().setSystemUiVisibility(flags);
            activity.getWindow().setStatusBarColor(defaultPrimary); // optional
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_library, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        clearLightStatusBar(getActivity());
        initView(view);
        getBitmapFromPath();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void initView(View view) {
        mIPhoto = (ImageView) view.findViewById(R.id.img_photo);
        mTitleLib = (TextView) view.findViewById(R.id.title_lib);

        view.findViewById(R.id.btn_Overlay).setOnClickListener(this);
        view.findViewById(R.id.btn_Filter).setOnClickListener(this);
        view.findViewById(R.id.btn_Edit).setOnClickListener(this);
        view.findViewById(R.id.btn_sticker).setOnClickListener(this);
        view.findViewById(R.id.btn_text).setOnClickListener(this);
        view.findViewById(R.id.btn_library_exit).setOnClickListener(this);
        view.findViewById(R.id.btn_library_reset).setOnClickListener(this);
        view.findViewById(R.id.btn_library_save).setOnClickListener(this);


        mContainerPhoto = (FrameLayout) view.findViewById(R.id.container_photo);
        mTopbar = (RelativeLayout) view.findViewById(R.id.topBar);
        mCollageView = (CollageView) view.findViewById(R.id.collageview_photo);
        mContainerLib = (RelativeLayout) view.findViewById(R.id.container_library);
    }


    private void setImageView() {
        mIPhoto.setImageBitmap(mBitmapPhoto);
        BitmapSwap.mBitmapOld = mBitmapPhoto;
        setContainerView();
    }

    private void setContainerView() {
        mDisplayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        mContainerLib.post(new Runnable() {
            @Override
            public void run() {
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.CENTER;
                mCollageView.setLayoutParams(params);
                mCollageView.invalidate();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void getBitmapFromPath() {

            if(!isOpenWidth){
                mImagePath = mImagePathOpenWith;
            }else {
                mImagePath = fileItem.path;
            }

            Flog.d("AAAAAAa", "fileItem: "+ fileItem.path  + "imagePaht: " + mImagePath);
            PreferenceManager.getDefaultSharedPreferences(getActivity())
                    .edit()
                    .putString(EditPhotoActivity.FILE_PATH, mImagePath)
                    .apply();
            mDisplayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
            new LoadImage(getActivity()).execute();


    }

    @Override
    public void backPressEdit() {
        mBitmapPhoto = BitmapSwap.mBitmapNew;
        BitmapSwap.mBitmapOld = mBitmapPhoto;
        mIPhoto.setImageBitmap(mBitmapPhoto);
        setContainerView();
    }

    @Override
    public void backPressFilter(Bitmap bitmap) {
        BitmapSwap.mBitmapNew = bitmap;
        mBitmapPhoto = BitmapSwap.mBitmapNew;
        BitmapSwap.mBitmapOld = mBitmapPhoto;
        mIPhoto.setImageBitmap(mBitmapPhoto);
        setContainerView();
    }

    @Override
    public void clickDoneSticker() {
        unSelectAll();
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void backPressOverlay(Bitmap bitmap) {
        mBitmapPhoto = bitmap;
        BitmapSwap.mBitmapOld = mBitmapPhoto;
        mIPhoto.setImageBitmap(mBitmapPhoto);
        setContainerView();
    }

    @Override
    public void saveInputText(String txt, boolean isCheckEdit) {
        setTextClick(txt, isCheckEdit);
        // fix keyboard xaomi
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(getActivity() != null){
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    if(getActivity().getCurrentFocus() != null){
                        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    }
                }
            }
        },200);
    }

    @Override
    public void clickDoneEditSticker() {
        unSelectAll();
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void clickDoneMenuText() {
        unSelectAll();
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    public class LoadImage extends AsyncTask<Void, Void, Void> {
        private Context mContext;

        public LoadImage(Context context) {
            mContext = context;
            progressDialog = MyProgressDialog.newInstance(mContext, mContext.getString(R.string.Please));
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (mImagePath != null) {
                mBitmapPhoto = new ResizeImage(mContext).getBitmap(mImagePath, mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels);
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            if (mBitmapPhoto == null) {
                Toast.makeText(mContext, R.string.imgerror, Toast.LENGTH_SHORT).show();
                getActivity().finish();
            } else {
                setImageView();
            }
        }

    }

    @Override
    public void onClick(View view) {
        Fragment menuFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.content_main);
        Fragment botFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.container_menu_bottom);

        if(System.currentTimeMillis() % 3 == 0){
            if(view.getId() != R.id.btn_sticker && view.getId() != R.id.btn_text && view.getId() != R.id.btn_library_exit){
                AdmobFull.show();
            }
        }

        switch (view.getId()) {
            case R.id.btn_Overlay:
                Bundle bundle2 = new Bundle();
                bundle2.putString(EditPhotoActivity.OVERLAY, "TEXTURE");

                requireActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.content_main, OverlayFragment.newInstance(bundle2, this, mBitmapPhoto))
                        .addToBackStack("OverlayOldFragment")
                        .commit();
                break;
            case R.id.btn_Filter:
                menuCheck = false;
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.content_main, FilterFragment.newInstance(mBitmapPhoto, this))
                        .addToBackStack("SquareFragment").commit();
                break;

            case R.id.btn_Edit:
                menuCheck = false;
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.content_main, EditFragment.newInstance(null, this))
                        .addToBackStack(EditFragment.class.getSimpleName())
                        .commit();
                break;

            case R.id.btn_sticker:
                menuCheck = false;
                ArrayList<String> lisStickerIcon = new ArrayList<>();
                for (int i = 1; i <= EditPhotoActivity.MAX_STICER; i++) {
                    mListIconSticker = ConstList.listLayout.get(ConstList.KEY_STICKER + i);
                    lisStickerIcon.add(mListIconSticker.get(0));
                    Log.d("lisStickerIcon ", " " + lisStickerIcon.get(i - 1));
                }
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.menu_up, R.anim.menu_down, R.anim.menu_pop_enter, R.anim.menu_pop_exit)
                        .add(R.id.container_menu_bottom, StickerFragment.newInstance(null, lisStickerIcon, this, this, R.id.container_library)).addToBackStack(StickerFragment.class.getSimpleName())
                        .commit();
                break;
            case R.id.btn_text:
                menuCheck = false;
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.content_main, InputTextStickerFragment.newFragment(this, null, false))
                        .addToBackStack(InputTextStickerFragment.class.getSimpleName())
                        .commit();
                break;

            case R.id.btn_library_exit:
                if (menuFragment != null) {
                    if (botFragment != null) {
                        if (botFragment instanceof MenuTextFragment) {
//                            mCollageView.dismissKeyboard();
                            unSelectAll();
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                        if ((botFragment instanceof StickerFragment || botFragment instanceof EditerStickerFragment)) {
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                    } else if (menuFragment instanceof LibraryFragment) {
                        getActivity().finish();
                    }
                }

                break;
            case R.id.btn_library_reset:
                mImagePath = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(EditPhotoActivity.FILE_PATH, null);
                mDisplayMetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
                showAlertDialog(getString(R.string.Confirm), getString(R.string.reset), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BitmapSwap.mBitmapOld = new ResizeImage(requireContext()).getBitmap(mImagePath, mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels);
                        mBitmapPhoto = BitmapSwap.mBitmapOld;
                        mIPhoto.setImageBitmap(mBitmapPhoto);
                    }
                }, getString(android.R.string.yes), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, getString(android.R.string.no));

                break;
            case R.id.btn_library_save:
                Bitmap bitmap = createFinalImage();
                showSaveDialog(getContext(), new OnDialogEventListener() {
                    @Override
                    public void onOk() {

//                    Uri uri = FileUtil.insert(EditPhotoActivity.this, mFileItem, mFinalBitmap);
//                    String savedPath = FileUtil.getRealPathFromURI(EditPhotoActivity.this, uri);
//                    FileUtil.scanMediaStore(EditPhotoActivity.this, mFileItem.path, savedPath);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MMdd_HHss");
                        String currentDateandTime = sdf.format(new Date());

                        String dstSavedDir = Environment.getExternalStorageDirectory().getAbsolutePath()
                                + File.separator + ConstValue.APP_FOLDER + File.separator;
                        String dstPath = dstSavedDir + fileItem.name + "_"
                                + currentDateandTime
                                + FileUtil.getExtension(fileItem.path);
                        try {
                            FileUtil.insert(getContext(), fileItem, bitmap , dstPath);

                            FileUtil.scanMediaStore(getContext(), dstPath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        Intent intent = new Intent();
                        intent.putExtra(ConstValue.EXTRA_FILE_ITEM_EDIITED, "aaa");
                        getActivity().setResult(RESULT_OK, intent);
                        CustomToast.showContent(getContext(), getString(R.string.save_photo_success));

                        getActivity().finish();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
        }



    }

    private void showSaveDialog(final Context context, final OnDialogEventListener listener) {

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        String title = context.getString(R.string.save_photo);
        String message = context.getString(R.string.confirm_save_to_storage_dialog);
        alert.setTitle(title)
                .setMessage(message)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (listener != null) {
                            listener.onCancel();
                        }
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        if (listener != null) {
                            listener.onOk();
                        }
                    }
                }).show();
    }

    @Override
    public void onItemIconClickListener(String s) {
        Fragment mIconFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.container_library);
        addStickerView(s);
    }

    private void addStickerView(String s) {
        if(stickerBitmap == null || stickerBitmap.size() > 17){
            Toast.makeText(requireContext(), "18 sticker max", Toast.LENGTH_SHORT).show();
            return;
        }
        final ItemStickerView stickerView = new ItemStickerView(mCollageView);
        stickerView.setOnItemInteractListener(this);
        stickerBitmap.add(BitmapUtil.getImageFromAssetsFile(getResources(), s));
        stickerView.setBitmap(BitmapUtil.getImageFromAssetsFile(getResources(), s));
        mCollageView.addItem(stickerView);
        Statics.OPACITY = stickerView.getOpacity();
        mCollageView.invalidate();
    }


    @Override
    public void onMovingItem(BaseItem item) {
        if (item instanceof ItemBubbleTextView) {
            Fragment menuTextFrag = getActivity().getSupportFragmentManager().findFragmentById(R.id.container_menu_bottom);
            mCollageView.dismissKeyboard();
            if (menuTextFrag instanceof MenuTextFragment) {
                if (((MenuTextFragment) menuTextFrag).getCurTab() == 0) {
                    ((MenuTextFragment) menuTextFrag).setHeightPager(0);
                }
            }
        }
    }


    public void unSelectAll() {
        try {
            mCollageView.unSelectAllComponent();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStopMovingItem(BaseItem item) {

    }

    @Override
    public void onItemDeleted(BaseItem item) {
        Fragment fragment1 = requireActivity().getSupportFragmentManager().findFragmentById(R.id.container_menu_bottom);
        if (fragment1 instanceof MenuTextFragment) {
            mCollageView.dismissKeyboard();
            requireActivity().getSupportFragmentManager().popBackStack();
        }
        if (fragment1 instanceof StickerFragment || fragment1 instanceof EditerStickerFragment) {
            requireActivity().getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onItemEdit(BaseItem item) {
        if (item instanceof ItemBubbleTextView) {
            Fragment fragment1 = requireActivity().getSupportFragmentManager().findFragmentById(R.id.container_menu_bottom);
            if (fragment1 instanceof MenuTextFragment) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_main, InputTextStickerFragment.newFragment(this, ((ItemBubbleTextView) item).getText(), true))
                    .addToBackStack(InputTextStickerFragment.class.getSimpleName())
                    .commit();
        }
    }

    @Override
    public void onItemClicked(BaseItem item, boolean isFirstClick) {
        Fragment curFragment = requireActivity().getSupportFragmentManager().findFragmentById(R.id.container_menu_bottom);

        if (item instanceof ItemStickerView) {
            if (!isCheckUnselected) {
                if (curFragment instanceof StickerFragment) {
                    requireActivity().getSupportFragmentManager().popBackStack();
                }
            }
            if (isCheckUnselected) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.menu_up, R.anim.menu_down, R.anim.menu_pop_enter, R.anim.menu_pop_exit)
                        .add(R.id.container_menu_bottom, EditerStickerFragment.newInstance(null, this, this, ((ItemStickerView) item).getOpacity()))
                        .addToBackStack(EditerStickerFragment.class.getSimpleName())
                        .commit();
                isCheckUnselected = false;
                return;
            }

            if (!(curFragment instanceof EditerStickerFragment)) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.menu_up, R.anim.menu_down, R.anim.menu_pop_enter, R.anim.menu_pop_exit)
                        .add(R.id.container_menu_bottom, EditerStickerFragment.newInstance(null, this, this, ((ItemStickerView) item).getOpacity()))
                        .addToBackStack(EditerStickerFragment.class.getSimpleName())
                        .commit();
            }
            isCheckUnselected = false;

        } else if (item instanceof ItemBubbleTextView) {
            if (isCheckUnselected) {
                ItemBubbleTextView bubbleTextView = (ItemBubbleTextView) item;
                curFragment = MenuTextFragment.newFragment(R.id.collageview_photo, this, this, this, this);
                Bundle bundle = new Bundle();
                bundle.putBoolean(MenuTextFragment.IS_INITIAL_EXPAND_KEY, !isFirstClick);
                bundle.putFloat(MenuTextFragment.TEXT_SIZE_KEY, bubbleTextView.getTextSize());
                bundle.putFloat(MenuTextFragment.PADDING_TEXT_KEY, bubbleTextView.getPadding());
                curFragment.setArguments(bundle);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.menu_up, R.anim.menu_down, R.anim.menu_pop_enter, R.anim.menu_pop_exit);
                ft
                        .add(R.id.container_menu_bottom, curFragment)
                        .addToBackStack(MenuTextFragment.class.getSimpleName())
                        .commit();
                isCheckUnselected = false;
                return;
            }

            if (curFragment instanceof StickerFragment) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }

            if (!(curFragment instanceof MenuTextFragment)) {
                ItemBubbleTextView bubbleTextView = (ItemBubbleTextView) item;
                curFragment = MenuTextFragment.newFragment(R.id.collageview_photo, this, this, this, this);
                Bundle bundle = new Bundle();
                bundle.putBoolean(MenuTextFragment.IS_INITIAL_EXPAND_KEY, !isFirstClick);
                bundle.putFloat(MenuTextFragment.TEXT_SIZE_KEY, bubbleTextView.getTextSize());
                bundle.putFloat(MenuTextFragment.PADDING_TEXT_KEY, bubbleTextView.getPadding());
                curFragment.setArguments(bundle);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.menu_up, R.anim.menu_down, R.anim.menu_pop_enter, R.anim.menu_pop_exit);
                ft
                        .add(R.id.container_menu_bottom, curFragment)
                        .addToBackStack(MenuTextFragment.class.getSimpleName())
                        .commit();
            }
        }
    }

    private boolean isCheckUnselected = false;

    @Override
    public void onItemUnselected(BaseItem item) {
        isCheckUnselected = true;
        Fragment fragmentBottom = requireActivity().getSupportFragmentManager().findFragmentById(R.id.container_menu_bottom);

        if (fragmentBottom instanceof MenuTextFragment) {
            mCollageView.dismissKeyboard();
            requireActivity().getSupportFragmentManager().popBackStack();
        }

        if ((fragmentBottom instanceof StickerFragment || fragmentBottom instanceof EditerStickerFragment)) {
            requireActivity().getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onItemOpacityClickListener(int opacity) {
        if (mCollageView.getCurrentItem() == null) return;
        ItemStickerView itemStickerView = (ItemStickerView) mCollageView.getCurrentItem();
        itemStickerView.setStickerPaint(opacity + 20);
        mCollageView.invalidate();
    }

    @Override
    public void onItemRotateClickListener() {
        ItemStickerView itemStickerView = (ItemStickerView) mCollageView.getCurrentItem();
        itemStickerView.postRotate5(Statics.ROTATE_STEP);
        mCollageView.invalidate();
    }

    @Override
    public void onItemIRotateClickListener() {
        ItemStickerView itemStickerView = (ItemStickerView) mCollageView.getCurrentItem();
        itemStickerView.postRotate5(-Statics.ROTATE_STEP);
        mCollageView.invalidate();
    }

    @Override
    public void onItemUpClickListener() {
        ItemStickerView itemStickerView = (ItemStickerView) mCollageView.getCurrentItem();
        itemStickerView.postTranslate(Statics.MOVE_UP, 0, -Statics.TRANSLATE_STEP);
        mCollageView.invalidate();

    }

    @Override
    public void onItemDownClickListener() {
        ItemStickerView itemStickerView = (ItemStickerView) mCollageView.getCurrentItem();
        itemStickerView.postTranslate(Statics.MOVE_DOWN, 0, Statics.TRANSLATE_STEP);
        mCollageView.invalidate();
    }

    @Override
    public void onItemLeftClickListener() {
        ItemStickerView itemStickerView = (ItemStickerView) mCollageView.getCurrentItem();
        itemStickerView.postTranslate(Statics.MOVE_LEFT, -Statics.TRANSLATE_STEP, 0);
        mCollageView.invalidate();
    }

    @Override
    public void onItemRightClickListener() {
        ItemStickerView itemStickerView = (ItemStickerView) mCollageView.getCurrentItem();
        itemStickerView.postTranslate(Statics.MOVE_RIGHT, Statics.TRANSLATE_STEP, 0);
        mCollageView.invalidate();
    }

    @Override
    public void onItemZoomInClickListener() {
        ItemStickerView itemStickerView = (ItemStickerView) mCollageView.getCurrentItem();
        itemStickerView.postScale(Statics.STEP_SCALE_IN);
        mCollageView.invalidate();
    }

    @Override
    public void onItemZoomOutClickListener() {
        ItemStickerView itemStickerView = (ItemStickerView) mCollageView.getCurrentItem();
        itemStickerView.postScale(Statics.STEP_SCALE_OUT);
        mCollageView.invalidate();
    }

    @Override
    public void onSeekbarOpacityListener(int intensity) {
        ItemStickerView itemStickerView = (ItemStickerView) mCollageView.getCurrentItem();
        Statics.OPACITY = intensity + 20;
        itemStickerView.setStickerPaint(Statics.OPACITY);
        mCollageView.invalidate();
    }


    private void setTextClick(String txt, boolean isCheckEdit) {
        if (!isCheckEdit) {
            ItemBubbleTextView bubbleTextView = new ItemBubbleTextView(mCollageView);
            bubbleTextView.setText(txt);
            bubbleTextView.setOnItemInteractListener(this);
            bubbleTextView.setTextColor(requireActivity().getResources().getColor(R.color.colorAccent));
            textBubble.add(bubbleTextView);
            mCollageView.addItem(bubbleTextView);
            mCollageView.requestKeyboard();
            mCollageView.invalidate();
            mCollageView.showRealEditText();

            MenuTextFragment curFragment = MenuTextFragment.newFragment(R.id.collageview_photo, this, this, this, this);
            Bundle bundle = new Bundle();
            bundle.putBoolean(MenuTextFragment.IS_INITIAL_EXPAND_KEY, true);
            bundle.putFloat(MenuTextFragment.TEXT_SIZE_KEY, bubbleTextView.getTextSize());
            bundle.putFloat(MenuTextFragment.PADDING_TEXT_KEY, bubbleTextView.getPadding());
            curFragment.setArguments(bundle);
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.menu_up, R.anim.menu_down, R.anim.menu_pop_enter, R.anim.menu_pop_exit);
            ft
                    .add(R.id.container_menu_bottom, curFragment)
                    .addToBackStack(MenuTextFragment.class.getSimpleName())
                    .commit();
        } else {
            if (mCollageView.getCurrentItem() instanceof ItemBubbleTextView) {
                ((ItemBubbleTextView) mCollageView.getCurrentItem()).setText(txt);
            }
        }
    }

    @Override
    public void onPatternClickListener(String s) {
        Bitmap bitmap = BitmapUtil.getImageFromAssetsFile(getResources(), s);
        Shader shader = new BitmapShader(bitmap,
                Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        ItemBubbleTextView view = (ItemBubbleTextView) mCollageView.getCurrentItem();
        view.setPatternType(shader);
        mCollageView.invalidate();
    }

    @Override
    public void onColorClickListener(@ColorInt int color) {
        try {
            if (mCollageView.getCurrentItem().getItemType() != BaseItem.ItemType.TEXT) return;
            ItemBubbleTextView view = (ItemBubbleTextView) mCollageView.getCurrentItem();
            view.setPatternType(null);
            view.setTextColor(color);
            mCollageView.invalidate();
        } catch (NullPointerException e) {
        }
    }

    @Override
    public void onTextSizeChangeListener(int unit) {
        try {
            if (mCollageView.getCurrentItem().getItemType() != BaseItem.ItemType.TEXT) return;
            ItemBubbleTextView view = (ItemBubbleTextView) mCollageView.getCurrentItem();
            view.setTextSize(unit);
            mCollageView.invalidate();
        } catch (NullPointerException e) {
        }
    }

    @Override
    public void onTextPaddingChangeListener(int unit) {
        try {
            if (mCollageView.getCurrentItem().getItemType() != BaseItem.ItemType.TEXT) return;
            ItemBubbleTextView view = (ItemBubbleTextView) mCollageView.getCurrentItem();
            view.setPaddingText(unit);
            mCollageView.invalidate();
        } catch (NullPointerException e) {
        }
    }

    @Override
    public void onFontChanged(Typeface typeface) {
        if (mCollageView.getCurrentItem().getItemType() != BaseItem.ItemType.TEXT) return;
        ItemBubbleTextView view = (ItemBubbleTextView) mCollageView.getCurrentItem();
        view.setFontType(typeface);
        mCollageView.invalidate();
    }

    @Override
    public void onPause() {
        super.onPause();
        mCollageView.dismissKeyboard();
        if ((progressDialog != null) && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private Bitmap createFinalImage() {
        saveView();
        TextPaint textPaint = new TextPaint();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);

        Bitmap bitmap = Bitmap.createBitmap(mBitmapPhoto.getWidth(), mBitmapPhoto.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 2));
        canvas.drawBitmap(mBitmapPhoto, 0, 0, paint);
        for (int i = 0; i < stickerModels.size(); i++) {
            paint.setAlpha(stickerModels.get(i).getmOpacity());
            canvas.drawBitmap(stickerModels.get(i).getmBitmap(), stickerModels.get(i).getmMatrix(), paint);
        }

        for (int i = 0; i < textModels.size(); i++) {
            textPaint.setColor(textModels.get(i).getmTextPaint().getColor());
            textPaint.setTypeface(textModels.get(i).getmTextPaint().getTypeface());
            textPaint.setShader(textModels.get(i).getmTextPaint().getShader());
            canvas.drawBitmap(textModels.get(i).getmBitmap(), textModels.get(i).getmMatrix(), textPaint);
        }

        return bitmap;
    }

    private void saveView() {
        ListAdaptiveItem listAdaptiveItem = mCollageView.getListItem();
        savedStickerView = new ItemStickerView(mCollageView);
        textModels.clear();
        stickerModels.clear();
        for (int i = 0; i < listAdaptiveItem.size(); i++) {
            BaseItem baseItem = listAdaptiveItem.get(i);
            if (baseItem instanceof ItemStickerView) {
                stickerModels.add(new StickerModel(baseItem.getBitmap(), fitRatioMatrix(baseItem.getMatrix(), setProportion()), ((ItemStickerView) baseItem).getOpacity()));
            } else if (baseItem instanceof ItemBubbleTextView) {
                textModels.add(new TextModel(baseItem.getBitmap(), fitRatioMatrix(baseItem.getMatrix(), setProportion()), ((ItemBubbleTextView) baseItem).getTextFont()));
            }
        }
    }

    private float setProportion() {
        int widthBitmap = mBitmapPhoto.getWidth();
        int widthView = mCollageView.getWidth();
        return widthBitmap * 1.0f / widthView;
    }

    private Matrix fitRatioMatrix(Matrix matrix, float ratio) {
        Matrix tmp = new Matrix(matrix);
        float values[] = new float[9];
        matrix.getValues(values);
//        float scale_X = values[0];
//        float skew_X = values[1];
        float transform_X = values[2];
//        float skew_Y = values[3];
//        float scale_Y = values[4];
        float transform_Y = values[5];

        // set new values for matrix stickers:
        float newTransform_X = transform_X * ratio;
        float newTransform_Y = transform_Y * ratio;
        tmp.preScale(ratio, ratio);

        Matrix concatMatrix = new Matrix();
        concatMatrix.setTranslate(newTransform_X - transform_X, newTransform_Y - transform_Y);
        tmp.postConcat(concatMatrix);
        return tmp;
    }

}
