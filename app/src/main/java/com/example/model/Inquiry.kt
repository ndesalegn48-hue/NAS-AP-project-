package com.example.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "inquiries")
data class Inquiry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val adId: Long,
    val businessName: String,
    val adTitle: String,
    val senderName: String,
    val senderPhone: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)
