package com.example.bondoman.database.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.bondoman.api.RetrofitClient
import com.example.bondoman.database.dao.TransactionDao
import com.example.bondoman.database.entity.TransactionEntity
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionRepository(private val transactionDao: TransactionDao) {
    val list: LiveData<List<TransactionEntity>> = transactionDao.getAll()

    companion object {
        private const val TAG = "TransactionRepository"
    }

    fun getByID(id: Int): TransactionEntity? {
        return transactionDao.getById(id)
    }

    suspend fun insert(transaction: TransactionEntity) {
        transactionDao.insert(transaction)
    }

    suspend fun update(transaction: TransactionEntity) {
        transactionDao.update(transaction)
    }

    suspend fun delete(transaction: TransactionEntity) {
        transactionDao.delete(transaction)
    }

    suspend fun deleteAll() {
        transactionDao.deleteAll()
    }

    suspend fun postUploadNota(imageReqBody: RequestBody): Boolean {
        try {
            // TODO: Get stored auth token
            val authToken =
                "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuaW0iOiIxMzUyMTE0OSIsImlhdCI6MTcxMTgxNTE1OCwiZXhwIjoxNzExODE1NDU4fQ.mASnON98EJmGWmVMdjWB47ef3pwYIVJelaYMpMjy_BY"
            val response = RetrofitClient.uploadInstance.uploadImage(
                MultipartBody.Part.createFormData(
                    "file", "test", imageReqBody
                ), authToken
            )

            if (!response.isSuccessful) {
                Log.d(TAG, response.code().toString())
                return false
            }

            for (item in response.body()!!.items.items) {
                insert(
                    TransactionEntity(
                        id = 0, title = item.name,
                        // TODO: Category
                        category = "scanned", amount = item.qty * item.price.toInt(),
                        // TODO: Location
                        location = "lokasi", timestamp = SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss", Locale.getDefault()
                        ).format(
                            Date()
                        )
                    )
                )
            }

            return true
        } catch (e: Exception) {
            Log.e(TAG, e.toString())

            return false
        }
    }
}