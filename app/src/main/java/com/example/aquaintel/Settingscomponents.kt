package com.example.aquaintel

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aquaintel.ui.theme.Charcoal
import com.example.aquaintel.ui.theme.ForestGreen
import com.example.aquaintel.ui.theme.GrayLight
import com.example.aquaintel.ui.theme.GrayMid
import com.example.aquaintel.ui.theme.White


@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            title,
            fontSize   = 13.sp,
            fontWeight = FontWeight.Bold,
            color      = GrayMid,
            modifier   = Modifier.padding(start = 4.dp, bottom = 4.dp)
        )
        Card(
            Modifier.fillMaxWidth(),
            RoundedCornerShape(16.dp),
            CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
            CardDefaults.cardElevation(1.dp)
        ) {
            Column(Modifier.padding(4.dp)) { content() }
        }
    }
}

@Composable
fun ToggleSetting(
    title: String,
    subtitle: String,
    checked: Boolean,
    onChecked: (Boolean) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        Arrangement.SpaceBetween,
        Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(title,    fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface,   fontWeight = FontWeight.Medium)
            Text(subtitle, fontSize = 11.sp, color = GrayLight)
        }
        Switch(
            checked         = checked,
            onCheckedChange = onChecked,
            colors = SwitchDefaults.colors(
                checkedThumbColor = White,
                checkedTrackColor = ForestGreen
            )
        )
    }
}

@Composable
fun SettingsRow(
    title: String,
    subtitle: String,
    titleColor: Color? = null
) {
    val actualTitleColor = titleColor ?: MaterialTheme.colorScheme.onSurface
    Row(
        Modifier
            .fillMaxWidth()
            .clickable {}
            .padding(horizontal = 16.dp, vertical = 14.dp),
        Arrangement.SpaceBetween,
        Alignment.CenterVertically
    ) {
        Column {
            Text(title,    fontSize = 14.sp, color = actualTitleColor, fontWeight = FontWeight.Medium)
            Text(subtitle, fontSize = 11.sp, color = GrayLight)
        }
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight,
            null,
            tint     = GrayLight,
            modifier = Modifier.size(18.dp)
        )
    }
}