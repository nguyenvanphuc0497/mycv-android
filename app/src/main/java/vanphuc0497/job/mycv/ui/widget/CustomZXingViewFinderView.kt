package vanphuc0497.job.mycv.ui.widget

import android.content.Context
import android.graphics.Rect
import me.dm7.barcodescanner.core.ViewFinderView

/**
 * Create by Nguyen Van Phuc on 2019-05-20
 */
class CustomZXingViewFinderView(content: Context) : ViewFinderView(content) {
    private val rectFraming = Rect()

    override fun getFramingRect(): Rect = Rect(
        rectFraming.left,
        rectFraming.top,
        width - rectFraming.right,
        height - rectFraming.bottom
    ).apply {
        if (true) {
            if (height() < width()) {
                val offsetCenter = (width - rectFraming.right) / 2
                right = bottom
                left += offsetCenter
                right += offsetCenter
            } else {
                val offsetCenter = (height - rectFraming.bottom) / 2
                bottom = right
                top += offsetCenter
                bottom += offsetCenter
            }
        }
    }

    internal fun setRectFraming(left: Int, top: Int, right: Int, bottom: Int) {
        rectFraming.set(left, top, right, bottom)
    }
}
