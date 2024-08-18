package com.example.expensetracker.nav

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.expensetracker.screens.AddExpense
import com.example.expensetracker.screens.EditExpense
import com.example.expensetracker.screens.GraphScreen
import com.example.expensetracker.screens.LoginScreen
import com.example.expensetracker.screens.ProfileScreen
import com.example.expensetracker.screens.ViewProfile
import com.example.expensetracker.screens.all_expenses.AllExpenses
import com.example.expensetracker.viewmodel.AuthViewModel
import kotlinx.serialization.Serializable

@Composable
fun Navigation(viewModel: AuthViewModel = hiltViewModel()) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = LoginScreen) {
        composable<LoginScreen> {
            LoginScreen(navController = navController)
        }
        composable<AddExpense> {
            AddExpense(navController = navController)
        }
        composable<ProfileScreen> {
            ProfileScreen(navController = navController)
        }
        composable<ViewProfile> {
            ViewProfile(navController = navController)
        }
        composable<AllExpense> {
            AllExpenses(navController = navController)
        }
        composable<EditNote> {
            EditExpense(navController = navController)
        }
        composable<ExpenseGraph> {
            GraphScreen(navController = navController)
        }
    }
}

@Serializable
object LoginScreen

@Serializable
object ProfileScreen

@Serializable
object AddExpense

@Serializable
object ViewProfile

@Serializable
object AllExpense

@Serializable
object EditNote

@Serializable
object ExpenseGraph