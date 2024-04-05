package com.example.bondoman.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import com.example.bondoman.database.dao.TransactionDao
import com.example.bondoman.database.entity.TransactionEntity

@Database(
    entities = [TransactionEntity::class],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration (
            from = 1,
            to = 2,
            spec = AppDatabase.MyAutoMigration::class
        )
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract val transactionDao: TransactionDao

    // Migration
    @DeleteColumn(tableName = "transactions", columnName = "location")
    class MyAutoMigration : AutoMigrationSpec

    // Static Instance
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "bondoman_db"
                    )
                        .allowMainThreadQueries()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}