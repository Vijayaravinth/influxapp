package com.influx.demo.viewmodel

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.influx.demo.view.fragment.CombosFragment
import com.influx.demo.view.fragment.CrepesFragment
import com.influx.demo.view.fragment.DrinksFragment


 class MainViewPagerAdapter(manager: FragmentManager, context: Context) :
    FragmentStatePagerAdapter(
        manager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {


    private val COMBOS = 0
    private val DRINKS = 1
    private val CREPES = 2

    private val TABS = intArrayOf(COMBOS, DRINKS, CREPES)

    var context: Context

    init {
        this.context = context.applicationContext
    }


    override fun getCount(): Int {
        return TABS.size
    }

    override fun getPageTitle(position: Int): CharSequence? {

        Log.w("Adapter ","pos "+ position)

        when (TABS[position]) {
            COMBOS ->
                return "COMBOS"
            DRINKS ->
                return "DRINKS"
            CREPES ->
                return "CREPES"
            else ->
                return null
        }


    }


    override fun getItem(position: Int): Fragment {

        Log.w("Adapter ","pos item"+ position)

        when (TABS[position]) {

            COMBOS ->
                return CombosFragment()
            DRINKS ->
                return DrinksFragment()
            else ->
                return CrepesFragment()

        }


    }


}