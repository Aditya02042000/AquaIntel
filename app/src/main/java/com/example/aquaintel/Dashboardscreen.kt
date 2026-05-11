package com.example.aquaintel

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.StrokeCap
import com.example.aquaintel.*
import com.example.aquaintel.AppBottomNav
import com.example.aquaintel.HealthMetricData
import com.example.aquaintel.MiniSparkline
import com.example.aquaintel.User
import com.example.aquaintel.ui.components.SectionHeader

import com.example.aquaintel.ui.theme.CardBlue
import com.example.aquaintel.ui.theme.CardGreen
import com.example.aquaintel.ui.theme.CardPink
import com.example.aquaintel.ui.theme.CardYellow
import com.example.aquaintel.ui.theme.Charcoal
import com.example.aquaintel.ui.theme.Cream
import com.example.aquaintel.ui.theme.CreamDark
import com.example.aquaintel.ui.theme.ForestGreen
import com.example.aquaintel.ui.theme.GoldYellow
import com.example.aquaintel.ui.theme.GrayLight
import com.example.aquaintel.ui.theme.GrayMid
import com.example.aquaintel.ui.theme.LeafGreen
import com.example.aquaintel.ui.theme.MintGreen
import com.example.aquaintel.ui.theme.SageGreen
import com.example.aquaintel.ui.theme.SoftOrange
import com.example.aquaintel.ui.theme.White

