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

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import bsoft.com.lib_filter.R;
import bsoft.com.lib_filter.filter.adapter.filter.FilterNewAdapter;
import bsoft.com.lib_filter.filter.model.FilterChild;
import bsoft.com.lib_filter.filter.gpu.AsyncSizeProcess;
import bsoft.com.lib_filter.filter.gpu.GPUFilterRes;
import bsoft.com.lib_filter.filter.gpu.SquareUiLidowFilterManager;
import bsoft.com.lib_filter.filter.listener.OnFilterFinishedListener;
import bsoft.com.lib_filter.filter.listener.OnPostFilteredListener;
import bsoft.com.lib_filter.filter.adapter.filter.HorizontalExpandableAdapter;
import bsoft.com.lib_filter.filter.recycler.ExpandableRecyclerAdapter;
import bsoft.com.lib_filter.filter.model.FilterHorizontalChild;
import bsoft.com.lib_filter.filter.model.FilterHorizontalParent;
import bsoft.com.lib_filter.filter.recycler.model.ExpandableWrapper;


public class FilterFragment extends Fragment implements ExpandableRecyclerAdapter.ExpandCollapseListener, HorizontalExpandableAdapter.OnItemChildListener, View.OnClickListener, FilterNewAdapter.OnItemChildListener {
    private RecyclerView mRFilter;
    private Bitmap mBitmapFilter;
    private ImageView mImgFilter;
    private Bitmap resultBitmap;
    private ImageView mImaSave;
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
    private FrameLayout mAdView;
    private Bitmap mBitmapFilterChild ;
    private RecyclerView mRecycleFilterNew;
    private List<ExpandableWrapper<FilterHorizontalParent, FilterHorizontalChild>> mFilterHorizontalParentChildren = new ArrayList<>();
    private List<ExpandableWrapper<FilterHorizontalParent, FilterHorizontalChild>> mFilterHorizontalChildren = new ArrayList<>();
    private List<FilterChild> mFilterChildren = new ArrayList<>();
    private ArrayList<String> mListTitleFilter = new ArrayList<>();
    private List<GPUFilterRes> mGpuFilterRes = new ArrayList<>();
    private UnifiedNativeAd mUnifiedNativeAd;

    public FilterFragment() {

    }

    public static FilterFragment newInstance(Bitmap bitmap, HandleBackFilter backFilter) {
        FilterFragment fragment = new FilterFragment();
        fragment.mBitmapFilter = bitmap;
        fragment.handleBackFilter = backFilter;
        return fragment;
    }

