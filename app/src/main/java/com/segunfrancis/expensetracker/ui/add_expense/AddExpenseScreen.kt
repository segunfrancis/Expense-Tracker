package com.segunfrancis.expensetracker.ui.add_expense

import android.Manifest
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.segunfrancis.expensetracker.bitmapToByteArray
import com.segunfrancis.expensetracker.byteArrayToBitmap
import com.segunfrancis.expensetracker.uriToBitmap

@Composable
fun AddExpenseScreen(onNavigateUp: () -> Unit) {
    val viewModel = hiltViewModel<AddExpenseViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.action.collect {
            when (it) {
                is AddExpenseAction.ShowMessage -> Toast.makeText(
                    context,
                    it.message,
                    Toast.LENGTH_LONG
                ).show()

                AddExpenseAction.NavigateUp -> {
                    onNavigateUp()
                }
            }
        }
    }

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
        uri?.let {
            uriToBitmap(context, uri)?.let { bitmap ->
                viewModel.setImageByteArray(bitmapToByteArray(bitmap))
            }
        }
    }

    val viewImagesPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                imagePickerLauncher.launch("image/*")
            } else {
                Toast.makeText(
                    context,
                    "Unable to proceed because image permission is not granted",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    AddExpenseContent(state = uiState, onAction = {
        if (it is AddExpenseScreenActions.OnAddImageClick) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                viewImagesPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                viewImagesPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        } else {
            viewModel.handleAction(it)
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseContent(state: AddExpenseUiState, onAction: (AddExpenseScreenActions) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
    ) {

        var expanded by remember { mutableStateOf(false) }
        val splitOptions = listOf(
            SplitOption(id = 1, value = "You paid and split equally"),
            SplitOption(id = 2, value = "Guest paid and split equally"),
            SplitOption(id = 3, value = "Owed full amount")
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 16.dp),
            value = state.description,
            onValueChange = { onAction(AddExpenseScreenActions.OnDescriptionChange(it)) },
            label = { Text(text = "Expense Description") },
            supportingText = { Text("* Required") },
            singleLine = false,
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = true,
                capitalization = KeyboardCapitalization.Sentences
            )
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 16.dp),
            value = state.price,
            onValueChange = { onAction(AddExpenseScreenActions.OnPriceChange(it)) },
            label = { Text("Price") },
            supportingText = { Text("* Required") },
            prefix = { Text(text = "₦") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        ExposedDropdownMenuBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 16.dp),
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                value = state.splitOption,
                onValueChange = {},
                readOnly = true,
                supportingText = { Text("* Required") },
                singleLine = true,
                label = { Text("Split options") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {
                splitOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = option.value) },
                        onClick = {
                            expanded = false
                            onAction(AddExpenseScreenActions.OnSplitOptionChange(option.value))
                        }
                    )
                }
            }
        }

        TextButton(
            onClick = {
                onAction(AddExpenseScreenActions.OnAddImageClick)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            Text("Upload receipt")
        }

        state.image?.let {
            Image(
                bitmap = byteArrayToBitmap(it).asImageBitmap(),
                contentDescription = "Uploaded image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().heightIn(max = 400.dp)
                    .padding(horizontal = 16.dp)
            )
        }

        ElevatedButton(
            onClick = {
                onAction(AddExpenseScreenActions.OnAddClick)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            enabled = state.isButtonEnabled
        ) {
            Text(text = "Add Expense")
        }
    }
}

@Preview
@Composable
fun AddExpenseScreenPreview() {
    MaterialTheme {
        Surface {
            AddExpenseContent(AddExpenseUiState(), onAction = {})
        }
    }
}

data class SplitOption(val id: Int, val value: String)

sealed interface AddExpenseScreenActions {
    data class OnDescriptionChange(val description: String) : AddExpenseScreenActions
    data class OnPriceChange(val price: String) : AddExpenseScreenActions
    data class OnSplitOptionChange(val splitOption: String) : AddExpenseScreenActions
    data object OnAddClick : AddExpenseScreenActions
    data object OnAddImageClick : AddExpenseScreenActions
}
