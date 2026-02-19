package com.bluecolab.watermonitor.domain.model

import java.time.Instant

/**
 * Domain model representing a single water quality reading from a sensor.
 */
data class WaterQualityReading(
    val timestamp: Instant,
    val temperature: Double,      // Celsius
    val ph: Double,               // pH scale (0-14)
    val dissolvedOxygen: Double,  // Percentage saturation
    val turbidity: Double,        // NTU (Nephelometric Turbidity Units)
    val conductivity: Double,     // μS/cm (microsiemens per centimeter)
    val salinity: Double          // ppt (parts per thousand)
) {
    val temperatureFahrenheit: Double
        get() = (temperature * 9 / 5) + 32
}

/**
 * Represents the overall water quality status based on sensor readings.
 */
enum class WaterQualityStatus {
    EXCELLENT,
    GOOD,
    FAIR,
    POOR,
    UNKNOWN;

    companion object {
        fun fromReading(reading: WaterQualityReading): WaterQualityStatus {
            var score = 0

            // pH check (6.5-8.5 is ideal)
            if (reading.ph in 6.5..8.5) score += 2
            else if (reading.ph in 6.0..9.0) score += 1

            // Dissolved Oxygen check (> 80% is excellent, > 60% is good)
            if (reading.dissolvedOxygen > 80) score += 2
            else if (reading.dissolvedOxygen > 60) score += 1

            // Turbidity check (< 10 NTU is excellent, < 50 is acceptable)
            if (reading.turbidity < 10) score += 2
            else if (reading.turbidity < 50) score += 1

            // Temperature check (5-25°C is good for most freshwater)
            if (reading.temperature in 5.0..25.0) score += 2
            else if (reading.temperature in 0.0..30.0) score += 1

            return when {
                score >= 7 -> EXCELLENT
                score >= 5 -> GOOD
                score >= 3 -> FAIR
                else -> POOR
            }
        }
    }
}

/**
 * Aggregated water quality data for display purposes.
 */
data class WaterQualityData(
    val locationName: String,
    val locationAddress: String,
    val currentReading: WaterQualityReading?,
    val status: WaterQualityStatus,
    val lastUpdated: Instant?,
    val readings: List<WaterQualityReading> = emptyList()
) {
    val hasData: Boolean get() = currentReading != null
}
