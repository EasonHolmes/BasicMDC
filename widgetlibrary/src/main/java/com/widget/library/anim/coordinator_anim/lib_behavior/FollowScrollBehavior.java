package com.widget.library.anim.coordinator_anim.lib_behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.widget.library.anim.coordinator_anim.CommonBehavior;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

/**
 * Created by cuiyang on 2017/4/28.
 * 跟随移动
 */

public abstract class FollowScrollBehavior extends CommonBehavior {
    int offsetTotal = 0;
    boolean scrolling = false;

    public FollowScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public abstract boolean isTopView();

    //判断垂直滑动
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        offset(child, dyConsumed);
    }

    public void offset(View child, int dy) {
        int old = offsetTotal;
        int top = offsetTotal - dy;
        top = Math.max(top, -child.getHeight());
        top = Math.min(top, 0);
        offsetTotal = top;
        if (old == offsetTotal) {
            scrolling = false;
            return;
        }
        int delta = offsetTotal - old;
        if (isTopView())
            child.offsetTopAndBottom(delta);
        else
            child.offsetTopAndBottom(-delta);
        scrolling = true;
    }
}
