package kr.nicepayment.paypro

import kotlin.reflect.KProperty

class ProtocolSetDelegate<T>(private val length: Int, private val padType: Pad) {
    private var value: String = ""

    operator fun getValue(thisRef: Any?, property: KProperty<*>): String = value
/*
    operator fun getValue(thisRef: Any?, property: KProperty<*>): ByteArray {
        return value.toByteArray(Charsets.UTF_8)
    }
*/

    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: T) {
        value = when (newValue) {
            is Number -> newValue.toString().padStart(length, '0')
            is String -> if (padType == Pad.ALPHA) newValue.padEnd(length, ' ') else newValue.padStart(length, '0')
            else -> newValue.toString()
        }
    }

    enum class Pad { NUMBER, ALPHA }
}