package com.example.data.local

import androidx.room.*
import com.example.data.models.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: String): Flow<User?>

    @Query("SELECT * FROM users ORDER BY name ASC")
    fun getAllUsers(): Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUser(userId: String)
}

@Dao
interface PatrolDao {
    @Query("SELECT * FROM patrols ORDER BY startTime DESC")
    fun getAllPatrols(): Flow<List<Patrol>>

    @Query("SELECT * FROM patrols WHERE id = :patrolId")
    fun getPatrolById(patrolId: String): Flow<Patrol?>

    @Query("SELECT * FROM patrols WHERE officerId = :officerId ORDER BY startTime DESC")
    fun getPatrolsForOfficer(officerId: String): Flow<List<Patrol>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatrol(patrol: Patrol)

    @Update
    suspend fun updatePatrol(patrol: Patrol)
}

@Dao
interface GpsPointDao {
    @Query("SELECT * FROM gps_points WHERE patrolId = :patrolId ORDER BY timestamp ASC")
    fun getPointsForPatrol(patrolId: String): Flow<List<GpsPoint>>

    @Query("SELECT * FROM gps_points WHERE isSynced = 0")
    suspend fun getUnsyncedPoints(): List<GpsPoint>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPoint(point: GpsPoint)

    @Query("UPDATE gps_points SET isSynced = 1 WHERE patrolId = :patrolId")
    suspend fun markPatrolPointsSynced(patrolId: String)
}

@Dao
interface IncidentDao {
    @Query("SELECT * FROM incidents ORDER BY timestamp DESC")
    fun getAllIncidents(): Flow<List<Incident>>

    @Query("SELECT * FROM incidents WHERE reporterId = :officerId ORDER BY timestamp DESC")
    fun getIncidentsForReporter(officerId: String): Flow<List<Incident>>

    @Query("SELECT * FROM incidents WHERE isDraft = 1")
    fun getDraftIncidents(): Flow<List<Incident>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncident(incident: Incident)

    @Update
    suspend fun updateIncident(incident: Incident)

    @Query("DELETE FROM incidents WHERE id = :incidentId")
    suspend fun deleteIncident(incidentId: String)
}

@Dao
interface ChatDao {
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessage)
}

@Dao
interface SosDao {
    @Query("SELECT * FROM sos_alerts ORDER BY timestamp DESC")
    fun getAllSosAlerts(): Flow<List<SosAlert>>

    @Query("SELECT * FROM sos_alerts WHERE status = 'ACTIVE' ORDER BY timestamp DESC")
    fun getActiveSosAlerts(): Flow<List<SosAlert>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSos(sos: SosAlert)

    @Update
    suspend fun updateSos(sos: SosAlert)
}

@Dao
interface GeofenceDao {
    @Query("SELECT * FROM geofence_zones ORDER BY name ASC")
    fun getAllGeofences(): Flow<List<GeofenceZone>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGeofence(zone: GeofenceZone)

    @Query("DELETE FROM geofence_zones WHERE id = :id")
    suspend fun deleteGeofence(id: String)
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<NotificationItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(item: NotificationItem)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markRead(id: String)

    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllRead()
}
