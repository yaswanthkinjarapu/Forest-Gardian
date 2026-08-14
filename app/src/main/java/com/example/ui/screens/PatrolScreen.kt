package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.data.models.Checkpoint
import com.example.data.models.Patrol
import com.example.data.models.PatrolStatus
import com.example.ui.components.NativeMapView

@Composable
fun PatrolScreen(
    activePatrol: Patrol?,
    checkpoints: List<Checkpoint>,
    onStartPatrol: () -> Unit,
    onPausePatrol: () -> Unit,
    onResumePatrol: () -> Unit,
    onEndPatrol: () -> Unit,
    onCheckInCheckpoint: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showEndDialog by remember { mutableStateOf(false) }
    var showQrDialog by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0D1F15))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Patrol Lifecycle Controls Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF142B1E)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Forest Foot Patrol Monitor", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Range: Kargudi • Beat: Teppakadu", fontSize = 12.sp, color = Color(0xFFA5D6A7))
                        }
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = when (activePatrol?.status) {
                                PatrolStatus.ACTIVE -> Color(0xFF2ECC71)
                                PatrolStatus.PAUSED -> Color(0xFFFFD54F)
                                else -> Color(0xFF9E9E9E)
                            }
                        ) {
                            Text(
                                text = activePatrol?.status?.name ?: "INACTIVE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0D1F15),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Auto-Computed Telemetry Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1C3828))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("Distance Covered", fontSize = 11.sp, color = Color(0xFFA5D6A7))
                                Text("${String.format("%.2f", (activePatrol?.distanceMeters ?: 0.0) / 1000.0)} km", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1C3828))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("Active Duration", fontSize = 11.sp, color = Color(0xFFA5D6A7))
                                Text("${(activePatrol?.durationSeconds ?: 0) / 60}m ${(activePatrol?.durationSeconds ?: 0) % 60}s", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1C3828))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("Avg Speed", fontSize = 11.sp, color = Color(0xFFA5D6A7))
                                Text("${String.format("%.1f", activePatrol?.averageSpeedKmh ?: 3.2)} km/h", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Patrol Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (activePatrol?.status != PatrolStatus.ACTIVE && activePatrol?.status != PatrolStatus.PAUSED) {
                            Button(
                                onClick = onStartPatrol,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2ECC71)),
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color(0xFF0D1F15))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("START PATROL", color = Color(0xFF0D1F15), fontWeight = FontWeight.Bold)
                            }
                        } else {
                            if (activePatrol?.status == PatrolStatus.ACTIVE) {
                                Button(
                                    onClick = onPausePatrol,
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD54F)),
                                    modifier = Modifier.weight(1f).height(48.dp),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(Icons.Default.Pause, contentDescription = null, tint = Color.Black)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("PAUSE", color = Color.Black, fontWeight = FontWeight.Bold)
                                }
                            } else {
                                Button(
                                    onClick = onResumePatrol,
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2ECC71)),
                                    modifier = Modifier.weight(1f).height(48.dp),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color(0xFF0D1F15))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("RESUME", color = Color(0xFF0D1F15), fontWeight = FontWeight.Bold)
                                }
                            }

                            Button(
                                onClick = { showEndDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE74C3C)),
                                modifier = Modifier.weight(1f).height(48.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Stop, contentDescription = null, tint = Color.White)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("END PATROL", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // 2. Live Route Tracking Map
        item {
            Text("Live Patrol Route & GPS Tracking", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            NativeMapView(
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 3. Checkpoints Verification List (QR / NFC / GPS)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Assigned Patrol Checkpoints", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("${checkpoints.count { it.isChecked }} / ${checkpoints.size} Checked", color = Color(0xFF2ECC71), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        items(checkpoints) { checkpoint ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (checkpoint.isChecked) Color(0xFF1B3D2A) else Color(0xFF142B1E)
                )
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Surface(
                            shape = CircleShape,
                            color = if (checkpoint.isChecked) Color(0xFF2ECC71) else Color(0xFF294E38),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = if (checkpoint.isChecked) Icons.Default.Check else Icons.Default.Place,
                                    contentDescription = null,
                                    tint = if (checkpoint.isChecked) Color(0xFF0D1F15) else Color.White
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(checkpoint.name, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                            Text("Lat: ${checkpoint.latitude} • Lng: ${checkpoint.longitude}", fontSize = 11.sp, color = Color(0xFFA5D6A7))
                        }
                    }

                    if (!checkpoint.isChecked) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            IconButton(onClick = { showQrDialog = checkpoint.id }) {
                                Icon(Icons.Default.QrCodeScanner, contentDescription = "Scan QR", tint = Color(0xFF2ECC71))
                            }
                            Button(
                                onClick = { onCheckInCheckpoint(checkpoint.id, "GPS-Proximity") },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2ECC71)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Check In", fontSize = 11.sp, color = Color(0xFF0D1F15), fontWeight = FontWeight.Bold)
                            }
                        }
                    } else {
                        Text("Checked In", fontSize = 11.sp, color = Color(0xFF2ECC71), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // End Patrol Dialog
    if (showEndDialog) {
        AlertDialog(
            onDismissRequest = { showEndDialog = false },
            title = { Text("End Forest Patrol") },
            text = { Text("Are you sure you want to end this patrol? All GPS points will be synced to the range server.") },
            confirmButton = {
                Button(
                    onClick = {
                        showEndDialog = false
                        onEndPatrol()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE74C3C))
                ) {
                    Text("End & Submit")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEndDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // QR Code Scanner Simulator Dialog
    if (showQrDialog != null) {
        val cpId = showQrDialog!!
        AlertDialog(
            onDismissRequest = { showQrDialog = null },
            title = { Text("Scan Checkpoint QR / NFC Tag") },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.Black,
                        modifier = Modifier.size(160.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.QrCode, contentDescription = null, tint = Color(0xFF2ECC71), modifier = Modifier.size(90.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Position QR code inside frame or tap NFC tag.", fontSize = 12.sp, color = Color.Gray)
                }
            },
            confirmButton = {
                Button(onClick = {
                    onCheckInCheckpoint(cpId, "QR-Scan")
                    showQrDialog = null
                }) {
                    Text("Simulate QR Check-In")
                }
            },
            dismissButton = {
                TextButton(onClick = { showQrDialog = null }) {
                    Text("Close Scanner")
                }
            }
        )
    }
}
