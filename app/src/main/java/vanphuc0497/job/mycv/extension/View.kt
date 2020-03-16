package vanphuc0497.job.mycv.extension

import android.view.View

/**
 * Create by Nguyen Van Phuc on 3/16/20
 */
fun View.showIfElseGone(isShow: Boolean) {
    when {
        isShow && visibility != View.VISIBLE -> {
            visibility = View.VISIBLE
        }
        !isShow && visibility != View.GONE -> {
            visibility = View.GONE
        }
    }
}
