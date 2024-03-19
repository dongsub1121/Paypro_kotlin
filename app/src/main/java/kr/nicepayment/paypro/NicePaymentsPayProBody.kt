package kr.nicepayment.paypro

import kr.nicepayment.paypro.ProtocolSetDelegate.*


class NicePaymentsPayProBody: NicePaymentBaseBody() {

    private var barcodeType: String by ProtocolSetDelegate(1, Pad.ALPHA)
    private var barcode: String by ProtocolSetDelegate(30, Pad.ALPHA)
    private var amount: String by ProtocolSetDelegate(12, Pad.NUMBER)
    private var tax: String by ProtocolSetDelegate(12, Pad.NUMBER)
    private var tip: String by ProtocolSetDelegate(12, Pad.NUMBER)
    private var pay: String by ProtocolSetDelegate(3, Pad.ALPHA)
    private var catType: String by ProtocolSetDelegate(1, Pad.ALPHA)
    private var currency: String by ProtocolSetDelegate(3, Pad.ALPHA)
    private var orderNumber: String by ProtocolSetDelegate(12, Pad.ALPHA)
    private var noTax: String by ProtocolSetDelegate(12, Pad.NUMBER)
    private var exchangeRate: String by ProtocolSetDelegate(24, Pad.ALPHA)
    private var subBusinessNumber: String by ProtocolSetDelegate(10, Pad.ALPHA)
    private var authorizationDate: String by ProtocolSetDelegate(6, Pad.ALPHA)
    private var authorizationNumber: String by ProtocolSetDelegate(12, Pad.ALPHA)
    private var cancelType: String by ProtocolSetDelegate(1, Pad.ALPHA)
    private var cancelReason: String by ProtocolSetDelegate(2, Pad.ALPHA)
    private var goodsItem: String by ProtocolSetDelegate(256, Pad.ALPHA)
    private var reserved: String by ProtocolSetDelegate(512, Pad.ALPHA)

    init {
        TODO("바코드 정규식 구분 pay set")
    }

    override fun refresh() {
        TODO("orderNumber refresh")
    }

    override fun barcode(code: String) {
        barcode = code
    }

    override fun barcodeType(type: BarcodeType) {
        barcodeType = when(type) {
            BarcodeType.QR -> "Q"
            else -> "B"
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

    override fun toData(): String {
        return barcodeType + barcode + amount + tax + tip + pay +
                catType + currency + orderNumber + noTax + exchangeRate + subBusinessNumber +
                authorizationDate + authorizationNumber + cancelType + cancelReason + goodsItem + reserved
    }

    override fun parseResponse(array: ByteArray): NicePaymentArgument {
        val point = 116

        val resCode = array.sliceArray(point + 0 until point + 4).toString(Charsets.UTF_8)
        val msg = array.sliceArray(point + 4 until point + 40).toString(Charsets.UTF_8)
        val authDate = array.sliceArray(point + 4 until point + 16).toString(Charsets.UTF_8)
        val authNum = array.sliceArray(point + 16 until point + 28).toString(Charsets.UTF_8)
        val authOrderNumber = array.sliceArray(point + 28 until point + 40).toString(Charsets.UTF_8)
        val totPrice = array.sliceArray(point + 52 until point + 64).toString(Charsets.UTF_8).toInt()
        val issuerCode = array.sliceArray(point + 148 until point + 151).toString(Charsets.UTF_8)
        val orderNumber = array.sliceArray(point + 136 until point + 148).toString(Charsets.UTF_8)

        val issuer = when (issuerCode) {
            "WXP" -> {
                "WeChat"
            }

            "ALP" -> {
                "Alipay"
            }

            "LIQ" -> {
                "Liquid"
            }

            else -> {
                ({}).toString()
            }

        }

        return NicePaymentArgument(issuerCode,
            issuer,
            orderNumber,
            authDate,
            authNum,
            authOrderNumber,
            totPrice,
            resCode,
            msg)
    }
}