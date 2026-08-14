package com.example.data.repository

import com.example.data.local.*
import com.example.data.models.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.util.UUID

class ForestRepository(
    private val database: ForestDatabase
) {
    val userDao = database.userDao()
    val patrolDao = database.patrolDao()
    val gpsPointDao = database.gpsPointDao()
    val incidentDao = database.incidentDao()
    val chatDao = database.chatDao()
    val sosDao = database.sosDao()
    val geofenceDao = database.geofenceDao()
    val notificationDao = database.notificationDao()

    val allUsers: Flow<List<User>> = userDao.getAllUsers()
    val allPatrols: Flow<List<Patrol>> = patrolDao.getAllPatrols()
    val allIncidents: Flow<List<Incident>> = incidentDao.getAllIncidents()
    val allChatMessages: Flow<List<ChatMessage>> = chatDao.getAllMessages()
    val allSosAlerts: Flow<List<SosAlert>> = sosDao.getAllSosAlerts()
    val activeSosAlerts: Flow<List<SosAlert>> = sosDao.getActiveSosAlerts()
    val allGeofences: Flow<List<GeofenceZone>> = geofenceDao.getAllGeofences()
    val allNotifications: Flow<List<NotificationItem>> = notificationDao.getAllNotifications()

    suspend fun seedInitialDataIfNeeded() {
        val existingUsers = userDao.getAllUsers().first()
        if (existingUsers.isEmpty()) {
            // Seed Users
            userDao.insertUser(
                User(
                    id = "usr_officer_1",
                    name = "Officer Rajesh Kumar",
                    email = "rajesh.kumar@forest.gov.in",
                    phone = "+91 98765 43210",
                    role = UserRole.FOREST_OFFICER,
                    badgeNumber = "FG-8402",
                    range = "Kargudi Range",
                    beat = "Teppakadu Beat",
                    biometricEnabled = true
                )
            )
            userDao.insertUser(
                User(
                    id = "usr_ranger_1",
                    name = "Ranger Anitha Sharma",
                    email = "anitha.sharma@forest.gov.in",
                    phone = "+91 98123 45678",
                    role = UserRole.RANGE_OFFICER,
                    badgeNumber = "RO-1024",
                    range = "Kargudi Range",
                    beat = "Kargudi Central"
                )
            )
            userDao.insertUser(
                User(
                    id = "usr_admin_1",
                    name = "Chief Warden Dr. Vikram Dev",
                    email = "vikram.dev@forest.gov.in",
                    phone = "+91 99000 11223",
                    role = UserRole.ADMIN,
                    badgeNumber = "CW-0001",
                    division = "State Headquarters"
                )
            )

            // Seed Incidents
            incidentDao.insertIncident(
                Incident(
                    id = "inc_101",
                    category = IncidentCategory.FOREST_FIRE,
                    title = "Dry Bamboo Brush Fire",
                    description = "Smoldering bamboo cluster detected near Sector 4 ridge. Smoke plume visible.",
                    latitude = 11.5650,
                    longitude = 76.5380,
                    severity = IncidentSeverity.HIGH,
                    status = IncidentStatus.UNDER_INVESTIGATION,
                    reporterId = "usr_officer_1",
                    reporterName = "Officer Rajesh Kumar",
                    rangeName = "Kargudi Range"
                )
            )
            incidentDao.insertIncident(
                Incident(
                    id = "inc_102",
                    category = IncidentCategory.ILLEGAL_LOGGING,
                    title = "Fresh Rosewood Timber Stacks",
                    description = "Chainsaw markings found on 3 freshly felled rosewood trunks near North Boundary.",
                    latitude = 11.5710,
                    longitude = 76.5290,
                    severity = IncidentSeverity.CRITICAL,
                    status = IncidentStatus.SUBMITTED,
                    reporterId = "usr_officer_1",
                    reporterName = "Officer Rajesh Kumar",
                    rangeName = "Kargudi Range"
                )
            )
            incidentDao.insertIncident(
                Incident(
                    id = "inc_103",
                    category = IncidentCategory.WILDLIFE_SIGHTING,
                    title = "Bengal Tiger Cub Pair",
                    description = "Healthy tigress with two 6-month-old cubs sighted at Moyar River crossing.",
                    latitude = 11.5580,
                    longitude = 76.5420,
                    severity = IncidentSeverity.LOW,
                    status = IncidentStatus.RESOLVED,
                    reporterId = "usr_officer_1",
                    reporterName = "Officer Rajesh Kumar",
                    rangeName = "Kargudi Range"
                )
            )

            // Seed Geofences
            geofenceDao.insertGeofence(
                GeofenceZone(
                    id = "geo_1",
                    name = "Teppakadu High Fire Risk Zone",
                    division = "Mudumalai Forest Division",
                    range = "Kargudi Range",
                    type = GeofenceType.RADIUS,
                    centerLat = 11.5623,
                    centerLng = 76.5342,
                    radiusMeters = 1200.0,
                    riskLevel = "HIGH"
                )
            )
            geofenceDao.insertGeofence(
                GeofenceZone(
                    id = "geo_2",
                    name = "Moyar River Elephant Corridor",
                    division = "Mudumalai Forest Division",
                    range = "Kargudi Range",
                    type = GeofenceType.POLYGON,
                    centerLat = 11.5580,
                    centerLng = 76.5420,
                    radiusMeters = 2000.0,
                    riskLevel = "PROTECTED"
                )
            )

            // Seed Notifications
            notificationDao.insertNotification(
                NotificationItem(
                    id = "notif_1",
                    title = "🔥 Fire Hazard Warning",
                    body = "Dry wind velocity reaching 24 km/h in Kargudi Range. Fire patrol recommended.",
                    category = NotificationCategory.FIRE_ALERT,
                    isCritical = true
                )
            )
            notificationDao.insertNotification(
                NotificationItem(
                    id = "notif_2",
                    title = "📍 Geofence Entry Alert",
                    body = "Officer Rajesh entered Teppakadu High Fire Risk Zone at 11:42 AM.",
                    category = NotificationCategory.GEOFENCE_VIOLATION,
                    isRead = true
                )
            )

            // Seed Chat
            chatDao.insertMessage(
                ChatMessage(
                    id = "msg_1",
                    senderId = "usr_ranger_1",
                    senderName = "Ranger Anitha Sharma",
                    senderRole = UserRole.RANGE_OFFICER,
                    receiverId = "usr_officer_1",
                    text = "Officer Rajesh, please verify the timber stash at North Boundary. Drone unit is en route.",
                    timestamp = System.currentTimeMillis() - 1800000
                )
            )
            chatDao.insertMessage(
                ChatMessage(
                    id = "msg_2",
                    senderId = "usr_officer_1",
                    senderName = "Officer Rajesh Kumar",
                    senderRole = UserRole.FOREST_OFFICER,
                    receiverId = "usr_ranger_1",
                    text = "Copy Ranger Anitha. I am currently 400m away on foot patrol. Coordinates captured.",
                    timestamp = System.currentTimeMillis() - 1200000
                )
            )
        }
    }
}
