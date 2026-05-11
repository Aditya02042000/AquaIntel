package com.example.aquaintel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.example.aquaintel.ChartCard
import com.example.aquaintel.StatTile
import com.example.aquaintel.TopBarWithBack
import com.example.aquaintel.User

import com.example.aquaintel.ui.theme.CardBlue
import com.example.aquaintel.ui.theme.CardGreen
import com.example.aquaintel.ui.theme.CardPink
import com.example.aquaintel.ui.theme.CardYellow
import com.example.aquaintel.ui.theme.Charcoal
import com.example.aquaintel.ui.theme.CoralRed
import com.example.aquaintel.ui.theme.Cream
import com.example.aquaintel.ui.theme.GrayMid
import com.example.aquaintel.ui.theme.RiskLow
import com.example.aquaintel.ui.theme.RiskRem
import com.example.aquaintel.ui.theme.SageGreen
import com.example.aquaintel.ui.theme.SoftOrange
import com.example.aquaintel.ui.theme.White


@Composable
fun ReportScreen(
    user: User = User("Guest"),
    onBack: () -> Unit = {}
) {
    val days   = listOf("Mon","Tue","Wed","Thu","Fri","Sat","Sun")
    val hrData = listOf(0.68f,0.72f,0.70f,0.75f,0.73f,0.80f,0.72f)
    val slData = listOf(0.52f,0.82f,0.22f,0.70f,0.65f,0.55f,0.60f)
    val bpData = listOf(0.62f,0.65f,0.70f,0.60f,0.68f,0.72f,0.70f)

    Scaffold(
        containerColor = Cream,
        topBar = { TopBarWithBack("Health Report", onBack) }
    ) { padding ->

        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(horizontal = 22.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            item {
                Text("Weekly Summary", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Charcoal)
                Text("Oct 14 – Oct 20, 2026  ·  ${user.name}", fontSize = 12.sp, color = GrayMid)
            }

            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    StatTile("Avg HR", "74 bpm", "❤️", CardPink, Modifier.weight(1f))
                    StatTile("Avg BP", "120/78", "🩸", CardYellow, Modifier.weight(1f))
                }
                Spacer(Modifier.height(10.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    StatTile("Avg Sleep", "6.4h", "😴", CardBlue, Modifier.weight(1f))
                    StatTile("Steps", "8,240", "👟", CardGreen, Modifier.weight(1f))
                }
            }

            item { ChartCard("Heart Rate (bpm)", days, hrData, CoralRed, "Average: 74 bpm") }
            item { ChartCard("Sleep Hours", days, slData, SageGreen, "Average: 6.4 hours") }
            item { ChartCard("Blood Pressure", days, bpData, SoftOrange, "Average: 120/78 mmHg") }

            item {
                Card(
                    Modifier.fillMaxWidth(),
                    RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(White)
                ) {
                    Column(
                        Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        Text("Risk Assessment", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Charcoal)

                        listOf(
                            Triple("Cardiovascular", "Low Risk", RiskLow),
                            Triple("Hypertension", "Moderate", RiskRem),
                            Triple("Sleep Disorder", "High Risk", CoralRed),
                            Triple("Diabetes", "Low Risk", RiskLow),
                            Triple("Obesity Risk", "Low Risk", RiskLow)
                        ).forEach { (label, risk, col) ->

                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(label, fontSize = 13.sp, color = Charcoal)

                                Box(
                                    Modifier
                                        .background(col.copy(0.15f), RoundedCornerShape(10.dp))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(risk, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = col)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}