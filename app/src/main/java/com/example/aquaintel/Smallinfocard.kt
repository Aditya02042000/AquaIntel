package com.example.aquaintel

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.aquaintel.ui.theme.Charcoal
import com.example.aquaintel.ui.theme.GrayMid

@Composable
fun SmallInfoCard(
    title: String,
    value: String,
    emoji: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier,
        RoundedCornerShape(16.dp),
        CardDefaults.cardColors(color),
        CardDefaults.cardElevation(0.dp)
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(emoji, fontSize = 20.sp)
            Text(value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Charcoal)
            Text(title, fontSize = 10.sp, color = GrayMid)
        }
    }
}