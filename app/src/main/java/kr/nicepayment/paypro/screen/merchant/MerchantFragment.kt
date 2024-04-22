package kr.nicepayment.paypro.screen.merchant

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import kr.nicepayment.paypro.R
import kr.nicepayment.paypro.databinding.FragmentMerchantBinding
import kr.nicepayment.paypro.model.MerchantRepository

class MerchantFragment : Fragment() {
    private var _binding: FragmentMerchantBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MerchantViewModel by viewModels {
        MerchantViewModel.MerchantViewModelFactory(MerchantRepository.getInstance(requireContext()))
    }
    private lateinit var adapter: MerchantAdapter

    companion object {
        fun newInstance() = MerchantFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMerchantBinding.inflate(inflater, container, false)
        setupRecyclerView()

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = MerchantAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        // ViewModel로부터 데이터를 가져와서 Adapter에 설정
        viewModel.allMerchants.observe(viewLifecycleOwner) { merchants ->
            adapter.submitList(merchants)  // ListAdapter의 submitList 사용
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}