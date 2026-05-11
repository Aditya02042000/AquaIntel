package com.example.aquaintel

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.*
import com.example.aquaintel.Screen
import com.example.aquaintel.ActivityRecord
import com.example.aquaintel.AppTextField
import com.example.aquaintel.SmallInfoCard
import com.example.aquaintel.TopBarWithBack


import com.example.aquaintel.ui.components.SectionHeader
import com.example.aquaintel.ui.theme.CardBlue
import com.example.aquaintel.ui.theme.CardGreen
import com.example.aquaintel.ui.theme.CardOrange
import com.example.aquaintel.ui.theme.CardPink
import com.example.aquaintel.ui.theme.CardYellow
import com.example.aquaintel.ui.theme.Charcoal
import com.example.aquaintel.ui.theme.Cream
import com.example.aquaintel.ui.theme.ForestGreen
import com.example.aquaintel.ui.theme.GrayLight
import com.example.aquaintel.ui.theme.LeafGreen
import com.example.aquaintel.ui.theme.White

@Composable
fun ActivityScreen(onNavigate: (Screen.Home) -> Unit) {
    val activities = remember {
        mutableStateListOf(
            ActivityRecord("Morning Run",    "30 min", 320, "🏃", CardGreen),
            ActivityRecord("Yoga Session",   "45 min", 180, "🧘", CardBlue),
            ActivityRecord("Evening Walk",   "20 min", 120, "🚶", CardYellow),
            ActivityRecord("Cycling",        "60 min", 450, "🚴", CardPink),
            ActivityRecord("Weight Training", "40 min", 280, "🏋️", CardOrange)
        )
    }
    val totalCal   = activities.sumOf { it.calories }
    var showAdd    by remember { mutableStateOf(false) }
    var newAct     by remember { mutableStateOf("") }
    var newDur     by remember { mutableStateOf("") }
    var newCal     by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Cream,
        topBar = {
            TopBarWithBack("Activity", { onNavigate(Screen.Home) })
        },
        floatingActionButton = {
            FloatingActionButton({ showAdd = true }, shape = CircleShape, containerColor = ForestGreen) {
                Icon(Icons.Default.Add, null, tint = White)
            }
        }
    ) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(horizontal = 22.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(Modifier.fillMaxWidth(), RoundedCornerShape(22.dp), CardDefaults.cardColors(CardGreen), CardDefaults.cardElevation(0.dp)) {
                    Column(Modifier.padding(20.dp)) {
                        Text("Today's Activity", fontSize = 13.sp, color = ForestGreen)
                        Spacer(Modifier.height(8.dp))
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                            Column {
                                Text("$totalCal", fontSize = 36.sp, fontWeight = FontWeight.ExtraBold, color = ForestGreen)
                                Text("kcal burned", fontSize = 12.sp, color = ForestGreen.copy(0.7f))
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("${activities.size}", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = LeafGreen)
                                Text("workouts", fontSize = 12.sp, color = LeafGreen.copy(0.7f))
                            }
                        }
                    }
                }
            }
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    SmallInfoCard("Steps",    "8,240", "👟", CardBlue,   Modifier.weight(1f))
                    SmallInfoCard("Distance", "6.2km", "📍", CardYellow, Modifier.weight(1f))
                    SmallInfoCard("Active",   "2h 15m","⏱️", CardPink,   Modifier.weight(1f))
                }
            }
            item { SectionHeader("Workout Log", Modifier.fillMaxWidth(), "See All") {
                onNavigate(
                    Screen.Home
                )
            }
            }
            items(activities) { act ->
                Card(Modifier.fillMaxWidth(), RoundedCornerShape(16.dp), CardDefaults.cardColors(White), CardDefaults.cardElevation(1.dp)) {
                    Row(Modifier.padding(14.dp).fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(44.dp).background(act.color, CircleShape), Alignment.Center) { Text(act.icon, fontSize = 22.sp) }
                            Column {
                                Text(act.name, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Charcoal)
                                Text(act.duration, fontSize = 11.sp, color = GrayLight)
                            }
                        }
                        Text("${act.calories} kcal", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = LeafGreen)
                    }
                }
            }
        }
    }

    if (showAdd) {
        AlertDialog(
            onDismissRequest = { showAdd = false },
            shape = RoundedCornerShape(24.dp),
            title = { Text("Log Activity", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    AppTextField(newAct, { newAct = it }, "Activity (e.g. Swimming)")
                    AppTextField(newDur, { newDur = it }, "Duration (e.g. 30 min)")
                    AppTextField(newCal, { newCal = it }, "Calories burned", keyType = KeyboardType.Number)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newAct.isNotBlank()) {
                            activities.add(ActivityRecord(newAct, newDur.ifBlank { "—" }, newCal.toIntOrNull() ?: 0, "🏅", CardGreen))
                            showAdd = false; newAct = ""; newDur = ""; newCal = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(ForestGreen), shape = RoundedCornerShape(12.dp)
                ) { Text("Save") }
            },
            dismissButton = { TextButton({ showAdd = false }) { Text("Cancel") } }
        )
    }
}