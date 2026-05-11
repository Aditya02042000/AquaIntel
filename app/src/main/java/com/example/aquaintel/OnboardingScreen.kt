package com.example.aquaintel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aquaintel.ui.theme.*

@Composable
fun OnboardingScreen(
    user: User,
    onComplete: (User) -> Unit
) {
    var name by remember { mutableStateOf(user.name) }
    var age by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    
    val genders = listOf("Male", "Female", "Other")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            
            Text(
                text = "Complete Your Profile",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Charcoal
            )
            
            Text(
                text = "Help us personalize your health journey",
                fontSize = 14.sp,
                color = GrayMid
            )

            Spacer(modifier = Modifier.height(20.dp))

            AppTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = "Full Name"
            )

            AppTextField(
                value = age,
                onValueChange = { if (it.all { char -> char.isDigit() }) age = it },
                placeholder = "Age",
                keyType = androidx.compose.ui.text.input.KeyboardType.Number
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AppTextField(
                    value = height,
                    onValueChange = { if (it.all { char -> char.isDigit() }) height = it },
                    placeholder = "Height (cm)",
                    keyType = androidx.compose.ui.text.input.KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )
                AppTextField(
                    value = weight,
                    onValueChange = { if (it.all { char -> char.isDigit() }) weight = it },
                    placeholder = "Weight (kg)",
                    keyType = androidx.compose.ui.text.input.KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )
            }

            Text(
                text = "Gender",
                modifier = Modifier.align(Alignment.Start),
                fontWeight = FontWeight.Bold,
                color = Charcoal
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                genders.forEach { g ->
                    val selected = gender == g
                    Button(
                        onClick = { gender = g },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selected) ForestGreen else White,
                            contentColor = if (selected) White else Charcoal
                        ),
                        shape = RoundedCornerShape(12.dp),
                        elevation = ButtonDefaults.buttonElevation(if (selected) 4.dp else 0.dp),
                        border = if (!selected) androidx.compose.foundation.BorderStroke(1.dp, CreamDark) else null
                    ) {
                        Text(g, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    if (name.isNotBlank() && age.isNotBlank() && height.isNotBlank() && weight.isNotBlank()) {
                        val updatedUser = user.copy(
                            name = name,
                            age = age,
                            height = height,
                            weight = weight,
                            gender = gender
                        )
                        onComplete(updatedUser)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
            ) {
                Text("Start Your Journey", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
