package kr.nicepayment.paypro.model


import kr.nicepayment.paypro.Merchant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.StandardCharsets.UTF_8
import java.security.MessageDigest
import java.time.LocalDate

object SalesRepository {
    private const val BASE_URL = "https://api.jtnet.co.kr"
    private const val TOKEN = "3O421T9V6hxb5dnGSuDVd27BFRsnQYYS"

    private val api: SalesVanAPI by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SalesVanAPI::class.java)
    }

    suspend fun getMerchantInfo(biz: String, mid: String): Merchant {
        val value = "$biz$mid"
        val sha = sha256(sha256(value) + TOKEN)
        return api.getMerchant(sha, biz, mid)
    }

    suspend fun getSalesSummary(startDate: String?, endDate: String?, biz: String, mid: String): SalesModel {
        val start = startDate.takeUnless { it.isNullOrEmpty() } ?: LocalDate.now().minusDays(7).toCompactFormat()
        val end = endDate.takeUnless { it.isNullOrEmpty() } ?: LocalDate.now().minusDays(1).toCompactFormat()
        val value = "$biz$mid$start$end"
        val sha = sha256(sha256(value) + TOKEN)
        return api.getSales(sha, biz, mid, start, end)
    }

    suspend fun getSalePurchase(biz: String, mid: String, date: String?): SalesDetailModel {
        val day = date.takeUnless { it.isNullOrEmpty() } ?: LocalDate.now().minusDays(1).toCompactFormat()
        val value = "$biz$mid$day"
        val sha = sha256(sha256(value) + TOKEN)
        return api.getISalesPurchase(sha, biz, mid, day)
    }

    private fun sha256(value: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(value.toByteArray(UTF_8))
        return bytes.joinToString(separator = "") { "%02x".format(it) }
    }

    private fun LocalDate.toCompactFormat(): String = this.toString().replace("-", "")
}
