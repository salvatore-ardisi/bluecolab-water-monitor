package com.bluecolab.watermonitor.data.remote.dto

import com.bluecolab.watermonitor.domain.model.FloodStatus
import com.bluecolab.watermonitor.domain.model.StreamGage
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UsgsInstantaneousResponse(
    @Json(name = "value")
    val value: UsgsIvValue
)

@JsonClass(generateAdapter = true)
data class UsgsIvValue(
    @Json(name = "timeSeries")
    val timeSeries: List<UsgsTimeSeries> = emptyList()
)

@JsonClass(generateAdapter = true)
data class UsgsTimeSeries(
    @Json(name = "sourceInfo")
    val sourceInfo: UsgsSourceInfo,
    @Json(name = "variable")
    val variable: UsgsVariable,
    @Json(name = "values")
    val values: List<UsgsValues> = emptyList()
)

@JsonClass(generateAdapter = true)
data class UsgsSourceInfo(
    @Json(name = "siteName")
    val siteName: String,
    @Json(name = "siteCode")
    val siteCode: List<UsgsSiteCode> = emptyList(),
    @Json(name = "geoLocation")
    val geoLocation: UsgsGeoLocation? = null
)

@JsonClass(generateAdapter = true)
data class UsgsSiteCode(
    @Json(name = "value")
    val value: String,
    @Json(name = "agencyCode")
    val agencyCode: String? = null
)

@JsonClass(generateAdapter = true)
data class UsgsGeoLocation(
    @Json(name = "geogLocation")
    val geogLocation: UsgsGeogLocation? = null
)

@JsonClass(generateAdapter = true)
data class UsgsGeogLocation(
    @Json(name = "latitude")
    val latitude: Double,
    @Json(name = "longitude")
    val longitude: Double
)

@JsonClass(generateAdapter = true)
data class UsgsVariable(
    @Json(name = "variableCode")
    val variableCode: List<UsgsVariableCode> = emptyList(),
    @Json(name = "variableName")
    val variableName: String? = null,
    @Json(name = "unit")
    val unit: UsgsUnit? = null
)

@JsonClass(generateAdapter = true)
data class UsgsVariableCode(
    @Json(name = "value")
    val value: String
)

@JsonClass(generateAdapter = true)
data class UsgsUnit(
    @Json(name = "unitCode")
    val unitCode: String? = null
)

@JsonClass(generateAdapter = true)
data class UsgsValues(
    @Json(name = "value")
    val value: List<UsgsValueEntry> = emptyList()
)

@JsonClass(generateAdapter = true)
data class UsgsValueEntry(
    @Json(name = "value")
    val value: String,
    @Json(name = "dateTime")
    val dateTime: String
)

/**
 * Groups time series data by site and maps to domain [StreamGage] models.
 * The IV API returns separate time series entries for gage height (00065) and streamflow (00060),
 * so we need to merge them by site ID.
 */
fun UsgsInstantaneousResponse.toStreamGages(): List<StreamGage> {
    data class SiteData(
        var siteName: String = "",
        var siteId: String = "",
        var latitude: Double = 0.0,
        var longitude: Double = 0.0,
        var gageHeight: Double? = null,
        var streamflow: Double? = null,
        var dateTime: String = ""
    )

    val siteMap = mutableMapOf<String, SiteData>()

    for (ts in value.timeSeries) {
        val siteId = ts.sourceInfo.siteCode.firstOrNull()?.value ?: continue
        val paramCode = ts.variable.variableCode.firstOrNull()?.value ?: continue
        val latestValue = ts.values.firstOrNull()?.value?.lastOrNull() ?: continue
        val numericValue = latestValue.value.toDoubleOrNull()

        val site = siteMap.getOrPut(siteId) {
            SiteData(
                siteName = ts.sourceInfo.siteName,
                siteId = siteId,
                latitude = ts.sourceInfo.geoLocation?.geogLocation?.latitude ?: 0.0,
                longitude = ts.sourceInfo.geoLocation?.geogLocation?.longitude ?: 0.0
            )
        }

        when (paramCode) {
            "00065" -> {
                site.gageHeight = numericValue
                site.dateTime = latestValue.dateTime
            }
            "00060" -> {
                site.streamflow = numericValue
                if (site.dateTime.isEmpty()) site.dateTime = latestValue.dateTime
            }
        }
    }

    return siteMap.values.map { site ->
        StreamGage(
            siteId = site.siteId,
            siteName = site.siteName,
            latitude = site.latitude,
            longitude = site.longitude,
            gageHeightFt = site.gageHeight,
            streamflowCfs = site.streamflow,
            dateTime = site.dateTime,
            floodStatus = FloodStatus.UNKNOWN
        )
    }
}
