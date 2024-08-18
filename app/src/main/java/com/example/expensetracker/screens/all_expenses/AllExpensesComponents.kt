package com.example.expensetracker.screens.all_expenses

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.expensetracker.R
import com.example.expensetracker.data.utils.Expense
import com.example.expensetracker.ui_constants.backgroundColor
import com.example.expensetracker.ui_constants.expenseCardColor

@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showBackground = true)
@Composable
fun SearchAndProfileBar(profile: () -> Unit = {}) {
    var text by remember { mutableStateOf("") }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp, start = 10.dp, end = 10.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color.White
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        ) {
            Row(
                Modifier

                    .clip(RoundedCornerShape(20.dp))
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    Modifier.fillMaxWidth(),
                    placeholder = { Text(text = "Search expenses...") },
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }

            Surface(
                Modifier
                    .padding(end = 14.dp)
                    .clip(shape = CircleShape)
                    .size(45.dp)
                    .align(Alignment.CenterEnd)
                    .clickable { profile.invoke() },
                shape = CircleShape,
                color = backgroundColor
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.padding(2.dp)
                )
            }
        }

    }
}

@Composable
fun FilterButtons(
    sortBy: (String) -> Unit = {}
) {
    var selectedButton by remember { mutableStateOf("date") }

    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(text = "Sort By", fontSize = 10.sp)

        Card(
            Modifier
                .clip(RoundedCornerShape(20.dp))
                .clickable {
                    selectedButton = "date"
                    sortBy(selectedButton)

                },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (selectedButton == "date") expenseCardColor else Color.White
            ),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Text(
                text = "Date",
                Modifier.padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp),
                fontSize = 12.sp
            )
        }

        Card(
            Modifier
                .clip(RoundedCornerShape(20.dp))
                .clickable {
                    selectedButton = "amount"
                    sortBy(selectedButton)

                },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (selectedButton == "amount") expenseCardColor else Color.White
            ),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Text(
                text = "Amount",
                Modifier.padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp),
                fontSize = 12.sp
            )
        }

        Card(
            Modifier
                .clip(RoundedCornerShape(20.dp))
                .clickable {
                    selectedButton = "category"
                    sortBy(selectedButton)

                },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (selectedButton == "category") expenseCardColor else Color.White
            ),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Text(
                text = "Category",
                Modifier.padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun ExpenseCard(
    expense: Expense? = null,
    deleteClicked: () -> Unit = {},
    editClicked: () -> Unit
) {

    Card(
        modifier = Modifier
            .padding(top = 16.dp, start = 20.dp, end = 20.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(expenseCardColor),
        elevation = CardDefaults.cardElevation(1.dp),
    ) {
        Column(modifier = Modifier.padding(start = 10.dp, end = 10.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = expense?.category.toString(),
                    modifier = Modifier
                        .padding(start = 4.dp),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Serif,
                    textAlign = TextAlign.Center
                )
                Text(text = "tag: ${expense?.tags}", fontSize = 10.sp)
            }
            Text(
                text = expense?.note.toString(),
                fontSize = 13.sp,
                fontFamily = FontFamily.Serif,
                maxLines = 3,
                letterSpacing = .07.em,
                style = TextStyle(lineHeight = 1.2.em)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "$${expense?.amount}",
                        modifier = Modifier,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = expense?.date.toString(),
                        modifier = Modifier.padding(2.dp),
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Monospace,
                    )
                }
                Row(
                    Modifier.padding(end = 10.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        onClick = { editClicked.invoke() },
                        Modifier.size(40.dp),
                        shape = CircleShape,
                        color = backgroundColor
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                modifier = Modifier.padding(5.dp),
                                painter = painterResource(id = R.drawable.edit_246),
                                contentDescription = "Edit expense"
                            )
                        }

                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        onClick = { deleteClicked.invoke() },
                        Modifier.size(40.dp),
                        shape = CircleShape,
                        color = backgroundColor
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                modifier = Modifier.padding(7.dp),
                                painter = painterResource(id = R.drawable.delete_589),
                                contentDescription = "Delete expense"
                            )
                        }

                    }
                }


            }
        }

    }
}
