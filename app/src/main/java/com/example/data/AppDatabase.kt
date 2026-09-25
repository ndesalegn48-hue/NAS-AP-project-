package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.model.AdvertisementEntity
import com.example.model.CommunityNotice
import com.example.model.Inquiry

@Database(
    entities = [AdvertisementEntity::class, CommunityNotice::class, Inquiry::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun advertisementDao(): AdvertisementDao
    abstract fun noticeDao(): NoticeDao
    abstract fun inquiryDao(): InquiryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gimbi_local_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
