package kr.nicepayment.paypro

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

abstract class NicePaymentsBaseFragment<T : ViewModel> : Fragment() {
    protected lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[getViewModelClass()]
    }

    abstract fun getViewModelClass() : Class<T>
}