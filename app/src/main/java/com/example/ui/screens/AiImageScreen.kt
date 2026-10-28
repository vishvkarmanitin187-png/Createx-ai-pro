package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Movie
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.MediaType
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
import com.example.viewmodel.CreateXViewModel

@Composable
fun AiImageScreen(
    viewModel: CreateXViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.language.collectAsState()
    val isGenerating by viewModel.isGenerating.collectAsState()
    var prompt by remember { mutableStateOf("Hyper-detailed cyberpunk creator studio glowing with purple neon lighting and holographic screens") }
    var negativePrompt by remember { mutableStateOf("blurry, low quality, distorted, watermark") }
    var selectedStyle by remember { mutableStateOf("Cyberpunk") }
    var selectedRatio by remember { mutableStateOf("9:16") }
    var activePreviewColor by remember { mutableStateOf(0xFF7928CA) }

    val styles = listOf("Cyberpunk", "Anime Aesthetics", "Cinematic 3D", "Photorealistic", "Fantasy Epic")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(StudioDarkBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = Localization.get("tool_ai_image", language),
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            color = TextPrimary
        )

        // Prompt input
        OutlinedTextField(
            value = prompt,
            onValueChange = { prompt = it },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("ai_image_prompt_input"),
            label = { Text("Visual Prompt") },
            placeholder = { Text(Localization.get("prompt_placeholder", language)) },
            minLines = 2,
            maxLines = 4
        )

        // Negative Prompt
        OutlinedTextField(
            value = negativePrompt,
            onValueChange = { negativePrompt = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Negative Prompt (Optional)") },
            placeholder = { Text("Elements to avoid: blurry, text, distortion...") },
            singleLine = true
        )

        // Honest AI Provider Setup Banner
        if (!viewModel.aiService.isConfigured) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF231738))
                    .border(1.dp, RadiantGold, RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        text = "⚙️ Cloud AI Provider Setup",
                        fontWeight = FontWeight.Bold,
                        color = RadiantGold,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Configure GEMINI_API_KEY in the AI Studio Secrets panel for live generative cloud rendering. Built-in creative canvas engine is active.",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        lineHeight = 15.sp
                    )
                }
            }
        }

        // Style chips
        Column {
            Text("Artistic Style", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                styles.forEach { style ->
                    val isSel = selectedStyle == style
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSel) NeonPurple else StudioSurfaceDark)
                            .border(1.dp, if (isSel) NeonPink else StudioCardBorder, RoundedCornerShape(8.dp))
                            .clickable {
                                selectedStyle = style
                                activePreviewColor = when (style) {
                                    "Cyberpunk" -> 0xFF7928CA
                                    "Anime Aesthetics" -> 0xFFFF007F
                                    "Cinematic 3D" -> 0xFF0070F3
                                    "Photorealistic" -> 0xFF00F5D4
                                    else -> 0xFFFFB703
                                }
                            }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = style,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSel) Color.White else TextSecondary
                        )
                    }
                }
            }
        }

        // Aspect Ratio
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            listOf("9:16" to "Reels", "16:9" to "YouTube", "1:1" to "Square").forEach { (ratio, desc) ->
                val isSel = selectedRatio == ratio
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSel) NeonCyan.copy(alpha = 0.2f) else StudioSurfaceDark)
                        .border(1.dp, if (isSel) NeonCyan else StudioCardBorder, RoundedCornerShape(8.dp))
                        .clickable { selectedRatio = ratio }
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(ratio, fontWeight = FontWeight.Bold, color = if (isSel) NeonCyan else TextPrimary)
                        Text(desc, fontSize = 10.sp, color = TextMuted)
                    }
                }
            }
        }

        // Generate Button
        Button(
            onClick = {
                viewModel.generateAiImage(prompt, selectedStyle, selectedRatio)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("ai_image_generate_btn"),
            enabled = !isGenerating && prompt.isNotBlank(),
            colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (isGenerating) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Synthesizing Visual Art...")
            } else {
                Icon(Icons.Default.AutoAwesome, contentDescription = "AI")
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = Localization.get("generate_btn", language),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        // Preview Canvas Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(if (selectedRatio == "9:16") 9f / 16f else if (selectedRatio == "16:9") 16f / 9f else 1f)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.radialGradient(
                        listOf(Color(activePreviewColor), StudioDarkBg, Color(0xFF070412))
                    )
                )
                .border(1.dp, NeonCyan, RoundedCornerShape(16.dp))
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = "Visual",
                    tint = NeonCyan,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$selectedStyle Art",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp
                )
                Text(
                    text = prompt.take(60) + "...",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    maxLines = 2
                )
            }
        }

        // Add to Project Button
        Button(
            onClick = {
                viewModel.addMediaClip(
                    uri = null,
                    name = "AI Art ($selectedStyle)",
                    type = MediaType.IMAGE,
                    durationMs = 5000L
                )
                viewModel.navigateTo(ScreenDestination.EDITOR)
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("add_ai_image_to_timeline_btn"),
            colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(Icons.Default.Movie, contentDescription = "Editor", tint = StudioDarkBg)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = Localization.get("save_to_project", language),
                color = StudioDarkBg,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
