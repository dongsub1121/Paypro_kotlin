package kr.nicepayment.paypro

class Utils {

    //USD 결제시 두 자리수 올림
    fun amount(number: Number) {
        val result = if (number.toDouble() % 1.0 != 0.0) {
            // 소수점 이하가 있는 경우
            (number.toDouble() * 100).toInt()
        } else {
            // 소수점 이하가 없는 경우
            number.toInt() * 100
        }

        println("Converted amount: $result")
    }

}