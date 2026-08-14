package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.*
import com.example.ui.components.NativeMapView

@Composable
fun DashboardScreen(
    currentRole: UserRole,
    activePatrol: Patrol?,
    incidents: List<Incident>,
    geofences: List<GeofenceZone>,
    onNavigate: (String) -> Unit,
    onStartPatrolClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0D1F15))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. AI Risk Advisory Header Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A2A)),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF00E676)))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0x3300E676),
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color(0xFF00E676))
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "AI FORESTRY ADVISORY (GEMINI)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00E676)
                        )
                        Text(
                            text = "High temperature (36.4°C) & low humidity in Teppakadu Beat. 78% Fire Risk. Drone sweep suggested.",
                            fontSize = 13.sp,
                            color = Color.White
                        )
                    }
                    IconButton(onClick = { onNavigate("ai") }) {
                        Icon(Icons.Default.ChevronRight, contentDescription = "View AI", tint = Color.White)
                    }
                }
            }
        }

        // 2. Role-Based Primary Action / Status Section
        item {
            when (currentRole) {
                UserRole.FOREST_OFFICER -> {
                    // Active Patrol Card for Officer
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF142B1E)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Active Foot Patrol Status", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    Text("Kargudi Range • Teppakadu Beat", fontSize = 12.sp, color = Color(0xFFA5D6A7))
                                }
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (activePatrol?.status == PatrolStatus.ACTIVE) Color(0xFF2ECC71) else Color(0xFFE67E22)
                                ) {
                                    Text(
                                        text = activePatrol?.status?.name ?: "READY",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0D1F15),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("Distance", fontSize = 11.sp, color = Color(0xFFA5D6A7))
                                    Text("${String.format("%.2f", (activePatrol?.distanceMeters ?: 0.0) / 1000.0)} km", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                                Column {
                                    Text("Duration", fontSize = 11.sp, color = Color(0xFFA5D6A7))
                                    Text("${(activePatrol?.durationSeconds ?: 0) / 60} mins", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                                Column {
                                    Text("Checkpoints", fontSize = 11.sp, color = Color(0xFFA5D6A7))
                                    Text("${activePatrol?.checkedCheckpoints ?: 0} / 4", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = onStartPatrolClick,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2ECC71)),
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.DirectionsWalk, contentDescription = null, tint = Color(0xFF0D1F15))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (activePatrol?.status == PatrolStatus.ACTIVE) "OPEN ACTIVE PATROL MONITOR" else "START NEW PATROL",
                                    color = Color(0xFF0D1F15),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                UserRole.RANGE_OFFICER -> {
                    // Range Officer Dashboard Overview Cards
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF142B1E))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("Active Officers", fontSize = 12.sp, color = Color(0xFFA5D6A7))
                                Text("12 / 15", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text("8 On Patrol", fontSize = 10.sp, color = Color(0xFF2ECC71))
                            }
                        }
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF142B1E))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("Pending Approvals", fontSize = 12.sp, color = Color(0xFFA5D6A7))
                                Text("3 Reports", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD54F))
                                Text("2 Logging Stacks", fontSize = 10.sp, color = Color(0xFFE67E22))
                            }
                        }
                    }
                }

                UserRole.ADMIN -> {
                    // System Admin Overview Cards
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF142B1E))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("Forest Divisions", fontSize = 12.sp, color = Color(0xFFA5D6A7))
                                Text("4 Circles", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text("18 Ranges Active", fontSize = 10.sp, color = Color(0xFF81C784))
                            }
                        }
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF142B1E))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("Total Personnel", fontSize = 12.sp, color = Color(0xFFA5D6A7))
                                Text("142 Users", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text("Role-Based Sync On", fontSize = 10.sp, color = Color(0xFF2ECC71))
                            }
                        }
                    }
                }
            }
        }

        // 3. Quick Shortcuts Grid
        item {
            Text("Field Operations Hub", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFF1B3D2A),
                    modifier = Modifier.weight(1f).clickable { onNavigate("incidents") }
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.ReportProblem, contentDescription = null, tint = Color(0xFFE74C3C), modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Report Incident", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                }
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFF1B3D2A),
                    modifier = Modifier.weight(1f).clickable { onNavigate("map") }
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Map, contentDescription = null, tint = Color(0xFF2ECC71), modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Live Map", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                }
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFF1B3D2A),
                    modifier = Modifier.weight(1f).clickable { onNavigate("chat") }
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = null, tint = Color(0xFF2980B9), modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Range Comms", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        // 4. Interactive Live Map Preview
        item {
            Text("Forest Sector Map Preview", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            NativeMapView(
                incidents = incidents,
                geofences = geofences,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 5. Recent Reported Incidents Feed
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Recent Incident Logs", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                TextButton(onClick = { onNavigate("incidents") }) {
                    Text("View All (${incidents.size})", color = Color(0xFF2ECC71))
                }
            }
        }

        items(incidents.take(3)) { incident ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF142B1E))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = when (incident.severity) {
                            IncidentSeverity.CRITICAL -> Color(0xFFE74C3C)
                            IncidentSeverity.HIGH -> Color(0xFFE67E22)
                            else -> Color(0xFF2980B9)
                        },
                        modifier = Modifier.size(10.dp)
                    ) {}
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(incident.title, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                        Text("${incident.category.label} • ${incident.rangeName}", fontSize = 11.sp, color = Color(0xFFA5D6A7))
                    }
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFF1B3D2A)
                    ) {
                        Text(
                            text = incident.status.name,
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
