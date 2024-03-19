package kr.nicepayment.paypro

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class NicePaymentsProtocolService {
    private val serverIp = "192.168.0.1"
    private val serverPort = 1002
    private val timeoutDuration = 30_000L
    private val inquiryInterval = 3_000L
    private lateinit var networkClient: NicePaymentsSocketClient
    private lateinit var builder: ProtocolDataBuilder

    fun processPayment(data: NicePaymentArgument) = CoroutineScope(Dispatchers.IO).launch {
        withTimeout(timeoutDuration) {
            networkClient = NicePaymentsSocketClient(serverIp, serverPort)
            networkClient.connect()

            builder = ProtocolDataBuilder(
                merchant = data.merchant,
                taxRate = data.taxRate,
                amount = data.amount,
                payMethod = data.payMethod,
                status = data.status,
                state = data.state,
                barcodeType = data.barcodeType,
                barcode = data.barcode
            )

            networkClient.sendProtocolData(builder)

            var responseData = receiveResponseWithDelay(initialDelay = true)

            while (responseData.state == ProtocolState.INQUIRY) {
                delay(inquiryInterval)

                builder.setState(ProtocolState.INQUIRY)
                networkClient.sendProtocolData(builder)

                responseData = receiveResponseWithDelay(initialDelay = false)
            }

            networkClient.closeConnection()
            processFinalResult(responseData)
        }
    }

    private suspend fun receiveResponseWithDelay(initialDelay: Boolean = false): NicePaymentArgument {
        if (!initialDelay) {
            delay(inquiryInterval)
        }
        val response = networkClient.receiveResponse()
        return parseResponseToCompleteData(response)
    }

    private fun parseResponseToCompleteData(response: ByteArray): NicePaymentArgument {
        return builder.parseResponse(response)
    }

    private fun processFinalResult(data: NicePaymentArgument) {

    }
}