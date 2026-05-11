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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.*
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.StrokeCap
import com.example.aquaintel.*
import com.example.aquaintel.AppTextField
import com.example.aquaintel.NutritionEntry
import com.example.aquaintel.SleepRecord
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
import com.example.aquaintel.ui.theme.CreamDark
import com.example.aquaintel.ui.theme.ForestGreen
import com.example.aquaintel.ui.theme.GoldYellow
import com.example.aquaintel.ui.theme.GrayLight
import com.example.aquaintel.ui.theme.GrayMid
import com.example.aquaintel.ui.theme.MintGreen
import com.example.aquaintel.ui.theme.RiskBad

import com.example.aquaintel.ui.theme.RiskLow
import com.example.aquaintel.ui.theme.RiskRem
import com.example.aquaintel.ui.theme.SoftOrange
import com.example.aquaintel.ui.theme.White


@Composable
fun SleepScreen(onNavigate: (Screen.Home) -> Unit) {
    val sleepData = remember {
        mutableStateListOf(
            SleepRecord("Oct 17", 5.2f, "Low",       RiskLow, "No Suggestion"),
            SleepRecord("Oct 16", 8.2f, "REM", RiskRem, "8 AI Suggestions"),
            SleepRecord("Oct 15", 2.2f, "Insomniac", RiskBad, "78 AI Suggestions"),
            SleepRecord("Oct 14", 7.0f, "REM",       RiskRem, "3 AI Suggestions"),
            SleepRecord("Oct 13", 6.5f, "Low",       RiskLow, "No Suggestion")
        )
    }
    var showAdd by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Cream,
        topBar = {
            TopBarWithBack("Sleep Tracker", { onNavigate(Screen.Home) })
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
                Card(Modifier.fillMaxWidth(), RoundedCornerShape(22.dp), CardDefaults.cardColors(MintGreen), CardDefaults.cardElevation(0.dp)) {
                    Column(Modifier.padding(20.dp)) {
                        Text("This Week", fontSize = 12.sp, color = ForestGreen)
                        Spacer(Modifier.height(6.dp))
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                            Column {
                                Text("6.2h", fontSize = 36.sp, fontWeight = FontWeight.ExtraBold, color = ForestGreen)
                                Text("Average sleep", fontSize = 12.sp, color = ForestGreen.copy(0.7f))
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(5.dp), verticalAlignment = Alignment.Bottom) {
                                listOf(0.52f,0.82f,0.22f,0.70f,0.65f,0.55f,0.60f).forEachIndexed { i,v ->
                                    Box(Modifier.width(14.dp).height((v*64).dp).clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)).background(if (i==1) ForestGreen else ForestGreen.copy(0.35f)))
                                }
                            }
                        }
                    }
                }
            }
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    SmallInfoCard("Best", "8.2h", "🌙", CardBlue,   Modifier.weight(1f))
                    SmallInfoCard("Worst","2.2h", "😵", CardPink,   Modifier.weight(1f))
                    SmallInfoCard("Streak","3d",  "🔥", CardYellow, Modifier.weight(1f))
                }
            }
            item { SectionHeader("Sleep Log", Modifier.fillMaxWidth(), "See All") {
                onNavigate(
                    Screen.Home
                )
            }
            }
            items(sleepData) { rec ->
                Card(Modifier.fillMaxWidth(), RoundedCornerShape(16.dp), CardDefaults.cardColors(White), CardDefaults.cardElevation(1.dp)) {
                    Row(Modifier.padding(14.dp).fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(44.dp).background(Cream, CircleShape), Alignment.Center) { Text("😴", fontSize = 20.sp) }
                            Column {
                                Text(rec.date, fontSize = 11.sp, color = GrayLight)
                                Text("${rec.hours}h sleep", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                                Text(rec.suggestions, fontSize = 11.sp, color = GrayMid)
                            }
                        }
                        Box(Modifier.background(rec.badgeColor, RoundedCornerShape(20.dp)).padding(horizontal = 12.dp, vertical = 5.dp)) {
                            Text(rec.label, color = White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    if (showAdd) {
        var hours by remember { mutableStateOf("") }
        var qual  by remember { mutableStateOf("REM") }
        AlertDialog(
            onDismissRequest = { showAdd = false },
            shape = RoundedCornerShape(24.dp),
            title = { Text("Log Sleep", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    AppTextField(hours, { hours = it }, "Hours slept (e.g. 7.5)", keyType = KeyboardType.Decimal)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("REM","Low","Insomniac").forEach { q ->
                            FilterChip(selected = qual==q, onClick = { qual=q }, label = { Text(q, fontSize = 12.sp) },
                                shape = RoundedCornerShape(20.dp),
                                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = ForestGreen, selectedLabelColor = White))
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        sleepData.add(0, SleepRecord("Today", hours.toFloatOrNull() ?: 7f, qual, when(qual){"REM"->RiskRem;"Insomniac"->RiskBad;else->RiskLow}, "0 AI Suggestions"))
                        showAdd = false
                    },
                    colors = ButtonDefaults.buttonColors(ForestGreen), shape = RoundedCornerShape(12.dp)
                ) { Text("Save") }
            },
            dismissButton = { TextButton({ showAdd = false }) { Text("Cancel") } }
        )
    }
}


@Composable
fun NutritionScreen(onNavigate: (Screen.Home) -> Unit) {
    val meals = remember {
        mutableStateListOf(
            NutritionEntry("Oatmeal & Berries", 320, "Breakfast", "🥣"),
            NutritionEntry("Grilled Chicken",     480, "Lunch",     "🍗"),
            NutritionEntry("Mixed Nuts",          180, "Snack",     "🥜"),
            NutritionEntry("Salmon & Veggies",    560, "Dinner",    "🐟")
        )
    }
    val total = meals.sumOf { it.calories }
    val goal  = 2000
    var showAdd by remember { mutableStateOf(false) }
    var newMeal by remember { mutableStateOf("") }
    var newCal  by remember { mutableStateOf("") }
    var newType by remember { mutableStateOf("Snack") }

    Scaffold(
        containerColor = Cream,
        topBar = {
            TopBarWithBack("Nutrition", { onNavigate(Screen.Home) })
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
                Card(Modifier.fillMaxWidth(), RoundedCornerShape(22.dp), CardDefaults.cardColors(White), CardDefaults.cardElevation(2.dp)) {
                    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                            Column {
                                Text("Today's Calories", fontSize = 13.sp, color = GrayMid)
                                Text("$total", fontSize = 36.sp, fontWeight = FontWeight.ExtraBold, color = Charcoal)
                                Text("/ $goal kcal goal", fontSize = 12.sp, color = GrayLight)
                            }
                            Box(contentAlignment = Alignment.Center,
                                modifier = Modifier.size(80.dp)
                            ) {
                                val prog = (total.toFloat()/goal).coerceIn(0f,1f)
                                Canvas(Modifier.fillMaxSize()) {
                                    val sw = 8.dp.toPx(); val ins = sw/2
                                    val sz = Size(size.width-sw, size.height-sw)
                                    drawArc(CreamDark, -90f, 360f, false, Offset(ins,ins), sz, style = Stroke(sw, cap = StrokeCap.Round))
                                    drawArc(SoftOrange, -90f, prog*360f, false, Offset(ins,ins), sz, style = Stroke(sw, cap = StrokeCap.Round))
                                }
                                Text("${(total*100/goal)}%", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = SoftOrange)
                            }
                        }
                        Box(Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)).background(CreamDark)) {
                            Box(Modifier.fillMaxWidth((total.toFloat()/goal).coerceIn(0f,1f)).fillMaxHeight().clip(RoundedCornerShape(3.dp)).background(Brush.horizontalGradient(listOf(SoftOrange, GoldYellow))))
                        }
                    }
                }
            }

            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    SmallInfoCard("Protein", "124g",  "💪", CardBlue,   Modifier.weight(1f))
                    SmallInfoCard("Carbs",   "210g",  "🌾", CardYellow, Modifier.weight(1f))
                    SmallInfoCard("Fat",     "58g",   "🥑", CardGreen,  Modifier.weight(1f))
                }
            }

            item { SectionHeader("Meals Today", Modifier.fillMaxWidth(), "See All") {
                onNavigate(
                    Screen.Home
                )
            }
            }

            items(meals) { entry ->
                Card(Modifier.fillMaxWidth(), RoundedCornerShape(16.dp), CardDefaults.cardColors(White), CardDefaults.cardElevation(1.dp)) {
                    Row(Modifier.padding(14.dp).fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(44.dp).background(CardOrange, CircleShape), Alignment.Center) { Text(entry.icon, fontSize = 22.sp) }
                            Column {
                                Text(entry.meal, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Charcoal)
                                Text(entry.type, fontSize = 11.sp, color = GrayLight)
                            }
                        }
                        Text("${entry.calories} kcal", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = SoftOrange)
                    }
                }
            }
        }
    }

    if (showAdd) {
        AlertDialog(
            onDismissRequest = { showAdd = false },
            shape = RoundedCornerShape(24.dp),
            title = { Text("Add Meal", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    AppTextField(newMeal, { newMeal = it }, "Meal name (e.g. Salad)")
                    AppTextField(newCal,  { newCal  = it }, "Calories (e.g. 350)", keyType = KeyboardType.Number)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("Breakfast","Lunch","Dinner","Snack").forEach { t ->
                            FilterChip(selected = newType==t, onClick = { newType=t }, label = { Text(t, fontSize = 11.sp) },
                                shape = RoundedCornerShape(20.dp),
                                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = ForestGreen, selectedLabelColor = White))
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newMeal.isNotBlank()) {
                            meals.add(NutritionEntry(newMeal, newCal.toIntOrNull() ?: 200, newType, "🍽️"))
                            showAdd = false; newMeal = ""; newCal = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(ForestGreen), shape = RoundedCornerShape(12.dp)
                ) { Text("Add") }
            },
            dismissButton = { TextButton({ showAdd = false }) { Text("Cancel") } }
        )
    }
}