package com.cui.mdc.mdcBasic

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat

import com.cui.mdc.R


/**
 * @author cuiyang
 */
@SuppressLint("Registered")
open class SwipeActivity : AppCompatActivity() {

    private var swipeLayout: SwipeLayout? = null

    /**
     * 是否可以滑动关闭页面
     */
    var isSwipeEnabled = false

    /**
     * 是否可以在页面任意位置右滑关闭页面，如果是false则从左边滑才可以关闭。
     */
    var isSwipeAnyWhere = false

    private var swipeFinished = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        swipeLayout = SwipeLayout(this)
    }


    override fun onResume() {
        super.onResume()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        swipeLayout!!.replaceLayer(this)
    }

    override fun onBackPressed() {
        //alearday close will be true
        if (swipeFinished) {
            swipeLayout!!.cancelPotentialAnimation()
            super.onBackPressed()
            overridePendingTransition(0, 0)
        } else {
            swipeLayout!!.cancelPotentialAnimation()
            super.onBackPressed()
        }
    }


    internal inner class SwipeLayout : FrameLayout {

        //private View backgroundLayer;用来设置滑动时的背景色
        private var leftShadow: Drawable? = null

        var canSwipe = false
        /**
         * 超过了touchslop仍然没有达到没有条件，则忽略以后的动作
         */
        var ignoreSwipe = false
        lateinit var content: View
        lateinit var mActivity: Activity
        var sideWidthInDP = 16
        var sideWidth = 72
        var screenWidth = 1080F
        lateinit var tracker: VelocityTracker

        var downX: Float = 0.toFloat()
        var downY: Float = 0.toFloat()
        var lastX: Float = 0.toFloat()
        var currentX: Float = 0.toFloat()
        var currentY: Float = 0.toFloat()

        var touchSlopDP = 20
        var touchSlop = 60

        var hasIgnoreFirstMove: Boolean = false

        var animator: ObjectAnimator? = null

        var contentX: Float
            get() = content.x
            set(x) {
                val ix = x.toInt()
                content.x = ix.toFloat()
                invalidate()
            }

        private val duration = 200

        constructor(context: Context) : super(context) {}

        constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

        constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

        fun replaceLayer(activity: Activity) {
            leftShadow = ResourcesCompat.getDrawable(activity.resources,R.drawable.left_shadow,null)
            touchSlop = (touchSlopDP * activity.resources.displayMetrics.density).toInt()
            sideWidth = (sideWidthInDP * activity.resources.displayMetrics.density).toInt()
            mActivity = activity
            screenWidth = getScreenWidth(activity)
            isClickable = true
            val root = activity.window.decorView as ViewGroup
            content = root.getChildAt(0)
            val params = content.layoutParams
            val params2 = ViewGroup.LayoutParams(-1, -1)
            root.removeView(content)
            this.addView(content, params2)
            root.addView(this, params)
        }

        override fun drawChild(canvas: Canvas, child: View, drawingTime: Long): Boolean {
            val result = super.drawChild(canvas, child, drawingTime)
            val shadowWidth = leftShadow!!.intrinsicWidth
            val left = contentX.toInt() - shadowWidth
            leftShadow!!.setBounds(left, child.top, left + shadowWidth, child.bottom)
            leftShadow!!.draw(canvas)
            return result
        }

        override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
            if (isSwipeEnabled && !canSwipe && !ignoreSwipe) {
                if (isSwipeAnyWhere) {
                    when (ev.action) {
                        MotionEvent.ACTION_DOWN -> {
                            downX = ev.x
                            downY = ev.y
                            currentX = downX
                            currentY = downY
                            lastX = downX
                        }
                        MotionEvent.ACTION_MOVE -> {
                            val dx = ev.x - downX
                            val dy = ev.y - downY
                            if (dx * dx + dy * dy > touchSlop * touchSlop) {
                                if (dy == 0f || Math.abs(dx / dy) > 1) {
                                    downX = ev.x
                                    downY = ev.y
                                    currentX = downX
                                    currentY = downY
                                    lastX = downX
                                    canSwipe = true
                                    tracker = VelocityTracker.obtain()
                                    return true
                                } else {
                                    ignoreSwipe = true
                                }
                            }
                        }
                    }
                } else if (ev.action == MotionEvent.ACTION_DOWN && ev.x < sideWidth) {
                    canSwipe = true
                    tracker = VelocityTracker.obtain()
                    return true
                }
            }
            if (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_CANCEL) {
                ignoreSwipe = false
            }
            return super.dispatchTouchEvent(ev)
        }

        override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
            return canSwipe || super.onInterceptTouchEvent(ev)
        }

        override fun onTouchEvent(event: MotionEvent): Boolean {
            if (canSwipe) {
                tracker.addMovement(event)
                val action = event.action
                when (action) {
                    MotionEvent.ACTION_DOWN -> {
                        downX = event.x
                        downY = event.y
                        currentX = downX
                        currentY = downY
                        lastX = downX
                    }
                    MotionEvent.ACTION_MOVE -> {
                        currentX = event.x
                        currentY = event.y
                        var dx = currentX - lastX
                        if (dx != 0f && !hasIgnoreFirstMove) {
                            hasIgnoreFirstMove = true
                            dx = dx / dx
                        }
                        if (contentX + dx < 0) {
                            contentX = 0F
                        } else {
                            contentX = contentX + dx
                        }
                        lastX = currentX
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        tracker.computeCurrentVelocity(10000)
                        tracker.computeCurrentVelocity(1000, 20000f)
                        canSwipe = false
                        hasIgnoreFirstMove = false
                        val mv = screenWidth * 3
                        if (Math.abs(tracker.xVelocity) > mv) {
                            animateFromVelocity(tracker.xVelocity)
                        } else {
                            if (contentX > screenWidth / 3) {
                                animateFinish(false)
                            } else {
                                animateBack(false)
                            }
                        }
                        tracker.recycle()
                    }
                    else -> {
                    }
                }
            }
            return super.onTouchEvent(event)
        }

        fun cancelPotentialAnimation() {
            if (animator != null) {
                animator!!.removeAllListeners()
                animator!!.cancel()
            }
        }


        /**
         * 弹回，不关闭，因为left是0，所以setX和setTranslationX效果是一样的
         *
         * @param withVel 使用计算出来的时间
         */
        private fun animateBack(withVel: Boolean) {
            cancelPotentialAnimation()
            animator = ObjectAnimator.ofFloat(this, "contentX", contentX, 0F)
            var tmpDuration = if (withVel) (duration * contentX / screenWidth).toInt() else duration
            if (tmpDuration < 100) {
                tmpDuration = 100
            }
            animator!!.duration = tmpDuration.toLong()
            animator!!.interpolator = DecelerateInterpolator()
            animator!!.start()
        }

        private fun animateFinish(withVel: Boolean) {
            cancelPotentialAnimation()
            animator = ObjectAnimator.ofFloat(this, "contentX", contentX, screenWidth)
            var tmpDuration = if (withVel) (duration * (screenWidth - contentX) / screenWidth).toInt() else duration
            if (tmpDuration < 100) {
                tmpDuration = 100
            }
            animator!!.duration = tmpDuration.toLong()
            animator!!.interpolator = DecelerateInterpolator()
            animator!!.addListener(object : Animator.AnimatorListener {

                override fun onAnimationStart(animation: Animator) {

                }

                override fun onAnimationRepeat(animation: Animator) {

                }

                override fun onAnimationEnd(animation: Animator) {
                    if (!mActivity.isFinishing) {
                        swipeFinished = true
                        mActivity.onBackPressed()
                    }
                }

                override fun onAnimationCancel(animation: Animator) {

                }
            })
            animator!!.start()
        }

        private fun animateFromVelocity(v: Float) {
            if (v > 0) {
                if (contentX < screenWidth / 3 && v * duration / 1000 + contentX < screenWidth / 3) {
                    animateBack(false)
                } else {
                    animateFinish(true)
                }
            } else {
                if (contentX > screenWidth / 3 && v * duration / 1000 + contentX > screenWidth / 3) {
                    animateFinish(false)
                } else {
                    animateBack(true)
                }
            }

        }
    }

    companion object {

        fun getScreenWidth(context: Context): Float {
            val metrics = DisplayMetrics()
            val manager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            manager.defaultDisplay?.getMetrics(metrics)
            return metrics.widthPixels.toFloat()
        }
    }

}
