package com.example.aquaintel

import androidx.compose.ui.graphics.Color



data class User(
    val name: String     = "",
    val email: String    = "",
    val age: String      = "0",
    val height: String   = "0",
    val weight: String   = "0",
    val gender: String   = "Not Specified",
    val isPro: Boolean   = false,
    val healthScore: Int = 0,
    val joinDate: String = "",
    val heartRate: String = "0",
    val bloodPressure: String = "0/0",
    val oxygenLevel: String = "0",
    val sleepHours: String = "0"
)

data class HealthMetricData(
    val title: String,
    val value: String,
    val unit: String,
    val icon: String,
    val cardColor: Color,
    val trend: List<Float> = listOf(0.4f, 0.6f, 0.5f, 0.7f, 0.65f, 0.8f, 0.75f)
)

data class SleepRecord(
    val date: String,
    val hours: Float,
    val label: String,
    val badgeColor: Color,
    val suggestions: String
)

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val time: String
)

data class NutritionEntry(
    val meal: String,
    val calories: Int,
    val type: String,
    val icon: String
)

data class ActivityRecord(
    val name: String,
    val duration: String,
    val calories: Int,
    val icon: String,
    val color: Color
)