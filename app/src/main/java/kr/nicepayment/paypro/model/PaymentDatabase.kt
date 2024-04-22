package kr.nicepayment.paypro.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [NicePaymentsResultEntity::class], version = 1, exportSchema = false)
@TypeConverters(EntityConverters::class)
abstract class PaymentDatabase : RoomDatabase() {
    abstract fun nicePaymentsDao(): NicePaymentsDao

    companion object {
        @Volatile
        private var INSTANCE: PaymentDatabase? = null

        fun getDatabase(context: Context): PaymentDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PaymentDatabase::class.java,
                    "nice_payments_database"
                ).build()
                INSTANCE = instance

                instance
            }
        }
    }
}