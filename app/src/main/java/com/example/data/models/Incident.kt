package com.example.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class IncidentCategory(val label: String) {
    FOREST_FIRE("Forest Fire"),
    ILLEGAL_LOGGING("Illegal Logging"),
    POACHING("Poaching"),
    WILDLIFE_SIGHTING("Wildlife Sighting"),
    ENCROACHMENT("Encroachment"),
    POLLUTION("Pollution"),
    FALLEN_TREES("Fallen Trees"),
    DAMAGED_ROADS("Damaged Roads"),
    ILLEGAL_MINING("Illegal Mining"),
    FLOOD("Flood"),
    LANDSLIDE("Landslide")
}

enum class IncidentSeverity {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
}

enum class IncidentStatus {
    DRAFT,
    SUBMITTED,
    UNDER_INVESTIGATION,
    APPROVED,
    RESOLVED
}

@Entity(tableName = "incidents")
data class Incident(
    @PrimaryKey val id: String,
    val category: IncidentCategory,
    val title: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long = System.currentTimeMillis(),
    val severity: IncidentSeverity,
    val status: IncidentStatus,
    val reporterId: String,
    val reporterName: String,
    val rangeName: String,
    val photoUris: String = "", // Comma-separated or JSON list
    val voiceNoteUri: String? = null,
    val isDraft: Boolean = false,
    val isSynced: Boolean = false
)
