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
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AiVoicePreset
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
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.util.Localization
import com.example.viewmodel.CreateXViewModel

@Composable
fun AiVoiceScreen(
    viewModel: CreateXViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.language.collectAsState()
    val isSpeaking by viewModel.ttsManager.isSpeaking.collectAsState()

    var voiceText by remember {
        mutableStateOf(
            if (language == AppLanguage.HINDI)
                "नमस्ते! CreateX AI में आपका स्वागत है। यहां आप मिनटों में शानदार रील्स और वीडियो बना सकते हैं।"
            else
                "Welcome to CreateX AI! Create viral videos, animated subtitles, and studio voiceovers in seconds."
        )
    }

    val voices = listOf(
        AiVoicePreset("v1", "Aarav", "आरव", "hi-IN", pitch = 1.0f, speed = 1.05f, tag = "Hindi Energetic"),
        AiVoicePreset("v2", "Priya", "प्रिया", "hi-IN", pitch = 1.15f, speed = 1.0f, tag = "Hindi Friendly"),
        AiVoicePreset("v3", "Alex", "एलेक्स", "en-US", pitch = 0.95f, speed = 1.0f, tag = "Studio Narrator"),
        AiVoicePreset("v4", "Sophia", "सोफिया", "en-US", pitch = 1.2f, speed = 1.1f, tag = "Reels Trendy"),
        AiVoicePreset("v5", "Vikram", "विक्रम", "hi-IN", pitch = 0.8f, speed = 0.95f, tag = "Deep Storyteller")
    )

    var selectedVoice by remember { mutableStateOf(voices.first()) }
    var pitch by remember { mutableFloatStateOf(1.0f) }
    var speed by remember { mutableFloatStateOf(1.0f) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(StudioDarkBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = Localization.get("tool_ai_voice", language),
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            color = TextPrimary
        )

        // Script input field
        OutlinedTextField(
            value = voiceText,
            onValueChange = { voiceText = it },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("tts_text_input"),
            label = { Text("Speech Narration Text") },
            placeholder = { Text("Type what the AI voice should say...") },
            minLines = 3,
            maxLines = 6
        )

        // Quick sample chips
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(
                "Viral Reel Hook" to "🛑 Wait! Don't scroll past this video. You need to see this now!",
                "हिंदी डायलॉग" to "हार मत मानो, क्योंकि असली जीत मेहनत और लगन से ही मिलती है।",
                "Product Promo" to "Transform your workflow today with the all-new CreateX AI studio."
            ).forEach { (label, sample) ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(StudioSurfaceDark)
                        .border(1.dp, StudioCardBorder, RoundedCornerShape(8.dp))
                        .clickable { voiceText = sample }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(label, fontSize = 11.sp, color = NeonCyan, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Voice Character Selector
        Column {
            Text("Voice Characters & Dialects", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                voices.forEach { voice ->
                    val isSel = voice.id == selectedVoice.id
                    Card(
                        modifier = Modifier
                            .width(130.dp)
                            .clickable {
                                selectedVoice = voice
                                pitch = voice.pitch
                                speed = voice.speed
                            },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = if (isSel) StudioSurfaceElevated else StudioSurfaceDark),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) NeonPink else StudioCardBorder)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(if (isSel) NeonPink else NeonPurple.copy(alpha = 0.3f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Mic, contentDescription = "Voice", tint = Color.White, modifier = Modifier.size(16.dp))
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (language == AppLanguage.HINDI) voice.nameHi else voice.nameEn,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                fontSize = 14.sp
                            )
                            Text(
                                text = voice.tag,
                                fontSize = 10.sp,
                                color = TextMuted
                            )
                        }
                    }
                }
            }
        }

        // Sliders (Pitch & Speed)
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Voice Pitch: ${String.format("%.2f", pitch)}x", fontSize = 12.sp, color = TextSecondary)
            Slider(
                value = pitch,
                onValueChange = { pitch = it },
                valueRange = 0.6f..1.6f,
                colors = SliderDefaults.colors(thumbColor = NeonCyan, activeTrackColor = NeonCyan)
            )

            Text("Speech Speed: ${String.format("%.2f", speed)}x", fontSize = 12.sp, color = TextSecondary)
            Slider(
                value = speed,
                onValueChange = { speed = it },
                valueRange = 0.5f..2.0f,
                colors = SliderDefaults.colors(thumbColor = RadiantGold, activeTrackColor = RadiantGold)
            )
        }

        // Live Audio Test Button
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(
                onClick = {
                    if (isSpeaking) {
                        viewModel.stopTts()
                    } else {
                        val isHindi = selectedVoice.langTag.startsWith("hi")
                        viewModel.playTts(voiceText, isHindi, pitch, speed)
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("test_tts_audio_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = if (isSpeaking) Color(0xFFEF4444) else NeonPurple),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = if (isSpeaking) Icons.Default.Stop else Icons.Default.PlayArrow,
                    contentDescription = "Test Audio",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isSpeaking) "Stop Audio" else "Play Audio Live",
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Attach to Project Timeline Button
        Button(
            onClick = {
                val voiceName = "${selectedVoice.nameEn} (${selectedVoice.tag})"
                viewModel.attachVoiceoverToTimeline(voiceName, 15000L)
                viewModel.navigateTo(ScreenDestination.EDITOR)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("attach_voice_to_project_btn"),
            colors = ButtonDefaults.buttonColors(containerColor = NeonPink),
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(Icons.Default.MusicNote, contentDescription = "Attach", tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Attach Voiceover to Project Timeline",
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
