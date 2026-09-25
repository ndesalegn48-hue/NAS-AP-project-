package com.example.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.Translation
import com.example.ui.navigation.Screen
import com.example.ui.theme.GimbiGreenContainer
import com.example.ui.theme.GimbiGreenPrimary

sealed class BottomNavItem(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val titleKey: (AppLanguage) -> String,
    val testTag: String
) {
    data object Discover : BottomNavItem(
        route = Screen.Discover.route,
        selectedIcon = Icons.Filled.Explore,
        unselectedIcon = Icons.Outlined.Explore,
        titleKey = { Translation.tabDiscover(it) },
        testTag = "nav_tab_discover"
    )

    data object Categories : BottomNavItem(
        route = Screen.Categories.route,
        selectedIcon = Icons.Filled.Category,
        unselectedIcon = Icons.Outlined.Category,
        titleKey = { Translation.tabCategories(it) },
        testTag = "nav_tab_categories"
    )

    data object PostAd : BottomNavItem(
        route = Screen.PostAd.route,
        selectedIcon = Icons.Filled.AddCircle,
        unselectedIcon = Icons.Outlined.AddCircleOutline,
        titleKey = { Translation.tabPostAd(it) },
        testTag = "nav_tab_post_ad"
    )

    data object Dashboard : BottomNavItem(
        route = Screen.Dashboard.route,
        selectedIcon = Icons.Filled.BarChart,
        unselectedIcon = Icons.Outlined.BarChart,
        titleKey = { Translation.tabDashboard(it) },
        testTag = "nav_tab_dashboard"
    )

    data object Admin : BottomNavItem(
        route = Screen.Admin.route,
        selectedIcon = Icons.Filled.AdminPanelSettings,
        unselectedIcon = Icons.Outlined.AdminPanelSettings,
        titleKey = { Translation.tabAdmin(it) },
        testTag = "nav_tab_admin"
    )
}

@Composable
fun GimbiBottomBar(
    currentRoute: String?,
    currentLanguage: AppLanguage,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem.Discover,
        BottomNavItem.Categories,
        BottomNavItem.PostAd,
        BottomNavItem.Dashboard,
        BottomNavItem.Admin
    )

    NavigationBar(
        modifier = modifier,
        windowInsets = WindowInsets.navigationBars,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.route) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.titleKey(currentLanguage)
                    )
                },
                label = {
                    Text(
                        text = item.titleKey(currentLanguage),
                        fontSize = 11.sp,
                        maxLines = 1
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = GimbiGreenContainer,
                    selectedIconColor = GimbiGreenPrimary,
                    selectedTextColor = GimbiGreenPrimary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag(item.testTag)
            )
        }
    }
}
