package vanphuc0497.job.mycv.ui.widget

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.res.ResourcesCompat
import me.dm7.barcodescanner.core.IViewFinder
import me.dm7.barcodescanner.zxing.ZXingScannerView
import vanphuc0497.job.mycv.R

/**
 * Create by Nguyen Van Phuc on 2019-05-20
 */
class CustomZXingScannerView(context: Context, attributes: AttributeSet) :
    ZXingScannerView(context, attributes) {
    internal lateinit var customZXingViewFinderView: CustomZXingViewFinderView

    override fun createViewFinderView(context: Context): IViewFinder {
        customZXingViewFinderView = CustomZXingViewFinderView(context).apply {
            setBorderLineLength(100)
            setBorderColor(ResourcesCompat.getColor(resources, R.color.colorWhite, null))
            setSquareViewFinder(true)
            setMaskColor(ResourcesCompat.getColor(resources, R.color.colorBlackOpacity50, null))
        }
        return customZXingViewFinderView
    }
}
