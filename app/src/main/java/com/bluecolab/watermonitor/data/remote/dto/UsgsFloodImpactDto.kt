package com.bluecolab.watermonitor.data.remote.dto

import com.bluecolab.watermonitor.domain.model.FloodImpactLocation
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UsgsFloodImpactDto(
    @Json(name = "lid")
    val lid: String? = null,
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "latitude")
    val latitude: Double? = null,
    @Json(name = "longitude")
    val longitude: Double? = null,
    @Json(name = "status")
    val status: String? = null,
    @Json(name = "isFlooding")
    val isFlooding: Boolean? = null,
    @Json(name = "description")
    val description: String? = null,
    @Json(name = "state")
    val state: String? = null
)

fun UsgsFloodImpactDto.toDomainModel(): FloodImpactLocation {
    return FloodImpactLocation(
        id = lid ?: "",
        name = name ?: "Unknown Location",
        latitude = latitude ?: 0.0,
        longitude = longitude ?: 0.0,
        isFlooding = isFlooding ?: (status?.lowercase()?.contains("flood") == true),
        description = description ?: status ?: ""
    )
}

fun List<UsgsFloodImpactDto>.toDomainModels(): List<FloodImpactLocation> {
    return map { it.toDomainModel() }
}
