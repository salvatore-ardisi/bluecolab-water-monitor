package com.bluecolab.watermonitor.data.remote.dto

import com.bluecolab.watermonitor.domain.model.WaterQualityReading
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.Instant

/**
 * Data Transfer Object for the Blue CoLab API response.
 * Maps the JSON structure from the API to Kotlin classes.
 */
@JsonClass(generateAdapter = true)
data class WaterQualityDto(
    @Json(name = "measurement")
    val measurement: String,

    @Json(name = "deployment_id")
    val deploymentId: Int,

    @Json(name = "timestamp")
    val timestamp: String,

    @Json(name = "sensors")
    val sensors: SensorsDto
)

@JsonClass(generateAdapter = true)
data class SensorsDto(
    @Json(name = "Temp")
    val temperature: Double,

    @Json(name = "pH")
    val ph: Double,

    @Json(name = "DOpct")
    val dissolvedOxygenPercent: Double,

    @Json(name = "Turb")
    val turbidity: Double,

    @Json(name = "Cond")
    val conductivity: Double,

    @Json(name = "Sal")
    val salinity: Double
)

/**
 * Extension function to convert DTO to domain model.
 */
fun WaterQualityDto.toDomainModel(): WaterQualityReading {
    return WaterQualityReading(
        timestamp = Instant.parse(timestamp),
        temperature = sensors.temperature,
        ph = sensors.ph,
        dissolvedOxygen = sensors.dissolvedOxygenPercent,
        turbidity = sensors.turbidity,
        conductivity = sensors.conductivity,
        salinity = sensors.salinity
    )
}

/**
 * Extension function to convert a list of DTOs to domain models.
 */
fun List<WaterQualityDto>.toDomainModels(): List<WaterQualityReading> {
    return map { it.toDomainModel() }
}