// ═══════════════════════════════════════════════════════════════════════════════
//  4. USER DASHBOARD
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
fun DashboardScreen(user: User, onNavigate: (Screen) -> Unit, onLogout: () -> Unit) {

    val metrics = listOf(
        HealthMetricData(
            "Heart Rate",
            user.heartRate,
            "bpm",
            "❤️",
            CardPink,
            listOf(0.5f, 0.65f, 0.55f, 0.72f, 0.68f, 0.8f, 0.72f)
        ),
        HealthMetricData("Blood Pressure",  user.bloodPressure,"mmHg", "🩸", CardYellow, listOf(0.62f,0.65f,0.7f,0.6f,0.68f,0.72f,0.7f)),
        HealthMetricData("Oxygen Level",    user.oxygenLevel,    "%",    "💨", CardBlue,   listOf(0.9f,0.92f,0.95f,0.93f,0.97f,0.97f,0.98f)),
        HealthMetricData("Sleep",           user.sleepHours,   "hrs",  "😴", CardGreen,  listOf(0.52f,0.82f,0.22f,0.70f,0.65f,0.55f,0.64f))
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            AppBottomNav(
                selectedIndex = 0,
                onHome    = { onNavigate(Screen.Home) },
                onChat    = { onNavigate(Screen.Chat) },
                onReport  = { onNavigate(Screen.Report) },
                onProfile = { onNavigate(Screen.Profile) }
            )
        }
    ) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {

            item {
                Box(
                    Modifier.fillMaxWidth()
                        .background(Brush.verticalGradient(listOf(ForestGreen, LeafGreen)))
                        .padding(horizontal = 22.dp, vertical = 28.dp)
                ) {
                    Column {
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.Top) {
                            Column {
                                Text("Good Morning,", fontSize = 14.sp, color = White.copy(0.75f))
                                val displayName = if (user.name.isNotBlank()) user.name.split(" ")[0] else "User"
                                Text("$displayName 👋", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = White)
                                Spacer(Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Icon(Icons.Default.Star, null, Modifier.size(14.dp), tint = GoldYellow)
                                    Text(
                                        if (user.isPro) "Pro Member" else "Free Member",
                                        fontSize = 12.sp, color = White.copy(0.85f)
                                    )
                                }
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Box(Modifier.size(38.dp).background(White.copy(0.2f), CircleShape).clickable { onNavigate(Screen.Profile) }, Alignment.Center) {
                                    Text(user.name.first().toString(), fontSize = 17.sp, fontWeight = FontWeight.Bold, color = White)
                                }
                                Box(Modifier.size(38.dp).background(White.copy(0.2f), CircleShape).clickable { onLogout() }, Alignment.Center) {
                                    Icon(Icons.AutoMirrored.Filled.ExitToApp, null, tint = White, modifier = Modifier.size(18.dp))
                                }
                            }
                        }

                        Spacer(Modifier.height(20.dp))

                        Row(
                            Modifier.fillMaxWidth()
                                .background(White.copy(0.15f), RoundedCornerShape(16.dp))
                                .padding(16.dp),
                            Arrangement.SpaceBetween, Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Health Score", fontSize = 13.sp, color = White.copy(0.8f))
                                Text("${user.healthScore}", fontSize = 36.sp, fontWeight = FontWeight.ExtraBold, color = White)
                                Text("Above Average 🎉", fontSize = 11.sp, color = GoldYellow)
                            }
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(72.dp)) {
                                var animScore by remember { mutableFloatStateOf(0f) }
                                LaunchedEffect(Unit) { animScore = user.healthScore / 100f }
                                val sc by animateFloatAsState(animScore, tween(1400, easing = FastOutSlowInEasing), label = "sc")
                                Canvas(Modifier.fillMaxSize()) {
                                    val sw = 6.dp.toPx(); val ins = sw/2
                                    val sz = Size(size.width-sw, size.height-sw)
                                    drawArc(White.copy(0.25f), -90f, 360f, false, Offset(ins,ins), sz, style = Stroke(sw, cap = StrokeCap.Round))
                                    drawArc(GoldYellow, -90f, sc*360f, false, Offset(ins,ins), sz, style = Stroke(sw, cap = StrokeCap.Round))
                                }
                                Text("${(sc * 100).toInt()}", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = White)
                            }
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(20.dp))
                SectionHeader(
                    "Today's Summary",
                    Modifier.padding(horizontal = 22.dp),
                    "See All"
                ) { onNavigate(Screen.Home) }
                Spacer(Modifier.height(10.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 22.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(listOf(
                        Triple("🔥","1,840 kcal","Burned"),
                        Triple("👟","8,240","Steps"),
                        Triple("💧","2.1L","Water"),
                        Triple("🧘","35 min","Active"),
                        Triple("😌",user.heartRate,"Resting HR")
                    )) { (emoji, val_, lab) ->
                        Card(
                            Modifier.width(100.dp), RoundedCornerShape(16.dp),
                            CardDefaults.cardColors(White), CardDefaults.cardElevation(1.dp)
                        ) {
                            Column(Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(emoji, fontSize = 22.sp)
                                Spacer(Modifier.height(4.dp))
                                Text(val_, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center)
                                Text(lab,  fontSize = 10.sp, color = GrayLight, textAlign = TextAlign.Center)
                            }
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(20.dp))
                SectionHeader(
                    "Quick Actions",
                    Modifier.padding(horizontal = 22.dp),
                    "See All"
                ) { onNavigate(Screen.Home) }
                Spacer(Modifier.height(10.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 22.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(listOf(
                        Triple("🩺", "BP Check",  Screen.BloodPressure),
                        Triple("❤️", "Heart Rate", Screen.HeartRate),
                        Triple("😴", "Sleep",      Screen.Sleep),
                        Triple("🥗", "Nutrition",  Screen.Nutrition),
                        Triple("🏃", "Activity",   Screen.Activity),
                        Triple("📊", "Report",     Screen.Report),
                        Triple("📄", "Upload",     Screen.ReportUpload)
                    )) { (emoji, label, dest) ->
                        Box(
                            Modifier
                                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(14.dp))
                                .border(1.dp, CreamDark, RoundedCornerShape(14.dp))
                                .clickable { onNavigate(dest) }
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(emoji, fontSize = 16.sp)
                                Text(label, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(20.dp))
                SectionHeader("Health Metrics", Modifier.padding(horizontal = 22.dp), "See All") { onNavigate(Screen.Home) }
                Spacer(Modifier.height(10.dp))
            }
            items(metrics.chunked(2)) { row ->
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 22.dp).padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    row.forEach { m ->
                        Card(
                            Modifier.weight(1f).clickable { onNavigate(Screen.BloodPressure) },
                            RoundedCornerShape(18.dp), CardDefaults.cardColors(m.cardColor), CardDefaults.cardElevation(0.dp)
                        ) {
                            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text(m.icon, fontSize = 16.sp)
                                    Text(m.title, fontSize = 11.sp, color = GrayMid, maxLines = 1)
                                }

                                MiniSparkline(m.trend, Modifier.fillMaxWidth().height(30.dp))
                                Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                    Text(m.value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Charcoal)
                                    Text(m.unit,  fontSize = 10.sp, color = GrayMid, modifier = Modifier.padding(bottom = 2.dp))
                                }
                            }
                        }
                    }
                    if (row.size == 1) Spacer(Modifier.weight(1f))
                }
            }

            item {
                Spacer(Modifier.height(8.dp))
                SectionHeader("Recent Activity", Modifier.padding(horizontal = 22.dp), "View All") { onNavigate(Screen.Activity) }
                Spacer(Modifier.height(10.dp))
                Column(
                    Modifier.padding(horizontal = 22.dp).clip(RoundedCornerShape(20.dp)).background(White)
                ) {
                    listOf(
                        Triple("🏃 Morning Run",      "30 min · 320 kcal", LeafGreen),
                        Triple("🧘 Yoga Session",     "45 min · 180 kcal", SageGreen),
                        Triple("🚶 Evening Walk",     "20 min · 120 kcal", SoftOrange)
                    ).forEachIndexed { i, (act, detail, col) ->
                        Row(
                            Modifier.fillMaxWidth().padding(16.dp),
                            Arrangement.SpaceBetween, Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Box(Modifier.size(40.dp).background(col.copy(0.15f), CircleShape), Alignment.Center) {
                                    Text(act.take(2), fontSize = 18.sp)
                                }
                                Column {
                                    Text(act.drop(3), fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Charcoal)
                                    Text(detail, fontSize = 11.sp, color = GrayMid)
                                }
                            }
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = GrayLight, modifier = Modifier.size(18.dp))
                        }
                        if (i < 2) HorizontalDivider(color = Cream,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }

            item {
                Spacer(Modifier.height(18.dp))
                SectionHeader("Sleep Overview", Modifier.padding(horizontal = 22.dp), "See All") { onNavigate(Screen.Sleep) }
                Spacer(Modifier.height(10.dp))
                Card(
                    Modifier.padding(horizontal = 22.dp).fillMaxWidth().clickable { onNavigate(Screen.Sleep) },
                    RoundedCornerShape(20.dp), CardDefaults.cardColors(MintGreen), CardDefaults.cardElevation(0.dp)
                ) {
                    Row(Modifier.padding(18.dp), Arrangement.spacedBy(14.dp), Alignment.CenterVertically) {
                        Text("😴", fontSize = 44.sp)
                        Column(Modifier.weight(1f)) {
                            Text("${user.sleepHours}h avg this week", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
                            Text("Health metrics analyzed by AI", fontSize = 12.sp, color = ForestGreen.copy(0.75f))
                            Spacer(Modifier.height(6.dp))
                            Text("Tap to view AI suggestions →", fontSize = 11.sp, color = LeafGreen)
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(18.dp))
                Card(
                    Modifier.padding(horizontal = 22.dp).fillMaxWidth().clickable { onNavigate(Screen.Chat) },
                    RoundedCornerShape(20.dp), CardDefaults.cardColors(ForestGreen), CardDefaults.cardElevation(4.dp)
                ) {
                    Row(Modifier.padding(20.dp), Arrangement.spacedBy(14.dp), Alignment.CenterVertically) {
                        Text("🤖", fontSize = 36.sp)
                        Column(Modifier.weight(1f)) {
                            Text("AI Health Assistant", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = White)
                            Text("Ask anything about your vitals", fontSize = 12.sp, color = White.copy(0.75f))
                        }
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = White, modifier = Modifier.size(20.dp))
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}