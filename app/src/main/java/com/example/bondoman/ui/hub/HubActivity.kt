package com.example.bondoman.ui.hub

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bondoman.BondomanApp
import com.example.bondoman.R
import com.example.bondoman.database.AppDatabase
import com.example.bondoman.database.repository.TransactionRepository
import com.example.bondoman.databinding.ActivityHubBinding
import com.example.bondoman.ui.login.LoginActivity
import com.example.bondoman.viewmodel.transaction.LocationViewModel
import com.example.bondoman.viewmodel.transaction.LocationViewModelFactory
import com.example.bondoman.viewmodel.transaction.TransactionViewModel
import com.example.bondoman.viewmodel.transaction.TransactionViewModelFactory

class HubActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHubBinding
    lateinit var transactionViewModel: TransactionViewModel
    private lateinit var locationViewModel: LocationViewModel
    private var configurationLock: Boolean = false

    private var broadcastReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            navigateToLogin()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize database and viewmodels
        val database = AppDatabase.getInstance(this)
        val transactionRepo = TransactionRepository(database.transactionDao)
        val transactionModelFactory = TransactionViewModelFactory(transactionRepo)
        transactionViewModel = ViewModelProvider(this, transactionModelFactory)[TransactionViewModel::class.java]
        val locationModelFactory = LocationViewModelFactory()
        locationViewModel = ViewModelProvider(this, locationModelFactory)[LocationViewModel::class.java]

        // Initialize binding
        binding = ActivityHubBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize header
        binding.headerContent.navBackButton.visibility = View.GONE
        binding.headerContent.navBackButton.setOnClickListener(::onBackClick)

        // Initialize navbars
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        binding.navView.setupWithNavController(navController)
        binding.navViewLandscape.setupWithNavController(navController)

        // Setup orientation
        configurationLock = false
        val orientation = resources.configuration.orientation

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.navView.visibility = View.GONE
            binding.navViewLandscape.visibility = View.VISIBLE
        } else {
            binding.navViewLandscape.visibility = View.GONE
            binding.navView.visibility = View.VISIBLE
        }


    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(
                broadcastReceiver,
                IntentFilter(BondomanApp.ACTION_UNAUTHORIZED))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if(!configurationLock){
            if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
                binding.navView.visibility = View.GONE
                binding.navViewLandscape.visibility = View.VISIBLE
            } else {
                binding.navViewLandscape.visibility = View.GONE
                binding.navView.visibility = View.VISIBLE
            }
        }
    }

    private fun onBackClick(view: View) {
        onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}