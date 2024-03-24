package kr.nicepayment.paypro

import java.io.InputStream
import java.io.OutputStream
import java.net.Socket

class NicePaymentsSocketClient (
    private val serverAddress: String,
    private val serverPort: Int
) {
    private lateinit var socket: Socket
    private lateinit var outputStream: OutputStream
    private lateinit var inputStream: InputStream

    fun connect() {
        try {
            socket = Socket(serverAddress, serverPort)
            outputStream = socket.getOutputStream()
            inputStream = socket.getInputStream()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun sendProtocolData(dataPacket: ByteArray) {
        try {
            outputStream.write(dataPacket)
            outputStream.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun receiveResponse(): ByteArray {
        try {
            val buffer = ByteArray(1024)
            val bytesRead = inputStream.read(buffer)
            if (bytesRead != -1) {
                return buffer.copyOfRange(0, bytesRead)
            }
        } catch (e: Exception) {
            e.printStackTrace()
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
}