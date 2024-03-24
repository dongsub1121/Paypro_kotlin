package kr.nicepayment.paypro

import kr.nicepayment.paypro.ProtocolSetDelegate.Pad.*

class NicePaymentHeader {
    private var jobCode: String by ProtocolSetDelegate(4, ALPHA)
    private var tid: String by ProtocolSetDelegate(10, ALPHA)
    private var protocolVersion: String by ProtocolSetDelegate(12, ALPHA)
    private var swVersion: String by ProtocolSetDelegate(10, ALPHA)
    private var hwUniqueId: String by ProtocolSetDelegate(10, ALPHA)
    private var ktc: String by ProtocolSetDelegate(32, ALPHA)
    private var sendDateTime: String by ProtocolSetDelegate(12, ALPHA)
    private var filler: String by ProtocolSetDelegate(5, ALPHA)
    private var packet: String by ProtocolSetDelegate(1, ALPHA)

    init {
        swVersion = "1000100001"
        hwUniqueId = "mpas"
        packet = "R"
        ktc = "#".repeat(ktc.length)
    }

    private fun update(){
        sendDateTime = getCurrentDateTime()
    }

    fun jobCode(code: String) {
        jobCode = code
        //update()
    }

    fun merchant(merchant: String) {
        tid = merchant
    }

    val data = jobCode + tid + protocolVersion + swVersion + hwUniqueId +
            ktc + sendDateTime + filler + packet

}
