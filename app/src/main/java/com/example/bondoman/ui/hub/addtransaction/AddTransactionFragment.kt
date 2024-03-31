package com.example.bondoman.ui.hub.addtransaction

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
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
import com.example.bondoman.R
import com.example.bondoman.database.entity.TransactionEntity
import com.example.bondoman.databinding.FragmentAddTransactionBinding
import com.example.bondoman.ui.hub.HubActivity
import com.example.bondoman.viewmodel.transaction.LocationViewModel
import com.example.bondoman.viewmodel.transaction.TransactionViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddTransactionFragment : Fragment() {
    private lateinit var binding: FragmentAddTransactionBinding
    private lateinit var hubActivity: HubActivity
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

        // Initialize view
        binding = FragmentAddTransactionBinding.inflate(layoutInflater)
        hubActivity = requireActivity() as HubActivity
        hubActivity.configurationLock = true
        hubActivity.binding.navView.visibility = View.GONE
        hubActivity.binding.navViewLandscape.visibility = View.GONE
        hubActivity.binding.headerContent.navBackButton.visibility = View.VISIBLE

        // Initialize viewmodel
        transactionViewModel = hubActivity.transactionViewModel
        locationViewModel = hubActivity.locationViewModel

        // Observe location
        locationViewModel.location.observe(hubActivity) {
            observeLocation(it)
        }


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
        val titleInitial = arguments?.getString(KEY_TITLE) ?: ""
        val amountInitial = arguments?.getInt(KEY_AMOUNT, 0) ?: 0
        val categoryInitial = arguments?.getInt(KEY_CATEGORY, 0) ?: 0
        var locationInitial = arguments?.getString(KEY_LOCATION) ?: ""

        binding.titleInput.setText(titleInitial)
        binding.amountInput.setText(amountInitial.toString())
        binding.categoryInput.setSelection(categoryInitial, true)
        binding.locationText.text = locationInitial

        // Initialize category dropdown color
        (binding.categoryInput.selectedView as TextView).setTextColor(ContextCompat.getColor(requireContext(), R.color.black))

        // Get action codes
        actionCode = arguments?.getInt(KEY_ACTION, 0) ?: 0
        transactionId = arguments?.getInt(KEY_TRANSACTION_ID, 0) ?: 0

        if(actionCode == ACTION_EDIT) {
            binding.categoryInput.isEnabled = false
            locateButton.isEnabled = false
            deleteButton.isEnabled = false
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hubActivity.configurationLock = false
        val orientation = resources.configuration.orientation

        hubActivity.binding.headerContent.navBackButton.visibility = View.GONE
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            hubActivity.binding.navView.visibility = View.GONE
            hubActivity.binding.navViewLandscape.visibility = View.VISIBLE
        } else {
            hubActivity.binding.navViewLandscape.visibility = View.GONE
            hubActivity.binding.navView.visibility = View.VISIBLE
        }
    }
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

    }

    private fun onDeleteClick(view: View) {
        locationViewModel.setLoc(null, null)
    }

    private fun onLocateClick(view: View) {
        getLastLocation()
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(hubActivity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )

            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {

                return
            }
        }

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(hubActivity,
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
            binding.locationText.text = loc.toString()
        } else {
            binding.locationText.text = requireContext().getString(R.string.no_location_data)
        }
    }

    private fun onSubmitClick(view: View){
        val title = binding.titleInput.text.toString()
        val category = binding.categoryInput.selectedItem.toString()
        val amount = binding.amountInput.text.toString()

        if (title.isEmpty()){
            Toast.makeText(requireContext(), requireContext().getString(R.string.transaction_add_toast_error_title), Toast.LENGTH_SHORT).show()
        }
        else if (amount.isEmpty()){
            Toast.makeText(requireContext(), requireContext().getString(R.string.transaction_add_toast_error_amount), Toast.LENGTH_SHORT).show()
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
                            timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(
                                Date()
                            ),
                            latitude = savedLat,
                            longitude = savedLng
                        )
                    )
                    Toast.makeText(requireContext(), requireContext().getString(R.string.transaction_add_toast_success), Toast.LENGTH_SHORT).show()
                }

                ACTION_EDIT -> {
                    transactionViewModel.update(
                        TransactionEntity(
                            id = requireArguments().getInt(KEY_TRANSACTION_ID, 0),
                            title = title,
                            category = category,
                            amount = amount.toInt(),
                            timestamp = requireArguments().getString(KEY_TIMESTAMP)!!,
                            latitude = savedLat,
                            longitude = savedLng
                        )
                    )
                    Toast.makeText(requireContext(), requireContext().getString(R.string.transaction_edit_toast_success), Toast.LENGTH_SHORT).show()
                }
            }
            hubActivity.onBackPressed()
        }
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