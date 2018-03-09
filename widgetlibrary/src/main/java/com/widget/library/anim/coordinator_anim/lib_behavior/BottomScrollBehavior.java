package com.widget.library.anim.coordinator_anim.lib_behavior;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by cuiyang on 2017/4/28.
 * bottom跟随
 * 直接写在需要根据可滑动view来滑动的view上就好
 */

public class BottomScrollBehavior extends FollowScrollBehavior {
    public BottomScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean isTopView() {
        return false;
    }


}
