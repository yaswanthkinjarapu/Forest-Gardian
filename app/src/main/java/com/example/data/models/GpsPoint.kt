package com.example.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gps_points")
data class GpsPoint(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val patrolId: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long = System.currentTimeMillis(),
    val speed: Float = 0f,
    val altitude: Double = 0.0,
    val bearing: Float = 0f,
    val accuracy: Float = 5f,
    val isSynced: Boolean = false
)
