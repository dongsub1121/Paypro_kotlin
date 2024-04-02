package kr.nicepayment.paypro

import android.util.Log
import kr.nicepayment.paypro.PaymentMethod.*
import kr.nicepayment.paypro.ProtocolSetDelegate.Pad.*
import kr.nicepayment.paypro.ProtocolState.*


class NicePaymentsPayProBody: NicePaymentBaseBody() {

    private var barcodeType: String by ProtocolSetDelegate(1, ALPHA)
    private var barcode: String by ProtocolSetDelegate(30, ALPHA)
    private var amount: String by ProtocolSetDelegate(12, NUMBER)
    private var tax: String by ProtocolSetDelegate(12, NUMBER)
    private var tip: String by ProtocolSetDelegate(12, NUMBER)
    private var pay: String by ProtocolSetDelegate(3, ALPHA)
    private var catType: String by ProtocolSetDelegate(1, ALPHA)
    private var currency: String by ProtocolSetDelegate(3, ALPHA)
    private var orderNumber: String by ProtocolSetDelegate(12, ALPHA)
    private var noTax: String by ProtocolSetDelegate(12, NUMBER)
    private var exchangeRate: String by ProtocolSetDelegate(24, NUMBER)
    private var subBusinessNumber: String by ProtocolSetDelegate(10, ALPHA)
    private var authorizationDate: String by ProtocolSetDelegate(6, ALPHA)
    private var authorizationNumber: String by ProtocolSetDelegate(12, ALPHA)
    private var cancelType: String by ProtocolSetDelegate(1, ALPHA)
    private var cancelReason: String by ProtocolSetDelegate(2, ALPHA)
    private var goodsItem: String by ProtocolSetDelegate(256, ALPHA)
    private var reserved: String by ProtocolSetDelegate(512, ALPHA)

    companion object {
        const val jobCode_Authorization = "8066"
        const val jobCode_inquiry = "8068"
        const val jobCode_netCancel = "8069"
        const val jobCode_Cancel = "8067"
    }
    override val codeOfAuthorization: String
        get() = jobCode_Authorization
    override val codeOfInquiry: String

        get() = jobCode_inquiry
    override val codeOfNetCancel: String
        get() = jobCode_netCancel
    override val condOfCancel: String
        get() = jobCode_Cancel

    override fun orderNumber(number: String?) {
        orderNumber = number?: getCurrentDateTime()
    }

    override fun barcode(code: String) {
        barcode = code
        updatePay(code)
    }

    override fun barcodeType(type: BarcodeType) {
        barcodeType = when(type) {
            BarcodeType.QR -> "Q"
            BarcodeType.BARCODE -> "B"
        }
    }

    override fun amount(amount: Number) {
        amount.toInt().let { this.amount = it.toString() }
    }

    override fun tax(tax: Number) {
        tax.toInt().let { this.tax = it.toString() }
    }

    override fun tip(tip: Number) {
        tip.toInt().let { this.tip = it.toString() }
    }

    override fun authorizationDate(date: String) {
        authorizationDate = date
    }

    override fun authorizationNumber(number: String) {
        authorizationNumber = number
    }

    private fun updatePay(code: String) {
        when {
            code.startsWith("L") -> {
                "LIQ"
            }
            code.matches("^14.*|^15.*|^16.*".toRegex()) -> {
                "WXP"
            }
            code.matches("^28.*|^29.*|^30.*|^31.*".toRegex()) -> {
                "ALP"
            }
            else -> {
                "Unknown"
            }
        }.also { pay = it }
    }

    override fun authorization(): String {
        return barcodeType + barcode + amount + tax + tip + pay +
                catType + currency + orderNumber + noTax + exchangeRate + subBusinessNumber +
                authorizationDate + authorizationNumber + cancelType + cancelReason + goodsItem + reserved + '\r'
    }
    override fun inquiry(): String {
        return barcodeType + barcode + amount + tax + tip + pay +
                catType + currency + orderNumber + noTax + exchangeRate + subBusinessNumber +
                authorizationDate + authorizationNumber + cancelType + cancelReason + goodsItem + reserved + '\r'
    }
    override fun cancellation(): String {
        return barcodeType + barcode + amount + tax + tip + pay +
                catType + currency + orderNumber + noTax + exchangeRate + subBusinessNumber +
                authorizationDate + authorizationNumber + cancelType + cancelReason + goodsItem + reserved + '\r'
    }

    override fun netCancel(): String {
        return barcodeType + barcode + amount + tax + tip + pay +
                catType + currency + orderNumber + noTax + exchangeRate + subBusinessNumber +
                authorizationDate + authorizationNumber + cancelType + cancelReason + goodsItem + reserved + '\r'
    }

