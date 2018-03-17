package app.touched.com.touched.Utilities;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Anshul on 3/17/2018.
 */

public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {
    LinearLayoutManager layoutManagerLinear;
    GridLayoutManager layoutManagerGrid;
    boolean isGridLayout = false;

    public PaginationScrollListener(GridLayoutManager layoutManager) {
        this.layoutManagerGrid = layoutManager;
        isGridLayout = true;
    }

    public PaginationScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManagerLinear = layoutManager;
        isGridLayout = false;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int visibleItemCount;
        int totalItemCount;
        int firstVisibleItemPosition;

        if (isGridLayout) {
            visibleItemCount = layoutManagerGrid.getChildCount();
            totalItemCount = layoutManagerGrid.getItemCount();
            firstVisibleItemPosition = layoutManagerGrid.findFirstVisibleItemPosition();
        } else {
            visibleItemCount = layoutManagerLinear.getChildCount();
            totalItemCount = layoutManagerLinear.getItemCount();
            firstVisibleItemPosition = layoutManagerLinear.findFirstVisibleItemPosition();

        }

        if (!isLoading()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0) {
                loadMoreItems();
            }
        }

    }

    protected abstract void loadMoreItems();

//    public abstract int getTotalPageCount();
//
//    public abstract boolean isLastPage();

    public abstract boolean isLoading();

}
