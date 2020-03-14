package vanphuc0497.job.mycv

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val viewModel by lazy { MainActivityViewModel(this.application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvTest.setOnClickListener {
            viewModel.getHomePageFromServer().subscribe({
                Log.e("xxx", it.source().toString())
            }, {})
        }
    }
}
