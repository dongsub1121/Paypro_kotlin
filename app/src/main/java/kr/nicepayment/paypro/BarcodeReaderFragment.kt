package kr.nicepayment.paypro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import kr.nicepayment.paypro.databinding.FragmentBarcodeReaderBinding

class BarcodeReaderFragment : Fragment() {

    private lateinit var viewModel: PaymentViewModel
    private lateinit var binding: FragmentBarcodeReaderBinding
    private lateinit var barcodeView: DecoratedBarcodeView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[PaymentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity())[PaymentViewModel::class.java]
        binding = FragmentBarcodeReaderBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        barcodeView = binding.barcodeView
        barcodeView.decodeSingle { result ->

            val BARCODEType = if (result.barcodeFormat.equals("QR_CODE")) {
                BarcodeType.QR
            }else {
                BarcodeType.BARCODE
            }
            viewModel.setBarcode(result.text, BARCODEType)
        }
    }


    override fun onResume() {
        super.onResume()
        barcodeView.resume()
    }

    override fun onPause() {
        super.onPause()
        barcodeView.pause()
    }

    companion object {
        const val PERMISSION_REQUEST_CAMERA = 1
    }
}