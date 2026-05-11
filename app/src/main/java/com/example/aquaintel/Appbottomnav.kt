package com.example.aquaintel

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aquaintel.ui.theme.ForestGreen
import com.example.aquaintel.ui.theme.GrayLight
import com.example.aquaintel.ui.theme.MintGreen
import com.example.aquaintel.ui.theme.White

@Composable
fun AppBottomNav(
    selectedIndex: Int,
    onHome: () -> Unit,
    onChat: () -> Unit,
    onReport: () -> Unit,
    onProfile: () -> Unit
) {
    NavigationBar(containerColor = White, tonalElevation = 8.dp) {
        listOf(
            Triple(Icons.Default.Home,        "Dashboard", 0),   // 🔁 Dashboard → Home
            Triple(Icons.Default.Favorite,    "Health",    1),   // 🔁 MonitorHeart → Favorite
            Triple(Icons.Default.Email,       "AI Chat",   2),   // 🔁 Message → Email
            Triple(Icons.AutoMirrored.Filled.List,        "Report",    3),   // 🔁 BarChart → List
            Triple(Icons.Default.Person,      "Profile",   4)    // ✅ same
        ).forEach { (icon, label, index) ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick  = {
                    when (index) {
                        0 -> onHome()
                        1 -> onHome()
                        2 -> onChat()
                        3 -> onReport()
                        4 -> onProfile()
                    }
                },
                icon  = { Icon(icon, label, modifier = Modifier.size(22.dp)) },
                label = { Text(label, fontSize = 10.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor   = ForestGreen,
                    selectedTextColor   = ForestGreen,
                    unselectedIconColor = GrayLight,
                    unselectedTextColor = GrayLight,
                    indicatorColor      = MintGreen
                )
            )
        }
    }
}