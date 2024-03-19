package kr.nicepayment.paypro

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NicePaymentHeader(merchant: String, payMethod: PaymentMethod, state: ProtocolState) {

    private var jobCode: String by ProtocolSetDelegate(4,ProtocolSetDelegate.Pad.ALPHA)
    private var tid: String by ProtocolSetDelegate(10,ProtocolSetDelegate.Pad.ALPHA)
    private var protocolVersion: String by ProtocolSetDelegate(12,ProtocolSetDelegate.Pad.ALPHA)
    private var swVersion: String by ProtocolSetDelegate(10,ProtocolSetDelegate.Pad.ALPHA)
    private var hwUniqueId: String by ProtocolSetDelegate(10,ProtocolSetDelegate.Pad.ALPHA)
    private var ktc: String by ProtocolSetDelegate(32,ProtocolSetDelegate.Pad.ALPHA)
    private var sendDateTime: String by ProtocolSetDelegate(12,ProtocolSetDelegate.Pad.ALPHA)
    private var filler: String by ProtocolSetDelegate(5,ProtocolSetDelegate.Pad.ALPHA)
    private var packet: String by ProtocolSetDelegate(1,ProtocolSetDelegate.Pad.ALPHA)

    init {
        swVersion = "1000100001"
        hwUniqueId = "mpas"
        packet = "R"
        ktc.let { "#".repeat(it.length) }
        tid = merchant
        jobCode = when{
            payMethod == PaymentMethod.PAYPRO && state == ProtocolState.AUTHORIZATION -> "8066"
            payMethod == PaymentMethod.PAYPRO && state == ProtocolState.INQUIRY -> "8066"
            payMethod == PaymentMethod.PAYPRO && state == ProtocolState.CANCEL -> "8066"
            payMethod == PaymentMethod.PAYPRO && state == ProtocolState.NET_CANCEL -> "8066"
            else -> "Unknown"
        }
        refresh()
    }

    private fun refresh(){
        val currentTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss")
        sendDateTime =  currentTime.format(formatter)
    }

    val data = jobCode + tid + protocolVersion + swVersion + hwUniqueId + ktc + sendDateTime + filler + packet

}
