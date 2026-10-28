package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.AspectRatioPreset
import com.example.model.MediaClipItem
import com.example.model.MediaType
import com.example.model.ScreenDestination
import com.example.model.TextAnimType
import com.example.model.TextOverlayItem
import com.example.model.TextPosition
import com.example.model.VideoFilterType
import com.example.ui.theme.AudioTrackColor
import com.example.ui.theme.ElectricBlue
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
import com.example.ui.theme.TextTrackColor
import com.example.util.Localization
import com.example.viewmodel.CreateXViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoEditorScreen(
    viewModel: CreateXViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.language.collectAsState()
    val activeProject by viewModel.activeProject.collectAsState()
    val aspectRatio by viewModel.aspectRatio.collectAsState()
    val activeFilter by viewModel.activeFilter.collectAsState()
    val clips by viewModel.clips.collectAsState()
    val selectedClipId by viewModel.selectedClipId.collectAsState()
    val overlays by viewModel.textOverlays.collectAsState()
    val audioTrack by viewModel.audioTrack.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val playheadMs by viewModel.currentPlayheadMs.collectAsState()
    val totalDurationMs = viewModel.getTotalDurationMs()

    var showFilterSheet by remember { mutableStateOf(false) }
    var showTextDialog by remember { mutableStateOf(false) }
    var showTrimDialog by remember { mutableStateOf(false) }
    var showSpeedDialog by remember { mutableStateOf(false) }
    var showRatioDialog by remember { mutableStateOf(false) }

    val mediaPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val clipName = "Imported Media (${clips.size + 1})"
            viewModel.addMediaClip(
                uri = uri.toString(),
                name = clipName,
                type = MediaType.VIDEO,
                durationMs = 6000L
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(StudioDarkBg)
    ) {
        EditorHeader(
            projectTitle = activeProject.title,
            aspectRatio = aspectRatio,
            language = language,
            onRatioClick = { showRatioDialog = true },
            onExportClick = { viewModel.navigateTo(ScreenDestination.EXPORT) }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            VideoPlayerViewport(
                aspectRatio = aspectRatio,
                activeFilter = activeFilter,
                playheadMs = playheadMs,
                totalDurationMs = totalDurationMs,
                clips = clips,
                overlays = overlays,
                isPlaying = isPlaying
            )
        }

        PlayerControlsBar(
            isPlaying = isPlaying,
            playheadMs = playheadMs,
            totalDurationMs = totalDurationMs,
            onPlayPause = { viewModel.togglePlayPause() },
            onStepBack = { viewModel.stepBackward() },
            onStepForward = { viewModel.stepForward() },
            onSeek = { viewModel.seekTo(it) }
        )

        MultiTrackTimeline(
            clips = clips,
            overlays = overlays,
            audioTrack = audioTrack,
            selectedClipId = selectedClipId,
            onSelectClip = { viewModel.selectClip(it) },
            onAddMediaClick = {
                mediaPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
                )
            }
        )

        EditorToolbar(
            language = language,
            onSplit = { viewModel.splitClipAtPlayhead() },
            onTrim = { showTrimDialog = true },
            onFilter = { showFilterSheet = true },
            onAddText = { showTextDialog = true },
            onSpeed = { showSpeedDialog = true },
            onDelete = { viewModel.deleteSelectedClip() },
            onAddVoice = { viewModel.navigateTo(ScreenDestination.AI_VOICE) },
            onMoveLeft = { selectedClipId?.let { viewModel.moveClipLeft(it) } },
            onMoveRight = { selectedClipId?.let { viewModel.moveClipRight(it) } }
        )
    }

    if (showFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFilterSheet = false },
            containerColor = StudioSurfaceElevated
        ) {
            FilterSelectorContent(
                activeFilter = activeFilter,
                onSelectFilter = {
                    viewModel.applyFilter(it)
                    showFilterSheet = false
                }
            )
        }
    }

    if (showTextDialog) {
        AddTextDialog(
            language = language,
            onDismiss = { showTextDialog = false },
            onAddText = { text, anim, pos, color ->
                viewModel.addTextOverlay(text, anim, pos, color)
                showTextDialog = false
            }
        )
    }

    if (showTrimDialog) {
        val selectedClip = clips.find { it.id == selectedClipId } ?: clips.firstOrNull()
        if (selectedClip != null) {
            TrimClipDialog(
                clip = selectedClip,
                language = language,
                onDismiss = { showTrimDialog = false },
                onSaveTrim = { start, end ->
                    viewModel.trimSelectedClip(start, end)
                    showTrimDialog = false
                }
            )
        }
    }

    if (showSpeedDialog) {
        SpeedDialog(
            language = language,
            onDismiss = { showSpeedDialog = false },
            onSelectSpeed = { speed ->
                viewModel.setClipSpeed(speed)
                showSpeedDialog = false
            }
        )
    }

    if (showRatioDialog) {
        AspectRatioDialog(
            activeRatio = aspectRatio,
            onDismiss = { showRatioDialog = false },
            onSelectRatio = { ratio ->
                viewModel.setAspectRatio(ratio)
                showRatioDialog = false
            }
        )
    }
}

