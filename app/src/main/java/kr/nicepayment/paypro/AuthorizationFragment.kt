package kr.nicepayment.paypro

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView


class AuthorizationFragment : Fragment() {

    private lateinit var viewModel: PaymentViewModel
    private lateinit var lottieAnimationView: LottieAnimationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(requireActivity())[PaymentViewModel::class.java]

        val view = inflater.inflate(R.layout.fragment_authorization, container, false)
        lottieAnimationView = view.findViewById(R.id.lottieAnimationView)
        lottieAnimationView.playAnimation()
        //lottieAnimationView.pauseAnimation()

        lifecycleScope.launchWhenStarted {
            viewModel.authorizationRequired.collect {
                if (it) {
                    Log.e("AuthorizationFragment","$it.")
                    viewModel.requestAuthorization()
                }
            }
        }
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        lottieAnimationView.cancelAnimation()
    }
}