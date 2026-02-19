package com.bluecolab.watermonitor.domain.usecase

import com.bluecolab.watermonitor.domain.model.WaterQualityData
import com.bluecolab.watermonitor.domain.repository.WaterQualityRepository
import com.bluecolab.watermonitor.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for fetching current water quality data.
 * This encapsulates the business logic for retrieving water quality information.
 */
class GetCurrentWaterQualityUseCase @Inject constructor(
    private val repository: WaterQualityRepository
) {
    operator fun invoke(): Flow<Resource<WaterQualityData>> {
        return repository.getCurrentWaterQuality()
    }
}
