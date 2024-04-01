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
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.bondoman.BondomanApp
import com.example.bondoman.api.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AuthService : Service() {

    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null
    private lateinit var sessionManager: SessionManager
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    companion object {
        const val INTERVAL: Long = 60000
    }

    private inner class ServiceHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            validate()
        }

        private fun validate() {
            coroutineScope.launch {
                while(true) {
                    task()
                    delay(INTERVAL)
                }
            }
        }

        private suspend fun task() {
            Log.d("bg task", Thread.currentThread().toString())
            val token = sessionManager.getToken() ?: return

            try {
                val response = RetrofitClient.authInstance.authToken("Bearer $token")

                if (response.code() == 200) {
                    val intent = Intent(BondomanApp.ACTION_AUTHORIZED)
                    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                } else if (response.code() == 401) {
                    sessionManager.clearToken()
                    val intent = Intent(BondomanApp.ACTION_UNAUTHORIZED)
                    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                }
            } catch (_: Exception) { }
        }
    }


    override fun onCreate() {
        Toast.makeText(this, "service created", Toast.LENGTH_SHORT).show()
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

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}