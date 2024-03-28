package com.example.bondoman.viewmodel.scan

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bondoman.database.entity.TransactionEntity
import com.example.bondoman.database.repository.TransactionRepository
import kotlinx.coroutines.launch

class ScanViewModel(private val repository: TransactionRepository) : ViewModel() {
    val isCameraPermissionGranted = MutableLiveData(false)

    fun insertUploaded(transaction: TransactionEntity) = viewModelScope.launch {
        repository.insert(transaction)
    }
}