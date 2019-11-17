package com.influx.demo.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {


    fun retrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("http://www.mocky.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()


}