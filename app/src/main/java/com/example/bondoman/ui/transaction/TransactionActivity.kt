package com.example.bondoman.ui.transaction

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.bondoman.BondomanApp
import com.example.bondoman.R
import com.example.bondoman.database.AppDatabase
import com.example.bondoman.database.entity.TransactionEntity
import com.example.bondoman.database.repository.TransactionRepository
import com.example.bondoman.databinding.ActivityTransactionBinding
import com.example.bondoman.viewmodel.transaction.LocationViewModel
import com.example.bondoman.viewmodel.transaction.LocationViewModelFactory
import com.example.bondoman.viewmodel.transaction.TransactionViewModel
import com.example.bondoman.viewmodel.transaction.TransactionViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransactionBinding
    private lateinit var transactionViewModel: TransactionViewModel
    private var actionCode: Int = 0
    private var transactionId: Int = 0

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationViewModel: LocationViewModel
    private var savedLat: Double? = null
    private var savedLng: Double? = null

    private lateinit var geocoder: Geocoder

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize database
        val database = AppDatabase.getInstance(this)
        val transactionRepo = TransactionRepository(database.transactionDao)
        val transactionModelFactory = TransactionViewModelFactory(transactionRepo)
        transactionViewModel = ViewModelProvider(this, transactionModelFactory)[TransactionViewModel::class.java]

        // Initialize geocoder
        geocoder = Geocoder(this, Locale("id", "ID"))

        // Location VM
        val locationModelFactory = LocationViewModelFactory()
        locationViewModel = ViewModelProvider(this, locationModelFactory)[LocationViewModel::class.java]
        locationViewModel.location.observe(this) {
            observeLocation(it)
        }

        // Initialize header
        binding.header.navTitle.text = getString(R.string.hub_nav_transaction)
        binding.header.navBackButton.setOnClickListener(::onBackClick)

        // Locate button
        val locateButton = binding.btnLocate
        locateButton.setOnClickListener(::onLocateClick)

        // Delete button
        val deleteButton = binding.btnDelete
        deleteButton.setOnClickListener(::onDeleteClick)

        // Submit button
        val submitButton = binding.submitButton
        submitButton.setOnClickListener(::onSubmitClick)

        // Initialize initial values
        val titleInitial = intent.getStringExtra(KEY_TITLE)
        val amountInitial = intent.getIntExtra(KEY_AMOUNT, 0)
        val categoryInitial = intent.getIntExtra(KEY_CATEGORY, 0)
        var latInitial: Double? = intent.getDoubleExtra(KEY_LATITUDE, BondomanApp.LOCATION_MARK)
        var lngInitial: Double? = intent.getDoubleExtra(KEY_LONGITUDE, BondomanApp.LOCATION_MARK)
        if (latInitial == BondomanApp.LOCATION_MARK || lngInitial == BondomanApp.LOCATION_MARK) {
            latInitial = null
            lngInitial = null
        }

        binding.titleInput.setText(titleInitial)
        binding.amountInput.setText(amountInitial.toString())
        binding.categoryInput.setSelection(categoryInitial, true)
        locationViewModel.setLoc(latInitial, lngInitial)

        // Initialize category dropdown color
        (binding.categoryInput.selectedView as TextView).setTextColor(ContextCompat.getColor(this, R.color.black))

        actionCode = intent.getIntExtra(KEY_ACTION, 0)
        transactionId = intent.getIntExtra(KEY_TRANSACTION_ID, 0)

        if(actionCode == ACTION_EDIT){
            binding.categoryInput.isEnabled = false
            binding.btnLocate.isEnabled = false
            binding.btnDelete.isEnabled = false

            binding.categoryInput.setBackgroundColor(resources.getColor(R.color.gray_500, theme))
            binding.btnLocate.backgroundTintList = resources.getColorStateList(R.color.gray_500, theme)
            binding.btnDelete.backgroundTintList = resources.getColorStateList(R.color.gray_500, theme)
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun onDeleteClick(view: View) {
        locationViewModel.setLoc(null, null)
    }

    private fun onLocateClick(view: View) {
        getLastLocation()
    }

    private fun onBackClick(view: View) {
        onBackPressed()
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {

                return
            }
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                2
            )
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                locationViewModel.setLoc(location?.latitude, location?.longitude)
            }
    }

    private fun observeLocation(loc: Pair<Double?, Double?>) {
        val (lat, lng) = loc
        savedLat = lat
        savedLng = lng

        if (lat != null && lng != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocation(
                    lat,
                    lng,
                    1,
                    (Geocoder.GeocodeListener {
                        if (it.size > 0) {
                            binding.locationText.text = it[0].locality.toString()
                        }
                    })
                )
            } else {
                binding.locationText.text = loc.toString()
            }
        } else {
            binding.locationText.text = getString(R.string.no_location_data)
        }
    }

    private fun onSubmitClick(view: View){
        val title = binding.titleInput.text.toString()
        val category = binding.categoryInput.selectedItem.toString()
        val amount = binding.amountInput.text.toString()

        if (title.isEmpty()){
            Toast.makeText(this, getString(R.string.transaction_add_toast_error_title), Toast.LENGTH_SHORT).show()
        }
        else if (amount.isEmpty()){
            Toast.makeText(this, getString(R.string.transaction_add_toast_error_amount), Toast.LENGTH_SHORT).show()
        }
        else{
            when (actionCode){
                ACTION_ADD -> {
                    transactionViewModel.insert(
                        TransactionEntity(
                            id = 0,
                            title = title,
                            category = category,
                            amount = amount.toInt(),
                            timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()),
                            latitude = savedLat,
                            longitude = savedLng
                        )
                    )
                    Toast.makeText(this, getString(R.string.transaction_add_toast_success), Toast.LENGTH_SHORT).show()
                }

                ACTION_EDIT -> {
                    transactionViewModel.update(
                        TransactionEntity(
                            id = intent.getIntExtra(KEY_TRANSACTION_ID, 0),
                            title = title,
                            category = category,
                            amount = amount.toInt(),
                            timestamp = intent.getStringExtra(KEY_TIMESTAMP)!!,
                            latitude = savedLat,
                            longitude = savedLng
                        )
                    )
                    Toast.makeText(this, getString(R.string.transaction_edit_toast_success), Toast.LENGTH_SHORT).show()
                }
            }
            onBackPressed()
        }
    }

    companion object{
        const val KEY_TITLE = "Title"
        const val KEY_AMOUNT = "Amount"
        const val KEY_CATEGORY = "Category"
        const val KEY_LATITUDE = "Latitude"
        const val KEY_LONGITUDE = "Longitude"

        const val KEY_ACTION = "Action"
        const val KEY_TRANSACTION_ID = "TransactionId"
        const val KEY_TIMESTAMP = "Timestamp"

        const val ACTION_ADD = 0
        const val ACTION_EDIT = 1

        const val CATEGORY_INCOME = 0
        const val CATEGORY_EXPENSES = 1
    }
}