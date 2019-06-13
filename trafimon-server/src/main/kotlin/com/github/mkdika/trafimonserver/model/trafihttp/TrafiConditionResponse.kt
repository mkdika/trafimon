package com.github.mkdika.trafimonserver.model.trafihttp

import com.fasterxml.jackson.annotation.JsonProperty
import com.github.mkdika.trafimonserver.helper.TrafficCongestionStatus
import java.util.*

data class TrafiConditionResponse(

    @JsonProperty("trafiId")
    val trafiId: String = "",

    @JsonProperty("distance")
    val distance: String = "",

    @JsonProperty("durationNormal")
    val durationNormal: String = "",

    @JsonProperty("durationInTraffic")
    val durationInTraffic: String = "",

    @JsonProperty("traficStatus")
    val traficStatus: TrafficStatus,

    @JsonProperty("gmpAction")
    val gmpAction: String = "",

    @JsonProperty("updatedAt")
    val updatedAt: Long = Date().time
)

data class TrafficStatus(

    @JsonProperty("status")
    val status: TrafficCongestionStatus = TrafficCongestionStatus.NORMAL,

    @JsonProperty("color")
    val color: String = TrafficCongestionStatus.NORMAL.color
)