    override fun parseAuthorizationResponse(response: ByteArray): NicePaymentsResult {
        val point = 116

        val resCode = response.sliceArray(point + 0 until point + 4).toString(Charsets.UTF_8)
        val authNum = response.sliceArray(point + 4 until point + 16).toString(Charsets.UTF_8)
        val authDate = response.sliceArray(point + 16 until point + 28).toString(Charsets.UTF_8)
        val authOrderNumber = response.sliceArray(point + 28 until point + 40).toString(Charsets.UTF_8)
        val amount = response.sliceArray(point + 40 until point + 52).toString(Charsets.UTF_8).toDouble()
        val orderNumber = response.sliceArray(point + 136 until point + 148).toString(Charsets.UTF_8)

        val pay = when (response.sliceArray(point + 148 until point + 151).toString(Charsets.UTF_8)) {
            "WXP" -> "WeChat"
            "ALP" ->   "Alipay"
            "LIQ" -> "Liquid"

            else -> ({}).toString()
        }

        //val barcodeType = array.sliceArray(point + 151 until point + 152).toString(Charsets.UTF_8)
        //val barcode = array.sliceArray(point + 152 until point + 182).toString(Charsets.UTF_8)
        //val exchangeRate = array.sliceArray(point + 182 until point + 209).toString(Charsets.UTF_8)
        //val reserved = array.sliceArray(point + 209 until point + 512).toString(Charsets.UTF_8)

        return NicePaymentsResult(result =
            when(resCode) {
                "0000" -> Result.SUCCESS
                "UPAY" -> Result.PROCESSING
                else  -> Result.FAIL }, resCode = resCode
            ).apply {
                this.pay = pay
                this.orderNumber = orderNumber

                this.authDate = authDate
                this.authNum = authNum
                this.authOrderNumber = authOrderNumber
                this.amount = amount
                this.state = if (resCode == "UPAY") INQUIRY else AUTHORIZATION
                this.method = PAYPRO }
    }

    override fun parseInquiryResponse(response: ByteArray): NicePaymentsResult {
        val point = 116

        val resCode = response.sliceArray(point + 0 until point + 4).toString(Charsets.UTF_8)
        val authNum = response.sliceArray(point + 4 until point + 16).toString(Charsets.UTF_8)
        val authDate = response.sliceArray(point + 16 until point + 28).toString(Charsets.UTF_8)
        val authOrderNumber = response.sliceArray(point + 28 until point + 40).toString(Charsets.UTF_8)
        val amount = response.sliceArray(point + 40 until point + 52).toString(Charsets.UTF_8).toDouble()
        val orderNumber = response.sliceArray(point + 136 until point + 148).toString(Charsets.UTF_8)

        val pay = when (response.sliceArray(point + 148 until point + 151).toString(Charsets.UTF_8)) {
            "WXP" -> "WeChat"
            "ALP" ->   "Alipay"
            "LIQ" -> "Liquid"

            else -> ({}).toString()
        }

        //val barcodeType = array.sliceArray(point + 151 until point + 152).toString(Charsets.UTF_8)
        //val barcode = array.sliceArray(point + 152 until point + 182).toString(Charsets.UTF_8)
        //val exchangeRate = array.sliceArray(point + 182 until point + 209).toString(Charsets.UTF_8)
        //val reserved = array.sliceArray(point + 209 until point + 512).toString(Charsets.UTF_8)

        return NicePaymentsResult(result =
            when(resCode) {
                "0000" -> Result.SUCCESS
                "UPAY" -> Result.PROCESSING
                else  -> Result.FAIL }, resCode = resCode
            ).apply {
                this.pay = pay
                this.orderNumber = orderNumber
                this.authDate = authDate
                this.authNum = authNum
                this.authOrderNumber = authOrderNumber
                this.amount = amount
                this.state = if (resCode == "UPAY") INQUIRY else AUTHORIZATION
                this.method = PAYPRO
            }
    }

    override fun toString(): String {
        return "BarcodeType: $barcodeType, " +
                "Barcode: $barcode, " +
                "Amount: $amount, " +
                "Tax: $tax, " +
                "Tip: $tip, " +
                "Pay: $pay, " +
                "CatType: $catType, " +
                "Currency: $currency, " +
                "OrderNumber: $orderNumber, " +
                "NoTax: $noTax, " +
                "ExchangeRate: $exchangeRate, " +
                "SubBusinessNumber: $subBusinessNumber, " +
                "AuthorizationDate: $authorizationDate, " +
                "AuthorizationNumber: $authorizationNumber, " +
                "CancelType: $cancelType, " +
                "CancelReason: $cancelReason, " +
                "GoodsItem: $goodsItem, " +
                "Reserved: $reserved\n" +
                "Data : $barcodeType$barcode$amount$tax$tip$pay$catType " +
                          "$currency$orderNumber$noTax$exchangeRate$subBusinessNumber$authorizationDate"  +
                          "$authorizationNumber$cancelType$cancelReason$goodsItem/ and Reserved/"
    }
}