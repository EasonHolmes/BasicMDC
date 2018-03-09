package com.widget.library.anim.coordinator_anim.lib_behavior;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by cuiyang on 2017/4/28.
 *  top跟随
 */

public class TopScrollBehavior extends FollowScrollBehavior {
    public TopScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean isTopView() {
        return true;
    }
}
