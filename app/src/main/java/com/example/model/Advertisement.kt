package com.example.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Category(
    val id: String,
    val iconName: String,
    val enName: String,
    val omName: String,
    val amName: String
) {
    AGRICULTURE("agri", "Agriculture", "Agriculture & Coffee", "Qonnaa fi Buna", "ግብርና እና ቡና"),
    BUSINESS("business", "Store", "Business & Retail", "Daldala fi Suuqii", "ንግድ እና ችርቻሮ"),
    SCHOOLS("schools", "School", "Schools & Colleges", "Manneen Barnootaa", "ትምህርት ቤቶች"),
    HEALTH("health", "LocalHospital", "Health & Clinics", "Fayyaa fi Kilinikoota", "ጤና እና ክሊኒኮች"),
    RESTAURANTS("restaurants", "Restaurant", "Restaurants & Cafes", "Mana Nyaataa fi Hoteela", "ምግብ ቤቶች እና ካፌዎች"),
    POULTRY_DAIRY("poultry", "Egg", "Poultry & Dairy", "Lukkuu fi Aannan", "ዶሮ እና የወተት ተዋጽኦ"),
    FARM_INPUTS("farm_inputs", "Grass", "Farm Inputs & Seeds", "Xaa'oo fi Sanyii", "የግብርና ግብዓቶች"),
    PRODUCTS_SHOPS("products", "ShoppingBag", "Products & Goods", "Oomishaalee fi Meeshaalee", "ምርቶች እና እቃዎች"),
    SERVICES("services", "Build", "Professional Services", "Tajaajila Ogeessotaa", "የሙያ አገልግሎቶች"),
    CHARITY_NGO("charity", "VolunteerActivism", "Charity & NGOs", "Dhaabbilee Tola Ooltummaa", "የበጎ አድራጎት ማህበራት"),
    JOBS("jobs", "Work", "Jobs & Vacancies", "Hojii fi Qacarrii", "የስራ እድሎች"),
    PROPERTY("property", "HomeWork", "Property & Rent", "Mana fi Lafa", "ቤቶች እና ኪራይ"),
    ELECTRONICS("electronics", "Devices", "Electronics & Solar", "Elektirooniksii fi Soolaarii", "ኤሌክትሮኒክስ እና ሶላር"),
    BEAUTY("beauty", "Face", "Beauty & Barber", "Miidhagina fi Rifeensa", "ውበት እና ፀጉር"),
    TUTORING("tutoring", "MenuBook", "Tutoring & Training", "Qayyabannaa fi Leenjii", "አጋዥ ትምህርት እና ስልጠና"),
    EVENTS("events", "Celebration", "Community Events", "Qophiilee fi Sagantaalee", "የማህበረሰብ ሁነቶች");

    fun getLocalizedName(lang: AppLanguage): String = when (lang) {
        AppLanguage.AFAAN_OROMOO -> omName
        AppLanguage.AMHARIC -> amName
        AppLanguage.ENGLISH -> enName
    }
}

enum class AdType(
    val enLabel: String,
    val omLabel: String,
    val amLabel: String
) {
    PRODUCT("Product", "Oomisha", "ምርት"),
    PROMOTION("Special Promotion", "Beeksisa Addaa", "ልዩ ቅናሽ"),
    INSTITUTION_ANNOUNCEMENT("Institution Notice", "Beeksisa Dhaabbataa", "የተቋም ማስታወቂያ"),
    JOB("Job Vacancy", "Hojii Banaa", "የስራ ማስታወቂያ"),
    EVENT("Event", "Sagantaa", "ሁነት"),
    PROPERTY("Real Estate / Land", "Lafa / Mana", "መሬት / ቤት"),
    AGRICULTURE("Agricultural Produce", "Oomisha Qonnaa", "የግብርና ምርት");

    fun getLocalized(lang: AppLanguage): String = when (lang) {
        AppLanguage.AFAAN_OROMOO -> omLabel
        AppLanguage.AMHARIC -> amLabel
        AppLanguage.ENGLISH -> enLabel
    }
}

enum class PackageTier(
    val tierName: String,
    val priceEtb: Int,
    val validityDays: Int,
    val isFeatured: Boolean,
    val priorityListing: Boolean
) {
    FREE("Free Listing", 0, 14, false, false),
    STANDARD("Standard Package", 100, 30, false, true),
    PREMIUM("Premium Package", 300, 60, true, true);

    fun getLocalizedName(lang: AppLanguage): String = when (this) {
        FREE -> when (lang) {
            AppLanguage.AFAAN_OROMOO -> "Kaffaltii Malee (0 ETB)"
            AppLanguage.AMHARIC -> "ነፃ ምዝገባ (0 ብር)"
            AppLanguage.ENGLISH -> "Free Listing (0 ETB)"
        }
        STANDARD -> when (lang) {
            AppLanguage.AFAAN_OROMOO -> "Sadarkaa Giddu-galeessa (100 ETB/ji'a)"
            AppLanguage.AMHARIC -> "መደበኛ (100 ብር/ወር)"
            AppLanguage.ENGLISH -> "Standard (100 ETB/mo)"
        }
        PREMIUM -> when (lang) {
            AppLanguage.AFAAN_OROMOO -> "Sadarkaa Ol'aanaa (300 ETB/ji'a)"
            AppLanguage.AMHARIC -> "ፕሪሚየም (300 ብር/ወር)"
            AppLanguage.ENGLISH -> "Premium (300 ETB/mo)"
        }
    }
}

enum class ModerationStatus {
    ACTIVE,
    PENDING,
    REJECTED
}

@Entity(tableName = "advertisements")
data class AdvertisementEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val titleEn: String,
    val titleOm: String,
    val titleAm: String,
    val descriptionEn: String,
    val descriptionOm: String,
    val descriptionAm: String,
    val businessName: String,
    val categoryId: String,
    val adType: String = AdType.PRODUCT.name,
    val packageTier: String = PackageTier.FREE.name,
    val priceText: String = "",
    val phoneNumber: String,
    val whatsappNumber: String = "",
    val email: String = "",
    val locationKebele: String, // e.g. "Kebele 01, Main Road", "Merkato area", "Bus Station"
    val imageResName: String = "", // e.g. "img_coffee_agri", "img_gimbi_hero"
    val isVerified: Boolean = false,
    val isFeatured: Boolean = false,
    val moderationStatus: String = ModerationStatus.ACTIVE.name,
    val viewsCount: Int = 0,
    val callsCount: Int = 0,
    val whatsappCount: Int = 0,
    val directionsCount: Int = 0,
    val profileVisitsCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val isSaved: Boolean = false,
    val isUserCreated: Boolean = false
) {
    fun getTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AFAAN_OROMOO -> titleOm.ifBlank { titleEn }
        AppLanguage.AMHARIC -> titleAm.ifBlank { titleEn }
        AppLanguage.ENGLISH -> titleEn.ifBlank { titleOm }
    }

    fun getDescription(lang: AppLanguage): String = when (lang) {
        AppLanguage.AFAAN_OROMOO -> descriptionOm.ifBlank { descriptionEn }
        AppLanguage.AMHARIC -> descriptionAm.ifBlank { descriptionEn }
        AppLanguage.ENGLISH -> descriptionEn.ifBlank { descriptionOm }
    }
}
