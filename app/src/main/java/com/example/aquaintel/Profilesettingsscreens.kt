package com.example.aquaintel

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.*
import com.example.aquaintel.*
import kotlinx.coroutines.launch
import com.example.aquaintel.AppBottomNav
import com.example.aquaintel.AppTextField
import com.example.aquaintel.SettingsRow
import com.example.aquaintel.SettingsSection
import com.example.aquaintel.ToggleSetting
import com.example.aquaintel.TopBarWithBack
import com.example.aquaintel.User
import com.example.aquaintel.ui.components.SettingsRow
import com.example.aquaintel.ui.theme.Charcoal
import com.example.aquaintel.ui.theme.CoralRed
import com.example.aquaintel.ui.theme.Cream
import com.example.aquaintel.ui.theme.ForestGreen
import com.example.aquaintel.ui.theme.GoldYellow
import com.example.aquaintel.ui.theme.GrayLight
import com.example.aquaintel.ui.theme.GrayMid
import com.example.aquaintel.ui.theme.LeafGreen
import com.example.aquaintel.ui.theme.MintGreen
import com.example.aquaintel.ui.theme.SageGreen
import com.example.aquaintel.ui.theme.SoftOrange
import com.example.aquaintel.ui.theme.White


@Composable
fun ProfileScreen(user: User, onUpdate: (User) -> Unit, onNavigate: (Screen) -> Unit, onBack: () -> Unit) {
    var name    by remember { mutableStateOf(user.name) }
    var age     by remember { mutableStateOf(user.age) }
    var height  by remember { mutableStateOf(user.height) }
    var weight  by remember { mutableStateOf(user.weight) }
    var editing by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { TopBarWithBack("My Profile", onBack) },
        bottomBar = {
            AppBottomNav(
                selectedIndex = 3,
                onHome    = { onNavigate(Screen.Dashboard) },
                onChat    = { onNavigate(Screen.Chat) },
                onReport  = { onNavigate(Screen.Report) },
                onProfile = {}
            )
        }
    ) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(horizontal = 22.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Card(Modifier.fillMaxWidth(), RoundedCornerShape(24.dp), CardDefaults.cardColors(MaterialTheme.colorScheme.surface), CardDefaults.cardElevation(2.dp)) {
                    Column(Modifier.padding(24.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(
                            Modifier.size(88.dp).background(Brush.radialGradient(listOf(LeafGreen, ForestGreen)), CircleShape),
                            Alignment.Center
                        ) { Text(user.name.first().toString(), fontSize = 40.sp, fontWeight = FontWeight.ExtraBold, color = White) }
                        Text(user.name, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Default.Star, null, Modifier.size(14.dp), tint = GoldYellow)
                            Text(if (user.isPro) "Pro Member" else "Free Member", fontSize = 13.sp, color = LeafGreen, fontWeight = FontWeight.SemiBold)
                        }
                        Text(user.email, fontSize = 12.sp, color = GrayLight)
                    }
                }
            }

            item {
                Card(Modifier.fillMaxWidth(), RoundedCornerShape(20.dp), CardDefaults.cardColors(MintGreen), CardDefaults.cardElevation(0.dp)) {
                    Row(Modifier.padding(18.dp).fillMaxWidth(), Arrangement.SpaceAround) {
                        listOf(Triple("88","Score","Health"), Triple("21.3","BMI","Healthy"), Triple("14d","Streak","Active")).forEach { (v,sub,lab) ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(v,   fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = ForestGreen)
                                Text(lab, fontSize = 11.sp, fontWeight = FontWeight.SemiBold,  color = ForestGreen)
                                Text(sub, fontSize = 10.sp, color = ForestGreen.copy(0.65f))
                            }
                        }
                    }
                }
            }

            item {
                Card(Modifier.fillMaxWidth(), RoundedCornerShape(20.dp), CardDefaults.cardColors(MaterialTheme.colorScheme.surface), CardDefaults.cardElevation(2.dp)) {
                    Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                            Text("Personal Details", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            TextButton(onClick = {
                                if (editing) onUpdate(user.copy(name=name, age=age, height=height, weight=weight))
                                editing = !editing
                            }) {
                                Text(if (editing) "Save ✓" else "Edit", color = LeafGreen, fontWeight = FontWeight.Bold)
                            }
                        }
                        if (editing) {
                            AppTextField(name,   { name   = it }, "Full Name")
                            AppTextField(age,    { age    = it }, "Age",       keyType = KeyboardType.Number)
                            AppTextField(height, { height = it }, "Height cm", keyType = KeyboardType.Number)
                            AppTextField(weight, { weight = it }, "Weight kg", keyType = KeyboardType.Number)
                        } else {
                            listOf(
                                Triple("👤","Name",    name),
                                Triple("🎂","Age",     "$age years"),
                                Triple("📏","Height",  "$height cm"),
                                Triple("⚖️","Weight",  "$weight kg"),
                                Triple("⚧","Gender",   user.gender)
                            ).forEach { (emoji, lbl, val_) ->
                                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                                    Text("$emoji $lbl", fontSize = 13.sp, color = GrayMid)
                                    Text(val_,          fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                                }
                            }
                        }
                    }
                }
            }

            item {
                Card(Modifier.fillMaxWidth(), RoundedCornerShape(20.dp), CardDefaults.cardColors(MaterialTheme.colorScheme.surface), CardDefaults.cardElevation(1.dp)) {
                    Column(Modifier.padding(4.dp)) {
                        listOf(
                            Triple(Icons.Default.Notifications, "Reminders & Alerts",  LeafGreen),
                            Triple(Icons.Default.Lock ,       "Privacy & Security",  SageGreen),
                            Triple(Icons.Default.Edit  ,        "Appearance",          SoftOrange),
                            Triple(Icons.Default.Info,           "Help & Support",      GoldYellow),
                            Triple(Icons.Default.Settings,       "Settings",            GrayMid)
                        ).forEachIndexed { i, (icon, label, tint) ->
                            Row(
                                Modifier.fillMaxWidth().clickable { onNavigate(Screen.Settings) }.padding(horizontal = 18.dp, vertical = 14.dp),
                                Arrangement.SpaceBetween, Alignment.CenterVertically
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Box(Modifier.size(36.dp).background(tint.copy(0.12f), CircleShape), Alignment.Center) {
                                        Icon(icon, null, tint = tint, modifier = Modifier.size(18.dp))
                                    }
                                    Text(label, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                                }
                                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = GrayLight, modifier = Modifier.size(18.dp))
                            }
                            if (i < 4) HorizontalDivider(color = Cream,
                                modifier = Modifier.padding(horizontal = 18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
//  14. SETTINGS SCREEN
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val themeManager = remember { ThemeManager(context) }
    
    val darkModeFlow = themeManager.isDarkMode.collectAsState(initial = false)
    
    var notifs    by remember { mutableStateOf(true) }
    var biometric by remember { mutableStateOf(true) }
    var dataSync  by remember { mutableStateOf(true) }

    Scaffold(containerColor = MaterialTheme.colorScheme.background, topBar = { TopBarWithBack("Settings", onBack) }) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(horizontal = 22.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                SettingsSection("Notifications") {
                    ToggleSetting("Push Notifications",   "Get health alerts",        notifs,    { notifs    = it })
                    HorizontalDivider(color = Cream,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    ToggleSetting("Daily Reminders",      "Log your vitals daily",    dataSync,  { dataSync  = it })
                }
            }
            item {
                SettingsSection("Security & Privacy") {
                    ToggleSetting("Biometric Lock",       "Fingerprint / Face ID",    biometric, { biometric = it })
                    HorizontalDivider(color = Cream,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    ToggleSetting("Data Sync",            "Sync data across devices", dataSync,  { dataSync  = it })
                }
            }
            item {
                SettingsSection("Appearance") {
                    ToggleSetting(
                        "Dark Mode",
                        "Switch to dark theme",
                        darkModeFlow.value,
                        { enabled ->
                            scope.launch {
                                themeManager.setDarkMode(enabled)
                            }
                        }
                    )
                }
            }
            item {
                SettingsSection("Data & Storage") {
                    SettingsRow("Export Health Data",   "Download as PDF / CSV")
                    HorizontalDivider(color = Cream,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    SettingsRow("Clear Cache",          "Free up storage space")
                    HorizontalDivider(color = Cream,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    SettingsRow("Delete Account",       "Permanently remove data", CoralRed)
                }
            }
            item {
                Card(Modifier.fillMaxWidth(), RoundedCornerShape(16.dp), CardDefaults.cardColors(MaterialTheme.colorScheme.surface)) {
                    Column(Modifier.padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("VitalCare v1.0.0", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = GrayMid)
                        Text("Built with ❤️ for your health", fontSize = 11.sp, color = GrayLight)
                    }
                }
            }
        }
    }
}