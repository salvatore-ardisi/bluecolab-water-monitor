package com.bluecolab.watermonitor.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bluecolab.watermonitor.domain.model.WaterQualityStatus
import com.bluecolab.watermonitor.presentation.theme.QualityExcellent
import com.bluecolab.watermonitor.presentation.theme.QualityFair
import com.bluecolab.watermonitor.presentation.theme.QualityGood
import com.bluecolab.watermonitor.presentation.theme.QualityPoor

@Composable
fun WaterQualityStatusBadge(
    status: WaterQualityStatus,
    modifier: Modifier = Modifier,
    large: Boolean = false
) {
    val (backgroundColor, textColor, statusText) = when (status) {
        WaterQualityStatus.EXCELLENT -> Triple(QualityExcellent, Color.White, "Excellent")
        WaterQualityStatus.GOOD -> Triple(QualityGood, Color.White, "Good")
        WaterQualityStatus.FAIR -> Triple(QualityFair, Color.Black, "Fair")
        WaterQualityStatus.POOR -> Triple(QualityPoor, Color.White, "Poor")
        WaterQualityStatus.UNKNOWN -> Triple(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurfaceVariant,
            "Unknown"
        )
    }

    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(if (large) 16.dp else 8.dp)
            )
            .padding(
                horizontal = if (large) 24.dp else 12.dp,
                vertical = if (large) 12.dp else 6.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = statusText,
            style = if (large) {
                MaterialTheme.typography.displaySmall
            } else {
                MaterialTheme.typography.labelLarge
            },
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
fun WaterQualityHeroStatus(
    status: WaterQualityStatus,
    modifier: Modifier = Modifier
) {
    val statusText = when (status) {
        WaterQualityStatus.EXCELLENT -> "Excellent"
        WaterQualityStatus.GOOD -> "Good"
        WaterQualityStatus.FAIR -> "Fair"
        WaterQualityStatus.POOR -> "Poor"
        WaterQualityStatus.UNKNOWN -> "Unknown"
    }

    val statusColor = when (status) {
        WaterQualityStatus.EXCELLENT -> QualityExcellent
        WaterQualityStatus.GOOD -> QualityGood
        WaterQualityStatus.FAIR -> QualityFair
        WaterQualityStatus.POOR -> QualityPoor
        WaterQualityStatus.UNKNOWN -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Text(
        text = statusText,
        style = MaterialTheme.typography.displayLarge,
        fontWeight = FontWeight.Bold,
        color = statusColor,
        modifier = modifier
    )
}
