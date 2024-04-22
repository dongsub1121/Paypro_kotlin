package kr.nicepayment.paypro.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MerchantDao {

    @Query("SELECT * FROM MerchantEntity")
    fun getAll(): Flow<List<MerchantEntity>>

    @Query("SELECT * FROM MerchantEntity WHERE sitename = :sitename")
    fun loadBySitename(sitename: String): Flow<List<MerchantEntity>>

    @Query("SELECT * FROM MerchantEntity WHERE master = :value")
    fun loadByMaster(value: Int): Flow<List<MerchantEntity>>

    @Query("UPDATE MerchantEntity SET master = :masterValue WHERE sitename = :sitename")
    suspend fun updateMasterBySitename(masterValue: Int, sitename: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg merchants: MerchantEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun replace(vararg merchants: MerchantEntity)

    @Query("DELETE FROM MerchantEntity WHERE sitename = :sitename")
    suspend fun deleteBySitename(sitename: String)

    @Query("DELETE FROM MerchantEntity")
    suspend fun deleteAll()
}