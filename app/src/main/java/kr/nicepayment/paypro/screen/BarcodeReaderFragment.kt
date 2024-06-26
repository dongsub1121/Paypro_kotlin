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
import com.google.android.material.snackbar.Snackbar
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

            // 현재 사용 중인 Alipay+ 결제 코드 (24자리)
            val regexCurrent = "^[0-9]{24}$"
            // Alipay+ 레거시 결제 코드
            val regexLegacy = "^(28[0-9]{15}|28[0-9]{16}|28[0-8][0-9]{16}|289[0-9]{10}[0-57-9][0-9]{5}|289[0-9]{10}6[0-9]{5})$"

            // 두 정규식 중 하나에 매치되는 경우
            if (barcode.isNotEmpty() && (barcode.matches(regexCurrent.toRegex()) || barcode.matches(regexLegacy.toRegex()))) {
                viewModel.setBarcode(barcode, barcodeType)
            } else {
                Snackbar.make(binding.root, "Invalid Alipay+ Payment Code", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        const val PERMISSION_REQUEST_CAMERA = 1
        const val CAMERA_PERMISSION_REQUEST_CODE = 101
    }
}