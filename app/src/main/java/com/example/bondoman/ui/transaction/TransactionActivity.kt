package com.example.bondoman.ui.transaction

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.bondoman.R
import com.example.bondoman.database.AppDatabase
import com.example.bondoman.database.entity.TransactionEntity
import com.example.bondoman.database.repository.TransactionRepository
import com.example.bondoman.databinding.ActivityTransactionBinding
import com.example.bondoman.viewmodel.transaction.TransactionViewModel
import com.example.bondoman.viewmodel.transaction.TransactionViewModelFactory

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
        val adapter = ArrayAdapter.createFromResource(this, R.array.category_choices, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Initialize category dropdown
        val submitButton = binding.submitButton

        // TODO: Actually do crud
        // Basically testing for now
        submitButton.setOnClickListener {
            transactionViewModel.insert(
                TransactionEntity(
                    id = 0,
                    title = "Dummy Data",
                    category = "Miscellaneous",
                    amount = 100,
                    location = "Dummy Location",
                    timestamp = "2024-03-11 12:00:00"
                )
            )
            println("Dummy data successfully created")
            transactionViewModel.update(
                TransactionEntity(
                    id = 1,
                    title = "Dummies",
                    category = "Miscellaneous",
                    amount = 100,
                    location = "Dummy Location",
                    timestamp = "2024-03-11 12:00:00"
                )
            )
            println("Dummy data successfully updated")

            val transaction: TransactionEntity? = transactionViewModel.getById(1)
            if(transaction != null){
                transactionViewModel.delete(transaction)
                println("Dummy data successfully deleted")
            } else{
                println("Dummy data unsuccessfully deleted")
            }

            transactionViewModel.deleteAll()
            println("Clearing all data")

            println("Crud test finished")
        }
    }

    // Header back button
    private fun onBackClick(view: View) {
        onBackPressed()
    }
}