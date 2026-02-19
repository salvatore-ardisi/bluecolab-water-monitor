package com.bluecolab.watermonitor.util

object Constants {
    // API Configuration
    const val BLUE_COLAB_BASE_URL = "https://colabprod01.pace.edu/api/influx/sensordata/"
    const val USGS_BASE_URL = "https://waterservices.usgs.gov/nwis/"

    // Default measurement station
    const val DEFAULT_MEASUREMENT = "Alan"

    // Location info
    const val CHOATE_POND_NAME = "Choate Pond"
    const val CHOATE_POND_LOCATION = "Pleasantville, NY"
    const val CHOATE_POND_LAT = 41.127494
    const val CHOATE_POND_LONG = -73.808235

    // Network
    const val NETWORK_TIMEOUT_SECONDS = 30L
    const val CACHE_SIZE_BYTES = 10L * 1024 * 1024 // 10 MB

    // Water Quality Thresholds
    object WaterQuality {
        // pH thresholds (EPA standards: 6.5-8.5 for freshwater)
        const val PH_MIN_SAFE = 6.5
        const val PH_MAX_SAFE = 8.5

        // Dissolved Oxygen (EPA: > 5 mg/L or > 60% saturation is healthy)
        const val DO_MIN_SAFE_PERCENT = 60.0

        // Turbidity (EPA: < 5 NTU for drinking water)
        const val TURBIDITY_MAX_SAFE = 50.0

        // Temperature (varies by species, general freshwater range)
        const val TEMP_MIN_SAFE_CELSIUS = 0.0
        const val TEMP_MAX_SAFE_CELSIUS = 30.0

        // Conductivity (varies widely, freshwater typically < 1500 μS/cm)
        const val CONDUCTIVITY_MAX_SAFE = 1500.0

        // Salinity (freshwater < 0.5 ppt)
        const val SALINITY_MAX_FRESHWATER = 0.5
    }

    // External Links
    const val BLUE_COLAB_WEBSITE = "https://bluecolab.pace.edu/"
    const val BLUE_COLAB_ABOUT = "https://www.pace.edu/seidenberg/blue-colab/about"
}
