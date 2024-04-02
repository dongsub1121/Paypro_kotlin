package kr.nicepayment.paypro

abstract class NicePaymentBaseBody {

    abstract val codeOfAuthorization: String
    abstract val codeOfInquiry: String
    abstract val codeOfNetCancel: String
    abstract val condOfCancel: String

    fun getBody(state: ProtocolState) {
        when (state) {
            ProtocolState.AUTHORIZATION -> authorization()
            ProtocolState.INQUIRY -> inquiry()
            ProtocolState.CANCEL -> cancellation()
            ProtocolState.NET_CANCEL -> netCancel()
        }
    }

    abstract fun orderNumber(number: String?)
    abstract fun barcode(code: String)
    abstract fun barcodeType(type: BarcodeType)
    abstract fun amount(amount: Number)
    abstract fun tax(tax: Number)
    abstract fun tip(tip: Number)
    abstract fun authorizationDate(date: String)
    abstract fun authorizationNumber(number: String)
    abstract fun authorization(): String
    abstract fun inquiry(): String
    abstract fun cancellation(): String
    abstract fun netCancel(): String
    abstract fun parseAuthorizationResponse(response: ByteArray): NicePaymentsResult
    abstract fun parseInquiryResponse(response: ByteArray): NicePaymentsResult
}
