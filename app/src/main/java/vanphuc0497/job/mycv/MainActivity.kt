package vanphuc0497.job.mycv

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.Result
import io.reactivex.Single
import kotlinx.android.synthetic.main.activity_main.*
import me.dm7.barcodescanner.zxing.ZXingScannerView
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        initEvents()
    }

    override fun onResume() {
        super.onResume()
        startCameraScanner()
    }

    override fun onStop() {
        super.onStop()
        cameraQrScanner2.stopCamera()
    }

    override fun handleResult(rawResult: Result?) {
        Log.e("xxx", rawResult?.text.toString())
        Log.e(
            "xxx",
            rawResult?.barcodeFormat.toString()
        )
        tvResultQr?.text = rawResult?.text
    }

    private fun initViews() {
        startCameraScanner()
    }

    private fun initEvents() {

    }

    private fun startCameraScanner() {
        cameraQrScanner2?.apply {
            startCamera()
            setResultHandler(this@MainActivity)
            startCamera()
        }
        Single.just("")
            .delay(3000,TimeUnit.MILLISECONDS)
            .subscribe({
                Log.e("xxx","start ne")
                cameraQrScanner2.startCamera()
            },{})
    }
}
