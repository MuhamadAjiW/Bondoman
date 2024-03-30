package com.example.bondoman.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.bondoman.ui.hub.HubActivity
import com.example.bondoman.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginButton = binding.loginButton
        loginButton.setOnClickListener(::onLoginClick)
    }

    //TODO: Implement login
    private fun onLoginClick(view: View) {
        val intent = Intent(this, HubActivity::class.java)
        startActivity(intent)
        finish()
    }
}