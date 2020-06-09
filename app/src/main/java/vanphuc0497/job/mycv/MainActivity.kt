package vanphuc0497.job.mycv

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val viewModel by lazy { MainViewModel(this.application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        initEvents()
    }

    private fun initViews() {

    }

    private fun initEvents() {
        edtPin?.setOnPinFullCallBack{
            Log.e("xxx","Full Roi:$it")
        }
    }
}
