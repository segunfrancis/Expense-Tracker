package com.segunfrancis.expensetracker.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.segunfrancis.expensetracker.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseItem(
    description: String,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onItemClick: () -> Unit
) {
    Card(onClick = onItemClick) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
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
                    .clip(CircleShape)
                    .fillMaxWidth()
                    .weight(1F)
                    .clickable { onEditClick() }
                    .padding(8.dp)
            )
            Icon(
                painter = painterResource(R.drawable.ic_delete),
                contentDescription = "Delete expense",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
                    .clip(CircleShape)
                    .clickable { onDeleteClick() }
                    .padding(8.dp),
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
            description = "This is a description of the expense that was entered by the user",
            onEditClick = {},
            onDeleteClick = {},
            onItemClick = {}
        )
    }
}
