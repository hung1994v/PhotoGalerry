package com.photo.gallery.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.photo.gallery.R;
import com.photo.gallery.adapters.AlbumsAdapter;
import com.photo.gallery.models.AlbumItem;
import com.photo.gallery.models.FileItem;
import com.photo.gallery.utils.ConstValue;
import com.photo.gallery.utils.CustomToast;
import com.photo.gallery.utils.DateUtils;
import com.photo.gallery.utils.DbUtils;
import com.photo.gallery.utils.FileUtil;
import com.photo.gallery.utils.Flog;
import com.photo.gallery.utils.KeyboardUtil;
import com.photo.gallery.utils.SharedPrefUtil;
import com.photo.gallery.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tung on 3/29/2018.
 */

public class SelectAlbumFragment extends BaseFragment implements AlbumsAdapter.OnAlbumsAdapterListener, View.OnClickListener {

    private static final java.lang.String TAG = SelectAlbumFragment.class.getSimpleName();
    private View btnBack, btnAdd, tvNoAlbumFound;
    private RecyclerView mRecyclerView = null;
    private AlbumsAdapter mAdapter = null;
    private ArrayList<AlbumItem> mListAlbums = new ArrayList<>();
    private ArrayList<FileItem> mListFileUsed = new ArrayList<>();
    private HashMap<String, ArrayList<FileItem>> mListFolders = null;
    private OnSelectAlbumListener listener = null;
    private String mInputText = "";
    private int mTypeAction = -1;
    private AlertDialog alertDialog = null;
    private ProgressDialog progressDialog = null;

    public static SelectAlbumFragment newInstance(HashMap<String, ArrayList<FileItem>> listFolders, ArrayList<FileItem> listPosChanged, int typeAction) {
        SelectAlbumFragment fragment = new SelectAlbumFragment();
        fragment.mListFolders = listFolders;
        fragment.mListFileUsed = listPosChanged;
        fragment.mTypeAction = typeAction;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_album, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initDialog();
        initViews();
        if (listener != null) {
            listener.onSelectAlbumFragmentReady();
        }
        initialize(mListFolders);

    }


