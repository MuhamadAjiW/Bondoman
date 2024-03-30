package com.example.bondoman

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bondoman.ui.hub.HubActivity
import com.example.bondoman.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        splashScreen()

        val intent: Intent = if(loginCheck()){
            Intent(this, HubActivity::class.java)
        } else{
            Intent(this, LoginActivity::class.java)
        }

        startActivity(intent)
        finish()
    }

    //TODO: Implement Splash screen
    private fun splashScreen(){
        println("Splash screen")
    }

    //TODO: Implement Login / network mechanism
    private fun loginCheck() : Boolean {
        println("Login check")
        return false
    }
}
