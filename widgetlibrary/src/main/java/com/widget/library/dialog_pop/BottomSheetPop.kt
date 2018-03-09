package com.widget.library.dialog_pop

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.Button

import com.widget.library.R
import com.widget.library.adapter.BottomSheetAdapter
import com.widget.library.entity.BottomSheetListEntity
import com.widget.library.refresh.listener.OnItemClickListener
import com.widget.library.refresh.recyclerview.DDRecyclerViewLayout

import java.util.ArrayList

/**
 * Created by cuiyang on 16/8/11.
 * 底部弹出BottomSheetDialog
 */

class BottomSheetPop(context: Context, listener: OnItemClickListener) : View.OnClickListener {
    private val mBottomSheetDialog: StrongBottomSheetDialog
    private val recyclerView: DDRecyclerViewLayout
    var adapter: BottomSheetAdapter? = null
        private set

    init {
        mBottomSheetDialog = StrongBottomSheetDialog(context)
        //        mBottomSheetDialog.setMaxHeight(1600);//这个不能设置否则低版本的手机会显示不全
        mBottomSheetDialog.setPeekHeight(1600)
        recyclerView = DDRecyclerViewLayout(context)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.isSmoothScrollbarEnabled = true
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.addFooterView(getFooter(context))

        recyclerView.setOnItemClickListener(listener)
        mBottomSheetDialog.setContentView(recyclerView)

    }

    @Deprecated("")
    fun setItemData(data: Array<String>): MutableList<BottomSheetListEntity> {
        val list = ArrayList<BottomSheetListEntity>()
        for (s in data) {
            if (!TextUtils.isEmpty(s)) {
                list.add(BottomSheetListEntity(s, 0))
            }
        }
        adapter = BottomSheetAdapter()
        recyclerView.adapter = adapter
        adapter!!.setNewData(list)
        return list
    }

    fun setItemData(data: MutableList<BottomSheetListEntity>): MutableList<BottomSheetListEntity> {
        val list = ArrayList<BottomSheetListEntity>()
        for (s in data) {
            if (!TextUtils.isEmpty(s.str)) {
                list.add(s)
            }
        }
        adapter = BottomSheetAdapter()
        recyclerView.adapter = adapter
        adapter!!.setNewData(list)
        return list
    }

    //这个方法是用来修复手动划出屏幕后不再显示的bug，这个方法必须在setContentView后面
    //系统的BottomSheetDialog是基于BottomSheetBehavior的这个我我们知道，这里判断了当我们滑动隐藏了BottomSheetBehavior中的View后，它替我们关闭了Dialog，所以我们再次调用dialog.show()的时候Dialog没法再此打开。
    //所以我们得自己来设置，并且在监听到用户滑动关闭BottomSheetDialog后，我们把BottomSheetBehavior的状态设置为BottomSheetBehavior.STATE_COLLAPSED，也就是半个打开状态（BottomSheetBehavior.STATE_EXPANDED为全打开），根据源码我把设置的方法提供下：
    //    private void setBehaviorCallback() {
    //        View view = mBottomSheetDialog.getDelegate().findViewById(android.support.design.R.id.design_bottom_sheet);
    //        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(view);
    //        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
    //            @Override
    //            public void onStateChanged(@NonNull View bottomSheet, int newState) {
    //                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
    //                    mBottomSheetDialog.dismiss();
    //                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    //                }
    //            }
    //
    //            @Override
    //            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
    //            }
    //        });
    //    }

    private fun getFooter(context: Context): View {
        val footerView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_footer, recyclerView, false)
        val btn_cancle = footerView.findViewById<Button>(R.id.cannel_btn) as Button
        btn_cancle.setOnClickListener(this)
        return footerView
    }


    override fun onClick(view: View) {
        dismiss()
    }

    fun dismiss() {
        mBottomSheetDialog.dismiss()
    }

    fun show() {
        mBottomSheetDialog.show()
    }

}
