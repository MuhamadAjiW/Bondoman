package com.example.bondoman.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction (
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name = "amount")
    val amount: Int,

    @ColumnInfo(name = "location")
    val location: String,

    @ColumnInfo(name = "timestamp")
    val timestamp: String,
) {
    // Static macros for category
    companion object {
        const val CATEGORY_INCOME = "income"
        const val CATEGORY_EXPENSES = "expenses"
    }
}
