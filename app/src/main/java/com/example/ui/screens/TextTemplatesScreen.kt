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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AiTemplateItem
import com.example.model.AppLanguage
import com.example.model.ScreenDestination
import com.example.model.TextAnimType
import com.example.model.TextPosition
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.StudioCardBorder
import com.example.ui.theme.StudioDarkBg
import com.example.ui.theme.StudioSurfaceElevated
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.util.Localization
import com.example.viewmodel.CreateXViewModel

@Composable
fun TextTemplatesScreen(
    viewModel: CreateXViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.language.collectAsState()

    val templates = listOf(
        AiTemplateItem(
            id = "t_hook",
            titleEn = "Viral Hook Pop",
            titleHi = "वायरल हुक पॉप",
            category = "Shorts & Reels",
            previewText = "WAIT! LOOK AT THIS ⚡",
            animType = TextAnimType.POP,
            primaryColorHex = 0xFFFFEA00L,
            secondaryColorHex = 0xFF000000L
        ),
        AiTemplateItem(
            id = "t_neon",
            titleEn = "Cyberpunk Neon Glow",
            titleHi = "साइबरपंक नियॉन ग्लो",
            category = "Sci-Fi & Gaming",
            previewText = "CYBERNETIC FUTURE",
            animType = TextAnimType.NEON_GLOW,
            primaryColorHex = 0xFF00F5D4L,
            secondaryColorHex = 0xFF7928CAL
        ),
        AiTemplateItem(
            id = "t_hindi",
            titleEn = "Hindi Desi Quote",
            titleHi = "हिंदी शायरी व कोट्स",
            category = "Inspirational",
            previewText = "सपनों को सच कर दिखाओ 🌟",
            animType = TextAnimType.FADE,
            primaryColorHex = 0xFFFF9933L,
            secondaryColorHex = 0xFF120E2EL
        ),
        AiTemplateItem(
            id = "t_podcast",
            titleEn = "Podcast Lower Third",
            titleHi = "पॉडकास्ट लोअर थर्ड",
            category = "Interviews",
            previewText = "🎙️ Host: Nitin | Episode #42",
            animType = TextAnimType.SLIDE_UP,
            primaryColorHex = 0xFFFFFFFFL,
            secondaryColorHex = 0xDD1D173CL
        ),
        AiTemplateItem(
            id = "t_bday",
            titleEn = "Birthday Celebration Pop",
            titleHi = "जन्मदिन की बधाई",
            category = "Birthday",
            previewText = "🎂 Happy Birthday Rockstar! 🎉",
            animType = TextAnimType.POP,
            primaryColorHex = 0xFFFF007FL,
            secondaryColorHex = 0xFF2A0845L
        ),
        AiTemplateItem(
            id = "t_travel",
            titleEn = "Travel Cinematic Stamp",
            titleHi = "ट्रैवल व्लॉग टाइटल",
            category = "Travel",
            previewText = "✈️ Next Stop: Tokyo, Japan 🌸",
            animType = TextAnimType.SLIDE_UP,
            primaryColorHex = 0xFF00F5D4L,
            secondaryColorHex = 0xEE090714L
        ),
        AiTemplateItem(
            id = "t_gaming",
            titleEn = "Pro Gaming Kill Feed",
            titleHi = "गेमिंग हाइलाइट टाइटल",
            category = "Gaming",
            previewText = "🏆 1v4 Insane Clutch Gameplay 🔥",
            animType = TextAnimType.NEON_GLOW,
            primaryColorHex = 0xFF3A86FFL,
            secondaryColorHex = 0xFF140D26L
        ),
        AiTemplateItem(
            id = "t_love",
            titleEn = "Romantic Melody Aesthetic",
            titleHi = "रोमांटिक लव टाइटल",
            category = "Love",
            previewText = "💖 Forever With You & Memories ✨",
            animType = TextAnimType.FADE,
            primaryColorHex = 0xFFFF4081L,
            secondaryColorHex = 0xDD2D0C3FL
        ),
        AiTemplateItem(
            id = "t_motivate",
            titleEn = "Motivational Hustle Title",
            titleHi = "प्रेरक विचार टाइटल",
            category = "Motivational",
            previewText = "⚡ Wake Up & Conquer 2026 🚀",
            animType = TextAnimType.POP,
            primaryColorHex = 0xFFFFB703L,
            secondaryColorHex = 0xFF000000L
        ),
        AiTemplateItem(
            id = "t_news",
            titleEn = "Breaking News Banner",
            titleHi = "ब्रेकिंग न्यूज़ टिकर",
            category = "Trending",
            previewText = "🔴 BREAKING: AI Video Launch",
            animType = TextAnimType.POP,
            primaryColorHex = 0xFFFF0000L,
            secondaryColorHex = 0xFFFFFFFFL
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(StudioDarkBg)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = Localization.get("tool_text_templates", language),
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = Localization.get("tool_text_templates_desc", language),
                fontSize = 12.sp,
                color = TextSecondary
            )
        }

        items(templates) { template ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("template_card_${template.id}"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = StudioSurfaceElevated),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioCardBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (language == AppLanguage.HINDI) template.titleHi else template.titleEn,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            fontSize = 14.sp
                        )
                        Text(template.category, fontSize = 11.sp, color = NeonCyan)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF0D0A1C))
                            .border(1.dp, StudioCardBorder, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(template.secondaryColorHex))
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = template.previewText,
                                color = Color(template.primaryColorHex),
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            viewModel.addTextOverlay(
                                text = template.previewText,
                                animType = template.animType,
                                position = TextPosition.BOTTOM,
                                colorHex = template.primaryColorHex,
                                bgHex = template.secondaryColorHex
                            )
                            viewModel.navigateTo(ScreenDestination.EDITOR)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = "Apply", tint = Color.White)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Apply to Timeline", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}
