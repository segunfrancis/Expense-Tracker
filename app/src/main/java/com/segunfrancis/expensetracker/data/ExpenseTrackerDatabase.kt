package com.segunfrancis.expensetracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ExpenseEntity::class], version = 1, exportSchema = true)
abstract class ExpenseTrackerDatabase : RoomDatabase() {

    abstract fun getDao(): ExpenseTrackerDao

    companion object {
        fun getDatabase(context: Context): ExpenseTrackerDatabase {
            return Room.databaseBuilder(
                context,
                ExpenseTrackerDatabase::class.java,
                "expense_tracker_database"
            ).build()
        }
    }
}
