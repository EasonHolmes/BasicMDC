package com.widget.library.refresh.listener

import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener

/**
 * Created by cuiyang on 2017/7/24.
 */

interface onCLoadMoreListener : OnLoadMoreListener {
    //    override fun onLoadMore(refreshLayout: RefreshLayout?) {
//        onLoadMore()
//    }
    override fun onLoadMore(refreshLayout: RefreshLayout) {
        onLoadMore()
    }

    fun onLoadMore()
}
