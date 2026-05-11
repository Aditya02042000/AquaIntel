package com.example.aquaintel

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aquaintel.ui.theme.Charcoal
import com.example.aquaintel.ui.theme.LeafGreen


@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    actionText: String = "",
    onAction: (() -> Unit)? = null
) {
    Row(modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
        Text(title, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Charcoal)
        if (actionText.isNotEmpty() && onAction != null) {
            TextButton(onAction, contentPadding = PaddingValues(0.dp)) {
                Text(actionText, fontSize = 12.sp, color = LeafGreen)
            }
        }
    }
}