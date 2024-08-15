package com.example.expensetracker.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.expensetracker.MainActivity
import com.example.expensetracker.data.utils.Expense
import com.example.expensetracker.nav.LoginScreen
import com.example.expensetracker.nav.ProfileScreen
import com.example.expensetracker.nav.ViewProfile
import com.example.expensetracker.viewmodel.AuthViewModel

@Composable
fun HomeScreen(viewModel: AuthViewModel = hiltViewModel(), navController: NavController) {
//    val loginState = viewModel.loginFlow.collectAsState()
    val context = LocalContext.current
    BackHandler {
        (context as? MainActivity)?.finish()
    }
    var category by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }


    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {navController.navigate(ViewProfile)}) {

        }
        Button(onClick = {
            viewModel.logout()
            navController.navigate(LoginScreen)

        }) {
            Text(text = "Logout")
        }
        OutlinedTextField(
            value = category, onValueChange = { category = it },
            label = { Text(text = "Category") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Button(
            onClick = {
                viewModel.addExpense(Expense(category = category, amount = amount.toDouble()))
            },
            enabled = category.isNotEmpty() && amount.isNotEmpty()
        ) {
            Text(text = "Add Expense")
        }
    }

}