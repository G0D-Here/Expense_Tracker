package com.example.expensetracker.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.data.Resource
import com.example.expensetracker.nav.AllExpense
import com.example.expensetracker.nav.ProfileScreen
import com.example.expensetracker.ui_components.TextFieldsCustom
import com.example.expensetracker.ui_constants.backgroundColor
import com.example.expensetracker.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    navController: NavController = rememberNavController()
) {
    val loginState = viewModel.loginFlow.collectAsState().value
    val signUpState = viewModel.signUpFlow.collectAsState().value
    var login by remember {
        mutableStateOf(false)
    }
    var error by remember {
        mutableStateOf("")
    }

    AuthPage(login = login, error = error, viewModel = viewModel) { login = it }
    when (loginState) {
        is Resource.Success -> {
            LaunchedEffect(Unit) {
                navController.navigate(AllExpense)

            }
        }

        is Resource.Failure -> {
            error = loginState.exception.message.toString()
        }

        Resource.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }

        null -> {}
    }
    when (signUpState) {
        is Resource.Failure -> {
            error = signUpState.exception.message.toString()

        }

        Resource.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is Resource.Success -> {
            LaunchedEffect(Unit) {
                navController.navigate(ProfileScreen)
            }

        }

        null -> {}
    }
}

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthPage(
    login: Boolean = false,
    error: String = "",
    viewModel: AuthViewModel = hiltViewModel(),
    logged: (Boolean) -> Unit = {}
) {

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
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(Modifier.weight(.6f), contentAlignment = Alignment.BottomCenter) {
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
        }

        Box(Modifier.weight(.7f)) {
            Column {
                TextFieldsCustom(label = "email") { email = it }
                TextFieldsCustom(label = "password") { password = it }
                if (!login) {
                    TextFieldsCustom(label = "name")
                    { name = it }
                }
            }
        }
        Box(Modifier.weight(.7f), contentAlignment = Alignment.TopCenter) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {


                Text(text = error)


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

    }
}
