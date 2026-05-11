package com.example.aquaintel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FirebaseAuth
import com.example.aquaintel.ui.theme.AquaIntelTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val themeManager = remember { ThemeManager(context) }
            val isDarkMode by themeManager.isDarkMode.collectAsState(initial = false)

            AquaIntelTheme(darkTheme = isDarkMode) {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val auth = remember { FirebaseAuth.getInstance() }
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Splash) }
    var user by remember { mutableStateOf(User()) }
    var isUserLoaded by remember { mutableStateOf(false) }
    var splashFinished by remember { mutableStateOf(false) }

    // Check login state and load data
    LaunchedEffect(Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            FirebaseUtils.getUser { loadedUser ->
                if (loadedUser != null) user = loadedUser
                isUserLoaded = true
            }
        } else {
            isUserLoaded = true
        }
    }

    // Navigation logic for Splash screen
    LaunchedEffect(isUserLoaded, splashFinished) {
        if (isUserLoaded && splashFinished && currentScreen == Screen.Splash) {
            if (auth.currentUser != null) {
                // If the user has a skeleton profile (age is "0"), send them to Onboarding
                currentScreen = if (user.age == "0" || user.age.isBlank()) Screen.Onboarding else Screen.Dashboard
            } else {
                currentScreen = Screen.Login
            }
        }
    }

    when (currentScreen) {
        Screen.Splash -> SplashScreen {
            splashFinished = true
        }
        Screen.Login -> LoginScreen(
            onLogin = { 
                // Navigate immediately to improve perceived speed
                currentScreen = Screen.Dashboard
                FirebaseUtils.getUser { loadedUser ->
                    if (loadedUser != null) user = loadedUser
                }
            },
            onSignUp = { currentScreen = Screen.SignUp }
        )
        Screen.SignUp -> SignUpScreen(
            onSignUp = { newUser ->
                user = newUser
                currentScreen = Screen.Onboarding
            },
            onBack = { currentScreen = Screen.Login }
        )
        Screen.Onboarding -> OnboardingScreen(
            user = user,
            onComplete = { updatedUser ->
                user = updatedUser
                currentScreen = Screen.Dashboard
                FirebaseUtils.saveUser(updatedUser) {
                    // saved in background
                }
            }
        )
        Screen.Dashboard -> DashboardScreen(
            user = user,
            onNavigate = { currentScreen = it },
            onLogout = { 
                auth.signOut()
                currentScreen = Screen.Login 
            }
        )
        Screen.Home -> HomeScreen(
            user = user,
            onNavigate = { currentScreen = it },
            onBack = { currentScreen = Screen.Dashboard }
        )
        Screen.BloodPressure -> BloodPressureScreen(
            onNavigate = { currentScreen = Screen.Home }
        )
        Screen.HeartRate -> HeartRateScreen(
            onBack = { currentScreen = Screen.Home }
        )
        Screen.Sleep -> SleepScreen(
            onNavigate = { currentScreen = Screen.Home }
        )
        Screen.Nutrition -> NutritionScreen(
            onNavigate = { currentScreen = Screen.Home }
        )
        Screen.Activity -> ActivityScreen(
            onNavigate = { currentScreen = Screen.Home }
        )
        Screen.Chat -> ChatScreen(
            onBack = { currentScreen = Screen.Dashboard }
        )
        Screen.Report -> ReportScreen(
            user = user,
            onBack = { currentScreen = Screen.Dashboard }
        )
        Screen.ReportUpload -> ReportUploadScreen(
            user = user,
            onReportAnalyzed = { updatedUser ->
                user = updatedUser
                FirebaseUtils.saveUser(updatedUser) {
                    currentScreen = Screen.Dashboard
                }
            },
            onBack = { currentScreen = Screen.Dashboard }
        )
        Screen.Profile -> ProfileScreen(
            user = user,
            onUpdate = { user = it },
            onNavigate = { currentScreen = it },
            onBack = { currentScreen = Screen.Dashboard }
        )
        Screen.Settings -> SettingsScreen(
            onBack = { currentScreen = Screen.Profile }
        )
    }
}
