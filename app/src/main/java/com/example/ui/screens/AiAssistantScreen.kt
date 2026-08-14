package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.data.models.FireRiskPrediction
import com.example.data.models.ImageClassificationResult

@Composable
fun AiAssistantScreen(
    fireRisk: FireRiskPrediction?,
    imageResult: ImageClassificationResult?,
    onRunAiAnalysis: () -> Unit,
    onClassifyImage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var queryText by remember { mutableStateOf("") }
    var aiResponse by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0D1F15))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Disclaimer Header Banner (Mandatory as per Section 4.9)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A2A)),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFFFD54F)))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFFFFD54F), modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("AI FORESTRY ADVISORY SYSTEM", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD54F))
                        Text("All AI risk scores, hotspot predictions, and image classifications are advisory and require human officer confirmation.", fontSize = 11.sp, color = Color.White)
                    }
                }
            }
        }

        // 1. Fire Hazard Predictor Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF142B1E))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = Color(0xFFE74C3C))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Fire-Prone Zone Predictor", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFE74C3C)
                        ) {
                            Text("${fireRisk?.riskScore ?: 78}% RISK", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Target Sector: ${fireRisk?.zoneName ?: "Teppakadu Beat"}", fontSize = 13.sp, color = Color(0xFFA5D6A7))
                    Text(fireRisk?.summary ?: "High ambient temperature (36.4°C) with dry teak forest biomass. Potential ignition probability elevated.", fontSize = 12.sp, color = Color.White)

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF1C3828)
                    ) {
                        Text(
                            text = "💡 Recommended Action: ${fireRisk?.recommendation ?: "Pre-position fire suppression quad-bikes & drone aerial sweep."}",
                            fontSize = 11.sp,
                            color = Color(0xFF00E676),
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
        }

        // 2. Illegal Logging Hotspot Predictor
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF142B1E))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Park, contentDescription = null, tint = Color(0xFFE67E22))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Illegal Logging Hotspot Predictor", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Highest Risk Beat: North Boundary Segment 3", fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                    Text("Probability: 84% based on historical night acoustic signatures & road proximity.", fontSize = 12.sp, color = Color(0xFFA5D6A7))

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Suggested Patrol Route: Route B-North", fontSize = 11.sp, color = Color(0xFF2ECC71))
                        Button(
                            onClick = onRunAiAnalysis,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2ECC71)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Re-Analyze", fontSize = 11.sp, color = Color(0xFF0D1F15), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // 3. Species & Fire Image Classification Tool
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF142B1E))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.RemoveRedEye, contentDescription = null, tint = Color(0xFF2980B9))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("AI Multimodal Vision Classifier", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Detects: Fire vs Smoke vs False Alarm, Species Identification", fontSize = 12.sp, color = Color(0xFFA5D6A7))

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { onClassifyImage("fire") },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B3D2A)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Test Fire Image", fontSize = 11.sp, color = Color.White)
                        }
                        Button(
                            onClick = { onClassifyImage("species") },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B3D2A)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Test Tiger Image", fontSize = 11.sp, color = Color.White)
                        }
                    }

                    if (imageResult != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF1C3828)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("Result: ${imageResult.category} (${imageResult.confidencePercent}% Confidence)", fontWeight = FontWeight.Bold, color = Color(0xFF2ECC71), fontSize = 13.sp)
                                Text("Identified: ${imageResult.speciesOrAnomaly}", fontSize = 12.sp, color = Color.White)
                                Text(imageResult.summary, fontSize = 11.sp, color = Color(0xFFA5D6A7))
                            }
                        }
                    }
                }
            }
        }
    }
}
