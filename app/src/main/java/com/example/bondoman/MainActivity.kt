package com.example.bondoman

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.bondoman.services.AuthService
import com.example.bondoman.services.SessionManager
import com.example.bondoman.types.util.Logger
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

        sessionManager = SessionManager(this)

        val authViewModelFactory = AuthViewModelFactory()
        authViewModel = ViewModelProvider(this, authViewModelFactory)[AuthViewModel::class.java]

        authViewModel.isAuthorized.observe(this) {
            // observer to "redirect" user
            it?.let{
                if (it) {
                    Logger.log("MAIN ACTIVITY: AUTH", "View model authorized")
                    // start service
                    startService(Intent(this, AuthService::class.java))

                    val intent = Intent(this, HubActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Logger.log("MAIN ACTIVITY: AUTH", "View model unauthorized")
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        authViewModel.isConnectionTimeout.observe(this) {
            if (it) {
                Logger.log("MAIN ACTIVITY: AUTH", "View model connection timeout")
                // start service
                startService(Intent(this, AuthService::class.java))

                val intent = Intent(this, HubActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        authViewModel.errMessage.observe(this) {
            // observer for error message
            if (it.isNotEmpty()) {
                Logger.log("MAIN ACTIVITY: AUTH", "View model error")
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            } else {
                // an error occurred

            }
        }

        authViewModel.removeToken.observe(this) {
            // observer for removing token
            if (it){
                sessionManager.clearToken()
                Logger.log("MAIN ACTIVITY: AUTH", "View model clear token")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val token = sessionManager.getToken()
        Logger.log("MAIN ACTIVITY: AUTH", "View model validating token")
        authViewModel.validate(token, true)
    }
}
