package com.example.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class UserRole {
    FOREST_OFFICER,
    RANGE_OFFICER,
    ADMIN
}

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val role: UserRole,
    val circle: String = "Southern Circle",
    val division: String = "Mudumalai Forest Division",
    val range: String = "Kargudi Range",
    val beat: String = "Teppakadu Beat",
    val zone: String = "Zone Alpha",
    val badgeNumber: String,
    val isOnline: Boolean = true,
    val biometricEnabled: Boolean = false,
    val lastActive: Long = System.currentTimeMillis()
)
