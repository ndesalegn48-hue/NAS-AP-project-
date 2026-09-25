package com.example.model

object Translation {
    fun appTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AFAAN_OROMOO -> "Gimbi Local"
        AppLanguage.AMHARIC -> "ጊምቢ ሎካል"
        AppLanguage.ENGLISH -> "Gimbi Local"
    }

    fun appTagline(lang: AppLanguage): String = when (lang) {
        AppLanguage.AFAAN_OROMOO -> "Argadhu. Beeksisi. Guddisi."
        AppLanguage.AMHARIC -> "ያግኙ። ያስተዋውቁ። ያሳድጉ።"
        AppLanguage.ENGLISH -> "Discover it. Advertise it. Grow it."
    }

    fun tabDiscover(lang: AppLanguage): String = when (lang) {
        AppLanguage.AFAAN_OROMOO -> "Sakatta'i"
        AppLanguage.AMHARIC -> "አግኝ"
        AppLanguage.ENGLISH -> "Discover"
    }

    fun tabCategories(lang: AppLanguage): String = when (lang) {
        AppLanguage.AFAAN_OROMOO -> "Ramaddii"
        AppLanguage.AMHARIC -> "ምድቦች"
        AppLanguage.ENGLISH -> "Categories"
    }

    fun tabPostAd(lang: AppLanguage): String = when (lang) {
        AppLanguage.AFAAN_OROMOO -> "Beeksisi"
        AppLanguage.AMHARIC -> "አስተዋውቅ"
        AppLanguage.ENGLISH -> "Advertise"
    }

    fun tabDashboard(lang: AppLanguage): String = when (lang) {
        AppLanguage.AFAAN_OROMOO -> "Daashboordii"
        AppLanguage.AMHARIC -> "ዳሽቦርድ"
        AppLanguage.ENGLISH -> "Dashboard"
    }

    fun tabAdmin(lang: AppLanguage): String = when (lang) {
        AppLanguage.AFAAN_OROMOO -> "Hoggansa"
        AppLanguage.AMHARIC -> "አስተዳደር"
        AppLanguage.ENGLISH -> "Admin"
    }

    fun searchPlaceholder(lang: AppLanguage): String = when (lang) {
        AppLanguage.AFAAN_OROMOO -> "Daldala, barnoota, buna, hojii sakatta'i..."
        AppLanguage.AMHARIC -> "ንግድ፣ ትምህርት፣ ቡና፣ ስራ ይፈልጉ..."
        AppLanguage.ENGLISH -> "Search businesses, schools, coffee, jobs..."
    }

    fun noticeBoardTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AFAAN_OROMOO -> "Beeksisa Hawaasa Gimbi"
        AppLanguage.AMHARIC -> "የጊምቢ ማህበረሰብ ማስታወቂያዎች"
        AppLanguage.ENGLISH -> "Gimbi Community Notices"
    }

    fun featuredTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AFAAN_OROMOO -> "Beeksisa Filatamaa"
        AppLanguage.AMHARIC -> "ተለይተው የቀረቡ ማስታወቂያዎች"
        AppLanguage.ENGLISH -> "Featured & Premium Ads"
    }

    fun allAdsTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AFAAN_OROMOO -> "Beeksisoota Haaraa"
        AppLanguage.AMHARIC -> "የቅርብ ጊዜ ማስታወቂያዎች"
        AppLanguage.ENGLISH -> "Recent Local Listings"
    }

    fun verifiedBadge(lang: AppLanguage): String = when (lang) {
        AppLanguage.AFAAN_OROMOO -> "Mirkanaa'e"
        AppLanguage.AMHARIC -> "የተረጋገጠ"
        AppLanguage.ENGLISH -> "Verified"
    }

    fun callAction(lang: AppLanguage): String = when (lang) {
        AppLanguage.AFAAN_OROMOO -> "Bilbili"
        AppLanguage.AMHARIC -> "ይደውሉ"
        AppLanguage.ENGLISH -> "Call"
    }

    fun whatsappAction(lang: AppLanguage): String = "WhatsApp"

    fun directionsAction(lang: AppLanguage): String = when (lang) {
        AppLanguage.AFAAN_OROMOO -> "Kallattii"
        AppLanguage.AMHARIC -> "አቅጣጫ"
        AppLanguage.ENGLISH -> "Directions"
    }

    fun sendInquiryAction(lang: AppLanguage): String = when (lang) {
        AppLanguage.AFAAN_OROMOO -> "Gaaffii Ergi"
        AppLanguage.AMHARIC -> "ጥያቄ ይላኩ"
        AppLanguage.ENGLISH -> "Send Inquiry"
    }

    fun packagesTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AFAAN_OROMOO -> "Paakeejota Beeksisaa"
        AppLanguage.AMHARIC -> "የማስታወቂያ ፓኬጆች"
        AppLanguage.ENGLISH -> "Advertising Packages"
    }

    fun dashboardMetricsTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AFAAN_OROMOO -> "Bu'aa fi Lakkoofsa Daashboordii"
        AppLanguage.AMHARIC -> "የአፈጻጸም ዳሽቦርድ መረጃዎች"
        AppLanguage.ENGLISH -> "Advertiser Performance Metrics"
    }

    fun viewsLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.AFAAN_OROMOO -> "Ilaalcha"
        AppLanguage.AMHARIC -> "እይታዎች"
        AppLanguage.ENGLISH -> "Views"
    }

    fun callsLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.AFAAN_OROMOO -> "Bilbila"
        AppLanguage.AMHARIC -> "ጥሪዎች"
        AppLanguage.ENGLISH -> "Calls"
    }

    fun whatsappClicksLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.AFAAN_OROMOO -> "Tuqaa WhatsApp"
        AppLanguage.AMHARIC -> "የዋትስአፕ ንክኪዎች"
        AppLanguage.ENGLISH -> "WhatsApp Leads"
    }

    fun profileVisitsLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.AFAAN_OROMOO -> "Daawwanna Profaayilii"
        AppLanguage.AMHARIC -> "የመገለጫ ጉብኝት"
        AppLanguage.ENGLISH -> "Profile Visits"
    }

    fun safetyTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AFAAN_OROMOO -> "Nageenya fi Amanamummaa"
        AppLanguage.AMHARIC -> "ደህንነት እና አስተማማኝነት"
        AppLanguage.ENGLISH -> "Safety & Trust Rules"
    }

    fun safetyDescription(lang: AppLanguage): String = when (lang) {
        AppLanguage.AFAAN_OROMOO -> "Dhaabbileen mirkanaa'an mallattoo asxaa qabu. Daldala fi gargaarsa sobaa irraa of eeggadhaa. Beeksisoota sirrii hin taane gabaasaa."
        AppLanguage.AMHARIC -> "የተረጋገጡ ተቋማት የሰማያዊ ማረጋገጫ ምልክት አላቸው። ካልተረጋገጠ ልገሳ ወይም የሀሰት ግብይት እራስዎን ይጠብቁ። አጠራጣሪ ማስታወቂያዎችን ይጠቁሙ።"
        AppLanguage.ENGLISH -> "Verified institutions carry a blue badge. Protect yourself from unverified fundraising or scams. Report suspicious advertisements to moderation."
    }

    fun selectLanguage(lang: AppLanguage): String = when (lang) {
        AppLanguage.AFAAN_OROMOO -> "Afaan Filadhu"
        AppLanguage.AMHARIC -> "ቋንቋ ይምረጡ"
        AppLanguage.ENGLISH -> "Choose Language"
    }

    fun postAdHeadline(lang: AppLanguage): String = when (lang) {
        AppLanguage.AFAAN_OROMOO -> "Daldala ykn Tajaajila kee Magaalaa Gimbii keessatti beeksisi"
        AppLanguage.AMHARIC -> "ንግድዎን ወይም አገልግሎትዎን በጊምቢ ከተማ ውስጥ ያስተዋውቁ"
        AppLanguage.ENGLISH -> "Advertise your business or institution in Gimbi town"
    }
}
