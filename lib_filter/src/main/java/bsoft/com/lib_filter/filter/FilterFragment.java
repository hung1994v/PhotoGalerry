package bsoft.com.lib_filter.filter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import bsoft.com.lib_filter.R;
import bsoft.com.lib_filter.filter.gpu.AsyncSizeProcess;
import bsoft.com.lib_filter.filter.gpu.GPUFilterRes;
import bsoft.com.lib_filter.filter.gpu.SquareUiLidowFilterManager;
import bsoft.com.lib_filter.filter.listener.OnFilterFinishedListener;
import bsoft.com.lib_filter.filter.listener.OnPostFilteredListener;
import bsoft.com.lib_filter.filter.adapter.filter.HorizontalExpandableAdapter;
import bsoft.com.lib_filter.filter.recycler.ExpandableRecyclerAdapter;
import bsoft.com.lib_filter.filter.model.FilterHorizontalChild;
import bsoft.com.lib_filter.filter.model.FilterHorizontalParent;


public class FilterFragment extends Fragment implements ExpandableRecyclerAdapter.ExpandCollapseListener, HorizontalExpandableAdapter.OnItemChildListener, View.OnClickListener {
    private RecyclerView mRFilter;
    private Bitmap mBitmapFilter;
    private ImageView mImgFilter;
    private Bitmap resultBitmap;
    private RelativeLayout mContenmain;
    private LinearLayoutCompat mImaSave;
    private ImageView mImaExit;

    private HandleBackFilter handleBackFilter;
    private SquareUiLidowFilterManager mFiterManager;
    private ArrayList<FilterHorizontalParent> mListFilter;
    private HorizontalExpandableAdapter mExpandableAdapter;
    private Map<Integer, ArrayList<GPUFilterRes>> listFilter = new HashMap<>();
    private ArrayList<GPUFilterRes> resArray = new ArrayList<>();
    List<FilterHorizontalChild> childItemList;
    private GPUFilterRes curFilterRes;
    public ArrayList<String> mListIconFilter = new ArrayList<>();
    private AdView mAdView;


    public FilterFragment() {

    }

    public static FilterFragment newInstance(Bitmap bitmap, HandleBackFilter backFilter) {
        FilterFragment fragment = new FilterFragment();
        fragment.mBitmapFilter = bitmap;
        fragment.handleBackFilter = backFilter;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initImageLoader();
        loadIconFilter();
        initView();
//        initAdmob();
        setImage();
        initRecyclerView();
    }

    private void setImage() {
        mImgFilter.setImageBitmap(mBitmapFilter);
        resultBitmap = mBitmapFilter;
    }

    private void getIconFilter(int num) {
        int i;
        resArray = new ArrayList<>();
        resArray.clear();
        mFiterManager = new SquareUiLidowFilterManager(getContext(), num, "");
        int count = this.mFiterManager.getCount();
        for (int j = 0; j < count; j++) {
            resArray.add((GPUFilterRes) mFiterManager.getRes(j));
        }
    }

