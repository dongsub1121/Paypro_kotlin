package kr.nicepayment.paypro.screen.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kr.nicepayment.paypro.databinding.FragmentSalesReportBinding

class SalesReportFragment : Fragment() {

    private var _binding: FragmentSalesReportBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var stringArrayAdapter: ArrayAdapter<String>
    private var yesterday: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val salesViewModel =
            ViewModelProvider(this)[SalesViewModel::class.java]

        _binding = FragmentSalesReportBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}