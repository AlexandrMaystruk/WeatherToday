package com.gmail.maystruks08.whatweathernow.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import javax.inject.Inject

class NetworkUtil @Inject constructor(
    private val context: Context
) {

    fun isNetworkTurnedOn(): Boolean {
        val conn = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = conn.activeNetworkInfo
        return networkInfo?.isConnected == true
    }


}