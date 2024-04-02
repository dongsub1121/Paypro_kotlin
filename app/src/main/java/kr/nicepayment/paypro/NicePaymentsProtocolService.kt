package kr.nicepayment.paypro

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.net.SocketTimeoutException

class NicePaymentsProtocolService {

    private lateinit var networkClient: NicePaymentsSocketClient
    private lateinit var builder: PacketBuilder

    companion object {
        private val TAG by lazy { "NicePaymentsProtocolService" }
        private const val serverIp: String = "211.48.96.28"
        private const val serverPort: Int = 11722  //11722
        private const val timeoutDuration = 30_000L
        private const val inquiryInterval = 3_000L
    }
    init {
        /*if (getEnvironment() == Environment.DEVELOP) {
            serverIp = "192.168.0.1"
            serverPort = 1002
        } else {
            serverIp = "211.48.96.28"
            serverPort = 11722
        }*/

    }

    suspend fun <T> initialRequest(data: T): NicePaymentsResult = withContext(Dispatchers.IO) {
        var responseData : NicePaymentsResult
        builder = payment(data)

        try {
            withTimeout(timeoutDuration) {
                responseData = processPaymentRequest(ProtocolState.AUTHORIZATION)
                Log.e(TAG,"1TR Result : ${responseData.result}")
                while (responseData.state == ProtocolState.INQUIRY) {
                    delay(inquiryInterval)
                    responseData = processPaymentRequest(ProtocolState.INQUIRY)
                    Log.e(TAG,"${responseData.result}")
                    Log.e(TAG,"$responseData")
                }
            }
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG, "Timeout: ${e.localizedMessage}")
            responseData = NicePaymentsResult(Result.FAIL, "9999")
        } catch (e: NetworkConnectionException) {
            Log.e(TAG, "Network Connection Error: ${e.localizedMessage}")
            responseData = NicePaymentsResult(Result.FAIL, "9999")
        } catch (e: NetworkDataException) {
            Log.e(TAG, "Data Transmission Error: ${e.localizedMessage}")
            responseData = NicePaymentsResult(Result.FAIL, "9999")
        } finally {
            safeCloseConnection()
        }

        responseData
    }

    private fun <T> payment(data: T): PacketBuilder {
        return when (data) {
            is CheckOut -> {
                PacketBuilder().authorizationBuilder(data)
            }
            is NicePaymentsResult -> {
                PacketBuilder().cancellationBuilder(data)
            }
            else -> throw IllegalArgumentException("Unsupported type")
        }
    }

    private fun parseResponseToCompleteData(response: ByteArray): NicePaymentsResult {
        Log.e(TAG,"parseResponseToCompleteData : $response")
        return builder.parseResponse(response)
    }

    private fun processPaymentRequest(state: ProtocolState): NicePaymentsResult {
        return safeNetworkConnection {
            builder.header(state)
            val data = builder.build()

            networkClient.sendProtocolData(data)
            parseResponseToCompleteData(networkClient.receiveResponse())
        }
    }

    private inline fun <T> safeNetworkConnection(action: () -> T): T {

        return try {
            networkClient.connect()
            action()
        } catch (e: NetworkConnectionException) {
            Log.e(TAG, "Connection Error: ${e.message}")
            throw e
        } catch (e: NetworkDataException) {
            Log.e(TAG, "Transmission Error: ${e.message}")
            throw e
        } catch (e: SocketTimeoutException) {
            Log.e(TAG, "Timeout Error: ${e.message}")
            throw e
        }finally {
            safeCloseConnection()
        }
    }

    private fun safeCloseConnection() {
        try {
            if(networkClient.isConnected()) networkClient.closeConnection()
        } catch (e: Exception) {
            TODO()
        }
    }

}