package com.example.expensetracker.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

//Gradient text
val text: @Composable (text: String, modifier: Modifier ) -> Unit =
    { text, modifier ->
        Text(
            text = text,
            fontSize = 18.sp,
            modifier = modifier
                .drawWithCache {
                    val brush = Brush.linearGradient(
                        listOf(
                            Color(0xFFE4DEF8),
                            Color(0xFF88C5F7)
                        )
                    )
                    onDrawBehind {
                        drawRoundRect(
                            brush,
                            cornerRadius = CornerRadius(10.dp.toPx())
                        )
                    }
                }
                .padding(horizontal = 10.dp, vertical = 10.dp)
        )
    }

var expenseId = ""


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldsCustom(
    modifier: Modifier = Modifier,
    note:String = "",
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    string: (String) -> Unit
) {

    var email by remember {
        mutableStateOf(note)
    }

    Row(
        modifier = modifier
            .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            Modifier
                .padding(10.dp)
                .clip(RoundedCornerShape(20.dp))
                .height(60.dp)
                .background(Color.White)
        ) {
            TextField(
                value = email,
                onValueChange = {
                    email = it
                    string(it)
                },
                Modifier
                    .background(Color.White)
                    .fillMaxWidth(),
                label = { Text(text = "Enter $label here") },
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.White
                )
            )
        }
    }

}