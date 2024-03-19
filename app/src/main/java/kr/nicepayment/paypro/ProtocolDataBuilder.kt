package kr.nicepayment.paypro

class ProtocolDataBuilder(
    private var merchant: String,
    var taxRate: Double,
    private var amount: Number,
    private var payMethod: PaymentMethod,
    private var status: PaymentStatus,
    private var state: ProtocolState,
    private var barcodeType: BarcodeType,
    private var barcode: String
) {

    private var stx: Byte = 0x02
    private var totalLen: String by ProtocolSetDelegate(4, ProtocolSetDelegate.Pad.NUMBER)
    private var identity: Int = 0x1212
    private var specVersion: String by ProtocolSetDelegate(3, ProtocolSetDelegate.Pad.ALPHA)
    private var dllVersion: String by ProtocolSetDelegate(8, ProtocolSetDelegate.Pad.ALPHA)
    private var cancelTransaction: String by ProtocolSetDelegate(1, ProtocolSetDelegate.Pad.ALPHA)
    private var filler: String by ProtocolSetDelegate(5, ProtocolSetDelegate.Pad.ALPHA)
    private var header: NicePaymentHeader
    private var body: NicePaymentBaseBody
    private var etx: Byte = 0x03

    init {
        specVersion = "PFC"
        dllVersion = "1.01.001"
        header = NicePaymentHeader(merchant, payMethod, ProtocolState.AUTHORIZATION)
        body = setBody(payMethod).apply {
            amount(amount)
            refresh()
            barcode(barcode)
            barcodeType(barcodeType)
        }
    }

    private fun setBody(method: PaymentMethod): NicePaymentBaseBody {
        return when(method) {
            PaymentMethod.PAYPRO -> NicePaymentsPayProBody()
            else -> NicePaymentsPayProBody()
        }
    }

    fun setState(newState: ProtocolState): ProtocolDataBuilder {
        this.header = NicePaymentHeader(merchant, payMethod, newState)
        return this
    }

    fun parseResponse(response: ByteArray): NicePaymentArgument {
        return body.parseResponse(response)
    }


    fun build(): ByteArray {

        val stxArray = byteArrayOf(stx)
        val identityArray = identity.toString().toByteArray(Charsets.UTF_8)
        val specVersionArray = specVersion.toByteArray(Charsets.UTF_8)
        val dllVersionArray = dllVersion.toByteArray(Charsets.UTF_8)
        val cancelTransactionArray = cancelTransaction.toByteArray(Charsets.UTF_8)
        val fillerArray = filler.toByteArray(Charsets.UTF_8)
        val headerArray = header?.toString()?.toByteArray(Charsets.UTF_8) ?: ByteArray(0)
        val bodyArray = body?.toString()?.toByteArray(Charsets.UTF_8) ?: ByteArray(0)
        val etxArray = byteArrayOf(etx)

        val combinedArray = identityArray + specVersionArray + dllVersionArray +
                cancelTransactionArray + fillerArray + headerArray + bodyArray + etxArray

        totalLen = (combinedArray.size + stxArray.size + totalLen.length).toString()
        val totalLenArray = totalLen.toByteArray(Charsets.UTF_8)

        return stxArray + totalLenArray + combinedArray
    }



}