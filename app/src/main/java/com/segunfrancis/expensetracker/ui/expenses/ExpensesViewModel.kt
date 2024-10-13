package com.segunfrancis.expensetracker.ui.expenses

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.segunfrancis.expensetracker.data.ExpenseEntity
import com.segunfrancis.expensetracker.data.ExpenseTrackerDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ExpensesViewModel @Inject constructor(
    private val dao: ExpenseTrackerDao,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow<ExpensesUiState>(ExpensesUiState.NoExpense)
    val uiState = _uiState.asStateFlow()

    init {
        getExpenses()
    }

    fun handleAction(action: ExpensesAction) {
        when (action) {
            is ExpensesAction.OnDeleteClick -> deleteExpense(action.id)
            else -> {}
        }
    }

    private fun getExpenses() {
        viewModelScope.launch {
            dao.getAllExpenses().catch {
                Log.e("ExpensesViewModel", "getExpenses: ${it.localizedMessage}", it)
            }.flowOn(dispatcher)
                .collect { expenses ->
                    _uiState.update { _ ->
                        if (expenses.isEmpty()) {
                            ExpensesUiState.NoExpense
                        } else {
                            ExpensesUiState.Expenses(expenses.map { it.mapToExpenseUi() })
                        }
                    }
                }
        }
    }

    private fun deleteExpense(id: Long) {
        viewModelScope.launch {
            withContext(dispatcher) {
                dao.deleteExpense(id)
            }
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
    val splitOption: String
)

fun ExpenseEntity.mapToExpenseUi(): Expense {
    return Expense(
        description = description,
        id = id,
        price = price,
        splitOption = splitOption
    )
}
