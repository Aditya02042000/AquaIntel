package com.example.aquaintel

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.example.aquaintel.*
import com.example.aquaintel.AppBottomNav
import com.example.aquaintel.HealthMetricData
import com.example.aquaintel.MetricTile
import com.example.aquaintel.TopBarWithBack
import com.example.aquaintel.User
import com.example.aquaintel.ui.components.SectionHeader
import com.example.aquaintel.ui.theme.CardBlue
import com.example.aquaintel.ui.theme.CardGreen
import com.example.aquaintel.ui.theme.CardPink
import com.example.aquaintel.ui.theme.CardYellow
import com.example.aquaintel.ui.theme.Charcoal
import com.example.aquaintel.ui.theme.Cream
import com.example.aquaintel.ui.theme.CreamDark
import com.example.aquaintel.ui.theme.White


@Composable
fun HomeScreen(user: User, onNavigate: (Screen) -> Unit, onBack: () -> Unit) {
    val metrics = listOf(
        HealthMetricData(
            "Heart Rate",
            "72",
            "bpm",
            "❤️",
            CardPink,
            listOf(0.5f, 0.65f, 0.55f, 0.72f, 0.68f, 0.8f, 0.72f)
        ),
        HealthMetricData("Blood Pressure", "121/80","mmHg", "🩸", CardYellow, listOf(0.62f,0.65f,0.7f,0.6f,0.68f,0.72f,0.7f)),
        HealthMetricData("Oxygen Level",   "98",    "%",    "💨", CardBlue,   listOf(0.9f,0.92f,0.95f,0.93f,0.97f,0.97f,0.98f)),
        HealthMetricData("Calories",       "1,840", "kcal", "🔥", CardGreen,  listOf(0.4f,0.5f,0.6f,0.55f,0.7f,0.65f,0.75f))
    )

    val screens = listOf(
        Triple("🩺","Blood Pressure", Screen.BloodPressure),
        Triple("❤️","Heart Rate",     Screen.HeartRate),
        Triple("😴","Sleep",          Screen.Sleep),
        Triple("🥗","Nutrition",      Screen.Nutrition),
        Triple("🏃","Activity",       Screen.Activity),
        Triple("📊","Reports",        Screen.Report)
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { TopBarWithBack("Health Hub", onBack) },
        bottomBar = {
            AppBottomNav(
                selectedIndex = 1,
                onHome    = { onNavigate(Screen.Dashboard) },
                onChat    = { onNavigate(Screen.Chat) },
                onReport  = { onNavigate(Screen.Report) },
                onProfile = { onNavigate(Screen.Profile) }
            )
        }
    ) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(horizontal = 22.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(screens) { (emoji, label, dest) ->
                        Column(
                            Modifier
                                .width(80.dp)
                                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                                .border(1.dp, CreamDark, RoundedCornerShape(16.dp))
                                .clickable { onNavigate(dest) }
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(emoji, fontSize = 24.sp)
                            Text(label, fontSize = 10.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center)
                        }
                    }
                }
            }

            item { SectionHeader(
                "All Health Metrics",
                Modifier.fillMaxWidth(),
                "See All"
            ) { onNavigate(Screen.Home) }
            }

            items(metrics.chunked(2)) { row ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    row.forEach { m ->
                        MetricTile(m, Modifier.weight(1f)) {
                            onNavigate(when (m.title) {
                                "Heart Rate"     -> Screen.HeartRate
                                "Blood Pressure" -> Screen.BloodPressure
                                "Sleep"          -> Screen.Sleep
                                else             -> Screen.BloodPressure
                            })
                        }
                    }
                    if (row.size == 1) Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}