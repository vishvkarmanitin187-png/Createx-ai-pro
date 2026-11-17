package com.creatorverse.ai

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Ink = Color(0xFF101020)
private val Panel = Color(0xFF1D1B35)
private val Purple = Color(0xFF9B7BFF)
private val Cyan = Color(0xFF53D9E8)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { CreatorVerseApp() }
    }
}

@Composable
fun CreatorVerseApp() {
    var page by remember { mutableStateOf("Home") }
    var selectedVideo by remember { mutableStateOf<Uri?>(null) }
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Purple,
            secondary = Cyan,
            background = Ink,
            surface = Panel
        )
    ) {
        Scaffold(
            containerColor = Ink,
            bottomBar = {
                NavigationBar(containerColor = Panel, contentColor = Color.White) {
                    listOf(
                        Triple("Home", Icons.Default.Home, "Home"),
                        Triple("Create", Icons.Default.AutoAwesome, "Create"),
                        Triple("Studio", Icons.Default.VideoLibrary, "Studio"),
                        Triple("Profile", Icons.Default.Person, "Profile")
                    ).forEach { (label, icon, route) ->
                        NavigationBarItem(
                            selected = page == route,
                            onClick = { page = route },
                            icon = { Icon(icon, contentDescription = label) },
                            label = { Text(label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Purple,
                                selectedTextColor = Color.White,
                                indicatorColor = Color(0xFF393052),
                                unselectedIconColor = Color.LightGray,
                                unselectedTextColor = Color.LightGray
                            )
                        )
                    }
                }
            }
        ) { padding ->
            when (page) {
                "Create" -> CreateScreen(Modifier.padding(padding))
                "Studio" -> StudioScreen(selectedVideo, { selectedVideo = it }, Modifier.padding(padding))
                "Profile" -> ProfileScreen(Modifier.padding(padding))
                else -> HomeScreen(
                    onCreate = { page = "Create" },
                    onStudio = { page = "Studio" },
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}

@Composable
fun HomeScreen(onCreate: () -> Unit, onStudio: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("CREATORVERSE", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Black)
                Text("AI CREATOR STUDIO", color = Purple, fontSize = 11.sp, letterSpacing = 2.sp)
            }
            Surface(color = Color(0xFF30264D), shape = RoundedCornerShape(50)) {
                Text("FREE PLAN", color = Cyan, modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), fontSize = 11.sp)
            }
        }
        Box(
            Modifier.fillMaxWidth().background(
                Brush.linearGradient(listOf(Color(0xFF45316F), Color(0xFF1C5263))),
                RoundedCornerShape(24.dp)
            ).padding(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Hello, Creator! ✨", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("Aaj kya create karna hai?", color = Color.White.copy(alpha = .85f))
                Button(onClick = onCreate, colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Ink)) {
                    Icon(Icons.Default.AutoAwesome, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Open Creator AI")
                }
            }
        }
        Text("Your creator tools", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        val tools = listOf(
            Triple("AI Script & Caption", "Ideas, hooks, captions", Icons.Default.EditNote),
            Triple("Video Studio", "Choose a video to preview", Icons.Default.VideoLibrary),
            Triple("Hashtag Ideas", "Platform-ready tags", Icons.Default.Tag),
            Triple("Content Planner", "Plan your next post", Icons.Default.CalendarMonth)
        )
        tools.chunked(2).forEach { rowTools ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                rowTools.forEach { item ->
                    Card(
                        modifier = Modifier.weight(1f).clickable { if (item.first == "Video Studio") onStudio() else onCreate() },
                        colors = CardDefaults.cardColors(containerColor = Panel),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(item.third, null, tint = Purple, modifier = Modifier.size(28.dp))
                            Text(item.first, color = Color.White, fontWeight = FontWeight.Bold)
                            Text(item.second, color = Color.LightGray, fontSize = 12.sp)
                        }
                    }
                }
                if (rowTools.size == 1) Spacer(Modifier.weight(1f))
            }
        }
        Card(colors = CardDefaults.cardColors(containerColor = Panel)) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Coming next", color = Cyan, fontWeight = FontWeight.Bold)
                Text("Auto Video Director • Thumbnail AI • Multi-platform export • Creator Analytics", color = Color.LightGray)
            }
        }
    }
}

