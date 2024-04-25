package kr.nicepayment.paypro

import kr.nicepayment.paypro.screen.utils.ProtocolSetDelegate
import kr.nicepayment.paypro.screen.utils.ProtocolSetDelegate.Pad.*

class NicePaymentHeader{
        private var jobCode: String by ProtocolSetDelegate(4, ALPHA)
        private var tid: String by ProtocolSetDelegate(10, ALPHA)
        private var protocolVersion: String by ProtocolSetDelegate(12, ALPHA)
        private var swVersion: String by ProtocolSetDelegate(10, ALPHA, "1000100001")
        private var hwUniqueId: String by ProtocolSetDelegate(10, ALPHA, "mpas1001")
        private var ktc: String by ProtocolSetDelegate(32, ALPHA, "#".repeat(32))
        private var sendDateTime: String by ProtocolSetDelegate(12, ALPHA, getCurrentDateTime())
        private var filler: String by ProtocolSetDelegate(5, ALPHA)
        private var packet: String by ProtocolSetDelegate(1, ALPHA,"R")

    private fun update(){
        sendDateTime = getCurrentDateTime()
    }

    fun jobCode(code: String) {
        jobCode = code
        update()
    }

    fun merchant(merchant: String) {
        tid = merchant
    }



    val data: String
        get() = jobCode + tid + protocolVersion + swVersion + hwUniqueId +
                ktc + sendDateTime + filler + packet

    override fun toString(): String {
        return "jobCode='$jobCode', tid='$tid', protocolVersion='$protocolVersion', swVersion='$swVersion', " +
                "hwUniqueId='$hwUniqueId', ktc='$ktc', sendDateTime='$sendDateTime', filler='$filler', packet='$packet')" +"\n" +
                "Data= $data "

    }
}
