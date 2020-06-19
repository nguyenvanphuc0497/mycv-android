package vanphuc0497.job.mycv.ui.widget

import android.content.Context
import android.graphics.Rect
import me.dm7.barcodescanner.core.ViewFinderView

/**
 * Create by Nguyen Van Phuc on 2019-05-20
 */
class CustomZXingViewFinderView(content: Context) : ViewFinderView(content) {
    private val rectFraming = Rect()

    override fun getFramingRect(): Rect {
        val frameFinder = Rect(
            rectFraming.left,
            rectFraming.top,
            width - rectFraming.right,
            height - rectFraming.bottom
        )
        if (mSquareViewFinder) {
            frameFinder.run {
                if (height() > width()) {
                    val heightOffset = height / 2
                    val newHeightOffset = width() / 2
                    top = heightOffset - newHeightOffset
                    bottom = heightOffset + newHeightOffset
                } else {
                    val widthOffset = width / 2
                    val newWidthOffset = height() / 2
                    left = widthOffset - newWidthOffset
                    right = widthOffset + newWidthOffset
                }
            }
        }
        return frameFinder
    }

    internal fun setRectFraming(left: Int, top: Int, right: Int, bottom: Int) {
        rectFraming.set(left, top, right, bottom)
    }
}
