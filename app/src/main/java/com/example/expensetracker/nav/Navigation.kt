package com.example.expensetracker.nav

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.screens.HomeScreen
import com.example.expensetracker.screens.LoginScreen
import com.example.expensetracker.screens.ProfileScreen
import com.example.expensetracker.screens.ViewProfile
import com.example.expensetracker.viewmodel.AuthViewModel
import kotlinx.serialization.Serializable

@Composable
fun Navigation(viewModel: AuthViewModel = hiltViewModel()) {
    val currentUser = viewModel.currentUser?.uid
    val startDestination = if (currentUser != null) {
        HomeScreen
    } else {
        LoginScreen
    }
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        composable<LoginScreen> {
            LoginScreen(navController = navController)
        }
        composable<HomeScreen> {
            HomeScreen(navController = navController)
        }
        composable<ProfileScreen> {
            ProfileScreen(navController = navController)
        }
        composable<ViewProfile> {
            ViewProfile(navController = navController)
        }
    }
}

@Serializable
object LoginScreen

@Serializable
object ProfileScreen

@Serializable
object HomeScreen

@Serializable
object ViewProfile