package com.example.bondoman.ui.transaction

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.bondoman.R
import com.example.bondoman.database.AppDatabase
import com.example.bondoman.database.entity.TransactionEntity
import com.example.bondoman.database.repository.TransactionRepository
import com.example.bondoman.databinding.ActivityTransactionBinding
import com.example.bondoman.viewmodel.transaction.TransactionViewModel
import com.example.bondoman.viewmodel.transaction.TransactionViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransactionBinding
    private lateinit var transactionViewModel: TransactionViewModel
    private var actionCode: Int = 0

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize database
        val database = AppDatabase.getInstance(this)
        val transactionRepo = TransactionRepository(database.transactionDao)
        val transactionModelFactory = TransactionViewModelFactory(transactionRepo)
        transactionViewModel = ViewModelProvider(this, transactionModelFactory)[TransactionViewModel::class.java]

        // Initialize header
        binding.header.navTitle.text = getString(R.string.hub_nav_transaction)
        val backButton = binding.header.navBackButton
        val submitButton = binding.submitButton
        backButton.setOnClickListener(::onBackClick)
        submitButton.setOnClickListener(::onSubmitClick)

        // Initialize initial values
        val titleInitial = intent.getStringExtra(titleKey)
        val amountInitial = intent.getStringExtra(amountKey)
        val categoryInitial = intent.getIntExtra(categoryKey, 0)
        val locationInitial = intent.getStringExtra(locationKey)

        binding.titleInput.setText(titleInitial)
        binding.amountInput.setText(amountInitial)
        binding.categoryInput.setSelection(categoryInitial, true)
        binding.locationInput.setText(locationInitial)

        // Initialize category dropdown color
        (binding.categoryInput.selectedView as TextView).setTextColor(ContextCompat.getColor(this, R.color.black))

        actionCode = intent.getIntExtra(actionKey, 0)

    }

    // Header back button
    private fun onBackClick(view: View) {
        onBackPressed()
    }

    // TODO: Handle edits
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
            transactionViewModel.insert(
                TransactionEntity(
                    id = 0,
                    title = title,
                    category = category,
                    amount = amount.toInt(),
                    // TODO: Location
                    location = binding.locationInput.text.toString(),
                    timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                )
            )
            Toast.makeText(this, getString(R.string.transaction_add_toast_success), Toast.LENGTH_SHORT).show()
//                TODO: Delete, this is for testing purposes
//                transactionViewModel.deleteAll()
            onBackPressed()
        }
    }

    companion object{
        val titleKey = "Title"
        val amountKey = "Amount"
        val categoryKey = "Category"
        val locationKey = "Location"
        val actionKey = "Action"

        val ACTION_ADD = 0
        val ACTION_EDIT = 1

        val CATEGORY_INCOME = 0
        val CATEGORY_EXPENSES = 1
    }
}