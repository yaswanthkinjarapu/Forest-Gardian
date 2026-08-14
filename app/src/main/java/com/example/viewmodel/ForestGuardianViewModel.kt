package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.ForestDatabase
import com.example.data.models.*
import com.example.data.repository.ForestRepository
import com.example.services.GeminiAiService
import com.example.services.LocationTrackingManager
import com.example.services.NetworkStatus
import com.example.services.SyncService
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ForestGuardianViewModel(application: Application) : AndroidViewModel(application) {

    private val database = ForestDatabase.getDatabase(application)
    private val repository = ForestRepository(database)
    val aiService = GeminiAiService()
    val syncService = SyncService()
    val locationTrackingManager = LocationTrackingManager(application)

    // State
    private val _currentRole = MutableStateFlow(UserRole.FOREST_OFFICER)
    val currentRole: StateFlow<UserRole> = _currentRole

    private val _currentRoute = MutableStateFlow("dashboard")
    val currentRoute: StateFlow<String> = _currentRoute

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    fun login(role: UserRole) {
        _currentRole.value = role
        _isAuthenticated.value = true
    }

    fun logout() {
        _isAuthenticated.value = false
    }

    private val _activePatrol = MutableStateFlow<Patrol?>(null)
    val activePatrol: StateFlow<Patrol?> = _activePatrol

    private val _activeSos = MutableStateFlow<SosAlert?>(null)
    val activeSos: StateFlow<SosAlert?> = _activeSos

    private val _fireRiskPrediction = MutableStateFlow<FireRiskPrediction?>(null)
    val fireRiskPrediction: StateFlow<FireRiskPrediction?> = _fireRiskPrediction

    private val _imageClassificationResult = MutableStateFlow<ImageClassificationResult?>(null)
    val imageClassificationResult: StateFlow<ImageClassificationResult?> = _imageClassificationResult

    // Initial Assigned Checkpoints
    private val _checkpoints = MutableStateFlow(
        listOf(
            Checkpoint("cp_1", "Teppakadu Checkpost Alpha", 11.5623, 76.5342),
            Checkpoint("cp_2", "Moyar River Crossing", 11.5580, 76.5420),
            Checkpoint("cp_3", "North Boundary Ridge", 11.5710, 76.5290),
            Checkpoint("cp_4", "Kargudi Watchtower", 11.5650, 76.5380)
        )
    )
    val checkpoints: StateFlow<List<Checkpoint>> = _checkpoints

    // Flows from Repository
    val users: StateFlow<List<User>> = repository.allUsers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val incidents: StateFlow<List<Incident>> = repository.allIncidents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val chatMessages: StateFlow<List<ChatMessage>> = repository.allChatMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val geofences: StateFlow<List<GeofenceZone>> = repository.allGeofences
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notifications: StateFlow<List<NotificationItem>> = repository.allNotifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfNeeded()
            runAiRiskPrediction()
        }
    }

    fun setRole(role: UserRole) {
        _currentRole.value = role
    }

    fun navigateTo(route: String) {
        _currentRoute.value = route
    }

    fun startPatrol() {
        val newPatrol = Patrol(
            id = "patrol_${System.currentTimeMillis()}",
            officerId = "usr_officer_1",
            officerName = "Officer Rajesh Kumar",
            rangeName = "Kargudi Range",
            status = PatrolStatus.ACTIVE,
            startTime = System.currentTimeMillis()
        )
        _activePatrol.value = newPatrol
        locationTrackingManager.startTracking(newPatrol.id)
    }

    fun pausePatrol() {
        _activePatrol.value = _activePatrol.value?.copy(status = PatrolStatus.PAUSED)
    }

    fun resumePatrol() {
        _activePatrol.value = _activePatrol.value?.copy(status = PatrolStatus.ACTIVE)
    }

    fun endPatrol() {
        val current = _activePatrol.value ?: return
        val ended = current.copy(
            status = PatrolStatus.COMPLETED,
            endTime = System.currentTimeMillis()
        )
        _activePatrol.value = null
        locationTrackingManager.stopTracking()
        viewModelScope.launch {
            repository.patrolDao.insertPatrol(ended)
        }
    }

    fun checkInCheckpoint(checkpointId: String, method: String) {
        val updated = _checkpoints.value.map { cp ->
            if (cp.id == checkpointId) {
                cp.copy(isChecked = true, checkedTime = System.currentTimeMillis(), checkMethod = method)
            } else cp
        }
        _checkpoints.value = updated
        _activePatrol.value = _activePatrol.value?.copy(
            checkedCheckpoints = updated.count { it.isChecked }
        )
    }

    fun triggerSos() {
        val sos = SosAlert(
            id = "sos_${System.currentTimeMillis()}",
            officerId = "usr_officer_1",
            officerName = "Officer Rajesh Kumar",
            officerRole = _currentRole.value,
            latitude = 11.5623,
            longitude = 76.5342,
            batteryLevel = 84
        )
        _activeSos.value = sos
        viewModelScope.launch {
            repository.sosDao.insertSos(sos)
        }
    }

    fun cancelSos(pin: String): Boolean {
        if (pin == "1234" || pin.isEmpty()) {
            _activeSos.value = null
            return true
        }
        return false
    }

    fun submitIncident(incident: Incident) {
        viewModelScope.launch {
            repository.incidentDao.insertIncident(incident)
            syncService.incrementPending()
        }
    }

    fun saveDraftIncident(incident: Incident) {
        viewModelScope.launch {
            repository.incidentDao.insertIncident(incident)
        }
    }

    fun approveIncident(incidentId: String) {
        viewModelScope.launch {
            val list = repository.allIncidents.first()
            val existing = list.find { it.id == incidentId }
            if (existing != null) {
                repository.incidentDao.updateIncident(
                    existing.copy(status = IncidentStatus.APPROVED)
                )
            }
        }
    }

    fun sendChatMessage(text: String, mediaType: String? = null) {
        val msg = ChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            senderId = "usr_officer_1",
            senderName = if (_currentRole.value == UserRole.FOREST_OFFICER) "Officer Rajesh Kumar" else "Ranger Anitha Sharma",
            senderRole = _currentRole.value,
            receiverId = "GROUP_RANGE",
            text = text,
            mediaType = mediaType,
            locationLat = if (mediaType == "GPS") 11.5650 else null,
            locationLng = if (mediaType == "GPS") 76.5380 else null
        )
        viewModelScope.launch {
            repository.chatDao.insertMessage(msg)
        }
    }

    fun addUser(user: User) {
        viewModelScope.launch {
            repository.userDao.insertUser(user)
        }
    }

    fun runAiRiskPrediction() {
        viewModelScope.launch {
            val prediction = aiService.predictFireRisk("Teppakadu Beat", 36.4, 28)
            _fireRiskPrediction.value = prediction
        }
    }

    fun classifyImage(hint: String) {
        viewModelScope.launch {
            val res = aiService.analyzeImageClassification("surveillance_photo_04.jpg", hint)
            _imageClassificationResult.value = res
        }
    }

    fun triggerAutoSync() {
        viewModelScope.launch {
            syncService.performAutoSync {
                // Sync completed
            }
        }
    }

    fun markNotificationRead(id: String) {
        viewModelScope.launch {
            repository.notificationDao.markRead(id)
        }
    }

    fun markAllNotificationsRead() {
        viewModelScope.launch {
            repository.notificationDao.markAllRead()
        }
    }
}
