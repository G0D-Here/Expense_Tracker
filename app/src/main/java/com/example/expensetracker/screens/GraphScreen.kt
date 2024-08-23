package com.example.expensetracker.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.expensetracker.data.Resource
import com.example.expensetracker.ui_constants.backgroundColor
import com.example.expensetracker.ui_constants.expenseCardColor
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
                    Modifier
                        .fillMaxSize()
                        .background(backgroundColor),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val pieChartData = viewModel.aggregateExpenses(it.result)
                    CustomPieChart(
                        data = pieChartData.toMutableMap(),
                        modifier = Modifier.size(200.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CustomPieChart(data: Map<String, Double>, modifier: Modifier = Modifier) {
    val colors = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Cyan)
    val totalValue = data.values.sum()
    val radius = 300f // Outer radius

    var selectedSlice by remember { mutableIntStateOf(-1) }

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(contentAlignment = Alignment.Center) {
            // Pie Chart
            Canvas(modifier = modifier) {
                var startAngle = 0.0

                data.entries.forEachIndexed { index, entry ->
                    val sweepAngle = (entry.value.toFloat() / totalValue) * 360f
                    val isSelected = selectedSlice == index
                    val sliceRadius =
                        if (isSelected) radius + 20 else radius // Increase radius if selected

                    drawArc(
                        color = colors[index % colors.size],
                        startAngle = startAngle.toFloat(),
                        sweepAngle = sweepAngle.toFloat(),
                        useCenter = true,
                        size = Size(sliceRadius * 2, sliceRadius * 2),
                        topLeft = Offset(
                            (size.width - sliceRadius * 2) / 2,
                            (size.height - sliceRadius * 2) / 2
                        )
                    )
                    startAngle += sweepAngle
                }
            }

            Canvas(modifier = Modifier
                .size(radius.dp)
                .background(Color.Transparent)) {
                drawCircle(
                    color = Color.White, 
                    radius = radius * 0.6f,
                    center = Offset(x = size.width / 2, y = size.height / 2)
                )
            }

            Text(
                text = "Total: Rs. $totalValue",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val chunks = data.entries.chunked(4)

        if (selectedSlice != -1) {
            Text(
                text = "Amount: Rs. ${data.values.toList()[selectedSlice]}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(chunks) { chunk ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    chunk.forEachIndexed { _, entry ->
                        val color = colors[(data.entries.indexOf(entry)) % colors.size]
                        Card(
                            Modifier
                                .padding(4.dp)
                                .background(expenseCardColor),
                            elevation = CardDefaults.outlinedCardElevation(2.dp)
                        ) {
                            Box {
                                Text(
                                    text = entry.key,
                                    modifier = Modifier
                                        .clickable {
                                            selectedSlice = data.entries.indexOf(entry)
                                        }
                                        .background(
                                            color = expenseCardColor,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .padding(8.dp),

                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }
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


