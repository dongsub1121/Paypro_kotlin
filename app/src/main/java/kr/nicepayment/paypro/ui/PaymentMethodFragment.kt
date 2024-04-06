package kr.nicepayment.paypro.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import kr.nicepayment.paypro.R

class PaymentMethodFragment : AbstractContentFragment() {


    override fun setupFragment() {
        titleTextView.text = "Payment"
        descriptionTextView.text = "원하시는 결제 방법을 선택하세요."
        val inflater = LayoutInflater.from(requireContext())

// XML 레이아웃 파일을 inflate하여 뷰 객체를 생성합니다. 여기서 R.layout.your_layout_file은 XML 레이아웃 리소스의 ID입니다.
        val inflatedView = inflater.inflate(R.layout.fragment_payment_method, mLayout, false)

// 생성된 뷰 객체를 mLayout에 추가합니다.
        mLayout.addView(inflatedView)
    }

}