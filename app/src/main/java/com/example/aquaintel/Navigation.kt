package com.example.aquaintel

sealed class Screen {
    object Splash         : Screen()
    object Login          : Screen()
    object SignUp         : Screen()
    object Onboarding     : Screen()
    object Dashboard      : Screen()
    object Home           : Screen()
    object BloodPressure  : Screen()
    object HeartRate      : Screen()
    object Sleep          : Screen()
    object Nutrition      : Screen()
    object Activity       : Screen()
    object Chat           : Screen()
    object Report         : Screen()
    object ReportUpload   : Screen()
    object Profile        : Screen()
    object Settings       : Screen()
}