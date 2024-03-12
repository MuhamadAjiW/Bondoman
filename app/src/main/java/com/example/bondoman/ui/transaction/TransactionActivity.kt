package com.example.bondoman.ui.transaction

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
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
import java.time.Duration
import java.util.Date
import java.util.Locale

class TransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransactionBinding

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize database
        val database = AppDatabase.getInstance(this)
        val transactionRepo = TransactionRepository(database)
        val transactionModelFactory = TransactionViewModelFactory(transactionRepo)
        val transactionViewModel = ViewModelProvider(this, transactionModelFactory)[TransactionViewModel::class.java]

        // Initialize header
        binding.header.navTitle.text = getString(R.string.hub_nav_transaction)
        val backButton = binding.header.navBackButton
        backButton.setOnClickListener(::onBackClick)

        // Initialize category dropdown
        val spinner = binding.categoryInput
        spinner.setSelection(0, true);
        (spinner.selectedView as TextView).setTextColor(ContextCompat.getColor(this, R.color.black))

        // Initialize category dropdown
        val submitButton = binding.submitButton

        // Create on click
        submitButton.setOnClickListener {
            val title = binding.titleInput.text.toString()
            val category = binding.categoryInput.selectedItem.toString()
            val amount = binding.amountInput.text.toString()

            if (title.isEmpty()){
                Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show()
            }
            else if (amount.isEmpty()){
                Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this, "Transaction saved successfully", Toast.LENGTH_SHORT).show()
//                TODO: Delete, this is for testing purposes
//                transactionViewModel.deleteAll()
                onBackPressed()
            }
        }
    }

    // Header back button
    private fun onBackClick(view: View) {
        onBackPressed()
    }
}