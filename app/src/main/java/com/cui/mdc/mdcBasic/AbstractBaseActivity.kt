package com.cui.mdc.mdcBasic

import android.content.Intent
import android.os.*
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.OnRebindCallback
import androidx.databinding.ViewDataBinding
import androidx.transition.TransitionManager
import com.cui.mdc.R
import com.cui.mdc.mdcHelper.ActivityHelper

import com.utils.library.rxpermission.RxPermissions
import com.utils.library.utils.isNotEmptyStr
import com.widget.library.utils.StatusBarUtil
import io.reactivex.disposables.Disposable
import org.json.JSONObject
import retrofit2.HttpException

/**
 * 为防止 Glide会出现You cannot start a load for a destroyed activity页面关闭recyclerview不再滑动 使用Lifecycle写在DDRecyclerviewLyoaut中在onStop生命周期
 */
abstract class AbstractBaseActivity<B : ViewDataBinding, T : BaseContract.BasePresenter> : AbstractBaseSwipeActivity(),
        View.OnClickListener, BaseContract.BaseView {
    /**
     * 传入参数, String 类型， startActivity 启动带入下一个界面的父的启动类的名称
     */
    private val EXTRA_PARENT_ACTIVITY_CLASS_NAME = "Base_Extra_ParentActivityClassName"
    protected val PAGESIZE: Int = 20

    val mHandler = Handler(Looper.getMainLooper()) { message -> false }
    protected val lastActivityName: String by lazy(LazyThreadSafetyMode.NONE) { intent.getStringExtra(EXTRA_PARENT_ACTIVITY_CLASS_NAME) }


    //一个界面会有一个mActivityHelper
    val mActivityHelper: ActivityHelper by lazy { ActivityHelper(this) }
    private val rxPermissions: RxPermissions by lazy { RxPermissions(this) }


    private val mToolbar: Toolbar by lazy(LazyThreadSafetyMode.NONE) { findViewById<Toolbar>(R.id.mToolBar) }
    protected var disposable: Disposable? = null
    lateinit var presenter: T//在oncreate中初始化P在Ondestory中释放V
    lateinit var binding: B//在onCreate中初始化

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
    protected open fun setStatuBarTheme() {
        StatusBarUtil.setColorForSwipeBack(this, resources.getColor(R.color.app_color), 50)
        // 经测试在代码里直接声明透明状态栏更有效 这个设置会在一些机子上有半透明效果
        //window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }

    protected fun setLightbarTheme() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtil.setColor(this, ResourcesCompat.getColor(resources,R.color.stalls_white_bg_color,null), 1)
            StatusBarUtil.setLightMode(this)
        } else {
            StatusBarUtil.setColor(this, resources.getColor(R.color.black_trans_background), 1)
        }
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


    protected fun initToolbar(titleResourceId: Int = 0, isNeedBack: Boolean = true, titleResouceStr: String = "") {
        initToolbar(if (titleResourceId > 0) resources.getString(titleResourceId) else titleResouceStr, isNeedBack)
    }

    protected fun getToolbar(): Toolbar {
        return mToolbar
    }

    private fun initToolbar(titleResouce: String, isNeedBack: Boolean) {
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
    override fun refreshError(error: Throwable?) {
        if (error is HttpException) {
//            val jsonObject = JSONObject(error.response().errorBody()?.string())
//            val errorContent = jsonObject.optString("message")
//            if (errorContent.isNotEmptyStr())
//                mActivityHelper.ErrordialogMessageByMineOrServerErrorStatusCode(errorContent)
        } else {
            val errorStr = error?.message
            if (errorStr.isNotEmptyStr()) {
                if (errorStr!!.contains("404")) {
//                    mActivityHelper.dialogMessage("未找到请求地址404\n" + error)
                } else if (errorStr.contains("500")) {
//                    mActivityHelper.dialogMessage("请求地址访问错误500\n" + error)
                } else if (errorStr.contains("SocketTimeoutException")) {
//                    mActivityHelper.dialogMessage("连接超时请重试SocketTimeoutException\n" + error)
                } else if (errorStr.contains("no address") || errorStr.contains("Failed to connect to")) {
//                    mActivityHelper.dialogMessage("没有网络连接no address or Failed to connect to\n" + error)
                } else {
//                    mActivityHelper.ErrordialogMessageByMineOrServerErrorStatusCode(error?.toString())
                }
            }
        }
    }
}
