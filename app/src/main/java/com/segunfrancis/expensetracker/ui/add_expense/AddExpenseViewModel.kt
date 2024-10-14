package com.segunfrancis.expensetracker.ui.add_expense

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.segunfrancis.expensetracker.data.ExpenseEntity
import com.segunfrancis.expensetracker.data.ExpenseTrackerDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    private val dao: ExpenseTrackerDao,
    private val dispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddExpenseUiState())
    val uiState = _uiState.asStateFlow()

    private val _action = MutableSharedFlow<AddExpenseAction>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val action = _action.asSharedFlow()

    private val id: Long? = savedStateHandle["id"]

    init {
        viewModelScope.launch {
            id?.let {
                dao.getExpense(it).catch { throwable ->
                    _action.tryEmit(AddExpenseAction.ShowMessage(throwable.localizedMessage.orEmpty()))
                }.flowOn(dispatcher)
                    .collect {
                        _uiState.update { state ->
                            state.copy(
                                description = it.description,
                                price = it.price.toString(),
                                splitOption = it.splitOption,
                                image = it.image
                            )
                        }
                        handleButtonVisibility()
                    }
            }
        }
    }

    private fun setDescription(description: String) {
        _uiState.update { it.copy(description = description) }
        handleButtonVisibility()
    }

    private fun setPrice(price: String) {
        _uiState.update { it.copy(price = price) }
        handleButtonVisibility()
    }

    private fun setSplitOption(splitOption: String) {
        _uiState.update { it.copy(splitOption = splitOption) }
        handleButtonVisibility()
    }

    fun setImageByteArray(image: ByteArray) {
        _uiState.update { it.copy(image = image) }
    }

    fun handleAction(action: AddExpenseScreenActions) {
        when (action) {
            is AddExpenseScreenActions.OnDescriptionChange -> setDescription(action.description)
            is AddExpenseScreenActions.OnPriceChange -> setPrice(action.price)
            is AddExpenseScreenActions.OnSplitOptionChange -> setSplitOption(action.splitOption)
            AddExpenseScreenActions.OnAddClick -> addExpense()
            else -> {}
        }
    }

    private fun handleButtonVisibility() {
        _uiState.update {
            it.copy(
                isButtonEnabled = uiState.value.price.isNotBlank() && uiState.value.description.isNotBlank() && uiState.value.splitOption.isNotBlank()
            )
        }
    }

    private fun addExpense() {
        viewModelScope.launch {
            val formatPrice = try {
                uiState.value.price.toDouble()
            } catch (e: Exception) {
                _action.tryEmit(AddExpenseAction.ShowMessage("Enter a valid price"))
                null
            }
            formatPrice?.let {
                withContext(dispatcher) {
                    if (id != null) {
                        dao.updateExpense(
                            ExpenseEntity(
                                id = id,
                                price = it,
                                description = uiState.value.description,
                                splitOption = uiState.value.splitOption,
                                image = uiState.value.image
                            )
                        )
                    } else {
                        dao.addExpense(
                            ExpenseEntity(
                                price = it,
                                description = uiState.value.description,
                                splitOption = uiState.value.splitOption,
                                image = uiState.value.image
                            )
                        )
                    }
                    _action.tryEmit(AddExpenseAction.NavigateUp)
                }
            }
        }
    }
}

data class AddExpenseUiState(
    val description: String = "",
    val price: String = "",
    val splitOption: String = "",
    val image: ByteArray? = null,
    val isButtonEnabled: Boolean = false
)

sealed interface AddExpenseAction {
    data class ShowMessage(val message: String) : AddExpenseAction
    data object NavigateUp : AddExpenseAction
}
