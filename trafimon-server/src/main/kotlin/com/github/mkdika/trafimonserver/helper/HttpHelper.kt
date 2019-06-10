package com.github.mkdika.trafimonserver.helper

import com.github.mkdika.trafimonserver.model.Trafi
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class HttpHelper {

    @Value("\${google.map.actionurl}")
    lateinit var googleMapActionurl: String

    fun encodeString(inputStr: String): String {
        val encodedStr = inputStr
            .replace(" ","%20")
            .replace("\"","%22")
            .replace(",","%2C")
            .replace("<","%3C")
            .replace(">","%3E")
            .replace("#","%23")
            .replace("%","%25")
            .replace("|","%7C")
        return encodedStr
    }

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