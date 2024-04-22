package kr.nicepayment.paypro.screen

import kr.nicepayment.paypro.screen.view.BarChartView
import kr.nicepayment.paypro.screen.view.BarData

class SalesFragment: AbstractContentFragment() {
    override fun setupFragment() {
        titleTextView.text = "Sales"
        descriptionTextView.text = "매출정보가 궁금하다면, 그래프를 클릭하세요,"

        val data = listOf(
            BarData(4803f, "월"),
            BarData(1815f, "화"),
            BarData(4902f, "수"),
            BarData(6400f, "목"), // 강조될 데이터
            BarData(3902f, "금"),
            BarData(2800f, "토"),
            BarData(2300f, "일")
        )

        val graph = BarChartView(requireActivity()).apply {
            setData(data = data)
            setDataUnit("만원")
        }

        setCardViewHeightInDp(300f)
        mCardView.addView(graph)

    }
}