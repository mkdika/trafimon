package com.github.mkdika.trafimonserver.service.impl

import com.github.mkdika.trafimonserver.helper.TrafficCongestionStatus
import com.github.mkdika.trafimonserver.model.gmphttp.DmElement
import com.github.mkdika.trafimonserver.model.trafihttp.TrafficStatus
import com.github.mkdika.trafimonserver.service.TrafficStatusChecker
import org.springframework.stereotype.Service

@Service
class SimpleTrafficStatusCheckerImpl : TrafficStatusChecker {

    private val normalTrafficDiff = 300
    private val mediumTrafficDiff = 301..720
    private val highTrafficDiff = 721..1200

    override fun checkTrafficStatus(distanceMatrixElement: DmElement): TrafficStatus {

        val diffTime = distanceMatrixElement.durationInTraffic.value - distanceMatrixElement.duration.value
        return when {
            diffTime <= normalTrafficDiff -> TrafficStatus(
                status = TrafficCongestionStatus.NORMAL,
                color = TrafficCongestionStatus.NORMAL.color
            )
            diffTime in mediumTrafficDiff -> TrafficStatus(
                status = TrafficCongestionStatus.MEDIUM,
                color = TrafficCongestionStatus.MEDIUM.color
            )
            diffTime in highTrafficDiff -> TrafficStatus(
                status = TrafficCongestionStatus.HIGH,
                color = TrafficCongestionStatus.HIGH.color
            )
            else -> TrafficStatus(
                status = TrafficCongestionStatus.HEAVY,
                color = TrafficCongestionStatus.HEAVY.color
            )
        }
    }
}