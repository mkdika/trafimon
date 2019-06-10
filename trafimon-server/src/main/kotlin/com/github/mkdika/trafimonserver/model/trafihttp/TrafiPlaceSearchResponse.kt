package com.github.mkdika.trafimonserver.model.trafihttp

import com.fasterxml.jackson.annotation.JsonProperty

data class TrafiPlaceSearchResponse (

    @JsonProperty("name")
    val name: String = "",

    @JsonProperty("address")
    val address: String = "",

    @JsonProperty("placeId")
    val placeId: String = "",

    @JsonProperty("lat")
    val lat: Double = 0.0,

    @JsonProperty("lng")
    val lng: Double = 0.0
)