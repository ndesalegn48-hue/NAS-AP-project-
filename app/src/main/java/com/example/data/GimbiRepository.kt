package com.example.data

import com.example.model.AdvertisementEntity
import com.example.model.CommunityNotice
import com.example.model.Inquiry
import com.example.model.ModerationStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GimbiRepository(
    private val advertisementDao: AdvertisementDao,
    private val noticeDao: NoticeDao,
    private val inquiryDao: InquiryDao
) {
    val allActiveAds: Flow<List<AdvertisementEntity>> = advertisementDao.getAllActiveAds()
    val featuredAds: Flow<List<AdvertisementEntity>> = advertisementDao.getFeaturedAds()
    val savedAds: Flow<List<AdvertisementEntity>> = advertisementDao.getSavedAds()
    val userCreatedAds: Flow<List<AdvertisementEntity>> = advertisementDao.getUserCreatedAds()
    val pendingAds: Flow<List<AdvertisementEntity>> = advertisementDao.getPendingAds()
    val allAdsAdmin: Flow<List<AdvertisementEntity>> = advertisementDao.getAllAdsAdmin()

    val allNotices: Flow<List<CommunityNotice>> = noticeDao.getAllNotices()
    val allInquiries: Flow<List<Inquiry>> = inquiryDao.getAllInquiries()

    fun getAdsByCategory(categoryId: String): Flow<List<AdvertisementEntity>> {
        return advertisementDao.getAdsByCategory(categoryId)
    }

    fun getAdById(id: Long): Flow<AdvertisementEntity?> {
        return advertisementDao.getAdById(id)
    }

    fun getInquiriesForAd(adId: Long): Flow<List<Inquiry>> {
        return inquiryDao.getInquiriesForAd(adId)
    }

    suspend fun insertAd(ad: AdvertisementEntity): Long = withContext(Dispatchers.IO) {
        advertisementDao.insertAd(ad)
    }

    suspend fun incrementViews(id: Long) = withContext(Dispatchers.IO) {
        advertisementDao.incrementViews(id)
    }

    suspend fun incrementCalls(id: Long) = withContext(Dispatchers.IO) {
        advertisementDao.incrementCalls(id)
    }

    suspend fun incrementWhatsapp(id: Long) = withContext(Dispatchers.IO) {
        advertisementDao.incrementWhatsapp(id)
    }

    suspend fun incrementDirections(id: Long) = withContext(Dispatchers.IO) {
        advertisementDao.incrementDirections(id)
    }

    suspend fun incrementProfileVisits(id: Long) = withContext(Dispatchers.IO) {
        advertisementDao.incrementProfileVisits(id)
    }

    suspend fun toggleSaved(id: Long, isSaved: Boolean) = withContext(Dispatchers.IO) {
        advertisementDao.toggleSaved(id, isSaved)
    }

    suspend fun approveAd(id: Long) = withContext(Dispatchers.IO) {
        advertisementDao.updateModerationStatus(id, ModerationStatus.ACTIVE.name)
    }

    suspend fun rejectAd(id: Long) = withContext(Dispatchers.IO) {
        advertisementDao.updateModerationStatus(id, ModerationStatus.REJECTED.name)
    }

    suspend fun toggleVerified(id: Long, isVerified: Boolean) = withContext(Dispatchers.IO) {
        advertisementDao.toggleVerified(id, isVerified)
    }

    suspend fun toggleFeatured(id: Long, isFeatured: Boolean) = withContext(Dispatchers.IO) {
        advertisementDao.toggleFeatured(id, isFeatured)
    }

    suspend fun deleteAd(id: Long) = withContext(Dispatchers.IO) {
        advertisementDao.deleteAd(id)
    }

    suspend fun insertInquiry(inquiry: Inquiry): Long = withContext(Dispatchers.IO) {
        inquiryDao.insertInquiry(inquiry)
    }

    suspend fun markInquiryAsRead(id: Long) = withContext(Dispatchers.IO) {
        inquiryDao.markAsRead(id)
    }

    suspend fun seedDatabaseIfNeeded() = withContext(Dispatchers.IO) {
        val count = advertisementDao.getCount()
        if (count == 0) {
            advertisementDao.insertAds(InitialSeedData.getInitialAds())
        }
        val noticeCount = noticeDao.getCount()
        if (noticeCount == 0) {
            noticeDao.insertNotices(InitialSeedData.getInitialNotices())
        }
    }
}
