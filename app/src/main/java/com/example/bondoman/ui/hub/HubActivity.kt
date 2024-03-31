package com.example.bondoman.ui.hub

import android.content.IntentFilter
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bondoman.BondomanApp
import com.example.bondoman.R
import com.example.bondoman.database.AppDatabase
import com.example.bondoman.database.repository.TransactionRepository
import com.example.bondoman.databinding.ActivityHubBinding
import com.example.bondoman.viewmodel.transaction.LocationViewModel
import com.example.bondoman.viewmodel.transaction.LocationViewModelFactory
import com.example.bondoman.viewmodel.transaction.TransactionViewModel
import com.example.bondoman.viewmodel.transaction.TransactionViewModelFactory

class HubActivity : AppCompatActivity() {
    private val randomReceiver = RandomBroadcastReceiver(supportFragmentManager)

    lateinit var binding: ActivityHubBinding
    lateinit var transactionViewModel: TransactionViewModel
    lateinit var locationViewModel: LocationViewModel
    var configurationLock: Boolean = false
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


        // Setup broadcast receiver
        val filter = IntentFilter(BondomanApp.ACTION_RANDOM_TRANSACTION)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(randomReceiver, filter, RECEIVER_EXPORTED)
        } else {
            registerReceiver(randomReceiver, filter)
        }
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
}