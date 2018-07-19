package com.cui.mdc.mdcBasic

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.cui.mdc.uiExample.adapter.BindingViewHolder
import com.widget.library.refresh.recyclerview.DDRecyclerViewLayout

/**
 * Created by cuiyang on 2018/3/28.
 * /**
 * 不需要上拉下拉时refreshLayout可传null
 * 这里只是控制在数据过于少和数据量恢复后停止和恢复上拉下拉加载
*/
 */
abstract class SingleTypeListAdapter<T, B : ViewDataBinding>(ddRecyclerViewLayout: DDRecyclerViewLayout) : AbstractBaseAdapter<T>(ddRecyclerViewLayout), LifecycleObserver {
    lateinit var binding: B

    override fun onBindViewHolder(holder1: RecyclerView.ViewHolder, position: Int) {
        val holder = holder1 as BindingViewHolder<*>
        val item = getItem(position)
        @Suppress("UNCHECKED_CAST")
        val binding = holder.binding as B
        onItemBinding(binding, item, position)
    }

    protected abstract fun setItemLayoutResource(): Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                setItemLayoutResource(), parent, false)
        return BindingViewHolder(binding)
    }

    abstract fun onItemBinding(binding: B, item: T, position: Int)

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun clearItemTypeMapAndData() {
        super.data.clear()
    }
}
