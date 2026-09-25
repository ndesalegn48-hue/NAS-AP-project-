package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.model.Inquiry
import kotlinx.coroutines.flow.Flow

@Dao
interface InquiryDao {
    @Query("SELECT * FROM inquiries ORDER BY timestamp DESC")
    fun getAllInquiries(): Flow<List<Inquiry>>

    @Query("SELECT * FROM inquiries WHERE adId = :adId ORDER BY timestamp DESC")
    fun getInquiriesForAd(adId: Long): Flow<List<Inquiry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInquiry(inquiry: Inquiry): Long

    @Query("UPDATE inquiries SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: Long)

    @Query("SELECT COUNT(*) FROM inquiries")
    suspend fun getCount(): Int
}
