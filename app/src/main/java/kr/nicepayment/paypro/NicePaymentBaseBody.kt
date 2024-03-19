package kr.nicepayment.paypro

abstract class NicePaymentBaseBody {
    abstract fun refresh()
    abstract fun barcode(code: String)
    abstract fun barcodeType(type: BarcodeType)
    abstract fun amount(amount: Number)
    abstract fun tax(tax: Number)
    abstract fun tip(tip: Number)
    abstract fun toData(): String
    abstract fun parseResponse(response: ByteArray): NicePaymentArgument
}
