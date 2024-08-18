package com.example.expensetracker.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.expensetracker.data.Resource
import com.example.expensetracker.data.utils.Expense
import com.example.expensetracker.ui_components.TextFieldsCustom
import com.example.expensetracker.ui_components.expenseId
import com.example.expensetracker.ui_constants.backgroundColor
import com.example.expensetracker.ui_constants.updateExpenseId
import com.example.expensetracker.viewmodel.AuthViewModel

@Composable
fun EditExpense(viewModel: AuthViewModel = hiltViewModel(), navController: NavController) {
    val expense = viewModel.expense.collectAsState().value
    val id = expenseId

    LaunchedEffect(Unit) {
        viewModel.getExpenseById(id)

    }
//    Column(
//        Modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(text = id)
//    }

    when (expense) {
        is Resource.Failure -> {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = expense.exception.message.toString())
                Text(text = id)
            }
        }

        Resource.Loading -> {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }

        is Resource.Success -> {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(backgroundColor),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val note = remember {
                    mutableStateOf(expense.result.note.toString())
                }
                val category = remember {
                    mutableStateOf(expense.result.category.toString())
                }
                val amount = remember {
                    mutableStateOf(expense.result.amount.toString())
                }
                val date = remember {
                    mutableStateOf(expense.result.date.toString())
                }
                Card(
                    Modifier
                        .padding(start = 30.dp, top = 10.dp)
                        .size(50.dp)
                        .align(Alignment.Start)
                        .clickable { navController.popBackStack() },
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Back",
                        )
                    }
                }


                TextFieldsCustom(label = "Edit note", note = note.value) { note.value = it }
                TextFieldsCustom(label = "Edit category", note = category.value) {
                    category.value = it
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextFieldsCustom(
                        modifier = Modifier.weight(.7f),
                        label = "Edit amount",
                        note = amount.value,
                        keyboardType = KeyboardType.Number
                    ) { amount.value = it }
                    Card(
                        Modifier
                            .padding(10.dp)
                            .size(60.dp)
                            .clickable {
                                viewModel.updateExpense(
                                    expenseId = updateExpenseId,
                                    expense = Expense(
                                        note = note.value,
                                        category = category.value,
                                        amount = amount.value.toFloat(),
                                        date = date.value
                                    )
                                )
                                updateExpenseId = ""
                                navController.popBackStack()
                            },
                        shape = CircleShape,
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                text = "Save",
                            )
                        }
                    }
                }
                TextFieldsCustom(label = "Edit date", note = date.value) { date.value = it }

            }
        }


        null -> {
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Null")
            }
        }
    }
}


