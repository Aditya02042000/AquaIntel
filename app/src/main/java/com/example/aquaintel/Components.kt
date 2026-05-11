package com.example.aquaintel.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.*
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.StrokeCap
import com.example.aquaintel.HealthMetricData


import com.example.aquaintel.ui.theme.Charcoal
import com.example.aquaintel.ui.theme.CreamDark
import com.example.aquaintel.ui.theme.ForestGreen
import com.example.aquaintel.ui.theme.GrayLight
import com.example.aquaintel.ui.theme.GrayMid
import com.example.aquaintel.ui.theme.LeafGreen
import com.example.aquaintel.ui.theme.MintGreen
import com.example.aquaintel.ui.theme.White


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithBack(title: String, onBack: () -> Unit) {
    TopAppBar(
        title = { Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Box(
                    Modifier.size(36.dp).background(MaterialTheme.colorScheme.onSurface.copy(0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(18.dp))
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
    )
}

@Composable
fun AppBottomNav(
    selectedIndex: Int,
    onHome: () -> Unit,
    onChat: () -> Unit,
    onReport: () -> Unit,
    onProfile: () -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {

        listOf(
            Triple(Icons.Default.Home, "Dashboard", 0),
            Triple(Icons.Default.Favorite, "Health", 1),
            Triple(Icons.Default.Email  , "Chat", 2),
            Triple(Icons.AutoMirrored.Filled.List , "Report", 3),  
            Triple(Icons.Default.Person, "Profile", 4)
        ).forEach { item ->   // ✅ FIXED destructuring issue

            val icon = item.first
            val label = item.second
            val index = item.third

            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = {
                    when (index) {
                        0 -> onHome()
                        1 -> onHome()
                        2 -> onChat()
                        3 -> onReport()
                        4 -> onProfile()
                    }
                },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(label, fontSize = 10.sp)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = ForestGreen,
                    selectedTextColor = ForestGreen,
                    unselectedIconColor = GrayLight,
                    unselectedTextColor = GrayLight,
                    indicatorColor = MintGreen
                )
            )
        }
    }
}

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector? = null,
    isPassword: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text(placeholder, fontSize = 13.sp, color = GrayLight) },
        leadingIcon = if (leadingIcon != null) {
            { Icon(leadingIcon, null, tint = GrayLight) }
        } else null,
        trailingIcon = trailingIcon,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyType),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = LeafGreen,
            unfocusedBorderColor = CreamDark,
            cursorColor = LeafGreen
        )
    )
}

@Composable
fun SmallInfoCard(title: String, value: String, emoji: String, color: Color, modifier: Modifier = Modifier) {
    Card(modifier, shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(color)) {
        Column(Modifier.padding(12.dp)) {
            Text(emoji, fontSize = 20.sp)
            Text(value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Charcoal)
            Text(title, fontSize = 10.sp, color = GrayMid)
        }
    }
}

@Composable
fun MetricTile(data: HealthMetricData, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.aspectRatio(0.88f).clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(data.cardColor)
    ) {
        Column(
            Modifier.fillMaxSize().padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(data.title, fontSize = 12.sp)
            Text(data.value, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun MiniSparkline(points: List<Float>, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        if (points.size < 2) return@Canvas
        val stepX = size.width / (points.size - 1)
        val path = Path()
        points.forEachIndexed { i, v ->
            val x = i * stepX
            val y = size.height * (1f - v)
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        drawPath(path, LeafGreen, style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round))
    }
}

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    string: String,
    function: () -> Unit
) {
    Text(title, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, modifier = modifier)
}

@Composable
fun BPHistoryRow(systolic: Int, diastolic: Int, date: String) {
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)) {
        Row(Modifier.padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text("$systolic/$diastolic mmHg")
                Text(date, fontSize = 11.sp, color = GrayLight)
            }
        }
    }
}

@Composable
fun AddBPDialog(onDismiss: () -> Unit, onSave: (Int, Int, Int) -> Unit) {
    var sys by remember { mutableStateOf("") }
    var dia by remember { mutableStateOf("") }
    var pul by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Log BP Reading") },
        text = {
            Column {
                AppTextField(sys, { sys = it }, "Systolic", keyType = KeyboardType.Number)
                AppTextField(dia, { dia = it }, "Diastolic", keyType = KeyboardType.Number)
                AppTextField(pul, { pul = it }, "Pulse", keyType = KeyboardType.Number)
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(sys.toIntOrNull() ?: 120, dia.toIntOrNull() ?: 80, pul.toIntOrNull() ?: 72)
            }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun SettingsRow(title: String, subtitle: String) {
    Row(
        Modifier.fillMaxWidth().clickable {}.padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(title)
            Text(subtitle, fontSize = 11.sp, color = GrayLight)
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null)
    }
}