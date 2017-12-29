package com.cui.mdc.uiExample;

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.cui.mdc.R
import com.cui.mdc.databinding.MainActBinding
import com.cui.mdc.mdcBasic.AbstractBaseActivity
import com.cui.mdc.mdcMode.MainActivityContract
import com.cui.mdc.mdcPresenter.MainActivityPresenter

class MainActivity : AbstractBaseActivity<MainActBinding, MainActivityPresenter>(),
        MainActivityContract.MainActivityView {

    private val EXTRA_KEY = "content"
    private var page = 0

    @SuppressLint("SetTextI18n")
    override fun onCreated(savedInstanceState: Bundle?) {
        page = intent.getIntExtra(EXTRA_KEY, 1)

        initToolbar(R.string.app_name, page != 1)

        binding.textview.text = "page_" + page

        setViewClickListener(binding.textview)

    }

    /***初始化P层类*/
    override fun initPresenter(): MainActivityPresenter = MainActivityPresenter(this)

    /***载入layout布局*/
    override fun setDataBindingContentViewId(): Int = R.layout.main_act

    override fun onClick(view: View) {
        when (view.id) {
            R.id.textview -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra(EXTRA_KEY, ++page)
                startActivity(intent)
            }
        }

    }
}
