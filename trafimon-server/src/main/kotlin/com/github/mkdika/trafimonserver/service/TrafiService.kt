package com.github.mkdika.trafimonserver.service

import com.github.mkdika.trafimonserver.helper.TravelMode
import com.github.mkdika.trafimonserver.model.Trafi
import com.github.mkdika.trafimonserver.model.trafihttp.TrafiConditionResponse
import com.github.mkdika.trafimonserver.model.trafihttp.TrafiPlaceSearchResponse
import java.util.*

interface TrafiService {

    fun searchPlace(keyWord: String): List<TrafiPlaceSearchResponse>

    fun getTrafiByUser(userId: String): List<Trafi>

    fun getTrafiById(id: String): Optional<Trafi>

    fun saveOrUpdateTrafi(userId: String, trafi: Trafi): Trafi

    fun removeTrafi(trafi: Trafi)

    fun getTrafficCondition(trafiId: String,
                            mode: TravelMode = TravelMode.DRIVING): TrafiConditionResponse
}