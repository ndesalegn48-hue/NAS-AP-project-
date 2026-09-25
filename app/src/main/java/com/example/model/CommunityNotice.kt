package com.example.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "community_notices")
data class CommunityNotice(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val titleEn: String,
    val titleOm: String,
    val titleAm: String,
    val contentEn: String,
    val contentOm: String,
    val contentAm: String,
    val issuedBy: String,
    val tag: String, // "MARKET", "HEALTH", "AGRICULTURE", "EDUCATION"
    val dateString: String,
    val isUrgent: Boolean = false
) {
    fun getTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.AFAAN_OROMOO -> titleOm
        AppLanguage.AMHARIC -> titleAm
        AppLanguage.ENGLISH -> titleEn
    }

    fun getContent(lang: AppLanguage): String = when (lang) {
        AppLanguage.AFAAN_OROMOO -> contentOm
        AppLanguage.AMHARIC -> contentAm
        AppLanguage.ENGLISH -> contentEn
    }
}
