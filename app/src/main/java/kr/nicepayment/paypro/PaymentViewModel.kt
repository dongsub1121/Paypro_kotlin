package kr.nicepayment.paypro

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class PaymentViewModel : ViewModel() {

    private val _amount = MutableStateFlow(0.0)
    val amount = _amount.asStateFlow()

    private val _barcode = MutableStateFlow("")
    val barcode = _barcode.asStateFlow()

    private val _barcodeType = MutableStateFlow(BarcodeType.BARCODE)
    val barcodeType = _barcodeType.asStateFlow()

    private val _payMethod = MutableStateFlow(PaymentMethod.PAYPRO)
    val payMethod = _payMethod.asStateFlow()

    private val _processing = MutableStateFlow(false)
    val processing = _processing.asStateFlow()

    private val _authorizationRequired = MutableStateFlow(false)
    val authorizationRequired = _authorizationRequired.asStateFlow()

/*
    private val _paymentResult = MutableLiveData<NicePaymentsResult>()
    val paymentResult: LiveData<NicePaymentsResult> = _paymentResult
*/

    private val _paymentResult = MutableLiveData<String>()
    val paymentResult: LiveData<String> = _paymentResult

    init {
        observePaymentConditions()
    }

    private fun observePaymentConditions() {
        viewModelScope.launch {
            combine(amount, barcode) { amount, barcode ->
                amount > 1 && barcode.length > 10
            }.collect { isAuthorizationRequired ->
                _authorizationRequired.value = isAuthorizationRequired
            }
        }
    }

    fun setAmount(amount: Double) {
        Log.e("viewModel",amount.toString())
        _amount.value = amount
    }

    fun setMethod(method: PaymentMethod) {
        _payMethod.value = method
    }

    fun setBarcode(code : String , type: BarcodeType) {
        Log.e("viewModel_setBarcode",code)
        _barcode.value = code
        _barcodeType.value = type
    }

    fun requestAuthorization() {
        viewModelScope.launch {
            try {
                //val result = NicePaymentsProtocolService().initialRequest(createCheckOut())
                val result = NicePaymentsProtocolService2().requestPayment(createCheckOut())
                //_paymentResult.value = result
            } catch (e: Exception) {
                e.printStackTrace()
            } /*catch (e: NetworkConnectionException) {
                Log.e("ViewModel", "Network Connection Error: ${e.localizedMessage}")
                _paymentResult.value = NicePaymentsResult(Result.FAIL, "Network Connection Error")
            } catch (e: NetworkDataException) {
                Log.e("ViewModel", "Data Transmission Error: ${e.localizedMessage}")
                _paymentResult.value = NicePaymentsResult(Result.FAIL, "Data Transmission Error")
            } catch (e: TimeoutCancellationException) {
                Log.e("ViewModel", "Timeout Error: ${e.localizedMessage}")
                _paymentResult.value = NicePaymentsResult(Result.FAIL, "Timeout Error")
            } catch (e: Exception) {
                Log.e("ViewModel", "General Error: ${e.localizedMessage}")
                _paymentResult.value = NicePaymentsResult(Result.FAIL, "General Error")
            }*/
        }
    }

    fun requestCancellation(nicePaymentsResult: NicePaymentsResult) {
        viewModelScope.launch {
            try {
                val result = NicePaymentsProtocolService2().requestPayment(nicePaymentsResult)
                _paymentResult.value = result.toString()
            } catch (e: Exception) {
                e.printStackTrace()
            } /*catch (e: NetworkConnectionException) {
                Log.e("ViewModel", "Network Connection Error: ${e.localizedMessage}")
                _paymentResult.value = NicePaymentsResult(Result.FAIL, "Network Connection Error")
            } catch (e: NetworkDataException) {
                Log.e("ViewModel", "Data Transmission Error: ${e.localizedMessage}")
                _paymentResult.value = NicePaymentsResult(Result.FAIL, "Data Transmission Error")
            } catch (e: TimeoutCancellationException) {
                Log.e("ViewModel", "Timeout Error: ${e.localizedMessage}")
                _paymentResult.value = NicePaymentsResult(Result.FAIL, "Timeout Error")
            } catch (e: Exception) {
                Log.e("ViewModel", "General Error: ${e.localizedMessage}")
                _paymentResult.value = NicePaymentsResult(Result.FAIL, "General Error")
            }*/
        }
    }

    private fun createCheckOut(): CheckOut {
        val merchant = Merchant(
            tid = "1802100001",
            taxRate = 10.0,
            tip = 0.0
        )

        val listItem: List<Item> = arrayListOf(Item(name = "mpas", price = amount.value, quantity = 1))
        val order = Order(listItem)
        val cart = Cart(order, merchant.tip.toInt())

        return CheckOut(merchant = merchant, cart = cart, paymentMethod = _payMethod.value).apply {
            barcode(_barcode.value, _barcodeType.value)
        }
    }
}