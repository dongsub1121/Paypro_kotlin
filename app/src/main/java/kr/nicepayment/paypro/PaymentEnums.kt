package kr.nicepayment.paypro

enum class PaymentMethod {
    PAYPRO,
    LINK,
    OTHER
}
enum class BarcodeType {
    QR,
    BARCODE
}

enum class PaymentStatus {
    AUTHORIZATION,
    READY,
    CANCEL,
    REFUNDED
}

enum class ProtocolState {
    AUTHORIZATION,
    INQUIRY,
    CANCEL,
    NET_CANCEL
}