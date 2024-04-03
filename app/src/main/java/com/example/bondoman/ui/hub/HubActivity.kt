package com.example.bondoman.ui.hub

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.bondoman.BondomanApp
import com.example.bondoman.R
import com.example.bondoman.database.AppDatabase
import com.example.bondoman.database.repository.TransactionRepository
import com.example.bondoman.databinding.ActivityHubBinding
import com.example.bondoman.services.AuthService
import com.example.bondoman.services.SessionManager
import com.example.bondoman.types.util.Logger
import com.example.bondoman.ui.login.LoginActivity
import com.example.bondoman.viewmodel.auth.AuthViewModel
import com.example.bondoman.viewmodel.auth.AuthViewModelFactory
import com.example.bondoman.viewmodel.transaction.LocationViewModel
import com.example.bondoman.viewmodel.transaction.LocationViewModelFactory
import com.example.bondoman.viewmodel.transaction.TransactionViewModel
import com.example.bondoman.viewmodel.transaction.TransactionViewModelFactory

class HubActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHubBinding
    lateinit var transactionViewModel: TransactionViewModel
    private lateinit var locationViewModel: LocationViewModel
    private var configurationLock: Boolean = false

    private lateinit var authViewModel: AuthViewModel
    private lateinit var sessionManager: SessionManager

    private var broadcastReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Logger.log("HUB ACTIVITY: AUTH", "Broadcast received")
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
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
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

        // auth check
        sessionManager = SessionManager(this)

        val authViewModelFactory = AuthViewModelFactory()
        authViewModel = ViewModelProvider(this, authViewModelFactory)[AuthViewModel::class.java]

        authViewModel.isAuthorized.observe(this) {
            // observer to "redirect" user
            it?.let{
                if (!it) {
                    Logger.log("HUB ACTIVITY: AUTH", "View model unauthorized")
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        authViewModel.removeToken.observe(this) {
            // observer for removing token
            if (it) {
                sessionManager.clearToken()
                Logger.log("HUB ACTIVITY: AUTH", "View model removing token")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val token = sessionManager.getToken()
        Logger.log("HUB ACTIVITY: AUTH", "View model validate token")
        authViewModel.validate(token, true)

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
        Toast.makeText(
            this,
            "Session expired",
            Toast.LENGTH_SHORT
        ).show()

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}