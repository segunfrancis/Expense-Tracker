package com.segunfrancis.expensetracker.ui.view_expense

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.segunfrancis.expensetracker.ui.add_expense.SplitOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ViewExpenseViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _uiState = MutableStateFlow(ViewExpenseUiState())
    val uiState = _uiState.asStateFlow()


}

data class ViewExpenseUiState(
    val description: String = "",
    val price: String = "",
    val splitOption: SplitOption? = null
)
