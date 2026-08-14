package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.ui.components.ForestHeader
import com.example.ui.components.RoleBasedBottomNav
import com.example.ui.components.SosEmergencyOverlay
import com.example.ui.screens.*
import com.example.ui.theme.ForestGuardianTheme
import com.example.viewmodel.ForestGuardianViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: ForestGuardianViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ForestGuardianTheme {
                val isAuthenticated by viewModel.isAuthenticated.collectAsState()
                val currentRole by viewModel.currentRole.collectAsState()
                val currentRoute by viewModel.currentRoute.collectAsState()
                val activePatrol by viewModel.activePatrol.collectAsState()
                val activeSos by viewModel.activeSos.collectAsState()
                val checkpoints by viewModel.checkpoints.collectAsState()
                val incidents by viewModel.incidents.collectAsState()
                val chatMessages by viewModel.chatMessages.collectAsState()
                val geofences by viewModel.geofences.collectAsState()
                val users by viewModel.users.collectAsState()
                val notifications by viewModel.notifications.collectAsState()
                val networkStatus by viewModel.syncService.networkStatus.collectAsState()
                val pendingSyncCount by viewModel.syncService.pendingSyncCount.collectAsState()
                val fireRisk by viewModel.fireRiskPrediction.collectAsState()
                val imageResult by viewModel.imageClassificationResult.collectAsState()

                val unreadNotifCount = notifications.count { !it.isRead }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF0D1F15))
                ) {
                    if (!isAuthenticated) {
                        AuthScreen(
                            onLoginSuccess = { role ->
                                viewModel.login(role)
                            }
                        )
                    } else {
                        Scaffold(
                            topBar = {
                                ForestHeader(
                                    currentRole = currentRole,
                                    networkStatus = networkStatus,
                                    pendingSyncCount = pendingSyncCount,
                                    unreadNotificationCount = unreadNotifCount,
                                    onRoleChanged = { viewModel.setRole(it) },
                                    onToggleNetwork = { viewModel.syncService.toggleNetworkMode() },
                                    onTriggerSync = { viewModel.triggerAutoSync() },
                                    onNotificationClick = { viewModel.navigateTo("notifications") },
                                    onTriggerSos = { viewModel.triggerSos() },
                                    onLogout = { viewModel.logout() }
                                )
                            },
                        bottomBar = {
                            RoleBasedBottomNav(
                                currentRole = currentRole,
                                currentRoute = currentRoute,
                                onNavigate = { viewModel.navigateTo(it) }
                            )
                        },
                        containerColor = Color(0xFF0D1F15)
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            when (currentRoute) {
                                "dashboard" -> DashboardScreen(
                                    currentRole = currentRole,
                                    activePatrol = activePatrol,
                                    incidents = incidents,
                                    geofences = geofences,
                                    onNavigate = { viewModel.navigateTo(it) },
                                    onStartPatrolClick = {
                                        if (activePatrol == null) viewModel.startPatrol()
                                        viewModel.navigateTo("patrol")
                                    }
                                )
                                "patrol" -> PatrolScreen(
                                    activePatrol = activePatrol,
                                    checkpoints = checkpoints,
                                    onStartPatrol = { viewModel.startPatrol() },
                                    onPausePatrol = { viewModel.pausePatrol() },
                                    onResumePatrol = { viewModel.resumePatrol() },
                                    onEndPatrol = { viewModel.endPatrol() },
                                    onCheckInCheckpoint = { id, method -> viewModel.checkInCheckpoint(id, method) }
                                )
                                "incidents" -> IncidentsScreen(
                                    incidents = incidents,
                                    onSubmitIncident = { viewModel.submitIncident(it) },
                                    onSaveDraft = { viewModel.saveDraftIncident(it) },
                                    onApproveIncident = { viewModel.approveIncident(it) },
                                    currentRole = currentRole
                                )
                                "map" -> MapScreen(
                                    incidents = incidents,
                                    geofences = geofences
                                )
                                "chat" -> ChatScreen(
                                    messages = chatMessages,
                                    onSendMessage = { text, mediaType -> viewModel.sendChatMessage(text, mediaType) },
                                    currentRole = currentRole
                                )
                                "ai" -> AiAssistantScreen(
                                    fireRisk = fireRisk,
                                    imageResult = imageResult,
                                    onRunAiAnalysis = { viewModel.runAiRiskPrediction() },
                                    onClassifyImage = { hint -> viewModel.classifyImage(hint) }
                                )
                                "analytics" -> AnalyticsScreen()
                                "admin" -> AdminScreen(
                                    users = users,
                                    onAddUser = { viewModel.addUser(it) }
                                )
                                "reports" -> ReportsScreen()
                                "notifications" -> NotificationsScreen(
                                    notifications = notifications,
                                    onMarkAllRead = { viewModel.markAllNotificationsRead() },
                                    onMarkRead = { viewModel.markNotificationRead(it) }
                                )
                            }
                        }
                    }
                }

                    // Emergency SOS Overlay over everything
                    SosEmergencyOverlay(
                        activeSos = activeSos,
                        onTriggerSos = { viewModel.triggerSos() },
                        onCancelSos = { pin -> viewModel.cancelSos(pin) }
                    )
                }
            }
        }
    }
}
