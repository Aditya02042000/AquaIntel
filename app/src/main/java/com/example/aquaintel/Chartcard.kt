package com.example.aquaintel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aquaintel.ui.theme.Charcoal
import com.example.aquaintel.ui.theme.GrayLight
import com.example.aquaintel.ui.theme.GrayMid
import com.example.aquaintel.ui.theme.White


@Composable
fun ChartCard(
    title: String,
    labels: List<String>,
    values: List<Float>,
    lineColor: Color,
    subtitle: String
) {
    Card(
        Modifier.fillMaxWidth(),
        RoundedCornerShape(20.dp),
        CardDefaults.cardColors(White),
        CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(18.dp)) {
            Text(title,    fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Charcoal)
            Text(subtitle, fontSize = 11.sp, color = GrayMid)
            Spacer(Modifier.height(14.dp))
            Row(
                Modifier.fillMaxWidth().height(90.dp),
                Arrangement.SpaceAround,
                Alignment.Bottom
            ) {
                values.forEachIndexed { i, v ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            Modifier
                                .width(26.dp)
                                .height((v * 72).dp)
                                .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                .background(
                                    Brush.verticalGradient(
                                        listOf(lineColor, lineColor.copy(0.35f))
                                    )
                                )
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(labels[i], fontSize = 9.sp, color = GrayLight)
                    }
                }
            }
        }
    }
}