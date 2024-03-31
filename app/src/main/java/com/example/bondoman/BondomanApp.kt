package com.example.bondoman

import android.app.Application
import com.example.bondoman.database.AppDatabase

class BondomanApp : Application() {

    override fun onCreate() {
        super.onCreate()
        AppDatabase.getInstance(this)
    }

    // Global statics should be here
    companion object{
        const val ACTION_RANDOM_TRANSACTION = "com.example.bondoman.ACTION_RANDOM_TRANSACTION"
        const val LOCATION_MARK: Double = 200.0
    }
}