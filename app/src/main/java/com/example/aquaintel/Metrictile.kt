package com.example.aquaintel

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.aquaintel.ui.theme.Charcoal
import com.example.aquaintel.ui.theme.GrayMid


@Composable
fun MetricTile(
    data: HealthMetricData,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier  = modifier.aspectRatio(0.88f).clickable(onClick = onClick),
        shape     = RoundedCornerShape(18.dp),
        colors    = CardDefaults.cardColors(data.cardColor),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(Modifier.fillMaxSize().padding(14.dp), Arrangement.SpaceBetween) {
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(data.icon,  fontSize = 16.sp)
                Text(data.title, fontSize = 11.sp, color = GrayMid, maxLines = 1)
            }
            MiniSparkline(data.trend, Modifier.fillMaxWidth().height(30.dp))
            Row(
                verticalAlignment     = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(data.value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Charcoal)
                Text(data.unit,  fontSize = 10.sp, color = GrayMid, modifier = Modifier.padding(bottom = 2.dp))
            }
        }
    }
}