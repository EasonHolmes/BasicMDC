package com.widget.library.anim.coordinator_anim.lib_behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.widget.library.anim.coordinator_anim.CommonBehavior;
import com.widget.library.anim.coordinator_anim.lib_anim.LFabVerticalBehaviorAnim;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

/**
 * 直接写在需要根据可滑动view来滑动的view上就好
 */
public class LFabVerticalBehavior extends AnimtionBehavior {

    public LFabVerticalBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        /*if (isInit) {
            mCommonAnim = new LFabVerticalBehaviorAnim(parent, child);
            isInit = false;
        }*/
        return super.onDependentViewChanged(parent, child, dependency);
    }

    //判断垂直滑动
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child,
                                       View directTargetChild, View target, int nestedScrollAxes) {
        if (isInit) {
            mCommonAnim = new LFabVerticalBehaviorAnim(coordinatorLayout, child);
            isInit = false;
        }
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }
}