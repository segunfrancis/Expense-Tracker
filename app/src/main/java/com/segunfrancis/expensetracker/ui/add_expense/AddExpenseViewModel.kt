package com.segunfrancis.expensetracker.ui.add_expense

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AddExpenseViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(AddExpenseUiState())
    val uiState = _uiState.asStateFlow()

    private fun setDescription(description: String) {
        _uiState.update { it.copy(description = description) }
        handleButtonVisibility()
    }

    private fun setPrice(price: String) {
        _uiState.update { it.copy(price = price) }
        handleButtonVisibility()
    }

    private fun setSplitOption(splitOption: SplitOption) {
        _uiState.update { it.copy(splitOption = splitOption) }
        handleButtonVisibility()
    }

    fun handleAction(action: AddExpenseScreenActions) {
        when (action) {
            is AddExpenseScreenActions.OnDescriptionChange -> setDescription(action.description)
            is AddExpenseScreenActions.OnPriceChange -> setPrice(action.price)
            is AddExpenseScreenActions.OnSplitOptionChange -> setSplitOption(action.splitOption)
        }
    }

    private fun handleButtonVisibility() {
        _uiState.update {
            it.copy(
                isButtonEnabled = uiState.value.price.isNotBlank() && uiState.value.description.isNotBlank() && uiState.value.splitOption != null
            )
        }
    }
}

data class AddExpenseUiState(
    val description: String = "",
    val price: String = "",
    val splitOption: SplitOption? = null,
    val isButtonEnabled: Boolean = false
)
