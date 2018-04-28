package com.cui.mdc.mdcBasic

import android.support.v7.widget.RecyclerView
import com.cui.mdc.mdcMode.entity.BaseEntity

import com.widget.library.refresh.recyclerview.DDRecyclerViewLayout



/**
 * Created by cuiyang on 16/6/3.
 * /**/**
 * 不需要上拉下拉时refreshLayout可传null
 * 这里只是控制在数据过于少和数据量恢复后停止和恢复上拉下拉加载
 */
abstract class AbstractBaseAdapter<T : BaseEntity>(private val ddRecyclerViewLayout: DDRecyclerViewLayout? = null) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mData = mutableListOf<T>()
    //    private val mDecelerateInterpolator = DecelerateInterpolator(2f)
//    private val mDuration = 500
//    private val mDelay = 30
//    private var mLastPosition = -1
//    private var isFirstOnly = true
//    protected lateinit var binding: ViewDataBinding

    val data: MutableList<T>
        get() = mData

    fun setNewData(data: MutableList<T>?) {
        setNewDatas(data, true)
    }

    fun addOneData(data: MutableList<T>?) {
        setNewDatas(data, false)
    }

    fun setNewDatas(data: MutableList<T>?, refresh: Boolean) {
        if (!refresh && data != null) {
            val p = this.mData.size
            this.mData.addAll(data)
            //p 更改起始位置  data.size已更改的数目
            //notifyItemRangeChanged(p, data.size)
            notifyItemRangeInserted(p, data.size)
        } else {
            if (data != null) {
                this.mData.clear()
                this.mData.addAll(data)
                notifyDataSetChanged()
            }
        }
        setLoadMore(data)
    }

    fun clearData() {
        if (mData.size > 0)
            mData.clear()
    }

    /**
     * 当没有数据时不再上拉加载
     */
    private fun setLoadMore(data: List<T>?) {
        if (data == null || data.isEmpty()) {
            ddRecyclerViewLayout?.setEnableLoadeMore(false)
        } else if (data.size > 6) {//数据大于10条时再恢复
            ddRecyclerViewLayout?.setEnableLoadeMore(true)
            ddRecyclerViewLayout?.setEnableAutoLoadeMore(true)
        }
    }

    fun addOneData(data: T, position: Int) {
        this.mData.add(position, data)
        notifyItemInserted(position)
    }

    fun removeOneData(position: Int) {
        this.mData.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getItem(position: Int): T {
        return mData[position]
    }

    override fun getItemCount(): Int {
        return mData.size
    }
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
//                setItemLayoutResource(), parent, false)
//        return BindingViewHolder(binding)
//    }

    //    @Override
    //    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    //        BindingViewHolder viewHolder = (BindingViewHolder) holder;
    //        ItemNewPurchaseBinding binding = (ItemNewPurchaseBinding) viewHolder.getBinding();
    //    }


//    /**
//     * item动画
//
//     * @param anim
//     * *
//     * @param holder
//     */
//    protected fun OpenAnimation(anim: AdapterAnim, holder: RecyclerView.ViewHolder) {
//        //第一次进入或者没有被显示过的item才会显示动画.否则显示过的就不再次显示为了性能着想
//        if (!isFirstOnly || holder.layoutPosition > mLastPosition) {
//            when (anim) {
//                AdapterAnim.SCALE -> {
//                    val scaleX = ObjectAnimator.ofFloat(holder.itemView, "scaleX", 0.6f, 1f)
//                    val scaleY = ObjectAnimator.ofFloat(holder.itemView, "scaleY", 0.6f, 1f)
//                    startAnim(scaleX, scaleY)
//                }
//                AdapterAnim.TRANSLATE -> {
//                    val translateY = ObjectAnimator.ofFloat(holder.itemView, "translationY", 300f, 1f)
//                    startAnim(translateY)
//                }
//            }
//            mLastPosition = holder.layoutPosition//存储已经显示过的最大position,最后一个显示过的动画位置
//        }
//    }
//
//    /**
//     * set anim to start when loading
//     */
//    protected fun startAnim(vararg animators: Animator) {
//        for (anim in animators) {
//            anim.setDuration(mDuration.toLong()).startDelay = mDelay.toLong()
//            // * mLastPosition
//            anim.start()
//            anim.interpolator = mDecelerateInterpolator
//        }
//    }

//    /**
//     * 是否每个item只显示一次动画.默认为true
//
//     * @param firstOnly
//     */
//    protected fun setFirstOnly(firstOnly: Boolean) {
//        this.isFirstOnly = firstOnly
//    }

//    public enum class AdapterAnim(val type: Int) {
//        SCALE(0),
//        TRANSLATE(1)
//    }
}
