package kr.nicepayment.paypro.ui

import android.widget.ExpandableListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.nicepayment.paypro.NicePaymentsResult
import kr.nicepayment.paypro.PaymentHistoryAdapter
import kr.nicepayment.paypro.PaymentMethod
import kr.nicepayment.paypro.PaymentStatus
import kr.nicepayment.paypro.R
import kr.nicepayment.paypro.Result

class PaymentHistoryFragment : AbstractContentFragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PaymentHistoryAdapter
    override fun setupFragment() {
        titleTextView.text = "판매 내역"
        descriptionTextView.text = " 자세히 보기 : 여기서 취소 할 수 있어요."
        adapter = PaymentHistoryAdapter(requireActivity())

        recyclerView = RecyclerView(requireActivity()).apply {
            layoutManager = LinearLayoutManager(requireActivity()).apply {  }
            adapter = this@PaymentHistoryFragment.adapter
        }

        var nicePaymentsResult1 = NicePaymentsResult(Result.SUCCESS,"0000","23").apply {
            authNum = "0090 8487"
            amount = 90000.0
            authDate = "2024년 04월 9일 21시34분19초"
            status = PaymentStatus.AUTHORIZATION
            method = PaymentMethod.ALIPAY
            pay = "알리페이"
        }
        var nicePaymentsResult2 = NicePaymentsResult(Result.FAIL,"0000","23").apply {
            authNum = ""
            amount = 70000.0
            authDate = "19시간 34분 20초 남음"
            status = PaymentStatus.READY
            pay = "링크프로"

        }
        var nicePaymentsResult3 = NicePaymentsResult(Result.FAIL,"0000","23").apply {
            authNum = "7756 8901"
            amount = 30000.0
            authDate = "19시간 34분 20초 남음"
            status = PaymentStatus.REFUNDED
            method = PaymentMethod.LINE
            pay = "라인페이"

        }

        val items = listOf(nicePaymentsResult1, nicePaymentsResult3, nicePaymentsResult2)
        adapter.setItems(items)

        setCardViewHeightInDp(130f)
        mCardView.addView(recyclerView)
    }

    private fun loadData() {
        var nicePaymentsResult1 = NicePaymentsResult(Result.SUCCESS,"0000","23").apply {
            authNum = "0090 8487"
            amount = 90000.0
            authDate = "2024년 04월 9일 21시34분19초"
            status = PaymentStatus.READY
            method = PaymentMethod.ALIPAY
        }
        var nicePaymentsResult2 = NicePaymentsResult(Result.FAIL,"0000","23").apply {
            authNum = "0090 8487"
            amount = 90000.0
            authDate = "2024년 04월 9일 21시34분19초"
            status = PaymentStatus.AUTHORIZATION
            method = PaymentMethod.ALIPAY
        }

        val items = listOf(nicePaymentsResult1, nicePaymentsResult2)
        adapter.setItems(items)
    }
}