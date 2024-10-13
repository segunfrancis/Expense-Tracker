package com.segunfrancis.expensetracker

import kotlinx.serialization.Serializable

sealed class ExpenseTrackerRoute {

    @Serializable
    data class AddExpenseRoute(val id: Long? = null) : ExpenseTrackerRoute()

    @Serializable
    data class ViewExpenseRoute(val id: Long) : ExpenseTrackerRoute()

    @Serializable
    data object ExpensesRoute : ExpenseTrackerRoute()
}
