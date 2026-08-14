package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.NativeBarChart
import com.example.ui.components.NativeLineChart

@Composable
fun AnalyticsScreen(
    modifier: Modifier = Modifier
) {
    var selectedRange by remember { mutableStateOf("This Week") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0D1F15))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Date Range Selector Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Forest Analytics Dashboard", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val ranges = listOf("Today", "This Week", "This Month", "This Year")
                items(ranges) { range ->
                    FilterChip(
                        selected = selectedRange == range,
                        onClick = { selectedRange = range },
                        label = { Text(range, fontSize = 11.sp) }
                    )
                }
            }
        }

        // Summary KPI Metrics Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF142B1E))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Area Patrolled", fontSize = 11.sp, color = Color(0xFFA5D6A7))
                        Text("1,420 km²", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("+14% vs last week", fontSize = 10.sp, color = Color(0xFF2ECC71))
                    }
                }
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF142B1E))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Active Patrols", fontSize = 11.sp, color = Color(0xFFA5D6A7))
                        Text("48 Completed", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("98.2% Checkpoint Rate", fontSize = 10.sp, color = Color(0xFF2ECC71))
                    }
                }
            }
        }

        // Bar Chart - Incidents Breakdown
        item {
            NativeBarChart(
                data = listOf(
                    "Fire" to 8,
                    "Logging" to 5,
                    "Poaching" to 2,
                    "Wildlife" to 14,
                    "Pollution" to 3
                )
            )
        }

        // Line Chart - Distance Coverage
        item {
            NativeLineChart(
                dataPoints = listOf(12f, 18f, 15f, 24f, 20f, 28f, 32f)
            )
        }
    }
}
