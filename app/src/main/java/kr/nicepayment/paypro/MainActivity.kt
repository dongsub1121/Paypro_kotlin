package kr.nicepayment.paypro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import kr.nicepayment.paypro.databinding.ActivityMainBinding
import kr.nicepayment.paypro.ui.PaymentHistoryFragment
import kr.nicepayment.paypro.ui.PaymentMethodFragment
import kr.nicepayment.paypro.ui.SalesFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) { // 화면 회전 등으로 인한 재생성을 방지
            supportFragmentManager.commit {

                add(R.id.sales_container, SalesFragment())
                add(R.id.pay_method_container, PaymentMethodFragment())
                add(R.id.pay_ledger_container, PaymentHistoryFragment())
            }
        }
    }
}