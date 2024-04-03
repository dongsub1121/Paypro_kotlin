package kr.nicepayment.paypro

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.os.HandlerCompat.postDelayed
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
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
                binding.textViewStream.text = "$formattedNumber 원"
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