package vanphuc0497.job.mycv.data.source

import com.google.android.gms.common.api.ApiException
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vanphuc0497.job.mycv.BuildConfig
import java.lang.reflect.Type
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Create by Nguyen Van Phuc on 02/03/2020
 */

open class ApiClient private constructor(url: String? = null) {
    private var baseUrl: String =
        if (url == null || url.isEmpty()) BuildConfig.BASE_API_URL else url
    internal var isFromUnitTest = false

    companion object : SingletonHolder<ApiClient, String>(::ApiClient) {
        private const val API_TIMEOUT = 15000L// 15s
    }

    val service: ApiService
        get() {
            return createService()
        }

    private fun createService(): ApiService {
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
        val gson = GsonBuilder()
            .registerTypeAdapter(
                object : TypeToken<ApiException>() {}.type,
                null
            )
            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY) //Using key name which is defined and unchanged
            .serializeNulls()
            .create()

        val nullOnEmptyConverterFactory = object : Converter.Factory() {
            fun converterFactory() = this
            override fun responseBodyConverter(
                type: Type,
                annotations: Array<out Annotation>,
                retrofit: Retrofit
            ) = object : Converter<ResponseBody, Any?> {
                val nextResponseBodyConverter =
                    retrofit.nextResponseBodyConverter<Any?>(converterFactory(), type, annotations)

                override fun convert(value: ResponseBody) =
                    if (value.contentLength() != 0L) nextResponseBodyConverter.convert(value) else null
            }
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(nullOnEmptyConverterFactory)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
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
    fun getInstance(arg: A?, isUnitTest: Boolean = false): T {
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
                if (isUnitTest) {
                    (created as? ApiClient)?.isFromUnitTest = isUnitTest
                }
                instance = created
                created
            }
        }
    }
}
