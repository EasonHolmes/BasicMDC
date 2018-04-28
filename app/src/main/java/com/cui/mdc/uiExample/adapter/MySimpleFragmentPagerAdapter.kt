package com.cui.mdc.uiExample.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

import java.util.ArrayList

/**
 * Created by cuiyang on 16/6/2.
 */
class MySimpleFragmentPagerAdapter(fm: FragmentManager, private val listFragment: List<Fragment>) : FragmentStatePagerAdapter(fm) {
    private var title: MutableList<String> = mutableListOf()

    override fun getItem(position: Int): Fragment {
        return listFragment[position]
    }

    override fun getCount(): Int {
        return listFragment.size
    }

    fun setPageTitle(title: MutableList<String>) {
        this.title = title
    }


    override fun getPageTitle(position: Int): CharSequence {
        return title[position]
    }

}
