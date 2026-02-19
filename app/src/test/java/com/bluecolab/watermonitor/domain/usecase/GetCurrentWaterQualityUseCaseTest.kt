package com.bluecolab.watermonitor.domain.usecase

import com.bluecolab.watermonitor.domain.model.WaterQualityData
import com.bluecolab.watermonitor.domain.model.WaterQualityReading
import com.bluecolab.watermonitor.domain.model.WaterQualityStatus
import com.bluecolab.watermonitor.domain.repository.WaterQualityRepository
import com.bluecolab.watermonitor.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.Instant

class GetCurrentWaterQualityUseCaseTest {

    private lateinit var repository: WaterQualityRepository
    private lateinit var useCase: GetCurrentWaterQualityUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetCurrentWaterQualityUseCase(repository)
    }

    @Test
    fun `invoke returns loading then success when repository succeeds`() = runTest {
        // Given
        val mockReading = WaterQualityReading(
            timestamp = Instant.now(),
            temperature = 10.0,
            ph = 7.0,
            dissolvedOxygen = 85.0,
            turbidity = 5.0,
            conductivity = 500.0,
            salinity = 0.3
        )
        val mockData = WaterQualityData(
            locationName = "Choate Pond",
            locationAddress = "Pleasantville, NY",
            currentReading = mockReading,
            status = WaterQualityStatus.EXCELLENT,
            lastUpdated = Instant.now()
        )

        coEvery { repository.getCurrentWaterQuality() } returns flowOf(
            Resource.Loading,
            Resource.Success(mockData)
        )

        // When
        val results = useCase().toList()

        // Then
        assertEquals(2, results.size)
        assertTrue(results[0] is Resource.Loading)
        assertTrue(results[1] is Resource.Success)
        assertEquals(mockData, (results[1] as Resource.Success).data)
    }

    @Test
    fun `invoke returns loading then error when repository fails`() = runTest {
        // Given
        val errorMessage = "Network error"
        coEvery { repository.getCurrentWaterQuality() } returns flowOf(
            Resource.Loading,
            Resource.Error(errorMessage)
        )

        // When
        val results = useCase().toList()

        // Then
        assertEquals(2, results.size)
        assertTrue(results[0] is Resource.Loading)
        assertTrue(results[1] is Resource.Error)
        assertEquals(errorMessage, (results[1] as Resource.Error).message)
    }
}
