package com.bluecolab.watermonitor.domain.repository

import com.bluecolab.watermonitor.domain.model.FloodAwarenessData
import com.bluecolab.watermonitor.util.Resource
import kotlinx.coroutines.flow.Flow

interface FloodDataRepository {

    fun getFloodDataByState(stateCd: String): Flow<Resource<FloodAwarenessData>>

    fun getFloodDataByLocation(lat: Double, lon: Double, radiusMiles: Double = 25.0): Flow<Resource<FloodAwarenessData>>
}
