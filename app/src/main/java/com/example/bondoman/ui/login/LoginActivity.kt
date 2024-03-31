package com.example.bondoman.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.bondoman.ui.hub.HubActivity
import com.example.bondoman.databinding.ActivityLoginBinding
import com.example.bondoman.models.Credential
import com.example.bondoman.services.SessionManager
import com.example.bondoman.viewmodel.login.AuthViewModel
import com.example.bondoman.viewmodel.login.AuthViewModelFactory
import com.example.bondoman.viewmodel.login.LoginViewModel
import com.example.bondoman.viewmodel.login.LoginViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginButton = binding.loginButton
        loginButton.setOnClickListener(::onLoginClick)

        sessionManager = SessionManager(this)
        val loginViewModelFactory = LoginViewModelFactory()
        loginViewModel = ViewModelProvider(this, loginViewModelFactory)[LoginViewModel::class.java]
        val authViewModelFactory = AuthViewModelFactory()
        authViewModel = ViewModelProvider(this, authViewModelFactory)[AuthViewModel::class.java]

        authViewModel.isAuthorized.observe(this) {
            if (it) {
                navigateToHub()
            }
        }

        authViewModel.removeToken.observe(this) {
            if (it) {
                Log.d("bondoman cuy", "removing token")
                sessionManager.clearToken()
            }
        }

        loginViewModel.loginToken.observe(this) {
            if (it != null) {
                sessionManager.saveToken(it)
                Log.d("bondoman cuy", "saving token")
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
        Log.d("bondoman cuy", "OnResume Called")
        val token = sessionManager.getToken()
        token?.let {
            authViewModel.validate(token)
        }
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
        val intent = Intent(this, HubActivity::class.java)
        startActivity(intent)
        finish()
    }
}