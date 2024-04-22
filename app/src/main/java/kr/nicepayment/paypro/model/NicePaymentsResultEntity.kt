package kr.nicepayment.paypro.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NicePaymentsResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val resCode: String,
    val resultReason: String?,
    val barcode: String,
    val barcodeType: String?,
    val orderNumber: String,
    val authorizationDate: String,
    val authorizationNumber: String,
    val authorizationOrderNumber: String,
    val amount: Double,
    val taxRate: Double,
    val merchant: String,
    val tax: Double,
    val tip: Double,
    val pay: String,
    val state: String?,
    val status: String,
    val method: String
)

