package kr.nicepayment.paypro.screen


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import kr.nicepayment.paypro.PaymentViewModel
import kr.nicepayment.paypro.databinding.FragmentAuthorizationBinding


class AuthorizationFragment : Fragment() {

    private lateinit var viewModel: PaymentViewModel
    private lateinit var lottieAnimationView: LottieAnimationView
    private lateinit var processingMsg: TextView
    private lateinit var binding: FragmentAuthorizationBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(requireActivity())[PaymentViewModel::class.java]
        binding = FragmentAuthorizationBinding.inflate(inflater, container, false)
        processingMsg = binding.processingMsg
        lottieAnimationView = binding.lottieAnimationView
        lottieAnimationView.playAnimation()

        lifecycleScope.launchWhenStarted {
            viewModel.authorizationRequired.collect {
                if (it) {
                    Log.e("AuthorizationFragment","$it.")
                    viewModel.requestAuthorization()
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.paymentResult.collect {

                if(it.length > 1) {
                    lottieAnimationView.pauseAnimation()
                }

            }
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        lottieAnimationView.cancelAnimation()
    }
}