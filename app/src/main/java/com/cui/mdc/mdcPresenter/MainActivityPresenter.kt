package com.cui.mdc.mdcPresenter

import com.cui.mdc.mdcBasic.AbstractBasePresenter
import com.cui.mdc.mdcMode.MainActivityContract
import com.cui.mdc.uiExample.MainActivity

class MainActivityPresenter(mView: MainActivity) : AbstractBasePresenter<MainActivity>(mView),
        MainActivityContract.MainActivityPresenterIml
