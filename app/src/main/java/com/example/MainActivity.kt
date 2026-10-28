package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ProjectEntity
import com.example.model.AspectRatioPreset
import com.example.model.ScreenDestination
import com.example.ui.components.CreateXTopBar
import com.example.ui.screens.AiImageScreen
import com.example.ui.screens.AiScriptWriterScreen
import com.example.ui.screens.AiVideoScreen
import com.example.ui.screens.AiVoiceScreen
import com.example.ui.screens.AutoSubtitlesScreen
import com.example.ui.screens.BackgroundRemoverScreen
import com.example.ui.screens.ExportScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProjectsScreen
import com.example.ui.screens.TextTemplatesScreen
import com.example.ui.screens.VideoEditorScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPink
import com.example.ui.theme.StudioDarkBg
import com.example.ui.theme.StudioSurfaceDark
import com.example.ui.theme.StudioSurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.util.Localization
import com.example.viewmodel.CreateXViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: CreateXViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                CreateXApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun CreateXApp(viewModel: CreateXViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val language by viewModel.language.collectAsState()
    val allProjects by viewModel.allProjects.collectAsState()

    // Handle Hardware Back button
    BackHandler(enabled = currentScreen != ScreenDestination.HOME) {
        viewModel.navigateBack()
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            CreateXTopBar(
                currentScreen = currentScreen,
                language = language,
                onBackClick = { viewModel.navigateBack() },
                onLanguageToggle = { viewModel.toggleLanguage() }
            )
        },
        bottomBar = {
            CreateXBottomNav(
                currentScreen = currentScreen,
                language = language,
                onNavigate = { viewModel.navigateTo(it) }
            )
        },
        containerColor = StudioDarkBg
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(StudioDarkBg)
        ) {
            when (currentScreen) {
                ScreenDestination.HOME -> {
                    HomeScreen(
                        language = language,
                        projects = allProjects,
                        onNavigate = { viewModel.navigateTo(it) },
                        onOpenProject = { viewModel.loadProject(it) },
                        onNewProject = {
                            viewModel.createNewProject("Viral Video", AspectRatioPreset.REELS_9_16)
                        }
                    )
                }
                ScreenDestination.EDITOR -> {
                    VideoEditorScreen(viewModel = viewModel)
                }
                ScreenDestination.AI_IMAGE -> {
                    AiImageScreen(viewModel = viewModel)
                }
                ScreenDestination.AI_VIDEO -> {
                    AiVideoScreen(viewModel = viewModel)
                }
                ScreenDestination.AI_VOICE -> {
                    AiVoiceScreen(viewModel = viewModel)
                }
                ScreenDestination.AUTO_SUBTITLES -> {
                    AutoSubtitlesScreen(viewModel = viewModel)
                }
                ScreenDestination.TEXT_TEMPLATES -> {
                    TextTemplatesScreen(viewModel = viewModel)
                }
                ScreenDestination.BG_REMOVER -> {
                    BackgroundRemoverScreen(viewModel = viewModel)
                }
                ScreenDestination.AI_SCRIPT -> {
                    AiScriptWriterScreen(viewModel = viewModel)
                }
                ScreenDestination.MY_PROJECTS -> {
                    ProjectsScreen(viewModel = viewModel)
                }
                ScreenDestination.EXPORT -> {
                    ExportScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun CreateXBottomNav(
    currentScreen: ScreenDestination,
    language: com.example.model.AppLanguage,
    onNavigate: (ScreenDestination) -> Unit
) {
    NavigationBar(
        modifier = Modifier
            .navigationBarsPadding()
            .testTag("createx_bottom_nav"),
        containerColor = StudioSurfaceDark,
        tonalElevation = 6.dp
    ) {
        // Home
        NavigationBarItem(
            selected = currentScreen == ScreenDestination.HOME,
            onClick = { onNavigate(ScreenDestination.HOME) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = {
                Text(
                    text = Localization.get("nav_home", language),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = NeonPink,
                selectedTextColor = NeonPink,
                indicatorColor = StudioSurfaceElevated,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            )
        )

        // Video Editor
        NavigationBarItem(
            selected = currentScreen == ScreenDestination.EDITOR,
            onClick = { onNavigate(ScreenDestination.EDITOR) },
            icon = { Icon(Icons.Default.Movie, contentDescription = "Editor") },
            label = {
                Text(
                    text = Localization.get("nav_editor", language),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = NeonCyan,
                selectedTextColor = NeonCyan,
                indicatorColor = StudioSurfaceElevated,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            )
        )

        // AI Tools Hub
        NavigationBarItem(
            selected = currentScreen in listOf(
                ScreenDestination.AI_VIDEO,
                ScreenDestination.AI_IMAGE,
                ScreenDestination.AI_VOICE,
                ScreenDestination.AI_SCRIPT
            ),
            onClick = { onNavigate(ScreenDestination.AI_VIDEO) },
            icon = { Icon(Icons.Default.AutoAwesome, contentDescription = "AI") },
            label = {
                Text(
                    text = "AI Studio",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF9D4EDD),
                selectedTextColor = Color(0xFF9D4EDD),
                indicatorColor = StudioSurfaceElevated,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            )
        )

        // Projects
        NavigationBarItem(
            selected = currentScreen == ScreenDestination.MY_PROJECTS,
            onClick = { onNavigate(ScreenDestination.MY_PROJECTS) },
            icon = { Icon(Icons.Default.Folder, contentDescription = "Projects") },
            label = {
                Text(
                    text = Localization.get("nav_projects", language),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = NeonPink,
                selectedTextColor = NeonPink,
                indicatorColor = StudioSurfaceElevated,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            )
        )

        // Export
        NavigationBarItem(
            selected = currentScreen == ScreenDestination.EXPORT,
            onClick = { onNavigate(ScreenDestination.EXPORT) },
            icon = { Icon(Icons.Default.Download, contentDescription = "Export") },
            label = {
                Text(
                    text = Localization.get("nav_export", language),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = NeonCyan,
                selectedTextColor = NeonCyan,
                indicatorColor = StudioSurfaceElevated,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            )
        )
    }
}
