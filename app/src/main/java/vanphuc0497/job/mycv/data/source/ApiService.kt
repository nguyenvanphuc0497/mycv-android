package vanphuc0497.job.mycv.data.source

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET

/**
 * Create by Nguyen Van Phuc on 3/2/20
 */
interface ApiService {
    @GET("tintuctrongngay.rss")
    fun getHomePage24H(): Single<ResponseBody>
}
