package vanphuc0497.job.mycv

import android.app.Application
import vanphuc0497.job.mycv.base.BaseViewModel
import vanphuc0497.job.mycv.extension.observeOnUiThread

/**
 * Create by Nguyen Van Phuc on 3/13/20
 */
class MainViewModel(application: Application) : BaseViewModel(application) {

    internal fun getHomePageFromServer() = newFeed24hRepository.getHomePage()
        .observeOnUiThread()
}
