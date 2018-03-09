package com.widget.library.refresh.recyclerview

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.os.SystemClock
import android.support.v7.widget.SimpleItemAnimator
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import com.widget.library.R
import com.widget.library.refresh.familiarrecyclerview.FamiliarRecyclerView
import com.widget.library.refresh.header_smartrefresh.MaterialHeader
import com.widget.library.refresh.listener.OnCRefreshListener
import com.widget.library.refresh.listener.onCLoadMoreListener
import com.widget.library.progress.ProgressBarCircularIndeterminate


/**
 * Created by cuiyang on 16/6/5.
 */
class DDRecyclerViewLayout : FamiliarRecyclerView, LifecycleObserver {

    var page = 1
    private var refreshLayout: SmartRefreshLayout? = null
    val PAGE_SIZE = 10
    private var progress: ProgressBarCircularIndeterminate? = null
    private var topRefreshListener: OnCRefreshListener? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    private fun init() {
        (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false//不设置动画避免notifyItemRangeChanged更新会闪烁问题
//        (itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
    }

    /**
     * 初始化上拉和下拉刷新 默认到底自动加载
     * 不需要下拉或上拉直接传null
     */
    fun bindRefreshLayoutAndSetRefreshListener(listener: OnCRefreshListener? = null, listener1: onCLoadMoreListener? = null) {
        if (this.parent is SmartRefreshLayout) {
            this.refreshLayout = this.parent as SmartRefreshLayout
            //设置 Header 为 Material风格
            this.refreshLayout?.refreshHeader = MaterialHeader(refreshLayout?.context).setColorSchemeColors(this.context.resources.getColor(R.color.header_color))
            this.refreshLayout?.setEnableHeaderTranslationContent(false)
            val footer = BallPulseFooter(refreshLayout?.context!!).setSpinnerStyle(SpinnerStyle.Translate)
            this.refreshLayout?.refreshFooter = footer
            refreshLayout?.refreshFooter?.setPrimaryColors(this.context.resources.getColor(R.color.footer_color))
            this.refreshLayout?.setEnableFooterTranslationContent(true)
        }
        if (listener == null) {
            setEnableRefresh(false)
        } else {
            this.topRefreshListener = listener
            this.refreshLayout?.setOnRefreshListener(listener)
        }
        if (listener1 == null) {
            setEnableLoadeMore(false)
        } else {
            this.refreshLayout?.setOnLoadmoreListener(listener1)
            setEnableAutoLoadeMore(true)
        }
    }

    /**
     * 设置是否监听列表在滚动到底部时触发加载事件(默认启用)
     */
    fun setEnableAutoLoadeMore(boolean: Boolean) {
        refreshLayout?.isEnableAutoLoadmore = boolean
    }

    /**
     * 设置是否启用上啦加载更多（默认启用）
     */
    fun setEnableLoadeMore(boolean: Boolean) {
        refreshLayout?.isEnableLoadmore = boolean
    }

    /**
     * 是否启用下拉刷新（默认启用）
     */
    fun setEnableRefresh(boolean: Boolean) {
        refreshLayout?.isEnableRefresh = boolean
    }

    /**
     * 自动刷新
     */
    fun refreshBeginTop(delay: Int = 0) {
        refreshLayout?.autoRefresh(delay)
    }

    /**
     * 刷新 中间显示progress
     */
    fun refreshBeginCenter() {
        //为空时进行初始化
        progress ?: let {
            //防止recyclerview单独使用的情况
            if (this.parent is SmartRefreshLayout && this.parent.parent is FrameLayout) {
                progress = ((this.parent as SmartRefreshLayout).parent as FrameLayout).findViewById<ProgressBarCircularIndeterminate>(R.id.progress_refresh)
            }
        }
        progress?.visibility = View.VISIBLE
        topRefreshListener?.onRefresh()
        refreshLayout?.isEnableRefresh = false
    }


    fun refresComplete() {
        refreshLayout?.isEnableRefresh = true
        refreshLayout?.finishLoadmore(100)
        refreshLayout?.finishRefresh(100)
        progress ?: let {
            //防止recyclerview单独使用的情况
            if (this.parent is SmartRefreshLayout && this.parent.parent is FrameLayout) {
                progress = ((this.parent as SmartRefreshLayout).parent as FrameLayout).findViewById<ProgressBarCircularIndeterminate>(R.id.progress_refresh)
            }
        }
        progress?.visibility = View.GONE
//        progress?.let {
//            it.visibility = View.GONE
//        }
    }

    fun getRefreshLayouts(): SmartRefreshLayout {
        return refreshLayout!!
    }

    /**
     * 设置为空提示

     * @return
     */
    fun setSimpleEmpty(isKeeyHeaderFooter: Boolean) {
        setEmptyView(LayoutInflater.from(this.context).inflate(R.layout.list_empty, this, false), isKeeyHeaderFooter)
    }

    fun setEmptyViewSetTxt(txtResrouceId: Int = 0, txtResrouce: String = "", isKeeyHeaderFooter: Boolean = true): View {
        val v = LayoutInflater.from(this.context).inflate(R.layout.list_empty, this, false)
        val txt = v.findViewById<TextView>(R.id.tv_empty) as TextView
        txt.text = if (!TextUtils.isEmpty(txtResrouce)) txtResrouce else this.context.resources.getString(txtResrouceId)
        setEmptyView(v, isKeeyHeaderFooter)
        return v
    }

    /**
     * 当是图片列表需要标注避免Recyclerview使用Glide加载图片时惯性运动在消毁页面时依然还在加载图片
     * 页面关闭recyclerview不再滑动 否则有可能Glide会出现You cannot start a load for a destroyed activity
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun stopFlingRecyclerview() {
        this.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(), MotionEvent.ACTION_CANCEL, 0F, 0F, 0))
    }

    /**
     * 下拉刷新传入true
     * 上拉加载传入false

     * @param isRefresh
     * *
     * @return
     */
    fun getPage(isRefresh: Boolean): Int {
        if (isRefresh) {
            page = 1
        } else {
            page++
        }
        return page
    }

}
