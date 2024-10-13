package com.segunfrancis.expensetracker.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import com.segunfrancis.expensetracker.ExpenseTrackerRoute
import com.segunfrancis.expensetracker.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseTrackerToolbar(
    navDestination: NavDestination?,
    onBackClick: () -> Unit
) {
    val title = when {
        navDestination?.hasRoute(ExpenseTrackerRoute.ExpensesRoute::class) == true -> "Expenses"
        navDestination?.hasRoute(ExpenseTrackerRoute.ViewExpenseRoute::class) == true -> "View Expense"
        navDestination?.hasRoute(ExpenseTrackerRoute.AddExpenseRoute::class) == true -> {
            if (navDestination.arguments.containsKey("id"))
                "Edit Expense"
            else
                "Add Expense"
        }

        else -> ""
    }
    TopAppBar(
        title = { Text(text = title) }, modifier = Modifier.fillMaxWidth(),
        navigationIcon = {
            if (navDestination?.hasRoute(ExpenseTrackerRoute.ExpensesRoute::class) == false) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = "Go back",
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { onBackClick() }
                        .padding(8.dp)
                )
            }
        })
}

@Preview
@Composable
fun ExpenseTrackerToolbarPreview() {
    ExpenseTrackerToolbar(NavDestination("")) {}
}
