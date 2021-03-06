package com.widget.library.progress

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.widget.RelativeLayout

open class CustomView(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {

    internal val disabledBackgroundColor = Color.parseColor("#E2E2E2")
    internal var beforeBackground: Int = 0

    // Indicate if user touched this view the last time
    var isLastTouch = false

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        if (enabled)
            setBackgroundColor(beforeBackground)
        else
            setBackgroundColor(disabledBackgroundColor)
        invalidate()
    }

    internal var animation = false

    override fun onAnimationStart() {
        super.onAnimationStart()
        animation = true
    }

    override fun onAnimationEnd() {
        super.onAnimationEnd()
        animation = false
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (animation)
            invalidate()
    }

    companion object {


        internal val MATERIALDESIGNXML = "http://schemas.android.com/apk/res-auto"
        internal val ANDROIDXML = "http://schemas.android.com/apk/res/android"
    }
}
