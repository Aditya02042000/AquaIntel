package com.example.aquaintel.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.aquaintel.ui.theme.CardPink
import com.example.aquaintel.ui.theme.CoralRed
import com.example.aquaintel.ui.theme.GrayLight
import com.example.aquaintel.ui.theme.RiskLow
import com.example.aquaintel.ui.theme.RiskRem
import com.example.aquaintel.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BPHistoryScreen(navController: NavController) {

    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text(
                        text = "BP History",
                        fontWeight = FontWeight.Bold
                    )
                },

                navigationIcon = {

                    IconButton(

                        onClick = {

                            navController.popBackStack()

                        }

                    ) {

                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )

                    }

                }

            )

        }

    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            BPHistoryRow(
                systolic = 118,
                diastolic = 78,
                date = "12 May 2026"
            )

            BPHistoryRow(
                systolic = 126,
                diastolic = 82,
                date = "10 May 2026"
            )

            BPHistoryRow(
                systolic = 145,
                diastolic = 95,
                date = "08 May 2026"
            )
        }
    }
}

@Composable
fun BPHistoryRow(
    systolic: Int,
    diastolic: Int,
    date: String
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),

            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = CardPink,
                            shape = CircleShape
                        ),

                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "🩸",
                        fontSize = 18.sp
                    )

                }

                Column {

                    Text(
                        text = "$systolic/$diastolic mmHg",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = date,
                        fontSize = 11.sp,
                        color = GrayLight
                    )

                }

            }

            val (category, color) = when {

                systolic < 120 -> {
                    "Normal" to RiskLow
                }

                systolic < 130 -> {
                    "Elevated" to RiskRem
                }

                else -> {
                    "Stage 1" to CoralRed
                }
            }

            Box(
                modifier = Modifier
                    .background(
                        color = color.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(
                        horizontal = 10.dp,
                        vertical = 4.dp
                    )
            ) {

                Text(
                    text = category,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )

            }

        }

    }

}