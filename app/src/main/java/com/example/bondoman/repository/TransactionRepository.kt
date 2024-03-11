package com.example.bondoman.repository

import com.example.bondoman.dao.TransactionDao
import com.example.bondoman.model.Transaction

class TransactionRepository(private val transactionDAO: TransactionDao) {
    suspend fun insert(transaction: Transaction){
        transactionDAO.insert(transaction)
    }

    suspend fun getAll(): List<Transaction>{
        return transactionDAO.getAll()
    }

    suspend fun update(transaction: Transaction){
        transactionDAO.update(transaction)
    }

    suspend fun delete(transaction: Transaction){
        transactionDAO.delete(transaction)
    }
}