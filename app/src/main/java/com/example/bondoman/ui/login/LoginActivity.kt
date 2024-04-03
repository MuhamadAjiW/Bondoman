package com.example.bondoman.ui.login

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.bondoman.BondomanApp
import com.example.bondoman.ui.hub.HubActivity
import com.example.bondoman.databinding.ActivityLoginBinding
import com.example.bondoman.models.Credential
import com.example.bondoman.services.AuthService
import com.example.bondoman.services.SessionManager
import com.example.bondoman.types.util.Logger
import com.example.bondoman.viewmodel.auth.LoginViewModel
import com.example.bondoman.viewmodel.auth.LoginViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var loginViewModel: LoginViewModel

    private var broadcastReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            navigateToHub()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginButton = binding.loginButton
        loginButton.setOnClickListener(::onLoginClick)

        sessionManager = SessionManager(this)
        val loginViewModelFactory = LoginViewModelFactory()
        loginViewModel = ViewModelProvider(this, loginViewModelFactory)[LoginViewModel::class.java]


        loginViewModel.loginToken.observe(this) {
            if (it != null) {
                sessionManager.saveToken(it)
                navigateToHub()
            }
        }

        loginViewModel.loginMessage.observe(this) {
            if (it.isNotEmpty()) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(
                broadcastReceiver,
                IntentFilter(BondomanApp.ACTION_AUTHORIZED)
            )
    }

    private fun onLoginClick(view: View) {
        val email = binding.emailLabel.text.toString()
        val password = binding.passwordInput.text.toString()
        val credential = Credential(email, password)

        if (!credential.validateEmail()) {
            Toast.makeText(this, "Email is empty or invalid", Toast.LENGTH_SHORT).show()
            return
        }

        if (!credential.validatePassword()) {
            Toast.makeText(this, "Password length must be > 7", Toast.LENGTH_SHORT).show()
            return
        }

        loginViewModel.loginUser(credential)
    }

    private fun navigateToHub() {
        Logger.log("LOGIN ACTIVITY: AUTH", "View model authorized")
        // start service
        startService(Intent(this, AuthService::class.java))

        val intent = Intent(this, HubActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
    }
}