package com.example.bondoman.util

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bondoman.dao.TransactionDao
import com.example.bondoman.model.Transaction

@Database(entities = [Transaction::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao

    // Static Instance
    companion object{
        @Volatile
        private var Instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase{
            return Instance ?: synchronized(this){
                Instance ?: buildInstance(context).also { Instance = it }
            }
        }

        private fun buildInstance(context: Context): AppDatabase{
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "bondoman_db"
            ).build()
        }
    }
}