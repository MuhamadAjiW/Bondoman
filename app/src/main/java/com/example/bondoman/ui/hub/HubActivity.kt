package com.example.bondoman.ui.hub

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewbinding.ViewBinding
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
    private lateinit var viewBinding: ViewBinding
    lateinit var transactionViewModel: TransactionViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize database
        val database = AppDatabase.getInstance(this)
        val transactionRepo = TransactionRepository(database.transactionDao)
        val transactionModelFactory = TransactionViewModelFactory(transactionRepo)
        transactionViewModel =
            ViewModelProvider(this, transactionModelFactory)[TransactionViewModel::class.java]

        // Initialize navbar and fragments
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            viewBinding = ActivityHubLandscapeBinding.inflate(layoutInflater)
            setContentView(viewBinding.root)

            // Initialize header
            (viewBinding as ActivityHubLandscapeBinding).headerContentLandscape.navBackButton.visibility = View.GONE

            val navView: NavigationView = (viewBinding as ActivityHubLandscapeBinding).navViewLandscape
            val navController = findNavController(R.id.nav_host_fragment_activity_main)
            navView.setupWithNavController(navController)
        } else {
            viewBinding = ActivityHubBinding.inflate(layoutInflater)
            setContentView(viewBinding.root)

            // Initialize header
            (viewBinding as ActivityHubBinding).headerContent.navBackButton.visibility = View.GONE

            val navView: BottomNavigationView = (viewBinding as ActivityHubBinding).navView
            val navController = findNavController(R.id.nav_host_fragment_activity_main)
            navView.setupWithNavController(navController)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}