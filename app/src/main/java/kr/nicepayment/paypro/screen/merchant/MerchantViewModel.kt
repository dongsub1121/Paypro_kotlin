package kr.nicepayment.paypro.screen.merchant

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kr.nicepayment.paypro.model.MerchantDao
import kr.nicepayment.paypro.model.MerchantEntity
import kr.nicepayment.paypro.model.MerchantRepository

class MerchantViewModel(private val repo: MerchantRepository): ViewModel() {

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    // LiveData holding all merchants
    val allMerchants = repo.getAllMerchants().asLiveData()

    // LiveData for merchants filtered by site name
    fun getMerchantsBySitename(sitename: String) = repo.getMerchantsBySitename(sitename).asLiveData()

    // LiveData for merchants filtered by master status
    fun getMerchantsByMaster(value: Boolean) = repo.getMerchantsByMaster(value).asLiveData()

    // Update master status for a given sitename
    fun updateMasterBySitename(masterValue: Boolean, sitename: String) {
        viewModelScope.launch {
            repo.updateMasterBySitename(masterValue, sitename)
        }
    }

    // Insert merchants into the database
    fun insertMerchants(vararg merchants: MerchantEntity) {
        viewModelScope.launch {
            repo.insertMerchants(*merchants)
        }
    }

    // Replace merchants in the database
    fun replaceMerchants(vararg merchants: MerchantEntity) {
        viewModelScope.launch {
            repo.replaceMerchants(*merchants)
        }
    }

    // Delete a merchant by sitename
    fun deleteMerchantBySitename(sitename: String) {
        viewModelScope.launch {
            repo.deleteMerchantBySitename(sitename)
        }
    }

    // Delete all merchants
    fun deleteAllMerchants() {
        viewModelScope.launch {
            repo.deleteAllMerchants()
        }
    }

    class MerchantViewModelFactory(private val repository: MerchantRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MerchantViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MerchantViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
