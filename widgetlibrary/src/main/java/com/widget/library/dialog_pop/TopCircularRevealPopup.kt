package com.widget.library.dialog_pop

import android.animation.*
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.RelativeLayout

import com.widget.library.R
import com.widget.library.refresh.listener.OnItemClickListener
import com.widget.library.refresh.recyclerview.DDRecyclerViewLayout

/**
 * 上部弹出popupWindow
 */
//DropView依附的view
class TopCircularRevealPopup(private val DropView: View) : PopupWindow(DropView.context), View.OnClickListener {

    val recyclerview: DDRecyclerViewLayout
    lateinit var animationLayout: View
    private val imgBackground: ImageView? = null
    private val root_layout: RelativeLayout
    private val parent_layout: FrameLayout
    private var root_layoutH: Int = 0
    private var root_layouhW: Int = 0
    private val adapter: RecyclerView.Adapter<*>? = null

    init {

        val view = View.inflate(DropView.context, R.layout.top_circular_dialog, null)

        initParam(view)

        recyclerview = view.findViewById<View>(R.id.pop_mRecyclerview) as DDRecyclerViewLayout
        parent_layout = view.findViewById<View>(R.id.parent_layout) as FrameLayout
        animationLayout = view.findViewById<View>(R.id.animationLayout)
        root_layout = view.findViewById<View>(R.id.root_layout) as RelativeLayout
        recyclerview.layoutManager = LinearLayoutManager(DropView.context)

        root_layout.setOnClickListener(this)
    }

    fun setDirection(direction: Int) {
        val params = animationLayout.layoutParams as FrameLayout.LayoutParams
        when (direction) {
            0 -> {
                params.gravity = Gravity.LEFT or Gravity.TOP
                animationLayout.layoutParams = params
            }
            1 -> {
                params.gravity = Gravity.CENTER or Gravity.TOP
                animationLayout.layoutParams = params
            }
            2 -> {
                params.gravity = Gravity.RIGHT or Gravity.TOP
                animationLayout.layoutParams = params
            }
            else -> {
            }
        }
    }

    fun setPopOnItemClickListener(listener: OnItemClickListener) {
        recyclerview.setOnItemClickListener(listener)
    }

    fun setAdapter(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        recyclerview.adapter = adapter
    }

    private fun initRootLayoutHW() {
        //等待绘图完成再进行动画这样才能取到控件的宽高 oncreate中只是绘制
        root_layout.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                root_layout.viewTreeObserver.removeOnPreDrawListener(this)
                root_layoutH = root_layout.height
                root_layouhW = root_layout.width
                startCircularRevealAnima()
                return true
            }
        })
    }

    private fun startCircularRevealAnima() {
        /**
         * 如果已经拿到rootlayout的宽高就直接开始动画,否则就先去拿值
         */
        if (root_layoutH == 0 || root_layouhW == 0) {
            initRootLayoutHW()
        } else {
            recyclerview.alpha = 0f
            animationLayout.scaleX = 0f
            animationLayout.scaleY = 0f
            val objectAnimatorScaleY = ObjectAnimator.ofFloat(animationLayout, "scaleY", 0f, root_layoutH * 2.toFloat())
            val objectAnimatorScaleX = ObjectAnimator.ofFloat(animationLayout, "scaleX", 0f, root_layouhW * 2.toFloat())
            val set = AnimatorSet()
            set.setDuration(500).startDelay = 50
            set.interpolator = AccelerateInterpolator()
            set.playTogether(objectAnimatorScaleX, objectAnimatorScaleY)
            set.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    val valueAnimator = recyclerview.animate()
                    valueAnimator.alpha(1f).setDuration(100).interpolator = DecelerateInterpolator(2f)
                    valueAnimator.setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            super.onAnimationEnd(animation)
                            animationLayout.scaleX = 0f
                            animationLayout.scaleY = 0f
                        }
                    })
                    valueAnimator.start()
                }
            })
            set.start()
        }
    }

    private fun initParam(view: View) {
        //设置popupwindow属性
        //        setFocusable(true);
        //        setOutsideTouchable(true);
        setBackgroundDrawable(BitmapDrawable())
        width = LinearLayout.LayoutParams.MATCH_PARENT
        height = LinearLayout.LayoutParams.MATCH_PARENT
        contentView = view
    }

    fun setNewData() {

    }

    // showPop
    fun showPop() {
        if (!isShowing) {
            showAsDropDown(DropView, 0, 0)
            startCircularRevealAnima()
        }
    }

    /**
     * 重写showAsDropDown解决7.0以上位置错误问题
     */
    override fun showAsDropDown(anchor: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val rect = Rect()
            anchor.getGlobalVisibleRect(rect)
            val h = anchor.resources.displayMetrics.heightPixels - rect.bottom
            height = h
        }
        super.showAsDropDown(anchor)
    }

    /**
     * 重写showAsDropDown解决7.0以上位置错误问题
     */
    override fun showAsDropDown(anchor: View, xoff: Int, yoff: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val rect = Rect()
            anchor.getGlobalVisibleRect(rect)
            val h = anchor.resources.displayMetrics.heightPixels - rect.bottom
            height = h
        }
        super.showAsDropDown(anchor, xoff, yoff)
    }

    fun dismissPop() {
        if (isShowing) {
            dismiss()
        }
    }

    override fun onClick(v: View) {
        val i = v.id
        if (i == R.id.root_layout) {
            dismissPop()
        }
    }

}
