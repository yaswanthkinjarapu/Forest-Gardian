package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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

data class ReportTemplate(
    val title: String,
    val description: String,
    val format: String,
    val lastGenerated: String
)

@Composable
fun ReportsScreen(
    modifier: Modifier = Modifier
) {
    var generatedMessage by remember { mutableStateOf<String?>(null) }

    val templates = listOf(
        ReportTemplate("Patrol Log Summary (Monthly)", "Includes distance, duration, checkpoints, and GPS trail data.", "PDF / Excel", "Today, 09:30 AM"),
        ReportTemplate("Incident Register & Crime Log", "Complete list of Forest Fire, Illegal Logging, and Poaching incidents.", "PDF / CSV", "Yesterday"),
        ReportTemplate("Officer Patrol Coverage Audit", "Performance metrics for Range Officers and Beat Officers.", "Excel / CSV", "02 Aug 2026"),
        ReportTemplate("Forest AI Risk Analysis Report", "Predicted fire hotspots, dry biomass index, and AI vision logs.", "PDF", "01 Aug 2026")
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0D1F15))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF142B1E))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text("Official Report Export Portal", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Generate authenticated PDF, Excel, and CSV documents for state headquarters.", fontSize = 12.sp, color = Color(0xFFA5D6A7))
                }
            }
        }

        items(templates) { template ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF142B1E))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(template.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF1B3D2A)
                        ) {
                            Text(template.format, fontSize = 10.sp, color = Color(0xFF2ECC71), modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(template.description, fontSize = 12.sp, color = Color(0xFFA5D6A7))
                    Text("Last generated: ${template.lastGenerated}", fontSize = 10.sp, color = Color.Gray)

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { generatedMessage = "Exported '${template.title}' as PDF document." },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2ECC71)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = Color(0xFF0D1F15), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Export PDF", fontSize = 11.sp, color = Color(0xFF0D1F15), fontWeight = FontWeight.Bold)
                        }
                        OutlinedButton(
                            onClick = { generatedMessage = "Exported '${template.title}' as Excel Spreadsheet." },
                            border = ButtonDefaults.outlinedButtonBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF2ECC71))),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.TableChart, contentDescription = null, tint = Color(0xFF2ECC71), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Export Excel", fontSize = 11.sp, color = Color.White)
                        }
                    }
                }
            }
        }
    }

    if (generatedMessage != null) {
        AlertDialog(
            onDismissRequest = { generatedMessage = null },
            title = { Text("Report Generated Successfully") },
            text = { Text(generatedMessage!!) },
            confirmButton = {
                Button(onClick = { generatedMessage = null }) {
                    Text("Open / Share File")
                }
            }
        )
    }
}
