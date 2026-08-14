package com.example.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class GeofenceType {
    RADIUS,
    POLYGON
}

@Entity(tableName = "geofence_zones")
data class GeofenceZone(
    @PrimaryKey val id: String,
    val name: String,
    val division: String,
    val range: String,
    val type: GeofenceType,
    val centerLat: Double,
    val centerLng: Double,
    val radiusMeters: Double = 1000.0,
    val polygonJson: String = "", // JSON list of LatLng pairs
    val alertOnEntry: Boolean = true,
    val alertOnExit: Boolean = true,
    val riskLevel: String = "HIGH" // HIGH, MEDIUM, SAFE
)
