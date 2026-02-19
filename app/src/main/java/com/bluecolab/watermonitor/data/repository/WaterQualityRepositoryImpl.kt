package com.bluecolab.watermonitor.data.repository

import com.bluecolab.watermonitor.data.remote.api.BlueColabApi
import com.bluecolab.watermonitor.data.remote.dto.toDomainModels
import com.bluecolab.watermonitor.domain.model.WaterQualityData
import com.bluecolab.watermonitor.domain.model.WaterQualityStatus
import com.bluecolab.watermonitor.domain.repository.WaterQualityRepository
import com.bluecolab.watermonitor.util.Constants
import com.bluecolab.watermonitor.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WaterQualityRepositoryImpl @Inject constructor(
    private val api: BlueColabApi
) : WaterQualityRepository {

    override fun getCurrentWaterQuality(): Flow<Resource<WaterQualityData>> = flow {
        emit(Resource.Loading)

        try {
            val response = api.getCurrentData(
                measurement = BlueColabApi.DEFAULT_MEASUREMENT,
                days = 1
            )

            val readings = response.toDomainModels()
            val currentReading = readings.maxByOrNull { it.timestamp }
            val status = currentReading?.let { WaterQualityStatus.fromReading(it) }
                ?: WaterQualityStatus.UNKNOWN

            val waterQualityData = WaterQualityData(
                locationName = Constants.CHOATE_POND_NAME,
                locationAddress = Constants.CHOATE_POND_LOCATION,
                currentReading = currentReading,
                status = status,
                lastUpdated = currentReading?.timestamp,
                readings = readings.sortedByDescending { it.timestamp }
            )

            emit(Resource.Success(waterQualityData))
        } catch (e: Exception) {
            emit(Resource.Error(
                message = e.message ?: "An unexpected error occurred",
                throwable = e
            ))
        }
    }

    override suspend fun refreshWaterQuality(): Resource<WaterQualityData> {
        return try {
            val response = api.getCurrentData(
                measurement = BlueColabApi.DEFAULT_MEASUREMENT,
                days = 1
            )

            val readings = response.toDomainModels()
            val currentReading = readings.maxByOrNull { it.timestamp }
            val status = currentReading?.let { WaterQualityStatus.fromReading(it) }
                ?: WaterQualityStatus.UNKNOWN

            Resource.Success(
                WaterQualityData(
                    locationName = Constants.CHOATE_POND_NAME,
                    locationAddress = Constants.CHOATE_POND_LOCATION,
                    currentReading = currentReading,
                    status = status,
                    lastUpdated = currentReading?.timestamp,
                    readings = readings.sortedByDescending { it.timestamp }
                )
            )
        } catch (e: Exception) {
            Resource.Error(
                message = e.message ?: "Failed to refresh water quality data",
                throwable = e
            )
        }
    }

    override fun getHistoricalData(days: Int): Flow<Resource<WaterQualityData>> = flow {
        emit(Resource.Loading)

        try {
            val endDate = Instant.now()
            val startDate = endDate.minusSeconds(days.toLong() * 24 * 60 * 60)

            val formatter = DateTimeFormatter.ISO_INSTANT.withZone(ZoneOffset.UTC)

            val response = api.getRangeData(
                measurement = BlueColabApi.DEFAULT_MEASUREMENT,
                startDate = formatter.format(startDate),
                stopDate = formatter.format(endDate)
            )

            val readings = response.toDomainModels()
            val currentReading = readings.maxByOrNull { it.timestamp }
            val status = currentReading?.let { WaterQualityStatus.fromReading(it) }
                ?: WaterQualityStatus.UNKNOWN

            emit(Resource.Success(
                WaterQualityData(
                    locationName = Constants.CHOATE_POND_NAME,
                    locationAddress = Constants.CHOATE_POND_LOCATION,
                    currentReading = currentReading,
                    status = status,
                    lastUpdated = currentReading?.timestamp,
                    readings = readings.sortedByDescending { it.timestamp }
                )
            ))
        } catch (e: Exception) {
            emit(Resource.Error(
                message = e.message ?: "Failed to load historical data",
                throwable = e
            ))
        }
    }
}
