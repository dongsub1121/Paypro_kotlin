package kr.nicepayment.paypro.model

import androidx.room.TypeConverter
import kr.nicepayment.paypro.BarcodeType
import kr.nicepayment.paypro.PaymentMethod
import kr.nicepayment.paypro.PaymentStatus
import kr.nicepayment.paypro.ProtocolState

class EntityConverters {
    @TypeConverter
    fun fromBarcodeType(barcodeType: BarcodeType?): String? {
        return barcodeType?.name
    }

    @TypeConverter
    fun toBarcodeType(barcodeTypeName: String?): BarcodeType? {
        return barcodeTypeName?.let { BarcodeType.valueOf(it) }
    }

    @TypeConverter
    fun fromPaymentMethod(paymentMethod: PaymentMethod?): String? {
        return paymentMethod?.name
    }

    @TypeConverter
    fun toPaymentMethod(paymentMethodName: String?): PaymentMethod? {
        return paymentMethodName?.let { PaymentMethod.valueOf(it) }
    }

    @TypeConverter
    fun fromProtocolState(protocolState: ProtocolState?): String? {
        return protocolState?.name
    }

    @TypeConverter
    fun toProtocolState(protocolStateName: String?): ProtocolState? {
        return protocolStateName?.let { ProtocolState.valueOf(it) }
    }

    @TypeConverter
    fun fromPaymentStatus(paymentStatus: PaymentStatus?): String? {
        return paymentStatus?.name
    }

    @TypeConverter
    fun toPaymentStatus(paymentStatusName: String?): PaymentStatus? {
        return paymentStatusName?.let { PaymentStatus.valueOf(it) }
    }
}