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
        val inflatedView = inflater.inflate(R.layout.fragment_payment_method, mLayout, false)

        mLayout.addView(inflatedView)
    }

}