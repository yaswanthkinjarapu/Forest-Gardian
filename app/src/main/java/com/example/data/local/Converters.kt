package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.models.*

class ForestConverters {
    @TypeConverter
    fun fromUserRole(role: UserRole): String = role.name

    @TypeConverter
    fun toUserRole(value: String): UserRole = enumValueOf(value)

    @TypeConverter
    fun fromPatrolStatus(status: PatrolStatus): String = status.name

    @TypeConverter
    fun toPatrolStatus(value: String): PatrolStatus = enumValueOf(value)

    @TypeConverter
    fun fromIncidentCategory(category: IncidentCategory): String = category.name

    @TypeConverter
    fun toIncidentCategory(value: String): IncidentCategory = enumValueOf(value)

    @TypeConverter
    fun fromIncidentSeverity(severity: IncidentSeverity): String = severity.name

    @TypeConverter
    fun toIncidentSeverity(value: String): IncidentSeverity = enumValueOf(value)

    @TypeConverter
    fun fromIncidentStatus(status: IncidentStatus): String = status.name

    @TypeConverter
    fun toIncidentStatus(value: String): IncidentStatus = enumValueOf(value)

    @TypeConverter
    fun fromDeliveryState(state: DeliveryState): String = state.name

    @TypeConverter
    fun toDeliveryState(value: String): DeliveryState = enumValueOf(value)

    @TypeConverter
    fun fromSosStatus(status: SosStatus): String = status.name

    @TypeConverter
    fun toSosStatus(value: String): SosStatus = enumValueOf(value)

    @TypeConverter
    fun fromGeofenceType(type: GeofenceType): String = type.name

    @TypeConverter
    fun toGeofenceType(value: String): GeofenceType = enumValueOf(value)

    @TypeConverter
    fun fromNotificationCategory(category: NotificationCategory): String = category.name

    @TypeConverter
    fun toNotificationCategory(value: String): NotificationCategory = enumValueOf(value)
}
