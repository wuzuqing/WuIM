package com.wuzuqing.component_base.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;


/**
 * Created by long on 2017/1/12.
 * 可滚动超出上拉的 ScrollView
 */
public class PullScrollView extends NestedScrollView {

    private View mFootView;
    private OnPullListener mPullListener;
    private boolean mIsPullStatus = false;
    private float mLastY;
    private int mPullCriticalDistance;

    public PullScrollView(Context context) {
        this(context, null);
    }

    public PullScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPullCriticalDistance = (int) TypedValue.applyDimension(1,70f,getResources().getDisplayMetrics());
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (t >= (getChildAt(0).getMeasuredHeight() - getHeight()) && mPullListener != null) {
            mPullListener.isDoPull();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (MotionEventCompat.getActionMasked(ev)) {
            case MotionEvent.ACTION_MOVE:
                if (!mIsPullStatus) {
                    if (getScrollY() >= (getChildAt(0).getMeasuredHeight() - getHeight()) || getChildAt(0).getMeasuredHeight() < getHeight()) {
                        if (mPullListener != null && mPullListener.isDoPull()) {
                            mIsPullStatus = true;
                            mLastY = ev.getY();
                        }
                    }
                } else if (mLastY < ev.getY()) {
                    mIsPullStatus = false;
                    _pullFootView(0);
                } else {
                    float offsetY = mLastY - ev.getY();
                    _pullFootView(offsetY);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mIsPullStatus) {
                    if (mFootView.getHeight() > mPullCriticalDistance && mPullListener != null) {
                        if (!mPullListener.handlePull()) {
                            doClipViewHeight(mFootView, mFootView.getHeight(), 0, 200);
                        }
                    } else {
                        doClipViewHeight(mFootView, mFootView.getHeight(), 0, 200);
                    }
                    mIsPullStatus = false;
                }
                break;
        }
        return super.onTouchEvent(ev);
    }


    /**
     * 裁剪视图宽度
     * @param view
     * @param srcHeight
     * @param endHeight
     * @param duration
     */
    public static void doClipViewHeight(final View view, int srcHeight, int endHeight, int duration) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(srcHeight, endHeight).setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int width = (int) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = width;
                view.setLayoutParams(layoutParams);
            }
        });
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.start();
    }
    public void setFootView(View footView) {
        mFootView = footView;
    }

    public void setPullListener(OnPullListener pullListener) {
        mPullListener = pullListener;
    }

    private void _pullFootView(float offsetY) {
        if (mFootView != null) {
            ViewGroup.LayoutParams layoutParams = mFootView.getLayoutParams();
            layoutParams.height = (int) (offsetY * 1 / 2);
            mFootView.setLayoutParams(layoutParams);
        }
    }

    public interface OnPullListener {
        boolean isDoPull();
        boolean handlePull();
    }
}
