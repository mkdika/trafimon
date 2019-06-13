package com.github.mkdika.trafimonserver.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.mkdika.trafimonserver.helper.HttpHelper
import com.github.mkdika.trafimonserver.helper.TrafiException.RecordNotFoundException
import com.github.mkdika.trafimonserver.helper.TravelMode
import com.github.mkdika.trafimonserver.model.Trafi
import com.github.mkdika.trafimonserver.model.gmphttp.DistanceMatrixResponse
import com.github.mkdika.trafimonserver.model.gmphttp.PlaceSearchResponse
import com.github.mkdika.trafimonserver.model.trafihttp.TrafiConditionResponse
import com.github.mkdika.trafimonserver.model.trafihttp.TrafiPlaceSearchResponse
import com.github.mkdika.trafimonserver.repository.TrafiRepository
import com.github.mkdika.trafimonserver.service.TrafficStatusChecker
import com.github.mkdika.trafimonserver.service.TrafiService
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
import java.util.*
import java.util.concurrent.TimeUnit

@Service
class TrafiServiceImpl : TrafiService, InitializingBean {

    @Value("\${google.map.apikey}")
    lateinit var googleMapApiKey: String

    @Value("\${google.map.baseurl}")
    lateinit var googleMapBaseurl: String

    @Autowired
    lateinit var customObjectMapper: ObjectMapper

    @Autowired
    lateinit var trafiRepository: TrafiRepository

    @Autowired
    lateinit var httpHelper: HttpHelper

    @Autowired
    lateinit var trafficStatusChecker: TrafficStatusChecker

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

    override fun searchPlace(keyWord: String): List<TrafiPlaceSearchResponse> {

        if (keyWord.isNullOrEmpty()) return emptyList()

        val response = gmpHttp.getSearchPlace(
            query = keyWord,
            key = googleMapApiKey
        ).execute()

        val searchPlaceResult = response.body()?.let {

            it.results.map { gmpPlace ->
                TrafiPlaceSearchResponse(
                    name = gmpPlace.name,
                    address = gmpPlace.formattedAddress,
                    placeId = gmpPlace.placeId,
                    lat = gmpPlace.geometry.location.lat,
                    lng = gmpPlace.geometry.location.lng
                )
            }.toList()
        }
        return searchPlaceResult.orEmpty()
    }

    @Throws(RecordNotFoundException::class)
    override fun getTrafficCondition(trafiId: String,
                                     mode: TravelMode): TrafiConditionResponse {

        val trafi = trafiRepository.findById(UUID.fromString(trafiId))
            .orElseThrow {
                RecordNotFoundException()
            }

        val response = gmpHttp.getDistanceMatrix(
            origins = "${trafi.origin.name},${trafi.origin.address}",
            destinations = "${trafi.destination.name},${trafi.destination.address}",
            depatureTime = "now",
            travelmode = mode.name.toLowerCase(),
            key = googleMapApiKey
        ).execute()

        val result = response.body()?.let {

            val distanceMatrixElement = it.rows.first().elements.first()

            val trafiConditionResponse = TrafiConditionResponse(
                trafiId = trafi.id.toString(),
                distance = distanceMatrixElement.distance.text,
                durationNormal = distanceMatrixElement.duration.text,
                durationInTraffic = distanceMatrixElement.durationInTraffic
                    .text
                    .ifEmpty { distanceMatrixElement.duration.text },
                traficStatus = trafficStatusChecker.checkTrafficStatus(distanceMatrixElement),
                gmpAction = httpHelper.generateGmpRouteAction(
                    trafi = trafi,
                    travelMode = mode
                )
            )
            trafiConditionResponse
        }
        return result
            ?: throw RecordNotFoundException("Submitted distanceMatrix not found!")
    }

    @Throws(RecordNotFoundException::class)
    override fun getTrafiByUser(userId: String): List<Trafi> {

        return trafiRepository.findByUserId(userId)
    }

    @Throws(RecordNotFoundException::class)
    override fun getTrafiById(id: String): Trafi {
        return trafiRepository.findById(UUID.fromString(id))
            .orElseThrow {
                RecordNotFoundException()
            }
    }

    @Throws(RecordNotFoundException::class)
    override fun removeTrafi(id: String) {

        val trafi = trafiRepository.findById(UUID.fromString(id))
            .orElseThrow {
                RecordNotFoundException()
            }

        trafiRepository.delete(trafi)
    }

    override fun saveOrUpdateTrafi(userId: String, trafi: Trafi): Trafi {

        val saveTrafi = trafi.copy(userId = userId)

        return trafiRepository.save(saveTrafi)
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
                           @Query("key") key: String): Call<PlaceSearchResponse>

        @GET("maps/api/distancematrix/json")
        fun getDistanceMatrix(@Query("origins") origins: String,
                              @Query("destinations") destinations: String,
                              @Query("departure_time") depatureTime: String,
                              @Query("travelmode") travelmode: String,
                              @Query("key") key: String): Call<DistanceMatrixResponse>
    }
}