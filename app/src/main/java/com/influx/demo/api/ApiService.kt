package com.influx.demo.api

import com.influx.demo.model.ResponseData
import kotlinx.coroutines.Deferred
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET

interface ApiService {


    @GET("v2/5b700cff2e00005c009365cf")
     fun getData(): Call<ResponseData>
}