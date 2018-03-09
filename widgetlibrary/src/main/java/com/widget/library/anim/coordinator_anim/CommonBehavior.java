package com.widget.library.anim.coordinator_anim;


import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;


@SuppressWarnings("unused")
public class CommonBehavior extends CoordinatorLayout.Behavior<View> {
    protected boolean canScroll = true;


    //这个构造方法必须 写因为Coordinatorlayout会根据这个反射
    public CommonBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    /*//判断垂直滑动
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }*/


    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }

    /**
     * 用来判断child是否有一个对应的dependency，如果有就返回true，默认情况下返回的是false
     * @param parent
     * @param child
     * @param dependency
     * @return
     */
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return true;
    }

    /**
     * 触发滑动嵌套滚动之前调用的方法
     *
     * @param coordinatorLayout coordinatorLayout父布局
     * @param child             使用Behavior的子View
     * @param target            触发滑动嵌套的View(实现NestedScrollingChild接口)
     * @param dx                滑动的X轴距离
     * @param dy                滑动的Y轴距离
     * @param consumed          父布局消费的滑动距离，consumed[0]和consumed[1]代表X和Y方向父布局消费的距离，默认为0
     */
    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
    }


    /**
     * 滑动嵌套滚动时触发的方法
     *
     * @param coordinatorLayout coordinatorLayout父布局
     * @param child             使用Behavior的子View
     * @param target            触发滑动嵌套的View
     * @param dxConsumed        TargetView消费的X轴距离
     * @param dyConsumed        TargetView消费的Y轴距离
     * @param dxUnconsumed      未被TargetView消费的X轴距离
     * @param dyUnconsumed      未被TargetView消费的Y轴距离(如RecyclerView已经到达顶部或底部，而用户继续滑动，此时dyUnconsumed的值不为0，可处理一些越界事件)
     */
    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, final View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);
    }

    public static CommonBehavior from(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (!(params instanceof CoordinatorLayout.LayoutParams)) {
            throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
        }
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) params).getBehavior();
        if (!(behavior instanceof CommonBehavior)) {
            throw new IllegalArgumentException("The view's behavior isn't an instance of CommonBehavior. Try to check the [app:layout_behavior]");
        }
        return (CommonBehavior) behavior;
    }

}
