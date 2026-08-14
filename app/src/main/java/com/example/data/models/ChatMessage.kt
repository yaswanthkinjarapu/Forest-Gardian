package com.example.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class DeliveryState {
    SENDING,
    SENT,
    DELIVERED,
    READ
}

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey val id: String,
    val senderId: String,
    val senderName: String,
    val senderRole: UserRole,
    val receiverId: String, // Or "GROUP_RANGE" / "GROUP_ADMIN"
    val text: String,
    val mediaUri: String? = null,
    val mediaType: String? = null, // "IMAGE", "VOICE", "GPS"
    val locationLat: Double? = null,
    val locationLng: Double? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val deliveryState: DeliveryState = DeliveryState.SENT,
    val isSynced: Boolean = true
)
