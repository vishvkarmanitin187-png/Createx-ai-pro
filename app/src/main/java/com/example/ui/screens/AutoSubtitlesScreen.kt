package com.example.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ClosedCaption
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.ScreenDestination
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.RadiantGold
import com.example.ui.theme.StudioCardBorder
import com.example.ui.theme.StudioDarkBg
import com.example.ui.theme.StudioSurfaceDark
import com.example.ui.theme.StudioSurfaceElevated
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.util.Localization
import com.example.viewmodel.CreateXViewModel

@Composable
fun AutoSubtitlesScreen(
    viewModel: CreateXViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.language.collectAsState()
    val isGenerating by viewModel.isGenerating.collectAsState()

    var speechScript by remember {
        mutableStateOf(
            if (language == AppLanguage.HINDI)
                "यह वीडियो आपको सिखाएगा कि कैसे AI की मदद से आप अपने रील्स को 10 गुना तेजी से वायरल बना सकते हैं।"
            else
                "This video will show you how to use AI to edit high retention viral short form videos effortlessly."
        )
    }

    var selectedStyle by remember { mutableStateOf("Hormozi Pop Highlight") }

    val styles = listOf(
        "Hormozi Pop Highlight" to RadiantGold,
        "Cyberpunk Neon Cyan" to NeonCyan,
        "Desi Gold Saffron" to Color(0xFFFF9933),
        "Clean Minimal White" to Color.White
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(StudioDarkBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = Localization.get("tool_auto_subtitles", language),
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            color = TextPrimary
        )

        // Text input to transcribe
        OutlinedTextField(
            value = speechScript,
            onValueChange = { speechScript = it },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("auto_subtitles_input"),
            label = { Text("Speech Audio Script") },
            placeholder = { Text("Enter video spoken audio to generate timed subtitles...") },
            minLines = 3,
            maxLines = 6
        )

        // Subtitle Style Preset Selector
        Column {
            Text("Trending Subtitle Styles", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(8.dp))
            styles.forEach { (style, color) ->
                val isSel = selectedStyle == style
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSel) StudioSurfaceElevated else StudioSurfaceDark)
                        .border(1.dp, if (isSel) color else StudioCardBorder, RoundedCornerShape(10.dp))
                        .clickable { selectedStyle = style }
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(style, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 13.sp)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.Black.copy(alpha = 0.8f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("SAMPLE", color = color, fontWeight = FontWeight.Black, fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Generate Subtitles Button
        Button(
            onClick = {
                val isHindi = language == AppLanguage.HINDI || speechScript.any { it.code in 0x0900..0x097F }
                viewModel.generateAutoSubtitles(speechScript, isHindi)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("generate_subtitles_btn"),
            enabled = !isGenerating && speechScript.isNotBlank(),
            colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
            shape = RoundedCornerShape(10.dp)
        ) {
            if (isGenerating) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Aligning Word Timestamps...")
            } else {
                Icon(Icons.Default.AutoAwesome, contentDescription = "Subtitles")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Generate Timed Subtitles", fontWeight = FontWeight.Bold)
            }
        }

        // Live Subtitle Preview Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF0F0A24))
                .border(1.dp, StudioCardBorder, RoundedCornerShape(14.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "PREVIEW",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonPink
                )
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.Black.copy(alpha = 0.9f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = speechScript.take(45) + "...",
                        color = styles.find { it.first == selectedStyle }?.second ?: RadiantGold,
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        var srtMessage by remember { mutableStateOf<String?>(null) }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    val srt = viewModel.exportSrt()
                    srtMessage = "SRT format generated & ready for export!"
                },
                modifier = Modifier
                    .weight(1f)
                    .testTag("export_srt_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Export SRT File", fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = {
                    viewModel.navigateTo(ScreenDestination.EDITOR)
                },
                modifier = Modifier
                    .weight(1f)
                    .testTag("apply_subtitles_to_timeline_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Burn into Video", color = StudioDarkBg, fontWeight = FontWeight.Bold)
            }
        }

        if (srtMessage != null) {
            Text(
                text = srtMessage!!,
                color = NeonCyan,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
