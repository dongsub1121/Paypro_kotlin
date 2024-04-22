package kr.nicepayment.paypro.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete

@Dao
interface NicePaymentsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPaymentResult(paymentResult: NicePaymentsResultEntity)

    @Update
    suspend fun updatePaymentResult(paymentResult: NicePaymentsResultEntity)

    @Delete
    suspend fun deletePaymentResult(paymentResult: NicePaymentsResultEntity)

    @Query("SELECT * FROM NicePaymentsResultEntity WHERE id = :id")
    suspend fun getPaymentResultById(id: Int): NicePaymentsResultEntity?
}