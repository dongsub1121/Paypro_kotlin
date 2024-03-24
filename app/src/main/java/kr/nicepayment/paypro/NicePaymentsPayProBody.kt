package kr.nicepayment.paypro

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
    private var exchangeRate: String by ProtocolSetDelegate(24, ALPHA)
    private var subBusinessNumber: String by ProtocolSetDelegate(10, ALPHA)
    private var authorizationDate: String by ProtocolSetDelegate(6, ALPHA)
    private var authorizationNumber: String by ProtocolSetDelegate(12, ALPHA)
    private var cancelType: String by ProtocolSetDelegate(1, ALPHA)
    private var cancelReason: String by ProtocolSetDelegate(2, ALPHA)
    private var goodsItem: String by ProtocolSetDelegate(256, ALPHA)
    private var reserved: String by ProtocolSetDelegate(512, ALPHA)

    init {
        TODO("바코드 정규식 구분 pay set")
    }

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

    override fun orderNumber() {
        orderNumber = getCurrentDateTime()
    }

    override fun barcode(code: String) {
        barcode = code
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

    override fun authorization(): String {
        return barcodeType + barcode + amount + tax + tip + pay +
                catType + currency + orderNumber + noTax + exchangeRate + subBusinessNumber +
                authorizationDate + authorizationNumber + cancelType + cancelReason + goodsItem + reserved
    }

    override fun cancellation(): String {
        TODO("Not yet implemented")
    }

    override fun parseResponse(response: ByteArray): NicePaymentsResult {
        val point = 116

        val resCode = response.sliceArray(point + 0 until point + 4).toString(Charsets.UTF_8)
        val authDate = response.sliceArray(point + 4 until point + 16).toString(Charsets.UTF_8)
        val authNum = response.sliceArray(point + 16 until point + 28).toString(Charsets.UTF_8)
        val authOrderNumber = response.sliceArray(point + 28 until point + 40).toString(Charsets.UTF_8)
        val amount = response.sliceArray(point + 52 until point + 64).toString(Charsets.UTF_8).toInt()
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

        return NicePaymentsResult(resCode = resCode,
                pay = pay,
                orderNumber = orderNumber,
                authDate = authDate,
                authNum = authNum,
                authOrderNumber = authOrderNumber,
                amount = amount.toString().toInt()
            ).apply {
                this.state = if (resCode == "UPAY") INQUIRY else AUTHORIZATION
                this.method = PAYPRO }
    }
}