package com.example.bondoman.ui.transaction

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bondoman.R
import com.example.bondoman.databinding.ActivityHubBinding
import com.example.bondoman.databinding.ActivityTransactionBinding
import com.example.bondoman.ui.hub.HubActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class TransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransactionBinding
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.navTitle.text = getString(R.string.hub_nav_transaction)
        val backButton = binding.header.navBackButton
        backButton.setOnClickListener(::onBackClick)

        val spinner = binding.categoryInput
        val adapter = ArrayAdapter.createFromResource(this, R.array.category_choices, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun onBackClick(view: View) {
        onBackPressed()
    }
}