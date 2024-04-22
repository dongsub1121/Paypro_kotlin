package kr.nicepayment.paypro.model

import kr.nicepayment.paypro.Merchant
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SalesVanAPI {
    @GET("/mpas/v1/site/info")
    suspend fun getMerchant(
        @Header("authorization") authorization: String,
        @Query("businessnumber") businessnumber: String,
        @Query("siteid") siteid: String
    ): Merchant

    @GET("/mpas/v1/trans/sum")
    suspend fun getSales(
        @Header("authorization") authorization: String,
        @Query("businessnumber") businessnumber: String,
        @Query("siteid") siteid: String,
        @Query("sdate") sdate: String,
        @Query("edate") edate: String
    ): SalesModel

    @GET("/mpas/v1/trans/sum/detail")
    suspend fun getISalesPurchase(
        @Header("authorization") authorization: String,
        @Query("businessnumber") businessnumber: String,
        @Query("siteid") siteid: String,
        @Query("transdate") transdate: String
    ): SalesDetailModel

/*    @GET("/mpas/v1/trans/list")
    suspend fun getSalesPurchaseDetail(
        @Query("businessnumber") businessnumber: String,
        @Query("siteid") siteid: String,
        @Query("transdate") transdate: String,
        @Query("page") page: Int,
        @Query("rowcount") rowcount: Int,
        @Query("token") token: String
    ): PurchaseDetail*/
}