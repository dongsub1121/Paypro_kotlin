package kr.nicepayment.paypro.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class MerchantEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sitename: String,
    val businessNo: String,
    val merchantNo: String,
    val siteaddress: String,
    val master: Boolean
)
