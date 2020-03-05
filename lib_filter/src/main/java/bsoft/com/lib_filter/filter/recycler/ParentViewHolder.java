package bsoft.com.lib_filter.filter.recycler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import bsoft.com.lib_filter.filter.recycler.model.Parent;


public class ParentViewHolder<P extends Parent<C>, C> extends RecyclerView.ViewHolder implements View.OnClickListener {
    @Nullable
    private ParentViewHolderExpandCollapseListener mParentViewHolderExpandCollapseListener;
    private boolean mExpanded;
    P mParent;
    ExpandableRecyclerAdapter mExpandableAdapter;

    interface ParentViewHolderExpandCollapseListener {

        @UiThread
        void onParentExpanded(int flatParentPosition);

        @UiThread
        void onParentCollapsed(int flatParentPosition);
    }

    @UiThread
    public ParentViewHolder(@NonNull View itemView) {
        super(itemView);
        mExpanded = false;
    }

    @UiThread
    public P getParent() {
        return mParent;
    }

    @UiThread
    public int getParentAdapterPosition() {
        int flatPosition = getAdapterPosition();
        if (flatPosition == RecyclerView.NO_POSITION) {
            return flatPosition;
        }

        return mExpandableAdapter.getNearestParentPosition(flatPosition);
    }

    @UiThread
    public void setMainItemClickToExpand() {
        itemView.setOnClickListener(this);
    }


    @UiThread
    public boolean isExpanded() {
        return mExpanded;
    }


    @UiThread
    public void setExpanded(boolean expanded) {
        mExpanded = expanded;
    }

    @UiThread
    public void onExpansionToggled(boolean expanded) {

    }


    @UiThread
    void setParentViewHolderExpandCollapseListener(ParentViewHolderExpandCollapseListener parentViewHolderExpandCollapseListener) {
        mParentViewHolderExpandCollapseListener = parentViewHolderExpandCollapseListener;
    }

    @Override
    @UiThread
    public void onClick(View v) {
        if (mExpanded) {
            collapseView();
        } else {
            expandView();
        }
    }

    @UiThread
    public boolean shouldItemViewClickToggleExpansion() {
        return true;
    }

    @UiThread
    public void expandView() {
        setExpanded(true);
        onExpansionToggled(false);

        if (mParentViewHolderExpandCollapseListener != null) {
            mParentViewHolderExpandCollapseListener.onParentExpanded(getAdapterPosition());
        }
    }

    @UiThread
    protected void collapseView() {
        setExpanded(false);
        onExpansionToggled(true);

        if (mParentViewHolderExpandCollapseListener != null) {
            mParentViewHolderExpandCollapseListener.onParentCollapsed(getAdapterPosition());
        }
    }
}
