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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.expensetracker.screens.all_expenses.TagCard
import com.example.expensetracker.ui_components.TextFieldsCustom
import com.example.expensetracker.ui_constants.backgroundColor
import com.example.expensetracker.viewmodel.AuthViewModel

@Composable
fun AddExpense(viewModel: AuthViewModel = hiltViewModel(), navController: NavController) {

    var category by rememberSaveable { mutableStateOf("") }
    var amount by rememberSaveable { mutableStateOf("") }
    var tags by rememberSaveable { mutableStateOf("") }
    var date by rememberSaveable { mutableStateOf("") }



    Column(
        Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(top = 40.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            Modifier
                .padding(start = 30.dp, end = 30.dp)
                .fillMaxWidth()
                .weight(.1f),
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
                            tags = tags,
                            date = date
                        )
                    )
                    navController.navigate(AllExpense)
                },
                Modifier
                    .size(90.dp, 40.dp),
                enabled = category.isNotEmpty() && amount.isNotEmpty() && date.isNotEmpty()
            ) {
                Text(text = "Add")
            }
        }

        Column(
            Modifier
                .fillMaxWidth()
                .weight(.7f)
                .background(backgroundColor)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            val trueFalse by remember {
                mutableStateOf(false)
            }
            val tagg = viewModel.tagg
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tagg.chunked(4)) { tagRow ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        tagRow.forEach { tag ->
                            TagCard(
                                tag = tag,
                                trueOrFalse = mutableStateOf(trueFalse),
                            ) {
                                tags = it

                            }
                        }
                    }
                }
            }

            TextFieldsCustom(label = "category") {
                category = it
            }
            TextFieldsCustom(label = "amount", keyboardType = KeyboardType.Number) {
                amount = it
            }
            TextFieldsCustom(label = "date") {
                date = it
            }

        }

    }
}
