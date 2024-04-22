package kr.nicepayment.paypro.model

import android.annotation.SuppressLint
import android.content.Context
import kotlinx.coroutines.flow.Flow

class MerchantRepository(private val merchantDao: MerchantDao) {

    //private val merchantDao = MerchantDatabase.getDatabase(context).merchantDao()

    fun getAllMerchants(): Flow<List<MerchantEntity>> {
        return merchantDao.getAll()
    }

    fun getMerchantsBySitename(sitename: String): Flow<List<MerchantEntity>> {
        return merchantDao.loadBySitename(sitename)
    }

    fun getMerchantsByMaster(value: Boolean): Flow<List<MerchantEntity>> {
        return merchantDao.loadByMaster(if (value) 1 else 0)
    }

    suspend fun updateMasterBySitename(masterValue: Boolean, sitename: String) {
        merchantDao.updateMasterBySitename(if (masterValue) 1 else 0, sitename)
    }

    suspend fun insertMerchants(vararg merchants: MerchantEntity) {
        merchantDao.insert(*merchants)
    }

    suspend fun replaceMerchants(vararg merchants: MerchantEntity) {
        merchantDao.replace(*merchants)
    }

    suspend fun deleteMerchantBySitename(sitename: String) {
        merchantDao.deleteBySitename(sitename)
    }

    suspend fun deleteAllMerchants() {
        merchantDao.deleteAll()
    }

    companion object {
        @Volatile private var instance: MerchantRepository? = null

        fun getInstance(context: Context): MerchantRepository {
            return instance ?: synchronized(this) {
                instance ?: MerchantRepository(MerchantDatabase.getDatabase(context).merchantDao()).also {
                    instance = it
                }
            }
        }
    }
}