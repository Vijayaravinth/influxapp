package com.influx.demo.viewmodel

import android.app.Application
import androidx.databinding.BindingAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.widget.RecyclerView
import com.influx.demo.adapter.DataAdapter
import com.influx.demo.model.FoodList
import com.influx.demo.model.fnblist

class DataViewModel(app: Application) : AndroidViewModel(app) {




    fun getData(type: String, foodList: ArrayList<FoodList>): ArrayList<fnblist> {

        val arrayList: ArrayList<fnblist> = ArrayList()

        for (i in 0 until foodList.size) {


            if (type.contentEquals(foodList.get(i).TabName)) {

                arrayList.addAll(foodList.get(i).fnblist)
            }

        }

        return arrayList

    }


    @BindingAdapter("data")
    fun <T> setRecyclerViewProperties(recyclerView: RecyclerView, items: ArrayList<fnblist>) {
        if (recyclerView.adapter is DataAdapter) {
            (recyclerView.adapter as DataAdapter).setData(items)
        }
    }


}