package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.AppDatabase
import com.example.data.GimbiRepository
import com.example.ui.components.GimbiBottomBar
import com.example.ui.components.GimbiTopBar
import com.example.ui.navigation.Screen
import com.example.ui.screens.AdDetailScreen
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.CategoriesScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PostAdScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.GimbiViewModel
import com.example.ui.viewmodel.GimbiViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: GimbiViewModel by viewModels {
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = GimbiRepository(
            advertisementDao = database.advertisementDao(),
            noticeDao = database.noticeDao(),
            inquiryDao = database.inquiryDao()
        )
        GimbiViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                GimbiApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun GimbiApp(viewModel: GimbiViewModel) {
    val currentLang by viewModel.currentLanguage.collectAsStateWithLifecycle()
    var currentRoute by remember { mutableStateOf(Screen.Discover.route) }
    var selectedAdId by remember { mutableStateOf<Long?>(null) }

    Scaffold(
        topBar = {
            if (selectedAdId == null) {
                GimbiTopBar(
                    currentLanguage = currentLang,
                    onLanguageSelected = { viewModel.setLanguage(it) }
                )
            }
        },
        bottomBar = {
            if (selectedAdId == null) {
                GimbiBottomBar(
                    currentRoute = currentRoute,
                    currentLanguage = currentLang,
                    onNavigate = { route ->
                        currentRoute = route
                    }
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (selectedAdId != null) {
                AdDetailScreen(
                    adId = selectedAdId!!,
                    viewModel = viewModel,
                    onBack = { selectedAdId = null }
                )
            } else {
                AnimatedContent(
                    targetState = currentRoute,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "ScreenTransition"
                ) { route ->
                    when (route) {
                        Screen.Discover.route -> {
                            HomeScreen(
                                viewModel = viewModel,
                                onAdClick = { adId -> selectedAdId = adId },
                                onNavigateToCategories = { currentRoute = Screen.Categories.route },
                                onNavigateToPostAd = { currentRoute = Screen.PostAd.route }
                            )
                        }
                        Screen.Categories.route -> {
                            BackHandler { currentRoute = Screen.Discover.route }
                            CategoriesScreen(
                                viewModel = viewModel,
                                onCategorySelected = {
                                    currentRoute = Screen.Discover.route
                                },
                                onAdClick = { adId -> selectedAdId = adId }
                            )
                        }
                        Screen.PostAd.route -> {
                            BackHandler { currentRoute = Screen.Discover.route }
                            PostAdScreen(
                                viewModel = viewModel,
                                onAdCreated = { adId ->
                                    selectedAdId = adId
                                }
                            )
                        }
                        Screen.Dashboard.route -> {
                            BackHandler { currentRoute = Screen.Discover.route }
                            DashboardScreen(
                                viewModel = viewModel,
                                onNavigateToPostAd = { currentRoute = Screen.PostAd.route },
                                onAdClick = { adId -> selectedAdId = adId }
                            )
                        }
                        Screen.Admin.route -> {
                            BackHandler { currentRoute = Screen.Discover.route }
                            AdminScreen(viewModel = viewModel)
                        }
                        else -> {
                            HomeScreen(
                                viewModel = viewModel,
                                onAdClick = { adId -> selectedAdId = adId },
                                onNavigateToCategories = { currentRoute = Screen.Categories.route },
                                onNavigateToPostAd = { currentRoute = Screen.PostAd.route }
                            )
                        }
                    }
                }
            }
        }
    }
}
