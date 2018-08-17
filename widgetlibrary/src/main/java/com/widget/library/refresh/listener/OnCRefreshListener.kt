package com.widget.library.refresh.listener

import com.scwang.smartrefresh.layout.api.RefreshLayout


/**
 * Created by cuiyang on 2017/7/24.
 */

interface OnCRefreshListener : com.scwang.smartrefresh.layout.listener.OnRefreshListener {
//    override fun onRefresh(refreshlayout: RefreshLayout?) {
//        onRefresh()
//    }
    override fun onRefresh(refreshLayout: RefreshLayout) {
        onRefresh()
    }

    fun onRefresh()
}
