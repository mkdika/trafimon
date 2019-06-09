package com.github.mkdika.trafimonserver.service

import com.github.mkdika.trafimonserver.helper.TravelMode
import com.github.mkdika.trafimonserver.model.TrafficCondition
import com.github.mkdika.trafimonserver.model.gmphttp.PlaceSearchResponse

interface TraficonService {

    fun searchPlace(keyWord: String): List<PlaceSearchResponse>

    fun getTrafficCondition(origins: String,
                            destinations: String,
                            mode: TravelMode = TravelMode.DRIVING): TrafficCondition
}