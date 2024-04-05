package com.example.bondoman

import android.app.Application
import android.content.IntentFilter
import android.os.Build
import com.example.bondoman.database.AppDatabase

class BondomanApp : Application() {
    private val randomReceiver = RandomBroadcastReceiver()

    override fun onCreate() {
        super.onCreate()
        AppDatabase.getInstance(this)

        val filter = IntentFilter(ACTION_RANDOM_TRANSACTION)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(randomReceiver, filter, RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(randomReceiver, filter)
        }
    }

    // Global statics should be here
    companion object{
        const val ACTION_RANDOM_TRANSACTION = "com.example.bondoman.ACTION_RANDOM_TRANSACTION"
        const val ACTION_AUTHORIZED = "com.example.bondoman.ACTION_AUTHORIZED"
        const val ACTION_UNAUTHORIZED = "com.example.bondoman.ACTION_UNAUTHORIZED"
        const val LOCATION_MARK: Double = 200.0
        const val JWT_CHECK_INTERVAL: Long = 60000 * 2
        const val LOGGER = true
    }
}