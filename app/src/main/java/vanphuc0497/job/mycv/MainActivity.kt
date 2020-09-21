package vanphuc0497.job.mycv

import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import net.lingala.zip4j.ZipFile


class MainActivity : AppCompatActivity() {
    private val viewModel by lazy { MainViewModel(this.application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        initEvents()
    }

    private fun initViews() {
        edtPath.setText("/sdcard/demounzip/image1.zip")
        val handlerThread = HandlerThread("content_observer")
        handlerThread.start()
        val handler: Handler = object : Handler(handlerThread.looper) {
            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)
            }
        }
        contentResolver.unregisterContentObserver()


        contentResolver.registerContentObserver(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            true,
            object : ContentObserver(handler) {
                override fun deliverSelfNotifications(): Boolean {
                    Log.d("xxx", "deliverSelfNotifications")
                    return super.deliverSelfNotifications()
                }

                override fun onChange(selfChange: Boolean) {
                    super.onChange(selfChange)
                }

                override fun onChange(selfChange: Boolean, uri: Uri) {
                    Log.d("xxx", "onChange " + uri.toString())
                    if (uri.toString()
                            .matches(Regex(MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString() + "/[0-9]+"))
                    ) {
                        var cursor: Cursor? = null
                        try {
                            cursor = contentResolver.query(
                                uri, arrayOf(
                                    MediaStore.Images.Media.DISPLAY_NAME,
                                    MediaStore.Images.Media.DATA
                                ), null, null, null
                            )
                            if (cursor != null && cursor.moveToFirst()) {
                                val fileName: String =
                                    cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
                                val path: String =
                                    cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
                                // TODO: apply filter on the file name to ensure it's screen shot event
                                Log.d("xxx", "screen shot added $fileName $path")
                            }
                        } finally {
                            if (cursor != null) {
                                cursor.close()
                            }
                        }
                    }
                    super.onChange(selfChange, uri)
                }
            }
        )
    }

    private fun initEvents() {
        tvTest.setOnClickListener {
            val a = ZipFile(
                edtPath.text.toString().trim()
            )
            if (a.isEncrypted) {
                a.setPassword(edtPassword.text.toString().trim().toCharArray())
            }
            a.extractAll("/sdcard/demounzip/result")
        }
    }
}