@Composable
fun CreateScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var topic by remember { mutableStateOf("") }
    var platform by remember { mutableStateOf("YouTube") }
    var language by remember { mutableStateOf("Hindi") }
    var kind by remember { mutableStateOf("Video Script") }
    var result by remember { mutableStateOf("") }

    Column(modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("Creator AI", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text("Apne topic se script, caption aur hashtags ka draft banayein.", color = Color.LightGray)
        OutlinedTextField(value = topic, onValueChange = { topic = it }, label = { Text("Video ka topic") }, placeholder = { Text("Example: 5 mobile photography tips") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
        Text("Platform", color = Color.White, fontWeight = FontWeight.SemiBold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("YouTube", "Instagram", "Facebook").forEach {
                FilterChip(selected = platform == it, onClick = { platform = it }, label = { Text(it) })
            }
        }
        Text("Language", color = Color.White, fontWeight = FontWeight.SemiBold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Hindi", "English", "Hinglish").forEach {
                FilterChip(selected = language == it, onClick = { language = it }, label = { Text(it) })
            }
        }
        Text("What do you need?", color = Color.White, fontWeight = FontWeight.SemiBold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Video Script", "Caption", "Hashtags").forEach {
                FilterChip(selected = kind == it, onClick = { kind = it }, label = { Text(it) })
            }
        }
        Button(
            onClick = { result = generateDraft(topic, platform, language, kind) },
            modifier = Modifier.fillMaxWidth(),
            enabled = topic.isNotBlank()
        ) {
            Icon(Icons.Default.AutoAwesome, null)
            Spacer(Modifier.width(8.dp))
            Text("Generate draft")
        }
        if (result.isNotBlank()) {
            Card(colors = CardDefaults.cardColors(containerColor = Panel)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Your draft", color = Cyan, fontWeight = FontWeight.Bold)
                    Text(result, color = Color.White)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = {
                            val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                            clipboard.setPrimaryClip(android.content.ClipData.newPlainText("CreatorVerse draft", result))
                        }) { Icon(Icons.Default.ContentCopy, null); Text("Copy") }
                        OutlinedButton(onClick = {
                            val send = Intent(Intent.ACTION_SEND).apply { type = "text/plain"; putExtra(Intent.EXTRA_TEXT, result) }
                            context.startActivity(Intent.createChooser(send, "Share draft"))
                        }) { Icon(Icons.Default.Share, null); Text("Share") }
                    }
                }
            }
        }
        Text("Note: Is first version mein draft generator on-device templates par chalta hai. Cloud AI API abhi connect nahi hai.", color = Color.Gray, fontSize = 12.sp)
    }
}

fun generateDraft(topic: String, platform: String, language: String, kind: String): String {
    val t = topic.trim()
    return when (kind) {
        "Video Script" -> when (language) {
            "English" -> "HOOK: Want to learn about $t?\\n\\nINTRO: In this video, we break it down simply.\\n\\nMAIN POINTS:\\n1. Explain the key idea.\\n2. Show a practical example.\\n3. Share one useful tip.\\n\\nCTA: Follow for more creator tips."
            "Hinglish" -> "HOOK: Kya aap $t ke baare mein ye jaante ho?\\n\\nINTRO: Aaj is topic ko simple way mein samjhenge.\\n\\nPOINTS:\\n1. Sabse important baat.\\n2. Ek practical example.\\n3. Ek pro tip.\\n\\nCTA: Aise aur content ke liye follow karo."
            else -> "HOOK: क्या आप $t के बारे में यह जानते हैं?\\n\\nINTRO: आज इस विषय को आसान भाषा में समझेंगे।\\n\\nमुख्य बातें:\\n1. सबसे जरूरी जानकारी।\\n2. एक आसान उदाहरण।\\n3. एक काम की टिप।\\n\\nCTA: ऐसे और वीडियो के लिए फॉलो करें।"
        }
        "Caption" -> "✨ $t\\n\\nAapki राय क्या है? Comment mein batayein!\\n\\nFollow for more.\\nPlatform: $platform"
        else -> "#${t.replace(Regex("[^A-Za-z0-9]"), "")} #Creator #ContentCreator #${platform.replace(" ", "")} #Reels #Shorts #CreatorVerse"
    }
}

@Composable
fun StudioScreen(video: Uri?, onVideo: (Uri?) -> Unit, modifier: Modifier = Modifier) {
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { onVideo(it) }
    Column(modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("Video Studio", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text("Choose a video from your phone to begin.", color = Color.LightGray)
        Button(onClick = { picker.launch("video/*") }, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.VideoLibrary, null)
            Spacer(Modifier.width(8.dp))
            Text("Select video")
        }
        if (video != null) {
            Card(colors = CardDefaults.cardColors(containerColor = Panel)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.CheckCircle, null, tint = Cyan)
                    Text("Video selected successfully", color = Color.White, fontWeight = FontWeight.Bold)
                    Text(video.lastPathSegment ?: video.toString(), color = Color.LightGray, fontSize = 12.sp)
                    Text("Next development step: add timeline trimming, filters, transitions and export engine.", color = Color.LightGray)
                    OutlinedButton(onClick = { onVideo(null) }) { Text("Remove selection") }
                }
            }
        }
        Card(colors = CardDefaults.cardColors(containerColor = Panel)) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Editing tools roadmap", color = Cyan, fontWeight = FontWeight.Bold)
                Text("Trim & split • Speed control • Filters • Text overlays • Music • Transitions • 1080p export", color = Color.White)
            }
        }
        Text("This starter build selects media but does not yet modify or render the video.", color = Color.Gray, fontSize = 12.sp)
    }
}

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("Creator Profile", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Card(colors = CardDefaults.cardColors(containerColor = Panel)) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.AccountCircle, tint = Purple, contentDescription = null, modifier = Modifier.size(48.dp))
                Text("Welcome to CREATORVERSE AI", color = Color.White, fontWeight = FontWeight.Bold)
                Text("Free plan • Local demo profile", color = Color.LightGray)
            }
        }
        Text("Premium plans, cloud sync, secure sign-in and connected social accounts will be added after backend setup.", color = Color.LightGray)
    }
}
