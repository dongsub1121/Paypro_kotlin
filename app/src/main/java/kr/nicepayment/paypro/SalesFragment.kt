package kr.nicepayment.paypro

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kr.nicepayment.paypro.databinding.FragmentSalesBinding


class SalesFragment : Fragment() {

    private lateinit var _binding: FragmentSalesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSalesBinding.inflate(inflater, container, false)
        return _binding.root

    }


}







