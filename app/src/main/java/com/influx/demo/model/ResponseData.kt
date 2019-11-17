package com.influx.demo.model


data class ResponseData(
    val status: Status,
    val Currency: String,
    val FoodList: ArrayList<FoodList>
) {


}