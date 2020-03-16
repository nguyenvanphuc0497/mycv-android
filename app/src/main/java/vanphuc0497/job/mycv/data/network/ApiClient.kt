package vanphuc0497.job.mycv.data.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import vanphuc0497.job.mycv.BuildConfig
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Create by Nguyen Van Phuc on 02/03/2020
 */
class ApiClient private constructor(url: String? = null) {
    companion object : SingletonHolder<ApiClient, String>(::ApiClient) {
        private const val API_TIMEOUT = 15000L// 15s
    }

    private var baseUrl: String = if (url == null || url.isEmpty()) {
        BuildConfig.BASE_API_URL
    } else {
        url
    }

    internal val service: ApiService
        get() = createService(ApiService::class.java)

    internal fun <T> createService(classService: Class<T>): T {
        val httpClientBuilder = OkHttpClient.Builder()
        /*Enable log for debug mode*/
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().let {
                it.level = HttpLoggingInterceptor.Level.BODY
                httpClientBuilder.addInterceptor(it)
            }
        }
        httpClientBuilder.interceptors().add(Interceptor { chain ->
            val original = chain.request()
            // Request customization: add request headers
            val requestBuilder =
                original.newBuilder().method(original.method(), original.body()).apply {
                    addHeader("app_version", BuildConfig.VERSION_NAME)
                    addHeader("User-Agent", "Android")
                }
            chain.proceed(requestBuilder.build())
        })
        val client = httpClientBuilder
            .connectTimeout(API_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(API_TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(API_TIMEOUT, TimeUnit.MILLISECONDS)
            .protocols(Collections.singletonList(Protocol.HTTP_1_1))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
        return retrofit.create(classService)
    }
}

/**
 * Use this class to create singleton object with argument
 */
open class SingletonHolder<out T, in A>(private var creator: (A?) -> T) {
    @kotlin.jvm.Volatile
    private var instance: T? = null

    /**
     * Generate instance for T class with argument A
     */
    fun getInstance(arg: A?): T {
        val i = instance
        if (i != null) {
            return i
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator(arg)
                instance = created
                created
            }
        }
    }
}
