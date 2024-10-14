package com.segunfrancis.expensetracker.ui.view_expense

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.segunfrancis.expensetracker.byteArrayToBitmap
import com.segunfrancis.expensetracker.ui.components.ViewExpenseTextComponent

@Composable
fun ViewExpenseScreen() {
    val viewModel: ViewExpenseViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState(ViewExpenseUiState())
    ViewExpenseContent(state = uiState)
}

@Composable
fun ViewExpenseContent(state: ViewExpenseUiState) {
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(state = rememberScrollState())) {
        Spacer(modifier = Modifier.height(24.dp))
        ViewExpenseTextComponent(title = "Description", value = state.description)
        Spacer(modifier = Modifier.height(24.dp))
        ViewExpenseTextComponent(title = "Price", value = "₦".plus(state.price))
        Spacer(modifier = Modifier.height(24.dp))
        ViewExpenseTextComponent(title = "Split Option", value = state.splitOption)
        Spacer(modifier = Modifier.height(24.dp))
        state.image?.let {
            Text(
                text = "Receipt",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Image(
                bitmap = byteArrayToBitmap(it).asImageBitmap(),
                contentDescription = "Uploaded image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().heightIn(max = 400.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

@Preview
@Composable
fun ViewExpenseScreenPreview() {
    MaterialTheme {
        Surface {
            ViewExpenseContent(ViewExpenseUiState())
        }
    }
}
