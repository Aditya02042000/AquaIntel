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
fun StatTile(
    label: String,
    value: String,
    emoji: String,
    color: Color,
    modifier: Modifier
) {
    Card(
        modifier,
        RoundedCornerShape(18.dp),
        CardDefaults.cardColors(color),
        CardDefaults.cardElevation(0.dp)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(emoji, fontSize = 22.sp)
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Charcoal)
            Text(label, fontSize = 11.sp, color = GrayMid)
        }
    }
}