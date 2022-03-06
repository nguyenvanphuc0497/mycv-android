package vanphuc0497.job.mycv

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import vanphuc0497.job.mycv.extension.subscribeLoadingProgressView


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
        tvTest.setOnClickListener {
//            viewModel.getHomePageFromServer()
//                .subscribeLoadingProgressView(pbHorizontal)
//                .subscribe({
//                    Log.d("xxx", "Body" + it.items.toString())
//                    Log.d("xxx", "Title" + it.channelTitle)
//                    Log.d("xxx", "description" + it.description)
//                }, {
//                    Log.e("xxx", "Error:" + it.cause)
//                })
        }
    }
}
