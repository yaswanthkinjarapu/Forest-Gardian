package com.example.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class PatrolStatus {
    NOT_STARTED,
    ACTIVE,
    PAUSED,
    COMPLETED
}

data class Checkpoint(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val isChecked: Boolean = false,
    val checkedTime: Long? = null,
    val checkMethod: String = "GPS" // QR, NFC, GPS
)

@Entity(tableName = "patrols")
data class Patrol(
    @PrimaryKey val id: String,
    val officerId: String,
    val officerName: String,
    val rangeName: String,
    val status: PatrolStatus,
    val startTime: Long,
    val endTime: Long? = null,
    val distanceMeters: Double = 0.0,
    val durationSeconds: Long = 0,
    val averageSpeedKmh: Double = 0.0,
    val totalCheckpoints: Int = 4,
    val checkedCheckpoints: Int = 0,
    val isSynced: Boolean = false
)
