package kr.nicepayment.paypro

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket
import java.net.SocketException
import java.net.SocketTimeoutException

class NicePaymentsSocketClient  {
    private var socket: Socket
    private var outputStream: OutputStream
    private var inputStream: InputStream

    companion object {
        private val TAG by lazy { "NicePaymentsProtocolService" }
        private const val serverIp: String = "211.48.96.28"
        private const val serverPort: Int = 11722  //11722
    }

    init {
        socket = Socket(serverIp, serverPort)
        outputStream = socket.getOutputStream()
        inputStream = socket.getInputStream()
    }

    fun connect() {
        try {
/*            socket = Socket(serverAddress, serverPort)
            outputStream = socket.getOutputStream()
            inputStream = socket.getInputStream()*/
        } catch (e: SocketException) {
            Log.e(TAG, "SocketException: ${e.localizedMessage}")
            throw NetworkConnectionException("SocketException", e)
        } catch (e: IOException) {
            Log.e(TAG, "IOException: ${e.localizedMessage}")
            throw NetworkConnectionException("IOException", e)
        }
    }

    fun sendProtocolData(dataPacket: ByteArray) {
        try {
            outputStream.write(dataPacket)
            outputStream.flush()
        } catch (e: IOException) {
            Log.e(TAG, "DataIOException: ${e.localizedMessage}")
            throw NetworkDataException("DataIOException", e)
        }
    }

/*    suspend fun receiveResponse(): ByteArray = withContext(Dispatchers.IO){
        val buffer = ByteArray(1024)
        val bytesRead = inputStream?.read(buffer) ?: -1
        if (bytesRead != -1) {
            buffer.copyOfRange(0, bytesRead)
        } else {
            ByteArray(0)
        }
    }*/

    fun receiveResponse(timeoutMillis: Long = 7000): ByteArray {
        val buffer = ByteArray(2048)
        val startTime = System.currentTimeMillis()

        try {
            /*while (inputStream.available() == 0) {
                if (System.currentTimeMillis() - startTime > timeoutMillis) {
                    throw SocketTimeoutException("Response timed out")
                }
                Thread.sleep(100)  // 더 짧은 sleep 시간을 사용하여 더 자주 확인
            }*/
            val bytesRead = inputStream.read(buffer)
            Log.e("receiveResponse",bytesRead.toString())
            if (bytesRead != -1) {
                return buffer.copyOfRange(0, bytesRead)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Receive Error: ${e.localizedMessage}")
            throw NetworkDataException("Receive Error", e)
        }
        return ByteArray(0)
    }

    fun closeConnection() {
        try {
            inputStream.close()
            outputStream.close()
            socket.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun isConnected(): Boolean {
        return socket.isConnected
    }
}