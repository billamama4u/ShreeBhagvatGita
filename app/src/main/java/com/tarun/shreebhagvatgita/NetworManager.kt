package com.tarun.shreebhagvatgita

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LiveData

class NetworManager(context: Context) : LiveData<Boolean>() {

    private val checkConnectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val networkRequest = NetworkRequest.Builder().apply {
        addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
        addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
    }.build()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            postValue(true)
        }

        override fun onUnavailable() {
            postValue(false)
        }

        override fun onLost(network: Network) {
            postValue(false)
        }
    }

    private var isCallbackRegistered = false

    override fun onActive() {
        super.onActive()
        checkInternet()
    }

    override fun onInactive() {
        super.onInactive()
        releaseCheckingInternet()
    }

    private fun checkInternet() {
        if (checkConnectivity.activeNetwork == null) {
            postValue(false)
            checkConnectivity.registerNetworkCallback(networkRequest, networkCallback)
            isCallbackRegistered = true
        } else {
            postValue(true)
        }
    }

    private fun releaseCheckingInternet() {
        if (isCallbackRegistered) {
            checkConnectivity.unregisterNetworkCallback(networkCallback)
            isCallbackRegistered = false
        }
    }
}
