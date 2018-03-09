package com.widget.library.refresh.listener

import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener

/**
 * Created by cuiyang on 2017/7/24.
 */

interface onCLoadMoreListener : OnLoadmoreListener {
    override fun onLoadmore(refreshlayout: RefreshLayout?) {
        onLoadMore()
    }
    fun onLoadMore()
}
