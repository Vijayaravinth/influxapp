package com.influx.demo.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class DrinksFragment : Fragment() {


    val TAG = "Drinks"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.w(TAG,"Drinks fragment")

        return super.onCreateView(inflater, container, savedInstanceState)
    }
}