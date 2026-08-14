package com.example.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class SosStatus {
    ACTIVE,
    RESOLVED,
    CANCELLED
}

@Entity(tableName = "sos_alerts")
data class SosAlert(
    @PrimaryKey val id: String,
    val officerId: String,
    val officerName: String,
    val officerRole: UserRole,
    val latitude: Double,
    val longitude: Double,
    val batteryLevel: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val status: SosStatus = SosStatus.ACTIVE,
    val resolvedBy: String? = null,
    val isSynced: Boolean = false
)
