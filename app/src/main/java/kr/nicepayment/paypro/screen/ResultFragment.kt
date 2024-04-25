package kr.nicepayment.paypro.screen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import kr.nicepayment.paypro.PaymentViewModel
import kr.nicepayment.paypro.R
import kr.nicepayment.paypro.databinding.FragmentResultBinding
import java.text.NumberFormat
import java.util.Locale

class ResultFragment : Fragment() {

    private lateinit var binding: FragmentResultBinding
    private lateinit var lottieAnimationView: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val viewModel = ViewModelProvider(requireActivity())[PaymentViewModel::class.java]
        binding = FragmentResultBinding.inflate(inflater, container, false)

        lottieAnimationView = binding.lottieAnimationViewSuccess
        lottieAnimationView.playAnimation()

        binding.textViewAmount.text =formatToCurrency("40000")
        binding.textViewDateTimeValue.text = "2024년 04월 26일 17시 40분 30초"
        binding.textViewAuthNumValue.text = "405265851000000001897654"

        lifecycleScope.launchWhenStarted {
            viewModel.paymentResult.collect {

            }
        }

        return binding.root
    }

    fun formatToCurrency(numberString: String): String {
        // 문자열을 숫자로 변환 (입력값이 유효한 숫자인지 확인 필요)
        val number = numberString.toLongOrNull() ?: return "Invalid Number"

        // 통화 포맷터 생성, 한국 원(KRW) 형식으로 설정
        val formatter = NumberFormat.getCurrencyInstance(Locale.KOREA)
        formatter.maximumFractionDigits = 0 // 소수점 없음

        // 숫자를 통화 형식으로 포맷
        return formatter.format(number)+" 원"
    }
}