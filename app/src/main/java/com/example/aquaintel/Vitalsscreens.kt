package com.example.aquaintel

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.unit.*
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import com.example.aquaintel.Screen
import com.example.aquaintel.ui.components.AddBPDialog
import com.example.aquaintel.ui.components.BPHistoryRow

import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

import com.example.aquaintel.ui.components.SectionHeader
import com.example.aquaintel.ui.components.SmallInfoCard
import com.example.aquaintel.ui.components.TopBarWithBack
import com.example.aquaintel.ui.theme.CardBlue
import com.example.aquaintel.ui.theme.CardGreen
import com.example.aquaintel.ui.theme.CardPink
import com.example.aquaintel.ui.theme.CardYellow
import com.example.aquaintel.ui.theme.Charcoal
import com.example.aquaintel.ui.theme.CoralRed
import com.example.aquaintel.ui.theme.Cream
import com.example.aquaintel.ui.theme.CreamDark
import com.example.aquaintel.ui.theme.ForestGreen
import com.example.aquaintel.ui.theme.GoldYellow
import com.example.aquaintel.ui.theme.GrayMid
import com.example.aquaintel.ui.theme.LeafGreen
import com.example.aquaintel.ui.theme.SoftOrange
import com.example.aquaintel.ui.theme.White


@Composable
fun BloodPressureScreen(onNavigate: (Screen.Home) -> Unit) {
    var systolic   by remember { mutableIntStateOf(121) }
    var diastolic  by remember { mutableIntStateOf(80) }
    var pulse      by remember { mutableIntStateOf(72) }
    var isReading  by remember { mutableStateOf(false) }
    var seconds    by remember { mutableIntStateOf(20) }
    var showDialog by remember { mutableStateOf(false) }

    val arcAnim by animateFloatAsState(if (isReading) (20f-seconds)/20f else 0f, tween(900), label = "arc")

    LaunchedEffect(isReading) {
        if (isReading) {
            seconds = 20
            while (seconds > 0) { delay(1000); seconds-- }
            isReading = false
        }
    }

    val readings = remember {
        mutableStateListOf(
            Triple(121,80,"Oct 20"), Triple(118,76,"Oct 19"),
            Triple(125,82,"Oct 18"), Triple(119,78,"Oct 17"),
            Triple(122,81,"Oct 16")
        )
    }

    Scaffold(
        containerColor = Cream,
        topBar = {
            TopBarWithBack("Blood Pressure", { onNavigate(Screen.Home) })
        }
    ) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(horizontal = 22.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Card(Modifier.fillMaxWidth(), RoundedCornerShape(24.dp), CardDefaults.cardColors(White), CardDefaults.cardElevation(3.dp)) {
                    Column(Modifier.padding(22.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("$systolic/$diastolic", fontSize = 54.sp, fontWeight = FontWeight.ExtraBold, color = Charcoal)
                        Text("SYS / DIA  mmHg", fontSize = 13.sp, color = GrayMid)

                        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(180.dp)) {
                            Canvas(Modifier.fillMaxSize()) {
                                val sw = 10.dp.toPx(); val ins = sw/2
                                val sz = Size(size.width-sw, size.height-sw)
                                drawArc(CreamDark, 135f, 270f, false, Offset(ins,ins), sz, style = Stroke(sw, cap = StrokeCap.Round))
                                if (arcAnim > 0f) drawArc(LeafGreen, 135f, 270f*arcAnim, false, Offset(ins,ins), sz, style = Stroke(sw, cap = StrokeCap.Round))
                                for (i in 0..10) {
                                    val ang = Math.toRadians((135 + i * 27).toDouble())
                                    val r1 = size.width/2 - sw - 4.dp.toPx(); val r2 = r1 - 5.dp.toPx()
                                    drawLine(CreamDark, cap = StrokeCap.Round, strokeWidth = 1.5.dp.toPx(),
                                        start = Offset(center.x + r1*cos(ang).toFloat(), center.y + r1*sin(ang).toFloat()),
                                        end   = Offset(center.x + r2*cos(ang).toFloat(), center.y + r2*sin(ang).toFloat()))
                                }
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("❤️", fontSize = 40.sp)
                                if (isReading) Text("${seconds}s", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = LeafGreen)
                            }
                        }

                        Text(if (isReading) "${seconds} sec left…" else "Tap to start measuring", fontSize = 13.sp, color = GrayMid)

                        Box(
                            Modifier.fillMaxWidth().height(54.dp).clip(RoundedCornerShape(27.dp))
                                .background(if (isReading) LeafGreen else Charcoal)
                                .clickable { if (!isReading) isReading = true },
                            Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(if (isReading) Icons.Default.Close  else Icons.Default.PlayArrow, null, tint = White, modifier = Modifier.size(20.dp))
                                Text(if (isReading) "Measuring…" else "Start Reading", color = White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }
                        }
                    }
                }
            }

            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    SmallInfoCard("Pulse",  "$pulse bpm",  "💓", CardPink,   Modifier.weight(1f))
                    SmallInfoCard("Status", "Normal",       "✅", CardGreen,  Modifier.weight(1f))
                    SmallInfoCard("Stage",  "Stage 1",      "⚠️", CardYellow, Modifier.weight(1f))
                }
            }

            item {
                OutlinedButton(
                    onClick = { showDialog = true },
                    Modifier.fillMaxWidth(), shape = RoundedCornerShape(26.dp),
                    border = BorderStroke(1.5.dp, LeafGreen)
                ) {
                    Icon(Icons.Default.Add, null, tint = LeafGreen)
                    Spacer(Modifier.width(6.dp))
                    Text("Log Manual Reading", color = LeafGreen, fontWeight = FontWeight.SemiBold)
                }
            }

            item { SectionHeader(
                "History",
                Modifier.fillMaxWidth(),
                "See All"
            ) { onNavigate(Screen.Home) }
            }
            items(readings) { (s,d,date) -> BPHistoryRow(s,d,date) }
        }
    }

    if (showDialog) {
        AddBPDialog(onDismiss = { showDialog = false }) { s,d,p ->
            systolic = s; diastolic = d; pulse = p
            readings.add(0, Triple(s,d,"Today"))
            showDialog = false
        }
    }
}



