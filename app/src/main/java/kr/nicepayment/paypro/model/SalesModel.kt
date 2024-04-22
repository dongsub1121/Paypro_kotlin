package kr.nicepayment.paypro.model

import com.google.gson.annotations.SerializedName

data class SalesModel(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("detail") val detail: String,
    @SerializedName("result") val saleDBList: List<SaleDB>
) {
    data class SaleDB(
        @SerializedName("transdate") val transdate: String,
        @SerializedName("transdatelabel") val transdatelabel: String,
        @SerializedName("usncnt") val usncnt: Int,
        @SerializedName("usnamt") val usnamt: Int,
        @SerializedName("uvncnt") val uvncnt: Int,
        @SerializedName("uvnamt") val uvnamt: Int,
        @SerializedName("trcnt") val trcnt: Int,
        @SerializedName("tramt") val tramt: Int
    ) {
        override fun toString(): String {
            return "SaleDB(transdate=$transdate, transdatelabel=$transdatelabel, usncnt=$usncnt, usnamt=$usnamt, uvncnt=$uvncnt, uvnamt=$uvnamt, trcnt=$trcnt, tramt=$tramt)"
        }
    }
}