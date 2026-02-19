package com.bluecolab.watermonitor.presentation.screen.home

import com.bluecolab.watermonitor.domain.model.WaterQualityData
import com.bluecolab.watermonitor.domain.model.WaterQualityReading
import com.bluecolab.watermonitor.domain.model.WaterQualityStatus
import com.bluecolab.watermonitor.domain.usecase.GetCurrentWaterQualityUseCase
import com.bluecolab.watermonitor.domain.usecase.RefreshWaterQualityUseCase
import com.bluecolab.watermonitor.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private lateinit var getCurrentWaterQualityUseCase: GetCurrentWaterQualityUseCase
    private lateinit var refreshWaterQualityUseCase: RefreshWaterQualityUseCase
    private val testDispatcher = StandardTestDispatcher()

    private val mockReading = WaterQualityReading(
        timestamp = Instant.now(),
        temperature = 10.0,
        ph = 7.0,
        dissolvedOxygen = 85.0,
        turbidity = 5.0,
        conductivity = 500.0,
        salinity = 0.3
    )

    private val mockData = WaterQualityData(
        locationName = "Choate Pond",
        locationAddress = "Pleasantville, NY",
        currentReading = mockReading,
        status = WaterQualityStatus.EXCELLENT,
        lastUpdated = Instant.now()
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getCurrentWaterQualityUseCase = mockk()
        refreshWaterQualityUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state loads water quality data`() = runTest {
        // Given
        coEvery { getCurrentWaterQualityUseCase() } returns flowOf(
            Resource.Loading,
            Resource.Success(mockData)
        )

        // When
        val viewModel = HomeViewModel(getCurrentWaterQualityUseCase, refreshWaterQualityUseCase)
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNotNull(state.waterQualityData)
        assertEquals("Choate Pond", state.waterQualityData?.locationName)
        assertNull(state.error)
    }

    @Test
    fun `refresh updates state on success`() = runTest {
        // Given
        coEvery { getCurrentWaterQualityUseCase() } returns flowOf(Resource.Success(mockData))
        coEvery { refreshWaterQualityUseCase() } returns Resource.Success(mockData)

        val viewModel = HomeViewModel(getCurrentWaterQualityUseCase, refreshWaterQualityUseCase)
        advanceUntilIdle()

        // When
        viewModel.refresh()
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertFalse(state.isRefreshing)
        assertNotNull(state.waterQualityData)
        assertNull(state.error)
    }

    @Test
    fun `refresh updates state on error`() = runTest {
        // Given
        coEvery { getCurrentWaterQualityUseCase() } returns flowOf(Resource.Success(mockData))
        coEvery { refreshWaterQualityUseCase() } returns Resource.Error("Network error")

        val viewModel = HomeViewModel(getCurrentWaterQualityUseCase, refreshWaterQualityUseCase)
        advanceUntilIdle()

        // When
        viewModel.refresh()
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertFalse(state.isRefreshing)
        assertEquals("Network error", state.error)
    }

    @Test
    fun `clearError removes error from state`() = runTest {
        // Given
        coEvery { getCurrentWaterQualityUseCase() } returns flowOf(
            Resource.Error("Some error")
        )

        val viewModel = HomeViewModel(getCurrentWaterQualityUseCase, refreshWaterQualityUseCase)
        advanceUntilIdle()

        // When
        viewModel.clearError()

        // Then
        assertNull(viewModel.uiState.value.error)
    }
}
