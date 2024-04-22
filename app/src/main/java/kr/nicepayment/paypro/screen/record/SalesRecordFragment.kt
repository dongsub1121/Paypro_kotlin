package kr.nicepayment.paypro.screen.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kr.nicepayment.paypro.databinding.FragmentSalesRecordBinding

class SalesRecordFragment : Fragment() {

    private var _binding: FragmentSalesRecordBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val salesRecordViewModel =
            ViewModelProvider(this).get(SalesRecordViewModel::class.java)

        _binding = FragmentSalesRecordBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        salesRecordViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}