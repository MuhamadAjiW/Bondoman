package com.example.bondoman.database.repository

import androidx.lifecycle.LiveData
import com.example.bondoman.database.AppDatabase
import com.example.bondoman.database.dao.TransactionDao
import com.example.bondoman.database.entity.TransactionEntity

class TransactionRepository(private val database: AppDatabase) {
    val list: LiveData<List<TransactionEntity>> = database.transactionDao.getAll()

    fun getByID(id: Int): TransactionEntity? {
        return database.transactionDao.getById(id)
    }
    suspend fun insert(transaction: TransactionEntity){
        database.transactionDao.insert(transaction)
    }

    suspend fun update(transaction: TransactionEntity){
        database.transactionDao.update(transaction)
    }

    suspend fun delete(transaction: TransactionEntity){
        database.transactionDao.delete(transaction)
    }

    suspend fun deleteAll(){
        database.transactionDao.deleteAll()
    }
}