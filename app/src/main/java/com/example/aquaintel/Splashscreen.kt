package com.example.aquaintel

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.StrokeCap
import com.example.aquaintel.ui.theme.ForestGreen
import com.example.aquaintel.ui.theme.LeafGreen
import com.example.aquaintel.ui.theme.SageGreen
import com.example.aquaintel.ui.theme.White
import kotlinx.coroutines.delay




@Composable
fun SplashScreen(onFinished: () -> Unit) {
    var visible by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue   = if (visible) 1f else 0.6f,
        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMediumLow),
        label = "scale"
    )
    val alpha by animateFloatAsState(
        targetValue   = if (visible) 1f else 0f,
        animationSpec = tween(800),
        label = "alpha"
    )
    val ring  = rememberInfiniteTransition(label = "ring")
    val angle by ring.animateFloat(0f, 360f, infiniteRepeatable(tween(2000, easing = LinearEasing)), label = "a")

    LaunchedEffect(Unit) {
        visible = true
        delay(2800)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(ForestGreen, LeafGreen, SageGreen))),
        contentAlignment = Alignment.Center
    ) {
        Canvas(Modifier.fillMaxSize()) {
            drawCircle(White.copy(0.04f), size.width * 0.7f, Offset(size.width * 0.85f, size.height * 0.08f))
            drawCircle(White.copy(0.04f), size.width * 0.5f, Offset(size.width * 0.1f,  size.height * 0.88f))
            drawCircle(White.copy(0.03f), size.width * 0.3f, Offset(size.width * 0.5f,  size.height * 0.15f))
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.alpha(alpha)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(140.dp).scale(scale)) {
                Canvas(Modifier.fillMaxSize()) {
                    val sw = 4.dp.toPx(); val ins = sw / 2
                    val sz = androidx.compose.ui.geometry.Size(size.width - sw, size.height - sw)
                    drawArc(White.copy(0.25f), 0f, 360f, false, Offset(ins,ins), sz, style = Stroke(sw, cap = StrokeCap.Round))
                    drawArc(White, angle, 110f, false, Offset(ins,ins), sz, style = Stroke(sw, cap = StrokeCap.Round))
                }
                Box(
                    Modifier.size(110.dp).background(White.copy(0.18f), CircleShape),
                    Alignment.Center
                ) {
                    Canvas(Modifier.size(64.dp)) {
                        val cx = size.width / 2; val cy = size.height / 2; val r = size.width * 0.36f
                        drawRoundRect(White, Offset(cx - r*0.28f, cy - r), androidx.compose.ui.geometry.Size(r*0.56f, r*2f), androidx.compose.ui.geometry.CornerRadius(r*0.14f))
                        drawRoundRect(White, Offset(cx - r, cy - r*0.28f), androidx.compose.ui.geometry.Size(r*2f, r*0.56f), androidx.compose.ui.geometry.CornerRadius(r*0.14f))
                        val leaf = Path().apply {
                            moveTo(cx, cy - r*0.4f)
                            cubicTo(cx+r*0.7f, cy-r*0.9f, cx+r*0.9f, cy+r*0.3f, cx, cy+r*0.55f)
                            cubicTo(cx-r*0.9f, cy+r*0.3f, cx-r*0.7f, cy-r*0.9f, cx, cy-r*0.4f)
                        }
                        drawPath(leaf, SageGreen.copy(0.8f))
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            Text("AquaIntel", fontSize = 40.sp, fontWeight = FontWeight.ExtraBold, color = White, letterSpacing = 1.sp)
            Text("Your Personal Health Companion", fontSize = 14.sp, color = White.copy(0.75f), modifier = Modifier.padding(top = 6.dp))

            Spacer(Modifier.height(48.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf("❤️ Heart","😴 Sleep","🤖 AI Chat").forEach { tag ->
                    Box(
                        Modifier.background(White.copy(0.18f), androidx.compose.foundation.shape.RoundedCornerShape(20.dp)).padding(horizontal = 14.dp, vertical = 7.dp)
                    ) { Text(tag, color = White, fontSize = 12.sp, fontWeight = FontWeight.Medium) }
                }
            }

            Spacer(Modifier.height(52.dp))
            val inf = rememberInfiniteTransition(label = "dots")
            val d1 by inf.animateFloat(0.3f, 1f, infiniteRepeatable(tween(500), RepeatMode.Reverse), label = "d1")
            val d2 by inf.animateFloat(0.3f, 1f, infiniteRepeatable(tween(500, 200), RepeatMode.Reverse), label = "d2")
            val d3 by inf.animateFloat(0.3f, 1f, infiniteRepeatable(tween(500, 400), RepeatMode.Reverse), label = "d3")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(d1,d2,d3).forEach { a -> Box(Modifier.size(8.dp).alpha(a).background(White, CircleShape)) }
            }
        }
    }
}