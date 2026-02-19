package com.bluecolab.watermonitor.data.remote.api

import com.bluecolab.watermonitor.data.remote.dto.UsgsFloodImpactDto
import retrofit2.http.GET
import retrofit2.http.Query

interface UsgsFloodApi {

    @GET("v0/locations")
    suspend fun getFloodImpactLocations(
        @Query("state") state: String? = null,
        @Query("bbox") bbox: String? = null
    ): List<UsgsFloodImpactDto>
}
