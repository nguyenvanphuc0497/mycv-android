package vanphuc0497.job.mycv.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import me.dm7.barcodescanner.core.ViewFinderView

/**
 * Create by Nguyen Van Phuc on 2019-05-20
 */
class CustomZXingViewFinderView(content: Context) : ViewFinderView(content) {
    internal var onCanvasDrawSuccess: (frameBottom: Int) -> Unit = {}

    override fun drawViewFinderMask(canvas: Canvas?) {
        super.drawViewFinderMask(canvas)
        onCanvasDrawSuccess(framingRect.bottom)
    }

    override fun getFramingRect(): Rect = Rect(super.getFramingRect()).apply {
        top -= 300
        bottom += 200
        left -= 100
        right += 100
    }
}
