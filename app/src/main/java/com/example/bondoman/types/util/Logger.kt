package com.example.bondoman.types.util

import android.util.Log
import com.example.bondoman.BondomanApp

class Logger {
    companion object {
        fun log(tag: String, message: String) {
            if (BondomanApp.LOGGER) {
                Log.d(tag, message)
            }
        }
    }
}