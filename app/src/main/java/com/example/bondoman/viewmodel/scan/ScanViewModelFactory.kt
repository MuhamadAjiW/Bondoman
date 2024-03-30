package com.example.bondoman.viewmodel.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bondoman.database.repository.TransactionRepository
import com.example.bondoman.viewmodel.transaction.TransactionViewModel

class ScanViewModelFactory(private val repository: TransactionRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ScanViewModel(repository) as T
    }
}