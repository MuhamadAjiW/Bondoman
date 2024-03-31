package com.example.bondoman

import android.app.Application
import android.content.IntentFilter
import android.os.Build
import com.example.bondoman.database.AppDatabase
import com.example.bondoman.ui.transaction.RandomBroadcastReceiver

class BondomanApp : Application() {
    private val randomReceiver = RandomBroadcastReceiver()

    override fun onCreate() {
        super.onCreate()
        AppDatabase.getInstance(this)

        val filter = IntentFilter(ACTION_RANDOM_TRANSACTION)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(randomReceiver, filter, RECEIVER_EXPORTED)
        } else {
            registerReceiver(randomReceiver, filter)
        }
    }

    // Global statics should be here
    companion object{
        const val ACTION_RANDOM_TRANSACTION = "com.example.bondoman.ACTION_RANDOM_TRANSACTION"
    }
}