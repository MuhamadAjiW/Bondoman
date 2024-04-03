package com.example.bondoman.viewmodel.scan

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bondoman.database.entity.TransactionEntity
import com.example.bondoman.database.repository.TransactionRepository
import okhttp3.RequestBody

class ScanViewModel(private val repository: TransactionRepository) : ViewModel() {
    val isCameraPermissionGranted = MutableLiveData(false)
    val isCameraBtnEnabled = MutableLiveData(false)
    val isSelectBtnEnabled = MutableLiveData(false)
    val showSnackbar = MutableLiveData(false)

    suspend fun uploadNota(imageReqBody: RequestBody): List<TransactionEntity> {
        return repository.postUploadNota(imageReqBody)
    }

    suspend fun saveScanned(scannedTransactions: List<TransactionEntity>) {
        for (item in scannedTransactions) {
            repository.insert(item)
        }
    }
}