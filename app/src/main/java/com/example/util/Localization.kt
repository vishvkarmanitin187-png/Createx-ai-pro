package com.example.util

import com.example.model.AppLanguage

object Localization {
    fun get(key: String, lang: AppLanguage): String {
        val entry = strings[key] ?: return key
        return if (lang == AppLanguage.HINDI) entry.second else entry.first
    }

    private val strings = mapOf(
        "app_title" to Pair("CreateX AI", "क्रिएटएक्स एआई"),
        "tagline" to Pair("Next-Gen AI Video Studio", "नेक्स्ट-जेन एआई वीडियो स्टूडियो"),
        
        // Navigation / Tabs
        "nav_home" to Pair("Home", "होम"),
        "nav_editor" to Pair("Editor", "एडिटर"),
        "nav_projects" to Pair("Projects", "प्रोजेक्ट्स"),
        "nav_export" to Pair("Export", "एक्सपोर्ट"),
        
        // Tools & Features
        "tool_video_editor" to Pair("Video Editor", "वीडियो एडिटर"),
        "tool_video_editor_desc" to Pair("Multi-track timeline, trim & split", "मल्टी-ट्रैक टाइमलाइन, ट्रिम और स्प्लिट"),
        
        "tool_ai_image" to Pair("AI Image Generator", "एआई इमेज जनरेटर"),
        "tool_ai_image_desc" to Pair("Turn text prompts into visuals", "प्रॉम्प्ट से बनाएं शानदार इमेजेस"),
        
        "tool_ai_video" to Pair("AI Video Generator", "एआई वीडियो जनरेटर"),
        "tool_ai_video_desc" to Pair("Animate images & text into clips", "तस्वीरों व टेक्स्ट से बनाएं वीडियो क्लिप्स"),
        
        "tool_ai_voice" to Pair("AI Voice (TTS)", "एआई वॉइस (टीटीएस)"),
        "tool_ai_voice_desc" to Pair("Bilingual Hindi & English voices", "हिंदी और इंग्लिश में वॉइसओवर तैयार करें"),
        
        "tool_auto_subtitles" to Pair("Auto Subtitles", "ऑटो सबटाइटल्स"),
        "tool_auto_subtitles_desc" to Pair("Viral captions & highlight sync", "वायरल कैप्शन्स और वर्ड हाइलाइट"),
        
        "tool_text_templates" to Pair("Text Templates", "टेक्स्ट टेम्पलेट्स"),
        "tool_text_templates_desc" to Pair("Trending motion graphics titles", "ट्रेंडिंग मोशन ग्राफिक्स और टाइटल्स"),
        
        "tool_bg_remover" to Pair("AI Cutout & BG Remover", "एआई बैकग्राउंड रिमूवर"),
        "tool_bg_remover_desc" to Pair("Isolate subject & swap backdrop", "सब्जेक्ट अलग करें और बैकड्रॉप बदलें"),
        
        "tool_script_writer" to Pair("AI Script Writer", "एआई स्क्रिप्ट राइटर"),
        "tool_script_writer_desc" to Pair("Viral hooks, body & CTAs for Reels", "रील्स के लिए हुक, बॉडी और सीटीए"),
        
        // Editor Controls
        "split_clip" to Pair("Split", "स्प्लिट"),
        "trim_clip" to Pair("Trim", "ट्रिम"),
        "filters" to Pair("Filters", "फिल्टर्स"),
        "add_text" to Pair("Text", "टेक्स्ट"),
        "add_audio" to Pair("Voice/Audio", "ऑडियो"),
        "speed" to Pair("Speed", "स्पीड"),
        "aspect_ratio" to Pair("Ratio", "अनुपात"),
        "delete_clip" to Pair("Delete", "हटाएं"),
        "play" to Pair("Play", "चलाएं"),
        "pause" to Pair("Pause", "रोकें"),
        "export_video" to Pair("Export Video", "वीडियो एक्सपोर्ट"),
        "import_media" to Pair("Add Media", "मीडिया जोड़ें"),
        
        // Headers & Labels
        "recent_projects" to Pair("Recent Projects", "हाल के प्रोजेक्ट्स"),
        "view_all" to Pair("View All", "सभी देखें"),
        "new_project" to Pair("New Project", "नया प्रोजेक्ट"),
        "empty_projects" to Pair("No projects yet. Create your first video masterpiece!", "अभी कोई प्रोजेक्ट नहीं है। अपना पहला वीडियो बनाएं!"),
        "prompt_placeholder" to Pair("Describe what you want to create...", "लिखें आप क्या बनाना चाहते हैं..."),
        "generate_btn" to Pair("Generate with AI", "एआई से बनाएं"),
        "applying_effect" to Pair("Processing...", "प्रोसेस हो रहा है..."),
        "save_to_project" to Pair("Add to Project Timeline", "टाइमलाइन में जोड़ें"),
        "resolution" to Pair("Resolution", "रिज़ॉल्यूशन"),
        "frame_rate" to Pair("Frame Rate", "फ्रेम रेट"),
        "export_progress" to Pair("Rendering your masterpiece...", "वीडियो रेंडर हो रहा है..."),
        "export_done" to Pair("Export Complete!", "एक्सपोर्ट पूरा हुआ!"),
        "save_video" to Pair("Save to Gallery", "गैलरी में सहेजें"),
        "share_video" to Pair("Share Video", "शेयर करें"),
        "setup_info" to Pair("Gemini Studio Connected", "जेमिनी स्टूडियो कनेक्टेड"),
        "language_toggle" to Pair("हिन्दी", "English")
    )
}
