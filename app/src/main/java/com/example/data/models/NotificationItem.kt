package com.example.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class NotificationCategory {
    FIRE_ALERT,
    SOS_EMERGENCY,
    PATROL_ASSIGNMENT,
    GEOFENCE_VIOLATION,
    WEATHER_ALERT,
    SYSTEM_BROADCAST
}

@Entity(tableName = "notifications")
data class NotificationItem(
    @PrimaryKey val id: String,
    val title: String,
    val body: String,
    val category: NotificationCategory,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val isCritical: Boolean = false,
    val actionPayload: String? = null
)