@Composable
fun EditorHeader(
    projectTitle: String,
    aspectRatio: AspectRatioPreset,
    language: AppLanguage,
    onRatioClick: () -> Unit,
    onExportClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(StudioSurfaceDark)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = projectTitle,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .testTag("editor_ratio_btn")
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF261D4C))
                    .clickable { onRatioClick() }
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text(
                    text = aspectRatio.label.split(" ").first(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonPink
                )
            }

            Box(
                modifier = Modifier
                    .testTag("editor_export_btn")
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        Brush.horizontalGradient(listOf(NeonPink, NeonPurple))
                    )
                    .clickable { onExportClick() }
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = Localization.get("export_video", language),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun VideoPlayerViewport(
    aspectRatio: AspectRatioPreset,
    activeFilter: VideoFilterType,
    playheadMs: Long,
    totalDurationMs: Long,
    clips: List<MediaClipItem>,
    overlays: List<TextOverlayItem>,
    isPlaying: Boolean
) {
    var accumulated = 0L
    var currentClip = clips.firstOrNull()
    for (clip in clips) {
        val len = clip.trimEndMs - clip.trimStartMs
        if (playheadMs in accumulated until (accumulated + len)) {
            currentClip = clip
            break
        }
        accumulated += len
    }

    val filterBrush = when (activeFilter) {
        VideoFilterType.CYBERPUNK -> Brush.linearGradient(
            listOf(Color(0xFF0F051D), Color(0xFF2A0845), Color(0xFF0D324D))
        )
        VideoFilterType.CINEMATIC -> Brush.linearGradient(
            listOf(Color(0xFF00223E), Color(0xFF1D3557), Color(0xFFE65C00))
        )
        VideoFilterType.RETRO_VHS -> Brush.linearGradient(
            listOf(Color(0xFF3B1D28), Color(0xFF281C38), Color(0xFF4A3E1B))
        )
        VideoFilterType.DRAMATIC_BW -> Brush.linearGradient(
            listOf(Color(0xFF111111), Color(0xFF2B2B2B), Color(0xFF444444))
        )
        VideoFilterType.SUNSET -> Brush.linearGradient(
            listOf(Color(0xFF421245), Color(0xFF8B2635), Color(0xFFE56B55))
        )
        VideoFilterType.GLITCH -> Brush.linearGradient(
            listOf(Color(0xFF00FFCC), Color(0xFFFF0055), Color(0xFF003366))
        )
        VideoFilterType.NORMAL -> Brush.linearGradient(
            listOf(Color(0xFF16112C), Color(0xFF241442), Color(0xFF1A1A3A))
        )
    }

    Box(
        modifier = Modifier
            .fillMaxHeight(0.95f)
            .aspectRatio(aspectRatio.ratio)
            .clip(RoundedCornerShape(12.dp))
            .background(filterBrush)
            .border(1.dp, StudioCardBorder, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = "Playing",
                    tint = NeonCyan,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = currentClip?.name ?: "Video Clip",
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )

            Text(
                text = "[${activeFilter.displayNameEn}]",
                color = NeonPink,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        overlays.forEach { overlay ->
            val isVisible = playheadMs in overlay.startMs..(overlay.startMs + overlay.durationMs)
            if (isVisible) {
                val alignModifier = when (overlay.position) {
                    TextPosition.TOP -> Modifier.align(Alignment.TopCenter).padding(top = 24.dp)
                    TextPosition.CENTER -> Modifier.align(Alignment.Center)
                    TextPosition.BOTTOM -> Modifier.align(Alignment.BottomCenter).padding(bottom = 32.dp)
                }

                Box(
                    modifier = alignModifier
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(overlay.bgColorHex))
                        .border(
                            1.dp,
                            if (overlay.isNeon) NeonCyan else Color.Transparent,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = overlay.text,
                        color = Color(overlay.textColorHex),
                        fontSize = overlay.fontSizeSp.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun PlayerControlsBar(
    isPlaying: Boolean,
    playheadMs: Long,
    totalDurationMs: Long,
    onPlayPause: () -> Unit,
    onStepBack: () -> Unit,
    onStepForward: () -> Unit,
    onSeek: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(StudioSurfaceDark)
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val formatTime = { ms: Long ->
                val totalSec = ms / 1000
                val min = totalSec / 60
                val sec = totalSec % 60
                val tenths = (ms % 1000) / 100
                String.format("%02d:%02d.%d", min, sec, tenths)
            }

            Text(
                text = formatTime(playheadMs),
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = NeonCyan
            )

            Slider(
                value = playheadMs.toFloat(),
                onValueChange = { onSeek(it.toLong()) },
                valueRange = 0f..totalDurationMs.coerceAtLeast(1000L).toFloat(),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp)
                    .testTag("timeline_scrub_slider"),
                colors = SliderDefaults.colors(
                    thumbColor = NeonPink,
                    activeTrackColor = NeonPink,
                    inactiveTrackColor = StudioCardBorder
                )
            )

            Text(
                text = formatTime(totalDurationMs),
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                color = TextMuted
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onStepBack,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.FastRewind,
                    contentDescription = "-1s",
                    tint = TextSecondary
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Box(
                modifier = Modifier
                    .testTag("player_play_pause_btn")
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(listOf(NeonPink, NeonPurple))
                    )
                    .clickable { onPlayPause() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            IconButton(
                onClick = onStepForward,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.FastForward,
                    contentDescription = "+1s",
                    tint = TextSecondary
                )
            }
        }
    }
}

@Composable
fun MultiTrackTimeline(
    clips: List<MediaClipItem>,
    overlays: List<TextOverlayItem>,
    audioTrack: com.example.model.AudioTrackItem?,
    selectedClipId: String?,
    onSelectClip: (String) -> Unit,
    onAddMediaClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .background(Color(0xFF0C091A))
            .border(1.dp, StudioCardBorder)
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .horizontalScroll(scrollState)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            clips.forEach { clip ->
                val isSelected = clip.id == selectedClipId
                val clipWidth = (((clip.trimEndMs - clip.trimStartMs) / 50).coerceIn(80L, 260L)).toInt().dp

                Box(
                    modifier = Modifier
                        .testTag("timeline_clip_${clip.id}")
                        .width(clipWidth)
                        .fillMaxHeight(0.85f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(clip.colorHex))
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) NeonCyan else Color.White.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .clickable { onSelectClip(clip.id) }
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Column {
                        Text(
                            text = clip.name,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1
                        )
                        Text(
                            text = "${(clip.trimEndMs - clip.trimStartMs) / 1000}s",
                            fontSize = 9.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .testTag("timeline_add_clip_btn")
                    .size(40.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(StudioSurfaceElevated)
                    .border(1.dp, StudioCardBorder, RoundedCornerShape(6.dp))
                    .clickable { onAddMediaClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Clip",
                    tint = NeonCyan,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(28.dp)
                .horizontalScroll(scrollState)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            overlays.forEach { overlay ->
                val textWidth = (overlay.durationMs / 60).coerceIn(60L, 200L).toInt().dp
                Box(
                    modifier = Modifier
                        .width(textWidth)
                        .fillMaxHeight(0.8f)
                        .clip(RoundedCornerShape(4.dp))
                        .background(TextTrackColor)
                        .padding(horizontal = 6.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "T: ${overlay.text}",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        maxLines = 1
                    )
                }
            }
        }

        if (audioTrack != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
                    .horizontalScroll(scrollState)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .fillMaxHeight(0.8f)
                        .clip(RoundedCornerShape(4.dp))
                        .background(AudioTrackColor)
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Voice",
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = audioTrack.title,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EditorToolbar(
    language: AppLanguage,
    onSplit: () -> Unit,
    onTrim: () -> Unit,
    onFilter: () -> Unit,
    onAddText: () -> Unit,
    onSpeed: () -> Unit,
    onDelete: () -> Unit,
    onAddVoice: () -> Unit,
    onMoveLeft: () -> Unit,
    onMoveRight: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(StudioSurfaceDark)
            .padding(horizontal = 6.dp, vertical = 6.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ToolbarActionButton(
            label = Localization.get("split_clip", language),
            icon = Icons.Default.ContentCut,
            accentColor = NeonPink,
            onClick = onSplit
        )
        ToolbarActionButton(
            label = Localization.get("trim_clip", language),
            icon = Icons.Default.AspectRatio,
            accentColor = NeonCyan,
            onClick = onTrim
        )
        ToolbarActionButton(
            label = "Move ←",
            icon = Icons.AutoMirrored.Filled.ArrowBack,
            accentColor = NeonCyan,
            onClick = onMoveLeft
        )
        ToolbarActionButton(
            label = "Move →",
            icon = Icons.AutoMirrored.Filled.ArrowForward,
            accentColor = NeonCyan,
            onClick = onMoveRight
        )
        ToolbarActionButton(
            label = Localization.get("filters", language),
            icon = Icons.Default.Filter,
            accentColor = NeonPurple,
            onClick = onFilter
        )
        ToolbarActionButton(
            label = Localization.get("add_text", language),
            icon = Icons.Default.TextFields,
            accentColor = RadiantGold,
            onClick = onAddText
        )
        ToolbarActionButton(
            label = Localization.get("add_audio", language),
            icon = Icons.Default.MusicNote,
            accentColor = Color(0xFF00E5FF),
            onClick = onAddVoice
        )
        ToolbarActionButton(
            label = Localization.get("speed", language),
            icon = Icons.Default.Speed,
            accentColor = ElectricBlue,
            onClick = onSpeed
        )
        ToolbarActionButton(
            label = Localization.get("delete_clip", language),
            icon = Icons.Default.Delete,
            accentColor = Color(0xFFEF4444),
            onClick = onDelete
        )
    }
}

@Composable
fun ToolbarActionButton(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = accentColor,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = TextSecondary
        )
    }
}

@Composable
fun FilterSelectorContent(
    activeFilter: VideoFilterType,
    onSelectFilter: (VideoFilterType) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Video Filters & Color Grading",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            VideoFilterType.entries.forEach { filter ->
                val isSelected = filter == activeFilter
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(StudioSurfaceDark)
                        .border(
                            2.dp,
                            if (isSelected) NeonPink else StudioCardBorder,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { onSelectFilter(filter) }
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = filter.displayNameEn,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) NeonPink else TextPrimary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = filter.displayNameHi,
                            fontSize = 10.sp,
                            color = TextMuted,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun AddTextDialog(
    language: AppLanguage,
    onDismiss: () -> Unit,
    onAddText: (String, TextAnimType, TextPosition, Long) -> Unit
) {
    var text by remember { mutableStateOf("") }
    var selectedAnim by remember { mutableStateOf(TextAnimType.POP) }
    var selectedPos by remember { mutableStateOf(TextPosition.BOTTOM) }
    var selectedColor by remember { mutableStateOf(0xFFFFEA00L) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = StudioSurfaceElevated,
        title = {
            Text(
                text = if (language == AppLanguage.HINDI) "टेक्स्ट ओवरले जोड़ें" else "Add Text Overlay",
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Text content") },
                    placeholder = { Text("e.g. Subscribe! / सब्सक्राइब करें") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Position", fontSize = 12.sp, color = TextSecondary)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextPosition.entries.forEach { pos ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (selectedPos == pos) NeonCyan else StudioSurfaceDark)
                                .clickable { selectedPos = pos }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = pos.name,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selectedPos == pos) StudioDarkBg else TextPrimary
                            )
                        }
                    }
                }

                Text("Animation Style", fontSize = 12.sp, color = TextSecondary)
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextAnimType.entries.forEach { anim ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (selectedAnim == anim) NeonPink else StudioSurfaceDark)
                                .clickable { selectedAnim = anim }
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = anim.name,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selectedAnim == anim) Color.White else TextPrimary
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (text.isNotBlank()) {
                        onAddText(text, selectedAnim, selectedPos, selectedColor)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = NeonPink)
            ) {
                Text("Add to Video")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextSecondary)
            }
        }
    )
}

@Composable
fun TrimClipDialog(
    clip: MediaClipItem,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSaveTrim: (Long, Long) -> Unit
) {
    var startVal by remember { mutableFloatStateOf(clip.trimStartMs.toFloat()) }
    var endVal by remember { mutableFloatStateOf(clip.trimEndMs.toFloat()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = StudioSurfaceElevated,
        title = {
            Text("Trim Clip: ${clip.name}", color = TextPrimary, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Start: ${(startVal / 1000).toInt()}s  -  End: ${(endVal / 1000).toInt()}s",
                    fontSize = 13.sp,
                    color = NeonCyan
                )

                Text("Start Trim", fontSize = 11.sp, color = TextMuted)
                Slider(
                    value = startVal,
                    onValueChange = { startVal = it.coerceAtMost(endVal - 500f) },
                    valueRange = 0f..clip.durationMs.toFloat()
                )

                Text("End Trim", fontSize = 11.sp, color = TextMuted)
                Slider(
                    value = endVal,
                    onValueChange = { endVal = it.coerceAtLeast(startVal + 500f) },
                    valueRange = 0f..clip.durationMs.toFloat()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSaveTrim(startVal.toLong(), endVal.toLong()) },
                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
            ) {
                Text("Apply Trim", color = StudioDarkBg, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextSecondary)
            }
        }
    )
}

@Composable
fun SpeedDialog(
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSelectSpeed: (Float) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = StudioSurfaceElevated,
        title = { Text("Playback Speed", color = TextPrimary) },
        text = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf(0.5f, 1.0f, 1.5f, 2.0f).forEach { spd ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(StudioSurfaceDark)
                            .clickable { onSelectSpeed(spd) }
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = "${spd}x",
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan
                        )
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Close", color = TextSecondary) }
        }
    )
}

@Composable
fun AspectRatioDialog(
    activeRatio: AspectRatioPreset,
    onDismiss: () -> Unit,
    onSelectRatio: (AspectRatioPreset) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = StudioSurfaceElevated,
        title = { Text("Canvas Aspect Ratio", color = TextPrimary) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                AspectRatioPreset.entries.forEach { ratio ->
                    val isSel = ratio == activeRatio
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSel) NeonPurple.copy(alpha = 0.3f) else StudioSurfaceDark)
                            .border(1.dp, if (isSel) NeonPink else StudioCardBorder, RoundedCornerShape(8.dp))
                            .clickable { onSelectRatio(ratio) }
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(ratio.label, fontWeight = FontWeight.Bold, color = TextPrimary)
                                Text(ratio.iconDesc, fontSize = 11.sp, color = TextSecondary)
                            }
                            if (isSel) {
                                Icon(Icons.Default.Check, contentDescription = "Active", tint = NeonPink)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Close", color = TextSecondary) }
        }
    )
}
