package com.github.mkdika.trafimonserver.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Document
data class Trafi (

    @Id
    @JsonProperty("id")
    val id: String,

    @JsonProperty("userId")
    val userId: String,

    @JsonProperty("origin")
    @field:NotNull(message = "Origin cant null")
    val origin: TrafiLocation,

    @JsonProperty("destination")
    @field:NotNull(message = "Destination cant null")
    val destination: TrafiLocation
)

data class TrafiLocation(

    @JsonProperty("name")
    @field:NotEmpty(message = "Location name cant empty")
    val name: String,

    @JsonProperty("address")
    @field:NotEmpty(message = "Location address cant empty")
    val address: String,

    @JsonProperty("placeId")
    @field:NotEmpty(message = "Location placeId cant empty")
    val placeId: String,

    @JsonProperty("lat")
    val lat: Double,

    @JsonProperty("lng")
    val lng: Double
)
