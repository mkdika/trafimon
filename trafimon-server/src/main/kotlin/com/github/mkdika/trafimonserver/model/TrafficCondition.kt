package com.github.mkdika.trafimonserver.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.github.mkdika.trafimonserver.helper.TrafficCongestionStatus

data class TrafficCondition (

    @JsonProperty("distance")
    val distance: String = "",

    @JsonProperty("durationNormal")
    val durationNormal: String = "",

    @JsonProperty("durationInTraffic")
    val durationInTraffic: String = "",

    @JsonProperty("trafficStatus")
    val trafficStatus: TrafficCongestionStatus = TrafficCongestionStatus.NORMAL,

    @JsonProperty("trafficColor")
    val trafficColor: String = TrafficCongestionStatus.NORMAL.color
)

