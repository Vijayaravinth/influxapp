package com.influx.demo.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.influx.demo.R
import com.influx.demo.adapter.DataAdapter
import com.influx.demo.model.FoodList
import com.influx.demo.model.UserSelectedItem
import com.influx.demo.model.fnblist
import com.influx.demo.view.activity.MainActivity

class CombosFragment : Fragment() {


    @BindView(R.id.recyclerView)
    lateinit var recyclerView: RecyclerView


    lateinit var adapter: DataAdapter


    val list: ArrayList<fnblist> = ArrayList<fnblist>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        Log.w(TAG, "on view created called")

        setAdapter()
    }

    val TAG = "Combo"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.layout_fragment, container, false)
        ButterKnife.bind(this, view)

        return view
    }


    fun setAdapter() {

        adapter = DataAdapter(activity!!, list, this@CombosFragment)
        recyclerView.adapter = adapter
    }


    public fun changeData(type: String, foodList: ArrayList<FoodList>) {

        list.clear()

        for (data in foodList) {

            if (type.contentEquals(data.TabName)) {
                list.addAll(data.fnblist)
            }
        }


        adapter.notifyDataSetChanged()

    }


    public fun getMap(): HashMap<String, HashMap<String, UserSelectedItem>> {
        return ((activity) as MainActivity).orderMap
    }

}