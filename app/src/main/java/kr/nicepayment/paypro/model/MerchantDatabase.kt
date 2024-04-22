package kr.nicepayment.paypro.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MerchantEntity::class], version = 1, exportSchema = false)
abstract class MerchantDatabase : RoomDatabase() {
    abstract fun merchantDao(): MerchantDao

    companion object {
        @Volatile
        private var INSTANCE: MerchantDatabase? = null

        fun getDatabase(context: Context): MerchantDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MerchantDatabase::class.java,
                    "nice_payments_database"
                ).build()
                INSTANCE = instance

                instance
            }
        }
    }
}