package com.widget.library.refresh.recyclerview

import android.annotation.SuppressLint
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.os.SystemClock
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.SimpleItemAnimator
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.widget.library.R
import com.widget.library.refresh.familiarrecyclerview.FamiliarRecyclerView
import com.widget.library.refresh.listener.OnCRefreshListener
import com.widget.library.refresh.listener.onCLoadMoreListener


/**
 * Created by cuiyang on 16/6/5.
 */
open class DDRecyclerViewLayout : FamiliarRecyclerView {

    private lateinit var refreshLayout: SmartRefreshLayout
    private var topRefreshListener: OnCRefreshListener? = null
    private var refreshLoadMoreListener: onCLoadMoreListener? = null
    private var progress: ProgressBar? = null
    private var footerEmptyViewTip = false
    var page = 1
    private val footerEmptyView by lazy(LazyThreadSafetyMode.NONE) {
        LayoutInflater.from(this.context).inflate(R.layout.empty_footer, this, false)
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    private fun init() {
        //不设置动画避免notifyItemRangeChanged更新会闪烁问题
        (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    }

    /**
     * 初始化上拉和下拉刷新 默认到底自动加载
     * 不需要下拉或上拉直接传null
     */
    open fun bindRefreshLayoutAndSetRefreshListener(listener: OnCRefreshListener? = null, listener1: onCLoadMoreListener? = null) {
        if (this.parent is SmartRefreshLayout) {
            this.refreshLayout = this.parent as SmartRefreshLayout
            setDefaultHeader(this.refreshLayout)
            setDefaultFooter(this.refreshLayout)
        }
        if (listener == null) {
            setEnableRefresh(false)
        } else {
            this.topRefreshListener = listener
            this.refreshLayout.setOnRefreshListener(listener)
        }
        if (listener1 == null) {
            setEnableLoadeMore(false)
        } else {
            this.refreshLoadMoreListener = listener1
            this.refreshLayout.setOnLoadMoreListener(listener1)
            setEnableAutoLoadeMore(true)
        }
    }

    /**
     * 用来判断是否有设置或需要下拉刷新
     */
    fun getRefreshListener(): OnCRefreshListener? {
        return topRefreshListener
    }

    /**
     * 用来判断是否有设置或需要上拉加载，
     * 用isEnableLoadMore方法不准确因为无数据就会变成false再下拉刷新时 上拉加载就不能用了。而在下拉刷新强制开启又有可能会让不需要上拉加载的页面开启该功能
     */
    fun getLoadmoreListener(): onCLoadMoreListener? {
        return refreshLoadMoreListener
    }

    /**
    设置 Header 为 Material风格
     */
    open fun setDefaultHeader(refreshLayout: SmartRefreshLayout) {
        refreshLayout.setEnableOverScrollBounce(false)
        refreshLayout.setEnableScrollContentWhenLoaded(false)
        refreshLayout.setRefreshHeader(com.scwang.smartrefresh.header.MaterialHeader(refreshLayout.context).setColorSchemeColors(ResourcesCompat.getColor(resources, R.color.header_color, null)))
        refreshLayout.setEnableHeaderTranslationContent(false)
//      refreshLayout.setEnableLoadMoreWhenContentNotFull(false)//是否在列表不满一页时候开启上拉加载功能
//      refreshLayout.refreshHeader = MaterialHeader(refreshLayout.context).setColorSchemeColors(ResourcesCompat.getColor(resources, R.color.header_color, null))

    }

    /**
    设置 Header 为 ClassicsFooter风格
     */
    @SuppressLint("RestrictedApi")
    open fun setDefaultFooter(refreshLayout: SmartRefreshLayout) {
        val footer = ClassicsFooter(refreshLayout.context!!)
        footer.spinnerStyle = SpinnerStyle.Translate
        this.refreshLayout.setRefreshFooter(footer)
        //设置是否启用内容视图拖动效果
        this.refreshLayout.setEnableFooterTranslationContent(true)
    }

    /**
     * 设置是否监听列表在滚动到底部时触发加载事件(默认启用)
     */
    fun setEnableAutoLoadeMore(boolean: Boolean) {
        refreshLayout.setEnableAutoLoadMore(boolean)
    }

    /**
     * 设置是否显示到底"已经没有内容了"
     */
    fun setEnableFooterEmptyViewTip(boolean: Boolean) {
        footerEmptyViewTip = boolean
    }

    /**
     * 设置是否启用上啦加载更多（默认启用）
     * 如果列表要加footerview 请注意添加顺序
     */
    open fun setEnableLoadeMore(boolean: Boolean) {
        refreshLayout.setEnableLoadMore(boolean)
        if (footerEmptyViewTip && refreshLoadMoreListener != null) {
            if (boolean) {
                this.removeFooterView(footerEmptyView)
            } else {
                this.addFooterView(footerEmptyView)
            }
        }
    }

    /**
     * 是否启用下拉刷新（默认启用）
     */
    fun setEnableRefresh(boolean: Boolean) {
        refreshLayout.setEnableRefresh(boolean)
    }

    /**
     * 自动刷新
     */
    fun refreshBeginTop(delay: Int = 0) {
        refreshLayout.autoRefresh(delay)
    }

    /**
     * 刷新 中间显示progress
     */
    open fun refreshBeginCenter() {
//        为空时进行初始化
        if (progress == null && (this.parent is SmartRefreshLayout && this.parent.parent is FrameLayout)) {
            progress = (this.parent.parent as FrameLayout).findViewById(R.id.progress_refresh)
        }
        progress?.visibility = View.VISIBLE
        topRefreshListener?.onRefresh()
    }


    open fun refresComplete(delay: Int = -1) {
        refreshLayout.finishLoadMore(if (delay == -1) 50 else delay)
        refreshLayout.finishRefresh(if (delay == -1) 50 else delay)
        //  防止recyclerview单独使用的情况
        progress?.let {
            it.visibility = View.GONE
        }
    }

    fun getRefreshLayouts(): SmartRefreshLayout {
        return refreshLayout
    }

    /**
     * 设置为空提示

     * @return
     */
    open fun setSimpleEmpty(isKeeyHeaderFooter: Boolean) {
        setEmptyView(LayoutInflater.from(this.context).inflate(R.layout.list_empty, this, false), isKeeyHeaderFooter)
    }

    open fun setEmptyViewSetTxt(txtResrouceId: Int = 0, txtResrouce: String = "", isKeeyHeaderFooter: Boolean = true): View {
        val v = LayoutInflater.from(this.context).inflate(R.layout.list_empty, this, false)
        val txt = v.findViewById<TextView>(R.id.tv_empty) as TextView
        txt.text = if (!TextUtils.isEmpty(txtResrouce)) txtResrouce else this.context.resources.getString(txtResrouceId)
        setEmptyView(v, isKeeyHeaderFooter)
        return v
    }

//    /**
//     * 当是图片列表需要标注避免Recyclerview使用Glide加载图片时惯性运动在消毁页面时依然还在加载图片
//     * 页面关闭recyclerview不再滑动 否则有可能Glide会出现You cannot start a load for a destroyed activity
//     */
//    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
//    private fun stopFlingRecyclerview() {
//        this.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(),
//                SystemClock.uptimeMillis(), MotionEvent.ACTION_CANCEL, 0F, 0F, 0))
//    }

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
