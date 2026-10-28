package com.example.data

import android.content.Context
import android.util.Log
import com.example.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.UUID

class FirestoreProjectRepository(
    private val db: FirebaseFirestore
) {
    // Secondary constructor resolving named database ID from string resource
    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private fun requireUserId(): String =
        Firebase.auth.currentUser?.uid ?: error("User must be authenticated for Firestore operations")

    fun observeUserProjects(userId: String): Flow<List<FirestoreVideoProject>> =
        db.collection("users").document(userId).collection("projects")
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(
                    FirestoreVideoProject::class.java,
                    DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                )
            }

    suspend fun getProject(userId: String, projectId: String): FirestoreVideoProject? {
        val doc = db.collection("users").document(userId)
            .collection("projects").document(projectId)
            .get()
            .await()
        return doc.toObject(
            FirestoreVideoProject::class.java,
            DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
        )
    }

    suspend fun saveProject(userId: String, project: FirestoreVideoProject): String {
        requireUserId()
        require(project.name.isNotBlank()) { "Project name must not be blank" }
        val projectId = if (project.id.isBlank()) UUID.randomUUID().toString() else project.id
        val docRef = db.collection("users").document(userId)
            .collection("projects").document(projectId)

        val projectToSave = project.copy(
            id = projectId,
            userId = userId,
            updatedAt = null // Evaluated as server timestamp
        )

        try {
            docRef.set(projectToSave).await()
            return projectId
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save project $projectId: ${e.message}", e)
            throw e
        }
    }

    suspend fun deleteProject(userId: String, projectId: String) {
        requireUserId()
        val docRef = db.collection("users").document(userId)
            .collection("projects").document(projectId)
        try {
            docRef.delete().await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Failed to delete project $projectId: ${e.message}", e)
            throw e
        }
    }

    suspend fun saveUserProfile(profile: FirestoreUserProfile) {
        requireUserId()
        require(profile.uid.isNotBlank()) { "User profile UID must not be blank" }
        val docRef = db.collection("users").document(profile.uid)
        try {
            docRef.set(profile).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save user profile ${profile.uid}: ${e.message}", e)
            throw e
        }
    }

    private companion object {
        const val TAG = "FirestoreProjectRepo"
    }
}
