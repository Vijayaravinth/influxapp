package com.influx.demo.view.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.material.tabs.TabLayout
import com.influx.demo.R
import com.influx.demo.adapter.DataAdapter
import com.influx.demo.adapter.OrderAdapter
import com.influx.demo.api.ApiClient
import com.influx.demo.api.ApiService
import com.influx.demo.model.FoodList
import com.influx.demo.model.ResponseData
import com.influx.demo.model.UserSelectedItem
import com.influx.demo.util.CheckInternet
import com.influx.demo.view.fragment.CombosFragment
import org.w3c.dom.Text
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), DataAdapter.orderItem {

    override fun removeItem(vistaId: String,subVistaId: String) {
        Log.w(TAG, "remove called")
        orderMap.remove(vistaId)
        for (item in orderList) {
            if (item.vistaId.contentEquals(subVistaId)) {
                totalPrice -= item.price.toFloat()
                setTotalPrice("AED " + totalPrice.toString())

                orderList.remove(item)

            }
        }


        if (orderAdapter != null) {
            orderAdapter!!.notifyDataSetChanged()
        }

        if (orderList.size == 0) {
            orderLayot.visibility = VISIBLE
        }

    }

    override fun updateItme(
        vistaId: String,
        subVistaId: String,
        count: Int,
        price: String,
        name: String,
        type: String
    ) {


        if (orderMap.containsKey(vistaId)) {
            if (type.contentEquals("add")) {
                addOrRemove(vistaId, subVistaId, count, price, name)
                totalPrice += price.toFloat()
                setTotalPrice("AED " + totalPrice.toString())
                Log.w(TAG, "add works" + orderMap.count())
            } else {
                totalPrice -= price.toFloat()
                setTotalPrice("AED " + totalPrice.toString())
                addOrRemove(vistaId, subVistaId, count, price, name)
            }


        } else {
            if (type.contentEquals("add")) {
                val map: HashMap<String, UserSelectedItem> = HashMap()
                val item: UserSelectedItem =
                    UserSelectedItem(vistaId, subVistaId, count, price, name)
                map.put(subVistaId, item)
                orderMap.put(vistaId, map)
                if (!orderLayot.isShown) {
                    orderLayot.visibility = VISIBLE
                }
                orderList.add(item)
                totalPrice += price.toFloat()
                setTotalPrice("AED " + totalPrice.toString())

                if (orderAdapter == null) {
                    setAdapter()
                }
            }

        }

    }


    private fun setTotalPrice(price: String) {

        txtTotalPrice.setText(price)
    }

    var totalPrice: Float = 0.0F;

    private fun addOrRemove(
        vistaId: String,
        subVistaId: String,
        count: Int,
        price: String,
        name: String
    ) {

        val map: java.util.HashMap<String, UserSelectedItem>? = orderMap.get(vistaId)

        if (map!!.containsKey(subVistaId)) {
            val userSelectedItem = map.get(subVistaId)
            Log.w(TAG, "count" + count)
            userSelectedItem!!.count = count
            map.put(subVistaId, userSelectedItem)
            orderMap.put(vistaId, map)
            Log.w(TAG, "count" + count)
        } else {
            val userSelectedItem = UserSelectedItem(vistaId, subVistaId, 1, price, name)
            map.put(subVistaId, userSelectedItem)
            orderMap.put(vistaId, map)
            orderList.add(userSelectedItem)


        }

        if (orderAdapter != null) {
            orderAdapter!!.notifyDataSetChanged()
        }

    }


    lateinit var responseData: ResponseData
    private val TAG = "MainActivity"

    @BindView(R.id.viewpager)
    lateinit var viewPager: ViewPager
    @BindView(R.id.tabLayout)
    lateinit var tabLayout: TabLayout
    @BindView(R.id.orderRecyclerview)
    lateinit var orderRecyclerView: RecyclerView

    @BindView(R.id.txtTitle)
    lateinit var txtTitle: TextView

    @BindView(R.id.totalPrice)
    lateinit var txtTotalPrice: TextView

    @BindView(R.id.orderLayout)
    lateinit var orderLayot: View


    val fragmentList = ArrayList<Fragment>()
    val titleList = ArrayList<String>()


    lateinit var comboFragment: CombosFragment


    var orderAdapter: OrderAdapter? = null

    val orderList: ArrayList<UserSelectedItem> = ArrayList()
    fun setAdapter() {

        orderAdapter = OrderAdapter(this, orderList)
        orderRecyclerView.adapter = orderAdapter


    }


   public  val orderMap: HashMap<String, HashMap<String, UserSelectedItem>> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        if (CheckInternet.isConnected) {
            callAPI()
        }
    }


    @OnClick(R.id.totalPrice)
    public fun priceClick() {

        if (orderList.size > 0) {

            if (orderRecyclerView.isShown) {
                orderRecyclerView.visibility = GONE
                txtTitle.visibility = GONE
                txtTotalPrice.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.icarrowup,
                    0
                );
            } else {
                orderRecyclerView.visibility = VISIBLE
                txtTitle.visibility = VISIBLE
                txtTotalPrice.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.icarrowdown,
                    0
                );

            }
        }
    }

    private fun callAPI() {
        val service: ApiService = ApiClient.retrofit().create(ApiService::class.java)
        val apiCall: retrofit2.Call<ResponseData> = service.getData()
        apiCall.enqueue(object : Callback<ResponseData> {
            override fun onFailure(call: retrofit2.Call<ResponseData>, t: Throwable) {
                Log.w(TAG, "error " + t.message)
                t.printStackTrace()

            }

            override fun onResponse(
                call: retrofit2.Call<ResponseData>,
                response: Response<ResponseData>
            ) {

                responseData = response.body()!!


                responseData.let {


                    val FoodList = responseData.FoodList

                    for (fnblist in FoodList) {

                        comboFragment = CombosFragment()
                        fragmentList.add(comboFragment)
                        titleList.add(fnblist.TabName)
                    }

                }


                setUpViewPager()

                Log.w(TAG, "response " + response.body())
            }
        })
    }


    class ViewpagerAapter(
        manager: FragmentManager,
        private val fragmentList: ArrayList<Fragment>,
        private val titleList: ArrayList<String>
    ) :
        FragmentStatePagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {


        override fun getItem(position: Int): Fragment {

            return fragmentList.get(position)
        }

        override fun getCount(): Int {

            return fragmentList.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titleList.get(position)
        }
    }


    private fun setUpViewPager() {
        val pagerAdapter = ViewpagerAapter(supportFragmentManager, fragmentList, titleList)

        viewPager.adapter = pagerAdapter

        tabLayout.setupWithViewPager(viewPager)



        viewPager.currentItem = 0

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {


                if (position == 0) {
                    Log.w(TAG, "on pager scroll selected " + position)

                    val fragment: CombosFragment = fragmentList.get(position) as CombosFragment

                    fragment.changeData(titleList.get(position), responseData.FoodList)
                }

            }

            override fun onPageSelected(position: Int) {

                val fragment: CombosFragment = fragmentList.get(position) as CombosFragment

                fragment.changeData(titleList.get(position), responseData.FoodList)
            }
        })
    }

}
