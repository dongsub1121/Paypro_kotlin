package kr.nicepayment.paypro.screen.report

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.nicepayment.paypro.model.SalesModel
import kr.nicepayment.paypro.model.SalesRepository

class SalesViewModel : ViewModel() {

    val salesDbMutableLiveData = MutableLiveData<List<SalesModel.SaleDB>>()
    private val repo = SalesRepository
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun getSales(startDate: String, endDate: String, biz: String, mid: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    repo.getSalesSummary(startDate, endDate, biz, mid)
                }
                if (response.status == "200") {
                    response.saleDBList.let {
                        salesDbMutableLiveData.value = it
                        it.forEach { db ->
                            Log.e("getSalesonSuccess", db.toString())
                        }
                    }
                } else {
                    Log.e("Error", "${response.status}:${response.detail}:${response.message}")
                }
            } catch (e: Exception) {
                Log.e("Error", "Failed to fetch sales data", e)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}