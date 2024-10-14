package com.segunfrancis.expensetracker.ui.view_expense

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.segunfrancis.expensetracker.data.ExpenseTrackerDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.text.NumberFormat
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ViewExpenseViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dispatcher: CoroutineDispatcher,
    private val dao: ExpenseTrackerDao
) : ViewModel() {

    val uiState = savedStateHandle.getStateFlow("id", 0L)
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0L)
        .flatMapLatest {
            dao.getExpense(it).map { expense ->
                ViewExpenseUiState(
                    description = expense.description,
                    price = NumberFormat.getNumberInstance().format(expense.price),
                    splitOption = expense.splitOption,
                    image = expense.image
                )
            }
        }.catch {
            Log.e("ViewExpenseViewModel", "Error: ${it.localizedMessage}", it)
        }.flowOn(dispatcher)
}

data class ViewExpenseUiState(
    val description: String = "",
    val price: String = "",
    val splitOption: String = "",
    val image: ByteArray? = null
)
