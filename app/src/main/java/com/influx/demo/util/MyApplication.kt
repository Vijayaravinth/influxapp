package com.influx.demo.util

import android.app.Application
import android.content.Context

/**
 * Created by w7 on 11/5/2017.
 */
 class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        instance = this
    }

    override fun attachBaseContext(newBase: Context) {

        super.attachBaseContext(newBase)
        // MultiDex.install(this)
    }

    fun setConnectivityListener(listener: CheckInternet.ConnectivityReceiverListener) {
        var connectivityReceiverListener: CheckInternet.ConnectivityReceiverListener = listener
    }

    companion object {
        @get:Synchronized
        var instance: MyApplication? = null
            private set

        annotation class Synchronized


    }
}