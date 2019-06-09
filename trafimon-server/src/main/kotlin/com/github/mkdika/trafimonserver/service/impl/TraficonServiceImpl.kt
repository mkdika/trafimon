package com.github.mkdika.trafimonserver.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.mkdika.trafimonserver.helper.HttpRequestHelper
import com.github.mkdika.trafimonserver.helper.TravelMode
import com.github.mkdika.trafimonserver.model.TrafficCondition
import com.github.mkdika.trafimonserver.model.gmphttp.DistanceMatrixResponse
import com.github.mkdika.trafimonserver.model.gmphttp.PlaceSearchResponse
import com.github.mkdika.trafimonserver.service.TraficonService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import kotlin.streams.toList

@Service
class TraficonServiceImpl : TraficonService, InitializingBean {

    @Value("\${google.map.apikey}")
    lateinit var googleMapApiKey: String

    @Value("\${google.map.baseurl}")
    lateinit var googleMapBaseurl: String

    @Autowired
    lateinit var customObjectMapper: ObjectMapper

    lateinit var gmpHttp: GmpHttp

    private val requestTimeout = 3000L

    private val httpLoggingLevel = Level.NONE

    override fun afterPropertiesSet() {

        gmpHttp = Retrofit.Builder()
            .baseUrl(googleMapBaseurl)
            .addConverterFactory(JacksonConverterFactory.create(customObjectMapper))
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(requestTimeout, TimeUnit.MILLISECONDS)
                    .readTimeout(requestTimeout, TimeUnit.MILLISECONDS)
                    .writeTimeout(requestTimeout, TimeUnit.MILLISECONDS)
                    .addInterceptor(NetworkFailureInterceptor())
                    .addInterceptor(createLoggingInterceptor())
                    .build())
            .build()
            .create(GmpHttp::class.java)
    }

    override fun searchPlace(keyWord: String): List<PlaceSearchResponse> {

        if (keyWord.isNullOrEmpty()) return emptyList()

        val encodedKeyWord = HttpRequestHelper.encodeString(keyWord)

        val response = gmpHttp.getSearchPlace(query = keyWord,
            key = googleMapApiKey).execute()

        return response.body().orEmpty()
    }

    override fun getTrafficCondition(origins: String,
                                     destinations: String,
                                     mode: TravelMode): TrafficCondition {
        return TrafficCondition()
    }

    private fun createLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = httpLoggingLevel
        return interceptor
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(this::class.java)
    }

    class NetworkFailureInterceptor : Interceptor {

        companion object {
            private val logger = LoggerFactory.getLogger(this::class.java)
        }

        @Throws(SocketTimeoutException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            return try {
                chain.proceed(chain.request())
            } catch (exception: SocketTimeoutException) {
                logger.error(exception.message, exception)
                throw SocketTimeoutException("Socket timeout exception when hit: ${chain.request().url()}")
            }
        }
    }

    interface GmpHttp {

        @GET("maps/api/place/textsearch/json")
        fun getSearchPlace(@Query("query") query: String,
                           @Query("key") key: String): Call<List<PlaceSearchResponse>>

        @GET("maps/api/distancematrix/json")
        fun getDistanceMatrix(@Query("origins") origins: String,
                              @Query("destinations") destinations: String,
                              @Query("travelmode") travelmode: String,
                              @Query("key") key: String): Call<DistanceMatrixResponse>
    }
}