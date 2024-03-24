package kr.nicepayment.paypro

import kr.nicepayment.paypro.ProtocolSetDelegate.Pad.*


class ProtocolDataBuilder {
    private var stx: Byte = 0x02
    private var totalLen: String by ProtocolSetDelegate(4, NUMBER)
    private var identity: Int = 0x1212
    private var specVersion: String by ProtocolSetDelegate(3, ALPHA)
    private var dllVersion: String by ProtocolSetDelegate(8, ALPHA)
    private var cancelTransaction: String by ProtocolSetDelegate(1, ALPHA)
    private var filler: String by ProtocolSetDelegate(5, ALPHA)
    private var header: NicePaymentHeader
    private var body: NicePaymentBaseBody
    private var etx: Byte = 0x03

    constructor(
        merchant: String,
        amount: Number,
        tax: Number,
        tip: Number,
        payMethod: PaymentMethod,
        barcodeType: BarcodeType,
        barcode: String
    ) {
        specVersion = "PFC"
        dllVersion = "1.01.001"
        body = setBody(payMethod).apply {
            amount(amount)
            tax(tax)
            tip(tip)
            orderNumber()
            barcode(barcode)
            barcodeType(barcodeType)
        }
        header = NicePaymentHeader().apply {
            jobCode(body.codeOfAuthorization)
            merchant(merchant)
        }
    }

    private fun setBody(method: PaymentMethod): NicePaymentBaseBody {
        return when(method) {
            PaymentMethod.PAYPRO -> NicePaymentsPayProBody()
            else -> NicePaymentsPayProBody()
        }
    }

    fun setState(newState: ProtocolState): ProtocolDataBuilder {
        val code = when(newState) {
            ProtocolState.AUTHORIZATION -> body.codeOfAuthorization
            ProtocolState.INQUIRY -> body.codeOfInquiry
            ProtocolState.CANCEL -> body.condOfCancel
            ProtocolState.NET_CANCEL -> body.codeOfNetCancel
        }
        header.jobCode(code)
        return this
    }

    fun parseResponse(response: ByteArray): NicePaymentsResult {
        return body.parseResponse(response)
    }


    fun build(): ByteArray {

        val stxArray = byteArrayOf(stx)
        val identityArray = identity.toString().toByteArray(Charsets.UTF_8)
        val specVersionArray = specVersion.toByteArray(Charsets.UTF_8)
        val dllVersionArray = dllVersion.toByteArray(Charsets.UTF_8)
        val cancelTransactionArray = cancelTransaction.toByteArray(Charsets.UTF_8)
        val fillerArray = filler.toByteArray(Charsets.UTF_8)
        val headerArray = header.toString().toByteArray(Charsets.UTF_8)
        val bodyArray = body.toString().toByteArray(Charsets.UTF_8)
        val etxArray = byteArrayOf(etx)

        val combinedArray = identityArray + specVersionArray + dllVersionArray +
                cancelTransactionArray + fillerArray + headerArray + bodyArray + etxArray

        totalLen = (combinedArray.size + stxArray.size + totalLen.length).toString()
        val totalLenArray = totalLen.toByteArray(Charsets.UTF_8)

        return stxArray + totalLenArray + combinedArray
    }



}