package com.widget.library.utils

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

/**
 * Created by cuiyang on 2018/3/9.
 */

object Utils {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * Convert Dp to Pixel
     */
    fun dpToPx(dp: Float, resources: Resources): Int {
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
        return px.toInt()
    }
}
