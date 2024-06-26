package kr.nicepayment.paypro

data class NicePaymentsResult(
    val result: Result,
    var resCode: String,
    var resultReason: String? = null
) {
    val barcode: String = ""
    val barcodeType: BarcodeType = BarcodeType.BARCODE
    var orderNumber = ""
    var authorizationDate = ""
    var authorizationNumber = ""
    var authorizationOrderNumber = ""
    var amount: Double = 0.0
    var taxRate: Double = 0.0
    var merchant= ""
    var tax: Double = 0.0
    var tip: Double = 0.0
    var state: ProtocolState? = null
    var status: PaymentStatus? = null
    var method: PaymentMethod? =null
}


