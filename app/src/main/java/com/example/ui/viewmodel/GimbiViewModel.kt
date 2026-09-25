package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.GimbiRepository
import com.example.model.AdType
import com.example.model.AdvertisementEntity
import com.example.model.AppLanguage
import com.example.model.Category
import com.example.model.CommunityNotice
import com.example.model.Inquiry
import com.example.model.ModerationStatus
import com.example.model.PackageTier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AdFilter {
    ALL,
    VERIFIED_ONLY,
    FEATURED_ONLY,
    SPECIAL_OFFERS
}

class GimbiViewModel(private val repository: GimbiRepository) : ViewModel() {

    private val _currentLanguage = MutableStateFlow(AppLanguage.AFAAN_OROMOO)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory: StateFlow<Category?> = _selectedCategory.asStateFlow()

    private val _selectedFilter = MutableStateFlow(AdFilter.ALL)
    val selectedFilter: StateFlow<AdFilter> = _selectedFilter.asStateFlow()

    init {
        viewModelScope.launch {
            repository.seedDatabaseIfNeeded()
        }
    }

    val communityNotices: StateFlow<List<CommunityNotice>> = repository.allNotices
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val featuredAds: StateFlow<List<AdvertisementEntity>> = repository.featuredAds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userCreatedAds: StateFlow<List<AdvertisementEntity>> = repository.userCreatedAds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allInquiries: StateFlow<List<Inquiry>> = repository.allInquiries
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pendingAds: StateFlow<List<AdvertisementEntity>> = repository.pendingAds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allAdsAdmin: StateFlow<List<AdvertisementEntity>> = repository.allAdsAdmin
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredAds: StateFlow<List<AdvertisementEntity>> = combine(
        repository.allActiveAds,
        _searchQuery,
        _selectedCategory,
        _selectedFilter,
        _currentLanguage
    ) { ads, query, category, filter, lang ->
        ads.filter { ad ->
            val matchesCategory = category == null || ad.categoryId == category.id
            val matchesFilter = when (filter) {
                AdFilter.ALL -> true
                AdFilter.VERIFIED_ONLY -> ad.isVerified
                AdFilter.FEATURED_ONLY -> ad.isFeatured
                AdFilter.SPECIAL_OFFERS -> ad.adType == AdType.PROMOTION.name
            }
            val matchesQuery = if (query.isBlank()) {
                true
            } else {
                val q = query.trim().lowercase()
                ad.getTitle(lang).lowercase().contains(q) ||
                    ad.getDescription(lang).lowercase().contains(q) ||
                    ad.businessName.lowercase().contains(q) ||
                    ad.locationKebele.lowercase().contains(q) ||
                    ad.priceText.lowercase().contains(q)
            }
            matchesCategory && matchesFilter && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setLanguage(lang: AppLanguage) {
        _currentLanguage.value = lang
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: Category?) {
        _selectedCategory.value = category
    }

    fun selectFilter(filter: AdFilter) {
        _selectedFilter.value = filter
    }

    fun trackView(adId: Long) {
        viewModelScope.launch {
            repository.incrementViews(adId)
        }
    }

    fun trackCall(adId: Long) {
        viewModelScope.launch {
            repository.incrementCalls(adId)
        }
    }

    fun trackWhatsapp(adId: Long) {
        viewModelScope.launch {
            repository.incrementWhatsapp(adId)
        }
    }

    fun trackDirections(adId: Long) {
        viewModelScope.launch {
            repository.incrementDirections(adId)
        }
    }

    fun trackProfileVisit(adId: Long) {
        viewModelScope.launch {
            repository.incrementProfileVisits(adId)
        }
    }

    fun toggleSave(adId: Long, currentSaved: Boolean) {
        viewModelScope.launch {
            repository.toggleSaved(adId, !currentSaved)
        }
    }

    fun createAdvertisement(
        title: String,
        description: String,
        businessName: String,
        category: Category,
        adType: AdType,
        packageTier: PackageTier,
        priceText: String,
        phone: String,
        whatsapp: String,
        email: String,
        locationKebele: String,
        imageResName: String = "",
        onComplete: (Long) -> Unit
    ) {
        viewModelScope.launch {
            // Free and standard ads go to active or pending. Let's auto-activate or pending review:
            val status = if (packageTier == PackageTier.PREMIUM) ModerationStatus.ACTIVE else ModerationStatus.ACTIVE
            val newAd = AdvertisementEntity(
                titleEn = title,
                titleOm = title,
                titleAm = title,
                descriptionEn = description,
                descriptionOm = description,
                descriptionAm = description,
                businessName = businessName,
                categoryId = category.id,
                adType = adType.name,
                packageTier = packageTier.name,
                priceText = priceText,
                phoneNumber = phone,
                whatsappNumber = whatsapp.ifBlank { phone },
                email = email,
                locationKebele = locationKebele,
                imageResName = imageResName,
                isVerified = false,
                isFeatured = packageTier == PackageTier.PREMIUM,
                moderationStatus = status.name,
                isUserCreated = true
            )
            val insertedId = repository.insertAd(newAd)
            onComplete(insertedId)
        }
    }

    fun submitInquiry(
        adId: Long,
        businessName: String,
        adTitle: String,
        senderName: String,
        senderPhone: String,
        message: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val inquiry = Inquiry(
                adId = adId,
                businessName = businessName,
                adTitle = adTitle,
                senderName = senderName,
                senderPhone = senderPhone,
                message = message
            )
            repository.insertInquiry(inquiry)
            onSuccess()
        }
    }

    fun approveAd(id: Long) {
        viewModelScope.launch {
            repository.approveAd(id)
        }
    }

    fun rejectAd(id: Long) {
        viewModelScope.launch {
            repository.rejectAd(id)
        }
    }

    fun toggleVerified(id: Long, current: Boolean) {
        viewModelScope.launch {
            repository.toggleVerified(id, !current)
        }
    }

    fun toggleFeatured(id: Long, current: Boolean) {
        viewModelScope.launch {
            repository.toggleFeatured(id, !current)
        }
    }

    fun deleteAd(id: Long) {
        viewModelScope.launch {
            repository.deleteAd(id)
        }
    }
}

class GimbiViewModelFactory(private val repository: GimbiRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GimbiViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GimbiViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
