package kr.nicepayment.paypro.screen.utils

import kotlin.reflect.KProperty

class ProtocolSetDelegate<T>(
    private val length: Int,
    private val padType: Pad,
    initialValue: T? = null
) {
    private var value: String = initialValue?.let { formatValue(it) } ?: defaultPadValue()

    operator fun getValue(thisRef: Any?, property: KProperty<*>): String = value

    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: T) {
        value = formatValue(newValue)
    }

    private fun formatValue(input: T): String {
        return when (padType) {
            Pad.NUMBER -> input.toString().padStart(length, '0')
            Pad.ALPHA -> input.toString().padEnd(length, ' ')
        }
    }

    private fun defaultPadValue(): String {
        return when (padType) {
            Pad.NUMBER -> "0".repeat(length)
            Pad.ALPHA -> " ".repeat(length)
        }
    }

    enum class Pad { NUMBER, ALPHA }
}