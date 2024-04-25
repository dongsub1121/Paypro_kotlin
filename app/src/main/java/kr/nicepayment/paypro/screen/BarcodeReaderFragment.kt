package kr.nicepayment.paypro.screen

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import kr.nicepayment.paypro.BarcodeType
import kr.nicepayment.paypro.PaymentViewModel
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
        if (checkCameraPermission()) {
            // 권한이 이미 허용된 경우
            startCamera()
        } else {
            // 권한 요청
            requestCameraPermission()
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

    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        requestPermissions(arrayOf(android.Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                Toast.makeText(context, "Camera permission is required to use the barcode scanner", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startCamera() {
        barcodeView = binding.barcodeView
        barcodeView.decodeSingle { result ->

            val barcode = result.text
            Log.e("barcode", barcode)
            val barcodeType = if (result.barcodeFormat.equals("QR_CODE")) {
                BarcodeType.QR
            } else {
                BarcodeType.BARCODE
            }

            if (barcode.isNotEmpty()) {
                viewModel.setBarcode(barcode, barcodeType)
            }
        }
    }

    companion object {
        const val PERMISSION_REQUEST_CAMERA = 1
        const val CAMERA_PERMISSION_REQUEST_CODE = 101
    }
}