package kr.nicepayment.paypro

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kr.nicepayment.paypro.model.PaymentDatabase
import kr.nicepayment.paypro.model.NicePaymentsRepository
import kr.nicepayment.paypro.model.NicePaymentsResultEntity

class NicePaymentsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NicePaymentsRepository

    init {
        val nicePaymentsDao = PaymentDatabase.getDatabase(application).nicePaymentsDao()
        repository = NicePaymentsRepository(nicePaymentsDao)
    }

    suspend fun getPaymentResultById(id: Int): NicePaymentsResultEntity? {
        return repository.getPaymentResultById(id)
    }

    suspend fun insertPaymentResult(paymentResult: NicePaymentsResultEntity) {
        repository.insertPaymentResult(paymentResult)
    }

    suspend fun updatePaymentResult(paymentResult: NicePaymentsResultEntity) {
        repository.updatePaymentResult(paymentResult)
    }

    suspend fun deletePaymentResult(paymentResult: NicePaymentsResultEntity) {
        repository.deletePaymentResult(paymentResult)
    }
}