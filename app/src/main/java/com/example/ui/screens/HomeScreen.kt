package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ClosedCaption
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Title
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.AspectRatioPreset
import com.example.data.ProjectEntity
import com.example.model.ScreenDestination
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.RadiantGold
import com.example.ui.theme.StudioCardBorder
import com.example.ui.theme.StudioDarkBg
import com.example.ui.theme.StudioSurfaceDark
import com.example.ui.theme.StudioSurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.util.Localization

@Composable
fun HomeScreen(
    language: AppLanguage,
    projects: List<ProjectEntity>,
    onNavigate: (ScreenDestination) -> Unit,
    onOpenProject: (ProjectEntity) -> Unit,
    onNewProject: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(StudioDarkBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 48.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Hero Creation Banner
        item {
            HeroBanner(
                language = language,
                onNewProject = onNewProject,
                onOpenEditor = { onNavigate(ScreenDestination.EDITOR) }
            )
        }

        // Core AI Tools Grid Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (language == AppLanguage.HINDI) "क्रिएटिव एआई टूल्स" else "AI Creative Tools",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "8 Studio Engines",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = NeonCyan
                )
            }
        }

        // Tools 2-column Grid
        item {
            ToolsGrid(
                language = language,
                onNavigate = onNavigate
            )
        }

        // Popular Templates Section
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (language == AppLanguage.HINDI) "लोकप्रिय वीडियो टेम्पलेट्स" else "Popular Video Templates",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = Localization.get("view_all", language),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NeonCyan,
                        modifier = Modifier.clickable { onNavigate(ScreenDestination.TEXT_TEMPLATES) }
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    val popularTemplates = listOf(
                        Triple("Birthday", "🎉 Happy Birthday Glow", NeonPink),
                        Triple("Travel", "✈️ Cinematic Wanderlust", NeonCyan),
                        Triple("Gaming", "🎮 Victory Royale Montages", NeonPurple),
                        Triple("Motivational", "🔥 Unstoppable Mindset 2026", RadiantGold),
                        Triple("Hindi Quotes", "✨ जिंदगी का सच शायरी", Color(0xFFFF9933)),
                        Triple("Love & Melody", "💖 Aesthetic Romance Vibes", Color(0xFFFF4081))
                    )
                    items(popularTemplates) { (cat, title, color) ->
                        Card(
                            modifier = Modifier
                                .width(160.dp)
                                .clickable { onNavigate(ScreenDestination.TEXT_TEMPLATES) },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = StudioSurfaceElevated),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StudioCardBorder)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(cat, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = color)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(title, fontSize = 12.sp, fontWeight = FontWeight.Black, color = TextPrimary, maxLines = 2)
                                Spacer(modifier = Modifier.height(10.dp))
                                Text("Use Template →", fontSize = 11.sp, color = color, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Recent Projects Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = Localization.get("recent_projects", language),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = Localization.get("view_all", language),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NeonPink,
                    modifier = Modifier.clickable { onNavigate(ScreenDestination.MY_PROJECTS) }
                )
            }
        }

        // Recent Projects List
        if (projects.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(StudioSurfaceDark)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = Localization.get("empty_projects", language),
                        color = TextSecondary,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(projects) { project ->
                        ProjectCard(
                            project = project,
                            onClick = { onOpenProject(project) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HeroBanner(
    language: AppLanguage,
    onNewProject: () -> Unit,
    onOpenEditor: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF4A0E4E),
                        Color(0xFF1E0A3C),
                        Color(0xFF0F1A3B)
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(listOf(NeonPink, NeonCyan)),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(20.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (language == AppLanguage.HINDI) "AI से बनाएं वायरल वीडियो" else "Create Viral Videos with AI",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = Localization.get("tagline", language),
                        fontSize = 13.sp,
                        color = NeonCyan,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.sweepGradient(listOf(NeonPurple, NeonPink, NeonCyan, NeonPurple))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "AI Magic",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Primary Action Button
                Box(
                    modifier = Modifier
                        .testTag("new_project_hero_btn")
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.horizontalGradient(listOf(NeonPink, NeonPurple))
                        )
                        .clickable { onNewProject() }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "New",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = Localization.get("new_project", language),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                // Quick Open Editor Button
                Box(
                    modifier = Modifier
                        .testTag("open_editor_hero_btn")
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF261D4C))
                        .border(1.dp, StudioCardBorder, RoundedCornerShape(12.dp))
                        .clickable { onOpenEditor() }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = "Editor",
                            tint = NeonCyan,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = Localization.get("tool_video_editor", language),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ToolsGrid(
    language: AppLanguage,
    onNavigate: (ScreenDestination) -> Unit
) {
    val tools = listOf(
        ToolItemData(
            id = "editor",
            title = Localization.get("tool_video_editor", language),
            desc = Localization.get("tool_video_editor_desc", language),
            icon = Icons.Default.Movie,
            accentColor = NeonPurple,
            destination = ScreenDestination.EDITOR
        ),
        ToolItemData(
            id = "ai_video",
            title = Localization.get("tool_ai_video", language),
            desc = Localization.get("tool_ai_video_desc", language),
            icon = Icons.Default.PlayArrow,
            accentColor = NeonPink,
            destination = ScreenDestination.AI_VIDEO
        ),
        ToolItemData(
            id = "ai_image",
            title = Localization.get("tool_ai_image", language),
            desc = Localization.get("tool_ai_image_desc", language),
            icon = Icons.Default.Image,
            accentColor = NeonCyan,
            destination = ScreenDestination.AI_IMAGE
        ),
        ToolItemData(
            id = "ai_voice",
            title = Localization.get("tool_ai_voice", language),
            desc = Localization.get("tool_ai_voice_desc", language),
            icon = Icons.Default.Mic,
            accentColor = RadiantGold,
            destination = ScreenDestination.AI_VOICE
        ),
        ToolItemData(
            id = "auto_subtitles",
            title = Localization.get("tool_auto_subtitles", language),
            desc = Localization.get("tool_auto_subtitles_desc", language),
            icon = Icons.Default.ClosedCaption,
            accentColor = Color(0xFF00E5FF),
            destination = ScreenDestination.AUTO_SUBTITLES
        ),
        ToolItemData(
            id = "text_templates",
            title = Localization.get("tool_text_templates", language),
            desc = Localization.get("tool_text_templates_desc", language),
            icon = Icons.Default.Title,
            accentColor = Color(0xFFFF4081),
            destination = ScreenDestination.TEXT_TEMPLATES
        ),
        ToolItemData(
            id = "bg_remover",
            title = Localization.get("tool_bg_remover", language),
            desc = Localization.get("tool_bg_remover_desc", language),
            icon = Icons.Default.ContentCut,
            accentColor = Color(0xFF7C4DFF),
            destination = ScreenDestination.BG_REMOVER
        ),
        ToolItemData(
            id = "script_writer",
            title = Localization.get("tool_script_writer", language),
            desc = Localization.get("tool_script_writer_desc", language),
            icon = Icons.Default.EditNote,
            accentColor = Color(0xFF10B981),
            destination = ScreenDestination.AI_SCRIPT
        )
    )

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        for (i in tools.indices step 2) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ToolCard(
                    tool = tools[i],
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigate(tools[i].destination) }
                )
                if (i + 1 < tools.size) {
                    ToolCard(
                        tool = tools[i + 1],
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(tools[i + 1].destination) }
                    )
                }
            }
        }
    }
}

