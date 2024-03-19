package kr.nicepayment.paypro

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PaymentViewModel : ViewModel() {
    private val _amount = MutableStateFlow(0L)
    val amount = _amount.asStateFlow()

    private val _barcode = MutableStateFlow("")
    val barcode = _barcode.asStateFlow()

    private val _BARCODEType = MutableStateFlow(BarcodeType.BARCODE)
    val barcodeType = _BARCODEType.asStateFlow()

    private val _payMethod = MutableStateFlow(PaymentMethod.PAYPRO)
    val payMethod = _payMethod.asStateFlow()

    fun setAmount(amount: String?) {
        _amount.value = if (amount.isNullOrEmpty()) 0L else amount.toLong()
    }

    fun setBarcode(code : String , type: BarcodeType) {
        _barcode.value = code
        _BARCODEType.value = type
    }

    fun requestAuthorization() {

    }

    fun requestCancellation() {

    }
}