package com.bluecolab.watermonitor.data.remote.api

import com.bluecolab.watermonitor.data.remote.dto.UsgsInstantaneousResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface UsgsWaterApi {

    @GET("iv/")
    suspend fun getInstantaneousValues(
        @Query("format") format: String = "json",
        @Query("stateCd") stateCd: String? = null,
        @Query("bBox") bBox: String? = null,
        @Query("parameterCd") parameterCd: String = "00065,00060",
        @Query("siteStatus") siteStatus: String = "active"
    ): UsgsInstantaneousResponse
}
