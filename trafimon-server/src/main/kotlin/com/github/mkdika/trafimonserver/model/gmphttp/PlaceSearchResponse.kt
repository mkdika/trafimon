package com.github.mkdika.trafimonserver.model.gmphttp

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class PlaceSearchResponse (

    @JsonProperty("results")
    val results: List<GmpPlaceAddress> = emptyList()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class GmpPlaceAddress (

    @JsonProperty("name")
    val name: String = "",

    @JsonProperty("formatted_address")
    val formattedAddress: String = "",

    @JsonProperty("place_id")
    val placeId: String = "",

    @JsonProperty("geometry")
    val geometry: GmpGeometry = GmpGeometry()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class GmpGeometry (

    @JsonProperty("location")
    val location: GmpLocation = GmpLocation()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class GmpLocation (

    @JsonProperty("lat")
    val lat: Double = 0.0,

    @JsonProperty("lng")
    val lng: Double = 0.0
)