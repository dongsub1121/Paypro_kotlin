package kr.nicepayment.paypro.model

import com.google.gson.annotations.SerializedName

data class SalesDetailModel(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("detail") val detail: String,
    @SerializedName("result") val salesDetailData: List<SalesDetailDB>
) {
    fun getTotalPrice(): Int = salesDetailData.sumOf { it.tramt }

    fun getListSize(): Int = salesDetailData.size

    data class SalesDetailDB(
        @SerializedName("transdate") val transdate: String,
        @SerializedName("transdatelabel") val transdatelabel: String,
        @SerializedName("ficode") val ficode: String,
        @SerializedName("bankname") val bankname: String,
        @SerializedName("usncnt") val usncnt: Int,
        @SerializedName("uvnamt") val uvnamt: Int,
        @SerializedName("trcnt") val trcnt: Int,
        @SerializedName("tramt") val tramt: Int
    )
}
