package com.example.expensetracker.screens.all_expenses

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.expensetracker.MainActivity
import com.example.expensetracker.data.Resource
import com.example.expensetracker.nav.AddExpense
import com.example.expensetracker.nav.EditNote
import com.example.expensetracker.nav.ExpenseGraph
import com.example.expensetracker.nav.ViewProfile
import com.example.expensetracker.ui_components.expenseId
import com.example.expensetracker.ui_constants.backgroundColor
import com.example.expensetracker.ui_constants.updateExpenseId
import com.example.expensetracker.viewmodel.AuthViewModel

@Composable
fun AllExpenses(viewModel: AuthViewModel = hiltViewModel(), navController: NavController) {
    val context = LocalContext.current

    BackHandler {
        (context as MainActivity).finish()
    }

    LaunchedEffect(Unit) {
        viewModel.getExpenses()
    }
    when (val expenses = viewModel.expenses.collectAsState().value) {
        is Resource.Failure -> {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(text = expenses.exception.message.toString())
                Button(onClick = { viewModel.getExpenses() }) {
                    Text(text = "Retry")
                }
            }

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
            Box(
                Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .background(backgroundColor)
                        .padding(top = 35.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                )
                {
//                    Text(text = "Jai Shree Krishna\nAll Expenses", fontSize = .sp, color = backgroundColor)
                    SearchAndProfileBar { navController.navigate(ViewProfile) }
                    FilterButtons {
                        viewModel.sortBy = it
                        viewModel.getExpenses()
                    }
                    if (expenses.result.isEmpty()) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = { navController.navigate(AddExpense) },
                                modifier = Modifier.padding(top = 40.dp),
                                colors = ButtonDefaults.buttonColors(
                                    Color(0xFFE0EFFC)
                                )
                            ) {
                                Text(
                                    text = "Add+\n your first Expense",
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        items(expenses.result) {
                            ExpenseCard(
                                expense = it,
                                deleteClicked = { viewModel.deleteExpense(it.id.toString()) },
                                editClicked = {
                                    updateExpenseId = it.id.toString()
                                    navController.navigate(EditNote)
                                    expenseId = it.id.toString()
                                }
                            )
                            Spacer(modifier = Modifier.width(4.dp))

                        }
                    }

                }
                FloatingActionButton(
                    onClick = { navController.navigate(ExpenseGraph) },
                    modifier = Modifier
                        .padding(end = 5.dp, bottom = 120.dp)
                        .clip(CircleShape)
                        .align(Alignment.BottomEnd)

                ) {
                    Text(text = "Graph")
                }
                if (expenses.result.isNotEmpty()) {
                    FloatingActionButton(
                        onClick = { navController.navigate(AddExpense) },
                        modifier = Modifier
                            .padding(end = 5.dp, bottom = 60.dp)
                            .clip(CircleShape)
                            .align(Alignment.BottomEnd)

                    ) {
                        Text(text = "Add+")
                    }
                }
            }
        }
    }


}
