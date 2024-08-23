package com.example.expensetracker.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.expensetracker.data.Resource
import com.example.expensetracker.data.utils.Profile
import com.example.expensetracker.nav.AllExpense
import com.example.expensetracker.ui_components.TextFieldsCustom
import com.example.expensetracker.ui_constants.backgroundColor
import com.example.expensetracker.viewmodel.AuthViewModel

@Composable
fun ProfileScreen(viewModel: AuthViewModel = hiltViewModel(), navController: NavController) {
    val profile = viewModel.getProfile.collectAsState().value



    profile.let {
        when (it) {
            is Resource.Failure -> TODO()
            Resource.Loading -> TODO()
            is Resource.Success -> {

                LaunchedEffect(Unit) {
                    viewModel.getProfile
                }
                var name by remember { mutableStateOf(it.result.name) }
                var age by remember { mutableStateOf("") }
                var bio by remember { mutableStateOf(it.result.bio) }
                var income by remember { mutableStateOf("") }
                var errorMsg by remember { mutableStateOf("") }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backgroundColor)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "",
                        Modifier.clickable { navController.popBackStack() })
                    Text(text = "Update Your Profile")

                    TextFieldsCustom(
                        label = "Name",
                        note = "",
                        string = { name = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextFieldsCustom(
                        label = "Age",
                        string = { age = it },
                        note = "",
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextFieldsCustom(
                        label = "Bio",
                        string = { bio = it },
                        note = "",
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextFieldsCustom(
                        label = "Income",
                        string = { income = it },
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            val ageInt = age.toIntOrNull()
                            val incomeDouble = income.toDoubleOrNull()

                            if (ageInt != null && incomeDouble != null) {
                                viewModel.addProfile(
                                    Profile(
                                        name = name,
                                        age = ageInt,
                                        bio = bio,
                                        income = incomeDouble
                                    )
                                )
                                navController.navigate(AllExpense)
                            } else {
                                errorMsg = "Please enter valid values for age and income."
                            }
                        },
                        enabled = if (name?.isNotEmpty() == true && age.isNotEmpty() && bio?.isNotEmpty() == true && income.isNotEmpty()) true
                        else {
                            false
                        }
                    ) {
                        Text("Save Profile")
                    }

                    if (errorMsg.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(errorMsg, color = Color.Red)
                    }
                }
            }

            null -> TODO()
        }
    }
}

