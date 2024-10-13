package com.segunfrancis.expensetracker.ui.expenses

import androidx.lifecycle.ViewModel
import com.segunfrancis.expensetracker.ui.add_expense.SplitOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ExpensesViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(ExpensesUiState.NoExpense)
    val uiState = _uiState.asStateFlow()

    fun handleAction(action: ExpensesAction) {
        when (action) {
            is ExpensesAction.OnDeleteClick -> {}
            is ExpensesAction.OnEditClick -> {}
            else -> {}
        }
    }
}

sealed class ExpensesUiState {
    data class Expenses(val expenses: List<Expense>) : ExpensesUiState()
    data object NoExpense : ExpensesUiState()
}

data class Expense(
    val description: String,
    val id: Long,
    val price: Double,
    val splitOption: SplitOption
)
