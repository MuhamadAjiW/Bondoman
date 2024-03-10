package com.example.bondoman.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.bondoman.R
import com.example.bondoman.databinding.ActivityHubBinding
import com.example.bondoman.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

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