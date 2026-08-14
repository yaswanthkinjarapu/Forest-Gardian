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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun IncidentsScreen(
    incidents: List<Incident>,
    onSubmitIncident: (Incident) -> Unit,
    onSaveDraft: (Incident) -> Unit,
    onApproveIncident: (String) -> Unit,
    currentRole: UserRole,
    modifier: Modifier = Modifier
) {
    var showCreateModal by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(IncidentCategory.FOREST_FIRE) }
    var titleText by remember { mutableStateOf("") }
    var descText by remember { mutableStateOf("") }
    var severity by remember { mutableStateOf(IncidentSeverity.HIGH) }
    var capturedPhotosCount by remember { mutableIntStateOf(2) }
    var hasVoiceNote by remember { mutableStateOf(true) }
    var selectedDetailIncident by remember { mutableStateOf<Incident?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0D1F15))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header & Create Button
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF142B1E))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Forest Incident Management", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("11 Prescribed Categories • Offline Queue", fontSize = 12.sp, color = Color(0xFFA5D6A7))
                        }
                        Button(
                            onClick = { showCreateModal = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE74C3C)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("NEW REPORT", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Incidents List Filter & Summary
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Reported Incidents (${incidents.size})", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("Auto-GPS Tagged", fontSize = 11.sp, color = Color(0xFF2ECC71))
            }
        }

        items(incidents) { incident ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedDetailIncident = incident },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF142B1E))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = when (incident.severity) {
                                    IncidentSeverity.CRITICAL -> Color(0xFFE74C3C)
                                    IncidentSeverity.HIGH -> Color(0xFFE67E22)
                                    IncidentSeverity.MEDIUM -> Color(0xFFFFD54F)
                                    IncidentSeverity.LOW -> Color(0xFF2980B9)
                                },
                                modifier = Modifier.size(10.dp)
                            ) {}
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(incident.category.label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2ECC71))
                        }
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF1B3D2A)
                        ) {
                            Text(
                                text = incident.status.name,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF81C784),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(incident.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(incident.description, fontSize = 12.sp, color = Color(0xFFA5D6A7), maxLines = 2)

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Place, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(2.dp))
                                Text("${String.format("%.4f", incident.latitude)}, ${String.format("%.4f", incident.longitude)}", fontSize = 10.sp, color = Color.Gray)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.PhotoLibrary, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(2.dp))
                                Text("2 Attachments", fontSize = 10.sp, color = Color.Gray)
                            }
                        }

                        if (currentRole == UserRole.RANGE_OFFICER && incident.status == IncidentStatus.SUBMITTED) {
                            Button(
                                onClick = { onApproveIncident(incident.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2ECC71)),
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text("APPROVE", fontSize = 10.sp, color = Color(0xFF0D1F15), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }

    // Create Incident Modal
    if (showCreateModal) {
        AlertDialog(
            onDismissRequest = { showCreateModal = false },
            title = { Text("Report Forest Incident") },
            text = {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Text("1. Select Incident Category:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            IncidentCategory.values().forEach { cat ->
                                FilterChip(
                                    selected = selectedCategory == cat,
                                    onClick = { selectedCategory = cat },
                                    label = { Text(cat.label, fontSize = 10.sp) }
                                )
                            }
                        }
                    }

                    item {
                        OutlinedTextField(
                            value = titleText,
                            onValueChange = { titleText = it },
                            label = { Text("Short Incident Title") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = descText,
                            onValueChange = { descText = it },
                            label = { Text("Detailed Field Description") },
                            modifier = Modifier.fillMaxWidth().height(90.dp)
                        )
                    }

                    item {
                        Text("2. Multi-Media Attachments:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(onClick = { capturedPhotosCount += 1 }) {
                                Icon(Icons.Default.CameraAlt, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Photo ($capturedPhotosCount)")
                            }
                            OutlinedButton(onClick = { hasVoiceNote = true }) {
                                Icon(Icons.Default.Mic, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Voice Note")
                            }
                        }
                    }

                    item {
                        Text("3. Auto-Captured Location:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("📍 Lat: 11.5650, Lng: 76.5380 (Accuracy: 4.2m)", fontSize = 11.sp, color = Color(0xFF2ECC71))
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val newInc = Incident(
                            id = "inc_${System.currentTimeMillis()}",
                            category = selectedCategory,
                            title = titleText.ifEmpty { "${selectedCategory.label} Event" },
                            description = descText.ifEmpty { "Field incident reported via mobile." },
                            latitude = 11.5650,
                            longitude = 76.5380,
                            severity = severity,
                            status = IncidentStatus.SUBMITTED,
                            reporterId = "usr_officer_1",
                            reporterName = "Officer Rajesh Kumar",
                            rangeName = "Kargudi Range"
                        )
                        onSubmitIncident(newInc)
                        showCreateModal = false
                        titleText = ""
                        descText = ""
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE74C3C))
                ) {
                    Text("Submit Report")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        val draft = Incident(
                            id = "draft_${System.currentTimeMillis()}",
                            category = selectedCategory,
                            title = titleText.ifEmpty { "Draft Report" },
                            description = descText,
                            latitude = 11.5650,
                            longitude = 76.5380,
                            severity = severity,
                            status = IncidentStatus.DRAFT,
                            reporterId = "usr_officer_1",
                            reporterName = "Officer Rajesh Kumar",
                            rangeName = "Kargudi Range",
                            isDraft = true
                        )
                        onSaveDraft(draft)
                        showCreateModal = false
                    }
                ) {
                    Text("Save Draft")
                }
            }
        )
    }

    // Detail Dialog View
    if (selectedDetailIncident != null) {
        val inc = selectedDetailIncident!!
        AlertDialog(
            onDismissRequest = { selectedDetailIncident = null },
            title = { Text(inc.title) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Category: ${inc.category.label}", fontWeight = FontWeight.Bold, color = Color(0xFF2ECC71))
                    Text("Severity: ${inc.severity.name}", color = Color(0xFFE74C3C))
                    Text("Description: ${inc.description}")
                    Text("Location: ${inc.latitude}, ${inc.longitude}")
                    Text("Reporter: ${inc.reporterName} (${inc.rangeName})")
                    Text("Status: ${inc.status.name}", fontWeight = FontWeight.Bold)
                }
            },
            confirmButton = {
                Button(onClick = { selectedDetailIncident = null }) {
                    Text("Close")
                }
            }
        )
    }
}
