package com.cui.mdc.mdcBasic

/**
 * Created by cuiyang on 16/5/11.
 */
interface BaseContract {

    interface BaseView {

        /**
         * 程序错误返回
         *
         * @param error
         */
        fun refreshError(error: Throwable?)

    }

    interface BasePresenter
}
