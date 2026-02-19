package com.bluecolab.watermonitor.domain.model

enum class FloodStatus {
    NORMAL,
    ELEVATED,
    FLOODING,
    UNKNOWN
}

data class StreamGage(
    val siteId: String,
    val siteName: String,
    val latitude: Double,
    val longitude: Double,
    val gageHeightFt: Double?,
    val streamflowCfs: Double?,
    val dateTime: String,
    val floodStatus: FloodStatus
)

data class FloodImpactLocation(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val isFlooding: Boolean,
    val description: String
)

data class FloodAwarenessData(
    val streamGages: List<StreamGage>,
    val floodImpactLocations: List<FloodImpactLocation>,
    val searchQuery: String
)
