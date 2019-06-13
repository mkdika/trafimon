package com.github.mkdika.trafimonserver.service

import com.github.mkdika.trafimonserver.helper.TrafiException.RecordNotFoundException
import com.github.mkdika.trafimonserver.helper.TravelMode
import com.github.mkdika.trafimonserver.model.Trafi
import com.github.mkdika.trafimonserver.model.trafihttp.TrafiConditionResponse
import com.github.mkdika.trafimonserver.model.trafihttp.TrafiPlaceSearchResponse

interface TrafiService {

    fun searchPlace(keyWord: String): List<TrafiPlaceSearchResponse>

    fun saveOrUpdateTrafi(userId: String, trafi: Trafi): Trafi

    @Throws(RecordNotFoundException::class)
    fun getTrafiByUser(userId: String): List<Trafi>

    @Throws(RecordNotFoundException::class)
    fun getTrafiById(id: String): Trafi

    @Throws(RecordNotFoundException::class)
    fun removeTrafi(id: String)

    @Throws(RecordNotFoundException::class)
    fun getTrafficCondition(trafiId: String,
                            mode: TravelMode = TravelMode.DRIVING): TrafiConditionResponse
}