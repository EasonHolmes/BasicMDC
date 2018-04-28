package com.cui.mdc.mdcBasic

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.res.Resources
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.cui.mdc.mdcMode.entity.BaseEntity
import com.cui.mdc.uiExample.adapter.BindingViewHolder

/**
 * Created by cuiyang on 2018/3/28.
 */
abstract class MultiTypeListAdapter<T : BaseEntity> : AbstractBaseAdapter<T>(), LifecycleObserver {
    lateinit var binding: ViewDataBinding
    lateinit var itemTypeMap: HashMap<Int, Int>

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder1: RecyclerView.ViewHolder, position: Int) {
        val holder = holder1 as BindingViewHolder<*>
        val item = getItem(position)
        onItemBinding(item, position,holder.binding)
    }

    abstract fun onItemBinding(item: T, position: Int, vararg itemBidnings: ViewDataBinding)

    abstract fun itemTypeBinding()

    override fun getItemViewType(position: Int): Int {
        return getItem(position).itemType
    }

    abstract fun addViewTypeToLayoutMap(itemTypeMap: HashMap<Int, Int>): HashMap<Int, Int>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                getLayoutRes(viewType), parent, false)
        return BindingViewHolder(binding)
    }

    @LayoutRes
    protected fun getLayoutRes(viewType: Int): Int = itemTypeMap[viewType]
            ?: throw Resources.NotFoundException("$viewType 对应的布局不存在")

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun clearItemTypeMapAndData() {
        itemTypeMap.clear()
        super.data.clear()
    }
}
