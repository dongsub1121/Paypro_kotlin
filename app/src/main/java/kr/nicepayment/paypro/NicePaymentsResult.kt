package kr.nicepayment.paypro

data class NicePaymentsResult(
    private var resCode: String,
    private var pay: String,
    private var orderNumber: String,
    private var authDate: String,
    private var authNum: String,
    private var authOrderNumber: String,
    private var amount: Int
) {
    var taxRate: Double = 0.0
    var merchant: String = ""
    var tax: Int = 0
    var tip: Int = 0
    var state: ProtocolState? = null
    var status: PaymentStatus? = null
    var method: PaymentMethod? =null
}


