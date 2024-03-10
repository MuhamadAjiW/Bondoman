package com.example.bondoman.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.bondoman.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_login)

        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener(::onLoginClick)
    }

    //TODO: Implement login
    private fun onLoginClick(view: View) {
        val intent = Intent(this, HubActivity::class.java)
        startActivity(intent)
        finish()
    }
}