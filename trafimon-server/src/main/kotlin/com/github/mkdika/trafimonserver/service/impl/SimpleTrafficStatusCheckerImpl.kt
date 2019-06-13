package com.github.mkdika.trafimonserver.service.impl

import com.github.mkdika.trafimonserver.helper.TrafficCongestionStatus
import com.github.mkdika.trafimonserver.model.gmphttp.DmElement
import com.github.mkdika.trafimonserver.model.trafihttp.TrafficStatus
import com.github.mkdika.trafimonserver.service.TrafficStatusChecker
import org.springframework.stereotype.Service

@Service
class SimpleTrafficStatusCheckerImpl : TrafficStatusChecker {

    override fun checkTrafficStatus(distanceMatrixElement: DmElement): TrafficStatus {

        val diffTime = distanceMatrixElement.durationInTraffic.value - distanceMatrixElement.duration.value
        return when {
            diffTime <= 5 -> TrafficStatus(
                status = TrafficCongestionStatus.NORMAL,
                color = TrafficCongestionStatus.NORMAL.color
            )
            diffTime in 6..12 -> TrafficStatus(
                status = TrafficCongestionStatus.MEDIUM,
                color = TrafficCongestionStatus.MEDIUM.color
            )
            diffTime in 13..20 -> TrafficStatus(
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