package com.example.bondoman.ui.hub

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bondoman.R
import com.example.bondoman.database.AppDatabase
import com.example.bondoman.database.repository.TransactionRepository
import com.example.bondoman.databinding.ActivityHubBinding
import com.example.bondoman.databinding.ActivityHubLandscapeBinding
import com.example.bondoman.viewmodel.transaction.TransactionViewModel
import com.example.bondoman.viewmodel.transaction.TransactionViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class HubActivity : AppCompatActivity() {
    private lateinit var portrait_binding: ActivityHubBinding
    private lateinit var landscape_binding: ActivityHubLandscapeBinding
    lateinit var transactionViewModel: TransactionViewModel
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        // Initialize database
        val database = AppDatabase.getInstance(this)
        val transactionRepo = TransactionRepository(database.transactionDao)
        val transactionModelFactory = TransactionViewModelFactory(transactionRepo)
        transactionViewModel = ViewModelProvider(this, transactionModelFactory)[TransactionViewModel::class.java]

        // Initialize navbar and fragments
        val orientation = resources.configuration.orientation
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            landscape_binding = ActivityHubLandscapeBinding.inflate(layoutInflater)
            setContentView(landscape_binding.root)

            // Initialize header
            landscape_binding.headerContentLandscape.navBackButton.visibility = View.GONE


            val navView: NavigationView = landscape_binding.navViewLandscape
            val navController = findNavController(R.id.nav_host_fragment_activity_main)
            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.hub_nav_transaction,
                    R.id.hub_nav_scan,
                    R.id.hub_nav_stats,
                    R.id.hub_nav_settings,
                )
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)
        }
        else{
            portrait_binding = ActivityHubBinding.inflate(layoutInflater)
            setContentView(portrait_binding.root)

            // Initialize header
            portrait_binding.headerContent.navBackButton.visibility = View.GONE


            val navView: BottomNavigationView = portrait_binding.navView
            val navController = findNavController(R.id.nav_host_fragment_activity_main)
            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.hub_nav_transaction,
                    R.id.hub_nav_scan,
                    R.id.hub_nav_stats,
                    R.id.hub_nav_settings,
                )
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}