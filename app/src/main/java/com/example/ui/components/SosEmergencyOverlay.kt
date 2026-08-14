package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.SosAlert

@Composable
fun SosEmergencyOverlay(
    activeSos: SosAlert?,
    onTriggerSos: () -> Unit,
    onCancelSos: (pin: String) -> Boolean
) {
    var showCancelDialog by remember { mutableStateOf(false) }
    var pinText by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf(false) }

    if (activeSos != null) {
        // Full screen critical emergency overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF8B0000),
                            Color(0xFF2B0000)
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFFF1744),
                    modifier = Modifier.size(120.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "SOS Emergency Active",
                            tint = Color.White,
                            modifier = Modifier.size(64.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "🚨 SOS EMERGENCY BROADCAST",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "High-Frequency GPS Tracking Active",
                    fontSize = 14.sp,
                    color = Color(0xFFFFCDD2)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0x33FFFFFF)),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFFF5252)))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("Officer Identity:", color = Color.White, fontWeight = FontWeight.SemiBold)
                            Text(activeSos.officerName, color = Color(0xFFFFCDD2))
                        }
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("GPS Coordinates:", color = Color.White, fontWeight = FontWeight.SemiBold)
                            Text("${String.format("%.4f", activeSos.latitude)}, ${String.format("%.4f", activeSos.longitude)}", color = Color(0xFFFFCDD2))
                        }
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("Device Battery:", color = Color.White, fontWeight = FontWeight.SemiBold)
                            Text("${activeSos.batteryLevel}%", color = Color(0xFFFFCDD2))
                        }
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("Status:", color = Color.White, fontWeight = FontWeight.SemiBold)
                            Text("Broadcasting to HQ & Nearby Units", color = Color(0xFFFFEA00), fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { showCancelDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF8B0000))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("CANCEL SOS (REQUIRES PIN)", color = Color(0xFF8B0000), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

            if (showCancelDialog) {
                AlertDialog(
                    onDismissRequest = { showCancelDialog = false },
                    title = { Text("Authenticate to Cancel SOS") },
                    text = {
                        Column {
                            Text("Enter 4-digit PIN (Default: 1234) or use Fingerprint verification.")
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedTextField(
                                value = pinText,
                                onValueChange = {
                                    if (it.length <= 4) {
                                        pinText = it
                                        pinError = false
                                    }
                                },
                                label = { Text("4-Digit PIN") },
                                isError = pinError,
                                visualTransformation = PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth()
                            )
                            if (pinError) {
                                Text("Invalid PIN. Please enter 1234.", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                            }
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                val success = onCancelSos(pinText)
                                if (success) {
                                    showCancelDialog = false
                                    pinText = ""
                                } else {
                                    pinError = true
                                }
                            }
                        ) {
                            Text("Deactivate Emergency")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showCancelDialog = false }) {
                            Text("Dismiss")
                        }
                    }
                )
            }
        }
    }
}
