package com.example.bondoman.services

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Process
import android.util.Log
import android.widget.Toast
//import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.bondoman.BondomanApp
import com.example.bondoman.api.RetrofitClient
import com.example.bondoman.types.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class AuthService : Service() {

    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null
    private lateinit var sessionManager: SessionManager
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    private inner class ServiceHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            coroutineScope.launch {
                while (true) {
                    val token = sessionManager.getToken()

                    if (token == null) {
                        Logger.log("AUTH SERVICE", "From service ${msg.arg1} thread ${Thread.currentThread()}: No token found")
                        Logger.log("AUTH SERVICE", "Stopping service !!!")
                        stopSelf()
                    }

                    try {
                        val response = RetrofitClient.authInstance.authToken("Bearer $token")

                        if (response.code() == 200) {
                            Logger.log("AUTH SERVICE", "From service ${msg.arg1} thread ${Thread.currentThread()}: Token authorized")
                        } else if (response.code() == 401) {
                            sessionManager.clearToken()

                            val intent = Intent(BondomanApp.ACTION_UNAUTHORIZED)
                            LocalBroadcastManager.getInstance(applicationContext)
                                .sendBroadcast(intent)

                            Logger.log("AUTH SERVICE", "From service ${msg.arg1} thread ${Thread.currentThread()}: Token unauthorized. Removed from session.")
                            Logger.log("AUTH SERVICE", "Stopping service !!!")
                            stopSelf()
                        } // ELSE: internal server error
                    } catch (_: Exception) {
                        // server error
                    }

                    delay(BondomanApp.JWT_CHECK_INTERVAL)
                }
            }
        }
    }


    override fun onCreate() {
        Logger.log("AUTH SERVICE", "Service started")
        sessionManager = SessionManager(applicationContext)

        HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND).apply {
            start()
            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        serviceHandler?.obtainMessage()?.also { msg ->
            msg.arg1 = startId
            serviceHandler?.sendMessage(msg)
        }

        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Logger.log("AUTH SERVICE", "onTaskRemoved")
        stopSelf()
    }

    override fun onDestroy() {
        Logger.log("AUTH SERVICE", "Service destroyed")
        coroutineScope.cancel()
        super.onDestroy()
    }
}