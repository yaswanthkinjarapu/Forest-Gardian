package com.example.data.models

data class FireRiskPrediction(
    val zoneName: String,
    val riskScore: Int, // 0 - 100
    val temperature: Double,
    val humidityPercent: Int,
    val windSpeedKmh: Int,
    val vegetationDryness: String,
    val summary: String,
    val recommendation: String
)

data class LoggingHotspotAlert(
    val beatName: String,
    val probabilityPercent: Int,
    val historicalIncidentsCount: Int,
    val lastDetectedTime: String,
    val recommendedAction: String
)

data class PatrolGapAnalysis(
    val zoneName: String,
    val lastPatrolledTime: String,
    val gapHours: Int,
    val recommendedOfficer: String
)

data class ImageClassificationResult(
    val category: String, // "Active Forest Fire", "Smoke Plume", "Illegal Timber Stacks", "Sambar Deer", "Bengal Tiger", "False Alarm"
    val confidencePercent: Int,
    val speciesOrAnomaly: String,
    val recommendedSeverity: IncidentSeverity,
    val summary: String
)
