package com.influx.demo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.influx.demo.R
import com.influx.demo.model.UserSelectedItem
import kotlinx.android.synthetic.main.adapter_order.view.*

class OrderAdapter(context: Context, list: ArrayList<UserSelectedItem>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    lateinit var context: Context
    lateinit var list: ArrayList<UserSelectedItem>

    init {

        this.context = context
        this.list = list
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderAdapter.OrderViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_order, parent, false)
        return OrderViewHolder(view)


    }

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        @BindView(R.id.txtName)
        lateinit var txtName: TextView
        @BindView(R.id.txtPrice)
        lateinit var txtPrice: TextView


        init {
            ButterKnife.bind(this, itemView)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: OrderAdapter.OrderViewHolder, position: Int) {


        val orderItem = list.get(position)


        var name : String = orderItem.name + " ("+ orderItem.count+" ) "
        var totalPrice = orderItem.price.toFloat() * orderItem.count

        holder.txtName.text = name
        holder.txtPrice.text = totalPrice.toString()

    }
}