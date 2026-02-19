package com.bluecolab.watermonitor.data.remote.api

import com.bluecolab.watermonitor.data.remote.dto.WaterQualityDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit API interface for the Blue CoLab water quality API.
 */
interface BlueColabApi {

    /**
     * Get recent water quality data.
     * @param measurement The measurement station name (e.g., "Alan" for Choate Pond)
     * @param days Number of days of data to retrieve (default: 1)
     */
    @GET("{measurement}/delta")
    suspend fun getCurrentData(
        @Path("measurement") measurement: String = DEFAULT_MEASUREMENT,
        @Query("days") days: Int = 1
    ): List<WaterQualityDto>

    /**
     * Get water quality data for a specific date range.
     * @param measurement The measurement station name
     * @param startDate Start date in ISO-8601 format
     * @param stopDate End date in ISO-8601 format
     * @param stream Whether to stream results (default: false)
     */
    @GET("{measurement}/range")
    suspend fun getRangeData(
        @Path("measurement") measurement: String = DEFAULT_MEASUREMENT,
        @Query("start_date") startDate: String,
        @Query("stop_date") stopDate: String,
        @Query("stream") stream: Boolean = false
    ): List<WaterQualityDto>

    companion object {
        const val DEFAULT_MEASUREMENT = "Alan"
    }
}