    public static FilterFragment newInstance(Bitmap bitmap, HandleBackFilter backFilter, UnifiedNativeAd unifiedNativeAd) {
        FilterFragment fragment = new FilterFragment();
        fragment.mBitmapFilter = bitmap;
        fragment.handleBackFilter = backFilter;
        fragment.mUnifiedNativeAd = unifiedNativeAd;
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
        loadIconFilter();
        initView();
        setImage();
        mBitmapFilterChild = getResizedBitmap(mBitmapFilter);
        initRecyclerView();
        initAdBanner();
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

    private void initRecyclerView() {
        mListFilter = setUpTestData();
//        mExpandableAdapter = new HorizontalExpandableAdapter(getActivity(), mListFilter, resArray,mBitmapFilterChild).setOnItemChildListener(this);
//        mExpandableAdapter.setExpandCollapseListener(this);
//        mRFilter.setAdapter(mExpandableAdapter);
//        mRFilter.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mFilterHorizontalParentChildren = generateFlattenedParentChildList(mListFilter);
        for (int i = 0; i < mFilterHorizontalParentChildren.size(); i++) {
            mFilterHorizontalChildren.clear();
            mFilterHorizontalChildren.addAll(mFilterHorizontalParentChildren.get(i).getWrappedChildList());
            for (int j = 0; j < mFilterHorizontalChildren.size(); j++) {
                mFilterChildren.add(new FilterChild(i,mFilterHorizontalChildren.get(j)));
            }
            SquareUiLidowFilterManager mFiterManager = new SquareUiLidowFilterManager(requireContext(),i, "");
            for (int k = 0; k < mFiterManager.getCount(); k++) {
                mGpuFilterRes.add((GPUFilterRes) mFiterManager.getRes(k));
            }
        }
        mRecycleFilterNew.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        FilterNewAdapter filterNewAdapter = new FilterNewAdapter(getActivity(),getListTitleFilter(), mGpuFilterRes,mFilterChildren,mBitmapFilterChild);
        mRecycleFilterNew.setAdapter(filterNewAdapter);
        filterNewAdapter.setOnItemChildListener(this);
    }

    private void initView() {
        mRFilter = getView().findViewById(R.id.recycle_filter);
        mRecycleFilterNew = getView().findViewById(R.id.recycle_filter_new);
        mImgFilter = getView().findViewById(R.id.img_filter);
        mImaExit = getView().findViewById(R.id.btn_filter_exit);
        mImaSave = getView().findViewById(R.id.btn_filter_save);
        mImaExit.setOnClickListener(this);
        mImaSave.setOnClickListener(this);
    }

    private void initAdBanner(){
        mAdView = getView().findViewById(R.id.container_ads_banner);
        AdView adView = new AdView(requireContext());
        adView.setAdUnitId(getString(R.string.admob_banner_ad));
        adView.setAdSize(getAdSize());
        adView.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mAdView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                mAdView.setVisibility(View.GONE);
            }
        });
        mAdView.addView(adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        Display display = requireActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = (float) outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationBannerAdSizeWithWidth(getContext(), adWidth);
    }

    public Bitmap getResizedBitmap(Bitmap bm) {
        int width = 200;
        int height = bm.getHeight()*200/bm.getWidth();
        return Bitmap.createScaledBitmap(bm, width, height, false);
    }

    private List<ExpandableWrapper<FilterHorizontalParent, FilterHorizontalChild>> generateFlattenedParentChildList(List<FilterHorizontalParent> parentList) {
        List<ExpandableWrapper<FilterHorizontalParent, FilterHorizontalChild>> flatItemList = new ArrayList<>();

        int parentCount = parentList.size();
        for (int i = 0; i < parentCount; i++) {
            FilterHorizontalParent parent = parentList.get(i);
            generateParentWrapper(flatItemList, parent, parent.isInitiallyExpanded());
        }

        return flatItemList;
    }

    private void generateParentWrapper(List<ExpandableWrapper<FilterHorizontalParent, FilterHorizontalChild>> flatItemList, FilterHorizontalParent parent, boolean shouldExpand) {
        ExpandableWrapper<FilterHorizontalParent, FilterHorizontalChild> parentWrapper = new ExpandableWrapper<>(parent);
        flatItemList.add(parentWrapper);
        if (shouldExpand) {
            generateExpandedChildren(flatItemList, parentWrapper);
        }
    }

    private void generateExpandedChildren(List<ExpandableWrapper<FilterHorizontalParent, FilterHorizontalChild>> flatItemList, ExpandableWrapper<FilterHorizontalParent, FilterHorizontalChild> parentWrapper) {
        parentWrapper.setExpanded(true);

        List<ExpandableWrapper<FilterHorizontalParent, FilterHorizontalChild>> wrappedChildList = parentWrapper.getWrappedChildList();
        int childCount = wrappedChildList.size();
        for (int j = 0; j < childCount; j++) {
            ExpandableWrapper<FilterHorizontalParent, FilterHorizontalChild> childWrapper = wrappedChildList.get(j);
            flatItemList.add(childWrapper);
        }
    }

    private ArrayList<String> getListTitleFilter() {
        mListTitleFilter.add("ORIGINAL");
        mListTitleFilter.add("SEASON");
        mListTitleFilter.add("CLASSIC");
        mListTitleFilter.add("SWEET");
        mListTitleFilter.add("LOMO");
        mListTitleFilter.add("FILM");
        mListTitleFilter.add("FADE");
        mListTitleFilter.add("B&W");
        mListTitleFilter.add("VINTAGE");
        mListTitleFilter.add("HALO");
        return mListTitleFilter;
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
//        mExpandableAdapter.onRestoreInstanceState(savedInstanceState);
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
            public void postFinished(Bitmap bitmap) {

            }
        });
    }

    private void updateImagePic(OnFilterFinishedListener listener) {
        if (mBitmapFilter != null && !mBitmapFilter.isRecycled()) {
            AsyncSizeProcess.executeAsyncFilter(getActivity(), mBitmapFilter, curFilterRes, null, null, new AnonymousClass1(listener));
        }
    }

    @Override
    public void onItemChildClickListener(int parent, int child) {
        curFilterRes = mGpuFilterRes.get(child);
        updateImagePic(new OnFilterFinishedListener() {
            @Override
            public void postFinished(Bitmap bitmap) {

            }
        });
    }

    class AnonymousClass1 implements OnPostFilteredListener {
        private OnFilterFinishedListener val$listener;

        AnonymousClass1(OnFilterFinishedListener onFilterFinishedListener) {
            val$listener = onFilterFinishedListener;
        }

        public void postFiltered(Bitmap result) {
            val$listener.postFinished(result);
            resultBitmap = result;
            mImgFilter.setImageBitmap(result);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_filter_exit) {
            requireActivity().getSupportFragmentManager().popBackStack();

        } else if (i == R.id.btn_filter_save) {
            Bitmap bmp = Bitmap.createBitmap(resultBitmap.getWidth(), resultBitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bmp);
            canvas.drawBitmap(resultBitmap, 0, 0, new Paint());
            if (!(bmp == resultBitmap || bmp == null || bmp.isRecycled())) {
                if (handleBackFilter != null) {
                    handleBackFilter.backPressFilter(bmp);
                    getActivity().getSupportFragmentManager().popBackStack();
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

    public interface HandleBackFilter {
        void backPressFilter(Bitmap bitmap);
    }
}
