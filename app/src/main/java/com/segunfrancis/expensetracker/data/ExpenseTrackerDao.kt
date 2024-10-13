package com.segunfrancis.expensetracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseTrackerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addExpense(expenseEntity: ExpenseEntity)

    @Update
    suspend fun updateExpense(expenseEntity: ExpenseEntity)

    @Query("SELECT * FROM ExpenseEntity ORDER BY id DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM ExpenseEntity WHERE :id is id")
    fun getExpense(id: Long): Flow<ExpenseEntity>

    @Query("DELETE FROM ExpenseEntity WHERE :id is id")
    suspend fun deleteExpense(id: Long)

    @Query("SELECT EXISTS(SELECT 1 FROM ExpenseEntity WHERE id = :id)")
    suspend fun peek(id: Long): Boolean
}
