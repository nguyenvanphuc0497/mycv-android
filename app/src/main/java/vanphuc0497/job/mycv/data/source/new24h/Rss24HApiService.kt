package vanphuc0497.job.mycv.data.source.new24h

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import vanphuc0497.job.mycv.data.model.response.base.BaseRssResponse

/**
 * Create by Nguyen Van Phuc on 3/14/20
 * 24h.com.vn không chịu trách nhiệm về các nội dung của các trang khác ngoài 24h.com.vn được dẫn trong trang này.
 * Khi sử dụng lại các tin lấy từ 24h.com.vn, bạn phải ghi rõ nguồn tin là "Theo 24h.com.vn" hoặc "24h.com.vn".
 */
interface Rss24HApiService {
    @GET("trangchu24h.rss")
    fun getHomePage24H(): Single<BaseRssResponse>

    @GET("tintuctrongngay.rss")
    fun getNewsOfTheDay24H(): Single<ResponseBody>

    @GET("bongda.rss")
    fun getFootball24H(): Single<ResponseBody>

    @GET("asiancup2019.rss")
    fun getAsianCup201924H(): Single<ResponseBody>

    @GET("anninhhinhsu.rss")
    fun getCriminalSecurity24H(): Single<ResponseBody>

    @GET("thoitrang.rss")
    fun getFashion24H(): Single<ResponseBody>

    @GET("thoitranghitech.rss")
    fun getFashionHiTech24H(): Single<ResponseBody>

    @GET("taichinhbatdongsan.rss")
    fun getFinanceRealEstate24H(): Single<ResponseBody>

    @GET("amthuc.rss")
    fun getCuisine24H(): Single<ResponseBody>

    @GET("lamdep.rss")
    fun getMakeUp24H(): Single<ResponseBody>

    @GET("phim.rss")
    fun getMovie24H(): Single<ResponseBody>

    @GET("giaoducduhoc.rss")
    fun getEducation24H(): Single<ResponseBody>

    @GET("bantrecuocsong.rss")
    fun getLife24H(): Single<ResponseBody>

    @GET("canhacmtv.rss")
    fun getMusicMTV24H(): Single<ResponseBody>

    @GET("thethao.rss")
    fun getSport24H(): Single<ResponseBody>

    @GET("phithuongkyquac.rss")
    fun getEccentric24H(): Single<ResponseBody>

    @GET("congnghethongtin.rss")
    fun getIT24H(): Single<ResponseBody>

    @GET("oto.rss")
    fun getOto24H(): Single<ResponseBody>

    @GET("thitruongtieudung.rss")
    fun getConsumerMarket24H(): Single<ResponseBody>

    @GET("dulich.rss")
    fun getTravel24H(): Single<ResponseBody>

    @GET("suckhoedoisong.rss")
    fun getHealthAndLife24H(): Single<ResponseBody>

    @GET("cuoi24h.rss")
    fun getRelaxSmile24H(): Single<ResponseBody>
}
