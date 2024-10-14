package com.segunfrancis.expensetracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlin.concurrent.Volatile

@Database(
    entities = [ExpenseEntity::class],
    version = 2,
    exportSchema = true
)
abstract class ExpenseTrackerDatabase : RoomDatabase() {

    abstract fun getDao(): ExpenseTrackerDao

    companion object {

        @Volatile
        private var INSTANCE: ExpenseTrackerDatabase? = null

        fun getDatabase(context: Context): ExpenseTrackerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    ExpenseTrackerDatabase::class.java,
                    "expense_tracker_database"
                ).addMigrations(MIGRATION_1_2).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE ExpenseEntity ADD COLUMN image BLOB")
    }
}
