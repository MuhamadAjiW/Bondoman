package com.example.bondoman.viewmodel.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bondoman.database.entity.TransactionEntity
import com.example.bondoman.database.repository.TransactionRepository
import kotlinx.coroutines.launch

class TransactionViewModel(private val repository: TransactionRepository) : ViewModel() {
    var list: LiveData<List<TransactionEntity>> = repository.list

    fun getById(id: Int): TransactionEntity? {
        return repository.getByID(id)
    }

    fun insert(transaction: TransactionEntity) = viewModelScope.launch {
        repository.insert(transaction)
    }

    fun update(transaction: TransactionEntity) = viewModelScope.launch {
        repository.update(transaction)
    }

    fun delete(transaction: TransactionEntity) = viewModelScope.launch {
        repository.delete(transaction)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}