package vanphuc0497.job.mycv.data.source.new24h

import vanphuc0497.job.mycv.BuildConfig
import vanphuc0497.job.mycv.base.BaseRepository
import vanphuc0497.job.mycv.data.network.ApiClient

/**
 * Create by Nguyen Van Phuc on 3/13/20
 */
class Rss24HRepository : BaseRepository() {
    private val rss24HApiService: Rss24HApiService =
        ApiClient.getInstance(BuildConfig.BASE_API_URL_24H_RSS)
            .createService(Rss24HApiService::class.java)

    fun getHomePage() = rss24HApiService.getHomePage24H()
}
