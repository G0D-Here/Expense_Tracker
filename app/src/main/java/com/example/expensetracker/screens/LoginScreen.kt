package com.example.expensetracker.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.expensetracker.data.Resource
import com.example.expensetracker.nav.ProfileScreen
import com.example.expensetracker.nav.HomeScreen
import com.example.expensetracker.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(viewModel: AuthViewModel = hiltViewModel(), navController: NavController) {
    val signupState = viewModel.signUpFlow.collectAsState()
    val loginState = viewModel.loginFlow.collectAsState()

    var login by remember {
        mutableStateOf(false)
    }
    var error by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current

    AuthPage(login, error, viewModel) { login = it }
    if (!login) signupState.value?.let {
        when (it) {
            is Resource.Loading -> {
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is Resource.Failure -> {
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    error = it.exception.message.toString()
                }
            }

            is Resource.Success -> {
                LaunchedEffect(Unit) {
                    delay(100)
                    navController.navigate(ProfileScreen)
                }
            }

        }
    }
    else {
        loginState.value?.let {
            when (it) {
                is Resource.Failure -> {
                    error = it.exception.message.toString()
                }

                Resource.Loading -> {
                    Column(
                        Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is Resource.Success -> {
                    LaunchedEffect(Unit) {
                        delay(100)
                        navController.navigate(HomeScreen)
                    }
                }

            }
        }
    }
}


@Composable
fun AuthPage(login: Boolean, error: String, viewModel: AuthViewModel, logged: (Boolean) -> Unit) {

    var password by remember {
        mutableStateOf("")
    }
    var name by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (!login) {
            Text(
                text = "Sign Up",
                modifier = Modifier
                    .padding(bottom = 10.dp),
                fontSize = 30.sp
            )
        } else
            Text(
                text = "Login",
                modifier = Modifier
                    .padding(bottom = 10.dp),
                fontSize = 30.sp
            )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Email") },
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Password") },
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            singleLine = true
        )
        if (!login) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(text = "Name") },
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                singleLine = true
            )
        }

        Button(onClick = {
            if (!login) viewModel.signUp(email, password, name) else viewModel.login(
                email,
                password
            )
        }) {
            if (!login) {
                Text(text = "Sign Up")
            } else Text(text = "Login")
        }
        Text(text = error)
        if (!login) {
            Text(text = "Already have an account? Login", Modifier.clickable {
                logged(true)
                email = ""
                password = ""
                name = ""
            })
        } else
            Text(text = "Don't have an account? Sign Up", Modifier.clickable {
                logged(false)
                email = ""
                password = ""
                name = ""
            })
    }
}