package com.example.bondoman.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "category")
    var category: String,

    @ColumnInfo(name = "amount")
    var amount: Int,

    @ColumnInfo(name = "location")
    var location: String,

    @ColumnInfo(name = "timestamp")
    val timestamp: String,
)
