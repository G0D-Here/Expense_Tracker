package com.example.expensetracker.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.expensetracker.data.utils.Expense
import com.example.expensetracker.nav.AllExpense
import com.example.expensetracker.ui_components.TextFieldsCustom
import com.example.expensetracker.ui_constants.backgroundColor
import com.example.expensetracker.viewmodel.AuthViewModel

@Composable
fun AddExpense(viewModel: AuthViewModel = hiltViewModel(), navController: NavController) {

    var category by rememberSaveable { mutableStateOf("") }
    var amount by rememberSaveable { mutableStateOf("") }
    var note by rememberSaveable { mutableStateOf("") }
    var tag by rememberSaveable { mutableStateOf("") }
    var date by rememberSaveable { mutableStateOf("") }



    Column(
        Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(top = 40.dp)
    ) {
        Row(
            Modifier
                .padding(start = 30.dp, end = 30.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .padding(end = 30.dp)
                    .clip(CircleShape)
                    .size(50.dp)
                    .clickable { navController.popBackStack() }
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "GO BACK",
                )


            }
            Button(
                onClick = {
                    viewModel.addExpense(
                        Expense(
                            category = category,
                            amount = amount.toFloat(),
                            note = note,
                            tags = tag,
                            date = date
                        )
                    )
                    navController.navigate(AllExpense)
                },
                Modifier
                    .size(90.dp, 40.dp),
                enabled = category.isNotEmpty() && amount.isNotEmpty() && note.isNotEmpty() && date.isNotEmpty()
            ) {
                Text(text = "Add")
            }
        }

        Column(
            Modifier
                .weight(.7f)
                .fillMaxWidth()
                .background(backgroundColor)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            

            TextFieldsCustom(label = "category") {
                category = it
            }
            TextFieldsCustom(label = "note") {
                note = it
            }
            TextFieldsCustom(label = "amount", keyboardType = KeyboardType.Number) {
                amount = it
            }
            TextFieldsCustom(label = "tags") {
                tag = it
            }
            TextFieldsCustom(label = "date") {
                date = it
            }

        }

    }
}
