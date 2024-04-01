package com.example.bondoman

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.bondoman.services.AuthService
import com.example.bondoman.services.SessionManager
import com.example.bondoman.ui.hub.HubActivity
import com.example.bondoman.ui.login.LoginActivity
import com.example.bondoman.viewmodel.auth.AuthViewModel
import com.example.bondoman.viewmodel.auth.AuthViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var authViewModel: AuthViewModel
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startService(Intent(this, AuthService::class.java))

        sessionManager = SessionManager(this)

        val authViewModelFactory = AuthViewModelFactory()
        authViewModel = ViewModelProvider(this, authViewModelFactory)[AuthViewModel::class.java]

        authViewModel.isAuthorized.observe(this) {
            it?.let{
                val intent = if (it) {
                    Intent(this, HubActivity::class.java)
                } else {
                    Intent(this, LoginActivity::class.java)
                }

                startActivity(intent)
                finish()
            }
        }

        authViewModel.removeToken.observe(this) {
            if (it) sessionManager.clearToken()
        }
    }

    override fun onResume() {
        super.onResume()
        val token = sessionManager.getToken()
        authViewModel.validate(token)
    }
}
