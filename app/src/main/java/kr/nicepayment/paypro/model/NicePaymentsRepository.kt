package kr.nicepayment.paypro.model

class NicePaymentsRepository(private val nicePaymentsDao: NicePaymentsDao) {

    suspend fun getPaymentResultById(id: Int): NicePaymentsResultEntity? {
        return nicePaymentsDao.getPaymentResultById(id)
    }

    suspend fun insertPaymentResult(paymentResult: NicePaymentsResultEntity) {
        nicePaymentsDao.insertPaymentResult(paymentResult)
    }

    suspend fun updatePaymentResult(paymentResult: NicePaymentsResultEntity) {
        nicePaymentsDao.updatePaymentResult(paymentResult)
    }

    suspend fun deletePaymentResult(paymentResult: NicePaymentsResultEntity) {
        nicePaymentsDao.deletePaymentResult(paymentResult)
    }
}