    private void initViews() {
        View viewParent = getView();
        if (viewParent == null) {
            return;
        }
        viewParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // DO NOTHING.
            }
        });

        btnBack = viewParent.findViewById(R.id.btn_back);
        btnAdd = viewParent.findViewById(R.id.btn_add);
        tvNoAlbumFound = viewParent.findViewById(R.id.tv_no_album_found);
        mRecyclerView = (RecyclerView) viewParent.findViewById(R.id.recycler_view);
    }

    public void initialize(HashMap<String, ArrayList<FileItem>> listFolders) {
        mListFolders = listFolders;

        if (mListFolders == null) {
            return;
        }

        Flog.d(TAG, "list folder size=" + mListFolders.size());

        setValues();
    }


    private void setValues() {

        initList();
        mAdapter = new AlbumsAdapter(mContext, mListAlbums).setListener(this);

        StaggeredGridLayoutManager glm = new StaggeredGridLayoutManager(ConstValue.NUM_OF_COLS_DAY_GRIDVIEW,StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(glm);
        mRecyclerView.setAdapter(mAdapter);

        btnBack.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        showTvNoAlbumFound(false);
    }

    private void showTvNoAlbumFound(boolean shown) {
        tvNoAlbumFound.setVisibility(shown ? View.VISIBLE : View.GONE);
    }

    private void initList() {

        if (mListAlbums == null) {
            mListAlbums = new ArrayList<>();
        }
        mListAlbums.clear();
        for (Map.Entry<String, ArrayList<FileItem>> entry : mListFolders.entrySet()) {
            String key = entry.getKey();
            ArrayList<FileItem> items = mListFolders.get(key);

            FileItem firstItem = items.get(0);

            AlbumItem item = new AlbumItem();
            item.name = firstItem.folder;
            item.mSize = getTotalSize(items);
            item.pathFirstImg = firstItem.path;
            item.alBumId = firstItem.folder_ID;
            item.numFile = String.valueOf(items.size());
            mListAlbums.add(item);
        }
        Flog.d(TAG, "size albums=" + mListAlbums.size());
    }

    private long getTotalSize(ArrayList<FileItem> mlistAlbum){
        long size =0;
        for(FileItem albumItem : mlistAlbum){
            size = (size + albumItem.mSize);
        }
        return size;
    }
    public SelectAlbumFragment setListener(OnSelectAlbumListener listener) {
        this.listener = listener;
        return this;
    }

    private void initDialog() {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    return true; // Consumed
                }
                return false;
            }
        });
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void showDialog() {
            if (progressDialog != null) {
                progressDialog.show();
            }
    }

    private void hideDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void onItemAlbumClicked(int position) {

        if (listener != null) {
            listener.onBackSelectAlbumFragment();
        }

        Flog.d(TAG, "onOpenAlbumViewer: " + position + "_size=" + mListFileUsed.size());
        if (mListFileUsed != null && mListFileUsed.size() > 0) {
            try {

                String pathAlbum = null;
                if (position == -1) {
                    pathAlbum = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DCIM).getAbsolutePath() + File.separator
                            + "Album/" + mInputText + File.separator;
                } else {
                    AlbumItem albumItem = mListAlbums.get(position);
                    pathAlbum = new File(albumItem.pathFirstImg).getParent() + File.separator;
                }

                Flog.d(TAG, "numOfItems=" + mListFileUsed.size());
                Flog.d(TAG, "mPathAlbum=" + pathAlbum);

                showDialog();
                new HandleFileTask(mListFileUsed, pathAlbum)
                        .execute();

            } catch (Exception ex) {
                ex.printStackTrace();
                showToastInform(false);
            }
        } else {

            showToastInform(false);
        }

        if (getFragmentManager() != null) {
            getFragmentManager().popBackStack();
        }


    }

    private void showToastInform(boolean success) {
        if (success) {
            if (mTypeAction == ConstValue.ACTION_COPY) {
                FileUtil.toastSuccess(mContext, mContext.getString(R.string.copy));
            } else if (mTypeAction == ConstValue.ACTION_MOVE) {
                FileUtil.toastSuccess(mContext, mContext.getString(R.string.move));
            }
        } else {
            if (mTypeAction == ConstValue.ACTION_COPY) {
                FileUtil.toastFailed(mContext, mContext.getString(R.string.copy));
            } else if (mTypeAction == ConstValue.ACTION_MOVE) {
                FileUtil.toastFailed(mContext, mContext.getString(R.string.move));
            }
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_back:
                if (listener != null) {
                    listener.onBackSelectAlbumFragment();
                }
                break;
            case R.id.btn_add:
                openDialogInputNameAlbum();
                break;
        }
    }




    private void openDialogInputNameAlbum() {

        LayoutInflater li = LayoutInflater.from(mContext);
        View promptsView = li.inflate(R.layout.dialog_input_filename, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        TextView titleInput = (TextView) promptsView.findViewById(R.id.textView1);
        titleInput.setText(mContext.getString(R.string.enter_album_name));

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);
        userInput.setFilters(new InputFilter[]{Utils.filter});


        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(mContext.getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                mInputText = userInput.getText().toString();
                                if(Utils.isStringHasCharacterSpecial(mInputText)){

                                    CustomToast.showContent(mContext,mContext.getString(R.string.file_name_error2));
                                    userInput.setText("");
                                    return;
                                }else
                                    if (mInputText.length() <= 0) {
                                    Toast.makeText(mContext, mContext.getString(R.string.please_enter_name_of_album),
                                            Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                onItemAlbumClicked(-1, null);
                                KeyboardUtil.hideKeyboard(mContext, userInput);
                                dialog.cancel();
                            }
                        })
                .setNegativeButton(mContext.getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                KeyboardUtil.hideKeyboard(mContext, userInput);
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        alertDialog = alertDialogBuilder.create();
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }

        // show it
        alertDialog.show();

        // Initially disable the button
        ((AlertDialog) alertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
                .setEnabled(false);

        // Now set the textchange listener for edittext
        userInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    // Disable ok button
                    ((AlertDialog) alertDialog).getButton(
                            AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } else {
                    // Something into edit text. Enable the button.
                    ((AlertDialog) alertDialog).getButton(
                            AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }
        });

    }

    public void updateUI(HashMap<String, ArrayList<FileItem>> listFolders) {
        if (mRecyclerView == null || mAdapter == null) {
            Flog.d(TAG, "4recycler not init");
            return;
        }

        mListFolders = listFolders;

        if (mListFolders == null) {
            return;
        }

        Flog.d(TAG, "4list folder size=" + mListFolders.size());
        initList();
        mAdapter.notifyDataSetChanged();
    }

    public void setDialog(String message) {
        if (progressDialog != null) {
            progressDialog.setMessage(message);
        }
    }

    @Override
    public void onItemAlbumClicked(int position, AlbumItem album) {
        onItemAlbumClicked(position);
    }

    @Override
    public void onItemAlbumLongClicked(int position, AlbumItem album) {

    }

    public interface OnSelectAlbumListener {
        void onSelectAlbumFragmentReady();

        void onBackSelectAlbumFragment();

        void onUpdateMainUI(int typeAction, FileItem[] items);
    }
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm_ss");



    private class HandleFileTask extends AsyncTask<Void, Integer, Boolean> {

        private ArrayList<FileItem> mListFileUsed = new ArrayList<>();
        private String mPathAlbum = null;
        private String[] mCopyPaths = null, mDelPaths = null;
        private int mNumOfFiles = 0;
        private FileItem[] mArrCopied = null;

        public HandleFileTask(ArrayList<FileItem> listFileUsed, String pathAlbum) {
            mListFileUsed = listFileUsed;
            mPathAlbum = pathAlbum;

            mNumOfFiles = mListFileUsed.size();
            mArrCopied = new FileItem[mNumOfFiles];
            mCopyPaths = new String[mNumOfFiles];
            mDelPaths = new String[mNumOfFiles];
        }


        @Override
        protected void onPreExecute() {
            setDialog(mContext.getString(R.string.loading) + " " + 0 + "%");
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            boolean success = true;

            for (int i = 0; i < mNumOfFiles; i++) {
                if(isDupplicatedPath(mPathAlbum, new File(mListFileUsed.get(i).path).getParent() + File.separator))
                {
                    continue;
                }
                FileItem fileItem = mListFileUsed.get(i);

                String actionName = (mTypeAction==ConstValue.ACTION_COPY?mContext.getString(R.string.copy):mContext.getString(R.string.move));

                String cmpName = fileItem.name + FileUtil.getExtension(fileItem.path);
                String nameNewFile;
                if (isDupplicatedName(mPathAlbum, cmpName)) {
                    if(fileItem.name.contains(actionName)){
                        nameNewFile = fileItem.name.split(actionName)[0];
                    }else {
                        nameNewFile = fileItem.name + " (" + actionName + " "
                                +  simpleDateFormat.format(new Date()) + ")";
                    }

                } else {
                    nameNewFile = fileItem.name;
                }
                nameNewFile += FileUtil.getExtension(fileItem.path);

                String srcPath = fileItem.path;
                String dstPath = mPathAlbum + nameNewFile;
                Flog.d(TAG, "copy " + i + ": src=" + srcPath + "\n\tdst=" + dstPath);
                boolean exists = FileUtil.isFileExistsInMediaStore(mContext, dstPath);
                Flog.d(TAG, "eexists=" + exists);
                if (exists) {
                    String name;
                    if(fileItem.name.contains(actionName)){
                        name = fileItem.name.split(actionName)[0];
                    }else {
                        name = fileItem.name + " (" + actionName + " "
                                +  simpleDateFormat.format(new Date()) + ")";
                    }
                    dstPath = mPathAlbum + name +  " (" + actionName + " "
                            +  simpleDateFormat.format(new Date()) + ")";
                }

                File dstFile  = new File(dstPath);
                File srcFile  = new File(srcPath);
                try {
                    success &= FileUtil.copy(mContext, srcFile, dstFile, fileItem);
                } catch (IOException e) {
                    e.printStackTrace();
                    success &= false;
                }

                mCopyPaths[i] = dstPath;

                FileItem fileCopied = fileItem.copy();
                fileCopied._ID = "";
                fileCopied.folder_ID = "";
                fileCopied.folder = dstFile.getParentFile().getName();
                fileCopied.date_modified = String.valueOf(System.currentTimeMillis());
                fileCopied.name = dstFile.getName();
                fileCopied.path = dstFile.getAbsolutePath();
                mArrCopied[i] = fileCopied;

                if (mTypeAction==ConstValue.ACTION_MOVE) {
                    boolean deleted = DbUtils.deleteFavourite(fileItem);
                    FileItem dstItem = fileItem.copy();
                    dstItem._ID = "";
                    dstItem.folder_ID = "";
                    dstItem.folder = dstFile.getParentFile().getName();
                    dstItem.date_modified = String.valueOf(System.currentTimeMillis());
                    dstItem.name = dstFile.getName();
                    dstItem.path = dstFile.getAbsolutePath();

                    if (deleted) {
                        DbUtils.addFavourite(dstItem);
                    }

                    success &= FileUtil.delete(mContext, Uri.fromFile(srcFile));
                    mDelPaths[i] = srcPath;
                }
                publishProgress(i);
            }

            return success;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int cnt = values[0];
            setDialog(mContext.getString(R.string.loading) + " " + (cnt * 100) / mNumOfFiles + "%");
        }

        @Override
        protected void onPostExecute(Boolean success) {
            hideDialog();
            if (success) {

                FileUtil.scanMediaStore(mContext, mCopyPaths);

                if (mTypeAction==ConstValue.ACTION_MOVE) {
                    FileUtil.scanMediaStore(mContext, mDelPaths);
                }

                if (listener != null) {
                    listener.onUpdateMainUI(mTypeAction, mArrCopied);
                }
                showToastInform(true);
            } else {
                showToastInform(false);
            }
        }
    }

    private boolean isDupplicatedName(String pathDir, String nameFile) {
        File albumDir = new File(pathDir);
        File[] list = albumDir.listFiles();
        if (list == null) {
            return false;
        }
        for (File file:list) {
            Flog.d(TAG, "name: 1="+file.getName()+"_2="+nameFile);
            if (file.getName().equals(nameFile)) {
                return true;
            }
        }
        return false;
    }

    private boolean isDupplicatedPath(String pathDir, String nameFile) {
        return pathDir.equals(nameFile);
    }
}
