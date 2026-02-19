package com.bluecolab.watermonitor.domain.usecase

import com.bluecolab.watermonitor.domain.model.FloodAwarenessData
import com.bluecolab.watermonitor.domain.repository.FloodDataRepository
import com.bluecolab.watermonitor.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFloodDataUseCase @Inject constructor(
    private val repository: FloodDataRepository
) {
    fun byState(stateCd: String): Flow<Resource<FloodAwarenessData>> {
        return repository.getFloodDataByState(stateCd)
    }

    fun byLocation(lat: Double, lon: Double, radiusMiles: Double = 25.0): Flow<Resource<FloodAwarenessData>> {
        return repository.getFloodDataByLocation(lat, lon, radiusMiles)
    }
}
