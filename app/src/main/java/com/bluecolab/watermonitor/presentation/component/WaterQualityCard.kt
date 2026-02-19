package com.bluecolab.watermonitor.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Opacity
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material.icons.outlined.Thermostat
import androidx.compose.material.icons.outlined.Waves
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SensorMetricCard(
    label: String,
    value: String,
    unit: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = unit,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun MetricsGrid(
    temperature: Double?,
    ph: Double?,
    dissolvedOxygen: Double?,
    turbidity: Double?,
    conductivity: Double?,
    salinity: Double?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // First row: Temperature, pH, DO
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SensorMetricCard(
                label = "Temperature",
                value = temperature?.let { "%.1f".format(it) } ?: "--",
                unit = "°C",
                icon = Icons.Outlined.Thermostat,
                modifier = Modifier.weight(1f)
            )
            SensorMetricCard(
                label = "pH",
                value = ph?.let { "%.2f".format(it) } ?: "--",
                unit = "",
                icon = Icons.Outlined.Science,
                modifier = Modifier.weight(1f)
            )
            SensorMetricCard(
                label = "Dissolved O₂",
                value = dissolvedOxygen?.let { "%.1f".format(it) } ?: "--",
                unit = "%",
                icon = Icons.Outlined.Opacity,
                modifier = Modifier.weight(1f)
            )
        }

        // Second row: Turbidity, Conductivity, Salinity
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SensorMetricCard(
                label = "Turbidity",
                value = turbidity?.let { "%.1f".format(it) } ?: "--",
                unit = "NTU",
                icon = Icons.Outlined.Waves,
                modifier = Modifier.weight(1f)
            )
            SensorMetricCard(
                label = "Conductivity",
                value = conductivity?.let { "%.0f".format(it) } ?: "--",
                unit = "μS/cm",
                icon = Icons.Outlined.Waves,
                modifier = Modifier.weight(1f)
            )
            SensorMetricCard(
                label = "Salinity",
                value = salinity?.let { "%.2f".format(it) } ?: "--",
                unit = "ppt",
                icon = Icons.Outlined.Waves,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