    private void initAdmob() {
        mAdView = (AdView) getView().findViewById(R.id.adView_filter);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void initRecyclerView() {
        mListFilter = setUpTestData();
        mExpandableAdapter = new HorizontalExpandableAdapter(getActivity(), mListFilter, resArray).setOnItemChildListener(this);
        mExpandableAdapter.setExpandCollapseListener(this);
        mRFilter.setAdapter(mExpandableAdapter);
        mRFilter.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void initView() {
        mRFilter = (RecyclerView) getView().findViewById(R.id.recycle_filter);
        mImgFilter = (ImageView) getView().findViewById(R.id.img_filter);
        mContenmain = (RelativeLayout) getView().findViewById(R.id.content_main_filter);
        mImaExit = (ImageView) getView().findViewById(R.id.btn_filter_exit);
        mImaSave = (LinearLayoutCompat) getView().findViewById(R.id.btn_filter_save);
        mImaExit.setOnClickListener(this);
        mImaSave.setOnClickListener(this);
    }

    @NonNull
    private ArrayList<FilterHorizontalParent> setUpTestData() {
        ArrayList<FilterHorizontalParent> horizontalParentList = new ArrayList<>();
        for (int i = 0; i <= 9; i++) {
            childItemList = new ArrayList<>();
            switch (i) {
                case 0:
                    getIconFilter(0);
                    for (int j = 0; j < resArray.size(); j++) {
                        FilterHorizontalChild horizontalChild0 = new FilterHorizontalChild();
                        horizontalChild0.setGpuFilterRes(resArray.get(j));
                        childItemList.add(horizontalChild0);
                    }
                    break;
                case 1:
                    getIconFilter(1);
                    for (int j = 0; j < resArray.size(); j++) {
                        FilterHorizontalChild horizontalChild1 = new FilterHorizontalChild();
                        horizontalChild1.setGpuFilterRes(resArray.get(j));
                        childItemList.add(horizontalChild1);
                    }
                    break;

                case 2:
                    getIconFilter(2);
                    for (int j = 0; j < resArray.size(); j++) {
                        FilterHorizontalChild horizontalChild2 = new FilterHorizontalChild();
                        horizontalChild2.setGpuFilterRes(resArray.get(j));
                        childItemList.add(horizontalChild2);
                    }
                    break;

                case 3:
                    getIconFilter(3);
                    for (int j = 0; j < resArray.size(); j++) {
                        FilterHorizontalChild horizontalChild3 = new FilterHorizontalChild();
                        horizontalChild3.setGpuFilterRes(resArray.get(j));
                        childItemList.add(horizontalChild3);
                    }
                    break;

                case 4:
                    getIconFilter(4);
                    for (int j = 0; j < resArray.size(); j++) {
                        FilterHorizontalChild horizontalChild4 = new FilterHorizontalChild();
                        horizontalChild4.setGpuFilterRes(resArray.get(j));
                        childItemList.add(horizontalChild4);
                    }
                    break;


                case 5:
                    getIconFilter(5);
                    for (int j = 0; j < resArray.size(); j++) {
                        FilterHorizontalChild horizontalChild5 = new FilterHorizontalChild();
                        horizontalChild5.setGpuFilterRes(resArray.get(j));
                        childItemList.add(horizontalChild5);
                    }
                    break;

                case 6:
                    getIconFilter(6);
                    for (int j = 0; j < resArray.size(); j++) {
                        FilterHorizontalChild horizontalChild6 = new FilterHorizontalChild();
                        horizontalChild6.setGpuFilterRes(resArray.get(j));
                        childItemList.add(horizontalChild6);
                    }
                    break;


                case 7:
                    getIconFilter(7);
                    for (int j = 0; j < resArray.size(); j++) {
                        FilterHorizontalChild horizontalChild7 = new FilterHorizontalChild();
                        horizontalChild7.setGpuFilterRes(resArray.get(j));
                        childItemList.add(horizontalChild7);
                    }
                    break;

                case 8:
                    getIconFilter(8);
                    for (int j = 0; j < resArray.size(); j++) {
                        FilterHorizontalChild horizontalChild8 = new FilterHorizontalChild();
                        horizontalChild8.setGpuFilterRes(resArray.get(j));
                        childItemList.add(horizontalChild8);
                    }
                    break;

                case 9:
                    getIconFilter(9);
                    for (int j = 0; j < resArray.size(); j++) {
                        FilterHorizontalChild horizontalChild9 = new FilterHorizontalChild();
                        horizontalChild9.setGpuFilterRes(resArray.get(j));
                        childItemList.add(horizontalChild9);
                    }
                    break;
            }

            FilterHorizontalParent horizontalParent = new FilterHorizontalParent();
            horizontalParent.setChildItemList(childItemList);
            horizontalParent.setPath(mListIconFilter.get(i));
            horizontalParentList.add(horizontalParent);
        }
        return horizontalParentList;
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        mExpandableAdapter.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onParentExpanded(int parentPosition) {

//        initRecyclerView(parentPosition + 1);
    }

    @Override
    public void onParentCollapsed(int parentPosition) {

    }

    @Override
    public void onItemChildClick(int parent, int child) {
        mFiterManager = new SquareUiLidowFilterManager(getContext(), parent, "");
        GPUFilterRes res = (GPUFilterRes) mFiterManager.getRes(child);
        curFilterRes = res;
        updateImagePic(new OnFilterFinishedListener() {
            @Override
            public void postFinished() {

            }
        });
    }

    private void updateImagePic(OnFilterFinishedListener listener) {
        if (mBitmapFilter != null && !mBitmapFilter.isRecycled()) {
            AsyncSizeProcess.executeAsyncFilter(getActivity(), mBitmapFilter, curFilterRes, null, null, new AnonymousClass1(listener));
        }
    }

    class AnonymousClass1 implements OnPostFilteredListener {
        private OnFilterFinishedListener val$listener;

        AnonymousClass1(OnFilterFinishedListener onFilterFinishedListener) {
            val$listener = onFilterFinishedListener;
        }

        public void postFiltered(Bitmap result) {
            val$listener.postFinished();
            resultBitmap = result;
            mImgFilter.setImageBitmap(result);
        }
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_filter_exit) {
            if (handleBackFilter != null) {
                handleBackFilter.onFilterExit();
            }

        } else if (i == R.id.btn_filter_save) {
            Bitmap bmp = Bitmap.createBitmap(resultBitmap.getWidth(), resultBitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bmp);
            canvas.drawBitmap(resultBitmap, 0, 0, new Paint());
            if (!(bmp == resultBitmap || bmp == null || bmp.isRecycled())) {

                if (handleBackFilter != null) {
                    handleBackFilter.backPressFilter(bmp);
                    getActivity().getSupportFragmentManager().popBackStack();
                    handleBackFilter.onFilterExit();
                }
            }

        }
    }

    public void loadIconFilter() {
        mListIconFilter.clear();
        for (int i = 0; i <= 9; i++) {
            mListIconFilter.add("filter/icon/f" + i + ".png");
        }
    }

    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .displayer(new FadeInBitmapDisplayer(200))
                .cacheOnDisk(false)
                .cacheInMemory(false)
                .build();
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(getActivity());

        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        //  config.memoryCache(new LRULimitedMemoryCache(50*1024*1024));
        //   config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        //   config.diskCacheSize(50*1024*1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.defaultDisplayImageOptions(defaultOptions);
//        config.writeDebugLogs(); // Remove for release app
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    public interface HandleBackFilter {
        void backPressFilter(Bitmap bitmap);
        void onFilterExit();
    }


}
