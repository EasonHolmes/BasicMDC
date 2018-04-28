package com.cui.mdc.uiExample

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cui.mdc.R
import com.cui.mdc.databinding.MainActBinding
import com.cui.mdc.mdcBasic.AbstractBaseActivity
import com.cui.mdc.mdcMode.MainActivityContract
import com.cui.mdc.mdcPresenter.MainActivityPresenter
import com.cui.mdc.uiExample.adapter.MySimpleFragmentPagerAdapter

class MainActivity : AbstractBaseActivity<MainActBinding, MainActivityContract.MainActivityPresenterIml>(), MainActivityContract.MainActivityView {

    private val list_fragment = ArrayList<Fragment>()


    override fun onCreated(savedInstanceState: Bundle?) {
        val viewPagerAdapter = MySimpleFragmentPagerAdapter(supportFragmentManager, getListFragment())
//        binding.mainviewpager.setAdapter(viewPagerAdapter)

//        @Suppress("DEPRECATION")
//        binding.mainviewpager.setOnPageChangeListener(MainViewPagerChangeListener(binding.mainviewpager, this,
//                getDefault_Bottom_drawable(), getSelect_Bottom_drawable(),
//                getChange_Bottom_TextViews(), getChange_Bottom_Viewgroup(), this))
//        binding.mainviewpager.offscreenPageLimit = list_fragment.size
//
//        val user = mActivityHelper.getCurrentUserInfo()
//        if (user.isLogin) PushAgent.getInstance(this).setAlias(user.userId.toString(), "zaojiu") { _, _ -> Unit }

    }

    /**
     * 默认tabbar图标
     *
     * @return
     */
    private fun getDefault_Bottom_drawable(): Array<Drawable> {
        return arrayOf(ResourcesCompat.getDrawable(resources, R.drawable.ic_launcher_background, null)!!,
                ResourcesCompat.getDrawable(resources, R.drawable.ic_launcher_background, null)!!)
    }

    /**
     * 选中tabbar图标
     *
     * @return
     */
    private fun getSelect_Bottom_drawable(): Array<Drawable> {
        return arrayOf(ResourcesCompat.getDrawable(resources, R.drawable.ic_launcher_background, null)!!,
                ResourcesCompat.getDrawable(resources, R.drawable.ic_launcher_background, null)!!)
    }

    /**
     * 点击的viewgroup
     *
     * @return
     */
//    private fun getChange_Bottom_Viewgroup(): Array<ViewGroup> {
//        return arrayOf(binding.include!!.tabbar1, binding.include!!.tabbar2)
//    }

    /**
     * tabbarTextView
     *
     * @return
     */
//    private fun getChange_Bottom_TextViews(): Array<TextView> {
//        return arrayOf<TextView>(binding.include!!.tabbar1Text, binding.include!!.tabbar2Text)
//    }

    override fun setStatuBarTheme() {
        setLightbarTheme()
    }

    private fun getListFragment(): List<Fragment> {
//        list_fragment.add(MainFragment())
//        list_fragment.add(VipFragment())
        return list_fragment
    }

    /***初始化P层类*/
    override fun initPresenter(): MainActivityPresenter = MainActivityPresenter(this)

    /***载入layout布局*/
    override fun setDataBindingContentViewId(): Int = R.layout.activity_main

    override fun onClick(view: View) {

    }
}