package kr.nicepayment.paypro

import com.google.zxing.client.android.BuildConfig

enum class PaymentMethod  {
    PAYPRO,
    LINK,
    ALIPAY,
    WECHAT,
    LINE,
    LIQUID,
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

enum class Result {
    SUCCESS,
    FAIL,
    NETWORK_ERROR,
    DATA_ERROR,
    PROCESSING,
    TIMEOUT
}

enum class Environment {
    DEVELOP,
    PRODUCTION
}
