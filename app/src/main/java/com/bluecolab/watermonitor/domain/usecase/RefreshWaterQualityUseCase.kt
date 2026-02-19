package com.bluecolab.watermonitor.domain.usecase

import com.bluecolab.watermonitor.domain.model.WaterQualityData
import com.bluecolab.watermonitor.domain.repository.WaterQualityRepository
import com.bluecolab.watermonitor.util.Resource
import javax.inject.Inject

/**
 * Use case for refreshing water quality data from the remote source.
 */
class RefreshWaterQualityUseCase @Inject constructor(
    private val repository: WaterQualityRepository
) {
    suspend operator fun invoke(): Resource<WaterQualityData> {
        return repository.refreshWaterQuality()
    }
}
