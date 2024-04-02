package kr.nicepayment.paypro

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kr.nicepayment.paypro.databinding.FragmentAmountBinding

class AmountFragment : Fragment() {

    private lateinit var viewModel: PaymentViewModel
    private lateinit var binding: FragmentAmountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding = FragmentAmountBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[PaymentViewModel::class.java]

        binding.motionButton.setOnClickListener{
            val amount =  binding.editTextAmount.text.toString()

            if (amount.isNotEmpty()){
                viewModel.setAmount(amount.toDouble())
            } else Toast.makeText(requireActivity(),"금액입력", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }
}