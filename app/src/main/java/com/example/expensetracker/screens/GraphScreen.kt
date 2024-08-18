package com.example.expensetracker.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.expensetracker.data.Resource
import com.example.expensetracker.viewmodel.AuthViewModel

@Composable
fun GraphScreen(viewModel: AuthViewModel = hiltViewModel(), navController: NavController) {
    val expenses = viewModel.expenses.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getExpenses()
    }

    expenses.value.let {
        when (it) {
            is Resource.Failure -> {
            }

            Resource.Loading -> {

            }

            is Resource.Success -> {

                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Graph Screen")
                    val pieChartData = viewModel.aggregateExpenses(it.result)
                    CustomPieChart(data = pieChartData.toMutableMap(), modifier = Modifier.size(200.dp))
                }
            }
        }
    }
}
@Composable
fun CustomPieChart(data: Map<String, Double>, modifier: Modifier = Modifier) {
    val colors = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Cyan)
    val totalValue = data.values.sum()
    val radius = 150f // You can adjust this radius as needed

    Canvas(modifier = modifier) {
        var startAngle = 0.0

        data.entries.forEachIndexed { index, entry ->
            val sweepAngle = (entry.value.toFloat() / totalValue) * 360f

            // Draw the arc
            drawArc(
                color = colors[index % colors.size],
                startAngle = startAngle.toFloat(),
                sweepAngle = sweepAngle.toFloat(),
                useCenter = true,
                size = Size(size.width, size.height)
            )

            // Calculate the midpoint angle
            val midAngle = startAngle + (sweepAngle / 2)

            // Calculate the position for the category name
            val x = (size.width / 2) + radius * kotlin.math.cos(Math.toRadians(midAngle)).toFloat()
            val y = (size.height / 2) + radius * kotlin.math.sin(Math.toRadians(midAngle)).toFloat()

            // Draw the category name at the calculated position
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    entry.key,
                    x,
                    y,
                    android.graphics.Paint().apply {
                        color = android.graphics.Color.BLACK
                        textAlign = android.graphics.Paint.Align.CENTER
                        textSize = 30f
                    }
                )
            }

            // Update startAngle for the next arc
            startAngle += sweepAngle
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun CustomPieChartPreview() {
    val sampleData = mapOf(
        "Food" to 300.0,
        "Transport" to 150.0,
        "Entertainment" to 100.0,
        "Bills" to 250.0
    )
    CustomPieChart(data = sampleData.toMutableMap(), modifier = Modifier.size(200.dp))
}