data class ToolItemData(
    val id: String,
    val title: String,
    val desc: String,
    val icon: ImageVector,
    val accentColor: Color,
    val destination: ScreenDestination
)

@Composable
fun ToolCard(
    tool: ToolItemData,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .testTag("tool_card_${tool.id}")
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = StudioSurfaceDark
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioCardBorder)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(tool.accentColor.copy(alpha = 0.15f))
                    .border(1.dp, tool.accentColor.copy(alpha = 0.4f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = tool.icon,
                    contentDescription = tool.title,
                    tint = tool.accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = tool.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = tool.desc,
                fontSize = 11.sp,
                color = TextSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 15.sp
            )
        }
    }
}

@Composable
fun ProjectCard(
    project: ProjectEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .testTag("project_card_${project.id}")
            .width(180.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = StudioSurfaceElevated
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioCardBorder)
    ) {
        Column {
            // Simulated Thumbnail Preview
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF2E1A47), Color(0xFF140D26), Color(0xFF0C243B))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = NeonCyan,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Aspect Ratio Pill
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.Black.copy(alpha = 0.7f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (project.aspectRatioName.contains("9_16")) "9:16" else "16:9",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonPink
                    )
                }
            }

            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = project.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${project.clipCount} clips",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                    Text(
                        text = "${project.durationMs / 1000}s",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = NeonCyan
                    )
                }
            }
        }
    }
}
