package com.example.bondoman.database.repository

import androidx.lifecycle.LiveData
import com.example.bondoman.database.AppDatabase
import com.example.bondoman.database.dao.TransactionDao
import com.example.bondoman.database.entity.TransactionEntity

class TransactionRepository(private val transactionDao: TransactionDao) {
    val list: LiveData<List<TransactionEntity>> = transactionDao.getAll()

    fun getByID(id: Int): TransactionEntity? {
        return transactionDao.getById(id)
    }
    suspend fun insert(transaction: TransactionEntity){
        transactionDao.insert(transaction)
    }

    suspend fun update(transaction: TransactionEntity){
        transactionDao.update(transaction)
    }

    suspend fun delete(transaction: TransactionEntity){
        transactionDao.delete(transaction)
    }

    suspend fun deleteAll(){
        transactionDao.deleteAll()
    }
}