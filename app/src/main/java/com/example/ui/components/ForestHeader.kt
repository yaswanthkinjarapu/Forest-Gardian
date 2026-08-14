package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.data.models.UserRole
import com.example.services.NetworkStatus

@Composable
fun ForestHeader(
    currentRole: UserRole,
    networkStatus: NetworkStatus,
    pendingSyncCount: Int,
    unreadNotificationCount: Int,
    onRoleChanged: (UserRole) -> Unit,
    onToggleNetwork: () -> Unit,
    onTriggerSync: () -> Unit,
    onNotificationClick: () -> Unit,
    onTriggerSos: () -> Unit,
    onLogout: () -> Unit = {}
) {
    var showRoleMenu by remember { mutableStateOf(false) }

    Surface(
        color = Color(0xFF0F2D1E),
        tonalElevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Title and Role Dropdown Selector
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF2ECC71),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Security, contentDescription = "Forest Guardian", tint = Color(0xFF0D1F15))
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "FOREST GUARDIAN",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { showRoleMenu = true }
                        ) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = when (currentRole) {
                                    UserRole.FOREST_OFFICER -> Color(0xFF1B5E20)
                                    UserRole.RANGE_OFFICER -> Color(0xFF0277BD)
                                    UserRole.ADMIN -> Color(0xFF6A1B9A)
                                }
                            ) {
                                Text(
                                    text = when (currentRole) {
                                        UserRole.FOREST_OFFICER -> "FOREST OFFICER"
                                        UserRole.RANGE_OFFICER -> "RANGE OFFICER"
                                        UserRole.ADMIN -> "SYSTEM ADMIN"
                                    },
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Switch Role", tint = Color.LightGray)
                        }

                        DropdownMenu(
                            expanded = showRoleMenu,
                            onDismissRequest = { showRoleMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Forest Officer View") },
                                onClick = {
                                    onRoleChanged(UserRole.FOREST_OFFICER)
                                    showRoleMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Range Officer View") },
                                onClick = {
                                    onRoleChanged(UserRole.RANGE_OFFICER)
                                    showRoleMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("System Admin View") },
                                onClick = {
                                    onRoleChanged(UserRole.ADMIN)
                                    showRoleMenu = false
                                }
                            )
                            HorizontalDivider()
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Color(0xFFE74C3C), modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Sign Out Officer", color = Color(0xFFE74C3C), fontWeight = FontWeight.Bold)
                                    }
                                },
                                onClick = {
                                    showRoleMenu = false
                                    onLogout()
                                }
                            )
                        }
                    }
                }

                // Actions: Network Status, Notifications, SOS Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Network Sync Status Pill
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = when (networkStatus) {
                            NetworkStatus.ONLINE -> Color(0x332ECC71)
                            NetworkStatus.OFFLINE -> Color(0x33E74C3C)
                            NetworkStatus.SYNCING -> Color(0x33FFD54F)
                        },
                        modifier = Modifier.clickable {
                            if (networkStatus == NetworkStatus.OFFLINE) onToggleNetwork() else onTriggerSync()
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(
                                        when (networkStatus) {
                                            NetworkStatus.ONLINE -> Color(0xFF2ECC71)
                                            NetworkStatus.OFFLINE -> Color(0xFFE74C3C)
                                            NetworkStatus.SYNCING -> Color(0xFFFFD54F)
                                        },
                                        CircleShape
                                    )
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = when (networkStatus) {
                                    NetworkStatus.ONLINE -> if (pendingSyncCount > 0) "Sync ($pendingSyncCount)" else "Online"
                                    NetworkStatus.OFFLINE -> "Offline ($pendingSyncCount)"
                                    NetworkStatus.SYNCING -> "Syncing..."
                                },
                                fontSize = 11.sp,
                                color = Color.White
                            )
                        }
                    }

                    // Notification Bell Badge
                    IconButton(onClick = onNotificationClick) {
                        BadgedBox(
                            badge = {
                                if (unreadNotificationCount > 0) {
                                    Badge(containerColor = Color(0xFFE74C3C)) {
                                        Text("$unreadNotificationCount")
                                    }
                                }
                            }
                        ) {
                            Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = Color.White)
                        }
                    }

                    // SOS Floating Trigger Button
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFFF1744),
                        modifier = Modifier
                            .size(38.dp)
                            .clickable { onTriggerSos() }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("SOS", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
