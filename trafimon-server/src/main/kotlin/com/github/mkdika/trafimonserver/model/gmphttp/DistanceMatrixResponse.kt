package com.github.mkdika.trafimonserver.model.gmphttp

import com.fasterxml.jackson.annotation.JsonProperty

data class DistanceMatrixResponse (

    @JsonProperty("destination_addresses")
    val destinationAddresses: List<String> = emptyList(),

    @JsonProperty("origin_addresses")
    val originAddresses: List<String> = emptyList(),

    @JsonProperty("rows")
    val rows: List<DmRow> = emptyList()

)

data class DmRow (

    @JsonProperty("status")
    val status: String = "",

    @JsonProperty("elements")
    val elements: List<DmElement> = emptyList()
)

data class DmElement (

    @JsonProperty("distance")
    val distance: DmElementPoint = DmElementPoint(),

    @JsonProperty("duration")
    val duration: DmElementPoint = DmElementPoint(),

    @JsonProperty("duration_in_traffic")
    val durationInTraffic: DmElementPoint = DmElementPoint(),

    @JsonProperty("status")
    val status: String = ""
)

data class DmElementPoint (

    @JsonProperty("text")
    val text: String = "",

    @JsonProperty("value")
    val value: Int = 0
)
