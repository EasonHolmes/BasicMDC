package com.widget.library.edittext.searchview

import android.animation.Animator
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.CardView
import android.support.v7.widget.SearchView
import android.text.InputType
import android.text.TextUtils
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import com.widget.library.R
import com.widget.library.utils.Utils.dpToPx


/**
 * Created by shahroz on 1/8/2016.
 */

class MaterialSearchView @JvmOverloads constructor(private val mContext: Context, attrs: AttributeSet? = null, defStyle: Int = -1) : FrameLayout(mContext, attrs, defStyle), View.OnClickListener {

    val searchView: SearchView
    private val mClearSearch: ImageView
    private var mOnSearchListener: onSearchListener? = null
    val cardLayout: CardView
    private val backArrowImg: ImageView
    private val DURATION_TIME = 400L

    init {
        val factory = LayoutInflater.from(mContext)
        factory.inflate(R.layout.toolbar_searchview, this)
        cardLayout = findViewById<CardView>(R.id.card_search) as CardView
        searchView = findViewById<SearchView>(R.id.edit_text_search) as SearchView
        backArrowImg = findViewById<ImageView>(R.id.image_search_back) as ImageView
        mClearSearch = findViewById<ImageView>(R.id.clearSearch) as ImageView
        searchView.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS

        //设置搜索框直接展开显示。左侧有放大镜(在搜索框中) 右侧有叉叉 可以关闭搜索框
//        mSearchView.setIconified(false);
////设置搜索框直接展开显示。左侧有放大镜(在搜索框外) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
//        mSearchView.setIconifiedByDefault(false);
        //设置搜索框直接展开显示。左侧有无放大镜(在搜索框中) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
        searchView.onActionViewExpanded()
        searchView.clearFocus()


        mClearSearch.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP)
        backArrowImg.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP)

        findViewById<ImageView>(R.id.image_search_back).setOnClickListener(this)
        mClearSearch.setOnClickListener(this)
        clearAnimation()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                mOnSearchListener?.onSearchSubmit(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                mOnSearchListener?.onSearchChanged(newText)
                toggleClearSearchButton(newText)
                return false
            }
        })

        searchView.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_SEARCH)) {
                val query = searchQuery
                if (!TextUtils.isEmpty(query) && mOnSearchListener != null) {
                    mOnSearchListener!!.onSearchChanged(query)
                }
                return@OnKeyListener true
            }
            false
        })
    }

    fun setOnSearchListener(l: onSearchListener) {
        mOnSearchListener = l
    }

    fun getSearchListener(): onSearchListener? {
        return mOnSearchListener
    }

    var searchQuery: String
        get() = if (searchView.query != null) searchView.query.toString() else ""
        set(query) {
            searchView.setQuery(query, false)
            toggleClearSearchButton(query)
        }


    val isSearchViewVisible: Boolean
        get() = cardLayout.visibility == View.VISIBLE

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
// Show the SearchView
    fun showSearch() {
        if (isSearchViewVisible) return
        cardLayout.visibility = View.VISIBLE
        if (cardLayout.visibility == View.VISIBLE) {
//            mOnSearchListener!!.searchViewOpened()
            val animator = ViewAnimationUtils.createCircularReveal(cardLayout,
                    cardLayout.width - dpToPx(56f, context.resources),
                    dpToPx(23f, context.resources),
                    0f,
                    Math.hypot(cardLayout.width.toDouble(), cardLayout.height.toDouble()).toFloat())
            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) {
//                    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
                    showSoftInput()
                }

                override fun onAnimationCancel(animation: Animator) {}

                override fun onAnimationRepeat(animation: Animator) {}
            })
            animator.duration = DURATION_TIME
            animator.start()
            cardLayout.isEnabled = true
        }

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun hideSearch() {
        if (!isSearchViewVisible) return
//        mOnSearchListener!!.searchViewClosed()

        hideSoftInput()

        val animatorHide = ViewAnimationUtils.createCircularReveal(cardLayout,
                cardLayout.width - dpToPx(56f, context.resources),
                dpToPx(23f, context.resources),
                Math.hypot(cardLayout.width.toDouble(), cardLayout.height.toDouble()).toFloat(),
                0f)
        animatorHide.duration = DURATION_TIME
        animatorHide.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                cardLayout.visibility = View.GONE
                clearSearch()
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })
        animatorHide.start()
    }

    private fun toggleClearSearchButton(query: CharSequence?) {
        mClearSearch.visibility = if (!TextUtils.isEmpty(query)) View.VISIBLE else View.INVISIBLE
    }

    private fun clearSearch() {
        searchView.setQuery("", false)
        mClearSearch.visibility = View.INVISIBLE
    }

    private fun onBackSearch() {
        if (!TextUtils.isEmpty(searchView.query.toString())) {
            searchView.setQuery("", false)
        }
//        else {
//            hideSearch()
//        }
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
//        if (event.action == KeyEvent.ACTION_UP && event.keyCode == KeyEvent.KEYCODE_BACK) {
//            onBackSearch()
//            return true
//        }
        return super.dispatchKeyEvent(event)
    }

    private fun getInputMethodManager(): InputMethodManager {
        return searchView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    @RequiresApi(Build.VERSION_CODES.CUPCAKE)
    private fun hideSoftInput() {
        if (getInputMethodManager().isActive) {
            getInputMethodManager().hideSoftInputFromWindow(searchView.windowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN)
        }
    }

    @RequiresApi(Build.VERSION_CODES.CUPCAKE)
    private fun showSoftInput() {
        searchView.isFocusable = true
        searchView.isFocusableInTouchMode = true
        searchView.requestFocus()
        getInputMethodManager().showSoftInput(searchView, 0)
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.image_search_back) {
            onBackSearch()
        } else if (id == R.id.clearSearch) {
            clearSearch()
        }
    }
}