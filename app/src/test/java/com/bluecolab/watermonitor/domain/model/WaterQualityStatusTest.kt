package com.bluecolab.watermonitor.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class WaterQualityStatusTest {

    @Test
    fun `fromReading returns EXCELLENT for optimal values`() {
        val reading = WaterQualityReading(
            timestamp = Instant.now(),
            temperature = 15.0,  // Good range
            ph = 7.5,            // Ideal
            dissolvedOxygen = 90.0,  // Excellent
            turbidity = 5.0,     // Low/excellent
            conductivity = 500.0,
            salinity = 0.3
        )

        val status = WaterQualityStatus.fromReading(reading)

        assertEquals(WaterQualityStatus.EXCELLENT, status)
    }

    @Test
    fun `fromReading returns GOOD for acceptable values`() {
        val reading = WaterQualityReading(
            timestamp = Instant.now(),
            temperature = 18.0,
            ph = 7.0,
            dissolvedOxygen = 70.0,  // Good but not excellent
            turbidity = 30.0,        // Acceptable
            conductivity = 600.0,
            salinity = 0.4
        )

        val status = WaterQualityStatus.fromReading(reading)

        assertEquals(WaterQualityStatus.GOOD, status)
    }

    @Test
    fun `fromReading returns POOR for bad values`() {
        val reading = WaterQualityReading(
            timestamp = Instant.now(),
            temperature = 35.0,      // Too hot
            ph = 5.0,                // Too acidic
            dissolvedOxygen = 30.0,  // Too low
            turbidity = 100.0,       // Too high
            conductivity = 2000.0,
            salinity = 1.0
        )

        val status = WaterQualityStatus.fromReading(reading)

        assertEquals(WaterQualityStatus.POOR, status)
    }

    @Test
    fun `temperatureFahrenheit calculates correctly`() {
        val reading = WaterQualityReading(
            timestamp = Instant.now(),
            temperature = 0.0,  // 32°F
            ph = 7.0,
            dissolvedOxygen = 80.0,
            turbidity = 10.0,
            conductivity = 500.0,
            salinity = 0.3
        )

        assertEquals(32.0, reading.temperatureFahrenheit, 0.01)
    }

    @Test
    fun `temperatureFahrenheit calculates correctly for 100C`() {
        val reading = WaterQualityReading(
            timestamp = Instant.now(),
            temperature = 100.0,  // 212°F
            ph = 7.0,
            dissolvedOxygen = 80.0,
            turbidity = 10.0,
            conductivity = 500.0,
            salinity = 0.3
        )

        assertEquals(212.0, reading.temperatureFahrenheit, 0.01)
    }
}
