package kr.nicepayment.paypro.screen

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kr.nicepayment.paypro.PaymentViewModel
import kr.nicepayment.paypro.R
import kr.nicepayment.paypro.databinding.ActivityPaymentPayProBinding
import java.text.NumberFormat
import java.util.Locale

class PaymentPayProActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentPayProBinding
    private lateinit var viewModel: PaymentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentPayProBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[PaymentViewModel::class.java]

        setupEditText()
        observeViewModel()
        observePayment()
    }

    private fun setupEditText() {
        binding.editTextAmount.apply {
            requestFocus()
            postDelayed({
                showSoftInput(this)
            }, 100)

            addTextChangedListener { text ->
                val inputNumber = text.toString().toDoubleOrNull() ?: 0.0
                val formattedNumber = NumberFormat.getNumberInstance(Locale.KOREA).format(inputNumber)
                //binding.textViewStream.text = "$formattedNumber 원"

                val fullText = "$formattedNumber 원을 결제 할께요"

                val spannable = SpannableStringBuilder(fullText)
                val start = fullText.indexOf(formattedNumber)
                val end = start + formattedNumber.length

                // 텍스트 크기를 변경 (예: 24sp)
                val textSizeSpan = RelativeSizeSpan(1.5f) // 1.5배 크기
                spannable.setSpan(textSizeSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                // 스타일 변경 (예: 굵은 글씨)
                val styleSpan = StyleSpan(Typeface.BOLD) // 굵은 글씨
                spannable.setSpan(styleSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                // 텍스트 색상 변경 (예: 파란색)
                val colorSpan = ForegroundColorSpan(Color.BLUE) // 파란색 텍스트
                spannable.setSpan(colorSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                binding.textViewStream.text = spannable
            }

            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    val amountStr = binding.textViewStream.text.toString()
                    val amount = extractNumberFromString(amountStr)
                    Log.e("amount put viewModel","$amountStr,,,$amount")
                    viewModel.setAmount(amount)

                    // AppBarLayout 축소
                    binding.appBarLayout.setExpanded(false, true)

                    // TextInputLayout과 textViewPrompt 숨기기
                    binding.inputLayoutAmount.visibility = View.GONE
                    binding.textViewPrompt.visibility = View.GONE

                    // 키보드 숨기기
                    hideSoftInputFromWindow(this.windowToken)

                    // BarcodeReaderFragment 추가
                    replaceFragmentWith(BarcodeReaderFragment())

                    true
                } else false
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.authorizationRequired.collect { isProcessing ->
                if (isProcessing) {
                    binding.textViewStream.visibility = View.GONE
                    replaceFragmentWith(AuthorizationFragment())
                }
            }
        }
    }

    private fun observePayment() {
        lifecycleScope.launch {
            viewModel.paymentResult.collect {
                if (it.length > 1) {
                    replaceFragmentWith(ResultFragment())
                }
            }
        }
    }

    private fun extractNumberFromString(formattedString: String): Double {
        val numberOnly = formattedString.replace(Regex("[^\\d]"), "")
        return numberOnly.toDoubleOrNull() ?: 0.0
    }

    private fun showSoftInput(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideSoftInputFromWindow(windowToken: IBinder) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun replaceFragmentWith(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}