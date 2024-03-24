package kr.nicepayment.paypro

import android.os.Build.VERSION_CODES.P
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class NicePaymentsProtocolService(checkOut: CheckOut) {
    private val serverIp = "192.168.0.1"
    private val serverPort = 1002
    private val timeoutDuration = 30_000L
    private val inquiryInterval = 3_000L
    private lateinit var networkClient: NicePaymentsSocketClient
    private val builder: ProtocolDataBuilder

    init {
        builder = ProtocolDataBuilder(
            merchant = checkOut.merchant.tid,
            amount = checkOut.amount,
            tax = checkOut.tax,
            tip = checkOut.tip,
            payMethod = checkOut.paymentMethod,
            barcodeType = checkOut.barcodeType,
            barcode = checkOut.barcode
        ).apply { setState(ProtocolState.AUTHORIZATION) }

        processPayment()
    }

    private fun processPayment() = CoroutineScope(Dispatchers.IO).launch {
        withTimeout(timeoutDuration) {
            networkClient = NicePaymentsSocketClient(serverIp, serverPort)
            networkClient.connect()
            networkClient.sendProtocolData(builder.build())

            var responseData = receiveResponseWithDelay(initialDelay = true)

            while (responseData.state == ProtocolState.INQUIRY) {
                delay(inquiryInterval)

                builder.setState(ProtocolState.INQUIRY)
                networkClient.sendProtocolData(builder.build())

                responseData = receiveResponseWithDelay(initialDelay = false)
            }

            networkClient.closeConnection()
            processFinalResult(responseData)
        }
    }

    private suspend fun receiveResponseWithDelay(initialDelay: Boolean = false): NicePaymentsResult {
        if (!initialDelay) {
            delay(inquiryInterval)
        }
        val response = networkClient.receiveResponse()
        return parseResponseToCompleteData(response)
    }

    private fun parseResponseToCompleteData(response: ByteArray): NicePaymentsResult {
        return builder.parseResponse(response)
    }

    private fun processFinalResult(data: NicePaymentsResult) {
        TODO("")
    }
}