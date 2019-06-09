package com.github.mkdika.trafimonserver.helper

enum class TravelMode {
    DRIVING,
    WALKING,
    BICYCLING
}

enum class TrafficCongestionStatus(val color: String) {
    NORMAL("info"),
    MEDIUM("warning"),
    HIGH("danger"),
    HEAVY("dark-red") // custom: #8B0000
}