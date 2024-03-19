package kr.nicepayment.paypro

import java.lang.StringBuilder

class NicePaymentArgument(
    private var issuerCode: String,
    private var issuer: String,
    private var orderNumber: String,
    private var authDate: String,
    private var authNum: String,
    private var authOrderNumber: String,
    private var totPrice: Int,
    private var resCode: String,
    private var msg: String
) {
    val taxRate: Double = 0.0
    val merchant: String = ""
    var state: ProtocolState? = null
    var status: PaymentStatus? = null

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("issuer = $issuer")
        sb.append("orderNumber = $orderNumber")
        sb.append("authDate = $authDate")
        sb.append("authNum = $authNum")
        sb.append("authOrderNumber = $authOrderNumber")
        sb.append("totPrice = $totPrice")
        sb.append("resCode = $resCode")
        sb.append("msg = $msg")
        return sb.toString()
    }
}
