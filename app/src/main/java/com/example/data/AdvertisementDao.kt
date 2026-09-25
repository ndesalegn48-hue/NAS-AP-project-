package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.model.AdvertisementEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AdvertisementDao {
    @Query("SELECT * FROM advertisements WHERE moderationStatus = 'ACTIVE' ORDER BY isFeatured DESC, createdAt DESC")
    fun getAllActiveAds(): Flow<List<AdvertisementEntity>>

    @Query("SELECT * FROM advertisements WHERE moderationStatus = 'ACTIVE' AND categoryId = :categoryId ORDER BY isFeatured DESC, createdAt DESC")
    fun getAdsByCategory(categoryId: String): Flow<List<AdvertisementEntity>>

    @Query("SELECT * FROM advertisements WHERE moderationStatus = 'ACTIVE' AND isFeatured = 1 ORDER BY createdAt DESC")
    fun getFeaturedAds(): Flow<List<AdvertisementEntity>>

    @Query("SELECT * FROM advertisements WHERE id = :id LIMIT 1")
    fun getAdById(id: Long): Flow<AdvertisementEntity?>

    @Query("SELECT * FROM advertisements WHERE isSaved = 1 ORDER BY createdAt DESC")
    fun getSavedAds(): Flow<List<AdvertisementEntity>>

    @Query("SELECT * FROM advertisements WHERE isUserCreated = 1 ORDER BY createdAt DESC")
    fun getUserCreatedAds(): Flow<List<AdvertisementEntity>>

    @Query("SELECT * FROM advertisements ORDER BY createdAt DESC")
    fun getAllAdsAdmin(): Flow<List<AdvertisementEntity>>

    @Query("SELECT * FROM advertisements WHERE moderationStatus = 'PENDING' ORDER BY createdAt DESC")
    fun getPendingAds(): Flow<List<AdvertisementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAd(ad: AdvertisementEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAds(ads: List<AdvertisementEntity>)

    @Update
    suspend fun updateAd(ad: AdvertisementEntity)

    @Query("UPDATE advertisements SET viewsCount = viewsCount + 1 WHERE id = :id")
    suspend fun incrementViews(id: Long)

    @Query("UPDATE advertisements SET callsCount = callsCount + 1 WHERE id = :id")
    suspend fun incrementCalls(id: Long)

    @Query("UPDATE advertisements SET whatsappCount = whatsappCount + 1 WHERE id = :id")
    suspend fun incrementWhatsapp(id: Long)

    @Query("UPDATE advertisements SET directionsCount = directionsCount + 1 WHERE id = :id")
    suspend fun incrementDirections(id: Long)

    @Query("UPDATE advertisements SET profileVisitsCount = profileVisitsCount + 1 WHERE id = :id")
    suspend fun incrementProfileVisits(id: Long)

    @Query("UPDATE advertisements SET isSaved = :isSaved WHERE id = :id")
    suspend fun toggleSaved(id: Long, isSaved: Boolean)

    @Query("UPDATE advertisements SET moderationStatus = :status WHERE id = :id")
    suspend fun updateModerationStatus(id: Long, status: String)

    @Query("UPDATE advertisements SET isVerified = :isVerified WHERE id = :id")
    suspend fun toggleVerified(id: Long, isVerified: Boolean)

    @Query("UPDATE advertisements SET isFeatured = :isFeatured WHERE id = :id")
    suspend fun toggleFeatured(id: Long, isFeatured: Boolean)

    @Query("DELETE FROM advertisements WHERE id = :id")
    suspend fun deleteAd(id: Long)

    @Query("SELECT COUNT(*) FROM advertisements")
    suspend fun getCount(): Int
}
