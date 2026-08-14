package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.GeofenceZone
import com.example.data.models.Incident
import com.example.ui.components.NativeMapView

@Composable
fun MapScreen(
    incidents: List<Incident>,
    geofences: List<GeofenceZone>,
    modifier: Modifier = Modifier
) {
    var showHeatmap by remember { mutableStateOf(false) }
    var selectedLayer by remember { mutableStateOf("ALL") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0D1F15))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Map Layers Filter Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Forest Geographic Map", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            FilterChip(
                selected = showHeatmap,
                onClick = { showHeatmap = !showHeatmap },
                label = { Text("Fire Heatmap", fontSize = 11.sp) },
                leadingIcon = { Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = Color(0xFFE74C3C)) }
            )
        }

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val layers = listOf("ALL", "Patrol Routes", "Fire Hotspots", "Geofence Zones", "Active Officers")
            items(layers) { layer ->
                FilterChip(
                    selected = selectedLayer == layer,
                    onClick = { selectedLayer = layer },
                    label = { Text(layer, fontSize = 11.sp) }
                )
            }
        }

        // Full Interactive Map Component
        NativeMapView(
            incidents = incidents,
            geofences = geofences,
            showHeatmap = showHeatmap,
            selectedFilter = selectedLayer,
            modifier = Modifier.weight(1f)
        )

        // Geofence Zone Inspector Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF142B1E))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("Monitored Geofence Boundaries (${geofences.size})", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(6.dp))
                geofences.forEach { zone ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Shield, contentDescription = null, tint = Color(0xFF2ECC71), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(zone.name, fontSize = 12.sp, color = Color.White)
                        }
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFF1B3D2A)
                        ) {
                            Text(
                                text = "${zone.type.name} • ${zone.riskLevel}",
                                fontSize = 10.sp,
                                color = Color(0xFF81C784),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
