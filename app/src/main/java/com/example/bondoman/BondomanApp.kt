package com.example.bondoman

import android.app.Application
import android.content.IntentFilter
import com.example.bondoman.database.AppDatabase
import com.example.bondoman.ui.transaction.RandomBroadcastReceiver

class BondomanApp : Application() {
    private val randomReceiver = RandomBroadcastReceiver()

    override fun onCreate() {
        super.onCreate()
        AppDatabase.getInstance(this)

        val filter = IntentFilter(ACTION_RANDOM_TRANSACTION)
        registerReceiver(randomReceiver, filter)
    }

    // Global statics should be here
    companion object{
        val ACTION_RANDOM_TRANSACTION = "com.example.bondoman.ACTION_RANDOM_TRANSACTION"
    }
}