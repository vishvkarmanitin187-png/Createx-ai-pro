package com.example.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class FirestoreUserProfile(
    @DocumentId val uid: String = "",
    val email: String = "",
    val displayName: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val lastActive: Timestamp? = null
)

@IgnoreExtraProperties
data class FirestoreVideoProject(
    @DocumentId val id: String = "",
    val userId: String = "",
    val name: String = "",
    val aspectRatio: String = "REELS_9_16",
    val resolution: String = "1080p",
    val fps: Long = 30L,
    val clipsCount: Long = 1L,
    val durationSeconds: Double = 15.0,
    val thumbnailUri: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)
