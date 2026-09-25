package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.model.CommunityNotice
import kotlinx.coroutines.flow.Flow

@Dao
interface NoticeDao {
    @Query("SELECT * FROM community_notices ORDER BY isUrgent DESC, id DESC")
    fun getAllNotices(): Flow<List<CommunityNotice>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotices(notices: List<CommunityNotice>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotice(notice: CommunityNotice): Long

    @Query("SELECT COUNT(*) FROM community_notices")
    suspend fun getCount(): Int
}
