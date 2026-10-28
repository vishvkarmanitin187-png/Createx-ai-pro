package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.model.AiTemplateItem
import com.example.model.AiVoicePreset
import com.example.model.AppLanguage
import com.example.model.AspectRatioPreset
import com.example.model.AudioTrackItem
import com.example.model.MediaClipItem
import com.example.model.MediaType
import com.example.data.ProjectEntity
import com.example.model.ScreenDestination
import com.example.model.ScriptResult
import com.example.model.TextAnimType
import com.example.model.TextOverlayItem
import com.example.model.TextPosition
import com.example.model.VideoFilterType
import com.example.service.GeminiAiService
import com.example.service.TtsManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class CreateXViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val projectDao = db.projectDao()
    val ttsManager = TtsManager(application)
    val aiService = GeminiAiService()

    // Screen Navigation
    private val _currentScreen = MutableStateFlow(ScreenDestination.HOME)
    val currentScreen: StateFlow<ScreenDestination> = _currentScreen.asStateFlow()

    private val screenStack = mutableListOf(ScreenDestination.HOME)

    // Language
    private val _language = MutableStateFlow(AppLanguage.ENGLISH)
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    // Room Projects
    val allProjects: StateFlow<List<ProjectEntity>> = projectDao.getAllProjects()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Active Project & Editor State
    private val _activeProject = MutableStateFlow(
        ProjectEntity(
            id = "proj_default_1",
            title = "Viral Cyberpunk Reel",
            aspectRatioName = AspectRatioPreset.REELS_9_16.name,
            durationMs = 15000L,
            clipCount = 3,
            hasVoiceover = true,
            filterApplied = VideoFilterType.CYBERPUNK.name
        )
    )
    val activeProject: StateFlow<ProjectEntity> = _activeProject.asStateFlow()

    private val _aspectRatio = MutableStateFlow(AspectRatioPreset.REELS_9_16)
    val aspectRatio: StateFlow<AspectRatioPreset> = _aspectRatio.asStateFlow()

    private val _activeFilter = MutableStateFlow(VideoFilterType.CYBERPUNK)
    val activeFilter: StateFlow<VideoFilterType> = _activeFilter.asStateFlow()

    // Timeline Media Clips
    private val _clips = MutableStateFlow<List<MediaClipItem>>(
        listOf(
            MediaClipItem(
                id = "clip_1",
                name = "Intro Neon Scene",
                mediaType = MediaType.VIDEO,
                durationMs = 5000L,
                trimStartMs = 0L,
                trimEndMs = 5000L,
                colorHex = 0xFF7928CA
            ),
            MediaClipItem(
                id = "clip_2",
                name = "AI Cyber Core",
                mediaType = MediaType.VIDEO,
                durationMs = 6000L,
                trimStartMs = 0L,
                trimEndMs = 6000L,
                colorHex = 0xFF0070F3
            ),
            MediaClipItem(
                id = "clip_3",
                name = "Outro Spark",
                mediaType = MediaType.IMAGE,
                durationMs = 4000L,
                trimStartMs = 0L,
                trimEndMs = 4000L,
                colorHex = 0xFFFF007F
            )
        )
    )
    val clips: StateFlow<List<MediaClipItem>> = _clips.asStateFlow()

    private val _selectedClipId = MutableStateFlow<String?>("clip_1")
    val selectedClipId: StateFlow<String?> = _selectedClipId.asStateFlow()

    // Text Overlays Track
    private val _textOverlays = MutableStateFlow<List<TextOverlayItem>>(
        listOf(
            TextOverlayItem(
                id = "text_1",
                text = "✨ CREATEX AI STUDIO",
                startMs = 500L,
                durationMs = 4500L,
                position = TextPosition.TOP,
                animType = TextAnimType.NEON_GLOW,
                textColorHex = 0xFF00F5D4,
                bgColorHex = 0xCC120E2E
            ),
            TextOverlayItem(
                id = "text_2",
                text = "Next-Gen AI Video Editor 🚀",
                startMs = 5000L,
                durationMs = 5000L,
                position = TextPosition.BOTTOM,
                animType = TextAnimType.POP,
                textColorHex = 0xFFFFB703,
                bgColorHex = 0xDD000000
            )
        )
    )
    val textOverlays: StateFlow<List<TextOverlayItem>> = _textOverlays.asStateFlow()

    // Audio Track
    private val _audioTrack = MutableStateFlow<AudioTrackItem?>(
        AudioTrackItem(
            id = "audio_default",
            title = "Aarav Energetic Hindi Narration",
            artistOrVoice = "AI Voice Engine",
            durationMs = 15000L,
            volume = 0.9f,
            isGeneratedVoiceover = true
        )
    )
    val audioTrack: StateFlow<AudioTrackItem?> = _audioTrack.asStateFlow()

    // Player Playhead & Playback
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentPlayheadMs = MutableStateFlow(0L)
    val currentPlayheadMs: StateFlow<Long> = _currentPlayheadMs.asStateFlow()

    private var playbackJob: Job? = null

    // AI Generation States
    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    private val _aiStatusMessage = MutableStateFlow<String?>(null)
    val aiStatusMessage: StateFlow<String?> = _aiStatusMessage.asStateFlow()

    private val _generatedScript = MutableStateFlow<ScriptResult?>(null)
    val generatedScript: StateFlow<ScriptResult?> = _generatedScript.asStateFlow()

    private val _generatedImagePrompt = MutableStateFlow("")
    val generatedImagePrompt: StateFlow<String> = _generatedImagePrompt.asStateFlow()

    private val _generatedImagesList = MutableStateFlow<List<String>>(
        listOf(
            "cyberpunk_neon_city",
            "anime_aesthetic_sunset",
            "futuristic_hologram_sphere",
            "hyper_detailed_portrait"
        )
    )
    val generatedImagesList: StateFlow<List<String>> = _generatedImagesList.asStateFlow()

    // Export State
    private val _exportResolution = MutableStateFlow("1080p Full HD")
    val exportResolution: StateFlow<String> = _exportResolution.asStateFlow()

    private val _exportFps = MutableStateFlow(60)
    val exportFps: StateFlow<Int> = _exportFps.asStateFlow()

    private val _exportProgress = MutableStateFlow(0f)
    val exportProgress: StateFlow<Float> = _exportProgress.asStateFlow()

    private val _isExporting = MutableStateFlow(false)
    val isExporting: StateFlow<Boolean> = _isExporting.asStateFlow()

    private val _isExportDone = MutableStateFlow(false)
    val isExportDone: StateFlow<Boolean> = _isExportDone.asStateFlow()

    init {
        // Seed initial project if database is empty
        viewModelScope.launch {
            projectDao.insertProject(_activeProject.value)
            projectDao.insertProject(
                ProjectEntity(
                    id = "proj_hindi_shorts",
                    title = "वायरल हिंदी रील्स गाइड",
                    aspectRatioName = AspectRatioPreset.REELS_9_16.name,
                    durationMs = 22000L,
                    clipCount = 4,
                    hasVoiceover = true,
                    filterApplied = VideoFilterType.SUNSET.name
                )
            )
            projectDao.insertProject(
                ProjectEntity(
                    id = "proj_tech_youtube",
                    title = "Tech AI Trends 2026",
                    aspectRatioName = AspectRatioPreset.YOUTUBE_16_9.name,
                    durationMs = 45000L,
                    clipCount = 6,
                    hasVoiceover = false,
                    filterApplied = VideoFilterType.CINEMATIC.name
                )
            )
        }
    }

    // Navigation methods
    fun navigateTo(destination: ScreenDestination) {
        if (_currentScreen.value != destination) {
            screenStack.add(destination)
            _currentScreen.value = destination
        }
    }

    fun navigateBack(): Boolean {
        if (screenStack.size > 1) {
            screenStack.removeAt(screenStack.lastIndex)
            _currentScreen.value = screenStack.last()
            return true
        }
        return false
    }

    fun toggleLanguage() {
        _language.value = if (_language.value == AppLanguage.ENGLISH) AppLanguage.HINDI else AppLanguage.ENGLISH
    }

    // Player controls
    fun togglePlayPause() {
        if (_isPlaying.value) {
            pausePlayer()
        } else {
            startPlayer()
        }
    }

    fun pausePlayer() {
        _isPlaying.value = false
        playbackJob?.cancel()
    }

    fun startPlayer() {
        _isPlaying.value = true
        playbackJob?.cancel()
        playbackJob = viewModelScope.launch {
            val total = getTotalDurationMs()
            while (_isPlaying.value) {
                delay(50)
                val next = _currentPlayheadMs.value + 50
                if (next >= total) {
                    _currentPlayheadMs.value = 0L
                } else {
                    _currentPlayheadMs.value = next
                }
            }
        }
    }

    fun seekTo(timeMs: Long) {
        val clamped = timeMs.coerceIn(0L, getTotalDurationMs().coerceAtLeast(1000L))
        _currentPlayheadMs.value = clamped
    }

    fun stepForward() {
        seekTo(_currentPlayheadMs.value + 1000L)
    }

    fun stepBackward() {
        seekTo(_currentPlayheadMs.value - 1000L)
    }

    fun getTotalDurationMs(): Long {
        val totalClipDuration = _clips.value.sumOf { it.trimEndMs - it.trimStartMs }
        return if (totalClipDuration > 0) totalClipDuration else 15000L
    }

    // Clip operations
    fun selectClip(id: String) {
        _selectedClipId.value = id
    }

    fun splitClipAtPlayhead() {
        val playhead = _currentPlayheadMs.value
        var accumulated = 0L
        val currentList = _clips.value.toMutableList()

        for (i in currentList.indices) {
            val clip = currentList[i]
            val clipLen = clip.trimEndMs - clip.trimStartMs
            if (playhead > accumulated && playhead < accumulated + clipLen) {
                // Split here!
                val splitOffset = playhead - accumulated
                val clip1 = clip.copy(
                    id = UUID.randomUUID().toString(),
                    name = "${clip.name} (Part 1)",
                    trimEndMs = clip.trimStartMs + splitOffset
                )
                val clip2 = clip.copy(
                    id = UUID.randomUUID().toString(),
                    name = "${clip.name} (Part 2)",
                    trimStartMs = clip.trimStartMs + splitOffset
                )
                currentList.removeAt(i)
                currentList.add(i, clip2)
                currentList.add(i, clip1)
                _clips.value = currentList
                _selectedClipId.value = clip2.id
                break
            }
            accumulated += clipLen
        }
    }

    fun trimSelectedClip(startMs: Long, endMs: Long) {
        val id = _selectedClipId.value ?: return
        _clips.value = _clips.value.map {
            if (it.id == id) it.copy(trimStartMs = startMs, trimEndMs = endMs.coerceAtLeast(startMs + 500L)) else it
        }
    }

    fun deleteSelectedClip() {
        val id = _selectedClipId.value ?: return
        if (_clips.value.size <= 1) return // Keep at least one clip
        _clips.value = _clips.value.filter { it.id != id }
        _selectedClipId.value = _clips.value.firstOrNull()?.id
    }

    fun moveClipLeft(clipId: String) {
        val list = _clips.value.toMutableList()
        val idx = list.indexOfFirst { it.id == clipId }
        if (idx > 0) {
            val item = list.removeAt(idx)
            list.add(idx - 1, item)
            _clips.value = list
        }
    }

    fun moveClipRight(clipId: String) {
        val list = _clips.value.toMutableList()
        val idx = list.indexOfFirst { it.id == clipId }
        if (idx >= 0 && idx < list.size - 1) {
            val item = list.removeAt(idx)
            list.add(idx + 1, item)
            _clips.value = list
        }
    }

    fun exportSrt(): String {
        val sb = StringBuilder()
        _textOverlays.value.forEachIndexed { index, overlay ->
            val startSec = overlay.startMs / 1000
            val startMsPart = overlay.startMs % 1000
            val endMs = overlay.startMs + overlay.durationMs
            val endSec = endMs / 1000
            val endMsPart = endMs % 1000
            val startTime = String.format("%02d:%02d:%02d,%03d", startSec / 3600, (startSec % 3600) / 60, startSec % 60, startMsPart)
            val endTime = String.format("%02d:%02d:%02d,%03d", endSec / 3600, (endSec % 3600) / 60, endSec % 60, endMsPart)
            sb.append("${index + 1}\n$startTime --> $endTime\n${overlay.text}\n\n")
        }
        return sb.toString()
    }

    fun setClipSpeed(speed: Float) {
        val id = _selectedClipId.value ?: return
        _clips.value = _clips.value.map {
            if (it.id == id) it.copy(speed = speed) else it
        }
    }

    fun addMediaClip(uri: String?, name: String, type: MediaType, durationMs: Long = 5000L) {
        val newClip = MediaClipItem(
            id = UUID.randomUUID().toString(),
            name = name,
            mediaType = type,
            uri = uri,
            durationMs = durationMs,
            trimStartMs = 0L,
            trimEndMs = durationMs,
            colorHex = if (type == MediaType.VIDEO) 0xFF4338CA else 0xFFFF007F
        )
        _clips.value = _clips.value + newClip
        _selectedClipId.value = newClip.id
    }

    fun applyFilter(filter: VideoFilterType) {
        _activeFilter.value = filter
        val selId = _selectedClipId.value
        _clips.value = _clips.value.map {
            if (it.id == selId) it.copy(filterType = filter) else it
        }
    }

    fun setAspectRatio(preset: AspectRatioPreset) {
        _aspectRatio.value = preset
        _activeProject.value = _activeProject.value.copy(aspectRatioName = preset.name)
    }

    // Text Overlays
    fun addTextOverlay(
        text: String,
        animType: TextAnimType = TextAnimType.POP,
        position: TextPosition = TextPosition.BOTTOM,
        colorHex: Long = 0xFFFFFFFF,
        bgHex: Long = 0xCC130E29
    ) {
        val newOverlay = TextOverlayItem(
            id = UUID.randomUUID().toString(),
            text = text,
            startMs = _currentPlayheadMs.value,
            durationMs = 4000L,
            position = position,
            animType = animType,
            textColorHex = colorHex,
            bgColorHex = bgHex
        )
        _textOverlays.value = _textOverlays.value + newOverlay
    }

    fun removeTextOverlay(id: String) {
        _textOverlays.value = _textOverlays.value.filter { it.id != id }
    }

    // AI Voice & Audio
    fun playTts(text: String, isHindi: Boolean, pitch: Float = 1.0f, speed: Float = 1.0f) {
        ttsManager.speak(text, isHindi, pitch, speed)
    }

    fun stopTts() {
        ttsManager.stop()
    }

    fun attachVoiceoverToTimeline(voiceTitle: String, durationMs: Long = 15000L) {
        _audioTrack.value = AudioTrackItem(
            id = UUID.randomUUID().toString(),
            title = voiceTitle,
            artistOrVoice = "CreateX AI Voice",
            durationMs = durationMs,
            isGeneratedVoiceover = true
        )
        _activeProject.value = _activeProject.value.copy(hasVoiceover = true)
    }

    // AI Script Generator
    fun generateScript(topic: String, platform: String, language: String, tone: String) {
        viewModelScope.launch {
            _isGenerating.value = true
            val res = aiService.generateScript(topic, platform, language, tone)
            _isGenerating.value = false
            res.onSuccess {
                _generatedScript.value = it
            }.onFailure {
                _aiStatusMessage.value = "AI generation note: ${it.localizedMessage}"
            }
        }
    }

    // AI Image Generation
    fun generateAiImage(prompt: String, style: String, ratio: String) {
        viewModelScope.launch {
            _isGenerating.value = true
            _generatedImagePrompt.value = prompt
            delay(1200) // realistic synthesis
            _isGenerating.value = false
            _generatedImagesList.value = listOf(
                "gen_${System.currentTimeMillis()}"
            ) + _generatedImagesList.value
        }
    }

    // Subtitles
    fun generateAutoSubtitles(text: String, isHindi: Boolean) {
        viewModelScope.launch {
            _isGenerating.value = true
            val chunks = aiService.generateAutoSubtitles(text, isHindi)
            _isGenerating.value = false
            val newOverlays = chunks.map { (timeStart, chunkText) ->
                TextOverlayItem(
                    id = UUID.randomUUID().toString(),
                    text = chunkText,
                    startMs = timeStart,
                    durationMs = 1800L,
                    position = TextPosition.BOTTOM,
                    animType = TextAnimType.POP,
                    textColorHex = 0xFFFFEA00,
                    bgColorHex = 0xEE090714
                )
            }
            _textOverlays.value = _textOverlays.value + newOverlays
        }
    }

    // Export simulation
    fun setExportSettings(resolution: String, fps: Int) {
        _exportResolution.value = resolution
        _exportFps.value = fps
    }

    fun startExport() {
        if (_isExporting.value) return
        _isExporting.value = true
        _isExportDone.value = false
        _exportProgress.value = 0f

        viewModelScope.launch {
            for (step in 1..100) {
                delay(35)
                _exportProgress.value = step / 100f
            }
            _isExporting.value = false
            _isExportDone.value = true
            saveCurrentProject()
        }
    }

    // Project Persistence
    fun saveCurrentProject() {
        viewModelScope.launch {
            val project = _activeProject.value.copy(
                clipCount = _clips.value.size,
                durationMs = getTotalDurationMs(),
                updatedAt = System.currentTimeMillis()
            )
            _activeProject.value = project
            projectDao.insertProject(project)
        }
    }

    fun loadProject(project: ProjectEntity) {
        _activeProject.value = project
        _aspectRatio.value = AspectRatioPreset.entries.find { it.name == project.aspectRatioName }
            ?: AspectRatioPreset.REELS_9_16
        _activeFilter.value = VideoFilterType.entries.find { it.name == project.filterApplied }
            ?: VideoFilterType.NORMAL
        navigateTo(ScreenDestination.EDITOR)
    }

    fun createNewProject(title: String, ratio: AspectRatioPreset) {
        val newProj = ProjectEntity(
            id = "proj_${System.currentTimeMillis()}",
            title = title,
            aspectRatioName = ratio.name,
            durationMs = 15000L,
            clipCount = 2,
            hasVoiceover = false,
            filterApplied = VideoFilterType.NORMAL.name
        )
        viewModelScope.launch {
            projectDao.insertProject(newProj)
            loadProject(newProj)
        }
    }

    fun deleteProject(id: String) {
        viewModelScope.launch {
            projectDao.deleteProjectById(id)
        }
    }

    override fun onCleared() {
        super.onCleared()
        playbackJob?.cancel()
        ttsManager.shutdown()
    }
}
