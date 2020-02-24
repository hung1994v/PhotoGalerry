package bsoft.com.lib_filter.filter.model;


import java.util.List;

import bsoft.com.lib_filter.filter.recycler.model.Parent;


public class FilterHorizontalParent implements Parent<FilterHorizontalChild> {

    private List<FilterHorizontalChild> mChildItemList;
    private String path;

    private boolean mInitiallyExpanded;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public List<FilterHorizontalChild> getChildList() {
        return mChildItemList;
    }

    public void setChildItemList(List<FilterHorizontalChild> childItemList) {
        mChildItemList = childItemList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return mInitiallyExpanded;
    }

    public void setInitiallyExpanded(boolean initiallyExpanded) {
        mInitiallyExpanded = initiallyExpanded;
    }

}
