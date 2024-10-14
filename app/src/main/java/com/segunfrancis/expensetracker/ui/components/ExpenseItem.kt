package com.segunfrancis.expensetracker.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.segunfrancis.expensetracker.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseItem(
    description: String,
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onItemClick: () -> Unit
) {
    Card(onClick = onItemClick, modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 70.dp)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth(0.8F)
            )
            Icon(
                painter = painterResource(R.drawable.ic_edit_note),
                contentDescription = "Edit expense",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
                    .clickable { onEditClick() }
                    .padding(4.dp)
            )
            Icon(
                painter = painterResource(R.drawable.ic_delete),
                contentDescription = "Delete expense",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
                    .clickable { onDeleteClick() }
                    .padding(4.dp),
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Preview
@Composable
fun ExpenseItemPreview() {
    Surface {
        ExpenseItem(
            description = "This is a description of the expense",
            onEditClick = {},
            onDeleteClick = {},
            onItemClick = {}
        )
    }
}
