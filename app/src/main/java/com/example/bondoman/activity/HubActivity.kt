package com.example.bondoman.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bondoman.R

class HubActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_hub)
    }
}