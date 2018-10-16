package com.cui.mdc.uiExample;

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cui.mdc.R
import com.cui.mdc.databinding.MainActBinding
import com.cui.mdc.mdcBasic.AbstractBaseActivity
import com.cui.mdc.mdcMode.MainActivityContract
import com.cui.mdc.mdcPresenter.MainActivityPresenter
import com.widget.library.refresh.listener.OnCRefreshListener
import com.widget.library.refresh.listener.onCLoadMoreListener
import com.widget.library.utils.StatusBarUtil
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainExampleActivity : AbstractBaseActivity<MainActBinding, MainActivityContract.MainActivityPresenterIml>(),
        MainActivityContract.MainActivityView, OnCRefreshListener, onCLoadMoreListener {


    private val EXTRA_KEY = "content"
    private var page = 0
    private val handler: Handler by lazy { Handler() }
    private val adapter: SimpleAdapter by lazy { SimpleAdapter() }

    @SuppressLint("SetTextI18n")
    override fun onCreated(savedInstanceState: Bundle?) {
        page = intent.getIntExtra(EXTRA_KEY, 1)
        binding.textview.text = "page_" + page

        setViewClickListener(binding.textview)


        binding.includeRefresh!!.swipeTarget.bindRefreshLayoutAndSetRefreshListener(this, this)
        binding.includeRefresh!!.swipeTarget.layoutManager = LinearLayoutManager(this)
        binding.includeRefresh!!.swipeTarget.adapter = adapter
//        binding.includeRefresh!!.swipeTarget.setBackgroundColor(resources.getColor(R.color.black))
//        (binding.includeRefresh!!.swipeTarget.getRefreshLayouts().refreshFooter as ViewGroup).setBackgroundColor(resources.getColor(R.color.black))
        binding.includeRefresh!!.swipeTarget.refreshBeginCenter()
    }

    override fun setStatuBarTheme() {
//        StatusBarUtil.setColorForSwipeBack(this, resources.getColor(R.color.textGray), 50)
        StatusBarUtil.setColor(this@MainExampleActivity, resources.getColor(R.color.white), 1)
        StatusBarUtil.setLightMode(this@MainExampleActivity)

    }

    override fun onLoadMore() {
        if (adapter.itemCount >= 300) {
            binding.includeRefresh!!.swipeTarget.refresComplete()
            binding.includeRefresh!!.swipeTarget.setEnableLoadeMore(false)
        }
        handler.postDelayed({
            val list = mutableListOf<String>()
            for (inex in 0..100) {
                list.add(inex.toString())
            }
            adapter.addData(list)
            adapter.notifyDataSetChanged()
            binding.includeRefresh!!.swipeTarget.refresComplete()
        }, 1500)
    }

    override fun onRefresh() {
        handler.postDelayed({
            val list = mutableListOf<String>()
            for (inex in 0..115) {
                list.add(inex.toString())
            }
            adapter.addNewData(list)
            adapter.notifyDataSetChanged()
            binding.includeRefresh!!.swipeTarget.refresComplete()
        }, 1500)
    }

    /***初始化P层类*/
    override fun initPresenter(): MainActivityPresenter = MainActivityPresenter(this)

    /***载入layout布局*/
    override fun setDataBindingContentViewId(): Int = R.layout.main_act

    override fun onClick(view: View) {
        when (view.id) {
            R.id.textview -> {
                val intent = Intent(this, MainExampleActivity::class.java)
                intent.putExtra(EXTRA_KEY, ++page)
                startActivity(intent)
            }
        }

    }

    class SimpleAdapter : RecyclerView.Adapter<SimpleAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_simple, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.text.text = list.get(position)
        }

        private val list: MutableList<String> = mutableListOf()

        override fun getItemCount(): Int {
            return list.size
        }


        fun addData(mutableList: MutableList<String>) {
            list.addAll(mutableList)
        }

        fun addNewData(mutableList: MutableList<String>) {
            list.clear()
            list.addAll(mutableList)
        }

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var text: TextView = view.findViewById<View>(R.id.item_txt) as TextView
        }
    }
}
