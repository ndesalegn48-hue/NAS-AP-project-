package com.example.ui.navigation

sealed class Screen(val route: String) {
    data object Discover : Screen("discover")
    data object Categories : Screen("categories")
    data object PostAd : Screen("post_ad")
    data object Dashboard : Screen("dashboard")
    data object Admin : Screen("admin")
    data object AdDetail : Screen("ad_detail/{adId}") {
        fun createRoute(adId: Long): String = "ad_detail/$adId"
    }
}
