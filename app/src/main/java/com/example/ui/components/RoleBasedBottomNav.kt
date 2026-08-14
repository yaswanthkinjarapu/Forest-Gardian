package com.example.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.data.models.UserRole

data class NavItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun RoleBasedBottomNav(
    currentRole: UserRole,
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val items = when (currentRole) {
        UserRole.FOREST_OFFICER -> listOf(
            NavItem("dashboard", "Dashboard", Icons.Default.Home),
            NavItem("patrol", "Patrol", Icons.Default.DirectionsWalk),
            NavItem("incidents", "Incidents", Icons.Default.ReportProblem),
            NavItem("map", "Map", Icons.Default.Map),
            NavItem("chat", "Chat", Icons.Default.Chat)
        )
        UserRole.RANGE_OFFICER -> listOf(
            NavItem("dashboard", "Range", Icons.Default.Dashboard),
            NavItem("patrol", "Officers", Icons.Default.Group),
            NavItem("incidents", "Approvals", Icons.Default.AssignmentTurnedIn),
            NavItem("ai", "AI Advisor", Icons.Default.AutoAwesome),
            NavItem("chat", "Comms", Icons.Default.Forum)
        )
        UserRole.ADMIN -> listOf(
            NavItem("dashboard", "System", Icons.Default.AdminPanelSettings),
            NavItem("admin", "Divisions", Icons.Default.AccountTree),
            NavItem("analytics", "Analytics", Icons.Default.BarChart),
            NavItem("ai", "AI Hub", Icons.Default.AutoAwesome),
            NavItem("reports", "Reports", Icons.Default.Description)
        )
    }

    NavigationBar(
        containerColor = Color(0xFF0F2D1E),
        contentColor = Color.White,
        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(item.route) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF0F2D1E),
                    selectedTextColor = Color(0xFF2ECC71),
                    indicatorColor = Color(0xFF2ECC71),
                    unselectedIconColor = Color(0xFF81C784),
                    unselectedTextColor = Color(0xFF81C784)
                )
            )
        }
    }
}
