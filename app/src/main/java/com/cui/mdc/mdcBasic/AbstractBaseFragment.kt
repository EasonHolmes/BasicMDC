package com.cui.mdc.mdcBasic

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.OnRebindCallback
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

import com.cui.mdc.R
import com.utils.library.utils.isNotEmptyStr


import io.reactivex.disposables.Disposable
import org.json.JSONObject
import retrofit2.HttpException


/**
 * Created by cuiyang on 16/6/6.
 * 生命周期顺序请注意 1setUserVisibleHint 2onCreateView 3 onCreateViewed
 * 为防止 Glide会出现You cannot start a load for a destroyed activity页面关闭recyclerview不再滑动 使用Lifecycle写在DDRecyclerviewLyoaut中在onStop生命周期
 */
abstract class AbstractBaseFragment<B : ViewDataBinding, T : BaseContract.BasePresenter> : Fragment(),
        View.OnClickListener, BaseContract.BaseView {

    private val EXTRA_PARENT_FRAGMENT_CLASS_NAME = "Base_Extra_ParentFragmentClassName"
    protected val lastFragmentName: String by lazy(LazyThreadSafetyMode.NONE) { activity.intent.getStringExtra(EXTRA_PARENT_FRAGMENT_CLASS_NAME) }

    protected val PAGESIZE: Int = 20
    protected var mToolbar: Toolbar? = null
    protected lateinit var activity: AbstractBaseActivity<*, *>
    protected var mContext: Context? = null
    protected var disposable: Disposable? = null
    protected lateinit var presenter: T//在oncreate中初始化P在Ondestory中释放V
    protected lateinit var binding: B

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate<B>(inflater, setDataBindingContentViewId(), container, false)
        mContext = container!!.context
        activity = getActivity() as AbstractBaseActivity<*, *>
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = initPresenter()
        lifecycle.addObserver(presenter as AbstractBasePresenter<*>)
        onFragmentViewCreated(view, savedInstanceState)
    }

    protected fun setBindingTranstionAnim() {
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

    protected abstract fun onFragmentViewCreated(view: View?, savedInstanceState: Bundle?)

    protected abstract fun setDataBindingContentViewId(): Int

    protected abstract fun initPresenter(): T

    /**
     * disposable

     * @param disposable
     */
    protected fun unsubscribe(disposable: Disposable?) {
        if (disposable != null && !disposable.isDisposed) {
            disposable.dispose()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unsubscribe(disposable)
    }


    protected fun initToolbar(rootView: View?, titleResourceId: Int): Toolbar {
        return initToolbar(rootView, resources.getString(titleResourceId))
    }

    protected fun initToolbar(v: View?, title: String?): Toolbar {
        mToolbar = v?.findViewById(R.id.mToolBar) as Toolbar
        mToolbar?.title = title
        (getActivity() as AppCompatActivity).setSupportActionBar(mToolbar)
        return mToolbar!!
    }

    /**
     * 访问失败未有连接

     * @param error
     */
    override fun refreshError(error: Throwable?) {
//        activity.mActivityHelper.dismissSimpleLoadDialog()
        if (error is HttpException) {
//            val jsonObject = JSONObject(error.response().errorBody()?.string())
//            val errorContent = jsonObject.optString("message")
//            if (errorContent.isNotEmptyStr())
//                activity.mActivityHelper.ErrordialogMessageByMineOrServerErrorStatusCode(errorContent)
        } else {
            val errorStr = error?.message
            if (errorStr.isNotEmptyStr()) {
                if (errorStr!!.contains("404")) {
//                    activity.mActivityHelper.dialogMessage("未找到请求地址404\n" + error)
                } else if (errorStr.contains("500")) {
//                    activity.mActivityHelper.dialogMessage("请求地址访问错误500\n" + error)
                } else if (errorStr.contains("SocketTimeoutException")) {
//                    activity.mActivityHelper.dialogMessage("连接超时请重试SocketTimeoutException\n" + error)
                } else if (errorStr.contains("no address") || errorStr.contains("Failed to connect to")) {
//                    activity.mActivityHelper.dialogMessage("没有网络连接no address or Failed to connect to\n" + error)
                } else {
//                    activity.mActivityHelper.ErrordialogMessageByMineOrServerErrorStatusCode(error?.toString())
                }
            }
        }
    }



    protected fun setViewClickListener(vararg views: View) {
        for (view in views) {
            view.setOnClickListener(this)
        }
    }

    override fun startActivity(intent: Intent?) {
        intent?.putExtra(EXTRA_PARENT_FRAGMENT_CLASS_NAME, this.javaClass.name)
        super.startActivity(intent)
    }

}
