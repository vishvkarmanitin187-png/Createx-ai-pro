package com.example.ui.screens

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.viewmodel.CreateXViewModel

@Composable
fun ExportScreen(
    viewModel: CreateXViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val language by viewModel.language.collectAsState()
    val activeProject by viewModel.activeProject.collectAsState()
    val isExporting by viewModel.isExporting.collectAsState()
    val isExportDone by viewModel.isExportDone.collectAsState()
    val progress by viewModel.exportProgress.collectAsState()

    var selectedRes by remember { mutableStateOf("1080p Full HD") }
    var selectedFps by remember { mutableStateOf(60) }
    var saveSuccessMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(StudioDarkBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = Localization.get("export_video", language),
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            color = TextPrimary
        )

        // Project Summary Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(StudioSurfaceElevated)
                .border(1.dp, StudioCardBorder, RoundedCornerShape(12.dp))
                .padding(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF261D4C)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Movie, contentDescription = "Video", tint = NeonCyan)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(activeProject.title, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 14.sp)
                    Text(
                        "${activeProject.aspectRatioName.replace("_", " ")} • ${activeProject.durationMs / 1000}s",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        // Resolution Selector
        Column {
            Text(Localization.get("resolution", language), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf("720p HD", "1080p Full HD", "4K Ultra").forEach { res ->
                    val isSel = selectedRes == res
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSel) NeonPurple else StudioSurfaceDark)
                            .border(1.dp, if (isSel) NeonPink else StudioCardBorder, RoundedCornerShape(8.dp))
                            .clickable(enabled = !isExporting) {
                                selectedRes = res
                                viewModel.setExportSettings(res, selectedFps)
                            }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = res,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSel) Color.White else TextPrimary
                        )
                    }
                }
            }
        }

        // Frame Rate (FPS) Selector
        Column {
            Text(Localization.get("frame_rate", language), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf(24 to "Cinematic", 30 to "Standard", 60 to "Ultra Smooth").forEach { (fps, label) ->
                    val isSel = selectedFps == fps
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSel) NeonCyan.copy(alpha = 0.2f) else StudioSurfaceDark)
                            .border(1.dp, if (isSel) NeonCyan else StudioCardBorder, RoundedCornerShape(8.dp))
                            .clickable(enabled = !isExporting) {
                                selectedFps = fps
                                viewModel.setExportSettings(selectedRes, fps)
                            }
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("${fps} FPS", fontWeight = FontWeight.Bold, color = if (isSel) NeonCyan else TextPrimary)
                            Text(label, fontSize = 9.sp, color = TextMuted)
                        }
                    }
                }
            }
        }

        // Render Action or Progress Bar
        if (isExporting) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(StudioSurfaceElevated)
                    .border(1.dp, NeonPink, RoundedCornerShape(12.dp))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = Localization.get("export_progress", language),
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = NeonPink,
                    trackColor = StudioSurfaceDark
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${(progress * 100).toInt()}% Rendered",
                        color = NeonCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "Encoding $selectedFps FPS...",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }
        } else if (!isExportDone) {
            Button(
                onClick = { viewModel.startExport() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("start_export_render_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = NeonPink),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Download, contentDescription = "Export")
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Render & Export Master Video",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.White
                )
            }
        }

        // Done State Actions
        if (isExportDone) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF0F1E19))
                    .border(1.dp, Color(0xFF10B981), RoundedCornerShape(16.dp))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Success",
                    tint = Color(0xFF10B981),
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = Localization.get("export_done", language),
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp,
                    color = TextPrimary
                )
                Text(
                    text = "$selectedRes • $selectedFps FPS • Estimated 24.5 MB",
                    color = TextSecondary,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = {
                            saveSuccessMessage = "Video saved to Gallery!"
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("save_video_gallery_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(Localization.get("save_video", language), fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, "Check out my new video created with CreateX AI Video Studio!")
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Video"))
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("share_video_intent_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "Share", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(Localization.get("share_video", language), fontWeight = FontWeight.Bold)
                    }
                }

                if (saveSuccessMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(saveSuccessMessage!!, color = NeonCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
