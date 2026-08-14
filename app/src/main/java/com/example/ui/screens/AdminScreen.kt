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
import com.example.data.models.User
import com.example.data.models.UserRole

@Composable
fun AdminScreen(
    users: List<User>,
    onAddUser: (User) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddUserModal by remember { mutableStateOf(false) }
    var nameText by remember { mutableStateOf("") }
    var emailText by remember { mutableStateOf("") }
    var badgeText by remember { mutableStateOf("") }
    var roleSelected by remember { mutableStateOf(UserRole.FOREST_OFFICER) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0D1F15))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // System Admin Header
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
                            Text("Forest System Administration", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Southern Circle Headquarters • Mudumalai Division", fontSize = 12.sp, color = Color(0xFFA5D6A7))
                        }
                        Button(
                            onClick = { showAddUserModal = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2ECC71)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.PersonAdd, contentDescription = null, tint = Color(0xFF0D1F15))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("ADD USER", color = Color(0xFF0D1F15), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Division Hierarchy Overview Card
        item {
            Text("Forest Organisational Hierarchy", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(6.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF142B1E))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("🌳 Circle: Southern Forest Circle", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                    Text("  └── 📁 Division: Mudumalai Forest Division", fontSize = 12.sp, color = Color(0xFFA5D6A7))
                    Text("        ├── 🌲 Range: Kargudi Range (12 Officers)", fontSize = 12.sp, color = Color(0xFFA5D6A7))
                    Text("        └── 🌲 Range: Masinagudi Range (10 Officers)", fontSize = 12.sp, color = Color(0xFFA5D6A7))
                }
            }
        }

        // Personnel List
        item {
            Text("Registered Personnel Accounts (${users.size})", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        items(users) { usr ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF142B1E))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Surface(
                            shape = CircleShape,
                            color = when (usr.role) {
                                UserRole.FOREST_OFFICER -> Color(0xFF1B5E20)
                                UserRole.RANGE_OFFICER -> Color(0xFF0277BD)
                                UserRole.ADMIN -> Color(0xFF6A1B9A)
                            },
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = Color.White)
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(usr.name, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                            Text("${usr.badgeNumber} • ${usr.email}", fontSize = 11.sp, color = Color(0xFFA5D6A7))
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFF1B3D2A)
                    ) {
                        Text(
                            text = usr.role.name,
                            fontSize = 10.sp,
                            color = Color(0xFF2ECC71),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }

    // Add User Modal
    if (showAddUserModal) {
        AlertDialog(
            onDismissRequest = { showAddUserModal = false },
            title = { Text("Register Forest Personnel") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = nameText,
                        onValueChange = { nameText = it },
                        label = { Text("Full Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = emailText,
                        onValueChange = { emailText = it },
                        label = { Text("Government Email") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = badgeText,
                        onValueChange = { badgeText = it },
                        label = { Text("Badge / Service Number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text("Select RBAC Role:", fontSize = 12.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        UserRole.values().forEach { r ->
                            FilterChip(
                                selected = roleSelected == r,
                                onClick = { roleSelected = r },
                                label = { Text(r.name.take(6), fontSize = 10.sp) }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    val newUser = User(
                        id = "usr_${System.currentTimeMillis()}",
                        name = nameText.ifEmpty { "New Forest Officer" },
                        email = emailText.ifEmpty { "officer@forest.gov.in" },
                        phone = "+91 98000 00000",
                        role = roleSelected,
                        badgeNumber = badgeText.ifEmpty { "FG-9900" }
                    )
                    onAddUser(newUser)
                    showAddUserModal = false
                    nameText = ""
                    emailText = ""
                    badgeText = ""
                }) {
                    Text("Register Account")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddUserModal = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
