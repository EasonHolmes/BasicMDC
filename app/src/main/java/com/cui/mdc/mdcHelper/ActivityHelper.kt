package com.cui.mdc.mdcHelper

import android.os.Handler
import com.cui.mdc.mdcBasic.AbstractBaseActivity


class ActivityHelper(protected val currentActivity: AbstractBaseActivity<*, *>) {
    val mCurrentActivity = currentActivity
    protected val mHandler: Handler  = mCurrentActivity.mHandler

}
