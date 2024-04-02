package kr.nicepayment.paypro

import android.util.Log
import kotlinx.coroutines.Dispatchers

class NicePaymentsProtocolService2 {
    private lateinit var networkClient: NicePaymentsSocketClient2
    private lateinit var builder: PacketBuilder

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

    suspend fun requestPayment(data: Any) = kotlinx.coroutines.withContext(Dispatchers.IO){
        builder = payment(data)

        val res = NicePaymentsSocketClient2().send(builder.build())
        res?.let { Log.e("NicePaymentsProtocolService2", it) }
        res
    }
}