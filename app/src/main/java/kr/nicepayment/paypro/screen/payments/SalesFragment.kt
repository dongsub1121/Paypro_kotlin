package kr.nicepayment.paypro.screen.payments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kr.nicepayment.paypro.R
import kr.nicepayment.paypro.databinding.FragmentPayBinding
import kr.nicepayment.paypro.screen.PaymentMethodFragment
import kr.nicepayment.paypro.screen.PaymentPayProActivity
import kr.nicepayment.paypro.screen.SalesFragment

class SalesFragment : Fragment() {

    private var _binding: FragmentPayBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("CommitTransaction")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[SalesViewModel::class.java]

        _binding = FragmentPayBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.imageButton.setOnClickListener {
            val intent = Intent(requireActivity(), PaymentPayProActivity::class.java)
            startActivity(intent)
        }

        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        val fragmentSales = SalesFragment()
        val fragmentMethod = PaymentMethodFragment()

        fragmentTransaction.add(R.id.containerMethod, fragmentMethod)
        fragmentTransaction.add(R.id.containerSales, fragmentSales)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}