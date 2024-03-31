package com.example.bondoman.ui.hub.addtransaction

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bondoman.R
import com.example.bondoman.database.AppDatabase
import com.example.bondoman.database.entity.TransactionEntity
import com.example.bondoman.database.repository.TransactionRepository
import com.example.bondoman.databinding.ActivityTransactionBinding
import com.example.bondoman.databinding.FragmentAddTransactionBinding
import com.example.bondoman.viewmodel.transaction.LocationViewModel
import com.example.bondoman.viewmodel.transaction.LocationViewModelFactory
import com.example.bondoman.viewmodel.transaction.TransactionViewModel
import com.example.bondoman.viewmodel.transaction.TransactionViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddTransactionFragment : Fragment() {
    private lateinit var binding: FragmentAddTransactionBinding
    private lateinit var transactionViewModel: TransactionViewModel
    private var actionCode: Int = 0
    private var transactionId: Int = 0

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationViewModel: LocationViewModel
    private var savedLat: Double? = null
    private var savedLng: Double? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentAddTransactionBinding.inflate(layoutInflater)

        return binding.root
    }
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
//
//        binding = ActivityTransactionBinding.inflate(layoutInflater)
//
//        // Initialize database
//        val database = AppDatabase.getInstance(this)
//        val transactionRepo = TransactionRepository(database.transactionDao)
//        val transactionModelFactory = TransactionViewModelFactory(transactionRepo)
//        transactionViewModel = ViewModelProvider(this, transactionModelFactory)[TransactionViewModel::class.java]
//
//        // Location VM
//        val locationModelFactory = LocationViewModelFactory()
//        locationViewModel = ViewModelProvider(this, locationModelFactory)[LocationViewModel::class.java]
//        locationViewModel.location.observe(this) {
//            observeLocation(it)
//        }
//
//        // Initialize header
//        binding.header.navTitle.text = getString(R.string.hub_nav_transaction)
//        val backButton = binding.header.navBackButton
//        backButton.setOnClickListener(::onBackClick)
//
//        // Initialize category dropdown
//        val spinner = binding.categoryInput
//        spinner.setSelection(0, true);
//        (spinner.selectedView as TextView).setTextColor(ContextCompat.getColor(this, R.color.black))
//
//        // Locate button
//        val locateButton = binding.btnLocate
//        locateButton.setOnClickListener(::onLocateClick)
//
//        // Delete button
//        val deleteButton = binding.btnDelete
//        deleteButton.setOnClickListener(::onDeleteClick)
//
//        // Initialize category dropdown
//        val submitButton = binding.submitButton
//        backButton.setOnClickListener(::onBackClick)
//        submitButton.setOnClickListener(::onSubmitClick)
//
//        // Initialize initial values
//        val titleInitial = intent.getStringExtra(KEY_TITLE)
//        val amountInitial = intent.getIntExtra(KEY_AMOUNT, 0)
//        val categoryInitial = intent.getIntExtra(KEY_CATEGORY, 0)
//        var locationInitial = intent.getStringExtra(KEY_LOCATION)
//
//        binding.titleInput.setText(titleInitial)
//        binding.amountInput.setText(amountInitial.toString())
//        binding.categoryInput.setSelection(categoryInitial, true)
//        binding.locationText.text = locationInitial
//
//        // Initialize category dropdown color
//        (binding.categoryInput.selectedView as TextView).setTextColor(ContextCompat.getColor(this, R.color.black))
//
//        actionCode = intent.getIntExtra(KEY_ACTION, 0)
//        transactionId = intent.getIntExtra(KEY_TRANSACTION_ID, 0)
//
//        if(actionCode == ACTION_EDIT) binding.categoryInput.isEnabled = false
//
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun onDeleteClick(view: View) {
//        locationViewModel.setLoc(null, null)
    }

    private fun onLocateClick(view: View) {
//        getLastLocation()
    }

    private fun getLastLocation() {
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(this,
//                arrayOf(
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                ),
//                1
//            )
//
//            if (ActivityCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED) {
//
//                return
//            }
//        }
//
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                arrayOf(
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                ),
//                2
//            )
//        }
//
//        fusedLocationClient.lastLocation
//            .addOnSuccessListener { location: Location? ->
//                locationViewModel.setLoc(location?.latitude, location?.longitude)
//            }
    }

    private fun observeLocation(loc: Pair<Double?, Double?>) {
//        val (lat, lng) = loc
//        savedLat = lat
//        savedLng = lng
//
//        if (lat != null && lng != null) {
//            binding.locationText.text = loc.toString()
//        } else {
//            binding.locationText.text = getString(R.string.no_location_data)
//        }
    }

    // Header back button
    private fun onBackClick(view: View) {
//        requireActivity().onBackPressed()
    }

    private fun onSubmitClick(view: View){
//        val title = binding.titleInput.text.toString()
//        val category = binding.categoryInput.selectedItem.toString()
//        val amount = binding.amountInput.text.toString()
//
//        if (title.isEmpty()){
//            Toast.makeText(requireContext(), getString(R.string.transaction_add_toast_error_title), Toast.LENGTH_SHORT).show()
//        }
//        else if (amount.isEmpty()){
//            Toast.makeText(requireContext(), getString(R.string.transaction_add_toast_error_amount), Toast.LENGTH_SHORT).show()
//        }
//        else{
//            when (actionCode){
//                ACTION_ADD -> {
//                    transactionViewModel.insert(
//                        TransactionEntity(
//                            id = 0,
//                            title = title,
//                            category = category,
//                            amount = amount.toInt(),
//                            timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(
//                                Date()
//                            ),
//                            latitude = savedLat,
//                            longitude = savedLng
//                        )
//                    )
//                    Toast.makeText(requireContext(), getString(R.string.transaction_add_toast_success), Toast.LENGTH_SHORT).show()
//                }
//
//                ACTION_EDIT -> {
//                    transactionViewModel.update(
//                        TransactionEntity(
//                            id = intent.getIntExtra(KEY_TRANSACTION_ID, 0),
//                            title = title,
//                            category = category,
//                            amount = amount.toInt(),
//                            timestamp = intent.getStringExtra(KEY_TIMESTAMP)!!,
//                            latitude = savedLat,
//                            longitude = savedLng
//                        )
//                    )
//                    Toast.makeText(requireContext(), getString(R.string.transaction_edit_toast_success), Toast.LENGTH_SHORT).show()
//                }
//            }
//            onBackPressed()
//        }
    }

    companion object{
        const val KEY_TITLE = "Title"
        const val KEY_AMOUNT = "Amount"
        const val KEY_CATEGORY = "Category"
        const val KEY_LOCATION = "Location"

        const val KEY_ACTION = "Action"
        const val KEY_TRANSACTION_ID = "TransactionId"
        const val KEY_TIMESTAMP = "Timestamp"

        const val ACTION_ADD = 0
        const val ACTION_EDIT = 1

        const val CATEGORY_INCOME = 0
        const val CATEGORY_EXPENSES = 1
    }
}