package kr.nicepayment.paypro

import android.util.Log
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.PrintWriter
import java.net.Socket

class NicePaymentsSocketClient2 {
    private var socket: Socket = Socket(serverIp, serverPort)
    private var outputStream: OutputStream = socket.getOutputStream()
    private var inputStream: InputStream = socket.getInputStream()

    companion object {
        private val TAG by lazy { "NicePaymentsProtocolService" }
        private const val serverIp: String = "211.48.96.28"
        private const val serverPort: Int = 11722  //11722
    }

    fun send(array: ByteArray) : String? {
        var socket = Socket(serverIp, serverPort)

        val input = BufferedReader(InputStreamReader(socket.getInputStream()))
        val output = PrintWriter(socket.getOutputStream(), true)

        println("서버에 메시지 전송: $array")
        output.println(array)

        val receivedMessage = input.readLine()
        Log.e("서버로부터 받은 메시지:" , receivedMessage)

        socket.close()

        return receivedMessage
    }
}