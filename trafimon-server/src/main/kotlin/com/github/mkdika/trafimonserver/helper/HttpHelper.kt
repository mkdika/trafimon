package com.github.mkdika.trafimonserver.helper

import com.github.mkdika.trafimonserver.model.Trafi
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class HttpHelper {

    @Value("\${google.map.actionurl}")
    lateinit var googleMapActionurl: String

    fun generateGmpRouteAction(trafi: Trafi,
                               travelMode: TravelMode): String {
        return "${googleMapActionurl}maps/dir/?api=1" +
            "&origin=${trafi.origin.name.toLowerCase().replace(" ","+")}" +
            "&origin_place_id=${trafi.origin.placeId}" +
            "&destination=${trafi.destination.name.toLowerCase().replace(" ", "+")}" +
            "&destination_place_id=${trafi.destination.placeId}" +
            "&travelmode=${travelMode.name.toLowerCase()}"
    }
}