package kr.nicepayment.paypro.screen

import android.content.Intent
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import kr.nicepayment.paypro.R

class PaymentMethodFragment : AbstractContentFragment() {


    override fun setupFragment() {
        titleTextView.text = "Payment"
        descriptionTextView.text = "원하시는 결제 방법을 선택하세요."
        val inflater = LayoutInflater.from(requireContext())
        val inflatedView = inflater.inflate(R.layout.fragment_payment_method, mLayout, false)

        mLayout.addView(inflatedView)

        val cardViewPayPro = mLayout.findViewById<CardView>(R.id.cardViewPayPro)
        val cardViewLink = mLayout.findViewById<CardView>(R.id.cardViewLink)

        cardViewPayPro.setOnClickListener{
            val intent = Intent(requireActivity(), PaymentPayProActivity::class.java)
            startActivity(intent)
        }

        cardViewLink.setOnClickListener {

        }
    }



}