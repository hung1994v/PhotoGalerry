package bsoft.com.lib_filter.filter.model;

import java.util.List;

import bsoft.com.lib_filter.filter.model.FilterHorizontalChild;
import bsoft.com.lib_filter.filter.model.FilterHorizontalParent;
import bsoft.com.lib_filter.filter.recycler.model.ExpandableWrapper;

public class FilterChild {
    private int currentParent;
    private ExpandableWrapper<FilterHorizontalParent, FilterHorizontalChild> expandableWrapper;

    public FilterChild() {
    }

    public FilterChild(int currentParent, ExpandableWrapper<FilterHorizontalParent, FilterHorizontalChild> expandableWrapper) {
        this.currentParent = currentParent;
        this.expandableWrapper = expandableWrapper;
    }

    public int getCurrentParent() {
        return currentParent;
    }

    public void setCurrentParent(int currentParent) {
        this.currentParent = currentParent;
    }

    public ExpandableWrapper<FilterHorizontalParent, FilterHorizontalChild> getExpandableWrapper() {
        return expandableWrapper;
    }

    public void setExpandableWrapper(ExpandableWrapper<FilterHorizontalParent, FilterHorizontalChild> expandableWrapper) {
        this.expandableWrapper = expandableWrapper;
    }
}
