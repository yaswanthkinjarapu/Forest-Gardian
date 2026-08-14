package com.example.services

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.*

enum class NetworkStatus {
    ONLINE,
    OFFLINE,
    SYNCING
}

class SyncService {

    private val _networkStatus = MutableStateFlow(NetworkStatus.ONLINE)
    val networkStatus: StateFlow<NetworkStatus> = _networkStatus

    private val _pendingSyncCount = MutableStateFlow(0)
    val pendingSyncCount: StateFlow<Int> = _pendingSyncCount

    private val _lastSyncedTime = MutableStateFlow(System.currentTimeMillis())
    val lastSyncedTime: StateFlow<Long> = _lastSyncedTime

    fun toggleNetworkMode() {
        _networkStatus.value = if (_networkStatus.value == NetworkStatus.ONLINE) {
            NetworkStatus.OFFLINE
        } else {
            NetworkStatus.ONLINE
        }
    }

    fun incrementPending() {
        _pendingSyncCount.value += 1
    }

    suspend fun performAutoSync(onSyncCompleted: () -> Unit) {
        if (_networkStatus.value == NetworkStatus.OFFLINE) return

        _networkStatus.value = NetworkStatus.SYNCING
        // Simulate auto-sync with server timestamp resolution
        kotlinx.coroutines.delay(1200)
        _pendingSyncCount.value = 0
        _lastSyncedTime.value = System.currentTimeMillis()
        _networkStatus.value = NetworkStatus.ONLINE
        onSyncCompleted()
    }

    fun getFormattedLastSynced(): String {
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return sdf.format(Date(_lastSyncedTime.value))
    }
}
