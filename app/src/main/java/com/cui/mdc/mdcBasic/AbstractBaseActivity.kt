package com.cui.mdc.mdcBasic

import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.OnRebindCallback
import android.databinding.ViewDataBinding
import android.os.*
import android.support.transition.TransitionManager
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.cui.mdc.R
import com.cui.mdc.mdcHelper.ActivityHelper

import com.tbruyelle.rxpermissions2.RxPermissions
import com.utils.library.utils.StatusBarUtil
import com.utils.library.utils.isNotEmptyStr
import com.widget.library.progress.ProgressBarCircularIndeterminate
import com.widget.library.refresh.recyclerview.DDRecyclerViewLayout
import io.reactivex.disposables.Disposable

abstract class AbstractBaseActivity<B : ViewDataBinding, T : BaseContract.BasePresenter> : AbstractBaseSwipeActivity(),
        View.OnClickListener, BaseContract.BaseView {
    /**
     * 传入参数, String 类型， startActivity 启动带入下一个界面的父的启动类的名称
     */
    private val EXTRA_PARENT_ACTIVITY_CLASS_NAME = "Base_Extra_ParentActivityClassName"
    protected val PAGESIZE: Int = 20

    protected val mHandler = Handler(Looper.getMainLooper()) { message -> false }
    protected val lastActivityName: String by lazy(LazyThreadSafetyMode.NONE) { intent.getStringExtra(EXTRA_PARENT_ACTIVITY_CLASS_NAME) }


    //一个界面会有一个mActivityHelper
    private val mActivityHelper: ActivityHelper by lazy { ActivityHelper(this) }
    private val rxPermissions: RxPermissions by lazy { RxPermissions(this) }


    private val mToolbar: Toolbar by lazy(LazyThreadSafetyMode.NONE) { findViewById<Toolbar>(R.id.mToolBar) }
    protected var disposable: Disposable? = null
    lateinit var presenter: T//在oncreate中初始化P在Ondestory中释放V
    lateinit var binding: B//在onCreate中初始化
    protected var swipeTarget: DDRecyclerViewLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatuBarTheme()
        //如果是过于简单的页面没有P层没有写泛型的话这个binding使用的时候需要强转
        binding = DataBindingUtil.setContentView(this, setDataBindingContentViewId())

        presenter = initPresenter()

        lifecycle.addObserver(presenter as AbstractBasePresenter<*>)

        onCreated(savedInstanceState)

    }

    /***初始化成功后 */
    protected abstract fun onCreated(savedInstanceState: Bundle?)

    /***这里是适配为不同的View 装载不同Presenter */
    protected abstract fun initPresenter(): T

    /***设置setContentViewId */
    protected abstract fun setDataBindingContentViewId(): Int

    /**
     * 设置statubar样式 如果要设置activity透明这个需要理新实现否则就不会透明了
     */
    protected fun setStatuBarTheme() {
        StatusBarUtil.setColorForSwipeBack(this, resources.getColor(R.color.app_color), 50)
        // 经测试在代码里直接声明透明状态栏更有效 这个设置会在一些机子上有半透明效果
        //window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }

    /**
     * 使用databinding显示隐藏或一些状态在xml中操作改变时可以进行默认的动画
     */
    protected fun beginBindingTranstionAnim() {
        binding.addOnRebindCallback(object : OnRebindCallback<ViewDataBinding>() {
            override fun onPreBind(binding: ViewDataBinding?): Boolean {
                val root = binding!!.root as ViewGroup
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition(root)
                }
                return true
            }
        })
    }


    protected fun initToolbar(titleResouceStr: String = "", titleResourceId: Int, isNeedBack: Boolean = true) {
        initToolbar(if (titleResourceId > 0) resources.getString(titleResourceId) else titleResouceStr, isNeedBack)
    }

    protected fun getToolbar(): Toolbar {
        return mToolbar
    }

    protected fun initToolbar(titleResouce: String, isNeedBack: Boolean) {
        mToolbar.title = titleResouce
        setSupportActionBar(mToolbar)
        if (isNeedBack) {
            isSwipeEnabled = true
            supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        if (outState != null) {
            val FRAGMENTS_TAG = "Android:support:fragments"
            //清除保存的fragemnt信息因为长时间后台可能回收了activity但fragment没有被回收
            //再打开时会新建activity算恢复但本质是一个新的。这时fragment的getactivity就是null因为get的是之前的
            outState.remove(FRAGMENTS_TAG)
        }
    }

    override protected fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
        unsubscribe(disposable)
    }

    protected fun unsubscribe(disposable: Disposable?) {
        if (disposable != null && !disposable.isDisposed) {
            disposable.dispose()
        }
    }

    protected fun setViewClickListener(vararg views: View) {
        for (view in views) {
            view.setOnClickListener(this)
        }
    }

    override fun startActivity(intent: Intent?) {
        intent?.putExtra(EXTRA_PARENT_ACTIVITY_CLASS_NAME, this.javaClass.name)
        super.startActivity(intent)
    }

    /**
     * 访问失败未有连接,程序内代码错误
     *
     * @param error
     */
    override fun refreshError(error: String) {
        //activity.getActivityHelper().dismissSimpleLoadDialog()
        if (error.isNotEmptyStr()) {
            if (error.contains("404")) {
//            mActivityHelper.dialogMessage("未找到请求地址404\n" + error)
            } else if (error.contains("500")) {
//            getActivityHelper().dialogMessage("请求地址访问错误500\n" + error)
            } else if (error.contains("SocketTimeoutException")) {
//            getActivityHelper().dialogMessage("连接超时请重试SocketTimeoutException\n" + error)
            } else if (error.contains("no address") || error.contains("Failed to connect to")) {
//            getActivityHelper().dialogMessage("没有网络连接no address or Failed to connect to\n" + error)
            } else {
//            getActivityHelper().ErrordialogMessageByMine(error)
            }
        }
        GoneRecyclerViewProgress()
    }

    override fun onStop() {
        stopFlingRecyclerview()
        super.onStop()

    }

    private fun GoneRecyclerViewProgress() {
        swipeTarget?.refresComplete()
    }

    /**
     * 当是图片列表需要标注避免Recyclerview使用Glide加载图片时惯性运动在消毁页面时依然还在加载图片
     * 页面关闭recyclerview不再滑动 否则有可能Glide会出现You cannot start a load for a destroyed activity
     */
    private fun stopFlingRecyclerview() {
        swipeTarget?.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(), MotionEvent.ACTION_CANCEL, 0F, 0F, 0))
    }
}
