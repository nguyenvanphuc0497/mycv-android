package vanphuc0497.job.mycv.ui.widget

import android.content.Context
import android.util.AttributeSet
import me.dm7.barcodescanner.core.IViewFinder
import me.dm7.barcodescanner.zxing.ZXingScannerView
import vanphuc0497.job.mycv.R

/**
 * Create by Nguyen Van Phuc on 2019-05-20
 */
class CustomZXingScannerView : ZXingScannerView {
    private var customZXingViewFinderView: CustomZXingViewFinderView? = null
    private var isCameraStarted = false

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        context.obtainStyledAttributes(
            attrs,
            R.styleable.CustomZXingScannerView
        ).apply {
            var paddingLeftFinder: Int
            var paddingRightFinder: Int
            getDimension(
                R.styleable.CustomZXingScannerView_paddingHorizontalFinder,
                0F
            ).toInt().also {
                if (it > 0) {
                    paddingLeftFinder = it
                    paddingRightFinder = it
                } else {
                    paddingLeftFinder = getDimension(
                        R.styleable.CustomZXingScannerView_paddingLeftFinder,
                        0F
                    ).toInt()
                    paddingRightFinder = getDimension(
                        R.styleable.CustomZXingScannerView_paddingRightFinder,
                        0F
                    ).toInt()
                }
            }
            customZXingViewFinderView?.apply {
                setRectFraming(
                    paddingLeftFinder,
                    getDimension(R.styleable.CustomZXingScannerView_paddingTopFinder, 0F).toInt(),
                    paddingRightFinder,
                    getDimension(R.styleable.CustomZXingScannerView_paddingBottomFinder, 0F).toInt()
                )
                setMaskColor(getColor(R.styleable.CustomZXingScannerView_maskColor, 0))
                setBorderLineLength(
                    getDimension(
                        R.styleable.CustomZXingScannerView_borderLength,
                        0F
                    ).toInt()
                )
                setBorderStrokeWidth(
                    getDimension(
                        R.styleable.CustomZXingScannerView_borderWidth,
                        0F
                    ).toInt()
                )
                setBorderColor(getColor(R.styleable.CustomZXingScannerView_borderColor, 0))
            }
        }.recycle()
    }

    override fun createViewFinderView(context: Context): IViewFinder {
        customZXingViewFinderView = CustomZXingViewFinderView(context)
        return customZXingViewFinderView ?: CustomZXingViewFinderView(context)
    }

    override fun startCamera() {
        if (!isCameraStarted) {
            super.startCamera()
            isCameraStarted = true
        }
    }

    override fun stopCamera() {
        super.stopCamera()
        isCameraStarted = false
    }
}
