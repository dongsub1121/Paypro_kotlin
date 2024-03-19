package kr.nicepayment.paypro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.flow.collect
import kr.nicepayment.paypro.databinding.ActivityPaymentBinding

class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var viewModel: PaymentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPager = binding.viewPage
        viewModel = ViewModelProvider(this)[PaymentViewModel::class.java]
        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter
        viewPager.isUserInputEnabled = false

        lifecycleScope.launchWhenStarted {
            viewModel.amount.collect {
                Log.e("amount", it.toString())
                if (it > 0) {
                    viewPager.currentItem = 1
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.barcode.collect {
                if (it.isNotEmpty()) {
                    viewPager.currentItem = 2
                }
            }
        }
    }

    private inner class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        private val fragments = arrayOf(
            AmountFragment(),
            BarcodeReaderFragment(),
            AuthorizationFragment(),
            ResultFragment()
        )

        override fun getItemCount(): Int = fragments.size
        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }
    }
}