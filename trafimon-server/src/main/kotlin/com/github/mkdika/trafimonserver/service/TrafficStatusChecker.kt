package com.github.mkdika.trafimonserver.service

import com.github.mkdika.trafimonserver.model.gmphttp.DmElement
import com.github.mkdika.trafimonserver.model.trafihttp.TrafficStatus

interface TrafficStatusChecker {

    fun checkTrafficStatus(distanceMatrixElement: DmElement): TrafficStatus
}