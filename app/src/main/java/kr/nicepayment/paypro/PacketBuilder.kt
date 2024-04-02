package kr.nicepayment.paypro

import android.util.Log
import kr.nicepayment.paypro.ProtocolSetDelegate.Pad.*


class PacketBuilder {
    private var stx: Byte = 0x02
    private var totalLen: String by ProtocolSetDelegate(4, NUMBER)
    private var identity: Int = 0x1212
    private var specVersion: String by ProtocolSetDelegate(3, ALPHA,  "PFC")
    private var dllVersion: String by ProtocolSetDelegate(8, ALPHA, "1.01.001")
    private var cancelTransaction: String by ProtocolSetDelegate(1, ALPHA)
    private var filler: String by ProtocolSetDelegate(5, ALPHA)
    private lateinit var header: NicePaymentHeader
    private lateinit var body: NicePaymentBaseBody
    private var etx: Byte = 0x03

    private fun getBody(method: PaymentMethod): NicePaymentBaseBody {
        return when(method) {
            PaymentMethod.PAYPRO -> NicePaymentsPayProBody()
            else -> NicePaymentsPayProBody()
        }
    }

    fun header(newState: ProtocolState): PacketBuilder {
        val code = when(newState) {
            ProtocolState.AUTHORIZATION -> body.codeOfAuthorization
            ProtocolState.INQUIRY -> body.codeOfInquiry
            ProtocolState.CANCEL -> body.condOfCancel
            ProtocolState.NET_CANCEL -> body.codeOfNetCancel
        }
        header.jobCode(code)
        return this
    }

    fun <T> buildPayment(data: T): PacketBuilder {
        return when (data) {
            is CheckOut -> {
                authorizationBuilder(data)
            }
            is NicePaymentsResult -> {
                cancellationBuilder(data)
            }
            else -> throw IllegalArgumentException("Unsupported type")
        }
    }

    fun authorizationBuilder(checkOut: CheckOut): PacketBuilder{
        body = getBody(checkOut.paymentMethod).apply {
            amount(checkOut.amount)
            tax(checkOut.tax)
            tip(checkOut.tip)
            orderNumber(null)
            barcode(checkOut.barcode)
            barcodeType(checkOut.barcodeType)
        }
        header = NicePaymentHeader().apply {
            jobCode(body.condOfCancel)
            merchant(checkOut.merchant.tid)
        }
        return this
    }

    fun cancellationBuilder(nicePaymentsResult: NicePaymentsResult): PacketBuilder {
        body = getBody(nicePaymentsResult.method!!).apply {
            amount(nicePaymentsResult.amount)
            tax(nicePaymentsResult.tax)
            tip(nicePaymentsResult.tip)
            orderNumber(nicePaymentsResult.orderNumber)
            authorizationDate(nicePaymentsResult.authDate)
            authorizationNumber(nicePaymentsResult.authOrderNumber)
            barcode(nicePaymentsResult.barcode)
            barcodeType(nicePaymentsResult.barcodeType)
        }
        header = NicePaymentHeader().apply {
            jobCode(body.condOfCancel)
            merchant(nicePaymentsResult.merchant)
        }
        return this
    }

    fun parseResponse(response: ByteArray): NicePaymentsResult {
        return body.parseAuthorizationResponse(response)
    }


    fun build(): ByteArray {

        val stxArray = byteArrayOf(stx)
        val identityArray = identity.toString().toByteArray(Charsets.UTF_8)
        val specVersionArray = specVersion.toByteArray(Charsets.UTF_8)
        val dllVersionArray = dllVersion.toByteArray(Charsets.UTF_8)
        val cancelTransactionArray = cancelTransaction.toByteArray(Charsets.UTF_8)
        val fillerArray = filler.toByteArray(Charsets.UTF_8)
        val headerArray = header.data.toByteArray(Charsets.UTF_8)
        val bodyArray = body.authorization().toByteArray(Charsets.UTF_8)
        val etxArray = byteArrayOf(etx)

        val combinedArray = identityArray + specVersionArray + dllVersionArray +
                cancelTransactionArray + fillerArray + headerArray + bodyArray + etxArray

        totalLen = (combinedArray.size + stxArray.size + totalLen.length).toString()
        val totalLenArray = totalLen.toByteArray(Charsets.UTF_8)

        val stxArrayString = stxArray.joinToString(separator = " ") { byte -> "%02x".format(byte) }
        val totalLenArrayString = totalLenArray.joinToString(separator = " ") { byte -> "%02x".format(byte) }
        val combinedArrayString = combinedArray.joinToString(separator = " ") { byte -> "%02x".format(byte) }

        Log.e("packetBuild","totalLen:$ $totalLen \n header:$header,\nbody: $body")

        return stxArray + totalLenArray + combinedArray
    }



}