package vanphuc0497.job.mycv.data.source.new24h

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET

/**
 * Create by Nguyen Van Phuc on 3/14/20
 */
interface Rss24HApiService {
    @GET("tintuctrongngay.rss")
    fun getHomePage24H(): Single<ResponseBody>
}
