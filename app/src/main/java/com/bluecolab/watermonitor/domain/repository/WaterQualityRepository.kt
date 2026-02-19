package com.bluecolab.watermonitor.domain.repository

import com.bluecolab.watermonitor.domain.model.WaterQualityData
import com.bluecolab.watermonitor.util.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for water quality data.
 * This defines the contract for data access, allowing for different implementations
 * (remote API, local cache, mock data for testing).
 */
interface WaterQualityRepository {

    /**
     * Get the current water quality data for Choate Pond.
     * Emits Loading, then either Success or Error.
     */
    fun getCurrentWaterQuality(): Flow<Resource<WaterQualityData>>

    /**
     * Force refresh the water quality data from the remote source.
     */
    suspend fun refreshWaterQuality(): Resource<WaterQualityData>

    /**
     * Get historical water quality data for a specific time range.
     * @param days Number of days of historical data to fetch
     */
    fun getHistoricalData(days: Int): Flow<Resource<WaterQualityData>>
}
