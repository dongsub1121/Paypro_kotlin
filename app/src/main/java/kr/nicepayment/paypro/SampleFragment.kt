package kr.nicepayment.paypro

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import kr.nicepayment.paypro.databinding.FragmentSampleBinding
import kr.nicepayment.paypro.screen.BarcodeReaderFragment
import java.text.NumberFormat
import java.util.Locale

class SampleFragment : Fragment() {

    private lateinit var binding: FragmentSampleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSampleBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editTextAmount = binding.editTextAmount
        editTextAmount.requestFocus()

        binding.editTextAmount.apply {
            // 커서 자동 활성화 및 키보드 보이기
            requestFocus()
            postDelayed({
                val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
            }, 100)

            // 입력 텍스트를 형식화하여 TextView에 반영
            addTextChangedListener { text ->
                val inputNumber = text.toString().toDoubleOrNull() ?: 0.0
                val formattedNumber = NumberFormat.getNumberInstance(Locale.KOREA).format(inputNumber)
                binding.textViewStream.text = "$formattedNumber 원"
            }

            // 완료 버튼 동작 처리
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // AppBarLayout 축소
                    binding.appBarLayout.setExpanded(false, true)

                    // TextInputLayout과 textViewPrompt 숨기기
                    binding.inputLayoutAmount.visibility = View.GONE
                    binding.textViewPrompt.visibility = View.GONE

                    // 키보드 숨기기
                    val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(windowToken, 0)


                    val fragmentManager = childFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    val barcodeReaderFragment = BarcodeReaderFragment()
                    fragmentTransaction.replace(R.id.fragment_container, barcodeReaderFragment)
                    fragmentTransaction.commit()

                    true
                } else {
                    false
                }
            }
        }
    }

    // 숫자만 추출하는 함수
    fun extractNumberFromString(formattedString: String): Double {
        val numberOnly = formattedString.replace(Regex("\\D"), "")
        return numberOnly.toDoubleOrNull() ?: 0.0
    }
}