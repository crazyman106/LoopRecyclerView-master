package com.angcyo.looprecyclerview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * @author Administrator
 */
public class ScalableCardHelper {
    private static final float STAY_SCALE = 0.5f;
    private String TAG = "ScalableCardHelper";
    private LinearSnapHelper snapHelper = new LinearSnapHelper();
    private RecyclerView recyclerView;
    private WeakReference<OnPageChangeListener> pageChangeListenerRef;
    private Context context;

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    pageScrolled();
                }
            });
        }
    };

    public ScalableCardHelper(OnPageChangeListener pageChangeListener) {
        if (pageChangeListener != null) {
            this.pageChangeListenerRef = new WeakReference<>(pageChangeListener);
        }
    }


    private boolean isFirst = true;
    public ScalableCardHelper() {
        this(null);
    }

    private void pageScrolled() {
        if (recyclerView == null || recyclerView.getChildCount() == 0) {
            return;
        }
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        final View snapingView = snapHelper.findSnapView(layoutManager);
        final int snapingViewPosition = recyclerView.getChildAdapterPosition(snapingView);
        final View leftSnapingView = layoutManager.findViewByPosition(snapingViewPosition - 1);
        final View left2SnapingView = layoutManager.findViewByPosition(snapingViewPosition - 2);
        final View rightSnapingView = layoutManager.findViewByPosition(snapingViewPosition + 1);
        final View right2SnapingView = layoutManager.findViewByPosition(snapingViewPosition + 2);
        float leftSnapingOffset = calculateOffset(recyclerView, leftSnapingView);
        float left2SnapingOffset = calculateOffset(recyclerView, left2SnapingView);
        float rightSnapingOffset = calculateOffset(recyclerView, rightSnapingView);
        float right2SnapingOffset = calculateOffset(recyclerView, right2SnapingView);
        float currentSnapingOffset = calculateOffset(recyclerView, snapingView);
        if (!isFirst){
            if (snapingView != null) {
                snapingView.findViewById(R.id.text_view).setScaleX(currentSnapingOffset);
                snapingView.findViewById(R.id.text_view).setScaleY(currentSnapingOffset);
                Log.e("offset",currentSnapingOffset+"");
                ((TextView) snapingView.findViewById(R.id.txt)).setTextSize((calculateOffset(recyclerView, snapingView) + 0.1f) * 13);
                snapingView.findViewById(R.id.item_bg).setBackground(new ColorDrawable(Color.parseColor("#00000000")));
            }
            Log.e("scale",right2SnapingView.findViewById(R.id.text_view).getScaleX()+"");
            if (leftSnapingView != null) {
                leftSnapingView.findViewById(R.id.text_view).setScaleX(leftSnapingOffset);
                leftSnapingView.findViewById(R.id.text_view).setScaleY(leftSnapingOffset);
                ((TextView) leftSnapingView.findViewById(R.id.txt)).setTextSize(13);
                leftSnapingView.findViewById(R.id.item_bg).setBackground(new ColorDrawable(Color.parseColor("#00000000")));
            }
            if (left2SnapingView != null) {
                left2SnapingView.findViewById(R.id.text_view).setScaleX(left2SnapingOffset);
                left2SnapingView.findViewById(R.id.text_view).setScaleY(left2SnapingOffset);
                ((TextView) left2SnapingView.findViewById(R.id.txt)).setTextSize(13);
                left2SnapingView.findViewById(R.id.item_bg).setBackground(getLeftDrawable(0.5f));
            }
            if (rightSnapingView != null) {
                rightSnapingView.findViewById(R.id.text_view).setScaleX(rightSnapingOffset);
                rightSnapingView.findViewById(R.id.text_view).setScaleY(rightSnapingOffset);
                ((TextView) rightSnapingView.findViewById(R.id.txt)).setTextSize(13);
                rightSnapingView.findViewById(R.id.item_bg).setBackground(new ColorDrawable(Color.parseColor("#00000000")));
            }
            if (right2SnapingView != null) {
                right2SnapingView.findViewById(R.id.text_view).setScaleX(right2SnapingOffset);
                right2SnapingView.findViewById(R.id.text_view).setScaleY(right2SnapingOffset);
                ((TextView) right2SnapingView.findViewById(R.id.txt)).setTextSize(13);
                right2SnapingView.findViewById(R.id.item_bg).setBackground(getRightDrawable(0.5f));
            }
            if (snapingView != null && currentSnapingOffset >= 1) {
                OnPageChangeListener listener = pageChangeListenerRef != null ? pageChangeListenerRef.get() : null;
                if (listener != null) {
                    listener.onPageSelected(snapingViewPosition);
                }
            }
            snapingView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(snapingViewPosition, 0);
                }
            });
            leftSnapingView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(snapingViewPosition - 1, -1);

                }
            });
            left2SnapingView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(snapingViewPosition - 2, -2);

                }
            });
            rightSnapingView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(snapingViewPosition + 1, 1);
                }
            });
            right2SnapingView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(snapingViewPosition + 2, 2);
                }
            });
        }else {
            if (snapingView != null) {
                ((TextView) snapingView.findViewById(R.id.txt)).setTextSize((calculateOffset(recyclerView, snapingView) + 0.1f) * 13);
                snapingView.findViewById(R.id.text_view).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ScaleAnimation animation = new ScaleAnimation(1.0f,1.0f,1.f,1.0f,
                                Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                        animation.setDuration(1000);
                        animation.setFillAfter(true);
                        animation.setFillEnabled(true);
                        snapingView.findViewById(R.id.text_view).startAnimation(animation);
                        isFirst = false;
//                        pageScrolled();
                    }
                },1500);
            }
            if (leftSnapingView != null) {
                ((TextView) leftSnapingView.findViewById(R.id.txt)).setTextSize(13);
                leftSnapingView.findViewById(R.id.text_view).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AnimatorSet animatorSet = new AnimatorSet();//组合动画
                        ValueAnimator scaleX  = ObjectAnimator.ofFloat(leftSnapingView.findViewById(R.id.text_view),"scaleX", 1f, 0.8f);
                        ValueAnimator scaleY  = ObjectAnimator.ofFloat(leftSnapingView.findViewById(R.id.text_view),"scaleY", 1f, 0.8f);
                        animatorSet.setDuration(1000);
                        animatorSet.play(scaleX).with(scaleY);//两个动画同时开始
                        animatorSet.start();
                        isFirst = false;
                    }
                },1500);
            }
            if (left2SnapingView != null) {
                ((TextView) left2SnapingView.findViewById(R.id.txt)).setTextSize(13);
                left2SnapingView.findViewById(R.id.text_view).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AnimatorSet animatorSet = new AnimatorSet();//组合动画
                        ValueAnimator scaleX  = ObjectAnimator.ofFloat(left2SnapingView.findViewById(R.id.text_view),"scaleX", 1f, 0.7f);
                        ValueAnimator scaleY  = ObjectAnimator.ofFloat(left2SnapingView.findViewById(R.id.text_view),"scaleY", 1f, 0.7f);
                        animatorSet.setDuration(1000);
                        animatorSet.play(scaleX).with(scaleY);//两个动画同时开始
                        animatorSet.start();
                        isFirst = false;
                    }
                },1500);
            }
            if (rightSnapingView != null) {
                ((TextView) rightSnapingView.findViewById(R.id.txt)).setTextSize(13);
                rightSnapingView.findViewById(R.id.text_view).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AnimatorSet animatorSet = new AnimatorSet();//组合动画
                        ValueAnimator scaleX  = ObjectAnimator.ofFloat(rightSnapingView.findViewById(R.id.text_view),"scaleX", 1f, 0.8f);
                        ValueAnimator scaleY  = ObjectAnimator.ofFloat(rightSnapingView.findViewById(R.id.text_view),"scaleY", 1f, 0.8f);
                        animatorSet.setDuration(1000);
                        animatorSet.play(scaleX).with(scaleY);//两个动画同时开始
                        animatorSet.start();
                        isFirst = false;
                    }
                },1500);
            }
            if (right2SnapingView != null) {
                ((TextView) right2SnapingView.findViewById(R.id.txt)).setTextSize(13);
                right2SnapingView.findViewById(R.id.text_view).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AnimatorSet animatorSet = new AnimatorSet();//组合动画
                        ValueAnimator scaleX  = ObjectAnimator.ofFloat(right2SnapingView.findViewById(R.id.text_view),"scaleX", 1f, 0.7f);
                        ValueAnimator scaleY  = ObjectAnimator.ofFloat(right2SnapingView.findViewById(R.id.text_view),"scaleY", 1f, 0.7f);
                        animatorSet.setDuration(1000);
                        animatorSet.play(scaleX).with(scaleY);//两个动画同时开始
                        animatorSet.start();
                        isFirst = false;
                    }
                },1500);
            }
        }
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
//        recyclerView.post(new Runnable() {
//            @Override
//            public void run() {
//                pageScrolled();
//            }
//        });
    }

    private OnClickListener listener;

    public interface OnClickListener {
        void onClick(int position, int offset);
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
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
            return 1.0f;
        }
        float offset = 1.f - (distance / (float) centerX);
        float result = (1.f - STAY_SCALE) * offset + STAY_SCALE;
        if (result < 0.7f) {
            return 0.7f;
        }
        return result;
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

    private GradientDrawable getLeftDrawable(float offset) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
        drawable.setColors(new int[]{Color.parseColor("#77ffffff"), Color.parseColor("#00000000")});
        return drawable;
    }

    private GradientDrawable getRightDrawable(float offset) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
        drawable.setColors(new int[]{Color.parseColor("#00000000"), Color.parseColor("#77ffffff")});
        return drawable;
    }
}
