package com.segunfrancis.expensetracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val price: Double,
    val description: String,
    val splitOption: String
)
