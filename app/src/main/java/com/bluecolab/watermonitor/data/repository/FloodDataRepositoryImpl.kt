package com.bluecolab.watermonitor.data.repository

import com.bluecolab.watermonitor.data.remote.api.UsgsFloodApi
import com.bluecolab.watermonitor.data.remote.api.UsgsWaterApi
import com.bluecolab.watermonitor.data.remote.dto.toDomainModels
import com.bluecolab.watermonitor.data.remote.dto.toStreamGages
import com.bluecolab.watermonitor.domain.model.FloodAwarenessData
import com.bluecolab.watermonitor.domain.model.FloodImpactLocation
import com.bluecolab.watermonitor.domain.model.FloodStatus
import com.bluecolab.watermonitor.domain.model.StreamGage
import com.bluecolab.watermonitor.domain.repository.FloodDataRepository
import com.bluecolab.watermonitor.util.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class FloodDataRepositoryImpl @Inject constructor(
    @Named("usgsWater") private val usgsWaterApi: UsgsWaterApi,
    @Named("usgsFlood") private val usgsFloodApi: UsgsFloodApi
) : FloodDataRepository {

    override fun getFloodDataByState(stateCd: String): Flow<Resource<FloodAwarenessData>> = flow {
        emit(Resource.Loading)

        try {
            val (streamGages, floodImpacts) = fetchBothApis(
                ivCall = { usgsWaterApi.getInstantaneousValues(stateCd = stateCd) },
                floodCall = { usgsFloodApi.getFloodImpactLocations(state = stateCd) }
            )

            val enrichedGages = enrichWithFloodStatus(streamGages, floodImpacts)

            emit(Resource.Success(
                FloodAwarenessData(
                    streamGages = enrichedGages,
                    floodImpactLocations = floodImpacts,
                    searchQuery = "State: $stateCd"
                )
            ))
        } catch (e: Exception) {
            emit(Resource.Error(
                message = e.message ?: "Failed to fetch flood data",
                throwable = e
            ))
        }
    }

    override fun getFloodDataByLocation(
        lat: Double,
        lon: Double,
        radiusMiles: Double
    ): Flow<Resource<FloodAwarenessData>> = flow {
        emit(Resource.Loading)

        try {
            val delta = radiusMiles / 69.0 // rough degree approximation
            val bBox = "${lon - delta},${lat - delta},${lon + delta},${lat + delta}"

            val (streamGages, floodImpacts) = fetchBothApis(
                ivCall = { usgsWaterApi.getInstantaneousValues(bBox = bBox) },
                floodCall = { usgsFloodApi.getFloodImpactLocations(bbox = bBox) }
            )

            val enrichedGages = enrichWithFloodStatus(streamGages, floodImpacts)

            emit(Resource.Success(
                FloodAwarenessData(
                    streamGages = enrichedGages,
                    floodImpactLocations = floodImpacts,
                    searchQuery = "Near %.4f, %.4f".format(lat, lon)
                )
            ))
        } catch (e: Exception) {
            emit(Resource.Error(
                message = e.message ?: "Failed to fetch flood data",
                throwable = e
            ))
        }
    }

    private suspend fun fetchBothApis(
        ivCall: suspend () -> com.bluecolab.watermonitor.data.remote.dto.UsgsInstantaneousResponse,
        floodCall: suspend () -> List<com.bluecolab.watermonitor.data.remote.dto.UsgsFloodImpactDto>
    ): Pair<List<StreamGage>, List<FloodImpactLocation>> = coroutineScope {
        val ivDeferred = async {
            try {
                ivCall().toStreamGages()
            } catch (_: Exception) {
                emptyList()
            }
        }
        val floodDeferred = async {
            try {
                floodCall().toDomainModels()
            } catch (_: Exception) {
                emptyList()
            }
        }
        Pair(ivDeferred.await(), floodDeferred.await())
    }

    /**
     * Enriches stream gage data with flood status from RTFI flood impact locations.
     * Matches by proximity (within ~0.05 degrees / ~3.5 miles).
     */
    private fun enrichWithFloodStatus(
        gages: List<StreamGage>,
        floodImpacts: List<FloodImpactLocation>
    ): List<StreamGage> {
        return gages.map { gage ->
            val nearbyFlood = floodImpacts.find { impact ->
                Math.abs(impact.latitude - gage.latitude) < 0.05 &&
                    Math.abs(impact.longitude - gage.longitude) < 0.05
            }

            val status = when {
                nearbyFlood?.isFlooding == true -> FloodStatus.FLOODING
                nearbyFlood != null -> FloodStatus.ELEVATED
                else -> gage.floodStatus
            }

            gage.copy(floodStatus = status)
        }
    }
}
