package kr.nicepayment.paypro

abstract class NicePaymentBaseBody {

    abstract val codeOfAuthorization: String
    abstract val codeOfInquiry: String
    abstract val codeOfNetCancel: String
    abstract val condOfCancel: String
    abstract fun orderNumber()
    abstract fun barcode(code: String)
    abstract fun barcodeType(type: BarcodeType)
    abstract fun amount(amount: Number)
    abstract fun tax(tax: Number)
    abstract fun tip(tip: Number)
    abstract fun authorization(): String
    abstract fun cancellation(): String
    abstract fun parseResponse(response: ByteArray): NicePaymentsResult
}
