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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import com.example.model.MediaType
import com.example.model.ScreenDestination
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.StudioCardBorder
import com.example.ui.theme.StudioDarkBg
import com.example.ui.theme.StudioSurfaceDark
import com.example.ui.theme.StudioSurfaceElevated
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.util.Localization
import com.example.viewmodel.CreateXViewModel

@Composable
fun BackgroundRemoverScreen(
    viewModel: CreateXViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.language.collectAsState()
    var isProcessing by remember { mutableStateOf(false) }
    var selectedBackdrop by remember { mutableStateOf("Cyber Studio") }

    val backdrops = listOf("Cyber Studio", "Chroma Green", "Neon Sunset", "Transparent PNG")

    val bgBrush = when (selectedBackdrop) {
        "Chroma Green" -> Brush.verticalGradient(listOf(Color(0xFF00FF00), Color(0xFF00AA00)))
        "Neon Sunset" -> Brush.linearGradient(listOf(Color(0xFFFF007F), Color(0xFFFFB703)))
        "Transparent PNG" -> Brush.linearGradient(listOf(Color(0xFF333333), Color(0xFF111111)))
        else -> Brush.radialGradient(listOf(Color(0xFF2A0845), Color(0xFF070412)))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(StudioDarkBg)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = Localization.get("tool_bg_remover", language),
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            color = TextPrimary
        )

        // Cutout Visual Preview Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(bgBrush)
                .border(1.dp, NeonCyan, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                        .border(2.dp, NeonPink, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Subject",
                        tint = Color.White,
                        modifier = Modifier.size(54.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Isolated Subject (AI Cutout)",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 14.sp
                )
                Text(
                    text = "Backdrop: $selectedBackdrop",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 11.sp
                )
            }
        }

        // Replacement Backdrop Selector
        Column {
            Text("Select Virtual Backdrop", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                backdrops.forEach { bd ->
                    val isSel = selectedBackdrop == bd
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSel) NeonCyan else StudioSurfaceDark)
                            .clickable { selectedBackdrop = bd }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = bd,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSel) StudioDarkBg else TextPrimary
                        )
                    }
                }
            }
        }

        // Add to Timeline Button
        Button(
            onClick = {
                viewModel.addMediaClip(
                    uri = null,
                    name = "AI Cutout ($selectedBackdrop)",
                    type = MediaType.IMAGE,
                    durationMs = 5000L
                )
                viewModel.navigateTo(ScreenDestination.EDITOR)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("apply_cutout_btn"),
            colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(Icons.Default.ContentCut, contentDescription = "Cutout", tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Insert Cutout Clip into Video", fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}
