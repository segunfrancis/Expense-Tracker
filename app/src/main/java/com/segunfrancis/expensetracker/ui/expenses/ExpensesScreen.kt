package com.segunfrancis.expensetracker.ui.expenses

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.segunfrancis.expensetracker.ui.components.ExpenseItem

@Composable
fun ExpensesScreen(onViewExpenseClick: (Long) -> Unit, onEditExpenseClick: (Long) -> Unit) {
    val viewModel: ExpensesViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    ExpensesContent(state = uiState, onAction = {
        when (it) {
            is ExpensesAction.OnItemClick -> onViewExpenseClick(it.id)
            is ExpensesAction.OnEditClick -> onEditExpenseClick(it.id)
            else -> viewModel.handleAction(it)
        }
    })
}

@Composable
fun ExpensesContent(state: ExpensesUiState, onAction: (ExpensesAction) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        when (state) {
            is ExpensesUiState.Expenses -> ExpensesState(
                state.expenses,
                onExpenseAction = { onAction(it) })

            ExpensesUiState.NoExpense -> NoExpenseState()
        }
    }
}

@Composable
fun ExpensesState(expenses: List<Expense>, onExpenseAction: (ExpensesAction) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp),
        ) {
            items(expenses) { expense ->
                ExpenseItem(
                    description = expense.description,
                    onEditClick = { onExpenseAction(ExpensesAction.OnEditClick(expense.id)) },
                    onDeleteClick = { onExpenseAction(ExpensesAction.OnDeleteClick(expense.id)) },
                    onItemClick = { onExpenseAction(ExpensesAction.OnItemClick(expense.id)) }
                )
            }
        }
    }
}

@Composable
fun NoExpenseState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No expenses have been recorded",
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(48.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Click on the \"+\" button below to add an expense",
            modifier = Modifier.padding(60.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
@Preview
fun ExpensesScreenPreview() {
    MaterialTheme {
        Surface {
            ExpensesContent(ExpensesUiState.NoExpense) { }
        }
    }
}

sealed interface ExpensesAction {
    data class OnItemClick(val id: Long) : ExpensesAction
    data class OnEditClick(val id: Long) : ExpensesAction
    data class OnDeleteClick(val id: Long) : ExpensesAction
}
