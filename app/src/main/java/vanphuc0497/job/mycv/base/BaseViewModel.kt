package vanphuc0497.job.mycv.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import vanphuc0497.job.mycv.data.source.new24h.Rss24HRepository

/**
 * Create by Nguyen Van Phuc on 3/13/20
 */
open class BaseViewModel(application: Application) : AndroidViewModel(application) {
    protected val newFeed24hRepository by lazy { Rss24HRepository() }
}
