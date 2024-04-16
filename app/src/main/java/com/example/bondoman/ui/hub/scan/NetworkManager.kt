package com.example.bondoman.ui.hub.scan

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.example.bondoman.viewmodel.scan.ScanViewModel

class NetworkManager(private val context: Context, private val viewModel: ScanViewModel) :
    ConnectivityManager.NetworkCallback() {
    private val connectivityManager: ConnectivityManager by lazy {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private fun isConnected(): Boolean = connectivityManager.activeNetworkInfo != null

    private fun updateViewModel() {
        viewModel.isCameraBtnEnabled.postValue(isConnected())
        viewModel.isSelectBtnEnabled.postValue(isConnected())
        viewModel.showSnackbar.postValue(!isConnected())
    }

    fun activate() {
        updateViewModel()

        connectivityManager.registerNetworkCallback(
            NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI).build(), this
        )
    }

    fun deactivate() {
        connectivityManager.unregisterNetworkCallback(this)
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        updateViewModel()
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        updateViewModel()
    }
}