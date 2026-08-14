package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.models.*

@Database(
    entities = [
        User::class,
        Patrol::class,
        GpsPoint::class,
        Incident::class,
        ChatMessage::class,
        SosAlert::class,
        GeofenceZone::class,
        NotificationItem::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(ForestConverters::class)
abstract class ForestDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun patrolDao(): PatrolDao
    abstract fun gpsPointDao(): GpsPointDao
    abstract fun incidentDao(): IncidentDao
    abstract fun chatDao(): ChatDao
    abstract fun sosDao(): SosDao
    abstract fun geofenceDao(): GeofenceDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: ForestDatabase? = null

        fun getDatabase(context: Context): ForestDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ForestDatabase::class.java,
                    "forest_guardian_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
