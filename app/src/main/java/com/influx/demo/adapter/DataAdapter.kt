package com.influx.demo.adapter

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.influx.demo.R
import com.influx.demo.model.UserSelectedItem
import com.influx.demo.model.fnblist
import com.influx.demo.model.subitems
import com.influx.demo.view.fragment.CombosFragment
import org.w3c.dom.Text
import com.influx.demo.adapter.DataAdapter.orderItem as orderItem

class DataAdapter(context: Context, list: ArrayList<fnblist>, fragment: CombosFragment) :
    RecyclerView.Adapter<DataAdapter.DataViewHolder>() {


    var context: Context = context
    var list: ArrayList<fnblist> = list


    val fragment: CombosFragment

    lateinit var listener: orderItem

    init {
        this.context = context
        this.list = list
        listener = context as orderItem
        this.fragment = fragment


    }


    interface orderItem {
        fun removeItem(vistaId: String, subVistaId: String)
        fun updateItme(
            vistaId: String,
            subVistaId: String,
            count: Int,
            price: String,
            name: String,
            type: String
        )
    }


    class DataViewHolder(binding: View) : RecyclerView.ViewHolder(binding) {


        @BindView(R.id.imgFood)
        lateinit var imgFood: ImageView

        @BindView(R.id.txtName)
        lateinit var txtName: TextView

        @BindView(R.id.txtPrice)
        lateinit var txtPrice: TextView

        @BindView(R.id.imgAdd)
        lateinit var imgAdd: ImageView

        @BindView(R.id.imgMinus)
        lateinit var imgMinus: ImageView

        @BindView(R.id.txtCount)
        lateinit var txtCount: TextView


        @BindView(R.id.txtNormal)
        lateinit var txtNormal: TextView

        @BindView(R.id.txtLarge)
        lateinit var txtLarge: TextView

        @BindView(R.id.txtRegular)
        lateinit var txtRegular: TextView

        @BindView(R.id.typLayout)
        lateinit var typeLayout: LinearLayout


        init {
            ButterKnife.bind(this, binding)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {


        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_data, parent, false)

        return DataViewHolder(view)
    }

    override fun getItemCount(): Int {

        return list.size
    }


    fun setData(list: ArrayList<fnblist>) {
        this.list = list
        notifyDataSetChanged()
    }


    private fun setText(textView: TextView, text: String, subItemsList: ArrayList<subitems>) {

        for (subitems in subItemsList) {


            if (text.contentEquals(subitems.Name.trim())) {
                textView.text = "AED " + subitems.SubitemPrice
                break
            }


        }
    }


    private fun getPositionOfSubItem(text: String, subItemsList: ArrayList<subitems>): Int {

        var pos: Int = -1
        for (i in 0 until subItemsList.size) {
            pos = i
            if (text.contentEquals(subItemsList.get(i).Name.trim())) {
                break
            }
        }
        return pos
    }


    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {


        var currentType = ""

        val dataObj = list.get(position)

        holder.txtName.text = dataObj.Name
        holder.txtPrice.text = "AED " + dataObj.PriceInCents


        val subListCount = dataObj.SubItemCount

        if (subListCount == 0) {

            holder.typeLayout.visibility = GONE
        } else {
            holder.typeLayout.visibility = VISIBLE




            for (subitems in dataObj.subitems) {

                when (subitems.Name.trim()) {
                    "MEDIUM" -> {
                        holder.txtRegular.visibility = VISIBLE
                        holder.txtPrice.text = "AED " + subitems.PriceInCents
                        currentType = "MEDIUM"
                    }
                    "SMALL" ->
                        holder.txtNormal.visibility = VISIBLE
                    "LARGE" ->
                        holder.txtLarge.visibility = VISIBLE

                }

            }

        }


        Glide.with(context).load(dataObj.ImageUrl)
            .into(holder.imgFood)


        holder.imgAdd.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                val currentCount = holder.txtCount.text


                val cout: String = currentCount.toString()

                val count = cout.toInt()

                Log.w("adapter", "count " + count)

                holder.txtCount.text = (count + 1).toString()

                if (subListCount != 0) {
                    val pos = getPositionOfSubItem(currentType, dataObj.subitems)
                    val subItem = dataObj.subitems.get(pos)
                    listener.updateItme(
                        dataObj.VistaFoodItemId,
                        subItem.VistaSubFoodItemId,
                        (count + 1),
                        subItem.SubitemPrice,
                        subItem.Name,
                        "add"
                    )
                } else {
                    listener.updateItme(
                        dataObj.VistaFoodItemId,
                        dataObj.VistaFoodItemId,
                        (count + 1),
                        dataObj.ItemPrice,
                        dataObj.Name,
                        "add"
                    )
                }

            }
        })

        holder.imgMinus.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val currentCount = holder.txtCount.text
                if (currentCount.equals("1")) {

                    if (!currentCount.equals("0")) {
                        if (subListCount != 0) {
                            val pos = getPositionOfSubItem(currentType, dataObj.subitems)
                            val subItem = dataObj.subitems.get(pos)

                            listener.removeItem(dataObj.VistaFoodItemId, subItem.VistaSubFoodItemId)
                        } else {
                            listener.removeItem(dataObj.VistaFoodItemId, dataObj.VistaFoodItemId)

                        }
                    }
                    holder.txtCount.text = "0"


                } else {

                    val cout: String = currentCount.toString()

                    val count = cout.toInt()

                    Log.w("adapter", "count " + count)

                    holder.txtCount.text = (count - 1).toString()

                    if (subListCount != 0) {
                        val pos = getPositionOfSubItem(currentType, dataObj.subitems)
                        val subItem = dataObj.subitems.get(pos)
                        listener.updateItme(
                            dataObj.VistaFoodItemId,
                            subItem.VistaSubFoodItemId,
                            count - 1,
                            subItem.SubitemPrice,
                            subItem.Name,
                            "remove"
                        )
                    } else {
                        listener.updateItme(
                            dataObj.VistaFoodItemId,
                            dataObj.VistaFoodItemId,
                            count - 1,
                            dataObj.ItemPrice,
                            dataObj.Name,
                            "remove"
                        )
                    }


                }
            }
        })

        holder.txtNormal.setOnClickListener {

            currentType = "SMALL"
            holder.txtNormal.background =
                ContextCompat.getDrawable(context, R.drawable.textbackground_normal)
            holder.txtLarge.background =
                ContextCompat.getDrawable(context, R.drawable.textview_backgroound)
            holder.txtRegular.background =
                ContextCompat.getDrawable(context, R.drawable.textview_backgroound)
            setText(holder.txtPrice, "SMALL", dataObj.subitems)
            val orderMap: HashMap<String, HashMap<String, UserSelectedItem>> = fragment.getMap()

            if (orderMap.containsKey(dataObj.VistaFoodItemId)) {

                if (subListCount != 0) {
                    val pos = getPositionOfSubItem(currentType, dataObj.subitems)
                    val subItem = dataObj.subitems.get(pos)
                    val map: java.util.HashMap<String, UserSelectedItem>? =
                        orderMap.get(dataObj.VistaFoodItemId)

                    val item = map!!.get(subItem.VistaSubFoodItemId)
                    if (item != null) {

                        holder.txtCount.text = item.count.toString()
                    } else {
                        holder.txtCount.text = "0"

                    }

                } else {

                    val map: java.util.HashMap<String, UserSelectedItem>? =
                        orderMap.get(dataObj.VistaFoodItemId)

                    val item = map!!.get(dataObj.VistaFoodItemId)
                    if (item != null) {

                        holder.txtCount.text = item.count.toString()
                    } else {
                        holder.txtCount.text = "0"

                    }

                }

            }

        }

        holder.txtRegular.setOnClickListener {

            currentType = "MEDIUM"

            holder.txtRegular.background =
                ContextCompat.getDrawable(context, R.drawable.textbackground_normal)
            holder.txtLarge.background =
                ContextCompat.getDrawable(context, R.drawable.textview_backgroound)
            holder.txtNormal.background =
                ContextCompat.getDrawable(context, R.drawable.textview_backgroound)
            setText(holder.txtPrice, "MEDIUM", dataObj.subitems)


            val orderMap: HashMap<String, HashMap<String, UserSelectedItem>> = fragment.getMap()

            if (orderMap.containsKey(dataObj.VistaFoodItemId)) {

                if (subListCount != 0) {
                    val pos = getPositionOfSubItem(currentType, dataObj.subitems)
                    val subItem = dataObj.subitems.get(pos)
                    val map: java.util.HashMap<String, UserSelectedItem>? =
                        orderMap.get(dataObj.VistaFoodItemId)

                    val item = map!!.get(subItem.VistaSubFoodItemId)
                    if (item != null) {

                        holder.txtCount.text = item.count.toString()
                    } else {
                        holder.txtCount.text = "0"

                    }

                } else {

                    val map: java.util.HashMap<String, UserSelectedItem>? =
                        orderMap.get(dataObj.VistaFoodItemId)

                    val item = map!!.get(dataObj.VistaFoodItemId)
                    if (item != null) {

                        holder.txtCount.text = item.count.toString()
                    } else {
                        holder.txtCount.text = "0"

                    }

                }

            }


        }
        holder.txtLarge.setOnClickListener {
            currentType = "LARGE"




            holder.txtLarge.background =
                ContextCompat.getDrawable(context, R.drawable.textbackground_normal)
            holder.txtNormal.background =
                ContextCompat.getDrawable(context, R.drawable.textview_backgroound)
            holder.txtRegular.background =
                ContextCompat.getDrawable(context, R.drawable.textview_backgroound)
            setText(holder.txtPrice, "LARGE", dataObj.subitems)

            val orderMap: HashMap<String, HashMap<String, UserSelectedItem>> = fragment.getMap()

            if (orderMap.containsKey(dataObj.VistaFoodItemId)) {

                if (subListCount != 0) {
                    val pos = getPositionOfSubItem(currentType, dataObj.subitems)
                    val subItem = dataObj.subitems.get(pos)
                    val map: java.util.HashMap<String, UserSelectedItem>? =
                        orderMap.get(dataObj.VistaFoodItemId)

                    val item = map!!.get(subItem.VistaSubFoodItemId)
                    if (item != null) {

                        holder.txtCount.text = item.count.toString()
                    } else {
                        holder.txtCount.text = "0"

                    }

                } else {

                    val map: java.util.HashMap<String, UserSelectedItem>? =
                        orderMap.get(dataObj.VistaFoodItemId)

                    val item = map!!.get(dataObj.VistaFoodItemId)
                    if (item != null) {

                        holder.txtCount.text = item.count.toString()
                    } else {
                        holder.txtCount.text = "0"

                    }

                }

            }

        }


    }
}