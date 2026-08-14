package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.*

@Composable
fun NativeMapView(
    patrolPoints: List<GpsPoint> = emptyList(),
    incidents: List<Incident> = emptyList(),
    geofences: List<GeofenceZone> = emptyList(),
    showHeatmap: Boolean = false,
    selectedFilter: String = "ALL",
    onMarkerClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(380.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0D2216)),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF234B34)))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Interactive Map Canvas
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            scale = (scale * zoom).coerceIn(0.8f, 3f)
                            offset += pan
                        }
                    }
            ) {
                val width = size.width
                val height = size.height

                // 1. Forest Topo Grid Background
                val gridSize = 60f * scale
                var x = (offset.x % gridSize)
                while (x < width) {
                    drawLine(
                        color = Color(0xFF1B3D2A),
                        start = Offset(x, 0f),
                        end = Offset(x, height),
                        strokeWidth = 1f
                    )
                    x += gridSize
                }
                var y = (offset.y % gridSize)
                while (y < height) {
                    drawLine(
                        color = Color(0xFF1B3D2A),
                        start = Offset(0f, y),
                        end = Offset(width, y),
                        strokeWidth = 1f
                    )
                    y += gridSize
                }

                // 2. Contour Elevation Lines (Stylized Forest Contour)
                val contourPath = Path().apply {
                    moveTo(0f, height * 0.3f)
                    cubicTo(width * 0.3f, height * 0.1f, width * 0.6f, height * 0.5f, width, height * 0.2f)
                    moveTo(0f, height * 0.7f)
                    cubicTo(width * 0.4f, height * 0.9f, width * 0.7f, height * 0.4f, width, height * 0.8f)
                }
                drawPath(
                    path = contourPath,
                    color = Color(0xFF2D5A3F),
                    style = Stroke(width = 2f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))
                )

                // 3. Geofence Zones (Circle & Polygon)
                geofences.forEachIndexed { idx, zone ->
                    val cx = (width * (0.35f + idx * 0.3f)) + offset.x
                    val cy = (height * (0.4f + idx * 0.2f)) + offset.y
                    val radius = 110f * scale

                    // Draw semi-transparent fill
                    drawCircle(
                        color = if (zone.riskLevel == "HIGH") Color(0x33E74C3C) else Color(0x332ECC71),
                        radius = radius,
                        center = Offset(cx, cy)
                    )
                    drawCircle(
                        color = if (zone.riskLevel == "HIGH") Color(0xFFE74C3C) else Color(0xFF2ECC71),
                        radius = radius,
                        center = Offset(cx, cy),
                        style = Stroke(width = 2.5f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f))
                    )
                }

                // 4. Heatmap Overlay if enabled
                if (showHeatmap) {
                    val heatmapPoints = listOf(
                        Offset(width * 0.3f + offset.x, height * 0.4f + offset.y),
                        Offset(width * 0.65f + offset.x, height * 0.3f + offset.y),
                        Offset(width * 0.45f + offset.x, height * 0.7f + offset.y)
                    )
                    heatmapPoints.forEach { center ->
                        drawCircle(
                            color = Color(0x55FF5252),
                            radius = 140f * scale,
                            center = center
                        )
                        drawCircle(
                            color = Color(0x88FF9800),
                            radius = 80f * scale,
                            center = center
                        )
                    }
                }

                // 5. Live Patrol Polyline Route
                val defaultRoute = listOf(
                    Offset(width * 0.15f + offset.x, height * 0.8f + offset.y),
                    Offset(width * 0.3f + offset.x, height * 0.65f + offset.y),
                    Offset(width * 0.45f + offset.x, height * 0.5f + offset.y),
                    Offset(width * 0.6f + offset.x, height * 0.55f + offset.y),
                    Offset(width * 0.75f + offset.x, height * 0.35f + offset.y)
                )

                val routePath = Path()
                if (defaultRoute.isNotEmpty()) {
                    routePath.moveTo(defaultRoute[0].x, defaultRoute[0].y)
                    for (i in 1 until defaultRoute.size) {
                        routePath.lineTo(defaultRoute[i].x, defaultRoute[i].y)
                    }
                }

                // Draw glowing polyline
                drawPath(
                    path = routePath,
                    color = Color(0x4400E676),
                    style = Stroke(width = 10f * scale)
                )
                drawPath(
                    path = routePath,
                    color = Color(0xFF00E676),
                    style = Stroke(width = 4f * scale)
                )

                // Checkpoint markers along route
                defaultRoute.forEachIndexed { i, pt ->
                    drawCircle(
                        color = if (i <= 2) Color(0xFF00E676) else Color(0xFFD4AF37),
                        radius = 8f * scale,
                        center = pt
                    )
                }

                // 6. Incident Markers
                val mockIncidentCoords = listOf(
                    Offset(width * 0.38f + offset.x, height * 0.45f + offset.y) to Color(0xFFE74C3C),
                    Offset(width * 0.68f + offset.x, height * 0.28f + offset.y) to Color(0xFFE67E22),
                    Offset(width * 0.52f + offset.x, height * 0.62f + offset.y) to Color(0xFF2980B9)
                )

                mockIncidentCoords.forEach { (pt, color) ->
                    // Outer pulse
                    drawCircle(
                        color = color.copy(alpha = 0.3f),
                        radius = 18f * scale,
                        center = pt
                    )
                    // Inner pin
                    drawCircle(
                        color = color,
                        radius = 10f * scale,
                        center = pt
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 4f * scale,
                        center = pt
                    )
                }
            }

            // Map Layer Controls Overlay (Top Right)
            Column(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SmallFloatingActionButton(
                    onClick = { scale = (scale + 0.3f).coerceAtMost(3f) },
                    containerColor = Color(0xFF143020),
                    contentColor = Color(0xFF00E676)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Zoom In")
                }
                SmallFloatingActionButton(
                    onClick = { scale = (scale - 0.3f).coerceAtLeast(0.8f) },
                    containerColor = Color(0xFF143020),
                    contentColor = Color(0xFF00E676)
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Zoom Out")
                }
            }

            // Legend Overlay (Bottom Left)
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
                    .background(Color(0xDD0F281B), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).background(Color(0xFF00E676), CircleShape))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Patrol Route", fontSize = 11.sp, color = Color.White)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).background(Color(0xFFE74C3C), CircleShape))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Fire Incident", fontSize = 11.sp, color = Color.White)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).background(Color(0xFFE67E22), CircleShape))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Logging", fontSize = 11.sp, color = Color.White)
                }
            }
        }
    }
}
