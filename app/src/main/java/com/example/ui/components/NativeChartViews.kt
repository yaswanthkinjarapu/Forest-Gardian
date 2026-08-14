package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NativeBarChart(
    data: List<Pair<String, Int>>,
    modifier: Modifier = Modifier,
    barColor: Color = Color(0xFF2ECC71)
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF142B1E))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Incident Statistics by Category",
                fontSize = 15.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))

            val maxVal = (data.maxOfOrNull { it.second } ?: 1).toFloat()

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                val width = size.width
                val height = size.height
                val barWidth = width / (data.size * 1.8f)
                val spacing = barWidth * 0.8f

                data.forEachIndexed { i, pair ->
                    val barHeight = (pair.second / maxVal) * (height - 30f)
                    val x = i * (barWidth + spacing) + spacing / 2
                    val y = height - barHeight - 20f

                    // Draw Bar
                    drawRoundRect(
                        color = when (pair.first) {
                            "Fire" -> Color(0xFFE74C3C)
                            "Logging" -> Color(0xFFE67E22)
                            "Poaching" -> Color(0xFF9B59B6)
                            else -> barColor
                        },
                        topLeft = Offset(x, y),
                        size = Size(barWidth, barHeight),
                        cornerRadius = CornerRadius(8f, 8f)
                    )
                }
            }

            // Labels Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                data.forEach { pair ->
                    Text(
                        text = pair.first,
                        fontSize = 10.sp,
                        color = Color(0xFFA5D6A7)
                    )
                }
            }
        }
    }
}

@Composable
fun NativeLineChart(
    dataPoints: List<Float>,
    modifier: Modifier = Modifier,
    lineColor: Color = Color(0xFF00E676)
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF142B1E))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Patrol Coverage Distance (km / Day)",
                fontSize = 15.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))

            val maxVal = (dataPoints.maxOrNull() ?: 1f)

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                val width = size.width
                val height = size.height
                val stepX = width / (dataPoints.size - 1)

                val path = Path()
                dataPoints.forEachIndexed { i, value ->
                    val x = i * stepX
                    val y = height - ((value / maxVal) * (height - 20f)) - 10f

                    if (i == 0) {
                        path.moveTo(x, y)
                    } else {
                        path.lineTo(x, y)
                    }

                    drawCircle(
                        color = lineColor,
                        radius = 5f,
                        center = Offset(x, y)
                    )
                }

                drawPath(
                    path = path,
                    color = lineColor,
                    style = Stroke(width = 4f)
                )
            }
        }
    }
}
