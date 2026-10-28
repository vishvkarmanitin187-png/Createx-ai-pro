package com.example.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class AppLanguage(val code: String, val label: String, val nativeLabel: String) {
    ENGLISH("en", "English", "English"),
    HINDI("hi", "Hindi", "हिन्दी")
}

enum class ScreenDestination {
    HOME,
    EDITOR,
    AI_IMAGE,
    AI_VIDEO,
    AI_VOICE,
    AUTO_SUBTITLES,
    TEXT_TEMPLATES,
    BG_REMOVER,
    AI_SCRIPT,
    MY_PROJECTS,
    EXPORT
}

enum class AspectRatioPreset(val label: String, val ratio: Float, val iconDesc: String) {
    REELS_9_16("9:16 Reels", 9f / 16f, "TikTok / Instagram Reels"),
    YOUTUBE_16_9("16:9 Landscape", 16f / 9f, "YouTube / Widescreen"),
    SQUARE_1_1("1:1 Square", 1f / 1f, "Instagram Post"),
    ASPECT_4_3("4:3 Standard", 4f / 3f, "Classic / Tablet 4:3"),
    PORTRAIT_4_5("4:5 Feed", 4f / 5f, "Feed Portrait")
}

enum class VideoFilterType(val displayNameEn: String, val displayNameHi: String) {
    NORMAL("Normal", "सामान्य"),
    CYBERPUNK("Cyberpunk Neon", "साइबरपंक नियॉन"),
    CINEMATIC("Teal & Orange", "सिनेमैटिक"),
    RETRO_VHS("Retro VHS", "विंटेज वीएचएस"),
    DRAMATIC_BW("Dramatic B&W", "ब्लैक एंड व्हाइट"),
    SUNSET("Golden Sunset", "गोल्डन सनसेट"),
    GLITCH("RGB Glitch", "ग्लिच इफेक्ट")
}

enum class MediaType {
    VIDEO,
    IMAGE
}

enum class TextAnimType {
    NONE,
    POP,
    FADE,
    SLIDE_UP,
    TYPEWRITER,
    NEON_GLOW
}

enum class TextPosition {
    TOP,
    CENTER,
    BOTTOM
}

data class MediaClipItem(
    val id: String,
    val name: String,
    val mediaType: MediaType = MediaType.VIDEO,
    val uri: String? = null,
    val durationMs: Long = 5000L,
    val trimStartMs: Long = 0L,
    val trimEndMs: Long = 5000L,
    val filterType: VideoFilterType = VideoFilterType.NORMAL,
    val speed: Float = 1.0f,
    val sampleResId: Int? = null,
    val colorHex: Long = 0xFF7928CAL
)

data class AudioTrackItem(
    val id: String,
    val title: String,
    val artistOrVoice: String = "CreateX AI",
    val durationMs: Long = 15000L,
    val volume: Float = 1.0f,
    val isMuted: Boolean = false,
    val isGeneratedVoiceover: Boolean = false
)

data class TextOverlayItem(
    val id: String,
    val text: String,
    val startMs: Long = 0L,
    val durationMs: Long = 4000L,
    val position: TextPosition = TextPosition.BOTTOM,
    val animType: TextAnimType = TextAnimType.POP,
    val textColorHex: Long = 0xFFFFFFFFL,
    val bgColorHex: Long = 0xAA000000L,
    val fontSizeSp: Float = 22f,
    val isNeon: Boolean = true
)

typealias ProjectEntity = com.example.data.ProjectEntity

data class AiTemplateItem(
    val id: String,
    val titleEn: String,
    val titleHi: String,
    val category: String,
    val previewText: String,
    val animType: TextAnimType,
    val primaryColorHex: Long,
    val secondaryColorHex: Long
)

data class AiVoicePreset(
    val id: String,
    val nameEn: String,
    val nameHi: String,
    val langTag: String,
    val pitch: Float = 1.0f,
    val speed: Float = 1.0f,
    val tag: String
)

data class ScriptResult(
    val hook: String,
    val body: String,
    val cta: String,
    val language: String,
    val estimatedDurationSec: Int
)
