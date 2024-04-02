package kr.nicepayment.paypro

import kotlinx.coroutines.flow.StateFlow


class CheckOut(val merchant: Merchant, private val cart: Cart, val paymentMethod: PaymentMethod,) {
    var barcode: String = ""
    var barcodeType: BarcodeType = BarcodeType.BARCODE

    fun barcode ( code: String, type: BarcodeType) {
        barcode = code
        barcodeType = type
    }

    val tax: Int
        get() = (cart.totalPrice * (merchant.taxRate / 100)).toInt()

    val amount: Int
        get() = (cart.totalPrice - tax).toInt()

    val tip: Int
        get() = merchant.tip.toInt()
}

data class Merchant( val tid: String, val taxRate: Double = 10.0, val tip: Double = 0.0)

data class Cart(val order: Order, val tip: Int? =0) {
    val totalPrice: Double
        get() = order.items.sumOf { it.price * it.quantity }
}

data class Order( val items: List<Item> )

data class Item(val name: String? =null, val price: Double, var quantity: Int) {
    fun add() = quantity++
}