@Composable
fun HeartRateScreen(onBack: () -> Unit) {
    var bpm       by remember { mutableIntStateOf(72) }
    var isMonitor by remember { mutableStateOf(false) }
    val animBpm   by animateIntAsState(bpm, tween(400), label = "bpm")
    val inf       = rememberInfiniteTransition(label = "wave")
    val waveOff   by inf.animateFloat(0f, 1f, infiniteRepeatable(tween(1400, easing = LinearEasing)), label = "wave")

    LaunchedEffect(isMonitor) {
        if (isMonitor) {
            repeat(30) { delay(500); bpm = (60..100).random() }
            isMonitor = false
        }
    }

    val cat = when {
        bpm < 60  -> "Bradycardia"  to SoftOrange
        bpm < 100 -> "Normal"       to LeafGreen
        bpm < 120 -> "Elevated"     to GoldYellow
        else      -> "Tachycardia"  to CoralRed
    }

    Scaffold(containerColor = Cream, topBar = { TopBarWithBack("Heart Rate", onBack) }) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(horizontal = 22.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Card(Modifier.fillMaxWidth(), RoundedCornerShape(24.dp), CardDefaults.cardColors(CardPink), CardDefaults.cardElevation(0.dp)) {
                    Column(Modifier.padding(22.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text("❤️", fontSize = 56.sp)
                        Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("$animBpm", fontSize = 58.sp, fontWeight = FontWeight.ExtraBold, color = Charcoal)
                            Text("bpm", fontSize = 18.sp, color = GrayMid, modifier = Modifier.padding(bottom = 10.dp))
                        }
                        Canvas(Modifier.fillMaxWidth().height(56.dp)) {
                            val pts = listOf(0f,0f,0.1f,0f,0.25f,0f,0.3f,-0.8f,0.35f,0.9f,0.4f,-0.3f,0.45f,0f,0.55f,0f,0.6f,-0.8f,0.65f,0.9f,0.7f,-0.3f,0.75f,0f,1f,0f)
                            val path = Path()
                            val cx = size.width; val cy = size.height/2; val amp = size.height*0.4f
                            for (i in pts.indices step 2) {
                                val x = ((pts[i]+waveOff)%1f)*cx
                                val y = cy + pts[i+1]*amp
                                if (i==0) path.moveTo(x,y) else path.lineTo(x,y)
                            }
                            drawPath(path, CoralRed.copy(0.8f), style = Stroke(2.5.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
                        }

                        Box(Modifier.background(cat.second.copy(0.15f), RoundedCornerShape(20.dp)).padding(horizontal = 18.dp, vertical = 6.dp)) {
                            Text(cat.first, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = cat.second)
                        }

                        Button(
                            onClick = { isMonitor = !isMonitor },
                            Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(26.dp),
                            colors = ButtonDefaults.buttonColors(if (isMonitor) CoralRed else ForestGreen)
                        ) { Text(if (isMonitor) "Stop Monitoring" else "Start Monitoring", fontWeight = FontWeight.Bold) }
                    }
                }
            }

            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    SmallInfoCard("Resting", "62 bpm",  "💤", CardGreen,  Modifier.weight(1f))
                    SmallInfoCard("Max HR",  "185 bpm", "🏃", CardYellow, Modifier.weight(1f))
                    SmallInfoCard("Avg",     "74 bpm",  "📊", CardBlue,   Modifier.weight(1f))
                }
            }

            item {
                Card(Modifier.fillMaxWidth(), RoundedCornerShape(18.dp), CardDefaults.cardColors(White), CardDefaults.cardElevation(1.dp)) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("💡 Health Tip", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = LeafGreen)
                        Text("A normal resting heart rate for adults ranges from 60–100 BPM. Regular aerobic exercise lowers your resting rate over time, improving cardiovascular efficiency.", fontSize = 12.sp, color = GrayMid, lineHeight = 18.sp)
                    }
                }
            }
        }
    }
}