package com.widget.library.dialog_pop

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.TextView

import com.widget.library.R
import com.widget.library.progress.ProgressWheel

/**
 * Created by cuiyang on 16/6/2.
 * 要加蒙版在style中把SimpleProgressDialog的windowBackground改为这样
 *         <!--<item name="android:windowBackground">@android:color/transparent</item>-->
 */
class SimpleProgressDialog : Dialog, DialogInterface.OnDismissListener {
    private var progressImg: ProgressWheel? = null
    private var progressTxt: TextView? = null


    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, theme: Int) : super(context, theme) {
        init()
    }

    fun setMessage(strMessage: String) {
        if (!TextUtils.isEmpty(strMessage)) {
            progressTxt?.visibility = View.VISIBLE
            progressTxt?.text = strMessage
        } else {
            progressTxt?.visibility = View.GONE
        }
    }

    fun init() {
        this@SimpleProgressDialog.setContentView(R.layout.simple_progress_dialog)
        this@SimpleProgressDialog.window!!.attributes.gravity = Gravity.CENTER
        progressTxt = this@SimpleProgressDialog.findViewById(R.id.text1) as TextView
        progressImg = this@SimpleProgressDialog.findViewById(R.id.progress) as ProgressWheel
    }

    override fun onDismiss(dialog: DialogInterface) {
        //        animStop();
    }
}
