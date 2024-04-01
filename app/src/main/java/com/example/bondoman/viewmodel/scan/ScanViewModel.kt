package com.example.bondoman.viewmodel.scan

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bondoman.database.repository.TransactionRepository
import okhttp3.RequestBody

class ScanViewModel(private val repository: TransactionRepository) : ViewModel() {
    val isCameraPermissionGranted = MutableLiveData(false)

    suspend fun uploadNota(imageReqBody: RequestBody, token: String): Boolean {
        return repository.postUploadNota(imageReqBody, token)
    }
}