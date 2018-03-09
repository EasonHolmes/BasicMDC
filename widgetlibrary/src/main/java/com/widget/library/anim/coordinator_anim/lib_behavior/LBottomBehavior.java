package com.widget.library.anim.coordinator_anim.lib_behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.widget.library.anim.coordinator_anim.lib_anim.LBottomBehaviorAnim;

/**
 * 直接写在需要根据可滑动view来滑动的view上就好
 */
public class LBottomBehavior extends AnimtionBehavior {

    public LBottomBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //判断垂直滑动
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child,
                                       View directTargetChild, View target, int nestedScrollAxes) {
        if (isInit) {
            mCommonAnim = new LBottomBehaviorAnim(child);
            isInit = false;
        }
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }
}