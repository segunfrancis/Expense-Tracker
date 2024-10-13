package com.segunfrancis.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.segunfrancis.expensetracker.ui.add_expense.AddExpenseScreen
import com.segunfrancis.expensetracker.ui.components.ExpenseTrackerToolbar
import com.segunfrancis.expensetracker.ui.expenses.ExpensesScreen
import com.segunfrancis.expensetracker.ui.theme.ExpenseTrackerTheme
import com.segunfrancis.expensetracker.ui.view_expense.ViewExpenseScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            ExpenseTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val backStackEntry by navController.currentBackStackEntryAsState()

                    Scaffold(floatingActionButton = {
                        if (backStackEntry?.destination?.hasRoute(
                                ExpenseTrackerRoute.ExpensesRoute::class
                            ) == true
                        ) {
                            FloatingActionButton(
                                onClick = { navController.navigate(ExpenseTrackerRoute.AddExpenseRoute()) },
                                elevation = FloatingActionButtonDefaults.elevation()
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_add),
                                    contentDescription = "Add expense"
                                )
                            }
                        }
                    },
                        topBar = {
                            ExpenseTrackerToolbar(
                                backStackEntry
                            ) { navController.navigateUp() }
                        }) {
                        NavHost(
                            navController = navController,
                            startDestination = ExpenseTrackerRoute.ExpensesRoute,
                            modifier = Modifier.padding(it)
                        ) {
                            composable<ExpenseTrackerRoute.ExpensesRoute> {
                                ExpensesScreen(
                                    onViewExpenseClick = { id ->
                                        navController.navigate(
                                            ExpenseTrackerRoute.ViewExpenseRoute(id)
                                        )
                                    }, onEditExpenseClick = { id ->
                                        navController.navigate(ExpenseTrackerRoute.AddExpenseRoute(id))
                                    }
                                )
                            }
                            composable<ExpenseTrackerRoute.ViewExpenseRoute> {
                                ViewExpenseScreen()
                            }
                            composable<ExpenseTrackerRoute.AddExpenseRoute> {
                                AddExpenseScreen(onNavigateUp = { navController.navigateUp() })
                            }
                        }
                    }
                }
            }
        }
    }
}
