package com.widget.library.anim.coordinator_anim.lib_behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;

import com.widget.library.anim.CommonAnim;
import com.widget.library.anim.coordinator_anim.CommonBehavior;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;


/**
 * Created by cuiyang on 2017/4/28.
 */

public class AnimtionBehavior extends CommonBehavior {
    protected CommonAnim mCommonAnim;
    protected boolean isHide;
    protected int mTotalScrollY;
    protected boolean isInit = true; //防止new Anim导致的parent 和child坐标变化

    private int mDuration = 400;
    private Interpolator mInterpolator = new LinearOutSlowInInterpolator();
    protected int minScrollY = 5;//触发滑动动画最小距离
    protected int scrollYDistance = 40;//设置最小滑动距离

    public AnimtionBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommonBehavior setDuration(int duration) {
        mDuration = duration;
        return this;
    }

    public CommonBehavior setInterpolator(Interpolator interpolator) {
        mInterpolator = interpolator;
        return this;
    }

    public CommonBehavior setMinScrollY(int minScrollY) {
        this.minScrollY = minScrollY;
        return this;
    }

    public CommonBehavior setScrollYDistance(int scrollYDistance) {
        this.scrollYDistance = scrollYDistance;
        return this;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        if (mCommonAnim != null) {
            mCommonAnim.setDuration(mDuration);
            mCommonAnim.setInterpolator(mInterpolator);
        }
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        if (super.canScroll) {
            mTotalScrollY += dyConsumed;
            if (Math.abs(dyConsumed) > minScrollY || Math.abs(mTotalScrollY) > scrollYDistance) {
                if (dyConsumed < 0) {
                    if (isHide) {
                        mCommonAnim.show();
                        isHide = false;
                    }
                } else if (dyConsumed > 0) {
                    if (!isHide) {
                        mCommonAnim.hide();
                        isHide = true;
                    }
                }
                mTotalScrollY = 0;
            }
        }
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return false;
    }

    public void show() {
        if (mCommonAnim != null) {
            isHide = false;
            mCommonAnim.show();
        }
    }

    public void hide() {
        if (mCommonAnim != null) {
            isHide = true;
            mCommonAnim.hide();
        }
    }


}
