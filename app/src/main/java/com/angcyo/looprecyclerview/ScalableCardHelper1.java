package com.angcyo.looprecyclerview;

import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.lang.ref.WeakReference;


/**
 * Created by yjwfn on 2017/9/21.
 */

public class ScalableCardHelper1 {

    private static final float STAY_SCALE = 0.5f;

    private String TAG = "ScalableCardHelper";
    private LinearSnapHelper snapHelper = new LinearSnapHelper();
    private RecyclerView recyclerView;
    private WeakReference<OnPageChangeListener> pageChangeListenerRef;

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            pageScrolled();
        }
    };

    public ScalableCardHelper1(OnPageChangeListener pageChangeListener) {
        if (pageChangeListener != null) {
            this.pageChangeListenerRef = new WeakReference<>(pageChangeListener);
        }
    }

    public ScalableCardHelper1() {
        this(null);
    }

    private void pageScrolled() {
        if (recyclerView == null || recyclerView.getChildCount() == 0) {
            return;
        }
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        View snapingView = snapHelper.findSnapView(layoutManager);
        int snapingViewPosition = recyclerView.getChildAdapterPosition(snapingView);
        Log.e(TAG, snapingViewPosition + "");
        View centerSanpingView = snapingView.findViewById(R.id.text_view);
        View leftSnapingView = layoutManager.findViewByPosition(snapingViewPosition - 1).findViewById(R.id.text_view);
        View left2SnapingView = layoutManager.findViewByPosition(snapingViewPosition - 2).findViewById(R.id.text_view);
        View rightSnapingView = layoutManager.findViewByPosition(snapingViewPosition + 1).findViewById(R.id.text_view);
        View right2SnapingView = layoutManager.findViewByPosition(snapingViewPosition + 2).findViewById(R.id.text_view);
        float leftSnapingOffset = /*calculateOffset(recyclerView, leftSnapingView)*/1.0f;
        float left2SnapingOffset = /*calculateOffset(recyclerView, left2SnapingView)*/0.8f;
        float rightSnapingOffset = /*calculateOffset(recyclerView, rightSnapingView)*/1.0f;
        float right2SnapingOffset = /*calculateOffset(recyclerView, right2SnapingView)*/0.8f;
        float currentSnapingOffset = calculateOffset(recyclerView, snapingView);
        if (centerSanpingView != null) {
            centerSanpingView.setScaleX(1.2f);
            centerSanpingView.setScaleY(1.2f);
        }
        if (leftSnapingView != null) {
            leftSnapingView.setScaleX(leftSnapingOffset);
            leftSnapingView.setScaleY(leftSnapingOffset);
        }
        if (left2SnapingView != null) {
            left2SnapingView.setScaleX(left2SnapingOffset);
            left2SnapingView.setScaleY(left2SnapingOffset);
        }
        if (rightSnapingView != null) {
            rightSnapingView.setScaleX(rightSnapingOffset);
            rightSnapingView.setScaleY(rightSnapingOffset);
        }
        if (right2SnapingView != null) {
            right2SnapingView.setScaleX(right2SnapingOffset);
            right2SnapingView.setScaleY(right2SnapingOffset);
        }
        if (snapingView != null && currentSnapingOffset >= 1) {
            OnPageChangeListener listener = pageChangeListenerRef != null ? pageChangeListenerRef.get() : null;
            if (listener != null) {
                listener.onPageSelected(snapingViewPosition);
            }
        }
        Log.d(TAG, String.format("left: %f, right: %f, current: %f", leftSnapingOffset, rightSnapingOffset, currentSnapingOffset));
    }


    public int getCurrentPage() {
        View page = snapHelper.findSnapView(recyclerView.getLayoutManager());
        if (page == null) {
            return -1;
        }
        return recyclerView.getChildAdapterPosition(page);
    }

    public void attachToRecyclerView(final RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.addOnScrollListener(scrollListener);
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                pageScrolled();
            }
        });
    }

    /**
     * 通过计算{@code view}中间点与{@link RecyclerView}的中间点的距离，算出{@code view}的偏移量。
     *
     * @param view view
     * @return
     */
    private float calculateOffset(RecyclerView recyclerView, View view) {
        if (view == null) {
            return -1;
        }
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        boolean isVertical = layoutManager.canScrollVertically();
        int viewStart = isVertical ? view.getTop() : view.getLeft();
        int viewEnd = isVertical ? view.getBottom() : view.getRight();
        int centerX = isVertical ? recyclerView.getHeight() / 2 : recyclerView.getWidth() / 2;
        int childCenter = (viewStart + viewEnd) / 2;
        int distance = Math.abs(childCenter - centerX);
        if (distance > centerX) {
            return STAY_SCALE;
        }
        float offset = 1.f - (distance / (float) centerX);
        return (1.f - STAY_SCALE) * offset + STAY_SCALE;
    }


    public void detachFromRecyclerView(RecyclerView recyclerView) {
        if (recyclerView != null) {
            recyclerView.removeOnScrollListener(scrollListener);
        }
        this.recyclerView = null;
    }

    public interface OnPageChangeListener {
        void onPageSelected(int position);
    }
}
