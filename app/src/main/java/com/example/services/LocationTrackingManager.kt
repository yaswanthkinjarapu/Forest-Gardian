package com.example.services

import android.content.Context
import android.location.Location
import com.example.data.models.GpsPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.*

class LocationTrackingManager(private val context: Context) {

    private val _currentLocation = MutableStateFlow<GpsPoint?>(null)
    val currentLocation: StateFlow<GpsPoint?> = _currentLocation

    private val _isTracking = MutableStateFlow(false)
    val isTracking: StateFlow<Boolean> = _isTracking

    // Initial default coordinate (Mudumalai Tiger Reserve / Forest Reserve)
    private var lastLat = 11.5623
    private var lastLng = 76.5342
    private var totalDistanceMeters = 0.0

    fun startTracking(patrolId: String) {
        _isTracking.value = true
    }

    fun stopTracking() {
        _isTracking.value = false
    }

    fun generateNextPoint(patrolId: String, isStationary: Boolean = false): GpsPoint {
        if (!isStationary) {
            // Small step approx 10-30 meters
            val latOffset = (Math.random() - 0.48) * 0.0003
            val lngOffset = (Math.random() - 0.48) * 0.0003
            val newLat = lastLat + latOffset
            val newLng = lastLng + lngOffset

            val stepDist = calculateDistanceMeters(lastLat, lastLng, newLat, newLng)
            totalDistanceMeters += stepDist

            lastLat = newLat
            lastLng = newLng
        }

        val point = GpsPoint(
            patrolId = patrolId,
            latitude = lastLat,
            longitude = lastLng,
            timestamp = System.currentTimeMillis(),
            speed = if (isStationary) 0f else (2.5f + (Math.random() * 2.0).toFloat()),
            altitude = 920.0 + (Math.random() * 15.0),
            bearing = (Math.random() * 360).toFloat(),
            accuracy = 4.5f,
            isSynced = true
        )
        _currentLocation.value = point
        return point
    }

    companion object {
        fun calculateDistanceMeters(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
            val r = 6371000.0 // Earth radius in meters
            val dLat = Math.toRadians(lat2 - lat1)
            val dLon = Math.toRadians(lon2 - lon1)
            val a = sin(dLat / 2) * sin(dLat / 2) +
                    cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                    sin(dLon / 2) * sin(dLon / 2)
            val c = 2 * atan2(sqrt(a), sqrt(1 - a))
            return r * c
        }
    }
}
