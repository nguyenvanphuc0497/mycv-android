package vanphuc0497.job.mycv

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.activity_main.*
import me.dm7.barcodescanner.zxing.ZXingScannerView


class MainActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {
    private val viewModel by lazy { MainViewModel(this.application) }
//    private lateinit var scannerView: ZXingScannerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        scannerView = ZXingScannerView(this)
//        setContentView(scannerView)
        setContentView(R.layout.activity_main)
        initViews()
        initEvents()
    }

    override fun onResume() {
        super.onResume()
        startCameraScanner()
//        scannerView.setResultHandler(this)
//        scannerView.startCamera()
    }

    override fun onStop() {
        super.onStop()
        cameraQrScanner2.stopCamera()
//        scannerView.stopCamera()
    }

    override fun handleResult(rawResult: Result?) {
        Log.e("xxx", rawResult?.text.toString())
        Log.e(
            "xxx",
            rawResult?.barcodeFormat.toString()
        )
    }

    private fun initViews() {
//        startCameraScanner()
//        testGenerateQRcode()
    }

    private fun initEvents() {

    }

    private fun startCameraScanner() {
        cameraQrScanner2?.apply {
            visibility = View.VISIBLE
            startCamera()
            setResultHandler(this@MainActivity)
            getFramingRectInPreview(0,0)
//            customZXingViewFinderView.onCanvasDrawSuccess =
//                ::handleCanvasDrawSuccess
        }
    }

    private fun handleCanvasDrawSuccess(frameBottom: Int) {
        val params = tvDescription?.layoutParams as ConstraintLayout.LayoutParams
        params.topMargin =
            frameBottom + 10
        tvDescription.apply {
            layoutParams = params
            visibility = View.VISIBLE
        }
    }

    private fun testGenerateQRcode() {
        imgViewQRCode.visibility = View.VISIBLE
        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap =
                barcodeEncoder.encodeBitmap(
                    "chao anh chang chao anh chang chao anh chang chao anh chang chao anh chang chao anh chang chao anh chang",
                    BarcodeFormat.QR_CODE,
                    400,
                    400
                )
            imgViewQRCode.setImageBitmap(bitmap)
        } catch (e: Exception) {
        }
    }
}
