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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.util.Localization
import com.example.viewmodel.CreateXViewModel

@Composable
fun AiScriptWriterScreen(
    viewModel: CreateXViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.language.collectAsState()
    val isGenerating by viewModel.isGenerating.collectAsState()
    val scriptResult by viewModel.generatedScript.collectAsState()

    var topic by remember { mutableStateOf("How AI is changing mobile video editing forever") }
    var selectedPlatform by remember { mutableStateOf("Instagram Reels") }
    var selectedLang by remember { mutableStateOf(if (language == AppLanguage.HINDI) "Hindi" else "English") }
    var selectedTone by remember { mutableStateOf("Viral & Catchy") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(StudioDarkBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = Localization.get("tool_script_writer", language),
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            color = TextPrimary
        )

        // Topic Input
        OutlinedTextField(
            value = topic,
            onValueChange = { topic = it },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("script_topic_input"),
            label = { Text("Video Topic or Niche") },
            placeholder = { Text("e.g. 5 AI tools every creator must use") },
            minLines = 2
        )

        // Platform Chips
        Column {
            Text("Target Platform", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Instagram Reels", "YouTube Shorts", "TikTok", "YouTube Explainer", "Ad Promo").forEach { plat ->
                    val isSel = selectedPlatform == plat
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSel) NeonPink else StudioSurfaceDark)
                            .border(1.dp, if (isSel) NeonPink else StudioCardBorder, RoundedCornerShape(8.dp))
                            .clickable { selectedPlatform = plat }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(plat, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.White else TextSecondary)
                    }
                }
            }
        }

        // Language & Tone Selectors
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            // Language
            Column(modifier = Modifier.weight(1f)) {
                Text("Script Language", fontSize = 12.sp, color = TextSecondary)
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("English", "Hindi", "Hinglish").forEach { lang ->
                        val isSel = selectedLang == lang
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSel) NeonCyan else StudioSurfaceDark)
                                .clickable { selectedLang = lang }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(lang, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSel) StudioDarkBg else TextPrimary)
                        }
                    }
                }
            }
        }

        // Generate Button
        Button(
            onClick = {
                viewModel.generateScript(topic, selectedPlatform, selectedLang, selectedTone)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("generate_script_btn"),
            enabled = !isGenerating && topic.isNotBlank(),
            colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
            shape = RoundedCornerShape(10.dp)
        ) {
            if (isGenerating) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Drafting Viral Script...")
            } else {
                Icon(Icons.Default.AutoAwesome, contentDescription = "Write")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Generate AI Script", fontWeight = FontWeight.Bold)
            }
        }

        // Script Output Cards (Hook, Body, CTA)
        if (scriptResult != null) {
            val script = scriptResult!!
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ScriptSectionCard(title = "1. Viral Hook (0-3s)", text = script.hook, accentColor = NeonPink)
                ScriptSectionCard(title = "2. Retention Value (4-25s)", text = script.body, accentColor = NeonCyan)
                ScriptSectionCard(title = "3. Call to Action (CTA)", text = script.cta, accentColor = RadiantGold)

                Spacer(modifier = Modifier.height(6.dp))

                // Actions
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = {
                            viewModel.navigateTo(ScreenDestination.AI_VOICE)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("send_script_to_tts_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonPink),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Mic, contentDescription = "Voice", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Send to AI Voice", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            viewModel.navigateTo(ScreenDestination.EDITOR)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("send_script_to_editor_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Movie, contentDescription = "Editor", tint = StudioDarkBg, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Create Video", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = StudioDarkBg)
                    }
                }
            }
        }
    }
}

@Composable
fun ScriptSectionCard(title: String, text: String, accentColor: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = StudioSurfaceElevated),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioCardBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, color = accentColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text, color = TextPrimary, fontSize = 13.sp, lineHeight = 18.sp)
        }
    }
}
